package com.example.competition1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FindingIdActivity extends AppCompatActivity {

    @Override
   public void onCreate(Bundle saveInstanceState){
       super.onCreate(saveInstanceState);
       setContentView(R.layout.activity_finding_id);

       Toolbar toolbar = findViewById(R.id.finding_id_toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
       getSupportActionBar().setTitle(""); // 툴바 제목 설정
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);  //이미지 설정
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
