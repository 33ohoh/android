package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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

        FrameLayout predictionResult = (FrameLayout)findViewById(R.id.prediction_result);
        Button predictionResultInquiry = (Button)findViewById(R.id.btn_prediction_result_inquiry);
        predictionResultInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predictionResult.setVisibility(View.VISIBLE);
            }
        });
    }
}