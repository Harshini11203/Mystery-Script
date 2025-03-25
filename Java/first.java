package com.example.mysteryscript;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class first extends AppCompatActivity {
    //private MediaPlayer backgroundMusic;
    ImageButton b1, b2, b3, btnMenu;
    //boolean isPlaying = true;
    AudioManager audioManager;
    //private float currentVolume = 1.0f; // Full volume

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        b1 = findViewById(R.id.btnLevel1);
        b2 = findViewById(R.id.btnLevel2);
        b3 = findViewById(R.id.btnLevel3);
        btnMenu = findViewById(R.id.btnMenu);

        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        boolean level1Completed = preferences.getBoolean("Level1Completed", false);
        boolean level2Completed = preferences.getBoolean("Level2Completed", false);

        b1.setEnabled(true); // Level 1 always enabled
        b2.setEnabled(level1Completed); // Unlock Level 2 if Level 1 is completed
        b3.setEnabled(level2Completed);

        // Initialize background music
//        backgroundMusic = MediaPlayer.create(this, R.raw.audio);
//        backgroundMusic.setLooping(true);
//        backgroundMusic.setVolume(1.0f, 1.0f); // Start at full volume
//        backgroundMusic.start();

        // Get Audio Manager
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Level 1 Click - Gradually decrease volume
        b1.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Level1.class);
            startActivity(intent);
            //graduallyDecreaseVolume();
        });

        b2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Level2.class);
            startActivity(intent);
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Level 3
                Intent intent = new Intent(first.this, Level3.class);
                startActivity(intent);
            }
        });
        btnMenu.setOnClickListener(view -> showMenuDialog());
    }

    // Gradually decrease volume to medium level
//    private void graduallyDecreaseVolume() {
//        Handler handler = new Handler();
//        new Thread(() -> {
//            while (currentVolume > 0.5f) { // Reduce volume until it reaches 0.5 (medium)
//                currentVolume -= 0.05f;
//                backgroundMusic.setVolume(currentVolume, currentVolume);
//                try {
//                    Thread.sleep(500); // Delay for smooth reduction
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
    private boolean isPaused = false; // Track pause state

    private void showMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.menu_dialog, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnPlay = view.findViewById(R.id.btnPlay);
        Button btnPause = view.findViewById(R.id.btnPause);
        Button btnRestart = view.findViewById(R.id.btnRestart);
        Button btnClose = view.findViewById(R.id.btnClose);

        // Play Button: Continue in the same page
        btnPlay.setOnClickListener(v -> {
            if (isPaused) {
                isPaused = false;
                btnPause.setText("Pause"); // Reset Pause Button
            }
            dialog.dismiss(); // Close the menu
        });

        // Pause Button: Change to "Paused" and resume on Play
        btnPause.setOnClickListener(v -> {
            isPaused = true;
            btnPause.setText("Paused");
        });

        // Restart Button: Move to second.class
        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(first.this, second.class);
            startActivity(intent);
            finish(); // Finish current activity
        });

        // Close Button: Close the menu
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

}
