package com.example.competition1.information;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.competition1.API.PestAPITask;
import com.example.competition1.R;
import com.example.competition1.report.LoadedData;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class SymptomInformationActivity extends AppCompatActivity {
    String cropName;
    String selectedSymptom="";
    NodeList symptomList;
    ArrayList<LoadedData> datas=new ArrayList<LoadedData>();
    androidx.gridlayout.widget.GridLayout gridLayout;;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_selection);
        Intent intent=getIntent();
        cropName=intent.getStringExtra("cropName");
        PestAPITask task=new PestAPITask();
        try{
            symptomList=task.execute(cropName,"symptom").get();
        }
        catch (Exception e){

        }
        gridLayout=(androidx.gridlayout.widget.GridLayout) findViewById(R.id.symptomGrid);
        TextView firstText=(TextView) findViewById(R.id.symptom_first_Text);
        TextView secondText=(TextView) findViewById(R.id.symptom_second_Text);
        TextView thirdText=(TextView) findViewById(R.id.symptom_third_Text);
        firstText.setText("질병");
        thirdText.setText("정보조회 버튼을 눌러주세요");
        //crop 이름으로 검색
        attachButton();
        AppCompatButton selectButton=(AppCompatButton) findViewById(R.id.symptomSaveButton);
        selectButton.setText("정보조회");
        if(symptomList.getLength()==0) {
            selectButton.setText("뒤로가기");
            firstText.setText(cropName);
            secondText.setText("와(과) 관련된");
            thirdText.setText("질병정보가 없습니다.");
        }
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(symptomList.getLength()==0)
                    finish();
                else {
                    if(selectedSymptom.equals("")){
                        Toast.makeText(getApplicationContext(), "질병을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent symptomIntent = new Intent(getApplicationContext(), InformationViewActivity.class);
                        symptomIntent.putExtra("sickKey", selectedSymptom);
                        for(int i=0;i<symptomList.getLength();i++){
                            if(getSymptomKey((Element) symptomList.item(i)).equals(selectedSymptom)) {
                                symptomIntent.putExtra("name",getSymptomName((Element)symptomList.item(i)));
                                symptomIntent.putExtra("image", getSymptomImage((Element) symptomList.item(i)));
                            }
                        }
                        symptomIntent.putExtra("type",1);
                        startActivity(symptomIntent);
                    }
                }
            }
        });

        TextInputEditText searchText=(TextInputEditText) findViewById(R.id.symptom_search);
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
                gridLayout.addView(datas.get(i).linearLayout);
        }
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < datas.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (datas.get(i).textView.getText().toString().contains(text))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    gridLayout.addView(datas.get(i).linearLayout);
                }
            }
        }
    }



    private void attachButton(){
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
            data.key=getSymptomKey((Element) node);
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
    private String getSymptomKey(Element element){
        NodeList nodeList=element.getElementsByTagName("sickKey").item(0).getChildNodes();
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
                selectedSymptom=data.key;
            }
            else {
                data.selected=false;
                data.cardView.setStrokeColor(Color.parseColor("#ffffff"));
                selectedSymptom="";
            }
        }
    }
}
