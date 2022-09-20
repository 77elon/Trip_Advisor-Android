package com.dhnns.navermaptest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Map_Test.class);
            startActivity(intent);
        });
    }
}