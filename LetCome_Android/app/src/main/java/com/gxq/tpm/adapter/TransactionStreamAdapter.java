package com.gxq.tpm.adapter;

import java.util.ArrayList;

import com.letcome.R;
import com.gxq.tpm.mode.account.TransactionStream.TransactionStreamItem;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TransactionStreamAdapter extends ArrayListAdapter<TransactionStreamItem>{
	
	public TransactionStreamAdapter(Context context, ArrayList<TransactionStreamItem> list) {
		super(context, list);
	}
	public TransactionStreamAdapter(Context context) {
		super(context);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_transaction_stream, parent, false);
			holder = new ViewHolder();
			holder.mViewLineTop=convertView.findViewById(R.id.line_top);
			holder.mViewTop=convertView.findViewById(R.id.view_top);
			holder.mViewLineBottom=convertView.findViewById(R.id.view_bottom);
			holder.mTvTsTime=(TextView) convertView.findViewById(R.id.tv_time);
			holder.mTvTsDealState=(TextView) convertView.findViewById(R.id.tv_deal_state);
			holder.mTvTsDealType=(TextView) convertView.findViewById(R.id.deal_type);
			holder.mTvTsPrice=(TextView) convertView.findViewById(R.id.tv_price);
			holder.mTvTsPriceShow=(TextView) convertView.findViewById(R.id.tv_price_show);
			holder.mTvTsAmount=(TextView) convertView.findViewById(R.id.tv_amount);
			holder.mTvTsAmountShow=(TextView) convertView.findViewById(R.id.tv_amount_show);
			holder.mTvTsFund=(TextView) convertView.findViewById(R.id.tv_fund);
			holder.mTvTsFundShow=(TextView) convertView.findViewById(R.id.tv_fund_show);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();	
		}
		
		if(position==0){
			holder.mViewLineTop.setVisibility(View.GONE);
			holder.mViewTop.setVisibility(View.GONE);
		}else{
			holder.mViewLineTop.setVisibility(View.VISIBLE);
			holder.mViewTop.setVisibility(View.VISIBLE);
		}
		
		if(position==getCount()-1){
			holder.mViewLineBottom.setVisibility(View.VISIBLE);
		}else{
			holder.mViewLineBottom.setVisibility(View.GONE);
		}
		
		assignItemView(holder,getList().get(position));
		return convertView;
	}
	private void assignItemView(ViewHolder holder, TransactionStreamItem transactionStreamItem) {
		String priceshow="";
		String amountshow="";
		String fundshow="";
		//买
		if(transactionStreamItem.sell_buy==1){
			priceshow=Util.transformString(R.string.transaction_stream_item_buy_price);
			amountshow=Util.transformString(R.string.transaction_stream_item_buy_amount);
			fundshow=Util.transformString(R.string.transaction_stream_item_deal_fund);
		//卖
		}else if(transactionStreamItem.sell_buy==2){
			//红利
			if(transactionStreamItem.type==7){
				priceshow=Util.transformString(R.string.transaction_stream_item_stock_bonus);
				amountshow=Util.transformString(R.string.transaction_stream_item_stock_bonus_amount);
				fundshow=Util.transformString(R.string.transaction_stream_item_stock_bonus_fund);
			}else{
				//其他交易类型
				priceshow=Util.transformString(R.string.transaction_stream_item_sell_price);
				amountshow=Util.transformString(R.string.transaction_stream_item_sell_amount);
				fundshow=Util.transformString(R.string.transaction_stream_item_deal_fund);
			}
		}
		holder.mTvTsPriceShow.setText(priceshow);
		holder.mTvTsAmountShow.setText(amountshow);
		holder.mTvTsFundShow.setText(fundshow);
		
		//时间显示
		String time="";
		if(transactionStreamItem.type==7||transactionStreamItem.type==8){
			time=NumberFormat.timeMilliToMonthDefault(transactionStreamItem.deal_time);
		}else{
			time=NumberFormat.timeMilliToMonthMinuteDefault(transactionStreamItem.deal_time);
		}
		holder.mTvTsTime.setText(time);
		int defaultcolor=0;
		//失败
		if(transactionStreamItem.state==0){
			holder.mTvTsDealState.setVisibility(View.VISIBLE);
			holder.mTvTsDealState.setTextColor(Util.transformColor(R.color.color_ff7814));
			holder.mTvTsDealState.setText(transactionStreamItem.state_name);
			String defaultstr=Util.transformString(R.string.default_value);
			defaultcolor=Util.transformColor(R.color.color_cccccc);
			holder.mTvTsPrice.setText(defaultstr);
			holder.mTvTsPrice.setTextColor(defaultcolor);
			holder.mTvTsAmount.setText(defaultstr);
			holder.mTvTsAmount.setTextColor(defaultcolor);
			holder.mTvTsFund.setText(defaultstr);
			holder.mTvTsFund.setTextColor(defaultcolor);
		}else{
			holder.mTvTsDealState.setVisibility(View.GONE);
			defaultcolor=Util.transformColor(R.color.color_222222);
			holder.mTvTsPrice.setText(NumberFormat.decimalFormat(transactionStreamItem.deal_price));
			holder.mTvTsPrice.setTextColor(defaultcolor);
			holder.mTvTsAmount.setText(String.valueOf(transactionStreamItem.deal_amount));
			holder.mTvTsAmount.setTextColor(defaultcolor);
			holder.mTvTsFund.setText(NumberFormat.bigDecimalFormat(transactionStreamItem.deal_fund));
			holder.mTvTsFund.setTextColor(defaultcolor);
		}
		holder.mTvTsDealType.setText(transactionStreamItem.type_name);
	}
	private static class ViewHolder {
		public View mViewLineTop;
		public View mViewTop;
		public View mViewLineBottom;
		public TextView mTvTsTime;
		public TextView mTvTsDealState;
		public TextView mTvTsDealType;
		public TextView mTvTsPrice;
		public TextView mTvTsPriceShow;
		public TextView mTvTsAmount;
		public TextView mTvTsAmountShow;
		public TextView mTvTsFund;
		public TextView mTvTsFundShow;
	}
}
