package com.gxq.tpm.activity.cooperation;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SignAgreementActivity;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UnsignAgreement;
import com.gxq.tpm.mode.cooperation.AgreementGetTips;
import com.gxq.tpm.mode.cooperation.ConfirmRisk;
import com.gxq.tpm.mode.cooperation.GetArticleContent;
import com.gxq.tpm.mode.cooperation.GetArticleList;
import com.gxq.tpm.mode.cooperation.ProductCreatePolicy;
import com.gxq.tpm.mode.cooperation.ProductPolicyList;
import com.gxq.tpm.mode.cooperation.ProductPolicyStatus;
import com.gxq.tpm.mode.cooperation.ProductPrePolicyCheck;
import com.gxq.tpm.mode.cooperation.ProductSchemeDetail;
import com.gxq.tpm.mode.cooperation.ProductPolicyList.Policy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.CountDown;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.WebHelper;
import com.gxq.tpm.tools.CountDown.OnCountDownListener;
import com.gxq.tpm.tools.WebHelper.WebParams;
import com.gxq.tpm.ui.AgreementSignContainer;
import com.gxq.tpm.ui.CHtmlDialog;
import com.gxq.tpm.ui.CPopupWindow;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.ProductDialog.ProductDialogListener;

public class CooperationStrategyConfirmActivity extends SuperActivity implements OnClickListener{
	
	private final static int FEEDBACK_SECOND = 15;
	
	private static final int MSG_STATE_CHECK = 1;
	
	private static final int TIPS_TYPE_LOSS = 101;
	private static final int TIPS_TYPE_FUND = 102;
	
	private TextView mTvTipsLoss, mTvTipsFund, mTvFreezeLoss, mTvFreezeFund, mTvPay, mTvArticleTitle, mTvArticle;
	private Button BtnNextStep;
	
	private CPopupWindow popupWindow;
	private CheckBox checkBox;
	
	private ProductSchemeDetail schemeDetail;
	private ProductPolicyList.Policy policy;
	
	private GetArticleList.Article article;
	
	private boolean mBuyOrder;
	private int p_id;	
		
	private CountDown countDown;
	
	private ProductDialog mTimeOutDialog;
	
