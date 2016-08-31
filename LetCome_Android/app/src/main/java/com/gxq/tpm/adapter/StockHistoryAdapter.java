package com.gxq.tpm.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.letcome.R;
import com.gxq.tpm.mode.Stock;
import com.gxq.tpm.mode.hq.GetHQInfo.HQInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;

public class StockHistoryAdapter extends ArrayListAdapter<Stock> {
	private List<HQInfo> mHqInfoList;
	private int mColor;
	
	public StockHistoryAdapter(Context context) {
		super(context);
		mColor = Util.transformColor(R.color.text_color_sub);
	}

	@Override
	public int getCount() {
		if (super.getCount() == 0)
			return 0;
		return super.getCount() + 1;
	}
	
	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			
			convertView = inflater.inflate(R.layout.stock_history_item, parent, false);
			
			viewHolder.contaienr = convertView.findViewById(R.id.container_search_history);
			viewHolder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
			viewHolder.tvStockCode = (TextView) convertView.findViewById(R.id.tv_stock_code);
			viewHolder.tvCurPrice = (TextView) convertView.findViewById(R.id.tv_cur_price);
			viewHolder.tvPriceDiff = (TextView) convertView.findViewById(R.id.tv_price_diff);
			viewHolder.tvPriceRatio = (TextView) convertView.findViewById(R.id.tv_price_ratio);
			viewHolder.tvText = (TextView) convertView.findViewById(R.id.tv_history_text);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (position == super.getCount()) {
			viewHolder.contaienr.setVisibility(View.GONE);
			viewHolder.tvText.setVisibility(View.VISIBLE);
			viewHolder.tvText.setText(R.string.clear_all_search_records);
		} else {
			viewHolder.contaienr.setVisibility(View.VISIBLE);
			viewHolder.tvText.setVisibility(View.GONE);
			
			Stock stock = (Stock) getList().get(position);
			if (stock != null) {
				viewHolder.tvStockName.setText(stock.getStockName());
				viewHolder.tvStockCode.setText(stock.getStockCode());
				
				HQInfo info = getHqInfo(stock);
				if (info != null && info.New > 0) {
					float diff = info.New - info.YClose;
					int color = Util.getColorByPlusMinus(diff);
					
					viewHolder.tvCurPrice.setText(NumberFormat.decimalFormat(info.New));
					viewHolder.tvCurPrice.setTextColor(color);
					
					viewHolder.tvPriceDiff.setText(NumberFormat.decimalFormatWithSymbol(diff));
					viewHolder.tvPriceDiff.setTextColor(color);
					
					viewHolder.tvPriceRatio.setText(NumberFormat.percentWithSymbol(diff/info.YClose));
					viewHolder.tvPriceRatio.setBackgroundResource(diff >= 0 ?
							R.drawable.btn_rect_gain : R.drawable.btn_rect_loss);
				} else {
					
					viewHolder.tvCurPrice.setText(R.string.number_default_value);
					viewHolder.tvCurPrice.setTextColor(mColor);
					
					viewHolder.tvPriceDiff.setText(R.string.number_default_value);
					viewHolder.tvPriceDiff.setTextColor(mColor);
					
					viewHolder.tvPriceRatio.setText(R.string.number_default_value);
					viewHolder.tvPriceRatio.setBackgroundResource(R.drawable.btn_rect_diable);
				}
			}
		}
		return convertView;
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
		View contaienr;
		TextView tvStockName;
		TextView tvStockCode;
		TextView tvCurPrice;
		TextView tvPriceDiff;
		TextView tvPriceRatio;
		TextView tvText;
	}
	
}
