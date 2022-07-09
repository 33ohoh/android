package com.example.competition1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
                showLogoutDialog();
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

    private void showLogoutDialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(this)
                .setTitle("로그아웃")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog alertDialog = msgBuilder.create();
        alertDialog.show();
    }

    private void showWithdrawalDialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(this)
                .setTitle("회원탈퇴")
                .setMessage("탈퇴하시겠습니까?\n탈퇴시 어플에서 제공하는 서비스를 더 이상 이용하실 수 없습니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog alertDialog = msgBuilder.create();
        alertDialog.show();
    }
}
