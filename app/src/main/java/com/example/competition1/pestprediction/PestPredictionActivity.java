package com.example.competition1.pestprediction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.competition1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PestPredictionActivity extends AppCompatActivity {
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_prediction);

        setMonthSpinner();                  //월을 선택하는 spinner값
        setPestInformationView();           //병해충 정보를 나타낼 recyclerView
        setPestInformationForEachCrop();    //작물별 병해충 정보
    }

    private int getCurrentMonth(){

        Date currentTime;
        SimpleDateFormat monthFormat;
        String currentMonth;
        int initialMonth;

        currentTime = Calendar.getInstance().getTime();                 //현재 월을 구함
        monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        currentMonth = monthFormat.format(currentTime);

        initialMonth = Integer.parseInt(currentMonth) - 1;

        return initialMonth;
    }

    private void setMonthSpinner(){

        String[] month;
        Spinner spnMonth;
        int currentMonth;

        month = getResources().getStringArray(R.array.month);   //예측정보를 불러올 월 목록(1,2,...,12)
        spnMonth = (Spinner)findViewById(R.id.spn_month);       //월을 표시한 스피너

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(        //월을 표시할 어뎁터
                this, R.layout.custom_dropdown_item, month);


        currentMonth = getCurrentMonth();
        spnMonth.setAdapter(adapter);
        spnMonth.setSelection(currentMonth);   //초기값은 현재 월로 표시
    }

    private void setPestInformationView(){
        RecyclerView recyclerView = findViewById(R.id.rv_prediction_result);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
    }

    private void setPestInformationForEachCrop(){
        for(int i=0;i<10;i++){
            PestsOnCropDTO itemData = new PestsOnCropDTO("옥수수"+i, "옥수수 벌레");
            adapter.addToPestsOnCropList(itemData);
        }
    }
}
