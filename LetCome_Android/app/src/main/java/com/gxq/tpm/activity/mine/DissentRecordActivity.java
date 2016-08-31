package com.gxq.tpm.activity.mine;

import com.gxq.tpm.GlobalConstant;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.ArrayListAdapter;
import com.gxq.tpm.adapter.DissentAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.DissentOrders;
import com.gxq.tpm.mode.mine.DissentOrders.DissentDetail;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.CListViewAdapterHelper;
import com.gxq.tpm.ui.CEmptyNoteView;
import com.gxq.tpm.ui.CListView;

import android.os.Bundle;
import android.view.View;

public class DissentRecordActivity extends SuperActivity {
	public static final int COUNT = 15;
	
	private CListView mList;
	private DissentDetailAdapterHelper mAdapterHelper;
	private DissentAdapter mAdapter;
	private View mContainerDissent, mContainerEmpty;

	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.user_dissent_record);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dissent_record);
		
		mContainerDissent = findViewById(R.id.container_dissent);
		
		mList = (CListView) findViewById(R.id.dissent_list);
		mAdapterHelper = new DissentDetailAdapterHelper(this, mList);
		
		mAdapter = (DissentAdapter) mAdapterHelper.getAdapter();
		mContainerEmpty = (CEmptyNoteView) findViewById(R.id.list_empty_container);
		resume();
	}

	private void resume() {
		requestList(0,0);

		showWaitDialog(RequestInfo.S_DISSENT_ORDERS);
    }
	
	private void requestList(long toTime, long start_id) {
		DissentOrders.Params params = new DissentOrders.Params();
		params.p_type = GlobalConstant.PRODUCT_ALL;
		if (toTime == 0)
			params.to_time = System.currentTimeMillis();// 点买时间<=该时间 Int 可选
		else
			params.to_time = toTime; 
//		to_time = params.to_time;
		params.limit = COUNT;
		params.start_id = start_id;//
		DissentOrders.doRequest(params, this);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		if (RequestInfo.S_DISSENT_ORDERS == info) {
			DissentOrders orders = (DissentOrders) res;
			if (orders.records != null) {
				mAdapterHelper.assignList(orders.records);
			    if (mAdapter.getList() == null || mAdapter.getList().size() == 0) {
			    	mContainerDissent.setVisibility(View.GONE);
					mContainerEmpty.setVisibility(View.VISIBLE);
				} else {
					mContainerDissent.setVisibility(View.VISIBLE);
					mContainerEmpty.setVisibility(View.GONE);
				}
			}
		}
	}
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (RequestInfo.S_DISSENT_ORDERS == info) {
			mAdapterHelper.finish();
		}
	    return super.netFinishError(info, what, msg, tag);
	}
	
	private class DissentDetailAdapterHelper extends CListViewAdapterHelper<DissentDetail> {

		public DissentDetailAdapterHelper(ICallBack callBack, CListView listView) {
			super(callBack, listView);
		}

		@Override
		protected ArrayListAdapter<DissentDetail> getListViewAdapter() {
			return new DissentAdapter(DissentRecordActivity.this);
		}

		@Override
		protected void requestList() {
			DissentRecordActivity.this.requestList(0, 0);
		}

		@Override
		protected void requestMore(String startId) {
			DissentRecordActivity.this.requestList(0, Long.parseLong(startId));
		}

		@Override
		protected String getLastId() {
			if (mContents.size() > 0)
				return Long.toString(mContents.get(mContents.size() - 1).id);
			return "-1";
		}
		
	}
	
}
