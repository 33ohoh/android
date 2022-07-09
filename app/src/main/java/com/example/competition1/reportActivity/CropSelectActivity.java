package com.example.competition1.reportActivity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.competition1.R;

import java.util.ArrayList;

public class CropSelectActivity extends Activity {
    String selectedCrop;
    ArrayList<CropData> datas =new ArrayList<CropData>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_selection);

        Intent intent=getIntent();
        selectedCrop =intent.getStringExtra("cropName");
        loadViews();
        for(int i=0;i<20;i++){
            if(datas.get(i).textView.getText().toString().equals(selectedCrop)) {
                datas.get(i).selected=true;
                datas.get(i).button.setStrokeColor(Color.parseColor("#04CF5C"));
            }
            datas.get(i).button.setOnClickListener(new cropClickListener(datas.get(i)));
        }

        AppCompatButton saveButton=(AppCompatButton) findViewById(R.id.cropSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cropIntent=new Intent();
                cropIntent.putExtra("cropName",selectedCrop);
                setResult(RESULT_OK,cropIntent);
                finish();
            }
        });

    }

    private void loadViews(){
        for(int i=0;i<20;i++)
            datas.add(new CropData());
        datas.get(0).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button1);
        datas.get(0).textView=(TextView) findViewById(R.id.text1);
        datas.get(1).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button2);
        datas.get(1).textView=(TextView) findViewById(R.id.text2);
        datas.get(2).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button3);
        datas.get(2).textView=(TextView) findViewById(R.id.text3);
        datas.get(3).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button4);
        datas.get(3).textView=(TextView) findViewById(R.id.text4);
        datas.get(4).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button5);
        datas.get(4).textView=(TextView) findViewById(R.id.text5);
        datas.get(5).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button6);
        datas.get(5).textView=(TextView) findViewById(R.id.text6);
        datas.get(6).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button7);
        datas.get(6).textView=(TextView) findViewById(R.id.text7);
        datas.get(7).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button8);
        datas.get(7).textView=(TextView) findViewById(R.id.text8);
        datas.get(8).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button9);
        datas.get(8).textView=(TextView) findViewById(R.id.text9);
        datas.get(9).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button10);
        datas.get(9).textView=(TextView) findViewById(R.id.text10);
        datas.get(10).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button11);
        datas.get(10).textView=(TextView) findViewById(R.id.text11);
        datas.get(11).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button12);
        datas.get(11).textView=(TextView) findViewById(R.id.text12);
        datas.get(12).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button13);
        datas.get(12).textView=(TextView) findViewById(R.id.text13);
        datas.get(13).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button14);
        datas.get(13).textView=(TextView) findViewById(R.id.text14);
        datas.get(14).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button15);
        datas.get(14).textView=(TextView) findViewById(R.id.text15);
        datas.get(15).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button16);
        datas.get(15).textView=(TextView) findViewById(R.id.text16);
        datas.get(16).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button17);
        datas.get(16).textView=(TextView) findViewById(R.id.text17);
        datas.get(17).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button18);
        datas.get(17).textView=(TextView) findViewById(R.id.text18);
        datas.get(18).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button19);
        datas.get(18).textView=(TextView) findViewById(R.id.text19);
        datas.get(19).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button20);
        datas.get(19).textView=(TextView) findViewById(R.id.text20);
    }

    public class cropClickListener implements View.OnClickListener {
        CropData data;

        public cropClickListener(CropData data) {
            this.data=data;
        }

        @Override
        public void onClick(View view) {
            if(data.selected==false) {
                for(int i=0;i<datas.size();i++){
                    if(datas.get(i).selected==true){
                        datas.get(i).button.setStrokeColor(Color.parseColor("#ffffff"));
                        datas.get(i).selected=false;
                    }
                }
                data.selected=true;
                data.button.setStrokeColor(Color.parseColor("#04CF5C"));
                selectedCrop =data.textView.getText().toString();
            }
            else {
                data.selected=false;
                data.button.setStrokeColor(Color.parseColor("#ffffff"));
                selectedCrop ="";
            }
        }
    }
}
