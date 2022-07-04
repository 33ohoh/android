package com.example.competition1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CurrentSituationActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_situation);

        Toolbar toolbar = findViewById(R.id.finding_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setTitle(""); // 툴바 제목 설정
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);  //이미지 설정


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.finding_id_button:
                Intent registerIntent = new Intent(getApplicationContext(), FindingIdActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.finding_password_button:
                Intent loginIntent = new Intent(getApplicationContext(), FindingPwActivity.class);
                startActivity(loginIntent);
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
