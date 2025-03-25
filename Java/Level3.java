package com.example.mysteryscript;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Level3 extends AppCompatActivity {
    private TextView questionText;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    private ImageButton nextButton;
    private ImageView star1, star2, star3;
    private ImageView[] stars;
    private int currentQuestionIndex = 0;

    private ImageView imageView;
    private String[] questions = {
            "The ship's cargo hold was found completely intact, with no signs of theft. What does this suggest?",
            "What was the last message from the ship’s captain?",
            "What does the word 'RETURN' carved onto the deck imply?"
    };

    private String[][] options = {
            {"A) The ship was attacked by sea creatures", "B) The crew intentionally sank the ship", "C) The ship never actually left Gujarat" , "D) The disappearance was not due to piracy"},
            {"A) 'We are under attack!'", "B) 'There's something wrong with the sea…'", "C) 'We will reach Mumbai soon.'", "D) 'Our compass is malfunctioning!'"},
            {"A) A crew member tried to warn future travelers", "B) The ship was part of a time loop", "C) The ocean itself was trying to send a message", "D) A secret code for a hidden treasure"}
    };
    private int[] correctAnswers = {3 , 1, 0}; // Correct answer indexes

    private String[] clues = {
            "(Clue: Pirates would have stolen the valuable spices and textiles.)",
            "(Clue: The message hints at an unnatural event.)",
            "(Clue: The message suggests urgency, possibly a warning.)"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level3);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(Level3.this);
                        builder.setTitle("Case Solved!")
                                .setMessage("The ship's distress call, the word 'RETURN' carved onto the deck, and the missing crew all pointed to a mysterious force at sea. " +
                                        "Investigators pieced together the ship's last moments, uncovering a shocking revelation about the vessel’s true fate. " +
                                        "\n\nExcellent work, Detective!")
                                .setPositiveButton("Main Menu", (dialog, which) -> {
                                    startActivity(new Intent(Level3.this, MainActivity.class));
                                    finish();
                                })
                                .setNegativeButton("Exit", (dialog, which) -> finish())
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