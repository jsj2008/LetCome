package com.letcome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.ui.CItemBar;
import com.letcome.App;
import com.letcome.R;

public class SettingActivity extends SuperActivity {
    CItemBar mLogouIb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getTitleBar().setTitle(R.string.setting_title);
        getTitleBar().setLeftImage(R.drawable.ic_prev_dialog_active);


        mLogouIb = (CItemBar)findViewById(R.id.user_logout);
        mLogouIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getUserPrefs().logout();
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                //  安全退出后跳转到home界面
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onLeftClick(View v) {
        super.onLeftClick(v);
        finish();
    }
}
