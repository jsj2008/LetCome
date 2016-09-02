package com.gxq.tpm.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.gxq.tpm.fragment.ViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

public class CViewPagerAdapter extends PagerAdapter {
	private List<ViewPagerFragment> mFragments;
	
	public CViewPagerAdapter(ViewPagerFragment[] fragments) {
		this.mFragments = new ArrayList<ViewPagerFragment>();
		for (ViewPagerFragment fragment : fragments) {
			mFragments.add(fragment);
		}
	}
	
	public CViewPagerAdapter(List<ViewPagerFragment> fragments) {
		this.mFragments = fragments;
	}
	
	public void onResume() {
		for (ViewPagerFragment fragment : mFragments) {
			fragment.resume();
		}
	}
	
	public void onShow() {
		for (ViewPagerFragment fragment : mFragments) {
			fragment.show();
		}
	}
	
	public void onPause() {
		for (ViewPagerFragment fragment : mFragments) {
			fragment.pause();
		}
	}
	
	public void onHide() {
		for (ViewPagerFragment fragment : mFragments) {
			fragment.hide();
		}
	}
	
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mFragments.get(position).getView());
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mFragments.get(position).getView());
		return mFragments.get(position).getView();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return super.getPageTitle(position);
	}

}
