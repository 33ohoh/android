package com.example.competition1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ListView reportHistoryView;
    private ListItemsAdapter reportHistoryAdapter;
    private ArrayList<ReportHistory> reportHistoryList;


    private List<ReportHistory> pestDamageReportHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_damage_report_history);

        setPestDamageReportHistory();

        reportHistoryAdapter = new ListItemsAdapter(getApplicationContext(), reportHistoryList);  // 어뎁터 객체 생성
        reportHistoryView.setAdapter(reportHistoryAdapter);   // 리스트뷰에 어뎁터 적용

        //MapView mapView = new MapView(this);

        //ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        //mapViewContainer.addView(mapView);
    }

    private void setPestDamageReportHistory(){
        reportHistoryView = (ListView) findViewById(R.id.list_items);
        reportHistoryList = new ArrayList<>();

        reportHistoryList.add(new ReportHistory("애호박", "2022-07-01", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("상추시들음병", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("늙은호박 병", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("옥수수 병리 문의", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("느티나무 잎벌레 문의입니다", "2022-06-29", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("오미자", "2022-06-29", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("사과잎 진단", "2022-06-28", "답변 대기중"));
    }
}