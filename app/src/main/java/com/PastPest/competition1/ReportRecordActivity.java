package com.PastPest.competition1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReportRecordActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_record);

        Intent intent = getIntent();
        ReportHistory reportHistory  = (ReportHistory)intent.getSerializableExtra("reportHistory");

        TextView title = findViewById(R.id.tx_title); //reportHistory.getTitle();
        TextView date = findViewById(R.id.tx_date); //reportHistory.getAddress();
        TextView address = findViewById(R.id.tx_location);
        TextView crop = findViewById(R.id.tx_crop_name);
        TextView symptom = findViewById(R.id.tx_symptom);
        TextView pest = findViewById(R.id.tx_pest_name);
        TextView details = findViewById(R.id.tx_details);

        title.setText(reportHistory.getTitle());
        date.setText(reportHistory.getDate());
        address.setText(reportHistory.getAddress());
        crop.setText(reportHistory.getCropName());
        symptom.setText(reportHistory.getSymptom());
        pest.setText(reportHistory.getPestName());
        details.setText(reportHistory.getDetails());

    }
}
