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

public class s2l2 extends AppCompatActivity {
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
    private int currentQuestionIndex = 1; // Track which question is being displayed
    // Declare star ImageViews
    private ImageView star1, star2, star3;
    private int currentStar = 0; // Track the number of stars filled

    private String fullText = "Renowned archaeologist Dr. Aarav Khanna was leading an excavation in Hampi, Karnataka, when he mysteriously vanished overnight. " +
            "His tent remained untouched—his wallet, phone, and research notes were still there, yet he was nowhere to be found. " +
            "A day before his disappearance, Dr. Khanna had told his team that he had uncovered something groundbreaking. " +
            "His last known words, scribbled hastily in his research journal, were:\n" +
            "\uD83D\uDCDD 'The truth was always hidden in the carvings. The lion does not roar in vain.' " +
            "There were no signs of forced entry or struggle, and whispers of an ancient legend about a hidden royal treasure began circulating. " +
            "Investigators must follow historical clues, decipher ancient symbols, and uncover secrets buried in time to locate him. " +
            "Can you solve the mystery and find Dr. Khanna before it’s too late?";

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
        setContentView(R.layout.activity_s2l2);
        Log.d("lifecycle","Level2 is started");
        // Initialize Views
        btnSubmit = findViewById(R.id.btnSubmit2);
        btnPlayAudio = findViewById(R.id.btnPlayAudio2);
        radioGroupQ1 = findViewById(R.id.radioGroupOptions2);
        tvRunningText = findViewById(R.id.tvRunningText2);
        scrollView = findViewById(R.id.scrollView2);
        tv2 = findViewById(R.id.tv2);
        // Initialize Stars
        star1 = findViewById(R.id.star12);
        star2 = findViewById(R.id.star22);
        star3 = findViewById(R.id.star32);

        // Initialize Audio
        mediaPlayer = MediaPlayer.create(this, R.raw.audio3);

        // Submit Button Click Listener
        btnSubmit.setOnClickListener(v -> handleQuestionSubmission());

        // Play Audio Button Click Listener
        btnPlayAudio.setOnClickListener(v -> handleAudioPlayback());

        // Load the first question
        loadNextQuestion();
    }

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
                if (selectedAnswer.equals("Shadow")) {
                    updateStars(); // Fill a star
                    showAlertWithNextQuestion("Correct! Shadow,The next clue involves observing where the lion carving’s shadow falls.", 2);
                } else {
                    showAlert("Incorrect answer. Please try again.");
                }
                break;

            case 2:
                if (selectedAnswer.equals("Two crossed keys")) {
                    updateStars(); // Fill a star
                    showAlertWithNextQuestion("Correct! Two crossed keys,This symbol indicates a sealed chamber or hidden entrance leading underground.", 3);
                } else {
                    showAlert("Incorrect answer. Please try again.");
                }
                break;

            case 3:
                if (selectedAnswer.equals("He discovered a hidden treasure but got trapped inside")) {
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
                tv2.setText("Question 1: Dr. Khanna’s journal had a half-smudged note. Only part of the text was legible:\n" +
                        " 'The lion does not roar in vain... Follow the ____ beneath the gaze.'\n" +
                        "What is the missing word that will lead to the next clue?");
                ((RadioButton) findViewById(R.id.option12)).setText("Echo");
                ((RadioButton) findViewById(R.id.option22)).setText("Path");
                ((RadioButton) findViewById(R.id.option32)).setText("Shadow");
                ((RadioButton) findViewById(R.id.option42)).setText("Sunlight");
                break;

            case 2:
                tv2.setText("Question 2: A stone slab near the excavation site had an ancient symbol carved into it. What did it represent?");
                ((RadioButton) findViewById(R.id.option12)).setText("A serpent coiled around a staff");
                ((RadioButton) findViewById(R.id.option22)).setText("Two crossed keys");
                ((RadioButton) findViewById(R.id.option32)).setText("A rising sun");
                ((RadioButton) findViewById(R.id.option42)).setText("An open hand");
                break;

            case 3:
                tv2.setText("Question 3: Based on the clues found at the site, what is the most likely fate of Dr. Khanna?");
                ((RadioButton) findViewById(R.id.option12)).setText("He was kidnapped for ransom");
                ((RadioButton) findViewById(R.id.option22)).setText("He fell into a hidden trapdoor");
                ((RadioButton) findViewById(R.id.option32)).setText("He discovered a hidden treasure but got trapped inside");
                ((RadioButton) findViewById(R.id.option42)).setText("He faked his own disappearance");
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
                                .setMessage("Dr. Khanna discovered an ancient treasure chamber but was trapped inside when the entrance collapsed behind him. " +
                                        "Thanks to your investigative skills, a rescue team was sent in time to save him.\n\n🔓 You have successfully solved the case!")
                                .setPositiveButton("Next Level", (dialog, which) -> {
                                    Intent intent = new Intent(s2l2.this, stage2.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .show();
                    }, 1000);
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