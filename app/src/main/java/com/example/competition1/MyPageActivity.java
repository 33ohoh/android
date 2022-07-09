package com.example.competition1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.competition1.pestprediction.PestPredictionActivity;

public class MyPageActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Button reportHistory = findViewById(R.id.report_history);
        Button password = findViewById(R.id.password);
        Button logout = findViewById(R.id.logout);
        Button withdrawal = findViewById(R.id.withdrawal);

        reportHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), ReportHistoryActivity.class);
                startActivity(registerIntent);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), PasswordResetActivity.class);
                startActivity(registerIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(registerIntent);
            }
        });

        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
