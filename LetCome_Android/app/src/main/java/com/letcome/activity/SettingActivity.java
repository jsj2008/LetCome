package com.letcome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.gxq.tpm.activity.SuperActivity;
import com.letcome.R;

public class SettingActivity extends SuperActivity {

    ListView mListView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getTitleBar().setTitle(R.string.setting_title);
        getTitleBar().setLeftImage(R.drawable.ic_prev_dialog_active);


        mListView = (ListView)findViewById(R.id.list_view);
        mListView.setAdapter();
    }

    @Override
    public void onLeftClick(View v) {
        super.onLeftClick(v);
        finish();
    }
}
