package com.example.competition1;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class PestPredictionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_prediction);

        String[] month = getResources().getStringArray(R.array.month);

        Spinner spnMonth = (Spinner)findViewById(R.id.spn_month);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.custom_dropdown_item, month);
        spnMonth.setAdapter(adapter);
        spnMonth.setSelection(6);
    }
}
