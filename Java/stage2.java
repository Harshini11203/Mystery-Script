package com.example.mysteryscript;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class stage2 extends AppCompatActivity {
    ImageButton b1, b2, b3, btnMenu;
    //AudioManager audioManager;
    LinearLayout mainLayout; // Reference to the main layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage2);

        b1 = findViewById(R.id.btnLevel1);
        b2 = findViewById(R.id.btnLevel2);
        b3 = findViewById(R.id.btnLevel3);
        btnMenu = findViewById(R.id.btnMenu);
        mainLayout = findViewById(R.id.mainLayout); // Assuming your main layout has this ID

        //audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Level 1 Click - Remove Background and Start Activity
        b1.setOnClickListener(view -> {
            removeBackground();
            Intent intent = new Intent(getApplicationContext(), s2l1.class);
            startActivity(intent);
        });

        b2.setOnClickListener(view -> {
            removeBackground();
            Intent intent = new Intent(getApplicationContext(), s2l2.class);
            startActivity(intent);
        });

        b3.setOnClickListener(view -> {
            removeBackground();
            Intent intent = new Intent(getApplicationContext(), s2l3.class);
            startActivity(intent);
        });

        btnMenu.setOnClickListener(view -> showMenuDialog());
        MainActivity.stopBackgroundMusic();
    }

    private void removeBackground() {
        if (mainLayout != null) {
            mainLayout.setBackground(null); // Removes the background
        }
    }

    private void showMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.menu_dialog, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }
}
