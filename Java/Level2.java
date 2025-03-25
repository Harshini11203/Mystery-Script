package com.example.mysteryscript;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Level2 extends AppCompatActivity {
    private TextView questionText;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    private ImageButton nextButton;
    private ImageView star1, star2, star3;
    private ImageView[] stars;
    private ImageView imageView;

    private int currentQuestionIndex = 0;

    private String[] questions = {
            "Deep inside an ancient South Indian temple, a sacred treasure—an emerald-studded idol of a deity—has mysteriously vanished. The temple priests are in shock, and the police suspect an inside job. \n What is the most suspicious clue left behind?",
            "How did the thief escape without being noticed?",
            "Who is the prime suspect in the theft?"
    };

    private String[][] options = {
            {"A) A torn piece of saffron cloth", "B) A half-burnt map of the temple", "C) A broken lamp", "D) A missing coconut from the offering plate"},
            {"A) Through a hidden underground passage", "B) By disguising as a devotee", "C) By bribing the temple guards", "D) By using a rope to climb over the temple walls"},
            {"A) The chief priest", "B) A visiting historian", "C) The temple security guard", "D) A group of tourists"}
    };

    private int[] correctAnswers = {1, 0, 2}; // Correct answer indexes

    private String[] clues = {
            "(Clue: The thief likely needed directions to find a secret chamber.)",
            "(Clue: A loose stone near the sanctum leads to a tunnel.)",
            "(Clue: His footprints match the ones near the secret tunnel.)"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);

        // Initialize UI components
        questionText = findViewById(R.id.questionText);
        radioGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        imageView = findViewById(R.id.crimeImage);
        imageView.setOnClickListener(v -> showZoomedImage());

        // Initialize stars
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        stars = new ImageView[]{star1, star2, star3};

        // Load first question
        loadQuestion();

        nextButton.setOnClickListener(view -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                showAlert("No Option Selected", "Please select an answer before proceeding.");
                return;
            }

            int selectedIndex = getSelectedIndex(selectedId);
            if (selectedIndex == correctAnswers[currentQuestionIndex]) {
                updateStar(currentQuestionIndex);  // ⭐ Update star when correct
                showCorrectAnswerDialog();
            } else {
                showAlert("Incorrect Answer", "That's not the correct choice. Try again!");
            }
        });
    }

    private void updateStar(int index) {
        if (index < stars.length) {
            stars[index].setImageResource(R.drawable.baseline_star_24);
        }
    }

    private void loadQuestion() {
        // Set new question & options
        questionText.setText(questions[currentQuestionIndex]);
        option1.setText(options[currentQuestionIndex][0]);
        option2.setText(options[currentQuestionIndex][1]);
        option3.setText(options[currentQuestionIndex][2]);
        option4.setText(options[currentQuestionIndex][3]);

        // Clear previous selection
        radioGroup.clearCheck();
    }

    private int getSelectedIndex(int selectedId) {
        if (selectedId == option1.getId()) return 0;
        if (selectedId == option2.getId()) return 1;
        if (selectedId == option3.getId()) return 2;
        if (selectedId == option4.getId()) return 3;
        return -1;
    }

    private void showCorrectAnswerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Correct Answer!")
                .setMessage("Well done! Let's move to the next question.")
                .setMessage(clues[currentQuestionIndex])
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

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Level2.this);
                        builder.setTitle("Case Solved!")
                                .setMessage("The half-burnt map revealed the thief's intent to find a hidden chamber, while the loose stone near the sanctum led to a secret escape tunnel. " +
                                        "Matching footprints exposed the temple security guard as the culprit.  " +
                                        "His insider knowledge and access made the theft possible, but his trail of clues ultimately led to his downfall. " +
                                        "\n\nGreat work, Detective!")
                                .setPositiveButton("Next Level", (dialog, which) -> {
                                    // Unlock Level 2
                                    SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("Level1Completed", true);
                                    editor.apply();

                                    // Move to Level 2
                                    startActivity(new Intent(Level2.this, first.class));
                                    finish();
                                })
                                .setNegativeButton("Exit", (dialog, which) -> {
                                    // Move to second.class instead of closing
                                    startActivity(new Intent(Level2.this, second.class));
                                    finish();
                                })
                                .setCancelable(false)
                                .show();
                    }, 1000); // Delay to allow animation effect
                }).start();
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

}