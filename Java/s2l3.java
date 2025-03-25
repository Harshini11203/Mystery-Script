package com.example.mysteryscript;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class s2l3 extends AppCompatActivity {
    private TextView tv2;
    private ImageView btnSubmit;
    private Button btnPlayAudio;
    private RadioGroup radioGroupQ1;
    private MediaPlayer mediaPlayer;
    private TextView tvRunningText;
    private ScrollView scrollView;
    private Handler handler = new Handler();
    private int textIndex = 0;
    private boolean isPlaying = false;
    private int currentQuestionIndex = 1; // Track question number

    // Declare star ImageViews
    private ImageView star1, star2, star3;
    private int currentStar = 0; // Tracks filled stars

    private String fullText = "December 24, 1999. Flight IC-814 was hijacked on its way from Kathmandu to New Delhi. " +
            "Five masked hijackers forced the plane to land in Kandahar, Afghanistan. " +
            "Intelligence agents intercepted a cryptic message: 'The eagle lands where the crescent falls at midnight.' " +
            "What were the hijackers planning? Decode their messages, uncover their motives, and solve the case.";

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
        setContentView(R.layout.activity_s2l3);

        Log.d("lifecycle", "Level233 started");

        btnSubmit = findViewById(R.id.btnSubmit);
        btnPlayAudio = findViewById(R.id.btnPlayAudio);
        radioGroupQ1 = findViewById(R.id.radioGroupOptions);
        tvRunningText = findViewById(R.id.tvRunningText);
        scrollView = findViewById(R.id.scrollView);
        tv2 = findViewById(R.id.tv);

        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);

        mediaPlayer = MediaPlayer.create(this, R.raw.audio4);

        btnSubmit.setOnClickListener(v -> handleQuestionSubmission());
        btnPlayAudio.setOnClickListener(v -> handleAudioPlayback());

        loadNextQuestion();
        MainActivity.stopBackgroundMusic();
    }

    private void updateStars() {
        ImageView[] stars = {star1, star2, star3};
        if (currentStar < 3) {
            stars[currentStar].setImageResource(R.drawable.baseline_star_24);
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
                if (selectedAnswer.equals("Kandahar, Afghanistan")) {
                    updateStars();
                    showAlertWithNextQuestion("Correct! The coded message points to Kandahar, a stronghold under Taliban control.", 2);
                } else {
                    showAlert("Incorrect answer. Try again.");
                }
                break;

            case 2:
                if (selectedAnswer.equals("A spy onboard")) {
                    updateStars();
                    showAlertWithNextQuestion("Correct! A hidden radio revealed an undercover intelligence agent in seat 13C.", 3);
                } else {
                    showAlert("Incorrect answer. Try again.");
                }
                break;

            case 3:
                if (selectedAnswer.equals("To spark future terror in India")) {
                    updateStars();
                    showCaseConclusion();
                } else {
                    showAlert("Incorrect answer. Try again.");
                }
                break;
        }
    }

    private void loadNextQuestion() {
        radioGroupQ1.clearCheck();

        switch (currentQuestionIndex) {
            case 1:
                tv2.setText("Question 1: What does the message 'The eagle lands where the crescent falls at midnight' reveal?");
                ((RadioButton) findViewById(R.id.option1)).setText("A secret ransom drop-off");
                ((RadioButton) findViewById(R.id.option2)).setText("The hijackers’ escape plan");
                ((RadioButton) findViewById(R.id.option3)).setText("Kandahar, Afghanistan");
                ((RadioButton) findViewById(R.id.option4)).setText("A hidden accomplice");
                break;

            case 2:
                tv2.setText("Question 2: Security found a broken radio with the code 'S3-X9-4'. What does it reveal?");
                ((RadioButton) findViewById(R.id.option1)).setText("The next hijacker target");
                ((RadioButton) findViewById(R.id.option2)).setText("A spy onboard");
                ((RadioButton) findViewById(R.id.option3)).setText("A ransom message");
                ((RadioButton) findViewById(R.id.option4)).setText("A secret exit point");
                break;

            case 3:
                tv2.setText("Question 3: What was the hijackers’ true objective?");
                ((RadioButton) findViewById(R.id.option1)).setText("To escape unnoticed");
                ((RadioButton) findViewById(R.id.option2)).setText("To free political prisoners");
                ((RadioButton) findViewById(R.id.option3)).setText("To spark future terror in India");
                ((RadioButton) findViewById(R.id.option4)).setText("To demand ransom money");
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
                                .setMessage("You uncovered the hijackers’ true plan to spark future terror in India by freeing their leader. " +
                                        "Your quick thinking prevented further disaster. Case closed! 🕵️‍♂️")
                                .setPositiveButton("Next Level", (dialog, which) -> {
                                    Intent intent = new Intent(s2l3.this, stage2.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .show();
                    },1000);
                }).start();
    }

    private void handleAudioPlayback() {
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
}