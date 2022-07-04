package com.example.competition1.reportActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.competition1.R;

public class ReportActivity extends AppCompatActivity {
    ImageView selectedImage;
    String detailAddress="";
    String loadAddress="현재위치";
    String cropName="";
    String symptomName="";
    String pestName="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        EditText editText=(EditText) findViewById(R.id.detailText);
        selectedImage=(ImageView)findViewById(R.id.selectedImage);
        AppCompatButton locationBtn=(AppCompatButton) findViewById(R.id.locationButton);
        AppCompatButton cropBtn=(AppCompatButton) findViewById(R.id.cropButton);
        AppCompatButton symptomBtn=(AppCompatButton) findViewById(R.id.symptomButton);
        AppCompatButton pestBtn=(AppCompatButton) findViewById(R.id.pestButton);
        AppCompatButton imageBtn=(AppCompatButton) findViewById(R.id.imageButton);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), LocationSelectActivity.class);
                //intent.putExtra("key","12345".toString());
                intent.putExtra("detailAddress",detailAddress);
                intent.putExtra("loadAddress",loadAddress);
                startActivityForResult(intent,1);
            }
        });

        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), CropSelectActivity.class);
                startActivityForResult(intent,2);
            }
        });

        symptomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SymptomSelectActivity.class);
                startActivityForResult(intent,3);
            }
        });

        pestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), PestSelectActivity.class);
                startActivityForResult(intent,4);
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 5);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==5){
            if(resultCode==RESULT_OK){
                Glide.with(getApplicationContext()).load(data.getData()).override(100,100).into(selectedImage);
            }
        }
        else if(requestCode==1){
            if(resultCode==RESULT_OK) {
                loadAddress = data.getStringExtra("loadAddress");
                detailAddress = data.getStringExtra("detailAddress");
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==2){
            if(resultCode==RESULT_OK) {
                cropName = data.getStringExtra("cropName");
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==3){
            if(resultCode==RESULT_OK) {
                symptomName = data.getStringExtra("symptomName");
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==4){
            if(resultCode==RESULT_OK) {
                pestName = data.getStringExtra("pestName");
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
    }
}
