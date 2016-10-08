package com.letcome.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gxq.tpm.activity.SuperActivity;
import com.letcome.R;

public class SearchActivity extends SuperActivity {

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
        EditText searchET = (EditText)v.findViewById(R.id.search_input);
        searchET.setEnabled(true);
        getTitleBar().setTitleView(v);
        getTitleBar().setRightText("搜索");
        getTabBar().selectTabItem(R.id.tab_me);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
    }
}
