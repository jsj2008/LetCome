package com.gxq.tpm.activity.mine;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.GetMyCustomerService;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.cache.AsyncImageLoaderProxy;
import com.gxq.tpm.ui.CItemBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class UserServiceActivity extends SuperActivity implements CItemBar.OnItemBarClickListener {

	private ImageView mIvCustomService;
	private TextView mTvServiceNickName;
	private TextView mTvServiceDesc;
	private GetMyCustomerService mService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_service);
		mIvCustomService = (ImageView) findViewById(R.id.custom_service_head_image);
		mTvServiceNickName = (TextView) findViewById(R.id.service_nickname);
		mTvServiceDesc = (TextView) findViewById(R.id.service_comment);
		
		((CItemBar) findViewById(R.id.user_customer_service_online)).setOnItemBarClickListener(this);
		((CItemBar) findViewById(R.id.user_customer_service_feedback)).setOnItemBarClickListener(this);
		((CItemBar) findViewById(R.id.user_common_question)).setOnItemBarClickListener(this);
		((CItemBar) findViewById(R.id.user_new_comer_guide)).setOnItemBarClickListener(this);
		
		findViewById(R.id.view_titleBar_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		requestMyCustomService();
	}

	private void requestMyCustomService() {
		GetMyCustomerService.doRequest(this);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		super.netFinishOk(info, result, tag);
		
		if (RequestInfo.GET_MY_CUSTOMER_SERVICE == info) {
			GetMyCustomerService customerService = (GetMyCustomerService) result;
			mService = customerService;
			mTvServiceNickName.setText(customerService.nickname);
			mTvServiceDesc.setText(customerService.desc);
			
			if (customerService.pic != null) {
				AsyncImageLoaderProxy proxy = new AsyncImageLoaderProxy(this);
				proxy.downloadCache2Sd(customerService.pic, new AsyncImageLoaderProxy.ImageCallback() {
					@Override
					public void onImageLoaded(Bitmap bitmap, String imageUrl) {
						mIvCustomService.setImageBitmap(bitmap);
					}
				});
			} else {
				mIvCustomService.setImageResource(R.drawable.custom_service_head_image);
			}
		}
	}

	@Override
	public void onItemClick(CItemBar v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.user_customer_service_online:
			if (mService != null && mService.url != null) { 
				intent = new Intent(this, WebActivity.class);
				intent.putExtra(ProductIntent.EXTRA_TITLE, getString(R.string.user_customer_service_online));
				intent.putExtra(ProductIntent.EXTRA_URL, mService.url);
				intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
				startActivity(intent);
			}
			break;
		case R.id.user_customer_service_feedback:
			intent = new Intent(this, WebActivity.class);
			intent.putExtra(ProductIntent.EXTRA_TITLE, getString(R.string.user_customer_service_feedback));
			intent.putExtra(ProductIntent.EXTRA_URL, RequestInfo.SUGGESTION_FEEDBACK.getUrl());
			intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
			intent.putExtra(ProductIntent.EXTRA_FINISH_DIRECT, true);
			intent.putExtra(ProductIntent.EXTRA_HTTP_REQUEST, ProductIntent.REQUEST_GET);
			startActivity(intent);
			break;
		case R.id.user_common_question:
			intent = new Intent();
			intent.putExtra(ProductIntent.EXTRA_TITLE, getString(R.string.user_common_question));
			intent.putExtra(ProductIntent.EXTRA_URL, RequestInfo.COMMON_QUESTION.getUrl());
			intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
			intent.setClass(this, WebActivity.class);
			startActivity(intent);
			break;
		case R.id.user_new_comer_guide:
			intent = new Intent(this, WebActivity.class);
			intent.putExtra(ProductIntent.EXTRA_TITLE, getString(R.string.user_new_comer_guide));
			intent.putExtra(ProductIntent.EXTRA_URL, RequestInfo.NEWCOMER_GUIDE.getUrl());
			intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
			startActivity(intent);
		    break;
		}
	}

}
