package com.letcome.fragement;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gxq.tpm.fragment.ViewPagerFragment;
import com.letcome.R;
import com.letcome.activity.LoginActivity;

/**
 * Created by rjt on 16/9/14.
 */
public class SignupFragment extends ViewPagerFragment implements TextWatcher,View.OnClickListener{

    LoginActivity parent;

    Button mSignupBtn;
    ImageView mEmailImg,mPwdImg,mFullnameImg,mQqImg;
    EditText mEmailEt,mPwdEt,mFullnameEt,mQqEt;

    public SignupFragment(LoginActivity activity) {
        super(activity, null);
        parent = activity;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_signup, container, false);

    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
        mSignupBtn = (Button)view.findViewById(R.id.signup_btn);
        mSignupBtn.setEnabled(false);
        mSignupBtn.setOnClickListener(this);

        mEmailEt = (EditText)view.findViewById(R.id.email_et);
        mEmailEt.addTextChangedListener(this);

        mEmailImg = (ImageView)view.findViewById(R.id.email_img);
        mPwdEt = (EditText)view.findViewById(R.id.pwd_et);
        mPwdEt.addTextChangedListener(this);
        mPwdImg = (ImageView)view.findViewById(R.id.pwd_img);

        mFullnameEt = (EditText)view.findViewById(R.id.fullname_et);
        mFullnameEt.addTextChangedListener(this);
        mFullnameImg = (ImageView)view.findViewById(R.id.fullname_img);

        mQqEt = (EditText)view.findViewById(R.id.qq_et);
        mQqEt.addTextChangedListener(this);
        mQqImg = (ImageView)view.findViewById(R.id.qq_img);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        refreshBtnState();
    }

    @Override
    public void onClick(View v) {
        parent.signup(mEmailEt.getText().toString(), mPwdEt.getText().toString(), mFullnameEt.getText().toString(), mQqEt.getText().toString());
    }

    public void refreshBtnState(){
        if (mEmailEt.getText().length()>0
                && mPwdEt.getText().length()>0
                && mFullnameEt.getText().length()>0
                && mQqEt.getText().length()>0){
            mSignupBtn.setEnabled(true);
        }else{
            mSignupBtn.setEnabled(false);
        }
    }
}
