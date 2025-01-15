package com.example.finalprojectgrizzbot_group8;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;


public class SignInActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn;
    private TextView createaccount;
    private FirebaseAuth mAuth;
    private static DBHelper dbHelper;
    public static String uid;

    public static String getUuid() {
        return uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login_page);
        emailInput = findViewById(R.id.Email_input);
        passwordInput = findViewById(R.id.Password_input);
        loginBtn = findViewById(R.id.login_btn);
        createaccount = findViewById(R.id.createaccountTV);

        dbHelper = new DBHelper(this);
        dbHelper.initializeDatabase();

        Intent intentFinshCreate = getIntent();
        if (intentFinshCreate != null && intentFinshCreate.hasExtra("email")) {
            String email = intentFinshCreate.getStringExtra("email");
            emailInput.setText(email);
        }

        loginBtn.setOnClickListener(view -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            // Retrieve the user ID (UID)
                            uid = currentUser.getUid();
                        }

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Navigate to another activity
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);

                        // Check if email or password has values and pass them to SignUpActivity
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        });
        createaccount.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, CreateUserActivity.class);

            // Check if email or password has values and pass them to SignUpActivity
            String email = emailInput.getText().toString();

            if (!email.isEmpty()) {
                intent.putExtra("email", email);
            }
            startActivity(intent);
        });
    }
}