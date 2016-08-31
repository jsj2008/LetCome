package com.gxq.tpm.fragment;

import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.ArrayListAdapter;
import com.gxq.tpm.adapter.ProductSettlementAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.GetTime;
import com.gxq.tpm.mode.account.DoSettle;
import com.gxq.tpm.mode.account.SettlementOrders;
import com.gxq.tpm.mode.account.SettlementOrders.Settlement;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.CListViewAdapterHelper;
import com.gxq.tpm.ui.CEmptyNoteView;
import com.gxq.tpm.ui.CListView;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SettlementFragment extends FragmentBase implements 
    OnClickListener,CEmptyNoteView.OnRefreshListener{

	public static final int COUNT = 3;
	
	private CEmptyNoteView mEmptyView;

	private CListView mListView;
	private ProductSettlementAdapter mAdapter;
	private SettlementAdapterHelper mAdapterHelper;
	private int mp_id;
	public SettlementFragment() {
		super();
	}

	public SettlementFragment(int markId) {
		super(markId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settlement, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
	}

	private void initView(View view) {
		mListView = (CListView) view.findViewById(R.id.list);
		mAdapterHelper = new SettlementAdapterHelper(SettlementFragment.this, mListView);
		mAdapter=(ProductSettlementAdapter) mAdapterHelper.getAdapter();
		mAdapter.setFragment(this);
		
		mEmptyView = (CEmptyNoteView) view.findViewById(R.id.rl_none);
		mEmptyView.setRefreshEnable(true);
		mEmptyView.setOnRefreshListener(this);
		
		mEmptyView.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if (isHidden()){
			return;
		}
		
		showWaitDialog(RequestInfo.PRODUCT_SETTLEMENT_ORDERS);
		requestSettlementList(0,0);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			requestSettlementList(0,0);
		}
	}

	
	
	@Override
	public void onDestroy() {
		mAdapter.stopCountDown();
		super.onDestroy();
	}

	private void requestSettlementList(int start_id,long close_time ) {
		SettlementOrders.Params params = new SettlementOrders.Params();
		params.limit = COUNT;
		params.start_id = start_id;
		params.close_time=close_time;
		SettlementOrders.doRequest(params, this);
	}
	
	public void instructCommit(int p_id){
		mp_id=p_id;
		DoSettle.Params params = new DoSettle.Params();
		params.p_id = p_id;
		DoSettle.doRequest(params, this);
		showWaitDialog(RequestInfo.PRODUCT_DO_SETTLE);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (RequestInfo.PRODUCT_SETTLEMENT_ORDERS == info) {
			SettlementOrders orders = (SettlementOrders) res;
			mAdapterHelper.assignList(orders.records);
			checkList();
		}else if(RequestInfo.GET_TIME == info){
			GetTime getTime = (GetTime) res;
			mAdapter.startCountDown(getTime.time);
		}else if(RequestInfo.PRODUCT_DO_SETTLE == info){
			DoSettle doSettle = (DoSettle) res;
			if(null != doSettle && doSettle.result.equals(BaseRes.RESULT_OK)){
				mAdapter.setDoSettle(mp_id);
				mContext.showToastMsg(R.string.settlement_result_pay_success);
			}else{
				mContext.showToastMsg(R.string.settlement_result_pay_failure);
			}
		}
	}
	private void checkList() {
		mEmptyView.onRefreshComplete();
		if (mAdapter.getCount() == 0) {
			mListView.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
			mAdapter.stopCountDown();
		} else {
			mListView.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);
			GetTime.doRequest(SettlementFragment.this);
		}
	}
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		mAdapterHelper.finish();
		checkList();
		return super.netFinishError(info, what, msg, tag);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_next_step:
			((SuperActivity) getActivity()).changeFragment(R.id.tab_cooperation, null);
			break;
		}
	}
	@Override
	public void onRefresh() {
		requestSettlementList(0,0);
	}

	private class SettlementAdapterHelper extends CListViewAdapterHelper<Settlement>{

		public SettlementAdapterHelper(ICallBack callBack, CListView listView) {
			super(callBack, listView);
			
		}

		@Override
		protected ArrayListAdapter<Settlement> getListViewAdapter() {
			return new ProductSettlementAdapter(mContext);
		}

		@Override
		protected void requestList() {
			requestSettlementList(0,0);
		}

		@Override
		protected void requestMore(String startId) {
			requestSettlementList(Integer.parseInt(startId),getCloseTime());
		}

		@Override
		protected String getLastId() {
			if (mContents.size() > 0) {
				return Integer.toString(mContents.get(mContents.size() - 1).id);
			}
			return "0";
		}

		private long getCloseTime() {
			if (mContents.size() > 0) {
				return mContents.get(mContents.size() - 1).close_time;
			}
			return 0;
		}
		
	}
}
