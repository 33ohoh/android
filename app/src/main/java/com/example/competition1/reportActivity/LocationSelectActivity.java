package com.example.competition1.reportActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.competition1.R;

public class LocationSelectActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        Intent intent=getIntent();
        String loadAddress=intent.getStringExtra("loadAddress");
        String detailAddress=intent.getStringExtra("detailAddress");
        TextView textView=(TextView) findViewById(R.id.address_text);
        textView.setText(loadAddress);
        EditText editText=(EditText) findViewById(R.id.detail_address);
        editText.setText(detailAddress);

        AppCompatButton saveBtn=(AppCompatButton) findViewById(R.id.locationSaveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent=new Intent();
                String detail_Address=editText.getText().toString();
                String load_Address=textView.getText().toString();
                locationIntent.putExtra("detailAddress",detail_Address);
                locationIntent.putExtra("loadAddress",load_Address);
                setResult(RESULT_OK,locationIntent);
                finish();
            }
        });

    }
}

