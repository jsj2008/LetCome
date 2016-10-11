package com.letcome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gxq.tpm.activity.SuperActivity;
import com.letcome.R;
import com.letcome.fragement.MeFragment;
import com.letcome.mode.WaterFallsRes;

public class ResultActivity extends SuperActivity {
    WaterFallsRes.Params mParams;
    EditText mSearchEt;
    public static void create(Activity activity,WaterFallsRes.Params params){
        Intent intent = new Intent(activity,ResultActivity.class);
        intent.putExtra("params", params);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParams = (WaterFallsRes.Params)getIntent().getSerializableExtra("params");
        initTitleView();

        setContentView(R.layout.activity_result);


        Fragment toFragment = new MeFragment(mParams,false);

        FragmentTransaction ft = mFragmentManager.beginTransaction();

        ft.add(R.id.c_content, toFragment/*getFragment(id)*/, "ResultActivity");
        ft.show(toFragment);

        //ft.commit();
        ft.commitAllowingStateLoss();
        //now execute
        mFragmentManager.executePendingTransactions();
    }

    void initTitleView(){
        RelativeLayout v = (RelativeLayout)getLayoutInflater().inflate(R.layout.tab_title_choose, null);
        v.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        lp.setMargins(0, 20, 0, 20);
        v.setBackgroundResource(R.drawable.view_radius_3dp_white);
        mSearchEt = (EditText)v.findViewById(R.id.search_input);
        mSearchEt.setEnabled(false);
        mSearchEt.setFocusable(false);
        Button searchbtn = (Button)v.findViewById(R.id.search_btn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        if (mParams!=null){
            mSearchEt.setText(mParams.getProductname());
        }
        getTitleBar().setTitleView(v);
        getTitleBar().setRightImage(R.drawable.ic_listing_filters_active);
        getTabBar().selectTabItem(R.id.tab_me);
        getTitleBar().setLeftImage(R.drawable.ic_prev_dialog_active);
    }

    @Override
    public void onRightClick(View v) {
        FilterActivity.create(this,mParams);
    }
}
