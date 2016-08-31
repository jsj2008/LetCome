package com.gxq.tpm.activity.cooperation;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SignAgreementActivity;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UnsignAgreement;
import com.gxq.tpm.mode.cooperation.ConfirmRisk;
import com.gxq.tpm.mode.cooperation.ProductPolicyList;
import com.gxq.tpm.mode.cooperation.ProductPrePolicyCheck;
import com.gxq.tpm.mode.cooperation.ProductPreSimpleCheck;
import com.gxq.tpm.mode.cooperation.ProductSchemeDetail;
import com.gxq.tpm.mode.cooperation.ProductPolicyList.Policy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.AgreementSignContainer;
import com.gxq.tpm.ui.ProductDialog;

public class CooperationStrategyEquityActivity extends SuperActivity implements OnClickListener{

	private TextView mTvStopProfitPointSuccess, mTvStopProfitPointFail,
				mTvSuccessCost, mTvSuccessProfit, mTvSuccessLoss,
				mTvFailCost, mTvFailProfit, mTvFailLoss, mTvNotice;
	
	private Button BtnNextStep;
	
	private ProductSchemeDetail schemeDetail;
	private ProductPolicyList.Policy policy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strategy_equity);
		
		schemeDetail = (ProductSchemeDetail) getIntent().getSerializableExtra(ProductIntent.EXTRA_SCHEME_DETAIL);
		policy = (Policy) getIntent().getSerializableExtra(ProductIntent.EXTRA_POLICY);
		
		initView();
		initAction();
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.cooperation_strategy_equity_title);
		getTitleBar().showBackImage();
	}
	
	private void initView(){
		mTvStopProfitPointSuccess = (TextView) findViewById(R.id.tv_stop_profit_point_success);
		mTvStopProfitPointFail = (TextView) findViewById(R.id.tv_stop_profit_point_fail);
		mTvSuccessCost = (TextView) findViewById(R.id.tv_success_cost);
		mTvSuccessProfit = (TextView) findViewById(R.id.tv_success_profit);
		mTvSuccessLoss = (TextView) findViewById(R.id.tv_success_loss);
		mTvFailCost = (TextView) findViewById(R.id.tv_fail_cost);
		mTvFailProfit = (TextView) findViewById(R.id.tv_fail_profit);
		mTvFailLoss = (TextView) findViewById(R.id.tv_fail_loss);
		
		mTvNotice = (TextView) findViewById(R.id.tv_bottom_notice);
		
		BtnNextStep = (Button) findViewById(R.id.btn_next_step);
		BtnNextStep.setText(R.string.cooperation_strategy_equity_next_step);
		BtnNextStep.setEnabled(false);
	}
	
	private void initAction(){
		BtnNextStep.setOnClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		assignSchemeDetail(schemeDetail);
		preSimpleCheck();
	}
	
	private void assignSchemeDetail(ProductSchemeDetail schemeDetail){
		if(null == schemeDetail){
			String strSuccess = getString(R.string.cooperation_strategy_equity_stop_profit_point_success, getString(R.string.default_value));
			int start = strSuccess.indexOf(getString(R.string.default_value));
			int end = start + getString(R.string.default_value).length();
			mTvStopProfitPointSuccess.setText(Util.strChangeColor(strSuccess, start, end, Util.transformColor(R.color.color_ff7814)));
			
			String strFail = getString(R.string.cooperation_strategy_equity_stop_profit_point_fail, getString(R.string.default_value));
			start = strFail.indexOf(getString(R.string.default_value));
			end = start + getString(R.string.default_value).length();
			mTvStopProfitPointFail.setText(Util.strChangeColor(strFail, start, end, Util.transformColor(R.color.color_ff7814)));
			return;
		}
		String strSuccess = getString(R.string.cooperation_strategy_equity_stop_profit_point_success, NumberFormat.percentWithInteger(schemeDetail.stop_profit_point));
		int start = strSuccess.indexOf(NumberFormat.percentWithInteger(schemeDetail.stop_profit_point));
		int end = start + NumberFormat.percentWithInteger(schemeDetail.stop_profit_point).length();
		mTvStopProfitPointSuccess.setText(Util.strChangeColor(strSuccess, start, end, Util.transformColor(R.color.color_ff7814)));
		
		String strFail = getString(R.string.cooperation_strategy_equity_stop_profit_point_fail, NumberFormat.percentWithInteger(schemeDetail.stop_profit_point));
		start = strFail.indexOf(NumberFormat.percentWithInteger(schemeDetail.stop_profit_point));
		end = start + NumberFormat.percentWithInteger(schemeDetail.stop_profit_point).length();
		mTvStopProfitPointFail.setText(Util.strChangeColor(strFail, start, end, Util.transformColor(R.color.color_ff7814)));
		
		mTvSuccessCost.setText(schemeDetail.success_cost);
		mTvSuccessProfit.setText("x"+schemeDetail.success_profit);
		mTvSuccessLoss.setText(schemeDetail.success_loss);
		mTvFailCost.setText(schemeDetail.fail_cost);
		mTvFailProfit.setText("x"+schemeDetail.fail_profit);
		mTvFailLoss.setText(schemeDetail.fail_loss);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next_step:
			if(!App.getUserPrefs().hasUserLogin()){
				showLoginActivity(new LoginCallback() {
					
					@Override
					public void login() {						
					}
					
					@Override
					public boolean isStrategy() {
						return true;
					}
					
					@Override
					public void cancel() {
					}
				});
			}else{
				prePolicyCheck();
			}
			break;
		}
	}
	
	private void gotoNext(){
		Intent intent = new Intent(CooperationStrategyEquityActivity.this, CooperationStrategyConfirmActivity.class);
		intent.putExtra(ProductIntent.EXTRA_POLICY, policy);
		intent.putExtra(ProductIntent.EXTRA_SCHEME_DETAIL, schemeDetail);
		startActivity(intent);
	}
	
	private void preSimpleCheck(){
		if(null == policy){
			return;
		}
		ProductPreSimpleCheck.Params params = new ProductPreSimpleCheck.Params();
		params.stock_code = policy.stock_code;
		ProductPreSimpleCheck.doRequest(params, CooperationStrategyEquityActivity.this);
		showWaitDialog(RequestInfo.PRODUCT_PRE_SIMPLE_CHECK);
	}
	
	private void prePolicyCheck(){
		ProductPrePolicyCheck.Params params = new ProductPrePolicyCheck.Params();
		params.scheme_id = policy.scheme_id;
		params.investor_id = policy.investor_id;
		params.stock_code = policy.stock_code;
		params.fund = policy.fund;
		params.cell_id = policy.cell_id;
		ProductPrePolicyCheck.doRequest(params, CooperationStrategyEquityActivity.this);
		showWaitDialog(RequestInfo.PRODUCT_PRE_POLICY_CHECK);
	}
	
	private void requestPlatformUnsignAgreement() {
		UnsignAgreement.Params params = new UnsignAgreement.Params();
		params.prd_type = GlobalConstant.PRODUCT_TYPE_PLATFORM;
		params.user_type = GlobalConstant.USER_TYPE_STRATEGY;
		UnsignAgreement.doRequest(params, this);
		
		showWaitDialog(RequestInfo.PROTOCOL_UNSIGNED);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if(RequestInfo.PRODUCT_PRE_SIMPLE_CHECK == info){
			ProductPreSimpleCheck preSimpleCheck = (ProductPreSimpleCheck) res;
			if(null != preSimpleCheck){
				if(!TextUtils.isEmpty(preSimpleCheck.error_reason_msg)){
					mTvNotice.setVisibility(View.VISIBLE);
					mTvNotice.setText(preSimpleCheck.error_reason_msg);
					BtnNextStep.setEnabled(false);
				}else{
					mTvNotice.setVisibility(View.GONE);
					BtnNextStep.setEnabled(true);
				}
			}
		} else if(RequestInfo.PRODUCT_PRE_POLICY_CHECK == info){
			ProductPrePolicyCheck prePolicyCheck = (ProductPrePolicyCheck) res;
			if (BaseRes.RESULT_OK.equals(prePolicyCheck.result)) {
				gotoNext();
			} else {
				if (prePolicyCheck.reason == ProductPrePolicyCheck.AUTH) {
					showAuthDialog(prePolicyCheck);
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.RISK_NOTICE) {
					showRiskNoticeDialog(prePolicyCheck);
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.RISK_INFORM) {
					if (mUserPrefs.showAuthInform()) {
						showAuthInfromDialog(prePolicyCheck);
					} else {
						gotoNext();
					}
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.AGREEMENT) {
					requestPlatformUnsignAgreement();
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.NICKNAME) {
					gotoNext();
				} else {
					gotoNext();
				}
			}
		}	else if (info == RequestInfo.PROTOCOL_UNSIGNED) {
			UnsignAgreement unsign = (UnsignAgreement) res;
			if (unsign.res_data != null && unsign.res_data.size() > 0) {
				if (unsign.res_data.size() > 0) {
					if (unsign.res_data.size() == 1) {
						showSingleAgreement(unsign.res_data.get(0));
					} else {
						showMultiAgreement(unsign.res_data);
					}
				} else {
					gotoNext();
				}
			}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if(RequestInfo.PRODUCT_PRE_SIMPLE_CHECK == info){
			mTvNotice.setVisibility(View.GONE);
			BtnNextStep.setEnabled(true);
			return ERROR_NONE;
		}
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void showAuthDialog(final ProductPrePolicyCheck prePolicyCheck) {
		Util.showAuthDialog(this, prePolicyCheck, null,
				new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						Util.gotoAuth(CooperationStrategyEquityActivity.this);
						finish();
					}
				});
	}
	
	private void showRiskNoticeDialog(final ProductPrePolicyCheck prePolicyCheck) {
		Util.showRiskNoticeDialog(this, prePolicyCheck, null,
				new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						ConfirmRisk.Params params = new ConfirmRisk.Params();
						params.msg_id = prePolicyCheck.msg_id;
						ConfirmRisk.doRequest(params, CooperationStrategyEquityActivity.this);
					}
				});
	}

	private void showAuthInfromDialog(final ProductPrePolicyCheck prePolicyCheck) {
		Util.showAuthInfromDialog(this, prePolicyCheck, new ProductDialog.ProductDialogListener() {
				@Override
				public void onDialogClick(int id) {
					mUserPrefs.saveAuthInform();
					gotoNext();
				}
			}, mAuthFinishListener);
	}
	
	private ProductDialog.ProductDialogListener mAuthFinishListener = new ProductDialog.ProductDialogListener() {
		public void onDialogClick(int id) {
			gotoNext();
		}
	};
	
	public void jumpUrl(String title, String url, boolean finishDirect) {
		Intent intent = new Intent(CooperationStrategyEquityActivity.this, WebActivity.class);
		intent.putExtra(ProductIntent.EXTRA_TITLE, title);
		intent.putExtra(ProductIntent.EXTRA_URL, url);
		intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true); 
		intent.putExtra(ProductIntent.EXTRA_FINISH_DIRECT, true);
		startActivity(intent);
	}
	
	private void showSingleAgreement(UnsignAgreement.Agreement agreement) {
		Intent intent = new Intent(this, SignAgreementActivity.class);
		intent.putExtra(ProductIntent.EXTRA_UNSIGN_AGREEMENT, agreement);
		intent.putExtra(ProductIntent.EXTRA_TYPE, ProductIntent.UNSIGN_AGREEMENT_SIGN);
		
		startActivity(intent);
	}
	
	private void showMultiAgreement(List<UnsignAgreement.Agreement> agreements) {
		ProductDialog.Builder builder = new ProductDialog.Builder(this);
		AgreementSignContainer signContainer = new AgreementSignContainer(this);
		for (UnsignAgreement.Agreement agreement : agreements) {
			signContainer.addAgreement(agreement);
		}
		final ProductDialog dialog = builder.setContentView(signContainer).create();
		signContainer.setOnAgreementSignedListener(new AgreementSignContainer.OnAgreementSignedListener() {
			@Override
			public void onAgreementSigned() {
				dialog.dismiss();
			}
		});	
		dialog.show();
	}
}
