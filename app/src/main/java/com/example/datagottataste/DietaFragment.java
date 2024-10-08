package com.example.datagottataste;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datagottataste.databinding.FragmentDietaBinding;
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
public class DietaFragment extends Fragment implements RecycleViewDateInterface{

    private List<RecipeDate> dateRecipeList;
    private RecipeDateListAdapter adapter;
    private FragmentDietaBinding binding;
    private SharedPreferences sharedPreferences;
    //private ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseReference mDataBase;
    StorageReference mStorageReference;
    FirebaseAuth auth;
    FirebaseUser user;
    private String currentDate;
    User userver;
    Integer totalCalories = 0;
    private ProgressBar progressBar;
    private RecipeDateListAdapter recipeAdapter;
    TextView showCal;

    public static DietaFragment newInstance(String currentDate, User userver) { // Изменено имя параметра
        DietaFragment fragment = new DietaFragment();
        Bundle args = new Bundle();
        args.putString("currentDate", currentDate);
        args.putParcelable("userInfo",userver);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDate = getArguments().getString("currentDate");
        userver = getArguments().getParcelable("userInfo");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDietaBinding.inflate(inflater, container, false);
        //getDataFromFirebase();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mDataBase = FirebaseDatabase.getInstance(Const.DB_URL).getReference(Const.KEY_DATE_RECIPE);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.empty_tall_divider));
        binding.recyclerView.addItemDecoration(itemDecorator);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dateRecipeList = new ArrayList<>();
        progressBar = binding.simpleProgressBar;
        showCal= binding.chetCal;

        binding.btnAddRecipe.setOnClickListener(v -> {
            Intent intentToAddNewDateRecipe = new Intent(getContext(), PickRecipeActivity.class);
            intentToAddNewDateRecipe.putExtra("CURRENT_DATE", currentDate);
            intentToAddNewDateRecipe.putExtra("USER_INFO",userver);
            startActivity(intentToAddNewDateRecipe);


        });
        getDataFromFirebase();
    }
    private void getDataFromFirebase() {
        // Добавляем слушатель для изменений данных
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Метод вызывается при каждом изменении данных в указанном пути
                dateRecipeList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RecipeDate item = snapshot.getValue(RecipeDate.class);


                    if(item.getUserId().equals(user.getUid())){
                        if(item.getDateOfDiet().equals(currentDate)){
                            dateRecipeList.add(item);
                        }
                    }

                }
                // Обработка или отображение полученного списка
                updateUI(dateRecipeList);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Обработка ошибки
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
    public void updateUI(List<RecipeDate> dateRecipeList) {
        recipeAdapter = new RecipeDateListAdapter(getContext(), dateRecipeList, this);
        binding.recyclerView.setAdapter(recipeAdapter);
        totalCalories = 0;
        calculateTotalCalories();
        progressBar.setProgress(totalCalories);
        showCal.setText(String.valueOf(totalCalories));
        Integer userMaxCalories = Integer.valueOf(userver.getDescription());
        progressBar.setMax(userMaxCalories);
    }
    @Override
    public void onItemClick(RecipeDate recipe) {
    }
    @Override
    public void onClickDelete(int position, RecipeDate recipe) {
        mDataBase.child(recipe.getId()).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Успешно удалено из Firebase
                Toast.makeText(getContext(), "Элемент удален успешно!", Toast.LENGTH_SHORT).show();

                // Удаление элемента из списка и уведомление адаптера об изменении
                dateRecipeList.remove(position);
                recipeAdapter.notifyItemRemoved(position);
            } else {
                Toast.makeText(getContext(), "Ошибка при удалении!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public double calculateTotalCalories() {
        if (dateRecipeList == null || dateRecipeList.isEmpty()) {
            return 0;
        }


        for (RecipeDate recipe : dateRecipeList) {
            String span;
            span = recipe.getCal();

            totalCalories += Integer.parseInt(span);
        }
        return (totalCalories); // Round to one decimal place
    }
}

