package com.example.mysteryscript;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class s2l1 extends AppCompatActivity {
    private TextView tv2;
    private ImageView btnSubmit;
    private Button btnPlayAudio;
    private ImageButton btnMenu;
    private MediaPlayer backgroundMusic;
    private boolean isPaused = false;
    private AudioManager audioManager;
    private RadioGroup radioGroupQ1;
    private MediaPlayer mediaPlayer;
    private TextView tvRunningText;
    private ScrollView scrollView;
    private Handler handler = new Handler();
    private int textIndex = 0;
    private boolean isPlaying = false;
    private int currentQuestionIndex = 1; // Track which question is being displayed
    // Declare star ImageViews
    private ImageView star1, star2, star3;
    private int currentStar = 0; // Track the number of stars filled

    private String fullText = "In 2016, Bengaluru saw one of the most sophisticated ATM fraud cases in India. " +
            "Several bank customers reported unauthorized withdrawals from their accounts, despite never using their ATM cards at those locations. " +
            "The common link? All victims had used a specific ATM in Koramangala before noticing the missing funds. " +
            "Investigators discovered that the ATM had been tampered with. " +
            "A skimming device was secretly installed over the card slot to copy users' card data, while a tiny hidden camera recorded their PINs. " +
            "This stolen information was then used to create cloned ATM cards, which allowed the criminals to withdraw money from various ATMs across different cities. " +
            "Through transaction logs and CCTV footage, authorities identified a group of suspects who moved strategically across multiple locations to avoid being traced. " +
            "The mastermind behind the fraud was suspected to be an insider—someone with technical knowledge of ATM systems. " +
            "Can you analyze the evidence and solve the case?";

    private Runnable textUpdater = new Runnable() {
        @Override
        public void run() {
            if (textIndex < fullText.length()) {
                tvRunningText.setText(fullText.substring(0, textIndex + 1));
                textIndex++;
                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
                handler.postDelayed(this, 60); // Adjust speed of text reveal
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s2l1);
        Log.d("lifecycle","Level1 is started");

        // Initialize Views
        btnSubmit = findViewById(R.id.btnSubmit);
        btnPlayAudio = findViewById(R.id.btnPlayAudio);
        radioGroupQ1 = findViewById(R.id.radioGroupOptions);
        tvRunningText = findViewById(R.id.tvRunningText);
        scrollView = findViewById(R.id.scrollView);
        tv2 = findViewById(R.id.tv);
        btnMenu = findViewById(R.id.menuButton);
        // Initialize Stars
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);

        // Initialize Audio
        mediaPlayer = MediaPlayer.create(this, R.raw.audio2);
        //audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        backgroundMusic = MediaPlayer.create(this, R.raw.audio); // Replace with your actual background audio
//        backgroundMusic.setLooping(true);
//        backgroundMusic.setVolume(0.5f, 0.5f); // Start at medium volume
//        backgroundMusic.start();

//        new Handler().postDelayed(() -> {
//            if (backgroundMusic.isPlaying()) {
//                backgroundMusic.stop();
//                backgroundMusic.release(); // Free up resources
//                backgroundMusic = null;
//            }
//        }, 3000);

        // Submit Button Click Listener
        btnSubmit.setOnClickListener(v -> handleQuestionSubmission());

        // Play Audio Button Click Listener
        btnPlayAudio.setOnClickListener(v -> handleAudioPlayback());

        // Menu Button Click Listener (Fixed)
        btnMenu.setOnClickListener(view -> showMenuDialog());


        // Load the first question
        loadNextQuestion();
        MainActivity.stopBackgroundMusic();
    }
    // Function to update stars on correct answers
    private void updateStars() {
        ImageView[] stars = {star1, star2, star3};

        if (currentStar < 3) { // Fill stars progressively
            stars[currentStar].setImageResource(R.drawable.baseline_star_24); // Use a filled star drawable
            currentStar++;
        }
    }

    private void handleQuestionSubmission() {
        int selectedId = radioGroupQ1.getCheckedRadioButtonId();
        if (selectedId == -1) {
            showAlert("Please select an answer.");
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedAnswer = selectedRadioButton.getText().toString();

        switch (currentQuestionIndex) {
            case 1:
                if (selectedAnswer.equals("Used the same ATM in Koramangala")) {
                    updateStars(); // Fill a star
                    showAlertWithNextQuestion("Correct! The victims all used the same ATM in Koramangala before reporting fraud.", 2);
                } else {
                    showAlert("Incorrect answer. Please try again.");
                }
                break;

            case 2:
                if (selectedAnswer.equals("Skimming devices and hidden cameras")) {
                    updateStars(); // Fill a star
                    showAlertWithNextQuestion("Correct! The encrypted message reveals 'Skimmer Devices and Hidden Cameras Installed'.", 3);
                } else {
                    showAlert("Incorrect answer. Please try again.");
                }
                break;

            case 3:
                if (selectedAnswer.equals("An insider with ATM system knowledge")) {
                    updateStars(); // Fill a star
                    showCaseConclusion();
                } else {
                    showAlert("Incorrect answer. Please try again.");
                }
                break;
        }
    }

    private void loadNextQuestion() {
        radioGroupQ1.clearCheck(); // Clear selected radio button

        switch (currentQuestionIndex) {
            case 1:
                tv2.setText("Question 1: What common factor linked all victims of the ATM fraud?");
                ((RadioButton) findViewById(R.id.option1)).setText("Used the same ATM in Koramangala");
                ((RadioButton) findViewById(R.id.option2)).setText("Withdrew large sums of money");
                ((RadioButton) findViewById(R.id.option3)).setText("Used online banking on public Wi-Fi");
                ((RadioButton) findViewById(R.id.option4)).setText("Received scam calls");
                break;

            case 2:
                tv2.setText("Question 2: The ATM log file contained a hidden message, encrypted in a simple Caesar Cipher with a shift of +3.\n🔎 Hidden text found in ATM logs:\nVklppi Qhylfhohu dqg Kllghq Fdphudv Lqvwdohg");
                ((RadioButton) findViewById(R.id.option1)).setText("Fake phone calls pretending to be bank officials");
                ((RadioButton) findViewById(R.id.option2)).setText("Skimming devices and hidden cameras");
                ((RadioButton) findViewById(R.id.option3)).setText("Hacking into mobile banking apps");
                ((RadioButton) findViewById(R.id.option4)).setText("Physically stealing ATM cards");
                break;

            case 3:
                tv2.setText("Question 3: Based on the transaction logs and CCTV footage, who is most likely behind the fraud?");
                ((RadioButton) findViewById(R.id.option1)).setText("A random street thief");
                ((RadioButton) findViewById(R.id.option2)).setText("An insider with ATM system knowledge");
                ((RadioButton) findViewById(R.id.option3)).setText("A victim of fraud");
                ((RadioButton) findViewById(R.id.option4)).setText("A bank security guard");
                break;
        }
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showAlertWithNextQuestion(String message, int nextQuestionIndex) {
        new AlertDialog.Builder(this)
                .setTitle("Correct!")
                .setMessage(message)
                .setPositiveButton("Next Question", (dialog, which) -> {
                    currentQuestionIndex = nextQuestionIndex;
                    loadNextQuestion();
                })
                .show();
    }

    private void showCaseConclusion() {
        // Find the stamp image
        ImageView stampImage = findViewById(R.id.stampImage);

        // Make the stamp visible
        stampImage.setVisibility(View.VISIBLE);

        // Create an animation (scale and fade-in effect)
        stampImage.setScaleX(0.5f);
        stampImage.setScaleY(0.5f);
        stampImage.setAlpha(0f);

        stampImage.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(800) // Stamp animation duration
                .withEndAction(() -> {
                    // After animation, show the alert dialog
                    new Handler().postDelayed(() -> {
                        new AlertDialog.Builder(this)
                                .setTitle("Case Solved!")
                                .setMessage("The investigation confirmed that an **insider** with deep ATM system knowledge was behind the fraud. The suspect was an ex-technician who had worked on ATM maintenance. By tracking transaction logs and CCTV footage, authorities arrested the criminal.\n\n🔓 You have successfully solved the case!")
                                .setPositiveButton("Next Level", (dialog, which) -> {
                                    Intent intent = new Intent(s2l1.this, stage2.class);
                                    Log.d("lifecycle", "Level2 initializing");
                                    startActivity(intent);
                                    finish();
                                })
                                .show();
                    }, 1000); // Delay to allow animation effect
                }).start();
    }

    private void handleAudioPlayback() {
        if (backgroundMusic != null) {
            if (backgroundMusic.isPlaying()) {
                backgroundMusic.stop();
            }
            backgroundMusic.release();
            backgroundMusic = null;
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.audio2);
        }

        if (isPlaying) {
            mediaPlayer.pause();
            handler.removeCallbacks(textUpdater);
            isPlaying = false;
        } else {
            handler.post(textUpdater);
            mediaPlayer.start();
            isPlaying = true;

            mediaPlayer.setOnCompletionListener(mp -> {
                isPlaying = false;
                handler.removeCallbacks(textUpdater);
            });
        }
    }


    private void showMenuDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.menu_dialog, null);
        builder.setView(view);

        Button btnPause = view.findViewById(R.id.btnPause);
        Button btnResume = view.findViewById(R.id.btnPlay);
        Button btnRestart = view.findViewById(R.id.btnRestart);
        Button btnSound = view.findViewById(R.id.btnSound);
        SeekBar volumeSeekBar = view.findViewById(R.id.volumeSeekBar);
        Button btnClose = view.findViewById(R.id.btnClose);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        btnPause.setOnClickListener(v -> {
            if (backgroundMusic != null && backgroundMusic.isPlaying()) {
                backgroundMusic.pause();
                isPaused = true;
            }
        });

        btnResume.setOnClickListener(v -> {
            if (backgroundMusic != null && isPaused) {
                backgroundMusic.start();
                isPaused = false;
            }
        });

        btnRestart.setOnClickListener(v -> {
            if (backgroundMusic != null) {
                backgroundMusic.seekTo(0);
                backgroundMusic.start();
                isPaused = false;
            }
        });

        btnSound.setOnClickListener(v -> {
            if (volumeSeekBar.getVisibility() == View.GONE) {
                volumeSeekBar.setVisibility(View.VISIBLE);
            } else {
                volumeSeekBar.setVisibility(View.GONE);
            }
        });

        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (backgroundMusic != null) {
                    float volume = progress / (float) volumeSeekBar.getMax();
                    backgroundMusic.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(textUpdater);
    }
}