package com.example.datagottataste;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toolbar;

import com.example.datagottataste.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    Calendar dateAndTime = Calendar.getInstance();
    String formattedDate;
    private DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser user;
    User userInfo;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setInitialDateTime();
        setContentView(binding.getRoot());
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(findViewById(R.id.toolbar));
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        //ActionBarForMenu
//        /* TODO why the fuck this isnt working*/
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance(Const.DB_URL);

        user = auth.getCurrentUser();
        setNewFragment(new DietaFragment());


    }

    private void setNewFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userInfo);
        bundle.putString("date",formattedDate);
        fragment.setArguments(bundle);



        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.containerOfDate, fragment)
                .commit();

    }

    private void setInitialDateTime() {
        LocalDate localDate = LocalDate.of(dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH) + 1, dateAndTime.get(Calendar.DAY_OF_MONTH));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedDate = localDate.format(formatter);
        binding.Date.setText(formattedDate);
        setNewFragment(new DietaFragment());
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
