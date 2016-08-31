package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CProductHqHead extends LinearLayout {
	private TextView mTvCurPrice, mTvDiff, mTvDiffRatio;
	private TextView mTvOpen, mTvYClose, mTvHigh, mTvLow;
	
	public CProductHqHead(Context context) {
		this(context, null);
	}
	
	public CProductHqHead(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CProductHqHead(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initUI();
	}

	private void initUI() {
		View view = View.inflate(getContext(), R.layout.product_hq_head, this);
		
		mTvCurPrice = (TextView) view.findViewById(R.id.tv_cur_price);
		mTvDiff = (TextView) view.findViewById(R.id.tv_diff);
		mTvDiffRatio = (TextView) view.findViewById(R.id.tv_diff_ratio);

		mTvOpen = (TextView) view.findViewById(R.id.tv_stock_open);
		mTvYClose = (TextView) view.findViewById(R.id.tv_stock_yclose);
		mTvHigh = (TextView) view.findViewById(R.id.tv_stock_high);
		mTvLow = (TextView) view.findViewById(R.id.tv_stock_low);
	}
	
	public void assignHandicapInfo(GetHandicapInfo handicapInfo) {
		if (handicapInfo != null && handicapInfo.New > 0) {
			float newPrice = handicapInfo.New;
			float yClose = handicapInfo.YClose;
			float diff = newPrice - yClose;
			
			int color = Util.getStockColor(newPrice, yClose, R.color.text_color_main);
			mTvCurPrice.setText(NumberFormat.decimalFormat(newPrice));
			mTvCurPrice.setTextColor(color);
			
			mTvDiff.setText(NumberFormat.decimalFormatWithSymbol(diff));
			mTvDiff.setTextColor(color);
			
			mTvDiffRatio.setText(NumberFormat.percentWithSymbol(diff/yClose));
			mTvDiffRatio.setTextColor(color);
			
			mTvOpen.setText(NumberFormat.decimalFormat(handicapInfo.Open));
			mTvYClose.setText(NumberFormat.decimalFormat(handicapInfo.YClose));
			mTvHigh.setText(NumberFormat.decimalFormat(handicapInfo.High));
			mTvLow.setText(NumberFormat.decimalFormat(handicapInfo.Low));
		} else {
			int color = Util.transformColor(R.color.text_color_main);
			mTvCurPrice.setText(R.string.number_default_value);
			mTvCurPrice.setTextColor(color);
			
			color = Util.transformColor(R.color.text_color_sub);
			mTvDiff.setText(R.string.number_default_value);
			mTvDiff.setTextColor(color);
			
			mTvDiffRatio.setText(R.string.number_default_value);
			mTvDiffRatio.setTextColor(color);
			
			mTvOpen.setText(R.string.number_default_value);
			mTvYClose.setText(R.string.number_default_value);
			mTvHigh.setText(R.string.number_default_value);
			mTvLow.setText(R.string.number_default_value);
		}
	}
	
}
