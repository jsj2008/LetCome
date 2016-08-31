package com.gxq.tpm.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.DissentPay;
import com.gxq.tpm.mode.mine.UserCompensateOrders.CompensateDetail;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CItemBar;
import com.gxq.tpm.ui.ProductDialog;

public class UserCompensateDetailActivity extends SuperActivity  implements View.OnClickListener {
	
	private CItemBar mPnoItem, mTimeItem,mDissentTimeItem, mWayItem, mMoneyItem, mStatusItem;
	private TextView mTvPay;
	private TextView mTvfixfailInfo;

	private CompensateDetail mDetail;
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.user_compensate_detail);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settlement_correction_detail);
		mDetail = (CompensateDetail) getIntent().getSerializableExtra(ProductIntent.EXTRA_DETAIL);
		
		mPnoItem = (CItemBar) findViewById(R.id.settlement_correction_pno);
		mTimeItem = (CItemBar) findViewById(R.id.settlement_correction_time);
		mDissentTimeItem=(CItemBar) findViewById(R.id.settlement_compensate_time);
		mWayItem = (CItemBar) findViewById(R.id.settlement_correction_way);
		mMoneyItem = (CItemBar) findViewById(R.id.dissent_correction_money);
		mStatusItem = (CItemBar) findViewById(R.id.dissent_correction_status);
		
		mTvfixfailInfo=(TextView) findViewById(R.id.fix_fail_time_str);
		
		mTvPay = (TextView) findViewById(R.id.settlement_correction_button);
		mTvPay.setOnClickListener(this);
		
		initView();
	}
	
	private void initView() {
		mPnoItem.setContent(mDetail.pno);
		mWayItem.setContent(mDetail.type_name);
		mTimeItem.setContent(TimeFormat.dateDefaultAllFormat(mDetail.start_time));
		mDissentTimeItem.setContent(TimeFormat.dateDefaultAllFormat(mDetail.create_time));
		
		String moneyFormat = "0.00";
		int color = R.color.gain_color;
				
		// 0 未支付 1 已支付 2 未补偿 3 已补偿 4支付中
		if (mDetail.state == 0) {
			mTvPay.setVisibility(View.VISIBLE);
			color = R.color.loss_color;
			mMoneyItem.setTitle(getString(R.string.compensate_pay));
		} else {
			mTvPay.setVisibility(View.GONE);
			mStatusItem.setVisibility(View.VISIBLE);
			
			if (mDetail.state == 1 || mDetail.state == 4) {
				color = R.color.loss_color;
				mMoneyItem.setTitle(getString(R.string.compensate_pay));
			} else if (mDetail.state == 2 || mDetail.state == 3) {
				color = R.color.gain_color;
				mMoneyItem.setTitle(getString(R.string.compensate_receive));
			}
		}
		mStatusItem.setContent(mDetail.state_name);
		//支付中，显示提示文字
		if (mDetail.fix_fail_time_str != null && !mDetail.fix_fail_time_str.isEmpty())
			mTvfixfailInfo.setText(mDetail.fix_fail_time_str);
		
		mMoneyItem.setContent(NumberFormat.moneySymbol( 
				NumberFormat.decimalFormat(moneyFormat, Math.abs(mDetail.delta_profit))));
		mMoneyItem.setContentColor(getResources().getColor(color));
	}
	
	@Override
	public void onClick(View v) {
		DissentPay.Params params = new DissentPay.Params();
//	params.p_type=App.prefs.getProductType();
		params.p_type = /*GlobalConstant.PRODUCT_ALL*/mDetail.p_type;
		params.p_id = mDetail.p_id;
		params.fix_id = mDetail.id;
		DissentPay.doRequest(params, this);
		showWaitDialog(RequestInfo.S_DISSENT_CORRECTION_PAY);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		super.netFinishOk(info, result, tag);
		if (RequestInfo.S_DISSENT_CORRECTION_PAY == info) {
			hideWaitDialog(info);
			DissentPay pay = (DissentPay) result;
			if (BaseRes.RESULT_OK.equals(pay.result)) {
				hideWaitDialog(null);
				new ProductDialog.Builder(this)
					.setContentText(R.string.compensate_pay_y, R.drawable.layer_success)
					.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
						
						@Override
						public void onDialogClick(int id) {
							finish();
						}
					}).create().show();
			} else {
				fail(pay.error_msg);
			}
		}
	}

	private void fail(String msg) {
		new ProductDialog.Builder(this)
			.setContentText((msg == null ? "" : msg) + Util.transformString(R.string.dissent_pay_n), R.drawable.layer_remind)
			.setPositiveButton(R.string.btn_confirm, null)
			.create().show();
    }
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		int type = 0;
		if (RequestInfo.S_DISSENT_CORRECTION_PAY == info) {
			type = 2;
			fail(msg);
		} else {
			type = 1;
			networkResultErr(msg);
		}
		return type;
	}
}
