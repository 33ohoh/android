package com.PastPest.competition1.report;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.gridlayout.widget.GridLayout;

import com.PastPest.competition1.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CropSelectActivity extends Activity {
    String selectedCrop;
    ArrayList<CropData> datas =new ArrayList<CropData>();
    GridLayout gridLayout;
    LinearLayout emptyLayout1;
    LinearLayout emptyLayout2;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_information);

        Intent intent=getIntent();
        selectedCrop =intent.getStringExtra("cropName");
        loadViews();
        for(int i=0;i<21;i++){
            if(datas.get(i).textView.getText().toString().equals(selectedCrop)) {
                datas.get(i).selected=true;
                datas.get(i).button.setStrokeColor(Color.parseColor("#04CF5C"));
            }
            datas.get(i).button.setOnClickListener(new cropClickListener(datas.get(i)));
        }
        gridLayout=(GridLayout)findViewById(R.id.cropGrid);
        emptyLayout1=(LinearLayout)findViewById(R.id.emptylayout1);
        emptyLayout2=(LinearLayout)findViewById(R.id.emptylayout2);
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
        TextInputEditText searchText=(TextInputEditText) findViewById(R.id.crop_search);
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String text = searchText.getText().toString();
                search(text);
            }
        });
    }

    private void search(String text){
        gridLayout.removeAllViews();
        if (text.length() == 0) {
            for(int i = 0;i < datas.size(); i++)
                gridLayout.addView(datas.get(i).layout);
        }
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            int count=0;
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < datas.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (datas.get(i).textView.getText().toString().contains(text))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    gridLayout.addView(datas.get(i).layout);
                    count++;
                }
            }
            if(count==2)
                gridLayout.addView(emptyLayout1);
            else if(count==1){
                gridLayout.addView(emptyLayout1);
                gridLayout.addView(emptyLayout2);
            }
        }
    }

    private void loadViews(){
        for(int i=0;i<21;i++)
            datas.add(new CropData());
        datas.get(0).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button1);
        datas.get(0).textView=(TextView) findViewById(R.id.text1);
        datas.get(1).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button0);
        datas.get(1).textView=(TextView) findViewById(R.id.text0);
        datas.get(2).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button2);
        datas.get(2).textView=(TextView) findViewById(R.id.text2);
        datas.get(3).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button3);
        datas.get(3).textView=(TextView) findViewById(R.id.text3);
        datas.get(4).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button4);
        datas.get(4).textView=(TextView) findViewById(R.id.text4);
        datas.get(5).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button5);
        datas.get(5).textView=(TextView) findViewById(R.id.text5);
        datas.get(6).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button6);
        datas.get(6).textView=(TextView) findViewById(R.id.text6);
        datas.get(7).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button7);
        datas.get(7).textView=(TextView) findViewById(R.id.text7);
        datas.get(8).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button8);
        datas.get(8).textView=(TextView) findViewById(R.id.text8);
        datas.get(9).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button9);
        datas.get(9).textView=(TextView) findViewById(R.id.text9);
        datas.get(10).button=(com.google.android.material.card.MaterialCardView)findViewById(R.id.button10);
        datas.get(10).textView=(TextView) findViewById(R.id.text10);
        datas.get(11).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button11);
        datas.get(11).textView=(TextView) findViewById(R.id.text11);
        datas.get(12).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button12);
        datas.get(12).textView=(TextView) findViewById(R.id.text12);
        datas.get(13).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button13);
        datas.get(13).textView=(TextView) findViewById(R.id.text13);
        datas.get(14).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button14);
        datas.get(14).textView=(TextView) findViewById(R.id.text14);
        datas.get(15).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button15);
        datas.get(15).textView=(TextView) findViewById(R.id.text15);
        datas.get(16).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button16);
        datas.get(16).textView=(TextView) findViewById(R.id.text16);
        datas.get(17).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button17);
        datas.get(17).textView=(TextView) findViewById(R.id.text17);
        datas.get(18).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button18);
        datas.get(18).textView=(TextView) findViewById(R.id.text18);
        datas.get(19).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button19);
        datas.get(19).textView=(TextView) findViewById(R.id.text19);
        datas.get(20).button=(com.google.android.material.card.MaterialCardView) findViewById(R.id.button20);
        datas.get(20).textView=(TextView) findViewById(R.id.text20);
        datas.get(0).layout=(LinearLayout)findViewById(R.id.layout1);
        datas.get(1).layout=(LinearLayout)findViewById(R.id.layout0);
        datas.get(2).layout=(LinearLayout)findViewById(R.id.layout2);
        datas.get(3).layout=(LinearLayout)findViewById(R.id.layout3);
        datas.get(4).layout=(LinearLayout)findViewById(R.id.layout4);
        datas.get(5).layout=(LinearLayout)findViewById(R.id.layout5);
        datas.get(6).layout=(LinearLayout)findViewById(R.id.layout6);
        datas.get(7).layout=(LinearLayout)findViewById(R.id.layout7);
        datas.get(8).layout=(LinearLayout)findViewById(R.id.layout8);
        datas.get(9).layout=(LinearLayout)findViewById(R.id.layout9);
        datas.get(10).layout=(LinearLayout)findViewById(R.id.layout10);
        datas.get(11).layout=(LinearLayout)findViewById(R.id.layout11);
        datas.get(12).layout=(LinearLayout)findViewById(R.id.layout12);
        datas.get(13).layout=(LinearLayout)findViewById(R.id.layout13);
        datas.get(14).layout=(LinearLayout)findViewById(R.id.layout14);
        datas.get(15).layout=(LinearLayout)findViewById(R.id.layout15);
        datas.get(16).layout=(LinearLayout)findViewById(R.id.layout16);
        datas.get(17).layout=(LinearLayout)findViewById(R.id.layout17);
        datas.get(18).layout=(LinearLayout)findViewById(R.id.layout18);
        datas.get(19).layout=(LinearLayout)findViewById(R.id.layout19);
        datas.get(20).layout=(LinearLayout)findViewById(R.id.layout20);
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
