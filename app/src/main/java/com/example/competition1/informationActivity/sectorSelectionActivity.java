package com.example.competition1.informationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.competition1.R;

public class sectorSelectionActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector_selection);
        AppCompatButton symptomButton=(AppCompatButton) findViewById(R.id.symptomInformationButton);
        AppCompatButton pestButton=(AppCompatButton) findViewById(R.id.pestInformationButton);
        String selectedCrop=getIntent().getStringExtra("cropName");
        symptomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),symptomInformationActivity.class);
                intent.putExtra("cropName",selectedCrop);
                startActivity(intent);
            }
        });
        pestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),pestInformationActivity.class);
                intent.putExtra("cropName",selectedCrop);
                startActivity(intent);
            }
        });
    }
}
