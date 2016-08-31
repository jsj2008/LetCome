package com.gxq.tpm.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gxq.tpm.activity.BaseActivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ArrayListAdapter<T> extends BaseAdapter {

	protected List<T> mList;
	protected BaseActivity mContext;

	protected OnItemSelectListener mListener;
	
	public ArrayListAdapter(Context context) {
		this.mContext = (BaseActivity) context;
	}

	public ArrayListAdapter(Context context, List<T> list) {
		this.mContext = (BaseActivity) context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		if (getList() != null)
			return getList().size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return getList() == null ? null : getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		return getItemView(position, convertView, parent);
	}

	public abstract View getItemView(int position, View convertView, ViewGroup parent);
	
	synchronized public void setList(List<T> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	synchronized public List<T> getList() {
		return mList;
	}

	public void setList(T[] list) {
		ArrayList<T> arrayList = new ArrayList<T>(list.length);
		for (T t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}
	
	public void setOnItemSelectListener(OnItemSelectListener listener) {
		mListener = listener;
	}

	public static interface OnItemSelectListener {
		void onItemSelected(int index);
	}
	
}
