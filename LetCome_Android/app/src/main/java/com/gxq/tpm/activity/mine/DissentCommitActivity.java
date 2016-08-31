package com.gxq.tpm.activity.mine;

import java.util.ArrayList;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.DissentPointAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.account.ProductDetails;
import com.gxq.tpm.mode.account.SettlementOrders.Settlement;
import com.gxq.tpm.mode.mine.DissentCreate;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CPopupWindow;
import com.gxq.tpm.ui.ProductDialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DissentCommitActivity extends SuperActivity implements
		OnClickListener {
	private TextView mTvTradePno, mTvPoint;
	private TextView mTvCurValue;
	private EditText mTvNewValue;
	private EditText mTvReason;
	private TextView mTvDissentCreate;
	
	private int mPoint = 0;
	private int mSelectedIndex = -1;
	private Settlement mSettlement;
	
	private ArrayList<String> types = new ArrayList<String>();

	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.user_dissent_commit_title);
		getTitleBar().showBackImage();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dissent_commit);
		
		mSettlement = (Settlement) getIntent().getSerializableExtra(ProductIntent.EXTRA_SEELEMENT);
		
		initUI();
	}
	
	private void initUI() {
		mTvTradePno = (TextView) findViewById(R.id.dissent_trade_pno);
		mTvTradePno.setText(mSettlement.pno);
		mTvPoint = (TextView) findViewById(R.id.dissent_point);
		
		mTvTradePno.setOnClickListener(this);
		
		mTvPoint.setOnClickListener(this);
		ImageView iv = (ImageView) findViewById(R.id.iv_point);
		iv.setOnClickListener(this);
		
		mTvCurValue = (TextView)findViewById(R.id.dissent_cur_value);
		mTvNewValue = (EditText)findViewById(R.id.dissent_new_value);
		mTvReason = (EditText) findViewById(R.id.dissent_reason);
		
		initType();
		
		mTvDissentCreate = (TextView) findViewById(R.id.dissent_create);
		mTvDissentCreate.setOnClickListener(this);
    }

	// 0、信息服务费 1、买入价 2、卖出价 3、合作权益 4、结算 5、其他
	private void initType(){
		types.add(getString(R.string.dissent_type_service_pay));
		types.add(getString(R.string.dissent_type_buying_price));
		types.add(getString(R.string.dissent_type_selling_price));
		types.add(getString(R.string.dissent_type_cooperation_feedback_pay));
		types.add(getString(R.string.dissent_type_settlement));
		types.add(getString(R.string.dissent_type_other));
	}
	
	// 异议点 1买入价 2卖出价 3盈亏分配 4交易数量 5亏损赔付 6履约金解冻 7其它 8 交易费用 9 止损 10 结算
	private void changePointStatus(int index) {
		if (mSettlement == null) return;
		
		switch (index) {
		case 0: // 信息服务费 
			mPoint = 8;
			mTvCurValue.setText(NumberFormat.decimalFormat(mSettlement.fee));
			mTvNewValue.setVisibility(View.VISIBLE);
			break;
		case 1: // 买入价
			mPoint = 1;
			mTvCurValue.setText(NumberFormat.decimalFormat(mSettlement.buy_deal_price_avg));
			mTvNewValue.setVisibility(View.VISIBLE);
			break;
		case 2: // 卖出价
			mPoint = 2;
			mTvCurValue.setText(NumberFormat.decimalFormat(mSettlement.sell_deal_price_avg));
			mTvNewValue.setVisibility(View.VISIBLE);
			break;
		case 3: // 合作权益
			if (mSettlement.user_profit >= 0) {
				mPoint = 3;
			} else {
				mPoint = 5;
			}
			mTvCurValue.setText(NumberFormat.decimalFormat(mSettlement.user_profit));
			mTvNewValue.setVisibility(View.VISIBLE);
			break;
		case 4: // 结算
			mPoint = 10;
			int a=1;
			if (/*mSettlement.settlemnt_flag*/ a> 0) {
				mTvCurValue.setText(R.string.dissent_deal);
			} else {
				mTvCurValue.setText(R.string.dissent_undeal);
			}
			mTvNewValue.setVisibility(View.INVISIBLE);
			break;
		case 5: // 其他
			mPoint = 7;
			mTvCurValue.setText(R.string.dissent_none);
			mTvNewValue.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	protected void onResume() {
	    super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_point:
		case R.id.dissent_point:
			if (mSettlement == null)
				return;
			
			showDissentPoint();
			break;
		case R.id.dissent_create:
			if (mSettlement == null) {
				showToastMsg(R.string.dissent_note_select_pno);
				return;
			}
			if (mPoint == 0) {
				showToastMsg(R.string.dissent_note_select_point);
				return;
			}
			
			String content = mTvReason.getText().toString();
			if (Util.isEmpty(content)) {
				showToastMsg(R.string.dissent_note_input_reason);
				return;
			}
			if (content.length() < 5) {
				showToastMsg(R.string.dissent_note_reason_not_enough);
				return;
			}
			DissentCreate.Params params = new DissentCreate.Params();
			params.p_type = GlobalConstant.p_type;
			params.p_id = mSettlement.id;
			params.point = mPoint;
			String value = mTvNewValue.getText().toString();
			if (Util.isEmpty(value))
				value = "0";
			params.price = Float.parseFloat(value);
			params.reason = content;
			DissentCreate.doRequest(params, this);
			showWaitDialog(RequestInfo.S_DISSENT_COMMIT);
			break;
		}
	}
	
	private void showDissentPoint() {
		final CPopupWindow popupWindow = new CPopupWindow(this);
		popupWindow.setContentView(R.layout.popup_window_dissent_point);
		ListView listView = (ListView) popupWindow.findViewById(R.id.listview);
		final DissentPointAdapter adapter = new DissentPointAdapter(this);
		adapter.setList(types);
		adapter.setSelected(mSelectedIndex);
		listView.setAdapter(adapter);
		
		popupWindow.setPopupWindowOnClickListener(R.id.btn_finish, 
				new CPopupWindow.OnPopupWindowClickListener() {
			@Override
			public void onPopupWindowClick(View view) {
				mSelectedIndex = adapter.getSelected();
				if (mSelectedIndex >= 0) {
					mTvPoint.setText(types.get(adapter.getSelected()));
					changePointStatus(adapter.getSelected());
				}
				popupWindow.dismiss();
			}
		});
		popupWindow.setPopupWindowOnClickListener(R.id.btn_cancel, 
				new CPopupWindow.OnPopupWindowClickListener() {
			@Override
			public void onPopupWindowClick(View view) {
				popupWindow.dismiss();
			}
		});
		popupWindow.showAtLocation(findViewById(R.id.dissent_create), Gravity.BOTTOM, 0, 0);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		super.netFinishOk(info, result, tag);
		if (RequestInfo.S_DISSENT_COMMIT == info) {
			DissentCreate create = (DissentCreate) result;
			if (BaseRes.RESULT_OK.equals(create.result)) {
				new ProductDialog.Builder(this)
					.setContentText(R.string.dissent_commit_y, R.drawable.layer_success)
					.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
						@Override
						public void onDialogClick(int id) {
								setResult(RESULT_OK);
								finish();
							}
						}).create().show();
			} else {
				fail(create.error_msg);
			}
		}
	}

	private void fail(String msg) {
		new ProductDialog.Builder(this)
			.setContentText(msg, R.drawable.layer_remind)
			.setPositiveButton(R.string.btn_confirm, null)
			.create().show();
    }
	
	@Override
	public int netFinishError(RequestInfo info, final int what, String msg, int tag) {
		if (RequestInfo.S_DISSENT_COMMIT == info) {
			showToastMsg(msg);
			return 0;
		} else {
			return super.netFinishError(info, what, msg, tag);
		}
	}

}
