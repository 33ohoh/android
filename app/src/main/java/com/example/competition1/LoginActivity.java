package com.example.competition1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity  implements View.OnClickListener {


    private long backKeyPressedTime = 0;

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
                Intent registerIntent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.login_button:
                Intent loginIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(loginIntent);
                break;

            case R.id.finding_button:
                Intent findingIntent = new Intent(getApplicationContext(),FindingActivity.class);
                startActivity(findingIntent);
                break;

        }
    }

    @Override
    public void onBackPressed(){

        if(System.currentTimeMillis() > backKeyPressedTime + 1500 ){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this,"한번더 버튼을 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPressedTime + 1500){
            moveTaskToBack(true);
            finishAndRemoveTask();
            System.exit(0);
        }
    }

}

