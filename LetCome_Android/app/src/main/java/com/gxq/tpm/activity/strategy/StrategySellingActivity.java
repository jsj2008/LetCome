package com.gxq.tpm.activity.strategy;

import android.os.Bundle;
import android.os.Handler;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.strategy.ProductProfit;
import com.gxq.tpm.mode.strategy.ProductProfit.Profit;
import com.gxq.tpm.mode.strategy.ProductSellOrder;
import com.gxq.tpm.mode.strategy.ProductSellStatus;
import com.gxq.tpm.mode.strategy.StrategySellingOrders.StrategySelling;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.CountDown.OnCountDownListener;
import com.gxq.tpm.ui.CCircleProgressBar;
import com.gxq.tpm.ui.CPopupWindow;
import com.gxq.tpm.ui.CTabTitleSelector;
import com.gxq.tpm.ui.KeyboardView;
import com.gxq.tpm.ui.KeyboardView.OnKeyboardListener;
import com.gxq.tpm.ui.ProductDialog;

public class StrategySellingActivity extends SuperActivity implements 
		DispatcherTimer.OnDispatcherTimerListener,OnClickListener,OnKeyboardListener{
	private final static int SELL_STATUS = 1;
	private final static int GOTO_BACK = 2;
	
	private final static int WAIT_TIME = 15;
	
	private ScrollView mContainerView;
	
	private TextView mTvBuyPrice, mTvCurPrice;
	private View mInputView; 
	private EditText mEtInputText;
	private Button mBtnNext;
	private View mBottomView;
	
	private StrategySelling mSelling;
	private DispatcherTimer mProfitTimer;
	
	//全部、1/2、1/3、1/4、1/5
	private CTabTitleSelector mAmountTabSelector;
	private int[] mInitFund = {1, 2, 3, 4, 5};
	
	private CPopupWindow mPwKeyboard;
	private KeyboardView mKeyboardView;
	
	private Profit mProfit;
	private ProductSellOrder mSellOrder;
	
	private CountDown mCountDown;
	private boolean mTimeOut;
	
	private long mStockAmount;
	private boolean mBackable = true; // 如果为true，界面不能退出
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == GOTO_BACK) {
				Util.gotoStrategy(StrategySellingActivity.this, ProductIntent.REFRESH_SELLING);
			} else if (msg.what == SELL_STATUS) {
				if (!mTimeOut) {
					requestSellStatus();
				}
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
		
		getTitleBar().setTitle2(R.string.strategy_selling_title);
		getTitleBar().setLeftImage(R.drawable.nav_back_w);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_strategy_selling);
		
		mSelling = (StrategySelling) getIntent().getSerializableExtra(ProductIntent.EXTRA_VALUE);

		mProfitTimer = new DispatcherTimer(this, 3);
		
		initView();
		bindAction();
	}
	
	private void initView() {
		mContainerView = (ScrollView) findViewById(R.id.container_view);
		
		TextView tv = (TextView) findViewById(R.id.tv_investor_name);
		tv.setText(mSelling.dealer_replace);
		tv = (TextView) findViewById(R.id.tv_investor_fund);
		tv.setText(NumberFormat.tenThousand(NumberFormat.decimalFormat0(mSelling.fund/10000)));
		tv = (TextView) findViewById(R.id.tv_stock_name);
		tv.setText(mSelling.stock_name);
		
		tv = (TextView) findViewById(R.id.tv_today_time);
		tv.setText(Integer.toString(mSelling.times + 1));
		
		tv = (TextView) findViewById(R.id.tv_today_limit);
		tv.setText(getString(R.string.strategy_today_limit, mSelling.sell_max_times));
		
		CCircleProgressBar cpb = (CCircleProgressBar) findViewById(R.id.cpb_times);
		cpb.setProgress(mSelling.times + 1, mSelling.sell_max_times);
		
		tv = (TextView) findViewById(R.id.tv_to_investor_stock_name);
		tv.setText(mSelling.stock_name);
		
		tv = (TextView) findViewById(R.id.tv_to_sell_amount);
		tv.setText(String.valueOf(mSelling.left_amount));
		
		mTvBuyPrice = (TextView) findViewById(R.id.tv_to_buy_start_price);
		
		mInputView = findViewById(R.id.container);
		mEtInputText = (EditText) findViewById(R.id.et_input_fund);
		mEtInputText.setEnabled(mSelling.times+1 < mSelling.sell_max_times);
		mAmountTabSelector = (CTabTitleSelector) findViewById(R.id.container_fund);
		mAmountTabSelector.setLayoutId(R.layout.strategy_fund_item);
		mAmountTabSelector.setSeparate(getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_5));
		
		for (int index = 0; index < mInitFund.length; index++) {
			if (index == 0) {
				mAmountTabSelector.newTabTitle(R.string.strategy_selling_total_amount);
			} else {
				mAmountTabSelector.newTabTitle("1/" + mInitFund[index]);
				mAmountTabSelector.setEnable(index, (mSelling.times+1 < mSelling.sell_max_times));
			}
		}
		
		mTvCurPrice = (TextView) findViewById(R.id.tv_cur_price);
		mBottomView = findViewById(R.id.bottom_container);
		
		mBtnNext = (Button) findViewById(R.id.btn_next_step);
		mBtnNext.setText(R.string.strategy_confirm_buying);
		
		mBtnNext.setEnabled(false);
	}
	
	private void bindAction() {
		findViewById(R.id.container_input_fund).setOnClickListener(this);
		
		mAmountTabSelector.setOnTabTitleSelectListener(new CTabTitleSelector.OnTabTitleSelectListener() {
			
			@Override
			public void onSelection(int position) {
				mInputView.setBackgroundResource(R.drawable.btn_rect_b3b3b3);
				mEtInputText.setTextColor(getResources().getColor(R.color.text_color_main));
				
				if (position > 0) {
					setStockAmount(mSelling.left_amount / mInitFund[position] / 100 * 100);
				} else {
					setStockAmount(mSelling.left_amount);
				}
			}
		});
		
		mBtnNext.setOnClickListener(this);
	}
	
	private void setStockAmount(long stockAmount) {
		this.mStockAmount = stockAmount/100*100;
		
		checkButtonStatus();
		
		mEtInputText.setText(Long.toString(mStockAmount));
		Selection.setSelection(mEtInputText.getText(), mEtInputText.length());
		
		if (mKeyboardView != null) {
			mKeyboardView.setConfirmEnable(mStockAmount > 0);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mProfitTimer.timerTaskControl(true);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mProfitTimer.timerTaskControl(false);
	}
	
	@Override
	public void onAlarmClock(int operationType) {
		super.onAlarmClock(operationType);
		ProductProfit.Params params = new ProductProfit.Params();
		params.p_id_arr = String.valueOf(mSelling.id);
		ProductProfit.doRequest(params, this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.container_input_fund:
			mEtInputText.setFocusable(true);
			mEtInputText.setFocusableInTouchMode(true);
			mEtInputText.requestFocus();
			
			if (mSelling.times + 1 == mSelling.sell_max_times) {
				return;
			}
			
			mPwKeyboard = new CPopupWindow(this);
			mPwKeyboard.setContentView(R.layout.layout_keyboard_view);
			mKeyboardView = (KeyboardView) mPwKeyboard.findViewById(R.id.view_keyboard);
			
			mKeyboardView.setText(mEtInputText.getText().toString(), KeyboardView.EDIT_MODE_SELL);
			mKeyboardView.setOnKeyboardListener(this);
			mKeyboardView.setConfirmEnable(mStockAmount > 0);
			
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
			requestSellOrder();
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
		
		mAmountTabSelector.setPosition(-1);
		
		setStockAmount(Long.parseLong(text));
	}
	
	@Override
	public void onKeyboardFinish(boolean fullFund) {
		mPwKeyboard.dismiss();
		mKeyboardView = null;
		
		if (fullFund) {
			mAmountTabSelector.setPosition(-1);
			
			setStockAmount(mSelling.left_amount);
		} else {
			requestSellOrder();
		}
	}
	
	private void requestSellOrder() {
		mBackable = false;
		if (checkInputValid()) {
			showWaitDialog(RequestInfo.PRODUCT_SELL_ORDER);
			
			ProductSellOrder.Params params = new ProductSellOrder.Params();
			
			params.p_id = mSelling.id;
			params.stock_amount = mStockAmount / 100 * 100;
			params.start_price = mProfit != null ? mProfit.cur_price : 0;
			ProductSellOrder.doRequest(params, this);
		} else {
			mBackable = true;
		}
	}
	
	private void requestSellStatus() {
		ProductSellStatus.doRequest(mSellOrder.order_id, this); 
	}
	
	private boolean checkInputValid(){
		if (mStockAmount == mSelling.left_amount) {
			return true;
		} else {
		    if (mStockAmount % 100 > 0 ) {
		    	showToastMsg(R.string.strategy_selling_must_hundred_multiple);
		        return false;
		    }
		    
		    if (mStockAmount > mSelling.left_amount) {
		    	showToastMsg(R.string.strategy_selling_input_number_greater_than);
		        return false;
		    }
		    
		    if (mSelling.times >= mSelling.sell_max_times - 1 && mStockAmount != mSelling.left_amount) {
		    	showToastMsg(R.string.strategy_selling_last_time);
		        return false;
		    }
		    return true;
		}	    
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		if (info == RequestInfo.PRODUCT_PROFIT){
			ProductProfit productProfit = (ProductProfit) res;
			if (productProfit.records != null && productProfit.records.size() > 0){
				Profit profit = productProfit.records.get(0);
				if (profit.id == mSelling.id){
					mProfit = profit;
					
					mTvBuyPrice.setText(getString(R.string.strategy_to_buy_start_price,
							NumberFormat.decimalFormat(profit.buy_deal_price_avg)));
					mTvCurPrice.setText(NumberFormat.decimalFormat(profit.cur_price));
					
					checkButtonStatus();
				}
			}
		} else if (info == RequestInfo.PRODUCT_SELL_ORDER) {
			mSellOrder = (ProductSellOrder)res;
			mCountDown = new CountDown(WAIT_TIME);
			mCountDown.startTimer();
			mCountDown.setOnCountDownListener(mCountDownListener);
		
			requestSellStatus();
			showWaitDialog(null);
		} else if (info == RequestInfo.PRODUCT_SELL_STATUS) {
			ProductSellStatus sellStatus = (ProductSellStatus) res;
			
			if (sellStatus.state == 2) {
				hideWaitDialog(null);
				
				gotoFail();
				mCountDown.stopTimer();
			} else if (sellStatus.state == 3) {
				hideWaitDialog(null);
				
				gotoSuccess();
				mCountDown.stopTimer();
			} else {
				mHandler.sendEmptyMessageDelayed(SELL_STATUS, 1000);
			}
		}
	}
	
	private void gotoFail() {
		mBackable = true;
		showToastMsg(R.string.strategy_sell_fail_title);
	}

	private void gotoSuccess() {
		if (mSelling.times >= mSelling.sell_max_times - 1) {
			new ProductDialog.Builder(this)
				.setContent(R.string.strategy_sell_success_title, R.string.strategy_sell_success_content)
				.setPositiveButton(R.string.btn_goto_check,
						new ProductDialog.ProductDialogListener() {
							@Override
							public void onDialogClick(int id) {
								gotoAccount();
							}
				}).create().show();
        } else {
        	showToastMsg(R.string.strategy_sell_success_title);
			mHandler.sendEmptyMessageDelayed(GOTO_BACK, 3000);
        }
	}
	
	private void gotoAccount(){
		Util.gotoStrategy(StrategySellingActivity.this, ProductIntent.REFRESH_NEW_ACCOUNT);
	}

	private void timeOut() {
		mTimeOut = true;
		
		mHandler.removeMessages(SELL_STATUS);
		new ProductDialog.Builder(this)
			.setContent(R.string.strategy_selling_time_out, R.string.strategy_selling_time_out_content)
			.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
				@Override
				public void onDialogClick(int id) {
					if (mSelling.times >= mSelling.buy_max_times - 1) {
						gotoAccount();
					} else {
						Util.gotoStrategy(StrategySellingActivity.this, ProductIntent.REFRESH_SELLING);
					}
					finish();
				}
			}).create().show();
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (info == RequestInfo.PRODUCT_SELL_ORDER) {
			mBackable = true;
		} else if (info == RequestInfo.PRODUCT_SELL_STATUS) {
			mHandler.sendEmptyMessageDelayed(SELL_STATUS, 1000);
		}
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void checkButtonStatus() {
		mBtnNext.setEnabled(mProfit != null && BaseRes.RESULT_OK.equals(mProfit.is_trade_time)
				&& mStockAmount > 0 && mBackable);
	}
	
}
