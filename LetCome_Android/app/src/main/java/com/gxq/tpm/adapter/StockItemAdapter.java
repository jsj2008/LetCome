package com.gxq.tpm.adapter;

import java.util.List;

import com.letcome.R;
import com.gxq.tpm.mode.Stock;
import com.gxq.tpm.mode.hq.GetHQInfo.HQInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StockItemAdapter extends ArrayListAdapter<Stock>{
	private List<HQInfo> mHqInfoList;

	public StockItemAdapter(Context context) {
		super(context);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		
		
		ViewHolder holder;
		if (null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.choose_stock_item, parent, false);
			View item = convertView.findViewById(R.id.item_container);
			item.setLayoutParams(new RelativeLayout.LayoutParams(Util.getScreenWidth(mContext)/3 - Util.dpToPixel(R.dimen.margin_xhdpi_12),
					LinearLayout.LayoutParams.WRAP_CONTENT));
			
			holder = new ViewHolder();
			holder.mTvStockName = (TextView)convertView.findViewById(R.id.tv_item_stock_name);
			holder.mTvStockCode = (TextView)convertView.findViewById(R.id.tv_item_stock_code);
			holder.mTvCurPrice = (TextView)convertView.findViewById(R.id.tv_item_cur_price);
			holder.mTvPriceDiff = (TextView)convertView.findViewById(R.id.tv_item_price_diff);
			holder.mTvPriceRatio = (TextView)convertView.findViewById(R.id.tv_item_price_ratio);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (getCount() > 0) {
			assignItemView(holder, getList().get(position));
		} else {
			assignItemView(holder, null);
		}
		return convertView;
	}
	
	private void assignItemView(ViewHolder viewHolder, Stock stock){
		if(null == stock){
			return;
		}
		int mColor = Util.transformColor(R.color.text_color_sub);
		
		viewHolder.mTvStockName.setText(stock.getStockName());
		viewHolder.mTvStockCode.setText(stock.getStockCode());
		
		HQInfo info = getHqInfo(stock);
		if (info != null && info.New > 0) {
			float diff = info.New - info.YClose;
			int color = Util.getColorByPlusMinus(diff);
			
			viewHolder.mTvCurPrice.setText(NumberFormat.decimalFormat(info.New));
			viewHolder.mTvCurPrice.setTextColor(color);
			
			viewHolder.mTvPriceDiff.setText(NumberFormat.decimalFormatWithSymbol(diff));
			
			viewHolder.mTvPriceRatio.setText(NumberFormat.percentWithSymbol(diff/info.YClose));
		} else {
			viewHolder.mTvCurPrice.setText(R.string.number_default_value);
			viewHolder.mTvCurPrice.setTextColor(mColor);
			
			viewHolder.mTvPriceDiff.setText(R.string.number_default_value);
			
			viewHolder.mTvPriceRatio.setText(R.string.number_default_value);
		}
	}
	
	public void assignHQ(List<HQInfo> hqInfos) {
		synchronized (this) {
			this.mHqInfoList = hqInfos;
		}
	}
	
	private HQInfo getHqInfo(Stock stock) {
		if (stock == null)
			return null;
		synchronized (this) {
			if (mHqInfoList != null) {
				for (HQInfo info : mHqInfoList) {
					if (info != null && (info.stockcode != null && 
							info.stockcode.equals(stock.getStockCode()))) {
							return info;
					}
				}
			}
		}
		return null;
	}
	
	private static class ViewHolder {
		public TextView mTvStockName;
		public TextView mTvStockCode;
		public TextView mTvCurPrice;
		public TextView mTvPriceDiff;
		public TextView mTvPriceRatio;
	}

}
