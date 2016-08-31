package com.gxq.tpm.activity.strategy;

import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.mode.strategy.ProductBuyOrder;
import com.gxq.tpm.mode.strategy.ProductBuyStatus;
import com.gxq.tpm.mode.strategy.ProductPreBuyCheck;
import com.gxq.tpm.mode.strategy.StrategyBuyingOrders.StrategyBuying;
import com.gxq.tpm.network.NetworkResultInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.CountDown.OnCountDownListener;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CCircleProgressBar;
import com.gxq.tpm.ui.CPopupWindow;
import com.gxq.tpm.ui.CTabTitleSelector;
import com.gxq.tpm.ui.KeyboardView;
import com.gxq.tpm.ui.ProductDialog;

public class StrategyBuyingActivity extends SuperActivity implements 
		DispatcherTimer.OnDispatcherTimerListener,
		View.OnClickListener, KeyboardView.OnKeyboardListener {
	private final static int BUY_STATUS = 1;
	private final static int GOTO_BACK = 2;
	
	private final static int WAIT_TIME = 15;
	
	private StrategyBuying mBuying;

	private ScrollView mContainerView;
	private View mInputView;
	private EditText mEtInputText;
	
	private CTabTitleSelector mFundTabSelector;
	private int[] mInitFund = {1, 2, 3, 4, 5};
	
	private View mBottomView, mContaienrAbountAmount;
	private TextView mTvCurPrice, mTvAboutAmount;
	
	private Button mBtnNext;
	
	private CPopupWindow mPwKeyboard;
	private KeyboardView mKeyboardView;
	
	private DispatcherTimer mHqTimer;
	private GetHandicapInfo mHandicapInfo;
	
	private double mTotalValue;
	private int mAmount = -1;
	
	private boolean mBackable = true; // 如果为false，界面不能退出
	private ProductBuyOrder mBuyOrder;
	
	private CountDown mCountDown;
	private boolean mTimeOut;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == BUY_STATUS) {
				if (!mTimeOut) {
					requestBuyStatus();
				}
			} else if (msg.what == GOTO_BACK) {
				Util.gotoStrategy(StrategyBuyingActivity.this, ProductIntent.REFRESH_BUYING);
				finish();
			}
		}
	};
	
	private OnCountDownListener mCountDownListener = new OnCountDownListener() {
		@Override
		public void onCountDownRun(int maxNum, int remainNum) {
		}
		@Override
		public void onCountDownFinished() {
			timeOut();
		}
	};
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle2(R.string.strategy_buying_title);
		getTitleBar().setLeftImage(R.drawable.nav_back_w);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_strategy_buying);
		
		mBuying = (StrategyBuying) getIntent().getSerializableExtra(ProductIntent.EXTRA_VALUE);
		
		mHqTimer = new DispatcherTimer(this, 3);
		
		initView();
		bindAction();
	}
	
	private void initView() {
		mContainerView = (ScrollView) findViewById(R.id.container_view);
		
		TextView tv = (TextView) findViewById(R.id.tv_investor_name);
		tv.setText(mBuying.dealer_replace);
		tv = (TextView) findViewById(R.id.tv_investor_fund);
		tv.setText(NumberFormat.tenThousand(NumberFormat.decimalFormat0(mBuying.fund/10000)));
		tv = (TextView) findViewById(R.id.tv_stock_name);
		tv.setText(mBuying.stock_name);
		
		tv = (TextView) findViewById(R.id.tv_today_time);
		tv.setText(Integer.toString(mBuying.times + 1));
		
		tv = (TextView) findViewById(R.id.tv_today_limit);
		tv.setText(getString(R.string.strategy_today_limit, mBuying.buy_max_times));
		
		CCircleProgressBar cpb = (CCircleProgressBar) findViewById(R.id.cpb_times);
		cpb.setProgress(mBuying.times + 1, mBuying.buy_max_times);
		
		tv = (TextView) findViewById(R.id.tv_to_investor_stock_name);
		tv.setText(mBuying.stock_name);
		
		tv = (TextView) findViewById(R.id.tv_to_buy_fund);
		tv.setText(NumberFormat.bigDecimalFormat(mBuying.left_fund));
		
		ProgressBar pb = (ProgressBar) findViewById(R.id.pb_to_buy_fund);
		pb.setProgress((int)(mBuying.left_fund * 100 / mBuying.fund));
		
		mInputView = findViewById(R.id.container);
		mEtInputText = (EditText) findViewById(R.id.et_input_fund);
		
		mFundTabSelector = (CTabTitleSelector) findViewById(R.id.container_fund);
		mFundTabSelector.setLayoutId(R.layout.strategy_fund_item);
		mFundTabSelector.setSeparate(getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_5));
		
		for (int index = 0; index < mInitFund.length; index++) {
			if (index == 0) {
				mFundTabSelector.newTabTitle(R.string.strategy_total_fund);
			} else {
				mFundTabSelector.newTabTitle("1/" + mInitFund[index]);
			}
		}
		
		mTvCurPrice = (TextView) findViewById(R.id.tv_cur_price);
		mContaienrAbountAmount = findViewById(R.id.container_about_amount);
		mTvAboutAmount = (TextView) findViewById(R.id.tv_about_amount);
		
		mBottomView = findViewById(R.id.bottom_container);
		
		mBtnNext = (Button) findViewById(R.id.btn_next_step);
		mBtnNext.setText(R.string.strategy_confirm_buying);
		
		checkButtonStatus();
	}

	private void bindAction() {
		findViewById(R.id.container_input_fund).setOnClickListener(this);
		
		mFundTabSelector.setOnTabTitleSelectListener(new CTabTitleSelector.OnTabTitleSelectListener() {
			
			@Override
			public void onSelection(int position) {
				mInputView.setBackgroundResource(R.drawable.btn_rect_b3b3b3);
				mEtInputText.setTextColor(getResources().getColor(R.color.text_color_main));
				
				setTotalValue(mBuying.left_fund / mInitFund[position]);
			}
		});
		
		mBtnNext.setOnClickListener(this);
	}
	
	private void setTotalValue(double totalValue) {
		mTotalValue = totalValue;
		if (mHandicapInfo != null) {
			 mAmount = calcAmount(mTotalValue);
			 mContaienrAbountAmount.setVisibility(View.VISIBLE);
			 mTvAboutAmount.setText(NumberFormat.stockAmountToString(mAmount));
			 
			 checkButtonStatus();
		}
		mEtInputText.setText(NumberFormat.decimalFormat(mTotalValue));
		Selection.setSelection(mEtInputText.getText(), mEtInputText.length());
	}
	
	private int calcAmount(double totalValue) {
		return (int) (totalValue / mHandicapInfo.price_max / 100) * 100;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		showWaitDialog(RequestInfo.GET_HANDICAP);
		mHqTimer.timerTaskControl(true);
	}
	
	@Override
	public void onAlarmClock(int operationType) {
		super.onAlarmClock(operationType);
		
		GetHandicapInfo.doRequest(mBuying.code, this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mHqTimer.timerTaskControl(false);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.container_input_fund:
			mEtInputText.setFocusable(true);
			mEtInputText.setFocusableInTouchMode(true);
			mEtInputText.requestFocus();
			
			mPwKeyboard = new CPopupWindow(this);
			mPwKeyboard.setContentView(R.layout.layout_keyboard_view);
			mKeyboardView = (KeyboardView) mPwKeyboard.findViewById(R.id.view_keyboard);
			
			mKeyboardView.setText(mEtInputText.getText().toString(), KeyboardView.EDIT_MODE_BUY);
			mKeyboardView.setOnKeyboardListener(this);
			mKeyboardView.setConfirmEnable(mAmount >= 0);
			
			moveContainerView(getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_214));
			
			mPwKeyboard.showAtLocation(mBottomView, Gravity.BOTTOM, 0, 0);
			mPwKeyboard.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					mContainerView.setTranslationY(0);
				}
			});
			
			break;
		case R.id.btn_next_step:
			requestPreBuyCheck();
			break;
		}
	}
	
	private void moveContainerView(int keyboardHeight) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int totalHeight = dm.heightPixels;
		
		mContainerView.scrollTo(0, totalHeight);
		
		int btnHeight = getResources().getDimensionPixelOffset(R.dimen.margin_xhdpi_64);
		
		mContainerView.setTranslationY(btnHeight - keyboardHeight);
		mContainerView.postInvalidate();
	}
	
	private void requestPreBuyCheck() {
		if (mAmount <= 0) {
			showToastMsg(R.string.strategy_buying_not_enough_amount);
		} else {
			showWaitDialog(RequestInfo.PRODUCT_PRE_BUY_CHECK);
			
			ProductPreBuyCheck.Params params = new ProductPreBuyCheck.Params();
			params.p_id = Integer.toString(mBuying.id);
			params.fund = mTotalValue;
			params.stock_amount = mAmount;
			params.start_price = mHandicapInfo == null ? 0 : mHandicapInfo.New;
			params.stock_code = mBuying.code;
			ProductPreBuyCheck.doRequest(params, this);
		}
	}
	
	private void requestBuyOrder() {
		mBackable = false;
		showWaitDialog(RequestInfo.PRODUCT_BUY_ORDER);
		
		ProductBuyOrder.Params params = new ProductBuyOrder.Params();
		params.p_id = Integer.toString(mBuying.id);
		params.fund = mTotalValue;
		params.stock_amount = mAmount;
		params.start_price = mHandicapInfo == null ? 0 : mHandicapInfo.New;
		ProductBuyOrder.doRequest(params, this);
	}
	
	private void requestBuyStatus() {
		ProductBuyStatus.Params params = new ProductBuyStatus.Params();
		params.order_id = mBuyOrder.order_id;
		ProductBuyStatus.doRequest(params, this);
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
	public void setText(String text) {
		mInputView.setBackgroundResource(R.drawable.btn_rect_tab_cur_selected);
		mEtInputText.setTextColor(getResources().getColor(R.color.tab_cur_selected));
		
		mTotalValue = Double.parseDouble(text);
		
		mFundTabSelector.setPosition(-1);
		mEtInputText.setText(text);
		Selection.setSelection(mEtInputText.getText(), mEtInputText.length());
		
		if (mHandicapInfo != null) {
			mAmount = calcAmount(mTotalValue);
			mContaienrAbountAmount.setVisibility(View.VISIBLE);
			mTvAboutAmount.setText(NumberFormat.stockAmountToString(mAmount));
			
			checkButtonStatus();
		}
		
		if (mKeyboardView != null) {
			mKeyboardView.setConfirmEnable(mAmount >= 0);
		}
	}
	
	private void checkButtonStatus() {
		mBtnNext.setEnabled(mAmount >= 0 && mBackable);
	}
	
	@Override
	public void onKeyboardFinish(boolean fullFund) {
		mPwKeyboard.dismiss();
		mKeyboardView = null;
		
		if (fullFund) {
			mFundTabSelector.setPosition(-1);
			
			setTotalValue(mBuying.left_fund);
		} else {
			requestPreBuyCheck();
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		
		if (info == RequestInfo.GET_HANDICAP) {
			GetHandicapInfo handicapInfo = (GetHandicapInfo) res;
			assignHandicapInfo(handicapInfo);
		} else if (info == RequestInfo.PRODUCT_PRE_BUY_CHECK) {
			ProductPreBuyCheck preBuyCheck = (ProductPreBuyCheck) res;
			if (BaseRes.RESULT_OK.equals(preBuyCheck.result)) {
				requestBuyOrder();
			} else {
				if (preBuyCheck.reason == 4) {
					if (preBuyCheck.error_reason == NetworkResultInfo.BUY_AMOUNT_NOT_MATCH.getValue()) {
						mAmount = preBuyCheck.stock_amount;
						mTvAboutAmount.setText(NumberFormat.stockAmountToString(mAmount));
					}
					new ProductDialog.Builder(this)
						.setContentText(preBuyCheck.msg)
						.setPositiveButton(R.string.btn_cancel, null)
						.setNegativeButton(R.string.btn_continue,
								new ProductDialog.ProductDialogListener() {
									@Override
									public void onDialogClick(int id) {
										requestBuyOrder();
									}
								}).create().show();
					
				} else if (preBuyCheck.reason == 7) {
					new ProductDialog.Builder(this)
						.setContentText(preBuyCheck.msg)
						.setPositiveButton(R.string.btn_look_again, null)
						.setNegativeButton(R.string.btn_continue,
								new ProductDialog.ProductDialogListener() {
									@Override
									public void onDialogClick(int id) {
										requestBuyOrder();
									}
								}).create().show();
				} else {
					requestBuyOrder();
				}
			}
		} else if (info == RequestInfo.PRODUCT_BUY_ORDER) {
			mBuyOrder = (ProductBuyOrder) res;

			if (mBuyOrder.order_id > 0) {
				mCountDown = new CountDown(WAIT_TIME);
				mCountDown.startTimer();
				mCountDown.setOnCountDownListener(mCountDownListener);
			
				requestBuyStatus();
				showWaitDialog(null);
			} else {
				mBackable = false;
				showToastMsg(res.error_msg);
			}
		} else if (info == RequestInfo.PRODUCT_BUY_STATUS) {
			ProductBuyStatus buyStatus = (ProductBuyStatus) res;
			
			if (buyStatus.state == 2) {
				hideWaitDialog(null);
				
				gotoFail();
				mCountDown.stopTimer();
			} else if (buyStatus.state == 3) {
				hideWaitDialog(null);
				
				gotoSuccess();
				mCountDown.stopTimer();
			} else {
				mHandler.sendEmptyMessageDelayed(BUY_STATUS, 1000);
			}
		}
	}
	
	private void gotoFail() {
		if (mBuying.times == mBuying.buy_max_times - 1) {
			new ProductDialog.Builder(this)
				.setContent(R.string.strategy_buying_fail, R.string.strategy_buying_fail_content)
				.setPositiveButton(R.string.btn_confirm, null)
				.create().show();
		} else {
			showToastMsg(R.string.strategy_buying_fail);
		}
		mBackable = true;
	}

	private void gotoSuccess() {
		if (mBuying.times == mBuying.buy_max_times - 1) {
			new ProductDialog.Builder(this)
				.setContent(R.string.strategy_buying_success, R.string.strategy_buying_success_content)
				.setPositiveButton(R.string.btn_goto_check, new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						Util.gotoStrategy(StrategyBuyingActivity.this, ProductIntent.REFRESH_NEW_NOTICING);
						finish();
					}
				}).create().show();
		} else {
			showToastMsg(R.string.strategy_buying_success);
			checkButtonStatus();
			
			mHandler.sendEmptyMessageDelayed(GOTO_BACK, 3000);
		}
	}
	
	private void timeOut() {
		mTimeOut = true;
		
		mHandler.removeMessages(BUY_STATUS);
		new ProductDialog.Builder(this)
			.setContent(R.string.strategy_buying_time_out, R.string.strategy_buying_time_out_content)
			.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
				@Override
				public void onDialogClick(int id) {
					if (mBuying.times == mBuying.buy_max_times - 1) {
						Util.gotoStrategy(StrategyBuyingActivity.this, ProductIntent.REFRESH_NEW_NOTICING);
					} else {
						Util.gotoStrategy(StrategyBuyingActivity.this, ProductIntent.REFRESH_BUYING);
					}
					finish();
				}
			}).create().show();
	}
	
	@Override
	public void netFinishError(RequestInfo info, BaseRes result, int tag) {
		if (info == RequestInfo.PRODUCT_BUY_ORDER) {
			if (result.error_code == NetworkResultInfo.BUY_AMOUNT_NOT_MATCH.getValue()) {
				mBuyOrder = (ProductBuyOrder) result;
				
				mAmount = mBuyOrder.stock_amount;
				mTvAboutAmount.setText(NumberFormat.stockAmountToString(mAmount));
				
				String text = getString(R.string.strategy_buying_order_content, mBuyOrder.stock_amount);
				new ProductDialog.Builder(this).
					setContentText(text).
					setPositiveButton(R.string.btn_cancel, null)
					.setNegativeButton(R.string.btn_continue, 
							new ProductDialog.ProductDialogListener() {
								@Override
								public void onDialogClick(int id) {
									requestBuyOrder();
								}
							}).create().show();
			}
		}
	}

	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (info == RequestInfo.PRODUCT_BUY_ORDER) {
			mBackable = true;
			if (what == NetworkResultInfo.BUY_AMOUNT_NOT_MATCH.getValue()) {
				return ERROR_NONE;
			}
		} else if (info == RequestInfo.PRODUCT_BUY_STATUS) {
			mHandler.sendEmptyMessageDelayed(BUY_STATUS, 1000);
			return ERROR_NONE;
		}
		
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void assignHandicapInfo(GetHandicapInfo handicapInfo) {
		if (handicapInfo == null) {
			mFundTabSelector.setEnabled(false);
		} else {
			mHandicapInfo = handicapInfo;
			
			float newPrice = mHandicapInfo.New > 0 ? mHandicapInfo.New : mHandicapInfo.YClose;
			mTvCurPrice.setText(NumberFormat.decimalFormat(newPrice));
		}
	}
	
}
