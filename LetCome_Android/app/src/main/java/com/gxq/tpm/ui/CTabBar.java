package com.gxq.tpm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.letcome.R;

import java.util.ArrayList;
import java.util.List;

public class CTabBar extends LinearLayout implements View.OnClickListener {
	
	private LinearLayout mViewContainer;
	private List<CTabBarItem> mViewList;
	
	private int mSelectedIndex = 0;
	private OnTabBarClickListener mListener;
	
	public CTabBar(Context context) {
		this(context, null);
	}

	public CTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.tab_bar, this);
		
		mViewContainer = (LinearLayout) findViewById(R.id.container_tabbar);
		mViewList = new ArrayList<CTabBarItem>();
		
		setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		if (mListener != null) {
			if (mViewList.get(mSelectedIndex) == v) {
				mListener.onDoubleTabClick(v.getId());
			} else {
				mListener.onTabClick(v.getId());
			}
		}
	}

	public CTabBarItem newTabBarItem(int id,int drawableResId) {
		CTabIconBarItem tabBarItem = new CTabIconBarItem(getContext());
		tabBarItem.setId(id);
		tabBarItem.setImageResource(drawableResId);
		tabBarItem.setOnClickListener(this);

		LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		mViewContainer.addView(tabBarItem, lp);

		mViewList.add(tabBarItem);

		setVisibility(View.VISIBLE);
		reloadHighlight();

		return tabBarItem;
	}
	
	public CTabBarItem newTabBarItem(int id, int textResId, int drawableResId) {
		CTabFullBarItem tabBarItem = new CTabFullBarItem(getContext());
		tabBarItem.setId(id);
		tabBarItem.setImageResource(drawableResId);
		tabBarItem.setText(textResId);
		tabBarItem.setOnClickListener(this);
		
		LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		mViewContainer.addView(tabBarItem, lp);
		
		mViewList.add(tabBarItem);
		
		setVisibility(View.VISIBLE);
		reloadHighlight();
		
		return tabBarItem;
	}

	private void reloadHighlight() {
		for(View view : mViewList) {
			view.setSelected(false);
		}
		mViewList.get(mSelectedIndex).setSelected(true);
	}
	
	public void selectTabItem(int id) {
		int oldSelectedIndex = mSelectedIndex;
		for (int i = 0; i < mViewList.size(); i++) {
			if (mViewList.get(i).getId() == id) {
				mSelectedIndex = i;
				break;
			}
		}
				
		if (oldSelectedIndex == mSelectedIndex) {
			if (mListener != null)
				mListener.onDoubleTabClick(id);
		} else {
			reloadHighlight();
			
			if (mListener != null) {
				mListener.onTabClick(id);
			}
		}
	}
	
	public void setTabItem(int id, boolean unread) {
		for (int i = 0; i < mViewList.size(); i++) {
			if (mViewList.get(i).getId() == id) {
				mViewList.get(i).setRemind(unread);
				break;
			}
		}
	}

	public void setOnTabBarClickListener(OnTabBarClickListener listener) {
		mListener = listener;
	}
	
    public static interface OnTabBarClickListener {
    	public void onTabClick(int id);
    	public void onDoubleTabClick(int id);
    }
    
}
