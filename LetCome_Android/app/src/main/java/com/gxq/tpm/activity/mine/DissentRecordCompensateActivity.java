package com.gxq.tpm.activity.mine;

import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.ui.CItemBar;

import android.content.Intent;
import android.os.Bundle;

public class DissentRecordCompensateActivity extends SuperActivity implements CItemBar.OnItemBarClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dissent_compensate);
		((CItemBar) findViewById(R.id.user_compensate)).setOnItemBarClickListener(this);
		((CItemBar) findViewById(R.id.user_dissent_record)).setOnItemBarClickListener(this);
	}

	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.user_dissentrecord_compensate);
		getTitleBar().showBackImage();
	}

	@Override
	public void onItemClick(CItemBar v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.user_dissent_record:
			intent.setClass(this, DissentRecordActivity.class);
			startActivity(intent);
			break;
		case R.id.user_compensate:
			intent.setClass(this, UserCompensateActivity.class);
			startActivity(intent);
			break;
		 }
	}
	

}
