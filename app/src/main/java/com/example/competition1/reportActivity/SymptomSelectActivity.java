package com.example.competition1.reportActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.competition1.R;

public class SymptomSelectActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_selection);

        androidx.gridlayout.widget.GridLayout gridLayout=(androidx.gridlayout.widget.GridLayout) findViewById(R.id.symptomGrid);
        final float scale = gridLayout.getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (120 * scale + 0.5f);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = ((DisplayMetrics) metrics).widthPixels * 1/3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixels, pixels);
        params.weight = 1;
        params.gravity=1;
        params.setMargins((int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f), (int) (10 * scale + 0.5f));
        ViewGroup.LayoutParams buttonParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (100 * scale + 0.5f));
        ViewGroup.LayoutParams textParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int) (20 * scale + 0.5f));
        for(int i=0;i<21;i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            AppCompatButton button = new AppCompatButton(this);
            button.setBackgroundResource(R.drawable.crop_button_background);
            button.setGravity(Gravity.CENTER);
            TextView textView=new TextView(this);
            textView.setText("준비중");
            textView.setGravity(Gravity.CENTER);
            button.setLayoutParams(buttonParams);
            textView.setLayoutParams(textParams);
            linearLayout.addView(button,0);
            linearLayout.addView(textView,1);
            gridLayout.addView(linearLayout, 0);
        }
    }
}
