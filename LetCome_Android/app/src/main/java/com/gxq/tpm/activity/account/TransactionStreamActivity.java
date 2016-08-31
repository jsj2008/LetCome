package com.gxq.tpm.activity.account;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.TransactionStreamAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.account.TransactionStream;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.ui.CListView;

import android.os.Bundle;
import android.text.TextUtils;

public class TransactionStreamActivity extends SuperActivity{
    private CListView mlistview;
    private TransactionStreamAdapter mTSadapter;
    private int pid;
    private String type;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_stream);
		mlistview=(CListView)findViewById(R.id.ts_list);
		mlistview.setMoreEnable(false);
		mlistview.setRefreshEnable(false);
		
		pid=getIntent().getIntExtra(ProductIntent.EXTRA_DETIAL_PID, 0);
		type=getIntent().getStringExtra(ProductIntent.EXTRA_DETIAL_TYPE);
		
		if(type.equals("1")){
			getTitleBar().setTitle(R.string.transaction_stream_title_buy);
		}else if(type.equals("2")){
			getTitleBar().setTitle(R.string.transaction_stream_title_sell);
		}else{
			getTitleBar().setTitle("");
		}
		getTitleBar().showBackImage();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(pid!=0&&type!=null&&!TextUtils.isEmpty(type)){
			showWaitDialog(RequestInfo.PRODUCT_DEAL_HISTORY);
			requestList();
		}
	}

	private void requestList() {
		TransactionStream.Params params = new TransactionStream.Params();
		params.p_id = pid;
		params.type =type ;//
		TransactionStream.doRequest(params, this);
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		if (RequestInfo.PRODUCT_DEAL_HISTORY == info) {
			TransactionStream orders = (TransactionStream) res;
			if (mTSadapter == null) {
				mTSadapter = new TransactionStreamAdapter(this);
				mlistview.setAdapter(mTSadapter);
			} 
			if(orders.records!=null&&orders.records.size()>0){
				mTSadapter.setList(orders.records);
			}
		}
	
	}

	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		return super.netFinishError(info, what, msg, tag);
	}

}
