package com.gxq.tpm.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.letcome.App;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.mine.ProdQuery;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.GlobalInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.WebHelper;
import com.gxq.tpm.tools.WebHelper.WebParams;
import com.gxq.tpm.ui.CItemBar;
import com.gxq.tpm.ui.CircularImage;

public class UserLgbAccountActivity extends SuperActivity implements
		CItemBar.OnItemBarClickListener {
	
	private CircularImage mIvHead;
	private TextView mTvLgbAccount, mTvAvailable, mTvFreeze;

	private ProdQuery mProd;

	@Override
	protected void initBar() {
		super.initBar();

		getTitleBar().setTitle(R.string.user_lgb_account_title);
		getTitleBar().showBackImage();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prod_item);

		mIvHead = (CircularImage) findViewById(R.id.iv_head);
		mTvLgbAccount = (TextView) findViewById(R.id.tv_lgb_account);
		
		mTvAvailable = (TextView) findViewById(R.id.tv_available);
		mTvAvailable.setText("");
		mTvFreeze = (TextView) findViewById(R.id.tv_freeze);
		mTvFreeze.setText("");

		((CItemBar) findViewById(R.id.user_prod_recharge)).setOnItemBarClickListener(this);
		((CItemBar) findViewById(R.id.user_prod_withdrawals)).setOnItemBarClickListener(this);
		
		((CItemBar) findViewById(R.id.user_prod_accountflow)).setOnItemBarClickListener(this);

		UserInfo info = App.getUserPrefs().getUserInfo();
		if (null != info) {
			GlobalInfo.setNetworkImage(mIvHead, info.pic, R.drawable.cpb_head90);
			if (!TextUtils.isEmpty(info.prod_username)) {
				mTvLgbAccount.setText(info.prod_username);
			}
		}
		
		TextView tv = (TextView) findViewById(R.id.tv_foot_view);
		tv.setText(Util.strChangeColor(getString(R.string.user_prod_tips),
				9, 12, Util.transformColor(R.color.text_color_link)));
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		mProd = (ProdQuery) getIntent().getSerializableExtra(ProductIntent.EXTRA_INSP_PROD);
		if (mProd != null) {
			assignProd(mProd);
		}
		requestProd();
		if (mProd == null) {
			showWaitDialog(RequestInfo.PROD_QUERY);
		}
	}
	
	private void requestProd() {
		ProdQuery.doRequest(null, this);
	}

	@Override
	public void onItemClick(CItemBar v) {
		switch (v.getId()) {
		case R.id.user_prod_recharge:
			jumpUrl(getString(R.string.user_prod_recharge_title),
					WebHelper.addParam(RequestInfo.PROD_URL)
					.addParam(WebParams.TYPE, 1).getUrl(), true);
			break;
		case R.id.user_prod_withdrawals:
			jumpUrl(getString(R.string.user_prod_withdrawals_title),
					WebHelper.addParam(RequestInfo.PROD_URL)
					.addParam(WebParams.TYPE, 2).getUrl(), true);
			break;
		case R.id.user_prod_accountflow:
			jumpUrl(getString(R.string.user_prod_accountflow_title),
					WebHelper.addParam(RequestInfo.PROD_URL)
					.addParam(WebParams.TYPE, 3).getUrl(), false);
			break;
		}
	}

	public void jumpUrl(String title, String url, boolean finishDirect) {
		Intent intent = new Intent(UserLgbAccountActivity.this, WebActivity.class);
		intent.putExtra(ProductIntent.EXTRA_TITLE, title);
		intent.putExtra(ProductIntent.EXTRA_URL, url);
		intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true); 
		intent.putExtra(ProductIntent.EXTRA_FINISH_DIRECT, true);
		startActivity(intent);
	}

	private void assignProd(ProdQuery prod) {
		String available = NumberFormat.moneySymbol( 
				NumberFormat.decimalFormat("###,##0.00", prod.available));
		String freeze = NumberFormat.moneySymbol( 
				NumberFormat.decimalFormat("###,##0.00", prod.freeze));
		int size=getResources().getDimensionPixelSize(R.dimen.font_size_xhdpi_12);
		mTvAvailable.setText(Util.strChangeSize(available,0,1,size));
		mTvFreeze.setText(Util.strChangeSize(freeze,0,1,size));
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		super.netFinishOk(info, result, tag);
		if (RequestInfo.PROD_QUERY == info) {
			mProd = (ProdQuery) result;
			assignProd(mProd);
		}
	}
	
}
