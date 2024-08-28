package com.example.datagottataste;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.datagottataste.databinding.ActivityLoginBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            updateUI(currentUser);
        }
        else {

        }
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent start = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(start);
    }

    public void onClickLogin(View view) {
        if (!TextUtils.isEmpty(binding.editTextMail.getText().toString()) && !TextUtils.isEmpty(binding.editTextPassword.getText().toString())){
            mAuth.signInWithEmailAndPassword(binding.editTextMail.getText().toString(),binding.editTextPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Вход прошел успешно.",
                                Toast.LENGTH_SHORT).show();

                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Ошибка аутентификации",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }

                }
            });
        }
    }
    public void onClickReg(View view) {
        if (!TextUtils.isEmpty(binding.editTextMail.getText().toString()) && !TextUtils.isEmpty(binding.editTextPassword.getText().toString()))
            mAuth.createUserWithEmailAndPassword(binding.editTextMail.getText().toString(),binding.editTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCustomToken:success");
                        Toast.makeText(LoginActivity.this, "Регистрация прошла успешно.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Ошибка аутентификации",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }

                }
            });

    }
}