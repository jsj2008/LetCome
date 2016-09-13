package com.letcome.activity;

import android.os.Bundle;
import android.widget.Button;

import com.gxq.tpm.activity.BaseActivity;
import com.letcome.R;

public class LoginActivity extends BaseActivity {

    Button mSignupBtn,mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initAction();
    }
    private void initView() {
        mSignupBtn = findViewById(R.id.signup_btn);
        mLoginBtn = findViewById(R.id.login_btn);

    }

    private void initAction() {


    }
}
