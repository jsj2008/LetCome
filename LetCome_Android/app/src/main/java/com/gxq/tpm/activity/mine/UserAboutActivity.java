package com.gxq.tpm.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CItemBar;

public class UserAboutActivity extends SuperActivity implements CItemBar.OnItemBarClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		initUI();
	}

	private void initUI() {
		TextView tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText(App.instance().getVersionName());

		CItemBar item = (CItemBar) findViewById(R.id.user_platform_agreement);
		item.setOnItemBarClickListener(this);

		item = (CItemBar) findViewById(R.id.user_investor_agreement);
		item.setOnItemBarClickListener(this);

		item = (CItemBar) findViewById(R.id.user_privacy_clause);
		item.setOnItemBarClickListener(this);
	}

	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.user_about);
		getTitleBar().showBackImage();
	}

	@Override
	public void onItemClick(CItemBar v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.user_platform_agreement:
			intent = new Intent(this, WebActivity.class);
			intent.putExtra(ProductIntent.EXTRA_TITLE,getString(R.string.user_platform_agreement));
			intent.putExtra(ProductIntent.EXTRA_URL,RequestInfo.PLATFORM_STRATEGY_AGREEMENT.getUrl());
			intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
			startActivity(intent);
			break;
		case R.id.user_investor_agreement:
			intent = new Intent(this, UserStrategyAgreementActivity.class);
			startActivity(intent);
			break;
		case R.id.user_privacy_clause:
			intent = new Intent(this, WebActivity.class);
			intent.putExtra(ProductIntent.EXTRA_TITLE,Util.transformString(R.string.user_privacy_clause));
			intent.putExtra(ProductIntent.EXTRA_URL,RequestInfo.PRIVATE_POLICY.getUrl());
			intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
			startActivity(intent);
			break;
		}
	}

}
