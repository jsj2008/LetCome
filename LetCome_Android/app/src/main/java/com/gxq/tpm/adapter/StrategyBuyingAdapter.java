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
import com.gxq.tpm.activity.strategy.StrategyBuyingActivity;
import com.gxq.tpm.mode.strategy.ProductProfit.Profit;
import com.gxq.tpm.mode.strategy.StrategyBuyingOrders.StrategyBuying;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;

public class StrategyBuyingAdapter extends StrategyProfitAdapter<StrategyBuying> 
		implements View.OnClickListener {

	public StrategyBuyingAdapter(Context context) {
		super(context);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item_strategy_buying, parent, false);
			
			holder = new ViewHolder();
			holder.viewHead = convertView.findViewById(R.id.head);
			holder.tvPno = (TextView) convertView.findViewById(R.id.tv_pno);
			holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_start_time);
			holder.tvInvestor = (TextView) convertView.findViewById(R.id.tv_investor_name);
			holder.tvInvestorFund = (TextView) convertView.findViewById(R.id.tv_investor_fund);
			holder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
			holder.tvStockName.setOnClickListener(this);
			
			holder.tvToBuyFund = (TextView) convertView.findViewById(R.id.tv_to_buy_fund);
			holder.tvBoughtFund = (TextView) convertView.findViewById(R.id.tv_bought_fund);
			holder.tvFundRatio = (TextView) convertView.findViewById(R.id.tv_use_ratio);
			
			holder.tvBoughtAmount = (TextView) convertView.findViewById(R.id.tv_bought_amount);
			holder.tvBuyPrice = (TextView) convertView.findViewById(R.id.tv_buy_price);
			holder.tvCurPrice = (TextView) convertView.findViewById(R.id.tv_cur_price);
			holder.tvRiseAndFall = (TextView) convertView.findViewById(R.id.tv_rise_and_fall);
			
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
		
		StrategyBuying buying = getList().get(position);
		
		if (buying != null) {
			holder.tvPno.setText(mContext.getString(R.string.strategy_pno, Util.checkS(buying.pno)));
			holder.tvStartTime.setText(TimeFormat.milliToMonthMinute(buying.create_time));
			holder.tvInvestor.setText(buying.dealer_replace);
			holder.tvInvestorFund.setText(NumberFormat.tenThousand(
					NumberFormat.decimalFormat0(buying.fund/10000)));
			holder.tvStockName.setText(buying.stock_name);
			
			holder.tvToBuyFund.setText(NumberFormat.bigDecimalFormat(buying.left_fund));
			holder.tvBoughtFund.setText(NumberFormat.bigDecimalFormat(buying.init_fund));
			holder.tvFundRatio.setText(NumberFormat.percent(buying.use_rate));
			
			holder.tvBoughtAmount.setText(NumberFormat.stockAmountToString(buying.amount));
			holder.btnNext.setText(mContext.getString(R.string.strategy_create_strategy, buying.times, buying.buy_max_times));
			holder.btnNext.setEnabled(buying.times < buying.buy_max_times);
			holder.pbStrategy.setMax(buying.buy_max_times);
			holder.pbStrategy.setProgress(buying.times);
			
			Profit profit = findProfit(buying.id);
			if (profit != null) {
				holder.tvCurPrice.setText(NumberFormat.moneyUnit(profit.cur_price));
				
				if (buying.times > 0) {
					holder.tvBuyPrice.setText(NumberFormat.moneyUnit(profit.buy_deal_price_avg));
					
					String raiseAndFall = NumberFormat.moneyUnit(NumberFormat.decimalFormatWithSymbol(profit.price_proift));
					int color = Util.getColorByPlusMinus(profit.price_proift);
					holder.tvRiseAndFall.setText(Util.strChangeColor(raiseAndFall, 0, raiseAndFall.length() - 1, color));
				} else {
					holder.tvBuyPrice.setText(R.string.default_value);
					holder.tvRiseAndFall.setText(R.string.default_value);
				}
			} else {
				holder.tvBuyPrice.setText(R.string.default_value);
				holder.tvCurPrice.setText(R.string.default_value);
				holder.tvRiseAndFall.setText(R.string.default_value);
			}
		}
		
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		StrategyBuying buying = getList().get(position);
		if (buying == null) return;
		
		Intent pendingIntent = new Intent(mContext, StrategyBuyingActivity.class);
		pendingIntent.putExtra(ProductIntent.EXTRA_VALUE, buying);
		
		switch (v.getId()) {
		case R.id.tv_stock_name:
			Intent intent = new Intent(mContext, StockHqActivity.class);
			intent.putExtra(ProductIntent.EXTRA_STOCK, buying.stock_name);
			intent.putExtra(ProductIntent.EXTRA_STOCK_CODE, buying.code);
			
			intent.putExtra(ProductIntent.EXTRA_TEXT, 
					mContext.getString(R.string.strategy_create_strategy, buying.times, buying.buy_max_times));
			intent.putExtra(ProductIntent.EXTRA_ENABLE, buying.times < buying.buy_max_times);
			
			intent.putExtra(ProductIntent.EXTRA_INTENT, pendingIntent);
			
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
		
		public TextView tvToBuyFund;
		public TextView tvBoughtFund;
		public TextView tvFundRatio;
		
		public TextView tvBoughtAmount;
		public TextView tvBuyPrice;
		public TextView tvCurPrice;
		public TextView tvRiseAndFall;
		
		public Button btnNext;
		
		public ProgressBar pbStrategy;
	}
 
}
