package com.example.mysteryscript;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class second extends AppCompatActivity {

    TextView stage1;

    TextView stage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        stage1 = findViewById(R.id.stageText);
        stage2 = findViewById(R.id.stageText2);
        stage1.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), first.class);
            startActivity(intent);
        });
        stage2.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), stage2.class);
            startActivity(intent);
        });


    }
}