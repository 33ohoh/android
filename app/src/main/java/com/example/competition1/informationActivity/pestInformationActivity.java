package com.example.competition1.informationActivity;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.competition1.API.pestAPITask;
import com.example.competition1.R;
import com.example.competition1.reportActivity.PestSelectActivity;
import com.example.competition1.reportActivity.loadedData;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class pestInformationActivity extends AppCompatActivity {
    String cropName;
    String selectedPest;
    NodeList pestList;
    ArrayList<loadedData> datas=new ArrayList<loadedData>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_selection);
        Intent intent=getIntent();
        cropName=intent.getStringExtra("cropName");
        pestAPITask task=new pestAPITask();
        try{
            pestList=task.execute(cropName,"pest").get();
        }
        catch (Exception e){

        }
        TextView firstText=(TextView) findViewById(R.id.pest_first_Text);
        TextView secondText=(TextView) findViewById(R.id.pest_second_Text);
        TextView thirdText=(TextView) findViewById(R.id.pest_third_Text);
        firstText.setText("해충");
        thirdText.setText("정보조회 버튼을 눌러주세요");
        //crop 이름으로 검색
        attachButton();
        AppCompatButton selectButton=(AppCompatButton) findViewById(R.id.pestSaveButton);
        selectButton.setText("정보조회");
        if(pestList.getLength()==0) {
            selectButton.setText("뒤로가기");
            firstText.setText(cropName);
            secondText.setText("와(과) 관련된");
            thirdText.setText("해충정보가 없습니다.");
        }
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pestList.getLength()==0)
                    finish();
                else {
                    if(selectedPest.equals("")){
                        Toast.makeText(getApplicationContext(), "해충을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent pestIntent = new Intent(getApplicationContext(),informationViewActivity.class);
                        pestIntent.putExtra("selectedPest", selectedPest);
                        startActivity(pestIntent);
                    }
                }
            }
        });
    }
    private void attachButton(){
        androidx.gridlayout.widget.GridLayout gridLayout=(androidx.gridlayout.widget.GridLayout) findViewById(R.id.pestGrid);
        final float scale = gridLayout.getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (120 * scale + 0.5f);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = ((DisplayMetrics) metrics).widthPixels * 1/3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixels, pixels);
        ViewGroup.LayoutParams buttonParams=new ViewGroup.LayoutParams(widthPixels,(int) (100 * scale + 0.5f));
        ViewGroup.LayoutParams textParams=new ViewGroup.LayoutParams(widthPixels,(int) (20 * scale + 0.5f));
        for(int i=0;i<pestList.getLength();i++) {
            Node node=pestList.item(i);
            datas.add(i,new loadedData());
            loadedData data=datas.get(i);
            data.key=getPestKey((Element) node);
            data.linearLayout=new LinearLayout(this);
            data.linearLayout.setMinimumWidth(widthPixels);
            params.setMargins(0, (int) (10 * scale + 0.5f), 0, (int) (10 * scale + 0.5f));
            data.linearLayout.setOrientation(LinearLayout.VERTICAL);
            data.linearLayout.setLayoutParams(params);
            data.cardView=new com.google.android.material.card.MaterialCardView(this);
            data.cardView.setLayoutParams(buttonParams);
            data.cardView.setRadius(80);
            data.cardView.setStrokeWidth(20);
            data.imageView=new ImageView(this);
            data.imageView.setClipToOutline(true);
            data.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            data.imageView.setOnClickListener(new imageClickListener(data));
            Glide.with(this).load(getPestImage((Element) node)).into(data.imageView);
            data.textView = new TextView(this);
            data.textView.setText(getPestName((Element) node));
            data.textView.setGravity(Gravity.CENTER);
            data.cardView.setLayoutParams(buttonParams);
            data.textView.setLayoutParams(textParams);
            data.cardView.addView(data.imageView);
            data.linearLayout.addView(data.cardView, 0);
            data.linearLayout.addView(data.textView, 1);
            gridLayout.addView(data.linearLayout,0);
        }
    }

    private String getPestName(Element element){
        NodeList nodeList=element.getElementsByTagName("insectKorName").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }
    private String getPestImage(Element element){
        NodeList nodeList=element.getElementsByTagName("thumbImg").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }
    private String getPestKey(Element element){
        NodeList nodeList=element.getElementsByTagName("insectKey").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }
    public class imageClickListener implements View.OnClickListener {
        loadedData data;

        public imageClickListener(loadedData data) {
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
                selectedPest=data.key;
            }
            else {
                data.selected=false;
                data.cardView.setStrokeColor(Color.parseColor("#ffffff"));
                selectedPest="";
            }
        }
    }
}
