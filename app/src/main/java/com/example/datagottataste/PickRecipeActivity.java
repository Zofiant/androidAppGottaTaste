package com.example.datagottataste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.datagottataste.databinding.ActivityPickRecipeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class PickRecipeActivity extends AppCompatActivity implements RecycleViewInterface {
    ActivityPickRecipeBinding binding;
    private RecipeListAdapter recipeAdapter;

    private List<RecipeBd> recipeList;
    FirebaseAuth auth;
    FirebaseUser user;

    DatabaseReference mDataBase;
    DatabaseReference mDataBase2;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    DatabaseReference userRef;
    User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(Const.DB_URL);
        mDataBase = database
                .getReference(Const.KEY_RECIPE);
        mDataBase2 = database
                .getReference(Const.KEY_DATE_RECIPE);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.empty_tall_divider));
        binding.recyclerView.addItemDecoration(itemDecorator);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        user = auth.getCurrentUser();
        userRef = database.getReference(Const.KEY_USER).child(user.getUid());
        user = auth.getCurrentUser();
        userInfo = getIntent().getParcelableExtra("USER_INFO");
        binding.fbtnAddNewRecipe.setVisibility(View.GONE);
        if (userInfo.isCreator())
        {
            binding.fbtnAddNewRecipe.setVisibility(View.VISIBLE);
        }
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Метод вызывается при каждом изменении данных в указанном пути
                recipeList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RecipeBd item = snapshot.getValue(RecipeBd.class);
                    recipeList.add(item);
                }
                // Обработка или отображение полученного списка
                updateUI(recipeList);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Обработка ошибки
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }

    private void updateUI(List<RecipeBd> list) {
        recipeAdapter = new RecipeListAdapter(this, recipeList, this);
        binding.recyclerView.setAdapter(recipeAdapter);
    }
    public void goActivityNewRec(View view){
        Intent goToAddNewRecipe = new Intent(PickRecipeActivity.this,MainActivity.class);
        startActivity(goToAddNewRecipe);


    }
    @Override
    public void onItemClick(RecipeBd recipe) {
        Intent intentToAddNewDateRecipe = getIntent();
        String currentDate = intentToAddNewDateRecipe.getStringExtra("CURRENT_DATE");
        if (currentDate !=null){
            currentDate.trim();
        }
        String id = mDataBase2.push().getKey();
        String recipeId = recipe.getId();
        String cal = recipe.getCal();
        String name = recipe.getName();
        String imageId = recipe.getImageUrl();
        String userId = user.getUid();
        RecipeDate newDateRecipe = new RecipeDate(id,recipeId,name,cal,imageId,userId,currentDate);
        if(id != null)
        {
            mDataBase2.child(id).setValue(newDateRecipe);
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickDelete(int position,RecipeBd recipe) {
        mDataBase.child(recipe.getId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Успешно удалено из Firebase
                Toast.makeText(this, "Элемент удален успешно!", Toast.LENGTH_SHORT).show();

                // Удаление элемента из списка и уведомление адаптера об изменении
                recipeList.remove(position);
                recipeAdapter.notifyItemRemoved(position);
            } else {
                Toast.makeText(this, "Ошибка при удалении!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


