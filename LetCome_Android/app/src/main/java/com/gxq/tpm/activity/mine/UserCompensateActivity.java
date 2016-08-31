package com.gxq.tpm.activity.mine;

import android.os.Bundle;
import android.view.View;

import com.gxq.tpm.GlobalConstant;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.ArrayListAdapter;
import com.gxq.tpm.adapter.CompensateAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.mine.UserCompensateOrders;
import com.gxq.tpm.mode.mine.UserCompensateOrders.CompensateDetail;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.CListViewAdapterHelper;
import com.gxq.tpm.ui.CListView;

public class UserCompensateActivity extends SuperActivity {
	public static final int COUNT = 15;
	
	private CListView mList;
	private CompensateAdapter mAdapter;
	private View mContainerCompensate, mContainerEmpty;
	
	private UserCompensateAdapterHelper mAdapterHelper;
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle(R.string.user_compensate);
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_compensate);
		
		mContainerCompensate = findViewById(R.id.container_compensate);
		mList = (CListView) findViewById(R.id.settlement_correction_list);

		mAdapterHelper = new UserCompensateAdapterHelper(this, mList);
		mAdapter = (CompensateAdapter) mAdapterHelper.getAdapter();
		
		mContainerEmpty = findViewById(R.id.list_empty_container);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		requestList(0, 0);
		showWaitDialog(RequestInfo.S_USER_COMPENSATE);
	}
	
	private void requestList(long toTime, long start_id) {
		UserCompensateOrders.Params params = new UserCompensateOrders.Params();
		params.p_type = GlobalConstant.PRODUCT_ALL;
		if (toTime == 0)
			params.to_time = System.currentTimeMillis();// 点买时间<=该时间 Int 可选
		else
			params.to_time = toTime; 
//		mToTime = params.to_time;
		params.limit = COUNT;
		params.start_id = start_id;//
		UserCompensateOrders.doRequest(params, this);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		super.netFinishOk(info, result, tag);
		if (RequestInfo.S_USER_COMPENSATE == info) {
			UserCompensateOrders orders = (UserCompensateOrders) result;
			if (orders.records != null) {
				mAdapterHelper.assignList(orders.records);
				
				if (mAdapter.getList() == null || mAdapter.getList().size() == 0) {
					mContainerCompensate.setVisibility(View.GONE);
					mContainerEmpty.setVisibility(View.VISIBLE);
				} else {
					mContainerCompensate.setVisibility(View.VISIBLE);
					mContainerEmpty.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (RequestInfo.S_USER_COMPENSATE == info) {
			mAdapterHelper.finish();
		}
	    return super.netFinishError(info, what, msg, tag);
	}
	
	private class UserCompensateAdapterHelper extends CListViewAdapterHelper<CompensateDetail> {

		public UserCompensateAdapterHelper(ICallBack callBack, CListView listView) {
			super(callBack, listView);
		}

		@Override
		protected ArrayListAdapter<CompensateDetail> getListViewAdapter() {
			return new CompensateAdapter(UserCompensateActivity.this);
		}

		@Override
		protected void requestList() {
			UserCompensateActivity.this.requestList(0, 0);
		}

		@Override
		protected void requestMore(String startId) {
			UserCompensateActivity.this.requestList(0, Long.parseLong(startId));
		}

		@Override
		protected String getLastId() {
			if (mContents.size() > 0)
				return Long.toString(mContents.get(mContents.size() - 1).id);
			return "0";
		}
		
	}
	
}
