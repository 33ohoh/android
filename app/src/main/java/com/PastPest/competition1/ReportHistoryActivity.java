package com.PastPest.competition1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class ReportHistoryActivity extends AppCompatActivity {
    private ListView reportHistoryView;
    private ReportHistoryAdapter reportHistoryAdapter;
    private ArrayList<ReportHistory> reportHistoryList;
    private String url = "http://ec2-3-39-234-0.ap-northeast-2.compute.amazonaws.com:3000";
    private ArrayList<ReportHistory> currentHistoryList;
    //private ArrayList<ReportHistory> searchHistoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_history);

        currentHistoryList = new ArrayList<>();

        reportHistoryView = (ListView) findViewById(R.id.lv_report_history);  //신고 내역 리스트가 보여지는 뷰

        ListView reportList = findViewById(R.id.lv_report_history);  //신고내역 리스트뷰

        getPestListFromServer();

        EditText searchTitle = findViewById(R.id.et_search_title);

        searchTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.v("테테테", searchTitle.getText()+"");
                String text = searchTitle.getText().toString();
                search(text);
            }
        });

        reportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ReportHistory reportHistory = reportHistoryList.get(position);    //신고내역

                Intent intent = new Intent(getApplicationContext(), ReportRecordActivity.class);
                intent.putExtra("reportHistory", reportHistory);
                startActivity(intent);
            }
        });

    }

    public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        currentHistoryList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            currentHistoryList.addAll(reportHistoryList);
            Log.v("오아우", "dd");
        }
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < reportHistoryList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (reportHistoryList.get(i).getTitle().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    currentHistoryList.add(reportHistoryList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.

        reportHistoryAdapter.notifyDataSetChanged();
    }

    private void setReportHistoryList(JSONObject jsonObject){
        reportHistoryList = new ArrayList<>();                                //신고 내역 리스트

        String title, date, address, cropName, symptom, pestName, imageUrl, details;

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("result");

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
    }

    private void getPestListFromServer(){

        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                requestJsonObject.put("id", ((LoginedId) getApplicationContext()).getId());

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/declarations/report", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            if (response.getBoolean("status")) {
                                setReportHistoryList(jsonObject);

                                currentHistoryList.addAll(reportHistoryList);
                                reportHistoryAdapter = new ReportHistoryAdapter(getApplicationContext(), currentHistoryList);  //어뎁터
                                reportHistoryView.setAdapter(reportHistoryAdapter);              //서버에서 가져온 작물, 각 병해충 정보 데이터 저장

                                if(reportHistoryList.size() == 0){
                                    findViewById(R.id.ll_no_result).setVisibility(View.VISIBLE);
                                }
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