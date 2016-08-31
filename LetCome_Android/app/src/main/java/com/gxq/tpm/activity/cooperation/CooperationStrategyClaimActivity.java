package com.gxq.tpm.activity.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.StockHqActivity;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.cooperation.ProductPolicyList;
import com.gxq.tpm.mode.cooperation.ProductPolicyList.Policy;
import com.gxq.tpm.mode.cooperation.ProductSchemeDetail;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.NumberFormat;

public class CooperationStrategyClaimActivity extends SuperActivity implements OnClickListener{
	
	private TextView mTvInvestorName, mTvFund, mTvStockName, mTvHoldTime,
					mTvBuyTime, mTvBuyWay, mTvBuyTimes, mTvApplySellDate, mTvApplySellTime,
					mTvSellTime, mTvSellWay, mTvSellTimes;
	
	private Button BtnNextStep;
	
	private ProductPolicyList.Policy policy;
	private ProductSchemeDetail schemeDetail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strategy_claim);
		
		policy = (Policy) getIntent().getSerializableExtra(ProductIntent.EXTRA_POLICY);
		
		initView();
		initAction();
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.cooperation_strategy_claim_title);
		getTitleBar().showBackImage();
	}
	
	private void initView(){
		mTvInvestorName = (TextView) findViewById(R.id.tv_investor_name);
		mTvFund = (TextView) findViewById(R.id.tv_fund);
		mTvStockName = (TextView) findViewById(R.id.tv_stock_name);
		
		mTvHoldTime = (TextView) findViewById(R.id.tv_hold_time);
		
		mTvBuyTime = (TextView) findViewById(R.id.tv_buy_time);
		mTvBuyWay = (TextView) findViewById(R.id.tv_buy_way);
		mTvBuyTimes = (TextView) findViewById(R.id.tv_buy_times);
		
		mTvApplySellDate = (TextView) findViewById(R.id.tv_apply_sell_date);
		mTvApplySellTime = (TextView) findViewById(R.id.tv_apply_sell_time);
		
		mTvSellTime = (TextView) findViewById(R.id.tv_sell_time);
		mTvSellWay = (TextView) findViewById(R.id.tv_sell_way);
		mTvSellTimes = (TextView) findViewById(R.id.tv_sell_times);
		
		BtnNextStep = (Button) findViewById(R.id.btn_next_step);
		BtnNextStep.setText(R.string.cooperation_strategy_claim_next_step);
		BtnNextStep.setEnabled(false);
	}
	
	private void initAction(){
		mTvStockName.setOnClickListener(this);
		BtnNextStep.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		requestSchemeDetail();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_stock_name:
			intent = new Intent(CooperationStrategyClaimActivity.this, StockHqActivity.class);
			intent.putExtra(ProductIntent.EXTRA_STOCK_CODE, policy.stock_code);
			intent.putExtra(ProductIntent.EXTRA_STOCK, policy.stock_name);
			startActivity(intent);
			break;
		case R.id.btn_next_step:
			intent = new Intent(CooperationStrategyClaimActivity.this, CooperationStrategyEquityActivity.class);
			intent.putExtra(ProductIntent.EXTRA_POLICY, policy);
			intent.putExtra(ProductIntent.EXTRA_SCHEME_DETAIL, schemeDetail);
			startActivity(intent);
			break;
		}
	}
	
	private void requestSchemeDetail(){
		if(null == policy){
			return;
		}
		ProductSchemeDetail.Params params = new ProductSchemeDetail.Params();
		params.scheme_id = policy.scheme_id;
		ProductSchemeDetail.doRequest(params, CooperationStrategyClaimActivity.this);
		showWaitDialog(RequestInfo.PRODUCT_SCHEME_DETAIL);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if(RequestInfo.PRODUCT_SCHEME_DETAIL == info){
			ProductSchemeDetail detail = (ProductSchemeDetail) res;
			if(null != detail){
				schemeDetail = detail;
				assignSchemeDetail(detail);
				BtnNextStep.setEnabled(true);
			}
		}
	}
	
	private void assignSchemeDetail(ProductSchemeDetail detail){
		mTvInvestorName.setText(policy.investor_code);
		mTvFund.setText(getString(R.string.cooperation_strategy_claim_fund, NumberFormat.decimalFormat0(policy.fund/10000)));
		mTvStockName.setText(policy.stock_name);
		mTvHoldTime.setText(detail.hold_time);
		mTvBuyTime.setText(detail.buy_time);
		mTvBuyWay.setText(detail.buy_way);
		mTvBuyTimes.setText(detail.buy_times);
		mTvApplySellDate.setText(detail.apply_sell_date);
		mTvApplySellTime.setText(detail.apply_sell_time);
		mTvSellTime.setText(detail.sell_time); 
		mTvSellWay.setText(detail.sell_way); 
		mTvSellTimes.setText(detail.sell_times);
	}

}
