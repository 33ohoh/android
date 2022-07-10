package com.example.competition1;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

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
    MainActivity mainActivity=this;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentCurrent fragmentCurrent = new FragmentCurrent();
    private FragmentDeclaration fragmentDeclaration = new FragmentDeclaration(mainActivity);
    private FragmentInformation fragmentInformation = new FragmentInformation();
    private FragmentMypage fragmentMypage = new FragmentMypage();
    private FragmentPredict fragmentPredict = new FragmentPredict();
    private long backKeyPressedTime = 0;

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
                    fragmentCurrent = new FragmentCurrent();
                    transaction.replace(R.id.frameLayout, fragmentCurrent).commitAllowingStateLoss();
                    break;
                case R.id.tab_declaration:
                    fragmentDeclaration = new FragmentDeclaration(mainActivity);
                    transaction.replace(R.id.frameLayout, fragmentDeclaration).commitAllowingStateLoss();
                    break;
                case R.id.tab_information:
                    fragmentInformation = new FragmentInformation();
                    transaction.replace(R.id.frameLayout, fragmentInformation).commitAllowingStateLoss();
                    break;
                case R.id.tab_mypage:
                    fragmentMypage = new FragmentMypage();
                    transaction.replace(R.id.frameLayout, fragmentMypage).commitAllowingStateLoss();
                    break;
                case R.id.predict_menu:
                    fragmentPredict = new FragmentPredict();
                    transaction.replace(R.id.frameLayout, fragmentPredict).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
    // 뒤로가기 2번 누르면 종료
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번더 버튼을 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            moveTaskToBack(true);
            finishAndRemoveTask();
            System.exit(0);
        }
    }
    public void reconnect(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragmentMypage = new FragmentMypage();
        transaction.replace(R.id.frameLayout, fragmentMypage).commitAllowingStateLoss();
    }
}