	private String mTipsLoss;
	private String mTipsFund;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_STATE_CHECK:
				requestPolicyStatus();
				break;
			}
		}
	};
	
	private OnCountDownListener mCountDownListener = new OnCountDownListener() {
		@Override
		public void onCountDownRun(int maxNum, int remainNum) {
		}
		@Override
		public void onCountDownFinished() {
			mBuyOrder = false;
			hideWaitDialog(null);
			countDown.stopTimer();
			showTimeoutDialog();	
			mHandler.removeMessages(MSG_STATE_CHECK);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_strategy_confirm);
		
		schemeDetail = (ProductSchemeDetail) getIntent().getSerializableExtra(ProductIntent.EXTRA_SCHEME_DETAIL);
		policy = (Policy) getIntent().getSerializableExtra(ProductIntent.EXTRA_POLICY);
		
		initView();
		initAction();
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.cooperation_strategy_confirm_title);
		getTitleBar().showBackImage();
	}
	
	@Override
	public void onLeftClick(View v) {
		if (!mBuyOrder) {
			super.onLeftClick(v);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (!mBuyOrder) {
			super.onBackPressed();
		}
	}
	
	private void initView(){
		mTvTipsLoss = (TextView) findViewById(R.id.tv_tips_loss);
		mTvTipsFund = (TextView) findViewById(R.id.tv_tips_fund);
		
		mTvFreezeLoss = (TextView) findViewById(R.id.tv_freeze_loss);
		mTvFreezeFund = (TextView) findViewById(R.id.tv_freeze_fund);
		mTvPay = (TextView) findViewById(R.id.tv_pay);
		
		checkBox = (CheckBox) findViewById(R.id.cb_article);
//		checkBox.setEnabled(false);
		
		mTvArticle = (TextView) findViewById(R.id.tv_article);
		
		mTvArticleTitle = (TextView) findViewById(R.id.tv_article_title);
		mTvArticleTitle.setText(getString(R.string.cooperation_strategy_confirm_article_name, getString(R.string.default_value)));
		mTvArticleTitle.setEnabled(false);
		
		BtnNextStep = (Button) findViewById(R.id.btn_next_step);
		BtnNextStep.setText(R.string.cooperation_strategy_confirm_next_step);
		BtnNextStep.setEnabled(false);
	}
	
	private void initAction(){
		mTvTipsLoss.setOnClickListener(this);
		mTvTipsFund.setOnClickListener(this);
		mTvArticleTitle.setOnClickListener(this);
		BtnNextStep.setOnClickListener(this);
		
		mTvArticle.setOnClickListener(this);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					BtnNextStep.setEnabled(true);
				}else{
					BtnNextStep.setEnabled(false);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tips_loss:
//			showTipsLossDialog();
			if(TextUtils.isEmpty(mTipsLoss)){
				requestAgreementTips(TIPS_TYPE_LOSS, TIPS_TYPE_LOSS);
			}else{
				showTipsLossDialog();
			}
			break;
		case R.id.tv_tips_fund:
//			showTipsFundDialog();
			if(TextUtils.isEmpty(mTipsFund)){
				requestAgreementTips(TIPS_TYPE_FUND, TIPS_TYPE_FUND);
			}else{
				showTipsFundDialog();
			}
			break;
		case R.id.tv_article:
			checkBox.setChecked(!checkBox.isChecked());
			break;
		case R.id.tv_article_title:
			if(null != article){
				requestArticleContent(article.id);
			}
			break;
		case R.id.btn_next_step:
			prePolicyCheck();
			break;
		case R.id.iv_close:
			popupWindow.dismiss();
			break;
		case R.id.btn_lgb_next_step:
			gotoNextStep();
			popupWindow.dismiss();
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		assignSchemeDetail(schemeDetail);
		
		requestArticleList();
	}
	
	private void assignSchemeDetail(ProductSchemeDetail schemeDetail){
		if(null == schemeDetail){
			return;
		}
		NumberFormat nf = new DecimalFormat("#,###.00");
		mTvFreezeLoss.setText(Util.transformString(R.string.money_symbol)+nf.format(schemeDetail.bid_bond));
		mTvFreezeFund.setText(Util.transformString(R.string.money_symbol)+nf.format(schemeDetail.break_cost));
		mTvPay.setText(Util.transformString(R.string.money_symbol)+nf.format(schemeDetail.fee));
	}
	
	private void gotoNextStep(){
		ProductCreatePolicy.Params params = new ProductCreatePolicy.Params();
		params.scheme_id = policy.scheme_id; //	方案号	Int	TRUE
		params.investor_id = policy.investor_id; //	投资人ID	Int	TRUE
		params.stock_code = policy.stock_code; //	股票代码	string	TRUE
		params.fund = policy.fund; //	本金	Float	TRUE
		params.cell_id = policy.cell_id;
		ProductCreatePolicy.doRequest(params, CooperationStrategyConfirmActivity.this);
//		showWaitDialog(RequestInfo.PRODUCT_CREATE_POLICY);
		showWaitDialog(null);
		mBuyOrder = true;
	}
	
	private void requestAgreementTips(int type, int tag){
		AgreementGetTips.Params params = new AgreementGetTips.Params();
		params.type = type; //	类型 101：亏损赔付履约金重要提示 102:成本补偿履约金重要提示	Int	TRUE
		params.scheme_id = policy.scheme_id;
		AgreementGetTips.doRequest(params, CooperationStrategyConfirmActivity.this, tag);
		showWaitDialog(RequestInfo.AGREEMENT_GET_TIPS);
	}
	
	private void requestPolicyStatus(){
		if(0 == p_id){
			return;
		}
		ProductPolicyStatus.Params params = new ProductPolicyStatus.Params();
		params.p_id = p_id;
		ProductPolicyStatus.doRequest(params, this);
//		showWaitDialog(RequestInfo.PRODUCT_POLICY_STATUS);
	}
	
	private void requestArticleList(){
		showWaitDialog(RequestInfo.P_ARTICLE_LIST);
		GetArticleList.Params params = new GetArticleList.Params();
		params.p_type = policy.p_type;
		params.user_type = 1;
		GetArticleList.doRequest(params, CooperationStrategyConfirmActivity.this);
	}
	
	private void requestArticleContent(int id){
		if(null == schemeDetail){
			return;
		}
		GetArticleContent.Params params = new GetArticleContent.Params();
		params.id = id;
		
		params.nickname_t = policy.dealer_replace;// 投资人昵称 String TRUE 
		params.uid_t = policy.investor_id+"";// 投资人UID String TRUE
		params.scheme_id = policy.scheme_id;
//		params.apportionment = NumberFormat.percentWithInteger(mSchemeItem.distribution.user_profit);
		
//		params.stockName = policy.stock_name;// 交易品种(参数例：使用展示文字 “招商银行”) String TRUE 
//		params.stockCode = policy.stock_code;// 股票代码(股票时需要，使用展示文字 参数例：“600036”) String TRUE 
//		params.amount = mTvAmount.getText().toString();
//		params.holdType = mHoldTime;
//		params.zy = mTvZy.getText().toString();// 止盈（使用展示文字 参数例：“240指数点/手”） String TRUE 
//		params.zs = mTvZs.getText().toString();// 止损（使用展示文字 参数例：“180指数点/手”） String TRUE
//		params.bid_bond = freeze+"";
		
		GetArticleContent.doRequest(params, CooperationStrategyConfirmActivity.this);
		showWaitDialog(RequestInfo.P_ARTICLE_CONTENT);
	}
	
	private void prePolicyCheck(){
		ProductPrePolicyCheck.Params params = new ProductPrePolicyCheck.Params();
		params.scheme_id = policy.scheme_id;
		params.investor_id = policy.investor_id;
		params.stock_code = policy.stock_code;
		params.fund = policy.fund;
		params.cell_id = policy.cell_id;
		ProductPrePolicyCheck.doRequest(params, CooperationStrategyConfirmActivity.this);
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
		if(info == RequestInfo.AGREEMENT_GET_TIPS){
			AgreementGetTips getTips = (AgreementGetTips) res;
			if(tag == TIPS_TYPE_FUND){
				mTipsFund = getTips.content;
				showTipsFundDialog();
			}else if(tag == TIPS_TYPE_LOSS){
				mTipsLoss = getTips.content;
				showTipsLossDialog();
			}
		} else if (info == RequestInfo.P_ARTICLE_LIST){
			hideWaitDialog(info);
			GetArticleList articleList = (GetArticleList) res;
			if(null != articleList && null != articleList.records && articleList.records.size() > 0){
				article = articleList.records.get(0);
				mTvArticleTitle.setText(getString(R.string.cooperation_strategy_confirm_article_name, article.name));
				mTvArticleTitle.setEnabled(true);
				checkBox.setEnabled(true);
			}
		} else if (info == RequestInfo.P_ARTICLE_CONTENT) {
			hideWaitDialog(info);
			GetArticleContent detail = (GetArticleContent) res;
			if(null != detail && null != detail.content) { 
				showArticleDialog(detail);
			}
		} else if (info == RequestInfo.PRODUCT_PRE_POLICY_CHECK){
			hideWaitDialog(info);
			ProductPrePolicyCheck prePolicyCheck = (ProductPrePolicyCheck) res;
			if (BaseRes.RESULT_OK.equals(prePolicyCheck.result)) {
				showLgbPopup();
			} else {
				if (prePolicyCheck.reason == ProductPrePolicyCheck.AUTH) {
					showAuthDialog(prePolicyCheck);
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.RISK_NOTICE) {
					showRiskNoticeDialog(prePolicyCheck);
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.RISK_INFORM) {
					if (mUserPrefs.showAuthInform()) {
						showAuthInfromDialog(prePolicyCheck);
					} else {
						showLgbPopup();
					}
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.AGREEMENT) {
					requestPlatformUnsignAgreement();
				} else if (prePolicyCheck.reason == ProductPrePolicyCheck.NICKNAME) {
					showLgbPopup();
				} else if(prePolicyCheck.reason == ProductPrePolicyCheck.CONTINUE_NORMAL){
					showContinueNormalDialog(prePolicyCheck);
				} else {
					showLgbPopup();
				}
			}
		} else if (info == RequestInfo.PROTOCOL_UNSIGNED) {
			UnsignAgreement unsign = (UnsignAgreement) res;
			if (unsign.res_data != null && unsign.res_data.size() > 0) {
				if (unsign.res_data.size() > 0) {
					if (unsign.res_data.size() == 1) {
						showSingleAgreement(unsign.res_data.get(0));
					} else {
						showMultiAgreement(unsign.res_data);
					}
				} else {
					showLgbPopup();
				}
			}
		} else if (info == RequestInfo.PRODUCT_CREATE_POLICY){
			hideWaitDialog(info);
			ProductCreatePolicy createPolicy = (ProductCreatePolicy) res;
			if (BaseRes.RESULT_OK.equals(createPolicy.result) && createPolicy.id > 0) {
				p_id = createPolicy.id;
				
				countDown = new CountDown(FEEDBACK_SECOND);
				countDown.setOnCountDownListener(mCountDownListener);
				countDown.startTimer();
				
				requestPolicyStatus();
			}
		} else if (info == RequestInfo.PRODUCT_POLICY_STATUS){
			ProductPolicyStatus policyStatus = (ProductPolicyStatus) res;
			assignStatus(policyStatus);
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (what == 30007 || what == 30015 || what == 30037 || what == 30034) {
//			mUserPrefs.saveSchemes(null, mProduct.p_type);
			
//			Intent intent = new Intent(this, StrategyCooperationActivity.class);
//			intent.putExtra(ProductIntent.EXTRA_PRODUCT, mProduct);
//			intent.putExtra(ProductIntent.EXTRA_STOCK, mStock);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			finish();
			return ERROR_TOAST;
		}
		if (info == RequestInfo.PRODUCT_CREATE_POLICY) {
			mBuyOrder = false;
			hideWaitDialog(null);
			networkResultErr(msg);
			if (what == 30303) {
				new ProductDialog.Builder(this)
					.setContentText(msg)
					.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
						@Override
						public void onDialogClick(int id) {
//							requestHandicapInfo(); 
						}
					}).create().show();
				return ERROR_NONE;
			} else if (what == 30014) {
				showLgbErrorDialog();
				return ERROR_NONE;
			}
			
		}else if(info == RequestInfo.PRODUCT_POLICY_STATUS){
			postMSG(MSG_STATE_CHECK, 1000);
			return ERROR_NONE;
		}
		return super.netFinishError(info, what, msg, tag);
	}	
	
	private void showTipsLossDialog(){
		ProductDialog mDialog = new ProductDialog.Builder(this).setContentView(R.layout.dialog_tips_loss).create();
		mDialog.setCancelable(null);
		mDialog.show();
	}
	
	private void showTipsFundDialog(){
		ProductDialog mDialog = new ProductDialog.Builder(this).setContentView(R.layout.dialog_tips_fund).create();
		mDialog.setCancelable(null);
		mDialog.show();
	}
	
	private void showArticleDialog(GetArticleContent content){
		CHtmlDialog CDialog = new CHtmlDialog(this);
		CDialog.show();
		CDialog.setContent(article.name, content.content);
	}
	
	@SuppressLint("ClickableViewAccessibility") 
	private void showLgbPopup(){
			popupWindow = new CPopupWindow(CooperationStrategyConfirmActivity.this);
			LayoutInflater layoutInflater = LayoutInflater.from(this);
			final View mView = layoutInflater.inflate(R.layout.popup_window_lgb, null, false);
			popupWindow.setContentView(mView);
			popupWindow.setAnimationStyle(R.style.anim_up_style);
			
			mView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int height = mView.findViewById(R.id.ll_top).getTop();
					int y=(int) event.getY();
					if(event.getAction()==MotionEvent.ACTION_UP){
						if(y<height){
							popupWindow.dismiss();
						}
					}				
					return true;
				}
			});
			
			TextView mTvLgbFreezeLoss = (TextView) popupWindow.findViewById(R.id.tv_freeze_loss);
			TextView mTvLgbFreezeFund = (TextView) popupWindow.findViewById(R.id.tv_freeze_fund);
			TextView mTvLgbFreezeTotal = (TextView) popupWindow.findViewById(R.id.tv_freeze_total);
			TextView mTvLgbPayCharge = (TextView) popupWindow.findViewById(R.id.tv_pay_charge);
			TextView mTvLgbPayTotal = (TextView) popupWindow.findViewById(R.id.tv_pay_total);
			
			NumberFormat nf = new DecimalFormat("#,###.00");
			mTvLgbFreezeLoss.setText(nf.format(schemeDetail.bid_bond));
			mTvLgbFreezeFund.setText(nf.format(schemeDetail.break_cost));
			mTvLgbFreezeTotal.setText(Util.transformString(R.string.money_symbol)+nf.format(schemeDetail.bid_bond + schemeDetail.break_cost));
			mTvLgbPayCharge.setText(nf.format(schemeDetail.fee));
			mTvLgbPayTotal.setText(Util.transformString(R.string.money_symbol)+nf.format(schemeDetail.fee));
			
			popupWindow.findViewById(R.id.btn_lgb_next_step).setOnClickListener(this);
			popupWindow.findViewById(R.id.iv_close).setOnClickListener(this);
			popupWindow.showAtLocation(findViewById(R.id.btn_next_step), Gravity.BOTTOM, 0, 0);
	}
	
	private void showLgbErrorDialog(){
		ProductDialog ErrorDialog = new ProductDialog.Builder(this).setContentText(R.string.cooperation_lgb_not_enough)
				.setPositiveButton(R.string.cooperation_lgb_not_enough_give_up, null)
				.setNegativeButton(R.string.cooperation_lgb_not_enough_recharge, new ProductDialogListener() {
					
					@Override
					public void onDialogClick(int id) {
						jumpUrl(getString(R.string.user_prod_recharge_title),
								WebHelper.addParam(RequestInfo.PROD_URL)
								.addParam(WebParams.TYPE, 1).getUrl(), true);
						finish();
					}
				}).create();
		ErrorDialog.show();
	}
	
	private void showAuthDialog(final ProductPrePolicyCheck prePolicyCheck) {
		Util.showAuthDialog(this, prePolicyCheck, null,
				new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						Util.gotoAuth(CooperationStrategyConfirmActivity.this);
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
						ConfirmRisk.doRequest(params, CooperationStrategyConfirmActivity.this);
					}
				});
	}

	private void showAuthInfromDialog(final ProductPrePolicyCheck prePolicyCheck) {
		Util.showAuthInfromDialog(this, prePolicyCheck, new ProductDialog.ProductDialogListener() {
				@Override
				public void onDialogClick(int id) {
					mUserPrefs.saveAuthInform();
					showLgbPopup();
				}
			}, mAuthFinishListener);
	}
	
	private ProductDialog.ProductDialogListener mAuthFinishListener = new ProductDialog.ProductDialogListener() {
		public void onDialogClick(int id) {
			showLgbPopup();
		}
	};
	
	public void jumpUrl(String title, String url, boolean finishDirect) {
		Intent intent = new Intent(CooperationStrategyConfirmActivity.this, WebActivity.class);
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
	
	private void showContinueNormalDialog(final ProductPrePolicyCheck prePolicyCheck){
		new ProductDialog.Builder(CooperationStrategyConfirmActivity.this)
		.setContentText(prePolicyCheck.msg)
		.setPositiveButton(R.string.cooperation_confirm_give_up, null)
		.setNegativeButton(R.string.cooperation_confirm_continue, new ProductDialogListener() {
			
			@Override
			public void onDialogClick(int id) {
				showLgbPopup();
			}
		})
		.create().show();
	}
	
	private void assignStatus(ProductPolicyStatus status) {
		if(null != mTimeOutDialog && mTimeOutDialog.isShowing()){
			return;
		}
		if(status.state == 1){
			mBuyOrder = false;
			hideWaitDialog(null);
			countDown.stopTimer();
			showSuccessDialog();
			mHandler.removeMessages(MSG_STATE_CHECK);
		} else if(status.state == 2){
			mBuyOrder = false;
			hideWaitDialog(null);
			countDown.stopTimer();
			showFailDialog();
			mHandler.removeMessages(MSG_STATE_CHECK);
		} else {
			postMSG(MSG_STATE_CHECK, 1000);
		}		
	}
	
	private void postMSG(final int what, final long delay) {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(what, delay);
		}
	}
	
	private void showSuccessDialog(){
		new ProductDialog.Builder(CooperationStrategyConfirmActivity.this)
		.setContent(R.string.cooperation_strategy_feedback_success_title, getString(R.string.cooperation_strategy_feedback_success, policy.dealer_replace))
		.setPositiveButton(R.string.cooperation_feedback_go_to_see_implement, new ProductDialogListener() {
			
			@Override
			public void onDialogClick(int id) {
				gotoStrategyBuying();
			}
		} ).create().show();
	}
	
	private void showFailDialog(){
		new ProductDialog.Builder(CooperationStrategyConfirmActivity.this)
		.setContent(R.string.cooperation_strategy_feedback_fail_title, getString(R.string.cooperation_strategy_feedback_fail))
		.setPositiveButton(R.string.cooperation_feedback_result_confirm, null).create().show();
	}
	
	private void showTimeoutDialog(){
		mTimeOutDialog = new ProductDialog.Builder(CooperationStrategyConfirmActivity.this)
		.setContent(R.string.cooperation_strategy_feedback_timeout_title, getString(R.string.cooperation_strategy_feedback_timeout))
		.setPositiveButton(R.string.cooperation_feedback_result_confirm, new ProductDialogListener() {
			
			@Override
			public void onDialogClick(int id) {
				gotoStrategyBuying();
			}
		}).create();
		mTimeOutDialog.show();
	}
	
	private void gotoStrategyBuying() {
		Util.gotoStrategy(this, ProductIntent.REFRESH_NEW_BUYING);
		finish();
	}
}
