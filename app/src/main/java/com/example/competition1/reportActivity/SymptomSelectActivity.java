package com.example.competition1.reportActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.competition1.API.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.competition1.R;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class SymptomSelectActivity extends AppCompatActivity {
    String cropName;
    String selectedSymptom;
    NodeList symptomList;
    ArrayList<LoadedData> datas=new ArrayList<LoadedData>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_selection);
        Intent intent=getIntent();
        cropName=intent.getStringExtra("cropName");
        selectedSymptom=intent.getStringExtra("selectedSymptom");
        System.out.println(selectedSymptom);
        PestAPITask task=new PestAPITask();
        try{
            symptomList=task.execute(cropName,"symptom").get();
        }
        catch (Exception e){

        }
        //crop 이름으로 검색
        attachButton();
        AppCompatButton selectButton=(AppCompatButton) findViewById(R.id.symptomSaveButton);
        if(symptomList.getLength()==0) {
            selectButton.setText("뒤로가기");
            TextView firstText=(TextView) findViewById(R.id.symptom_first_Text);
            firstText.setText(cropName);
            TextView secondText=(TextView) findViewById(R.id.symptom_second_Text);
            secondText.setText("와(과) 관련된");
            TextView thirdText=(TextView) findViewById(R.id.symptom_third_Text);
            thirdText.setText("증상정보가 없습니다.");
        }
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent symptomIntent=new Intent();
                symptomIntent.putExtra("selectedSymptom",selectedSymptom);
                setResult(RESULT_OK,symptomIntent);
                finish();
            }
        });
    }



    private void attachButton(){
        androidx.gridlayout.widget.GridLayout gridLayout=(androidx.gridlayout.widget.GridLayout) findViewById(R.id.symptomGrid);
        final float scale = gridLayout.getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (120 * scale + 0.5f);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = ((DisplayMetrics) metrics).widthPixels * 1/3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixels, pixels);
        ViewGroup.LayoutParams buttonParams=new ViewGroup.LayoutParams(widthPixels,(int) (100 * scale + 0.5f));
        ViewGroup.LayoutParams textParams=new ViewGroup.LayoutParams(widthPixels,(int) (20 * scale + 0.5f));
        for(int i=0;i<symptomList.getLength();i++) {
            Node node=symptomList.item(i);
            datas.add(i,new LoadedData());
            LoadedData data=datas.get(i);
            data.linearLayout=new LinearLayout(this);
            data.linearLayout.setMinimumWidth(widthPixels);
            params.setMargins(0, (int) (10 * scale + 0.5f), 0, (int) (10 * scale + 0.5f));
            data.linearLayout.setLayoutParams(params);
            data.linearLayout.setOrientation(LinearLayout.VERTICAL);
            data.cardView=new com.google.android.material.card.MaterialCardView(this);
            data.cardView.setLayoutParams(buttonParams);
            data.cardView.setRadius(80);
            data.cardView.setStrokeWidth(20);
            data.cardView.setStrokeColor(null);
            data.imageView=new ImageView(this);
            data.textView = new TextView(this);
            data.imageView.setClipToOutline(true);
            data.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            data.textView.setText(getSymptomName((Element) node));
            if(data.textView.getText().toString().equals(selectedSymptom)){
                data.cardView.setStrokeColor(Color.parseColor("#04CF5C"));
                data.selected=true;
            }
            else
                data.cardView.setStrokeColor(null);
            data.imageView.setOnClickListener(new imageClickListener(data));
            Glide.with(this).load(getSymptomImage((Element) node)).into(data.imageView);
            data.textView.setGravity(Gravity.CENTER);
            data.textView.setLayoutParams(textParams);
            data.cardView.addView(data.imageView);
            data.linearLayout.addView(data.cardView, 0);
            data.linearLayout.addView(data.textView, 1);
            gridLayout.addView(data.linearLayout, 0);
        }
    }

    private String getSymptomName(Element element){
        NodeList nodeList=element.getElementsByTagName("sickNameKor").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }
    private String getSymptomImage(Element element){
        NodeList nodeList=element.getElementsByTagName("thumbImg").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public class imageClickListener implements View.OnClickListener {
        LoadedData data;

        public imageClickListener(LoadedData data) {
            this.data=data;
        }

        @Override
        public void onClick(View view) {
            if(data.selected==false) {
                for(int i=0;i<datas.size();i++){
                    if(datas.get(i).selected==true){
                        datas.get(i).cardView.setStrokeColor(Color.parseColor("#ffffff"));
                        datas.get(i).selected=false;
                    }
                }
                data.selected=true;
                data.cardView.setStrokeColor(Color.parseColor("#04CF5C"));
                selectedSymptom=data.textView.getText().toString();
            }
            else {
                data.selected=false;
                data.cardView.setStrokeColor(Color.parseColor("#ffffff"));
                selectedSymptom="";
            }
        }
    }
}
