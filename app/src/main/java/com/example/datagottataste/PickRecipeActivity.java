package com.example.datagottataste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.datagottataste.databinding.ActivityPickRecipeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class PickRecipeActivity extends AppCompatActivity {
    ActivityPickRecipeBinding binding;
    private RecipeListAdapter recipeAdapter;
    private User user;
    private List<RecipeItem> recipeList;

    DatabaseReference mDataBase;
    StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDataBase = FirebaseDatabase
                .getInstance(Const.DB_URL)
                .getReference(Const.KEY_EVENTS);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.empty_tall_divider));
        binding.recyclerView.addItemDecoration(itemDecorator);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeList = new ArrayList<>();
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Метод вызывается при каждом изменении данных в указанном пути
                recipeList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RecipeItem item = snapshot.getValue(RecipeItem.class);
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

    private void updateUI(List<RecipeItem> list) {
        recipeAdapter = new RecipeListAdapter(this, recipeList, event -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            bundle.putParcelable("event", event);

        });
        binding.recyclerView.setAdapter(recipeAdapter);
    }

    public void goActivityNewRec(){
        Intent start = new Intent(PickRecipeActivity.this, HomeActivity.class);
        startActivity(start);

    }

}


