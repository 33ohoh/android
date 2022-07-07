package com.example.competition1.pestadvisory;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.competition1.R;

import java.util.ArrayList;

public class PestAdvisoryActivity extends AppCompatActivity {
    private ListView pestAdvisoryView;
    private Adapter adapter;
    private ArrayList<PestAdvisory> pestAdvisoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_forecast);

        setPestAdvisoryList();

        adapter = new Adapter(getApplicationContext(), pestAdvisoryList);
        pestAdvisoryView.setAdapter(adapter);
    }

    private void setPestAdvisoryList(){
        pestAdvisoryView = (ListView) findViewById(R.id.lv_pest_advisory);     //이달의 해충 방제 정보 리스트가 보여지는 뷰
        pestAdvisoryList = new ArrayList<>();                                  //헤충 방제 정보 리스트

        pestAdvisoryList.add(new PestAdvisory("먹노린재", "",
                "성충의 방제 적기는 겨울을 지난 성충의 이동 최성기인 " +
                "6월 하순 ∼7월 상순으로 주변 논두렁이나 배수로 등 서식처가 될만한 곳까지 약제를 " +
                "살포하면 방제 효과를 높일 수 있습니다."));
        pestAdvisoryList.add(new PestAdvisory("담배나방·파밤나방(고추,콩등)", "",
                "해마다 발생하여 피해를 주는 해충으로 주로 장마가 끝나고 기온이 높아지면 " +
                        "담배나방, 파밤나방 등의 발생이 증가할 우려가 있음"));
        pestAdvisoryList.add(new PestAdvisory("복숭아심식나방 병", "",
                "제1회 성충은 6월 상순에서 8월 상순 사이에 발생하고, 제2회 성충은 " +
                        "7월 하순부터 9월 상순에 발생하며, 발생최성기는 8월 중순경입니다."));
    }
}
