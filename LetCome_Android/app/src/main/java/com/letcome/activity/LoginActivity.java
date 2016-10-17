package com.letcome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.CViewPagerAdapter;
import com.gxq.tpm.fragment.ViewPagerFragment;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.ui.CTabTitleSelector;
import com.letcome.App;
import com.letcome.R;
import com.letcome.fragement.LoginFragment;
import com.letcome.fragement.SignupFragment;
import com.letcome.mode.LoginRes;
import com.letcome.mode.SignupRes;
import com.letcome.mode.SsoRes;
import com.letcome.prefs.UserPrefs;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends SuperActivity implements IUiListener {

    private final static int TAB_SIGNUP 	= 0;
    private final static int TAB_LOGIN 	= 1;

//    TextView mSignupTV,mLoginTV;
    Button mCloseBtn;
    CTabTitleSelector mTabTitle;
    ViewPager mViewPager;
    private CViewPagerAdapter mAdapter;

    private SignupFragment mSignupFragment;
    private LoginFragment mLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initAction();
    }
    private void initView() {
        mSignupFragment = new SignupFragment(this);
        mLoginFragment = new LoginFragment(this);

        mTabTitle = (CTabTitleSelector) findViewById(R.id.tab_title);
        mTabTitle.setLayoutId(R.layout.login_tab_title);
        mTabTitle.setSeparateLayoutId(R.layout.tab_title_vertical_sep);

        mTabTitle.newTabTitle(R.string.sign_up);
        mTabTitle.newTabTitle(R.string.log_in);

        mCloseBtn = (Button)findViewById(R.id.close_btn);

        mViewPager = (ViewPager)findViewById(R.id.strategy_viewpager);

        List<ViewPagerFragment> fragments = new ArrayList<ViewPagerFragment>();
        fragments.add(mSignupFragment);
        fragments.add(mLoginFragment);
        mAdapter = new CViewPagerAdapter(fragments);
        mViewPager.setAdapter(mAdapter);
        mAdapter.onShow();

    }

    private void initAction() {
        mTabTitle.setOnTabTitleSelectListener(new CTabTitleSelector.OnTabTitleSelectListener() {
            @Override
            public void onSelection(int position) {
                setSelection(position);
            }
        });
        mTabTitle.setViewPager(mViewPager);
        setSelection(TAB_LOGIN);
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();

            }
        });
    }

    private void setSelection(int position) {
        mTabTitle.setPosition(position);
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.onShow();
    }

    public void login(String name,String pwd){
        showWaitDialog(RequestInfo.LOGIN);
        LoginRes.Params p = new LoginRes.Params();
        p.setEmail(name);
        p.setPwd(pwd);
        LoginRes.doRequest(p, this);
    }

    public void signup(String name,String pwd,String fullname,String qq){
        showWaitDialog(RequestInfo.SIGNUP);
        SignupRes.Params p = new SignupRes.Params();
        p.setEmail(name);
        p.setPwd(pwd);
        p.setFullname(fullname);
        p.setQq(qq);
        SignupRes.doRequest(p, this);
    }

    @Override
    public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
        if (info==RequestInfo.LOGIN){
            LoginRes l = (LoginRes) res;
            UserPrefs prefs = App.getUserPrefs();
            prefs.setUserInfo(l);
            prefs.setUid(l.getUid());
            prefs.setSession(l.getSessionid());
            prefs.save();

            this.finish();
        }else if (info==RequestInfo.SIGNUP){
            SignupRes l = (SignupRes) res;
            UserPrefs prefs = App.getUserPrefs();
            LoginRes lr = new LoginRes();
            lr.setUid(l.getUid());
            lr.setFullname(l.getFullname());
            lr.setQq(l.getQq());
            prefs.setUserInfo(lr);
            prefs.setUid(l.getUid());
            prefs.setSession(l.getSessionid());
            prefs.save();

            this.finish();
        } else if (info==RequestInfo.SSO){
            SsoRes l = (SsoRes) res;
            UserPrefs prefs = App.getUserPrefs();
            LoginRes lr = new LoginRes();
            lr.setUid(l.getUid());
            lr.setFullname(l.getFullname());
            lr.setQq(l.getQq());
            prefs.setUserInfo(lr);
            prefs.setUid(l.getUid());
            prefs.setSession(l.getSessionid());
            prefs.save();

            this.finish();
        }
        super.netFinishOk(info, res, tag);
    }

    @Override
    public void netFinishError(RequestInfo info, BaseRes result, int tag) {
//        if(info==RequestInfo.LOGIN){
//            showToastMsg(result.error_msg);
//        }
        super.netFinishError(info, result, tag);
    }

    public void qqLogin(){
        Tencent mTencent = App.instance().getTencent();
        if (!mTencent.isSessionValid())
        {
            mTencent.login(this, "get_simple_userinfo",this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this);
    }

    @Override
    public void onComplete(Object response) {
//        mBaseMessageText.setText("onComplete:");
//        mMessageText.setText(response.toString());
//        doComplete(response);
        Print.i("LoginActivity", "onComplete:" + response);
        showWaitDialog(RequestInfo.SSO);
        try {
            if (response instanceof JSONObject) {
                JSONObject obj = (JSONObject) response;
                SsoRes.Params p = new SsoRes.Params();
                p.setOpenid(obj.getString("openid"));
                p.setAccesstoken(obj.getString("access_token"));
                SsoRes.doRequest(p,this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError e) {
        Print.e("LoginActivity", "onError:" + e);
//        showResult("onError:", "code:" + e.errorCode + ", msg:"
//                + e.errorMessage + ", detail:" + e.errorDetail);
    }
    @Override
    public void onCancel() {
        Print.e("LoginActivity", "onCancel");
//        showResult("onCancel", "");
    }


}
