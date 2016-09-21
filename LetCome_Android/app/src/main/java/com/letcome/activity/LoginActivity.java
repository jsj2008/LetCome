package com.letcome.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.CViewPagerAdapter;
import com.gxq.tpm.fragment.ViewPagerFragment;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.ui.CTabTitleSelector;
import com.letcome.App;
import com.letcome.R;
import com.letcome.fragement.LoginFragment;
import com.letcome.fragement.SignupFragment;
import com.letcome.mode.LoginRes;
import com.letcome.prefs.UserPrefs;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends SuperActivity{

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
        mSignupFragment = new SignupFragment(this, null);
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
}
