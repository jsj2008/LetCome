package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.letcome.R;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CStockHandicapView extends LinearLayout {
	private final static int COUNT = 5;
	
	private boolean mSell;
	
	private int mColor = Util.transformColor(R.color.text_color_main);
	private int mGainColor = Util.transformColor(R.color.gain_color);
	private int mLossColor = Util.transformColor(R.color.loss_color);
	
	private List<ViewHolder> mViewHolderList;
	
	public CStockHandicapView(Context context) {
		this(context, null);
	}
	
	public CStockHandicapView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CStockHandicapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mViewHolderList = new ArrayList<ViewHolder>(COUNT);
		
		LayoutInflater mInflater = LayoutInflater.from(getContext());
		for (int i = 0; i < COUNT; i++) {
			View view = mInflater.inflate(R.layout.handicap_info_item, this, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
			viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
			viewHolder.tvVolume = (TextView) view.findViewById(R.id.tv_volume);
			
			mViewHolderList.add(viewHolder);
			addView(view);
		}
		
	}
	
	public void assignHandicap(GetHandicapInfo handicapInfo) {
		if (handicapInfo != null && handicapInfo.New > 0) {
			for (int index = 0; index < COUNT; index++) {
				float price = 0;
				long volume = 0;
				ViewHolder viewHolder = mViewHolderList.get(index);
				if (mSell) {
					if (index == 0) {
						price = handicapInfo.Sell5;
						volume = handicapInfo.SellVol5;
					} else if (index == 1) {
						price = handicapInfo.Sell4;
						volume = handicapInfo.SellVol4;
					} else if (index == 2) {
						price = handicapInfo.Sell3;
						volume = handicapInfo.SellVol3;
					} else if (index == 3) {
						price = handicapInfo.Sell2;
						volume = handicapInfo.SellVol2;
					} else if (index == 4) {
						price = handicapInfo.Sell1;
						volume = handicapInfo.SellVol1;
					}
				} else {
					if (index == 0) {
						price = handicapInfo.Buy1;
						volume = handicapInfo.BuyVol1;
					} else if (index == 1) {
						price = handicapInfo.Buy2;
						volume = handicapInfo.BuyVol2;
					} else if (index == 2) {
						price = handicapInfo.Buy3;
						volume = handicapInfo.BuyVol3;
					} else if (index == 3) {
						price = handicapInfo.Buy4;
						volume = handicapInfo.BuyVol4;
					} else if (index == 4) {
						price = handicapInfo.Buy5;
						volume = handicapInfo.BuyVol5;
					}
				}
				viewHolder.tvPrice.setText(NumberFormat.decimalFormat(price));
				viewHolder.tvPrice.setTextColor(price >= handicapInfo.YClose ? mGainColor : mLossColor);
					
				viewHolder.tvVolume.setText(getVol(volume/100));
			}
		} else {
			for (int index = 0; index < COUNT; index++) {
				ViewHolder viewHolder = mViewHolderList.get(index);
				viewHolder.tvPrice.setText(R.string.number_default_value);
				viewHolder.tvPrice.setTextColor(mColor);
				
				viewHolder.tvVolume.setText(R.string.number_default_value);
			}
		}
	}
	
	private String getVol(long volume) {
		if (volume < 10000) {
			return Long.toString(volume);
		} else {
			return getContext().getResources().getString(R.string.ten_thousand,
					NumberFormat.decimalFormat1(volume/10000.0));
		}
	}
	
	public void setSell(boolean sell) {
		this.mSell = sell;
		
		if (mSell) {
			mViewHolderList.get(0).tvTitle.setText(R.string.sell5);
			mViewHolderList.get(1).tvTitle.setText(R.string.sell4);
			mViewHolderList.get(2).tvTitle.setText(R.string.sell3);
			mViewHolderList.get(3).tvTitle.setText(R.string.sell2);
			mViewHolderList.get(4).tvTitle.setText(R.string.sell1);
		} else {
			mViewHolderList.get(0).tvTitle.setText(R.string.buy1);
			mViewHolderList.get(1).tvTitle.setText(R.string.buy2);
			mViewHolderList.get(2).tvTitle.setText(R.string.buy3);
			mViewHolderList.get(3).tvTitle.setText(R.string.buy4);
			mViewHolderList.get(4).tvTitle.setText(R.string.buy5);
		}
		
		for (int index = 0; index < COUNT; index++) {
			ViewHolder viewHolder = mViewHolderList.get(index);
			viewHolder.tvPrice.setText(R.string.number_default_value);
			viewHolder.tvPrice.setTextColor(mColor);
			
			viewHolder.tvVolume.setText(R.string.number_default_value);
		}
	}

	private class ViewHolder {
		public TextView tvTitle;
		public TextView tvPrice;
		public TextView tvVolume;
	}
	
}
