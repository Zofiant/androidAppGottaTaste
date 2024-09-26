package com.example.datagottataste;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import com.example.datagottataste.databinding.ActivityHomeBinding;
import androidx.fragment.app.FragmentManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    Calendar dateAndTime = Calendar.getInstance();
    String formattedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setInitialDateTime();
        setContentView(binding.getRoot());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containerOfDate, new DietaFragment())
                .commit();
    }

    private void setInitialDateTime() {
        LocalDate localDate = LocalDate.of(dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH) + 1, dateAndTime.get(Calendar.DAY_OF_MONTH));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedDate = localDate.format(formatter);
        binding.Date.setText(formattedDate);
        updateFragment(formattedDate);
    }

    public void setDate(View v) {
        new DatePickerDialog(HomeActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();

        }
    };

    private void updateFragment(String formattedDate) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DietaFragment fragment = DietaFragment.newInstance(formattedDate); // Передаем строку с форматированным временем
        fragmentManager.beginTransaction()
                .replace(R.id.containerOfDate, fragment)
                .commit();
    }
}
