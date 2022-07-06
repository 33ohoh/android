package com.example.competition1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private String url = "http://ec2-43-200-8-163.ap-northeast-2.compute.amazonaws.com:3000";
    private TextInputEditText editId;
    private TextInputEditText editPassword;
    private TextInputEditText editRepassword;
    private TextInputEditText editName;
    private TextInputEditText editPhone;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button doubleIdCheck = findViewById(R.id.check_id);
        Button register = findViewById(R.id.register_result_button);
        editId = findViewById(R.id.register_id);
        editPassword = findViewById(R.id.register_password);
        editRepassword = findViewById(R.id.register_repassword);
        editName = findViewById(R.id.register_name);
        editPhone = findViewById(R.id.register_phone);

        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setTitle(""); // 툴바 제목 설정
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backbutton);  //이미지 설정

        register.setOnClickListener(this);
        doubleIdCheck.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String id;
        String password;
        String name;
        String phone;

        switch (view.getId()) {
            case R.id.check_id:

                break;

            case R.id.register_result_button:

                id = editId.getText().toString();
                password = editPassword.getText().toString();
                name = editName.getText().toString();
                phone = editPhone.getText().toString();

                requestRegister(id,password,name,phone);
                break;
            default:
                break;


        }
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

    private void requestRegister(String id, String password, String name, String phone ){

        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {

                requestJsonObject.put("id", id);
                requestJsonObject.put("password", password);
                requestJsonObject.put("name", name);
                requestJsonObject.put("phone", phone);

                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/users/register", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //회원가입 후 로그인 페이지로 이동
                        Toast.makeText(getApplicationContext(), "회원가입 완료.", Toast.LENGTH_SHORT).show();
                        Intent registerIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(registerIntent);
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
