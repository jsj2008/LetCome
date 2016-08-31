package com.gxq.tpm.activity.account;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.account.ProductDetails;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SettlementDetailActivity extends SuperActivity implements OnClickListener{
	
	private static final String BUY_YTPE = "1";
	private static final String SELL_YTPE = "2";
	
	private View mViewBuyExecuteResult,mViewSellExecuteResult;
	
	private TextView mTvBuyDate, mTvBuyTimes,mTvBuyFund,
	                 mTvBuyPrice,mTvBuyAmount;
	private TextView mTvApplySellDate, mTvYSellProfitRate,
					 mTvYColosePrice, mTvApplySellDays,
	                 mTvApplyTitle;
	private TextView mTvSellDate, mTvSellTimes,mTvSellFund,
	                 mTvSellPrice, mTvSellAmount,mTvSellTitle;
	
	private int p_id;
	
	private ProductDetails productDetails;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settlement_detail);
		
		p_id = getIntent().getIntExtra(ProductIntent.EXTRA_ID, -1);
		initView();
		initAction();
		requestData();
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.settlement_detail_title);
		getTitleBar().showBackImage();
	}
	
	private void initView() {	
		mViewBuyExecuteResult=findViewById(R.id.view_buy_execute_result);
		mViewSellExecuteResult=findViewById(R.id.view_sell_execute_result);
		
		mTvBuyDate = (TextView) findViewById(R.id.tv_buy_date);
		mTvBuyTimes = (TextView) findViewById(R.id.tv_buy_times);
		mTvBuyFund = (TextView) findViewById(R.id.tv_buy_fund);
		mTvBuyPrice = (TextView) findViewById(R.id.tv_buy_price);
		mTvBuyAmount = (TextView) findViewById(R.id.tv_buy_amount);
		
		mTvApplySellDate = (TextView) findViewById(R.id.tv_apply_sell_date);
		mTvApplySellDays = (TextView) findViewById(R.id.tv_apply_sell_days);
		mTvYColosePrice = (TextView) findViewById(R.id.tv_y_colose_price);
		mTvYSellProfitRate = (TextView) findViewById(R.id.tv_y_sell_profit_rate);
		mTvApplyTitle = (TextView) findViewById(R.id.tv_notify_sell_date);
		
		mTvSellDate = (TextView) findViewById(R.id.tv_sell_date);
		mTvSellTimes = (TextView) findViewById(R.id.tv_sell_times);
		mTvSellFund = (TextView) findViewById(R.id.tv_sell_fund);
		mTvSellPrice = (TextView) findViewById(R.id.tv_sell_price);
		mTvSellAmount = (TextView) findViewById(R.id.tv_sell_amount);
		mTvSellTitle = (TextView) findViewById(R.id.tv_sell_execute_result);
	}
	
	private void initAction() {
		mViewBuyExecuteResult.setOnClickListener(this);
		mViewSellExecuteResult.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int pid=0;
		if(productDetails!=null){
			pid=productDetails.p_id;
		}
		Intent intent=new Intent(SettlementDetailActivity.this,TransactionStreamActivity.class);
		intent.putExtra(ProductIntent.EXTRA_DETIAL_PID,pid);
		if(v.getId()==R.id.view_buy_execute_result){
        	intent.putExtra(ProductIntent.EXTRA_DETIAL_TYPE, BUY_YTPE);
        }else if(v.getId()==R.id.view_sell_execute_result){
        	intent.putExtra(ProductIntent.EXTRA_DETIAL_TYPE, SELL_YTPE);
		}
        startActivity(intent);	
	}
	
	private void requestData(){
		if(p_id == -1){
			return;
		}
		ProductDetails.Params params = new ProductDetails.Params();
		params.p_id = this.p_id;
		params.p_type=GlobalConstant.p_type;
		ProductDetails.doRequest(params, SettlementDetailActivity.this);
		showWaitDialog(RequestInfo.PRODUCT_DETAILS);
	}
	
	private void updateView(ProductDetails details){
		if (null == details) {
			return;
		}
		productDetails = details;
		mTvBuyDate.setText(TimeFormat.milliToMonthMonth(
				Long.parseLong(productDetails.buy_date)));
		mTvBuyTimes.setText(NumberFormat.getStrategyTimes(productDetails.buy_times));
		mTvBuyFund.setText(NumberFormat.bigDecimalFormat(productDetails.buy_fund));
		mTvBuyPrice.setText(NumberFormat.decimalFormat(productDetails.buy_deal_avg_price));
		mTvBuyAmount.setText(String.valueOf(productDetails.buy_amount));
		mTvApplySellDate.setText(TimeFormat.milliToMonthMonth(
				Long.parseLong(productDetails.apply_sell_date)));
		mTvApplySellDays.setText(
				Util.transformString(R.string.settlement_detail_sell_days, 
						productDetails.apply_sell_days));
		mTvYColosePrice.setText(NumberFormat.decimalFormat(productDetails.y_close));
		mTvYSellProfitRate.setText(NumberFormat.percentWithSymbol(productDetails.apply_sell_profit_rate));
		mTvYSellProfitRate.setTextColor(Util.getColorByPlusMinus(productDetails.apply_sell_profit_rate));
		mTvSellDate.setText(TimeFormat.milliToMonthMonth(productDetails.sell_date));
		mTvSellTimes.setText(NumberFormat.getStrategyTimes(productDetails.sell_times));
		mTvSellFund.setText(NumberFormat.bigDecimalFormat(productDetails.sell_fund));
		
		//账单生成中显示
		if(productDetails.sell_deal_avg_price==0.0){
			mTvSellPrice.setText(Util.transformString(R.string.default_value));
			mTvSellPrice.setTextColor(Util.transformColor(R.color.color_cccccc));
		}else{
			mTvSellPrice.setText(NumberFormat.decimalFormat(productDetails.sell_deal_avg_price));
			mTvSellPrice.setTextColor(Util.transformColor(R.color.color_222222));
		}
		if(productDetails.sell_amount==0){
			mTvSellAmount.setText(Util.transformString(R.string.default_value));
			mTvSellAmount.setTextColor(Util.transformColor(R.color.color_cccccc));
		}else{
			mTvSellAmount.setText(String.valueOf(productDetails.sell_amount));
			mTvSellAmount.setTextColor(Util.transformColor(R.color.color_222222));
		}
		if(productDetails.state == 1){
			mTvApplyTitle.setText(R.string.settlement_detail_notify_sell_date);
			mTvSellTitle.setText(R.string.settlement_detail_sell_execute_result);
		}else if(productDetails.state == 2){
			mTvApplyTitle.setText(R.string.settlement_detail_notify_sell_date_deadline);
			mTvSellTitle.setText(R.string.settlement_detail_sell_execute_result_deadline);
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if(RequestInfo.PRODUCT_DETAILS == info){
			hideWaitDialog(info);
			ProductDetails details = (ProductDetails) res;
			updateView(details);
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if(RequestInfo.PRODUCT_DO_SETTLE == info){
			
		}
		return super.netFinishError(info, what, msg, tag);
	}
}
