package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //작물 목록
        Spinner cropSpinner = (Spinner)findViewById(R.id.spinner_crop);
        ArrayAdapter cropAdapter = ArrayAdapter.createFromResource(
                this, R.array.crop, android.R.layout.simple_spinner_item);

        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropSpinner.setAdapter(cropAdapter);
    }
}