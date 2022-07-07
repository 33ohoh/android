package com.example.competition1.pestprediction;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.competition1.R;
import com.example.competition1.ReportHistory;
import com.example.competition1.ReportHistoryAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(     //월을 표시할 어뎁터
                this, R.layout.custom_dropdown_item, month);


        currentMonth = getCurrentMonth();
        spnMonth.setAdapter(adapter);
        spnMonth.setSelection(currentMonth);   //초기값은 현재 월로 표시
    }

    private void setPestInformationView(){

        RecyclerView recyclerView = findViewById(R.id.rv_prediction_result);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter(getApplicationContext());  //각 작물에 대한 병해충 리스트가 보여지는 뷰);
        recyclerView.setAdapter(adapter);
    }

    private void setPestInformationForEachCrop(){
        adapter.addToPestsOnCropList(new PestsOnCropDTO("논벼", "", "먹노린재,잎도열병", "벼멸구,벼물바구미,줄무늬잎마름병,혹명나방,흰등멸구"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("옥수수", "", "열대거세미나방", "멸강나방"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("콩", "","파밤나방", ""));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("배", "과수화상병", "갈색날개매미충,검은별무늬병", "복숭아심식나방"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("복숭아", "", "갈색날개매미충,복숭아심식나방,세균성구멍병", "복숭아순나방,잿빛무늬병,탄저병"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("사과", "가지검은마름병,과수화상병", "갈색날개매미충,복숭아심식나방", "복숭아잎말이나방,사과무늬잎말이나방,애모무늬잎말이나방,탄저병"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("포도", "", "꽃매미", "새눈무늬병,탄저병"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("고추", "", "담배나방,역병,탄저병,파밤나방", "목화진딧물"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("수수", "", "", "멸강나방"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("조", "", "", "멸강나방"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("감", "", "", "감꼭지나방"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("배", "", "", "애모무늬잎말이나방"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("가지", "", "", "온실가루이"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("무", "", "", "무름병, 뿌리혹병, 무사마귀병"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("배추", "", "", "무름병"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("수박", "", "", "덩굴마름병"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("오이", "", "", "꽃노랑총채벌레"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("참외", "", "", "덩굴마름병"));
        adapter.addToPestsOnCropList(new PestsOnCropDTO("토마토", "", "", "담배가루이, 반점위조바이러스, 온실가루이, 황화잎말림바이러스"));
    }
}
