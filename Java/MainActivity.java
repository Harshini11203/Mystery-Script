package com.example.mysteryscript;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {
    ImageButton bt;
    private static MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (backgroundMusic == null) { // Prevent restarting music on re-entry
            backgroundMusic = MediaPlayer.create(this, R.raw.audio);
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(1.0f, 1.0f);
            backgroundMusic.start();
        }

        bt = findViewById(R.id.bt);
        bt.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, second.class);
            startActivity(intent);
        });
    }

    public static void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't stop music here, as we only want to stop it in specific cases
    }
}
