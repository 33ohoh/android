package com.example.competition1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_damage_report_history);

        ListView listView = findViewById(R.id.listView);

        List<String> pestDamageReportHistory = new ArrayList<>();

        pestDamageReportHistory.add("사과");
        pestDamageReportHistory.add("바나나");
        pestDamageReportHistory.add("키위");
        pestDamageReportHistory.add("딸기");
        pestDamageReportHistory.add("수박");
        pestDamageReportHistory.add("귤");
        pestDamageReportHistory.add("참외");
        pestDamageReportHistory.add("딸기");
        pestDamageReportHistory.add("수박");
        pestDamageReportHistory.add("귤");
        pestDamageReportHistory.add("참외");
        pestDamageReportHistory.add("딸기");
        pestDamageReportHistory.add("수박");
        pestDamageReportHistory.add("귤");
        pestDamageReportHistory.add("참외");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, pestDamageReportHistory);

        listView.setAdapter(adapter);

        //MapView mapView = new MapView(this);

        //ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        //mapViewContainer.addView(mapView);
    }
}