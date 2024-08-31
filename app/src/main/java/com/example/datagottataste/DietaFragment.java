package com.example.datagottataste;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.datagottataste.databinding.FragmentDietaBinding;

import java.io.Serializable;
import java.util.List;
/*TODO understand what that shitty code does and add RecipeAdapter and RecipeItem for it.
   Choose between recycleView and listView */
public class DietaFragment extends Fragment {
    private static final String ARG_DATE = "arg_date";
    private String formattedDate;
    private ListView recipeList;
    private RecipeAdapter adapter;
    private List<RecipeChar> checkedRecipe;
    private FragmentDietaBinding binding;
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<Intent> activityResultLauncher;


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
        if (getArguments()!= null) {
            formattedDate = getArguments().getString("formattedDate"); // Получаем строку с форматированным временем
        }
    }

    //sharedPreferences = getActivity().getSharedPreferences("DietaPrefs", Context.MODE_PRIVATE);
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDietaBinding.inflate(inflater, container, false);
        loadDietData();
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Создание ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Обработка результата
                        Intent data = result.getData();
                        if (data!= null) {

                            checkedRecipe = (List<RecipeChar>) data.getSerializableExtra("checkedRecipe");
                            adapter = new RecipeAdapter(getContext(), checkedRecipe);
                            binding.dateRecipeList.setAdapter(adapter);
                            double totalCal = adapter.calculateTotalCal(checkedRecipe);
                            binding.calories.setText(totalCal+"/2000 каллорий");

                        }
                    }
                });

        binding.btnAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PickRecipeActivity.class);
            activityResultLauncher.launch(intent);
        });
    }

    private void loadDietData() {

        binding.textViewDietData.setText(String.valueOf(formattedDate));
    }

    public void saveDietData(String dietData) {
        String dateKey = String.valueOf(formattedDate);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(dateKey, dietData);
        editor.apply();
    }




}
