package com.example.competition1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.competition1.pestdetails.PestDetailsActivity;
import com.example.competition1.pestprediction.OnViewHolderItemClickListener;
import com.example.competition1.pestprediction.PestsOnCropDTO;
import com.example.competition1.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReportHistoryActivity extends AppCompatActivity {
    private ListView reportHistoryView;
    private ReportHistoryAdapter reportHistoryAdapter;
    private ArrayList<ReportHistory> reportHistoryList;
    private String url = "http://ec2-43-200-8-163.ap-northeast-2.compute.amazonaws.com:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        //Log.v("아이디값 테스트", ((LoginedId) getApplicationContext()).getId());
        //getPestListFromServer();

        reportHistoryView = (ListView) findViewById(R.id.lv_report_history);  //신고 내역 리스트가 보여지는 뷰
        reportHistoryList = new ArrayList<>();

        //임시로 넣은 신고데이터
        reportHistoryList.add(new ReportHistory("옥수수 시들음", "2022-07-01", "서울특별시 군자동(세종대학교 학생회관 304B)", "옥수수", "과수화상병", "멸강나방", "", "멸강나방으로 옥수수가 피해를 입었습니다."));         //신고내역 리스트에 들어갈 데이터
        reportHistoryList.add(new ReportHistory("사과 시들음", "2022-07-01", "서울특별시 군자동(세종대학교 학생회관 304B)", "옥수수", "과수화상병", "멸강나방", "", "멸강나방으로 옥수수가 피해를 입었습니다."));
        reportHistoryList.add(new ReportHistory("배추 시들음", "2022-07-01", "서울특별시 군자동(세종대학교 학생회관 304B)", "옥수수", "과수화상병", "멸강나방", "", "멸강나방으로 옥수수가 피해를 입었습니다."));
        reportHistoryList.add(new ReportHistory("양피 무름", "2022-07-01", "서울특별시 군자동(세종대학교 학생회관 304B)", "옥수수", "과수화상병", "멸강나방", "", "멸강나방으로 옥수수가 피해를 입었습니다."));
        reportHistoryList.add(new ReportHistory("옥수수 무름", "2022-07-01", "서울특별시 군자동(세종대학교 학생회관 304B)", "옥수수", "과수화상병", "멸강나방", "", "멸강나방으로 옥수수가 피해를 입었습니다."));

        reportHistoryAdapter = new ReportHistoryAdapter(getApplicationContext(), reportHistoryList);  //어뎁터
        reportHistoryView.setAdapter(reportHistoryAdapter);                                           //신고내역 뷰에 어뎁터 연결

        ListView reportList = findViewById(R.id.lv_report_history);  //신고내역 리스트뷰

        reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ReportHistory reportHistory = reportHistoryList.get(position);    //신고내역

                Intent findingIntent = new Intent(getApplicationContext(), FindingActivity.class);
                startActivity(findingIntent);

                Intent intent = new Intent(getApplicationContext(), ReportRecordActivity.class);
                intent.putExtra("reportHistory", reportHistory);
                startActivity(intent);
            }
        });

    }

    private void setReportHistoryList(JSONObject jsonObject){
        reportHistoryView = (ListView) findViewById(R.id.lv_report_history);  //신고 내역 리스트가 보여지는 뷰
        reportHistoryList = new ArrayList<>();                                //신고 내역 리스트

        String title, date, address, cropName, symptom, pestName, imageUrl, details;

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("pests");

            for(int index = 0; index < jsonArray.length(); index++){
                JSONObject obj = jsonArray.getJSONObject(index);

                title = obj.getString("title");
                date = obj.getString("date");
                address = String.format("%s(%s)", obj.getString("street_address"), obj.getString("detailed_address"));
                cropName = obj.getString("product_name");
                symptom = obj.getString("symptom");
                pestName = obj.getString("pest_name");
                imageUrl = obj.getString("image_url");
                details = obj.getString("details");

                reportHistoryList.add(new ReportHistory(title, date, address, cropName, symptom, pestName, imageUrl, details));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*reportHistoryList.add(new ReportHistory("애호박", "2022-07-01", "답변 대기중"));         //신고내역 리스트에 들어갈 데이터
        reportHistoryList.add(new ReportHistory("상추시들음병", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("늙은호박 병", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("옥수수 병리 문의", "2022-06-30", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("느티나무 잎벌레 문의입니다", "2022-06-29", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("오미자", "2022-06-29", "답변 대기중"));
        reportHistoryList.add(new ReportHistory("사과잎 진단", "2022-06-28", "답변 대기중"));*/
    }

    private void getPestListFromServer(){

        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                requestJsonObject.put("id", ((LoginedId) getApplicationContext()).getId());

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/declaration/report", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Log.v("제이슨테스트트트트",response.toString() );

                            if (response.getBoolean("status")) {
                                setReportHistoryList(jsonObject);   //서버에서 가져온 작물, 각 병해충 정보 데이터 저장
                            } else {   //로그인 실패

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getApplicationContext().getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }

    }
}