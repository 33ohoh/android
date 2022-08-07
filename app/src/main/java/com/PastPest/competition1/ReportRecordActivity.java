package com.PastPest.competition1;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ReportRecordActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_record);

        Intent intent = getIntent();
        ReportHistory reportHistory  = (ReportHistory)intent.getSerializableExtra("reportHistory");

        TextView title = findViewById(R.id.tx_title); //reportHistory.getTitle();
        TextView date = findViewById(R.id.tx_date); //reportHistory.getAddress();
        TextView address = findViewById(R.id.tx_location);
        TextView crop = findViewById(R.id.tx_crop_name);
        TextView symptom = findViewById(R.id.tx_symptom);
        TextView pest = findViewById(R.id.tx_pest_name);
        TextView details = findViewById(R.id.tx_details);
        ImageView image=findViewById(R.id.uploadedImage);

        //Toast.makeText(getApplicationContext(), reportHistory, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), reportHistory.getPestName(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), reportHistory.getFileName(), Toast.LENGTH_SHORT).show();

        title.setText(reportHistory.getTitle());
        date.setText(reportHistory.getDate());
        address.setText(reportHistory.getAddress());
        crop.setText(reportHistory.getCropName());
        symptom.setText(reportHistory.getSymptom());
        pest.setText(reportHistory.getPestName());
        details.setText(reportHistory.getDetails());
        if(reportHistory.getImageUrl()!="none"){
            try {
                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        getApplicationContext(),
                        "ap-northeast-2:05fe3813-8b11-402b-8e0b-544354a50280", // 자격 증명 풀 ID
                        Regions.AP_NORTHEAST_2 // 리전
                );
                TransferNetworkLossHandler.getInstance(getApplicationContext());
                AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
                TransferUtility transferUtility = new TransferUtility(s3, getApplicationContext());

                s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
                s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");
                File tempFile = new File(getCacheDir(), "downloadedImage");
                tempFile.createNewFile();
                TransferObserver observer = transferUtility.download("pestpestimage",reportHistory.getImageUrl(), tempFile);
                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if(state==TransferState.COMPLETED){
                            Log.e("Aws","complete");
                            String imgpath = getCacheDir() + "/downloadedImage";
                            Bitmap bm = BitmapFactory.decodeFile(imgpath);
                            image.setImageBitmap(bm);
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        try {
                            int done= Integer.parseInt(String.valueOf((bytesCurrent / bytesTotal) * 100));
                            Log.e("Aws",String.valueOf(done));
                        }
                        catch (Exception e){
                            Log.e("Aws","error1");
                        }
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        Log.e("Aws","error2");
                    }
                });
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
