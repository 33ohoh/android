package com.PastPest.competition1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.PastPest.competition1.utility.Constants;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.PastPest.competition1.LoginActivity;
import com.PastPest.competition1.LoginedId;
import com.PastPest.competition1.NetworkStatusActivity;
import com.PastPest.competition1.PasswordResetActivity;
import com.PastPest.competition1.R;
import com.PastPest.competition1.ReportHistory.ReportHistoryActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class FragmentMypage extends Fragment {


    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_mypage, container, false);


        Button reportHistory = view.findViewById(R.id.report_history);
        Button password = view.findViewById(R.id.password);
        Button logout = view.findViewById(R.id.logout);
        Button withdrawal = view.findViewById(R.id.withdrawal);
        TextView loginedId = view.findViewById(R.id.mypage_logined_id);

        loginedId.setText(  ((LoginedId) getActivity().getApplication()).getId() + " 님");  // 로그인된 아이디 표시;

        reportHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getActivity().getApplicationContext(), ReportHistoryActivity.class);
                startActivity(registerIntent);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getActivity().getApplicationContext(), PasswordResetActivity.class);
                startActivity(registerIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(registerIntent);
            }
        });

        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject requestJsonObject = new JSONObject();

                //인터넷 연결확인
                int status = NetworkStatusActivity.getConnectivityStatus(getActivity().getApplicationContext());
                if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
                    try {

                        requestJsonObject.put("id",  ((LoginedId) getActivity().getApplication()).getId());

                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL + "/users/delete", requestJsonObject, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    if (response.getBoolean("status")) {   //로그인 성공

                                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);


                                    } else {   //탈퇴실패
                                        Toast.makeText(getActivity().getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(getActivity().getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}