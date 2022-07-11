package com.PastPest.competition1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class FindingIdActivity extends AppCompatActivity {

    private TextInputEditText editName;
    private TextInputEditText editPhone;
    private String url = "http://ec2-3-39-234-0.ap-northeast-2.compute.amazonaws.com:3000";

    @Override
   public void onCreate(Bundle saveInstanceState){
       super.onCreate(saveInstanceState);
       setContentView(R.layout.activity_finding_id);

       Toolbar toolbar = findViewById(R.id.finding_id_toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
       getSupportActionBar().setTitle(""); // 툴바 제목 설정
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);  //이미지 설정

        Button findingId = findViewById(R.id.finding_id_result_button);
        editName = findViewById(R.id.finding_id_name);
        editPhone = findViewById(R.id.finding_id_phone);

        findingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();
                requestFindingId(name,phone);

            }
        });

   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestFindingId(String name, String phone){

        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                requestJsonObject.put("name", name);
                requestJsonObject.put("phone", phone);

                RequestQueue requestQueue = Volley.newRequestQueue(FindingIdActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/users/findid", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            if (response.getBoolean("exist")) {   // 존재하는 회원
                                AlertDialog.Builder idDialog = new AlertDialog.Builder(FindingIdActivity.this);
                                idDialog.setMessage("ID : "+ response.getString("id"));

                                idDialog.setPositiveButton("로그인하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent findingIdIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(findingIdIntent);
                                    }
                                });
                                idDialog.show();

                            } else {   //존재하지 않는 회원
                                Toast.makeText(getApplicationContext(), "존재하지 않는 회원입니다.", Toast.LENGTH_SHORT).show();
                                editName.setText("");
                                editPhone.setText("");
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
