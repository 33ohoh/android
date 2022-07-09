package com.example.competition1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.competition1.reportActivity.ReportActivity;
import com.example.competition1.informationActivity.cropInformationActivity;

public class LoginActivity extends Activity  implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button register = findViewById(R.id.register_button);
        Button login = findViewById(R.id.login_button);
        Button finding = findViewById(R.id.finding_button);

        // 로그인, 회원가입, 아이디/비밀번호 찾기 리스너 달기
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        finding.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.register_button:
                Intent registerIntent = new Intent(getApplicationContext(),cropInformationActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.login_button:
                Intent loginIntent = new Intent(getApplicationContext(), ReportActivity.class);
                startActivity(loginIntent);
                break;

            case R.id.finding_button:
                Intent findingIntent = new Intent(getApplicationContext(),FindingActivity.class);
                startActivity(findingIntent);
                break;

        }
    }

}

