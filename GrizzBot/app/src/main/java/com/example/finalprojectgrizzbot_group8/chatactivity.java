package com.example.finalprojectgrizzbot_group8;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class chatactivity extends AppCompatActivity {
    Intent v1;
    Button gotomainpage;
    private DBHelper dbHelper;
    private LinearLayout chatContainer;  // LinearLayout to hold all chat messages
    private EditText userInput;
    private Button sendButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);

        // Initialize the views
        gotomainpage = findViewById(R.id.gotomainpagebtn);
        dbHelper = new DBHelper(this);
        dbHelper.getReadableDatabase();
        chatContainer = findViewById(R.id.chatContainer);  // Make sure this view is in your layout
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);

        // Intent for navigation to MainActivity
        v1 = new Intent(this, MainActivity.class);
        gotomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(v1);  // Start MainActivity when the button is clicked
            }
        });

        // Setting up the click listener for the send button
        sendButton.setOnClickListener(v -> {
            String userMessage = userInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                // Fetch response from the DB
                String botResponse = dbHelper.getResponse(userMessage);

                // Add User message with image and text
                addMessageToChat(userMessage, true); // 'true' for user message

                // Add Bot response with image and text
                addMessageToChat(botResponse, false); // 'false' for bot message

                dbHelper.insertQuestionHistory(userMessage,botResponse,SignInActivity.getUuid());
                // Clear the user input field
                userInput.setText("");
            }
        });

        // Set up insets (for system bars like status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intentViewHistory = getIntent();
        if (intentViewHistory != null && intentViewHistory.hasExtra("history")) {
            String email = intentViewHistory.getStringExtra("history");
            if (Objects.equals(email, "TRUE")) {
                loadPreviousMessages();
            }
        }
    }

    // Method to dynamically add a message to the chat container
    private void addMessageToChat(String message, boolean isUserMessage) {
        // Create a new LinearLayout for the message
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.HORIZONTAL);
        messageLayout.setPadding(16, 8, 16, 8);

        // Set gravity depending on whether it's a user or bot message
        if (isUserMessage) {
            messageLayout.setGravity(Gravity.END); // Align user message to the right
        } else {
            messageLayout.setGravity(Gravity.START); // Align bot message to the left
        }

        // Add the ImageView (user or bot icon) to the layout
        ImageView userIcon = new ImageView(this);
        if (isUserMessage) {
            userIcon.setImageResource(R.drawable.user); // Add your user icon here
        } else {
            userIcon.setImageResource(R.drawable.chatbot); // Add your bot icon here
        }
        userIcon.setLayoutParams(new LinearLayout.LayoutParams(150, 150)); // Set image size
        userIcon.setPadding(0, 0, 16, 0); // Padding between icon and text

        // Add the TextView for the message
        TextView messageText = new TextView(this);
        messageText.setText(message);
        messageText.setTextSize(20);
        messageText.setPadding(16, 8, 16, 8);

        // Set the background for the message (rounded corners)
        if (isUserMessage) {
            messageText.setBackgroundResource(R.drawable.user_message_background); // User bubble background
            messageText.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            messageText.setBackgroundResource(R.drawable.bot_message_background); // Bot bubble background
            messageText.setTextColor(getResources().getColor(android.R.color.black));
        }

        // Add ImageView and TextView to the message layout
        messageLayout.addView(userIcon);
        messageLayout.addView(messageText);

        // Add the message layout to the chat container
        chatContainer.addView(messageLayout);
    }

    // Load the previous messages from the database (for display in the chat container)
    private void loadPreviousMessages() {
        // For the sake of demonstration, we are assuming that dbHelper can fetch
        // all previous messages. You would replace this with actual DB fetching logic.
        Cursor history = dbHelper.getQuestionHistory(SignInActivity.getUuid());

        if (history != null) {
            try {
                while (history.moveToNext()) {
                    // Example: Get data from specific columns
                    String question = history.getString(history.getColumnIndexOrThrow("question"));
                    String response = history.getString(history.getColumnIndexOrThrow("answer"));

                    if (question != null && !response.isEmpty()) {
                        addMessageToChat(question, true); // Display previous user message
                        addMessageToChat(response, false); // Display previous bot message
                    }
                    // Process the data
                    Log.d("CursorData", "Question: " + question + ", Response: " + response);
                }
            } finally {
                // Always close the cursor to free resources
                history.close();
            }
        }
    }
}

