package com.example.competition1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.competition1.fragment.FragmentCurrent;
import com.example.competition1.fragment.FragmentDeclaration;
import com.example.competition1.fragment.FragmentInformation;
import com.example.competition1.fragment.FragmentMypage;
import com.example.competition1.fragment.FragmentPredict;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentCurrent fragmentCurrent = new FragmentCurrent();
    private FragmentDeclaration fragmentDeclaration = new FragmentDeclaration();
    private FragmentInformation fragmentInformation = new FragmentInformation();
    private FragmentMypage fragmentMypage = new FragmentMypage();
    private FragmentPredict fragmentPredict = new FragmentPredict();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentPredict).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.tab_current:
                    transaction.replace(R.id.frameLayout, fragmentCurrent).commitAllowingStateLoss();
                    break;
                case R.id.tab_declaration:
                    transaction.replace(R.id.frameLayout, fragmentDeclaration).commitAllowingStateLoss();
                    break;
                case R.id.tab_information:
                    transaction.replace(R.id.frameLayout, fragmentInformation).commitAllowingStateLoss();
                    break;
                case R.id.tab_mypage:
                    transaction.replace(R.id.frameLayout, fragmentMypage).commitAllowingStateLoss();
                    break;
                case R.id.predict_menu:
                    transaction.replace(R.id.frameLayout, fragmentPredict).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}