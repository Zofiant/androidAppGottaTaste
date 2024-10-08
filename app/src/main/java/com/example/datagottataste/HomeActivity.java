package com.example.datagottataste;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.example.datagottataste.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    ActivityHomeBinding binding;
    Calendar dateAndTime = Calendar.getInstance();
    String formattedDate;
    private DrawerLayout drawerLayout;
    FirebaseAuth auth;
    FirebaseUser user;
    User userInfo;
    FirebaseDatabase database;
    DatabaseReference userRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(findViewById(R.id.toolbar));
//
//        drawerLayout = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        //ActionBarForMenu

//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
        setInitialDateTime();
        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance(Const.DB_URL);

        user = auth.getCurrentUser();
        userId = user.getUid();
        userRef = database.getReference(Const.KEY_USER);


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(binding.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, HomeActivity.class));
        } else if (id == R.id.nav_profile) {
            findById(userId);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Закрыть текущую активность
        }

        drawerLayout.closeDrawer(GravityCompat.START); // Закрыть боковое меню после выбора
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setInitialDateTime() {
        LocalDate localDate = LocalDate.of(dateAndTime.get(Calendar.YEAR), dateAndTime.get(Calendar.MONTH) + 1, dateAndTime.get(Calendar.DAY_OF_MONTH));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedDate = localDate.format(formatter);
        binding.Date.setText(formattedDate);
        updateFragment(formattedDate,userInfo);





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


    private void updateFragment(String formattedDate, User userInfo) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DietaFragment fragment = DietaFragment.newInstance(formattedDate,userInfo);// Передаем строку с форматированным временем
        fragmentManager.beginTransaction()
                .replace(R.id.containerOfDate, fragment)
                .commit();
    }



        private TextView nameTextView;

        public void findById(String userId) {
            userRef.orderByChild("id")
                    .equalTo(userId)
                    .limitToFirst(1)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            User userver = dataSnapshot.getValue(User.class);
                            onUserRetrieved(userver);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }




    private void onUserRetrieved(User userver) {
            Intent navProfile = new Intent(this, ProfileActivity.class);
            navProfile.putExtra("PROFILE",userver);
            startActivity(navProfile);

        }



}
