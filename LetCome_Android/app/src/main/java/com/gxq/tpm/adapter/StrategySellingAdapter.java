package com.gxq.tpm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.StockHqActivity;
import com.gxq.tpm.activity.strategy.StrategySellingActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.strategy.ProductProfit.Profit;
import com.gxq.tpm.mode.strategy.StrategySellingOrders.StrategySelling;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;

public class StrategySellingAdapter extends StrategyProfitAdapter<StrategySelling>
		implements View.OnClickListener {

	public StrategySellingAdapter(Context context) {
		super(context);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item_strategy_selling, parent, false);
			
			holder = new ViewHolder();
			holder.viewHead = convertView.findViewById(R.id.head);
			holder.tvPno = (TextView) convertView.findViewById(R.id.tv_pno);
			holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_start_time);
			holder.tvInvestor = (TextView) convertView.findViewById(R.id.tv_investor_name);
			holder.tvInvestorFund = (TextView) convertView.findViewById(R.id.tv_investor_fund);
			holder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
			holder.tvStockName.setOnClickListener(this);
			
			holder.tvToSellAmount = (TextView) convertView.findViewById(R.id.tv_to_sell_amount);
			holder.tvSelledAmount = (TextView) convertView.findViewById(R.id.tv_selled_amount);
			holder.tvTotalAmount = (TextView) convertView.findViewById(R.id.tv_total_amount);
			
			holder.tvBuyPrice = (TextView) convertView.findViewById(R.id.tv_buy_price);
			holder.tvSellPrice = (TextView) convertView.findViewById(R.id.tv_sell_price);
			holder.tvCurPrice = (TextView) convertView.findViewById(R.id.tv_cur_price);
			
			holder.tvProfit = (TextView) convertView.findViewById(R.id.tv_profit);
			holder.tvProfitRatio = (TextView) convertView.findViewById(R.id.tv_profit_ratio);
			
			holder.btnNext = (Button) convertView.findViewById(R.id.btn_next_step);
			holder.btnNext.setOnClickListener(this);
			holder.pbStrategy = (ProgressBar) convertView.findViewById(R.id.pb_strategy);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (position == 0) {
			holder.viewHead.setVisibility(View.GONE);
		} else {
			holder.viewHead.setVisibility(View.VISIBLE);
		}
		
		holder.tvStockName.setTag(position);
		holder.btnNext.setTag(position);
		
		StrategySelling selling = getList().get(position);
		if (selling != null) {
			holder.tvPno.setText(mContext.getString(R.string.strategy_pno, Util.checkS(selling.pno)));
			holder.tvStartTime.setText(TimeFormat.milliToMonthMinute(selling.create_time));
			holder.tvInvestor.setText(selling.dealer_replace);
			holder.tvInvestorFund.setText(NumberFormat.tenThousand(
					NumberFormat.decimalFormat0(selling.fund/10000)));
			holder.tvStockName.setText(selling.stock_name);
			
			holder.tvToSellAmount.setText(NumberFormat.stockAmountToString(selling.left_amount));
			holder.tvSelledAmount.setText(NumberFormat.stockAmountToString(selling.sell_amount));
			holder.tvTotalAmount.setText(NumberFormat.stockAmountToString(selling.amount));
			
			holder.btnNext.setText(mContext.getString(R.string.strategy_close_strategy, selling.times, selling.sell_max_times));
			holder.pbStrategy.setMax(selling.sell_max_times);
			holder.pbStrategy.setProgress(selling.times);
			
			boolean enable = selling.state == 1; // 0：禁止平仓 1：允许平仓(APP再根据是否为交易时间判断样式) 2：系统平仓
			
			Profit profit = findProfit(selling.id);
			if (profit != null) {
				holder.tvBuyPrice.setText(NumberFormat.moneyUnit(profit.buy_deal_price_avg));
				if (selling.times > 0) {
					holder.tvSellPrice.setText(NumberFormat.moneyUnit(profit.sell_deal_price_avg));
				} else {
					holder.tvSellPrice.setText(R.string.default_value);
				}
				holder.tvCurPrice.setText(NumberFormat.moneyUnit(profit.cur_price));
				
				int color = Util.getColorByPlusMinus(profit.profit);
				holder.tvProfit.setText(NumberFormat.bigDecimalFormat(profit.profit));
				holder.tvProfit.setTextColor(color);
				holder.tvProfitRatio.setText(NumberFormat.percentWithSymbol(profit.profit_rate));
				holder.tvProfitRatio.setTextColor(color);
				
				enable = enable && BaseRes.RESULT_OK.equals(profit.is_trade_time);
			} else {
				holder.tvBuyPrice.setText(R.string.default_value);
				holder.tvCurPrice.setText(R.string.default_value);
				holder.tvSellPrice.setText(R.string.default_value);
				
				int color = Util.transformColor(R.color.text_color_sub);
				holder.tvProfit.setText(R.string.default_value);
				holder.tvProfit.setTextColor(color);
				holder.tvProfitRatio.setText(R.string.default_value);
				holder.tvProfitRatio.setTextColor(color);
			}
			
			holder.btnNext.setEnabled(enable && selling.times < selling.sell_max_times);
		}
		
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		StrategySelling selling = getList().get(position);
		if (selling == null) return;
		
		Intent pendingIntent = new Intent(mContext, StrategySellingActivity.class);
		pendingIntent.putExtra(ProductIntent.EXTRA_VALUE, selling);
		
		switch (v.getId()) {
		case R.id.tv_stock_name:
			Intent intent = new Intent(mContext, StockHqActivity.class);
			intent.putExtra(ProductIntent.EXTRA_STOCK, selling.stock_name);
			intent.putExtra(ProductIntent.EXTRA_STOCK_CODE, selling.code);
			
			intent.putExtra(ProductIntent.EXTRA_TEXT, 
					mContext.getString(R.string.strategy_close_strategy, selling.times, selling.sell_max_times));
			intent.putExtra(ProductIntent.EXTRA_ENABLE, selling.times < selling.sell_max_times);
			
			intent.putExtra(ProductIntent.EXTRA_INTENT, pendingIntent);
			intent.putExtra(ProductIntent.EXTRA_VALUE, selling);
			
			mContext.startActivity(intent);
			
			break;
		case R.id.btn_next_step:
			mContext.startActivity(pendingIntent);
			break;
		}
	}
	
	private class ViewHolder {
		public View viewHead;
		
		public TextView tvPno;
		public TextView tvStartTime;
		public TextView tvInvestor;
		public TextView tvInvestorFund;
		public TextView tvStockName;
		
		public TextView tvToSellAmount;
		public TextView tvSelledAmount;
		public TextView tvTotalAmount;
		
		public TextView tvBuyPrice;
		public TextView tvSellPrice;
		public TextView tvCurPrice;
		
		public TextView tvProfit;
		public TextView tvProfitRatio;
		
		public Button btnNext;
		public ProgressBar pbStrategy;
	}
	
}
