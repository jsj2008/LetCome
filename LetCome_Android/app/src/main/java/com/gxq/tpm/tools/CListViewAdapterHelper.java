package com.gxq.tpm.tools;

import java.util.ArrayList;
import java.util.List;

import com.gxq.tpm.adapter.ArrayListAdapter;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.ui.CListView;

public abstract class CListViewAdapterHelper<T> implements CListView.OnRefreshListener,
			CListView.OnMoreListener {
	protected ICallBack mCallBack;
	protected CListView mListView;
	protected List<T> mContents;
	
	protected ArrayListAdapter<T> mAdapter;
	
	protected boolean mRefresh = true;
	
	protected boolean mMoreEnable = true;
	protected boolean mRefreshEnable = true;
	
	public CListViewAdapterHelper(ICallBack callBack, CListView listView) {
		this.mCallBack = callBack;
		this.mListView = listView;
		
		mAdapter = getListViewAdapter();
		mListView.setAdapter(mAdapter);
		
		mListView.setRefreshEnable(mRefreshEnable);
		mListView.setonRefreshListener(this);
		mListView.setMoreEnable(mMoreEnable);
		mListView.setMoreListener(this);
	}
	
	public ArrayListAdapter<T> getAdapter() {
		return mAdapter;
	}
	
	protected abstract ArrayListAdapter<T> getListViewAdapter();
	
	protected abstract void requestList();

	@Override
	public void onMore() {
		mRefresh = false;
		requestMore(getLastId());
	}
	
	protected abstract void requestMore(String startId);

	protected abstract String getLastId();
	
	@Override
	public void onRefresh() {
		mRefresh = true;
		requestList();
	}
	
	public void assignList(List<T> list) {
		if (mMoreEnable) {
			if (list == null || list.size() == 0) {
				this.mListView.setMoreEnable(false);
			} else {
				this.mListView.setMoreEnable(true);
			}
		}
		if (mContents == null) {
			mContents = new ArrayList<T>();
			mContents.addAll(list);
			mAdapter.setList(mContents);
			mAdapter.notifyDataSetChanged();
		} else {
			if (mRefresh) {
				mContents.clear();
				mContents.addAll(list);
				mAdapter.notifyDataSetChanged();
				this.mListView.onRefreshComplete();
			} else {
				mContents.addAll(list);
				mAdapter.notifyDataSetChanged();
				this.mListView.onMoreComplete();
				mRefresh = true;
			}
		}
	}
	
	public void finish() {
		if (mRefresh) {
			this.mListView.onRefreshComplete();
		} else {
			this.mListView.onMoreComplete();
			mRefresh = true;
		}
	}
	
	public void refresh() {
		mAdapter.notifyDataSetChanged();
	}
	
	public void setMoreEnable(boolean moreEnable) {
		mListView.setMoreEnable(moreEnable);
		this.mMoreEnable = moreEnable;
	}
	
	public void setRefreshEnable(boolean refreshEnable) {
		mListView.setRefreshEnable(refreshEnable);
		this.mRefreshEnable = refreshEnable;
	}
	
	public void clearList(){
		if (null != mContents){
			mContents.clear();
		}
	}
	
}
