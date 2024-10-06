package com.example.datagottataste;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datagottataste.databinding.FragmentDietaBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
/*TODO understand what that shitty code does and add RecipeAdapter and RecipeBd for it.
   Choose between recycleView and listView */
public class DietaFragment extends Fragment {
    private static final String ARG_DATE = "arg_date";
    private String formattedDate;
    private List<RecipeDate> dateRecipeList;
    private RecipeListAdapter adapter;
    private FragmentDietaBinding binding;
    private SharedPreferences sharedPreferences;
    //private ActivityResultLauncher<Intent> activityResultLauncher;
    DatabaseReference mDataBase;
    StorageReference mStorageReference;
    private User user;
    private String currentDate;


    public static DietaFragment newInstance(String formattedDate) { // Изменено имя параметра
        DietaFragment fragment = new DietaFragment();
        Bundle args = new Bundle();
        args.putString("formattedDate", formattedDate);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            user = args.getParcelable("user");
            currentDate = args.getString("date");
        }
    }

    //sharedPreferences = getActivity().getSharedPreferences("DietaPrefs", Context.MODE_PRIVATE);
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


        binding.btnAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PickRecipeActivity.class);
            activityResultLauncher.launch(intent);
        });
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

                    if(item.getUserId().contains(user.getId())){
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
    }
    public void saveDietData(String dietData) {


    }
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {


                            /*TODO Здесь нужно создать новое поле Date
                            TODO и в начале сделать прогрузку Date при выборе даты */
                        }
                    });

}
