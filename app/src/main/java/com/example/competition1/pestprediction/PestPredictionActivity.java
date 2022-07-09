package com.example.competition1.pestprediction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.competition1.R;
import com.example.competition1.utility.Constants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PestPredictionActivity extends AppCompatActivity {
    private Adapter adapter;
    private ArrayList<PestsOnCropDTO> foodResourcesList;
    private ArrayList<PestsOnCropDTO> vegetableList;
    private ArrayList<PestsOnCropDTO> fruitTreeList;
    private String url = "http://ec2-43-200-8-163.ap-northeast-2.compute.amazonaws.com:3000";
    private String httpResult="엥";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_prediction);

        setMonthSpinner();                  //월을 선택하는 spinner값

        Button btnInquiry = findViewById(R.id.btn_prediction_result_inquiry);  //결과 조회 버튼
        btnInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout resultLayout = findViewById(R.id.ll_prediction_result);   //조회 결과를 표시할 레이아웃
                resultLayout.setVisibility(View.VISIBLE);

                switch (Integer.parseInt(getMonth())){
                    case 7:
                        setPestInformationForEachCrop1();    //작물별 병해충 정보를 어뎁터에 담음
                        setCropList();                      //작물목록
                        break;
                    case 10:
                        setPestInformationForEachCrop2();    //작물별 병해충 정보를 어뎁터에 담음
                        setCropList();                      //작물목록
                        break;
                }
            }
        });

    }

    private String getMonth(){
        Spinner month = (Spinner)findViewById(R.id.spn_month);
        return month.getSelectedItem().toString();
    }

    private void setCropList(){
        View foodResourcesView = findViewById(R.id.food_resources);
        View vegetableView = findViewById(R.id.vegetable);
        View fruitTreeView = findViewById(R.id.fruit_tree);

        ((TextView)foodResourcesView.findViewById(R.id.tx_crop_type)).setText(Constants.FOOD_RESOURCES);
        setPestInformationView(foodResourcesView.findViewById(R.id.rv_prediction_result));           //병해충 정보를 나타낼 recyclerView
        addToAddapter(foodResourcesList);

        ((TextView)vegetableView.findViewById(R.id.tx_crop_type)).setText(Constants.VEGETABLE);
        setPestInformationView(vegetableView.findViewById(R.id.rv_prediction_result));           //병해충 정보를 나타낼 recyclerView
        addToAddapter(vegetableList);

        ((TextView)fruitTreeView.findViewById(R.id.tx_crop_type)).setText(Constants.FRUIT_TREE);
        setPestInformationView(fruitTreeView.findViewById(R.id.rv_prediction_result));           //병해충 정보를 나타낼 recyclerView
        addToAddapter(fruitTreeList);

        setNoResultMessage(foodResourcesList, (LinearLayout) foodResourcesView.findViewById(R.id.ll_no_result), Constants.FOOD_RESOURCES);
        setNoResultMessage(vegetableList, (LinearLayout) vegetableView.findViewById(R.id.ll_no_result), Constants.VEGETABLE);
        setNoResultMessage(fruitTreeList, (LinearLayout) fruitTreeView.findViewById(R.id.ll_no_result), Constants.FRUIT_TREE);
    }

    private void setNoResultMessage(ArrayList<PestsOnCropDTO> pestList, LinearLayout noResultMessage, String cropType){
        if(pestList.size() == 0){
            ((TextView)noResultMessage.findViewById(R.id.tx_crop_type_with_no_result)).setText(cropType);
            noResultMessage.setVisibility(View.VISIBLE);
        }
        else{
            noResultMessage.setVisibility(View.GONE);
        }
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

    private void setPestInformationView(RecyclerView recyclerView){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter(getApplicationContext());  //각 작물에 대한 병해충 리스트가 보여지는 뷰);
        recyclerView.setAdapter(adapter);
    }

    private void addToAddapter(ArrayList<PestsOnCropDTO> pestList){
        int length = pestList.size();

        for(int index = 0; index < length; index++){
            adapter.addToPestsOnCropList(pestList.get(index));
        }
    }

    private void setPestInformationForEachCrop1() {
        foodResourcesList = new ArrayList<>();
        fruitTreeList = new ArrayList<>();
        vegetableList = new ArrayList<>();

        foodResourcesList.add(new PestsOnCropDTO("식량작물", "논벼", "", "먹노린재,잎도열병", "벼멸구,벼물바구미,줄무늬잎마름병,혹명나방,흰등멸구"));
        foodResourcesList.add(new PestsOnCropDTO("식량작물", "옥수수", "", "열대거세미나방", "멸강나방"));
        foodResourcesList.add(new PestsOnCropDTO("식량작물", "콩", "", "파밤나방", ""));
        foodResourcesList.add(new PestsOnCropDTO("식량작물", "수수", "", "", "멸강나방"));
        foodResourcesList.add(new PestsOnCropDTO("식량작물", "조", "", "", "멸강나방"));

        fruitTreeList.add(new PestsOnCropDTO("과수", "배", "과수화상병", "갈색날개매미충,검은별무늬병", "복숭아심식나방,애모무늬잎말이나방"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "복숭아", "", "갈색날개매미충,복숭아심식나방,세균성구멍병", "복숭아순나방,잿빛무늬병,탄저병"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "사과", "가지검은마름병,과수화상병", "갈색날개매미충,복숭아심식나방", "복숭아잎말이나방,사과무늬잎말이나방,애모무늬잎말이나방,탄저병"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "포도", "", "꽃매미", "새눈무늬병,탄저병"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "감", "", "", "감꼭지나방"));

        vegetableList.add(new PestsOnCropDTO("채소", "고추", "", "담배나방,역병,탄저병,파밤나방", "목화진딧물"));
        vegetableList.add(new PestsOnCropDTO("채소", "가지", "", "", "온실가루이"));
        vegetableList.add(new PestsOnCropDTO("채소", "무", "", "", "무름병, 뿌리혹병, 무사마귀병"));
        vegetableList.add(new PestsOnCropDTO("채소", "배추", "", "", "무름병"));
        vegetableList.add(new PestsOnCropDTO("채소", "수박", "", "", "덩굴마름병"));
        vegetableList.add(new PestsOnCropDTO("채소", "오이", "", "", "꽃노랑총채벌레"));
        vegetableList.add(new PestsOnCropDTO("채소", "참외", "", "", "덩굴마름병"));
        vegetableList.add(new PestsOnCropDTO("채소", "토마토", "", "", "담배가루이, 반점위조바이러스, 온실가루이, 황화잎말림바이러스"));
    }

    private void setPestInformationForEachCrop2() {
        foodResourcesList = new ArrayList<>();
        fruitTreeList = new ArrayList<>();
        vegetableList = new ArrayList<>();

        fruitTreeList.add(new PestsOnCropDTO("과수", "배", "과수화상병", "갈색날개매미충,검은별무늬병", "복숭아심식나방,애모무늬잎말이나방"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "복숭아", "", "갈색날개매미충,복숭아심식나방,세균성구멍병", "복숭아순나방,잿빛무늬병,탄저병"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "사과", "가지검은마름병,과수화상병", "갈색날개매미충,복숭아심식나방", "복숭아잎말이나방,사과무늬잎말이나방,애모무늬잎말이나방,탄저병"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "포도", "", "꽃매미", "새눈무늬병,탄저병"));
        fruitTreeList.add(new PestsOnCropDTO("과수", "감", "", "", "감꼭지나방"));

        vegetableList.add(new PestsOnCropDTO("채소", "고추", "", "담배나방,역병,탄저병,파밤나방", "목화진딧물"));
        vegetableList.add(new PestsOnCropDTO("채소", "가지", "", "", "온실가루이"));
        vegetableList.add(new PestsOnCropDTO("채소", "무", "", "", "무름병, 뿌리혹병, 무사마귀병"));
        vegetableList.add(new PestsOnCropDTO("채소", "배추", "", "", "무름병"));
        vegetableList.add(new PestsOnCropDTO("채소", "수박", "", "", "덩굴마름병"));
        vegetableList.add(new PestsOnCropDTO("채소", "오이", "", "", "꽃노랑총채벌레"));
        vegetableList.add(new PestsOnCropDTO("채소", "참외", "", "", "덩굴마름병"));
        vegetableList.add(new PestsOnCropDTO("채소", "토마토", "", "", "담배가루이, 반점위조바이러스, 온실가루이, 황화잎말림바이러스"));
    }

}
