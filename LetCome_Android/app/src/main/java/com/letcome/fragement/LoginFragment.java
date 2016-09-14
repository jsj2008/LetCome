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
public class LoginFragment extends ViewPagerFragment implements TextWatcher{
    LoginActivity parent;

    Button mLoginBtn;
    ImageView mEmailImg,mPwdImg;
    EditText mEmailEt,mPwdEt;


    public LoginFragment(LoginActivity activity) {
        super(activity, null);
        parent = activity;
    }


    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
        mLoginBtn = (Button)view.findViewById(R.id.login_btn);
        mLoginBtn.setEnabled(false);
        mEmailEt = (EditText)view.findViewById(R.id.email_et);

        mEmailEt.addTextChangedListener(this);
        
        mEmailImg = (ImageView)view.findViewById(R.id.email_img);
        mPwdEt = (EditText)view.findViewById(R.id.pwd_et);
        mPwdEt.addTextChangedListener(this);
        mPwdImg = (ImageView)view.findViewById(R.id.pwd_img);


    }

    public void refreshBtnState(){
        if (mEmailEt.getText().length()>0 && mPwdEt.getText().length()>0){
            mLoginBtn.setEnabled(true);
        }else{
            mLoginBtn.setEnabled(false);
        }
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
}
