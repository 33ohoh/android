package com.example.competition1.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.competition1.LoginActivity;
import com.example.competition1.LoginedId;
import com.example.competition1.NetworkStatusActivity;
import com.example.competition1.R;
import com.example.competition1.report.CropSelectActivity;
import com.example.competition1.report.LocationSelectActivity;
import com.example.competition1.report.PestSelectActivity;
import com.example.competition1.report.SymptomSelectActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FragmentDeclaration extends Fragment {

    private String url = "http://ec2-43-200-8-163.ap-northeast-2.compute.amazonaws.com:3000";
    ImageView selectedImage;
    double latitude=37.5495538;
    double longitude=127.075032;
    String detailAddress="";
    String loadAddress="경기도";
    String cropName="";
    String symptomName="";
    String pestName="";
    AppCompatButton locationBtn;
    AppCompatButton cropBtn;
    AppCompatButton symptomBtn;
    AppCompatButton pestBtn;
    AppCompatButton imageBtn;
    AppCompatButton reportBtn;
    EditText detailText;
    TextInputEditText titleText;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_declaration, container, false);

        detailText = (EditText) view.findViewById(R.id.detailText);
        titleText =(TextInputEditText) view.findViewById(R.id.titleText);
        selectedImage = (ImageView) view.findViewById(R.id.selectedImage);
        locationBtn = (AppCompatButton) view.findViewById(R.id.locationButton);
        cropBtn = (AppCompatButton) view.findViewById(R.id.cropButton);
        symptomBtn = (AppCompatButton) view.findViewById(R.id.symptomButton);
        pestBtn = (AppCompatButton) view.findViewById(R.id.pestButton);
        imageBtn = (AppCompatButton) view.findViewById(R.id.imageButton);
        reportBtn = (AppCompatButton) view.findViewById(R.id.reportButton);


        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), LocationSelectActivity.class);

                //intent.putExtra("key","12345".toString());
                intent.putExtra("loadAddress", loadAddress);
                intent.putExtra("detailAddress", detailAddress);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivityForResult(intent, 1);
            }
        });

        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CropSelectActivity.class);
                intent.putExtra("cropName", cropName);
                startActivityForResult(intent, 2);
            }
        });

        symptomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropName.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "작물이 등록되어있지 않습니다", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SymptomSelectActivity.class);
                    intent.putExtra("selectedSymptom", symptomName);
                    intent.putExtra("cropName", cropName);
                    startActivityForResult(intent, 3);
                }
            }
        });

        pestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropName.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "작물이 등록되어있지 않습니다", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), PestSelectActivity.class);
                    intent.putExtra("selectedPest", pestName);
                    intent.putExtra("cropName", cropName);
                    startActivityForResult(intent, 4);
                }
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 5);
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detail=detailText.getText().toString();
                String title=titleText.getText().toString();
                //데이터베이스에 결과들 저장해주는 함수 만들기
                if (loadAddress.equals("") || cropName.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "위치정보와 임산물 정보는 필수입니다", Toast.LENGTH_LONG).show();
                }else if(title.length()==0) {
                    Toast.makeText(getActivity().getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_LONG).show();
                }else if(detail.length()==0){
                    Toast.makeText(getActivity().getApplicationContext(), "세부사항을 입력해주세요", Toast.LENGTH_LONG).show();

                }else{
                    BitmapDrawable bitmapDrawable=(BitmapDrawable)selectedImage.getDrawable();
                    Bitmap bitmap=bitmapDrawable.getBitmap();
                    requestRegister(((LoginedId)getActivity().getApplication()).getId(),detail,bitmapToByteArray(bitmap),title);
                }
            }
        });

        AppCompatButton deleteButton=(AppCompatButton) view.findViewById(R.id.imageDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage.setImageBitmap(null);
                imageBtn.setBackgroundResource(R.drawable.button_background);
            }
        });


        return view;
    }

    private String bitmapToByteArray(Bitmap bitmap){
        String image="";
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        image = byteArrayToBinaryString(byteArray);
        return image;
    }
    public String byteArrayToBinaryString(byte[] b){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<b.length;++i){
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }
    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==5){
            if(resultCode== Activity.RESULT_OK){
                Glide.with(getActivity().getApplicationContext()).load(data.getData()).override(300,300).into(selectedImage);
                selectedImage.setClipToOutline(true);
                selectedImage.setScaleType(ImageView.ScaleType.FIT_XY);
                imageBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
        }
        else if(requestCode==1){
            if(resultCode==Activity.RESULT_OK) {
                latitude = data.getDoubleExtra("latitude",latitude);
                longitude = data.getDoubleExtra("longitude",longitude);
                detailAddress = data.getStringExtra("detailAddress");
                loadAddress=data.getStringExtra("loadAddress");
                locationBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==2){
            if(resultCode==Activity.RESULT_OK) {
                String temp=data.getStringExtra("cropName");
                if(temp.equals(""))
                    cropBtn.setBackgroundResource(R.drawable.button_background);
                else
                    cropBtn.setBackgroundResource(R.drawable.seleted_button_background);
                if(!cropName.equals(temp)) {
                    cropName = temp;
                    symptomName="";
                    pestName="";
                    symptomBtn.setBackgroundResource(R.drawable.button_background);
                    pestBtn.setBackgroundResource(R.drawable.button_background);
                }
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==3){
            if(resultCode==Activity.RESULT_OK) {
                symptomName = data.getStringExtra("selectedSymptom");
                if(symptomName.equals(""))
                    symptomBtn.setBackgroundResource(R.drawable.button_background);
                else
                    symptomBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==4){
            if(resultCode==Activity.RESULT_OK) {
                pestName = data.getStringExtra("selectedPest");
                if(pestName.equals(""))
                    pestBtn.setBackgroundResource(R.drawable.button_background);
                else
                    pestBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
    }
    private void requestRegister(String id, String detailText,String bitmap,String title ){

        JSONObject requestJsonObject = new JSONObject();
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus( getActivity().getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                requestJsonObject.put("id", id);
                requestJsonObject.put("title", title);
                requestJsonObject.put("date",dateFormat.format(date) );
                requestJsonObject.put("street_address", loadAddress);
                requestJsonObject.put("detailed_address", detailAddress);
                requestJsonObject.put("latitude", latitude);
                requestJsonObject.put("longitude", longitude);
                requestJsonObject.put("product_name", cropName);
                requestJsonObject.put("symptom", symptomName);
                requestJsonObject.put("pest_name", pestName);
                requestJsonObject.put("image_url", bitmap);
                requestJsonObject.put("details", detailText);

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url + "/declarations/report/add", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //회원가입 후 로그인 페이지로 이동
                        try {
                            if(response.getBoolean("status")){
                                Toast.makeText(getActivity().getApplicationContext(), "신고가 정상적으로 접수되었습니다", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity().getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

}
