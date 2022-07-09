package com.example.competition1.informationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.competition1.API.DetailAPIData;
import com.example.competition1.API.DetailAPITask;
import com.example.competition1.API.PestAPITask;
import com.example.competition1.R;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class InformationViewActivity extends AppCompatActivity {
    String insectKey;
    String sickKey;
    String imageURL;
    String name;
    int type;
    NodeList List;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Intent intent=getIntent();
        insectKey=intent.getStringExtra("insectKey");
        sickKey=intent.getStringExtra("sickKey");
        type=intent.getIntExtra("type",0);
        imageURL=intent.getStringExtra("image");
        name=intent.getStringExtra("name");
        ImageView detailImage=(ImageView) findViewById(R.id.detailImage);
        detailImage.setScaleType(ImageView.ScaleType.FIT_XY);
        detailImage.setClipToOutline(true);
        Glide.with(this).load(imageURL).into(detailImage);
        TextView detailText=(TextView) findViewById(R.id.detailNameText);
        EditText nameEdit=(EditText) findViewById(R.id.detailNameEditText);
        TextView characterText=(TextView) findViewById(R.id.characterText);
        TextView damageText=(TextView) findViewById(R.id.damageText);
        nameEdit.setText(name);
        if(type==1){//질병정보
            characterText.setText("감염경로");
            damageText.setText("증상");
            DetailAPITask task=new DetailAPITask();
            try{
                List=task.execute(sickKey,"symptom").get();
            }
            catch (Exception e){

            }

        }
        else if(type==2){
            detailText.setText("해충명");
            damageText.setText("증상");
            DetailAPITask task=new DetailAPITask();
            try{
                List=task.execute(insectKey,"pest").get();
            }
            catch (Exception e){

            }

        }

    }
}
