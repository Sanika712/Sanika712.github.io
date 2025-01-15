package com.example.finalprojectgrizzbot_group8;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class settingsactivity extends AppCompatActivity {
    Button gotomainpage;
    Intent v1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settingsactivity);

        gotomainpage = findViewById(R.id.gotomainpagebtn);

        v1 = new Intent(this, MainActivity.class);

        gotomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsactivity.this.startActivity(v1);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}