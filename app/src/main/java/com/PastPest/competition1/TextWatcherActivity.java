package com.PastPest.competition1;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TextWatcherActivity  implements TextWatcher {

    private final TextInputLayout textInputLayout;
    private TextInputEditText dupCheckTextView;
    private final String errorMsg;

    public TextWatcherActivity(TextInputLayout textInputLayout, TextInputEditText textView, String errorMsg){
        this.textInputLayout = textInputLayout;
        this.dupCheckTextView = textView;
        this.errorMsg = errorMsg;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!validatePhone(s.toString())){
            textInputLayout.setError(errorMsg);
            textInputLayout.setErrorEnabled(true);
        }
        else{
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        if(dupCheckTextView != null){
            dupCheckTextView.setVisibility(View.INVISIBLE);
        }
    }

    public boolean validatePhone(String s){
        String phonePattern = "01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}" ;
        return s.matches(phonePattern);
    }
}