package com.example.finalprojectgrizzbot_group8;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class CreateUserActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput, reenterPasswordInput;
    private Button createAccountButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser_page);

        mAuth = FirebaseAuth.getInstance();

        // Initialize fields
        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.Email_input);
        passwordInput = findViewById(R.id.Password_input);
        reenterPasswordInput = findViewById(R.id.reenterPassword_input);
        createAccountButton = findViewById(R.id.createaccount_btn);

        createAccountButton.setOnClickListener(v -> {
           validateFields();
        });
    }

    private void validateFields() {
        boolean isValid = true;

        // Validate Name
        if (TextUtils.isEmpty(nameInput.getText())) {
            nameInput.setBackgroundResource(R.drawable.red_border);
            nameInput.setError("Name is required");
            isValid = false;
        } else {
            nameInput.setBackgroundResource(R.drawable.rounded_corner);
        }

        // Validate Email
        if (TextUtils.isEmpty(emailInput.getText())) {
            emailInput.setBackgroundResource(R.drawable.red_border);
            emailInput.setError("Email is required");
            isValid = false;
        } else {
            emailInput.setBackgroundResource(R.drawable.rounded_corner);
        }

        // Validate Password
        if (TextUtils.isEmpty(passwordInput.getText())) {
            passwordInput.setBackgroundResource(R.drawable.red_border);
            passwordInput.setError("Password is required");
            isValid = false;
        } else {
            passwordInput.setBackgroundResource(R.drawable.rounded_corner);
        }

        // Validate Re-enter Password
        if (TextUtils.isEmpty(reenterPasswordInput.getText())) {
            reenterPasswordInput.setBackgroundResource(R.drawable.red_border);
            reenterPasswordInput.setError("Re-enter Password is required");
            isValid = false;
        } else if (!reenterPasswordInput.getText().toString().equals(passwordInput.getText().toString())) {
            reenterPasswordInput.setBackgroundResource(R.drawable.red_border);
            reenterPasswordInput.setError("Passwords do not match");
            isValid = false;
        } else {
            reenterPasswordInput.setBackgroundResource(R.drawable.rounded_corner);
        }

        // If all fields are valid
        if (isValid) {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            String name = nameInput.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User registration successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name) // Set the user's display name
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                Log.d("Firebase", "Registration successful");
                                                Intent intent = new Intent(CreateUserActivity.this, SignInActivity.class);

                                                if (!email.isEmpty()) {
                                                    intent.putExtra("email", email);
                                                }
                                                startActivity(intent);
                                                // Navigate to login or main activity

                                                Log.d("Firebase", "User profile updated.");
                                            } else {
                                                Log.e("Firebase", "Error updating profile: " + updateTask.getException());
                                            }
                                        });
                                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("Firebase", "Registration failed: " + task.getException().getMessage());
                                Toast.makeText(this, "Account Failed to Create!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }
    }
}