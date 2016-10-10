package com.letcome.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gxq.tpm.activity.SuperActivity;
import com.letcome.R;
import com.letcome.mode.WaterFallsRes;

public class SearchActivity extends SuperActivity {
    EditText mSearchEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initTitleView();
    }

    void initTitleView(){
        RelativeLayout v = (RelativeLayout)getLayoutInflater().inflate(R.layout.tab_title_choose, null);
        v.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        lp.setMargins(0, 20, 0, 20);
        v.setBackgroundResource(R.drawable.view_radius_3dp_white);
        mSearchEt = (EditText)v.findViewById(R.id.search_input);
        mSearchEt.setEnabled(true);
        mSearchEt.setFocusable(true);
        Button searchbtn = (Button)v.findViewById(R.id.search_btn);
        searchbtn.setVisibility(View.GONE);
        getTitleBar().setTitleView(v);
        getTitleBar().setRightText("搜索");
        getTitleBar().setLeftImage(R.drawable.ic_prev_dialog_active);
        getTabBar().selectTabItem(R.id.tab_me);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        if(mSearchEt.getText().toString().isEmpty()){
            showToastMsg(R.string.title_search_hint);
        }
        WaterFallsRes.Params params = new WaterFallsRes.Params();
        params.setLongitude("0");
        params.setLatitude("0");
        params.setDistance("0");
        params.setProductname(mSearchEt.getText().toString());
        ResultActivity.create(this,params);
        finish();
    }
}
