package com.example.competition1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.Calendar;


public class LoginActivity extends Activity implements View.OnClickListener {


    private long backKeyPressedTime = 0;
    private TextInputEditText editId;
    private TextInputEditText editPassword;
    private Calendar VolleyNetwork;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button register = findViewById(R.id.register_button);
        Button login = findViewById(R.id.login_button);
        Button finding = findViewById(R.id.finding_button);
        editId = findViewById(R.id.edit_login_id);
        editPassword= findViewById(R.id.edit_login_pwd);


        // 로그인, 회원가입, 아이디/비밀번호 찾기 리스너 달기
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        finding.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.register_button:
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.login_button:
                requestLogin( editId.getText().toString(),  editPassword.getText().toString());
                break;

            case R.id.finding_button:
                Intent findingIntent = new Intent(getApplicationContext(), FindingActivity.class);
                startActivity(findingIntent);
                break;

        }
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번더 버튼을 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            moveTaskToBack(true);
            finishAndRemoveTask();
            System.exit(0);
        }
    }


    private void requestLogin(String id, String password) {

        String url = "http://ec2-43-200-8-163.ap-northeast-2.compute.amazonaws.com:3000";

        JSONObject requestJsonObject = new JSONObject();

        try {
            requestJsonObject.put("id", id);
            requestJsonObject.put("password",password);


            final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url+"/users/login",requestJsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {


                        JSONObject jsonObject = new JSONObject(response.toString());

                        //Log.v("test","test :" + response.getBoolean("status"));

                        if(response.getBoolean("status")){   //로그인 성공
                            // 여기다가 로그인 후 넘어가는 페이지 넣기

                        }else{   //로그인 실패
                            Toast.makeText( getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT ).show();
                            editId.setText("");
                            editPassword.setText("");
                        }

//

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


    }

}

