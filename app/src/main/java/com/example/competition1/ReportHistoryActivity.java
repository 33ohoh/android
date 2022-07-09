package com.example.competition1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.competition1.fragment.FragmentDeclaration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReportHistoryActivity extends AppCompatActivity {
    private ListView reportHistoryView;
    private ReportHistoryAdapter reportHistoryAdapter;
    private ArrayList<ReportHistory> reportHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        setReportHistoryList();  //신고내역 데이터 세팅

        reportHistoryAdapter = new ReportHistoryAdapter(getApplicationContext(), reportHistoryList);  //어뎁터
        reportHistoryView.setAdapter(reportHistoryAdapter);                                           //신고내역 뷰에 어뎁터 연결
    }

    private void setReportHistoryList(){
        reportHistoryView = (ListView) findViewById(R.id.lv_report_history);  //신고 내역 리스트가 보여지는 뷰
        reportHistoryList = new ArrayList<>();                                  //신고 내역 리스트

        reportHistoryList.add(new ReportHistory("애호박", "2022-07-01", "답변 대기중"));         //신고내역 리스트에 들어갈 데이터
        reportHistoryList.add(new ReportHistory("상추시들음병", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("늙은호박 병", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("옥수수 병리 문의", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("느티나무 잎벌레 문의입니다", "2022-06-29", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("오미자", "2022-06-29", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("사과잎 진단", "2022-06-28", "답변 대기중"));
    }
}