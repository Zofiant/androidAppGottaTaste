package com.example.datagottataste;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.datagottataste.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Button addRec,addPhoto;
    private EditText edName,edCal;
    private DatabaseReference mDataBase;

    private ImageView imgRec;
    private StorageReference mStorageRef;
    private Uri uploadUri;
    FirebaseAuth auth;
    User userInfo;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();

    }

    public void init(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Const.DB_URL);

        user = auth.getCurrentUser();

        userRef = database.getReference(Const.KEY_USER).child(user.getUid());
        addRec = binding.buttonAddRec;
        edName = binding.editTextName;
        edCal = binding.editTextCal;
        mDataBase = FirebaseDatabase.getInstance().getReference(Const.KEY_RECIPE);
        imgRec = binding.imageRec;
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageDB");
    }

    public void saveRecipe(String name,String cal){
        String id = mDataBase.push().getKey();
        RecipeBd newRecipe = new RecipeBd(id,name,cal,uploadUri.toString());


        if(id != null)
        {
            mDataBase.child(id).setValue(newRecipe);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            Intent savedRecipe = new Intent(MainActivity.this, PickRecipeActivity.class);
            startActivity(savedRecipe);

        }
        else {
            Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
        }


    }
    public void onClickSave(View view){

        String name = edName.getText().toString();
        String cal = edCal.getText().toString();
        if (!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(cal)){
            uploadImage(name,cal);
        }
        else {
            Toast.makeText(this, "Пустое поле", Toast.LENGTH_SHORT).show();
        }

    }

    public void onClickChooseImage(View view){
        getImage();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data!=null && data.getData() != null) {
            if(resultCode == RESULT_OK) {
                Log.d("MyLog","Image URI:" + data.getData());
                imgRec.setImageURI(data.getData());

            }
        }
    }

    private void getImage(){
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser,1);

    }
    //Загрузка на бд Firebase
    private void uploadImage(String name, String cal){
        Bitmap bitMap = ((BitmapDrawable) imgRec.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] byteArray = baos.toByteArray();
        final StorageReference mRef = mStorageRef.child(System.currentTimeMillis()+"Recipe");//Названия зависят от милисекунд
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                saveRecipe(name,cal);

            }
        });
    }
}