package com.example.competition1.pestdetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.competition1.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PestDetailsActivity extends AppCompatActivity {

    private String imageUrl;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_details);

        Intent intent = getIntent();
        PestDetails pestDetails = (PestDetails)intent.getSerializableExtra("details");

        ImageView imageView = findViewById(R.id.imageView);
        TextView pestName = findViewById(R.id.tx_pest_name);
        TextView symptom = findViewById(R.id.tx_symptom);
        TextView controlMethod = findViewById(R.id.tx_pest_control_method);

        pestName.setText(String.format("%s(%s)",                         //병해충명
                pestDetails.getPsetName(), pestDetails.getCropName()));
        symptom.setText(pestDetails.getSymptom());                       //증상
        controlMethod.setText(pestDetails.getControlMethod());           //방제방법

        imageUrl = pestDetails.getImage();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    //URL url = new URL("https://asddsa.soll0803.repl.co/kospi.PNG");
                    URL url = new URL(imageUrl);

                    // Web에서 이미지를 가져온 뒤
                    // ImageView에 지정할 Bitmap을 만든다
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // 서버로 부터 응답 수신
                    conn.connect();

                    InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start(); // Thread 실행

        try {
            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
            // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
            mThread.join();

            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
