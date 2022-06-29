package com.example.competition1;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onButton1Clicked(View v){
        Toast.makeText(this,"확인1 버튼이 눌렸어요",Toast.LENGTH_LONG).show();
    }
}