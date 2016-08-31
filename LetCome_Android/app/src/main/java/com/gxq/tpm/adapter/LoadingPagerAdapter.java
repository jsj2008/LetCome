package com.gxq.tpm.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class LoadingPagerAdapter extends PagerAdapter{
	private View mViews[] = new View[0];

	public LoadingPagerAdapter(View[] views){
		this.mViews = views;
	}
	@Override
	public int getCount() {
		return mViews.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		ViewPager pager = (ViewPager) container;
		pager.addView(mViews[position]);
		return mViews[position];
	}

	@Override
	public void destroyItem(View container, int position, Object o) {
		((ViewPager)container).removeView((View)o);
	}
	
}
