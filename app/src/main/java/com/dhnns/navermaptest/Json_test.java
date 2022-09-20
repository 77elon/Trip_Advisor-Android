package com.dhnns.navermaptest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Json_test extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_test);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}