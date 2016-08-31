package com.gxq.tpm.activity;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.mode.strategy.ProductProfit;
import com.gxq.tpm.mode.strategy.ProductProfit.Profit;
import com.gxq.tpm.mode.strategy.StrategySellingOrders.StrategySelling;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.ui.CProductHqHead;
import com.gxq.tpm.ui.CProductHqKLineChart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StockHqActivity extends SuperActivity implements
		View.OnClickListener, DispatcherTimer.OnDispatcherTimerListener {
	private final static int HQ 	= 1;
	private final static int PROFIT = 2;
	
	private CProductHqHead mHqHead;
	private CProductHqKLineChart mHqChart;
	private View mContainerBottom;
	private Button mBtnNext;
	
	private Intent mPendingIntent;
	
	private String mStockName, mStockCode, mText;
	private StrategySelling mSelling;
	private boolean mEnable = true;
	
	private DispatcherTimer mHqTimer;
	private DispatcherTimer mProfitTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_stock_hq);
		
		if (getIntent() != null) {
			mStockName = getIntent().getStringExtra(ProductIntent.EXTRA_STOCK);
			mStockCode = getIntent().getStringExtra(ProductIntent.EXTRA_STOCK_CODE);
			mText =  getIntent().getStringExtra(ProductIntent.EXTRA_TEXT);
			
			mPendingIntent = getIntent().getParcelableExtra(ProductIntent.EXTRA_INTENT);
			mEnable = getIntent().getBooleanExtra(ProductIntent.EXTRA_ENABLE, true);
			mSelling = (StrategySelling) getIntent().getSerializableExtra(ProductIntent.EXTRA_VALUE);
		}
		
		initUI();
		
		mHqTimer = new DispatcherTimer(this, 3, HQ);
		mProfitTimer = new DispatcherTimer(this, 3, PROFIT);
	}
	
	private void initUI() {
		findViewById(R.id.iv_left).setOnClickListener(this);
		
		TextView tv = (TextView) findViewById(R.id.tv_stock_name);
		tv.setText(mStockName);
		
		tv = (TextView) findViewById(R.id.tv_stock_code);
		tv.setText(mStockCode);
		
		mHqHead = (CProductHqHead) findViewById(R.id.container_hq_head);
		mHqChart = (CProductHqKLineChart) findViewById(R.id.container_hq_chart);
		mHqChart.setStockCode(mStockCode);
		
		mContainerBottom = findViewById(R.id.container_bottom);
		mBtnNext = (Button) findViewById(R.id.btn_next_step);
		mBtnNext.setOnClickListener(this);
		mBtnNext.setText(mText);
		mBtnNext.setEnabled(mEnable);
		
		mContainerBottom.setVisibility(mPendingIntent == null ? View.INVISIBLE : View.VISIBLE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mHqChart.onResume();
		mHqChart.show();
		
		mHqTimer.timerTaskControl(true);
		
		if (mSelling != null) {
			showWaitDialog(RequestInfo.PRODUCT_PROFIT);
			mProfitTimer.timerTaskControl(true);
			
			mBtnNext.setEnabled(mEnable && mSelling.state == 1);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mHqChart.onPause();
		mHqChart.hide();
		
		mHqTimer.timerTaskControl(false);
		mProfitTimer.timerTaskControl(false);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.btn_next_step:
			if (mPendingIntent != null) {
				startActivity(mPendingIntent);
			}
			break;
		}
	}
	
	@Override
	public void onAlarmClock(int operationType) {
		super.onAlarmClock(operationType);
		
		if (operationType == HQ) {
			GetHandicapInfo.doRequest(mStockCode, this);
		} else if (operationType == PROFIT) {
			if (mSelling != null) {
				ProductProfit.Params params = new ProductProfit.Params();
				params.p_id_arr = Integer.toString(mSelling.id);
				ProductProfit.doRequest(params, this);
			}
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		
		if (RequestInfo.GET_HANDICAP == info) {
			GetHandicapInfo handicapInfo = (GetHandicapInfo) res;
			mHqHead.assignHandicapInfo(handicapInfo);
			mHqChart.assignHandicapInfo(handicapInfo);
		} else if (RequestInfo.PRODUCT_PROFIT == info) {
			ProductProfit productProfit = (ProductProfit) res; 
			if (productProfit.records != null && mSelling != null) {
				for (Profit profit : productProfit.records) {
					if (profit.id == mSelling.id) {
						mBtnNext.setEnabled(mEnable && mSelling.state == 1
							&& BaseRes.RESULT_OK.equals(profit.is_trade_time));
					}
				}
			}
		}
	}
	
}
