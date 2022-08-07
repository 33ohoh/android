package com.PastPest.competition1.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import com.PastPest.competition1.LoginedId;
import com.PastPest.competition1.MainActivity;
import com.PastPest.competition1.NetworkStatusActivity;
import com.PastPest.competition1.R;
import com.PastPest.competition1.report.CropSelectActivity;
import com.PastPest.competition1.report.LocationSelectActivity;
import com.PastPest.competition1.report.PestSelectActivity;
import com.PastPest.competition1.report.SymptomSelectActivity;
import com.PastPest.competition1.utility.Constants;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentDeclaration extends Fragment {
    MainActivity mainActivity;
    CognitoCachingCredentialsProvider credentialsProvider;
    ImageView selectedImage;
    double latitude=37.5495538;
    double longitude=127.075032;
    String detailAddress="";
    String loadAddress="";
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
    boolean isImageSelected=false;

    public FragmentDeclaration(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }
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


        credentialsProvider = new CognitoCachingCredentialsProvider(
                getActivity().getApplicationContext(),
                "ap-northeast-2:05fe3813-8b11-402b-8e0b-544354a50280", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );

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
                    requestRegister(((LoginedId)getActivity().getApplication()).getId(),detail,title);
                }
            }
        });

        AppCompatButton deleteButton=(AppCompatButton) view.findViewById(R.id.imageDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage.setImageBitmap(null);
                imageBtn.setBackgroundResource(R.drawable.button_background);
                //내부 캐시 데이터도 삭제해야함
                deleteImage();
                isImageSelected=false;
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==5){
            if(resultCode== Activity.RESULT_OK){
                Uri fileUri = data.getData();
                ContentResolver resolver = getActivity().getApplicationContext().getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    selectedImage.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    instream.close();   // 스트림 닫아주기
                    saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
                    imageBtn.setBackgroundResource(R.drawable.seleted_button_background);
                    //저장했는지 유무확인하기
                    isImageSelected=true;
                } catch (Exception e) {}
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
    private void requestRegister(String id, String detailText,String title ) {
        Blob blob=new Blob() {
            @Override
            public long length() throws SQLException {
                return 0;
            }

            @Override
            public byte[] getBytes(long l, int i) throws SQLException {
                return new byte[0];
            }

            @Override
            public InputStream getBinaryStream() throws SQLException {
                return null;
            }

            @Override
            public long position(byte[] bytes, long l) throws SQLException {
                return 0;
            }

            @Override
            public long position(Blob blob, long l) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long l, byte[] bytes) throws SQLException {
                return 0;
            }

            @Override
            public int setBytes(long l, byte[] bytes, int i, int i1) throws SQLException {
                return 0;
            }

            @Override
            public OutputStream setBinaryStream(long l) throws SQLException {
                return null;
            }

            @Override
            public void truncate(long l) throws SQLException {

            }

            @Override
            public void free() throws SQLException {

            }

            @Override
            public InputStream getBinaryStream(long l, long l1) throws SQLException {
                return null;
            }
        };
        JSONObject requestJsonObject = new JSONObject();
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus( getActivity().getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                //저장했는지 유무 확인하자
                String fileName=createFileName(id);
                if(!isImageSelected)
                    fileName="none";
                //
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
                requestJsonObject.put("details", detailText);
                requestJsonObject.put("image_url",fileName);

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL + "/declarations/report/add", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //회원가입 후 로그인 페이지로 이동
                        try {
                            if(response.getBoolean("status")){
                                Toast.makeText(getActivity().getApplicationContext(), "신고가 정상적으로 접수되었습니다", Toast.LENGTH_SHORT).show();
                                mainActivity.reconnect();
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

                //이미지 불러왔으면
                if(isImageSelected) {
                    String imgpath = getActivity().getCacheDir() + "/selectedImage";
                    File testfile = new File(imgpath);
                    s3save(fileName, testfile);
                }


            } catch (JSONException e) {
               Toast.makeText(getActivity().getApplicationContext(), "서버 통신 오류", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        File tempFile = new File(getActivity().getCacheDir(), "selectedImage");    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
        } catch (Exception e) {
        }
    }

    private void s3save(String filename, File file){
        AmazonS3 s3=new AmazonS3Client(credentialsProvider);
        TransferNetworkLossHandler.getInstance(getActivity().getApplicationContext());
        TransferUtility transferUtility=new TransferUtility(s3,getActivity().getApplicationContext());

        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        TransferObserver observer=transferUtility.upload("pestpestimage",filename,file);
    }

    private String createFileName(String id){
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",java.util.Locale.getDefault());
        String strDate=dateFormat.format(date);
        return id+strDate;
    }

    private void deleteImage() {    // 이미지 삭제
        try {
            File file = getActivity().getCacheDir();  // 내부저장소 캐시 경로를 받아오기
            File[] flist = file.listFiles();
            for (int i = 0; i < flist.length; i++) {    // 배열의 크기만큼 반복
                if (flist[i].getName().equals("selectedImage")) {   // 삭제하고자 하는 이름과 같은 파일명이 있으면 실행
                    flist[i].delete();  // 파일 삭제
                    Toast.makeText(getActivity().getApplicationContext(), "파일 삭제 성공", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "파일 삭제 실패", Toast.LENGTH_SHORT).show();
        }
    }
}
