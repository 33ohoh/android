package com.PastPest.competition1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.PastPest.competition1.ReportHistory.ReportHistory;
import com.PastPest.competition1.ReportHistory.ReportHistoryAdapter;
import com.PastPest.competition1.utility.Constants;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportRecordActivity extends AppCompatActivity {
    private String whetherToSolve = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_record);

        Intent intent = getIntent();
        ReportHistory reportHistory  = (ReportHistory)intent.getSerializableExtra("reportHistory");

        TextView title = findViewById(R.id.tx_title);
        TextView date = findViewById(R.id.tx_date);
        TextView address = findViewById(R.id.tx_location);
        TextView crop = findViewById(R.id.tx_crop_name);
        TextView symptom = findViewById(R.id.tx_symptom);
        TextView pest = findViewById(R.id.tx_pest_name);
        TextView details = findViewById(R.id.tx_details);
        TextView txWhetherToSolve = findViewById(R.id.tx_whether_to_solve);
        Button btnWhetherToSolve = findViewById(R.id.btn_whether_to_solve);

        title.setText(reportHistory.getTitle());
        date.setText(reportHistory.getDate());
        address.setText(reportHistory.getAddress());
        crop.setText(reportHistory.getCropName());
        symptom.setText(reportHistory.getSymptom());
        pest.setText(reportHistory.getPestName());
        details.setText(reportHistory.getDetails());

        getWhetherToSolveFromServer(reportHistory, btnWhetherToSolve, txWhetherToSolve);

        btnWhetherToSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(whetherToSolve.equals("0")){   //아직 해결되지 않은 신고내역 -> 신고 완료로 바꿈
                    //해결완료버튼 비활성화
                    btnWhetherToSolve.setEnabled(false);
                    //진행상태 표시
                    txWhetherToSolve.setText("해결 완료");
                    //해결완료값 서버에 저장
                    requestChangeWhetherToSolve( ((LoginedId) getApplicationContext()).getId(),
                            reportHistory.getCropName(), reportHistory.getTitle(),
                            reportHistory.getDetails(), reportHistory.getDate());
                }
            }
        });
    }
    private void setWhetherToSolve(ReportHistory reportHistory, JSONObject jsonObject){
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            JSONObject obj = jsonArray.getJSONObject(0);

            whetherToSolve = obj.getString("whether_to_solve");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getWhetherToSolveFromServer(ReportHistory reportHistory, Button btnWhetherToSolve, TextView txWhetherToSolve){

        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                requestJsonObject.put("id", reportHistory.getId());
                requestJsonObject.put("product_name", reportHistory.getCropName());
                requestJsonObject.put("symptom", reportHistory.getSymptom());
                requestJsonObject.put("pest_name", reportHistory.getPestName());
                requestJsonObject.put("details", reportHistory.getDetails());
                requestJsonObject.put("date", reportHistory.getDate());
                requestJsonObject.put("title", reportHistory.getTitle());

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        Constants.SERVER_URL + "/declarations/whethertosolve", requestJsonObject,
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            if (response.getBoolean("status")) {

                                setWhetherToSolve(reportHistory, jsonObject);   //해결완료여부 저장

                                if(whetherToSolve.equals(Constants.RESOLVED_REPORT)){  //진행상태에 해결완료 표시
                                    txWhetherToSolve.setText("해결 완료");
                                }

                                if(reportHistory.getId().equals(((LoginedId)getApplicationContext()).getId())
                                        == !Constants.LOGIN_ID) {
                                    btnWhetherToSolve.setVisibility(View.GONE);
                                }//로그인한 회원의 신고내역이 아닌 경우 해결완료버튼 숨김
                                else if(whetherToSolve.equals(Constants.RESOLVED_REPORT)) {
                                    btnWhetherToSolve.setEnabled(false);
                                }//로그인한 회원의 신고내역이면서 해결이 된 신고내역이라면 '해결완료'버튼 비활성화
                                else {
                                    txWhetherToSolve.setText("신고 사항이 해결되었다면 아래의 해결 완료 버튼을 눌러주세요.");
                                }//로그인한 회원의 신고내역이면서 아직 해결되지 않은 신고내역

                            } else {
                                Toast.makeText(getApplicationContext().getApplicationContext(),
                                        "오류가 발생하였습니다", Toast.LENGTH_SHORT).show();
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

    private void requestChangeWhetherToSolve(String id, String product_name, String title, String details, String date){
        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {

                requestJsonObject.put("id", id);
                requestJsonObject.put("product_name", product_name);
                requestJsonObject.put("title", title);
                requestJsonObject.put("details", details);
                requestJsonObject.put("date", date);

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                        Constants.SERVER_URL + "/declarations/changewhethertosolve", requestJsonObject,
                        new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if ( response.getBoolean("status")) {   // 변경이 완료되었을때
                                Toast.makeText(getApplicationContext(),
                                        "신고내역이 해결 완료로 변경되었습니다", Toast.LENGTH_SHORT).show();
                            } else {   //에러
                                Toast.makeText(getApplicationContext(),
                                        "오류가 발생하였습니다", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
