package com.example.competition1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordResetActivity extends Activity {

    private String url = "http://ec2-43-200-8-163.ap-northeast-2.compute.amazonaws.com:3000";
    private EditText recentPassword;
    private EditText newPassword;
    private EditText rePassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        Button passwordResetSuccess = findViewById(R.id.password_reset_success);
        recentPassword = findViewById(R.id.reset_password);
        newPassword = findViewById(R.id.resset_new_password);
        rePassword = findViewById(R.id.reset_double_password);

        passwordResetSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCorrectPw(newPassword.getText().toString());
            }
        });

    }

    //현재 비밀번호가 맞는지 확인
    private void requestCorrectPw(String password){
        JSONObject requestJsonObject = new JSONObject();
        //인터넷 연결확인

        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                requestJsonObject.put("id",   ((LoginedId) getApplication()).getId());
                requestJsonObject.put("password", password);

                RequestQueue requestQueue = Volley.newRequestQueue(PasswordResetActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/users/login", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            if (response.getBoolean("status")) { // 비밀번호가 일치할때

                                if(!rePassword.getText().toString().equals(newPassword.getText().toString()) ){
                                    rePassword.setText("");
                                    newPassword.setText("");
                                    Toast.makeText(getApplicationContext(), "입력한 비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    requestChangePw(((LoginedId) getApplication()).getId(),password);
                                }

                            } else {   //비밀번호 불일치
                                recentPassword.setText("");
                                Toast.makeText(getApplicationContext(), "현재 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
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

    // 비밀번호 변경
    private void requestChangePw(String id,String password){
        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {


                requestJsonObject.put("id", id);
                requestJsonObject.put("password", password);

                RequestQueue requestQueue = Volley.newRequestQueue(PasswordResetActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url + "/users/changepw", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if ( response.getBoolean("status")) {   // 변경이 완료되었을때
                                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show();
                                Intent registerIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(registerIntent);

                            } else {   //에러
                                Toast.makeText(getApplicationContext(), "변경 오류가 발생하였습니다", Toast.LENGTH_SHORT).show();
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
