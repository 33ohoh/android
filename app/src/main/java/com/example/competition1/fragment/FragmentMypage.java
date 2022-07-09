package com.example.competition1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.competition1.LoginActivity;
import com.example.competition1.PasswordResetActivity;
import com.example.competition1.R;
import com.example.competition1.ReportHistoryActivity;


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
                Intent registerIntent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(registerIntent);
            }
        });

        return view;
    }
}