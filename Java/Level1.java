package com.example.mysteryscript;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.os.Handler;
import android.os.CountDownTimer;

public class Level1 extends AppCompatActivity {
    private MediaPlayer backgroundMusic;
    private boolean isPaused = false;
    private AudioManager audioManager;
    private TextView questionText;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    private ImageButton nextButton, btnMenu;
    private ImageView star1, star2, star3  ;
    private ImageView[] stars;
    private ImageView imageView;

    private int currentQuestionIndex = 0;

    // Game Questions & Answers
    private String[] questions = {
            "A well-known politician was assassinated during a public rally in Delhi. Thousands were present, but the shooter vanished into the crowd. Investigators believe it was an inside job. \n What was the murder weapon?",
            "What is the most suspicious clue?",
            "Who is the prime suspect?"
    };

    private String[][] options = {
            {"A) A poisoned handkerchief", "B) A sniper rifle", "C) A car explosion", "D) A knife hidden in a bouquet"},
            {"A) A missing security report", "B) A misplaced ID card", "C) A speech paper with edits", "D) A broken microphone"},
            {"A) A rival party member", "B) A former bodyguard", "C) A news reporter", "D) A local businessman"}
    };

    private int[] correctAnswers = {1, 0, 1}; // Correct answer indexes

    private String[] clues = {
            "(Clue: A bullet casing was found on a nearby rooftop.)",
            "(Clue: A security officer had flagged a possible threat but was ignored.)",
            "(Clue: He was recently fired after a heated argument with the politician.)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);

        // Initialize UI Components
        questionText = findViewById(R.id.questionText);
        radioGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        btnMenu = findViewById(R.id.btnMenu);
        imageView = findViewById(R.id.crimeImage);
        imageView.setOnClickListener(v -> showZoomedImage());

        // Initialize stars
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        stars = new ImageView[]{star1, star2, star3};

        // Load first question
        loadQuestion();

        // Initialize Background Music
        backgroundMusic = MediaPlayer.create(this, R.raw.audio);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f, 0.5f); // Start with medium volume
        backgroundMusic.start();

        // Initialize Audio Manager
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //imageView.setOnClickListener(v -> showZoomedImage());

        // Button Click Listeners
        nextButton.setOnClickListener(view -> checkAnswer());
        btnMenu.setOnClickListener(view -> showMenuDialog());
    }

    private void loadQuestion() {
        questionText.setText(questions[currentQuestionIndex]);
        option1.setText(options[currentQuestionIndex][0]);
        option2.setText(options[currentQuestionIndex][1]);
        option3.setText(options[currentQuestionIndex][2]);
        option4.setText(options[currentQuestionIndex][3]);

        radioGroup.clearCheck(); // Clear previous selection
    }

    private void checkAnswer() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            showAlert("No Option Selected", "Please select an answer before proceeding.");
            return;
        }

        int selectedIndex = getSelectedIndex(selectedId);
        if (selectedIndex == correctAnswers[currentQuestionIndex]) {
            updateStar(currentQuestionIndex); // ⭐ Update star when correct
            showCorrectAnswerDialog();
        } else {
            showAlert("Incorrect Answer", "That's not the correct choice. Try again!");
        }
    }

    private int getSelectedIndex(int selectedId) {
        if (selectedId == option1.getId()) return 0;
        if (selectedId == option2.getId()) return 1;
        if (selectedId == option3.getId()) return 2;
        if (selectedId == option4.getId()) return 3;
        return -1;
    }

    private void updateStar(int index) {
        if (index < stars.length) {
            stars[index].setImageResource(R.drawable.baseline_star_24);
        }
    }

    private void showCorrectAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Correct Answer!")
                .setMessage("Well done! Let's move to the next question.\n" + clues[currentQuestionIndex])
                .setPositiveButton("Next", (dialog, which) -> {
                    currentQuestionIndex++;
                    if (currentQuestionIndex < questions.length) {
                        loadQuestion();
                    } else {
                        showCaseConclusion();
                    }
                })
                .setCancelable(false)
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Level1.this);
                        builder.setTitle("Case Solved!")
                                .setMessage("You solved the case! Moving to the next level.\n\nThe murder weapon, a sniper rifle, pointed to a planned and precise attack. Among the scattered clues, the missing security report stood out, revealing a warning that had been deliberately ignored. Finally, the prime suspect, a former bodyguard, had both motive and opportunity.")
                                .setPositiveButton("Next Level", (dialog, which) -> {
                                    // Unlock Level 2
                                    SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("Level1Completed", true);
                                    editor.apply();

                                    // Move to Level 2
                                    startActivity(new Intent(Level1.this, first.class));
                                    finish();
                                })
                                .setNegativeButton("Exit", (dialog, which) -> {
                                    // Move to second.class instead of closing
                                    startActivity(new Intent(Level1.this, second.class));
                                    finish();
                                })
                                .setCancelable(false)
                                .show();
                    }, 1000); // Delay to allow animation effect
                }).start();
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }

    private void showMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.menu_dialog, null);
        builder.setView(view);

        Button btnPause = view.findViewById(R.id.btnPause);
        Button btnResume = view.findViewById(R.id.btnPlay);
        Button btnRestart = view.findViewById(R.id.btnRestart);
        Button btnSound = view.findViewById(R.id.btnSound);
        SeekBar volumeSeekBar = view.findViewById(R.id.volumeSeekBar);
        Button btnClose = view.findViewById(R.id.btnClose);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnPause.setOnClickListener(v -> {
            if (!isPaused) {
                backgroundMusic.pause();
                isPaused = true;
            }
        });

        btnResume.setOnClickListener(v -> {
            if (isPaused) {
                backgroundMusic.start();
                isPaused = false;
            }
        });

        btnRestart.setOnClickListener(v -> {
            backgroundMusic.seekTo(0);
            backgroundMusic.start();
            isPaused = false;
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
                float volume = progress / (float) volumeSeekBar.getMax();
                backgroundMusic.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
    }
    private void showZoomedImage() {

        // Create a dialog to show the zoomed-in image
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_zoom_image);

        // Get the ImageView from the dialog
        ImageView zoomedImageView = dialog.findViewById(R.id.zoomedImageView);
        TextView timerTextView = dialog.findViewById(R.id.timerTextView);
        // Set the same image as the main image (without affecting audio)
        zoomedImageView.setImageDrawable(imageView.getDrawable());

        // Show the dialog
        dialog.show();

        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time Left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                dialog.dismiss(); // Close the dialog after 10 seconds
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusic != null) {
            backgroundMusic.release();
        }
    }
}
