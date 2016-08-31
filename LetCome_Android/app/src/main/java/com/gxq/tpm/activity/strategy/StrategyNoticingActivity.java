package com.gxq.tpm.activity.strategy;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.GetTime;
import com.gxq.tpm.mode.cooperation.ProductSchemeDetail;
import com.gxq.tpm.mode.strategy.ProductApplySell;
import com.gxq.tpm.mode.strategy.ProductPreApplySellCheck;
import com.gxq.tpm.mode.strategy.ProductProfit;
import com.gxq.tpm.mode.strategy.ProductProfit.Profit;
import com.gxq.tpm.mode.strategy.StrategyNoticingOrders.StrategyNoticing;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeCountDown;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CCircleProgressBar;
import com.gxq.tpm.ui.ProductDialog;

public class StrategyNoticingActivity extends SuperActivity 
		implements DispatcherTimer.OnDispatcherTimerListener, View.OnClickListener,
		TimeCountDown.OnTimeCountDownListener, CountDown.OnCountDownListener {
	private final static int PROFIT 	= 1;
	private final static int GET_TIME	= 2;
	
	private final static int WAIT_TIME = 15;
	
	private StrategyNoticing mNoticing;
	
	private Button mBtnNext;
	private TextView mTvProfit, mTvCountDown, mTvNotNoticeTime, mTvFailCost;
	private CCircleProgressBar mCpbProfit;
	
	private DispatcherTimer mProfitTimer, mGetTimeTimer;
	
	private TimeCountDown mNoticeTimeCountDown;
	private CountDown mCountDown;
	
	private boolean mBackable = true; // 如果为false，界面不能退出
	private boolean mTimeOut = false; // 是否超时
	private boolean mSuccess = false; // 是否通知成功
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Util.gotoStrategy(StrategyNoticingActivity.this, ProductIntent.REFRESH_NEW_SELLING);
			finish();
		}
	};
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle2(R.string.strategy_notice_title);
		getTitleBar().setLeftImage(R.drawable.nav_back_w);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_strategy_noticing);
		
		mNoticing = (StrategyNoticing) getIntent().getSerializableExtra(ProductIntent.EXTRA_VALUE);
		
		initUI();
		bindAction();
		
		mProfitTimer = new DispatcherTimer(this, 3, PROFIT);
		mGetTimeTimer = new DispatcherTimer(this, 3, GET_TIME);
	}
	
	private void initUI() {
		TextView tv = (TextView) findViewById(R.id.tv_investor_name);
		tv.setText(mNoticing.dealer_replace);
		
		tv = (TextView) findViewById(R.id.tv_investor_fund);
		tv.setText(NumberFormat.tenThousand(
				NumberFormat.decimalFormat0(mNoticing.fund/10000)));
		
		tv = (TextView) findViewById(R.id.tv_stock_name);
		tv.setText(mNoticing.stock_name);
		
		CCircleProgressBar cpb = (CCircleProgressBar) findViewById(R.id.cpb_today_days);
		cpb.setProgress(mNoticing.trade_day, mNoticing.max_trade_day);
		cpb.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_4));
		
		tv = (TextView) findViewById(R.id.tv_notice_today_days);
		tv.setText(Integer.toString(mNoticing.trade_day));
		
		tv = (TextView) findViewById(R.id.tv_leave_day);
		tv.setText(getString(R.string.strategy_notice_leave_day, 
				mNoticing.max_trade_day - mNoticing.trade_day));
		
		mTvProfit = (TextView) findViewById(R.id.tv_profit);
		
		mCpbProfit = (CCircleProgressBar) findViewById(R.id.cpb_profit);
		mCpbProfit.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_4));
		mCpbProfit.setTotal(mNoticing.stop_profit_point);
		
		String startTime = TimeFormat.milliToHourMinute(mNoticing.apply_start_time);
		String endTime = TimeFormat.milliToHourMinute(mNoticing.apply_end_time);
		
		tv = (TextView) findViewById(R.id.tv_notice_time);
		tv.setText(getString(R.string.strategy_notice_notice_time, startTime, endTime));
		
		mTvCountDown = (TextView) findViewById(R.id.tv_count_down);
		mTvNotNoticeTime = (TextView) findViewById(R.id.tv_not_notice_time);
		
		tv = (TextView) findViewById(R.id.tv_important_tip1);
		tv.setText(getString(R.string.strategy_notice_important_tip1, 
				NumberFormat.percentWithInteger(mNoticing.stop_profit_point)));
		
		mTvFailCost = (TextView) findViewById(R.id.tv_important_tip2);
		
		tv = (TextView) findViewById(R.id.tv_important_tip3);
		tv.setText(getString(R.string.strategy_notice_important_tip3,
				mNoticing.max_trade_day - 2, startTime, endTime,
				mNoticing.max_trade_day - 1));
		
		mBtnNext = (Button) findViewById(R.id.btn_next_step);
		mBtnNext.setText(R.string.strategy_notice_confirm);
	}
	
	private void bindAction() {
		mBtnNext.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mNoticeTimeCountDown = null;
		
		showWaitDialog(RequestInfo.GET_TIME);
		
		mProfitTimer.timerTaskControl(true);
		mGetTimeTimer.timerTaskControl(true);
		
		requestSchemeDetail();
	}
	
	private void requestSchemeDetail() {
		ProductSchemeDetail.Params params = new ProductSchemeDetail.Params();
		params.scheme_id = mNoticing.scheme_id;
		ProductSchemeDetail.doRequest(params, this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mProfitTimer.timerTaskControl(false);
		mGetTimeTimer.timerTaskControl(false);
		
		if (mNoticeTimeCountDown != null) {
			mNoticeTimeCountDown.stopTimer();
		}
	}
	
	@Override
	public void onAlarmClock(int operationType) {
		super.onAlarmClock(operationType);
		
		if (operationType == PROFIT) {
			ProductProfit.Params params = new ProductProfit.Params();
			params.p_id_arr = Integer.toString(mNoticing.id);
			ProductProfit.doRequest(params, this);
		} else if (operationType == GET_TIME) {
			GetTime.doRequest(this);
		}
	}
	
	private void requestPreApplySellCheck() {
		mBackable = false;
		
		showWaitDialog(RequestInfo.PRODUCT_PRE_APPLY_SELL_CHECK);
		ProductPreApplySellCheck.doRequest(mNoticing.id, this);
	}
	
	private void requestApplySell() {
		showWaitDialog(RequestInfo.PRODUCT_APPLY_SELL);
		
		mCountDown = new CountDown(WAIT_TIME);
		mCountDown.setOnCountDownListener(this);
		mCountDown.startTimer();
		
		ProductApplySell.doRequest(mNoticing.id, this);
	}

	@Override
	public void onLeftClick(View v) {
		if (mBackable)
			super.onLeftClick(v);
	}
	
	@Override
	public void onBackPressed() {
		if (mBackable)
			super.onBackPressed();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.btn_next_step:
			requestPreApplySellCheck();
			break;
		}
	}
	
	private void assignProfit(Profit profit) {
		mTvProfit.setText(NumberFormat.percent(profit.profit_rate));
		mCpbProfit.setProgress(profit.profit_rate);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		
		if (RequestInfo.PRODUCT_SCHEME_DETAIL == info) {
			ProductSchemeDetail schemeDetail = (ProductSchemeDetail) res;
			mTvFailCost.setText(getString(R.string.strategy_notice_important_tip2, schemeDetail.fail_cost));
		} else if (RequestInfo.GET_TIME == info) {
			mGetTimeTimer.timerTaskControl(false);
			
			GetTime getTime = (GetTime) res;
			
			mNoticeTimeCountDown = new TimeCountDown(mNoticing.apply_end_time);
			mNoticeTimeCountDown.startTimer(getTime.time);
			
			mNoticeTimeCountDown.setOnTimeCountDownListener(this);
			
			checkTime(getTime.time);
		} else if (RequestInfo.PRODUCT_PROFIT == info) {
			ProductProfit profit = (ProductProfit) res;
			if (profit.records != null && profit.records.size() > 0) {
				assignProfit(profit.records.get(0));
			}
		} else if (RequestInfo.PRODUCT_PRE_APPLY_SELL_CHECK == info) {
			ProductPreApplySellCheck check = (ProductPreApplySellCheck) res;
			if (BaseRes.RESULT_OK.equals(check.result)) {
				requestApplySell();
			} else {
				ProductDialog.Builder builder = null;
				if (check.reason == 4) {
					builder = new ProductDialog.Builder(this);
					builder.setContent(R.string.strategy_notice_to_sell, check.msg);
				} else if (check.reason == 5) {
					builder = new ProductDialog.Builder(this);
					builder.setContent(R.string.strategy_notice_to_sell, R.drawable.layer_remind, check.msg);
				}
				
				if (builder != null) {	
					builder.setPositiveButton(R.string.btn_cancel, new ProductDialog.ProductDialogListener() {
							@Override
							public void onDialogClick(int id) {
								mBackable = true;
							}
						}).setNegativeButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
							@Override
							public void onDialogClick(int id) {
								requestApplySell();
							}
						}).create().show();
				} else {
					requestApplySell();
				}
			}
		} else if (RequestInfo.PRODUCT_APPLY_SELL == info) {
			if (mCountDown != null)
				mCountDown.stopTimer();

			if (!mTimeOut) {
				ProductApplySell applySell = (ProductApplySell) res;
				if (BaseRes.RESULT_OK.equals(applySell.result)) {
					showToastMsg(R.string.strategy_notice_suceess);
					
					mSuccess = true;
					mBtnNext.setEnabled(false);
					mHandler.sendEmptyMessageDelayed(0, 3000);
				} else {
					mBackable = true;
					showToastMsg(R.string.strategy_notice_fail);
				}
			}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (RequestInfo.PRODUCT_PRE_APPLY_SELL_CHECK == info) {
			mBackable = true;
		} else if (RequestInfo.PRODUCT_APPLY_SELL == info) {
			mBackable = true;
			if (!mTimeOut) {
				showToastMsg(R.string.strategy_notice_fail);
				if (mCountDown != null) {
					mCountDown.stopTimer();
				}
			}
			return ERROR_NONE;
		}
		
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void checkTime(long time) {
		try {
			if (time >= mNoticing.apply_start_time && time < mNoticing.apply_end_time) {
				mTvCountDown.setVisibility(View.VISIBLE);
				
				long leaveTime = mNoticing.apply_end_time - time;
				mTvCountDown.setText(milliToHourSecond(leaveTime));
				
				mTvNotNoticeTime.setVisibility(View.GONE);
				mBtnNext.setEnabled(!mSuccess);
			} else {
				mTvCountDown.setVisibility(View.GONE);
				mTvNotNoticeTime.setVisibility(View.VISIBLE);
				mBtnNext.setEnabled(false);
			}
		} catch (Exception e) {
			mTvCountDown.setVisibility(View.GONE);
			mTvNotNoticeTime.setVisibility(View.GONE);
			mBtnNext.setEnabled(!mSuccess);
		}
	}
	
	private String milliToHourSecond(long time) {
		return format(time / 3600) + ":" + format((time % 3600) / 60) + ":" + format(time % 60);
	}
	
	private String format(long value) {
		return value < 10 ? "0" + value : Long.toString(value);
	}
	
	@Override
	public void onTimeStep(long value) {
		checkTime(value);
	}
	
	@Override
	public void onTimeFinish() {
		mTvCountDown.setVisibility(View.GONE);
		mTvNotNoticeTime.setVisibility(View.VISIBLE);
		mBtnNext.setEnabled(!mSuccess);
	}
	
	@Override
	public void onCountDownRun(int maxNum, int remainNum) {
	}
	
	@Override
	public void onCountDownFinished() {
		mTimeOut = true;
		new ProductDialog.Builder(this)
			.setContent(R.string.strategy_notice_time_out, R.string.strategy_notice_time_out_content)
			.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
				@Override
				public void onDialogClick(int id) {
					Util.gotoStrategy(StrategyNoticingActivity.this, ProductIntent.REFRESH_NEW_SELLING);
					finish();
				}
			}).create().show();
	}
		
}
