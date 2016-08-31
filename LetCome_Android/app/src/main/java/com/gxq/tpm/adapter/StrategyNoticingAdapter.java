package com.gxq.tpm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.StockHqActivity;
import com.gxq.tpm.activity.strategy.StrategyNoticingActivity;
import com.gxq.tpm.mode.strategy.ProductProfit.Profit;
import com.gxq.tpm.mode.strategy.StrategyNoticingOrders.StrategyNoticing;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CLossRatioView;
import com.gxq.tpm.ui.CPointProgressBar;

public class StrategyNoticingAdapter extends StrategyProfitAdapter<StrategyNoticing>
		implements View.OnClickListener {

	public StrategyNoticingAdapter(Context context) {
		super(context);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item_strategy_noticing, parent, false);
			holder = new ViewHolder();
			holder.viewHead = convertView.findViewById(R.id.head);
			holder.tvPno = (TextView) convertView.findViewById(R.id.tv_pno);
			holder.tvStartTime = (TextView) convertView.findViewById(R.id.tv_start_time);
			holder.tvInvestor = (TextView) convertView.findViewById(R.id.tv_investor_name);
			holder.tvInvestorFund = (TextView) convertView.findViewById(R.id.tv_investor_fund);
			holder.tvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
			holder.tvStockName.setOnClickListener(this);
			
			holder.tvTotalAmount = (TextView) convertView.findViewById(R.id.tv_total_amount);
			holder.tvProfit = (TextView) convertView.findViewById(R.id.tv_profit);
			holder.tvProfitRatio = (TextView) convertView.findViewById(R.id.tv_profit_ratio);
			
			holder.tvBuyPrice = (TextView) convertView.findViewById(R.id.tv_buy_price);
			holder.tvCurPrice = (TextView) convertView.findViewById(R.id.tv_cur_price);
			holder.tvRiseAndFail = (TextView) convertView.findViewById(R.id.tv_rise_and_fall);
			
			holder.tvTradeDay = (TextView) convertView.findViewById(R.id.tv_trade_day);
			holder.pbTradeDay = (CPointProgressBar) convertView.findViewById(R.id.pb_trade_day);
			
			holder.lossRatio = (CLossRatioView) convertView.findViewById(R.id.view_loss_ratio);
			
			holder.btnNext = (Button) convertView.findViewById(R.id.btn_next_step);
			holder.btnNext.setText(R.string.strategy_notice_to_sell);
			holder.btnNext.setOnClickListener(this);
			
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
		
		StrategyNoticing noticing = getList().get(position);
		if (noticing != null) {
			holder.tvPno.setText(mContext.getString(R.string.strategy_pno, Util.checkS(noticing.pno)));
			holder.tvStartTime.setText(TimeFormat.milliToMonthMinute(noticing.create_time));
			holder.tvInvestor.setText(noticing.dealer_replace);
			holder.tvInvestorFund.setText(NumberFormat.tenThousand(
					NumberFormat.decimalFormat0(noticing.fund/10000)));
			holder.tvStockName.setText(noticing.stock_name);
			
			holder.tvTotalAmount.setText(NumberFormat.stockAmountToString(noticing.amount));
			
			String tradeDay = Integer.toString(noticing.trade_day);
			String value = mContext.getString(R.string.strategy_trade_day, tradeDay);
			holder.tvTradeDay.setText(Util.strChangeColor(value, 3, 5 + tradeDay.length(), 
					Util.transformColor(R.color.text_color_tips)));
			
			holder.pbTradeDay.setMax(noticing.max_trade_day);
			holder.pbTradeDay.setProgress(noticing.trade_day);
			
			holder.lossRatio.setProgress((int)(noticing.stop_loss_point * 100), 
					(int)(noticing.mid_safe_point * 100),
					(int)(noticing.max_safe_point * 100));
			
			Profit profit = findProfit(noticing.id);
			if (profit != null) {
				int color = Util.getColorByPlusMinus(profit.profit);
				holder.tvProfit.setText(NumberFormat.bigDecimalFormat(profit.profit));
				holder.tvProfit.setTextColor(color);
				holder.tvProfitRatio.setText(NumberFormat.percentWithSymbol(profit.profit_rate));
				holder.tvProfitRatio.setTextColor(color);
				
				holder.tvBuyPrice.setText(NumberFormat.moneyUnit(profit.buy_deal_price_avg));
				holder.tvCurPrice.setText(NumberFormat.moneyUnit(profit.cur_price));
				String raiseAndFall = NumberFormat.moneyUnit(NumberFormat.decimalFormatWithSymbol(profit.price_proift));
				
				color = Util.getColorByPlusMinus(profit.price_proift);
				holder.tvRiseAndFail.setText(Util.strChangeColor(raiseAndFall, 0, raiseAndFall.length() - 1, color));
				
				holder.lossRatio.setPercent(profit.earnest_loss_rate * 100);
			} else {
				holder.tvProfit.setText(R.string.default_value);
				holder.tvProfitRatio.setText(R.string.default_value);
				holder.tvBuyPrice.setText(R.string.default_value);
				holder.tvCurPrice.setText(R.string.default_value);
				holder.tvRiseAndFail.setText(R.string.default_value);
				
				int color = Util.transformColor(R.color.text_color_main);
				holder.tvProfit.setTextColor(color);
				holder.tvProfitRatio.setTextColor(color);
				
				holder.lossRatio.setPercent(-1);
			}
		}
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		StrategyNoticing noticing = getList().get(position);
		if (noticing == null) return;
		
		Intent pendingIntent = new Intent(mContext, StrategyNoticingActivity.class);
		pendingIntent.putExtra(ProductIntent.EXTRA_VALUE, noticing);
		
		switch (v.getId()) {
		case R.id.tv_stock_name:
			Intent intent = new Intent(mContext, StockHqActivity.class);
			intent.putExtra(ProductIntent.EXTRA_STOCK, noticing.stock_name);
			intent.putExtra(ProductIntent.EXTRA_STOCK_CODE, noticing.code);
			
			intent.putExtra(ProductIntent.EXTRA_TEXT, Util.transformString(R.string.strategy_notice_to_sell));
			
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
		
		public TextView tvTotalAmount;
		public TextView tvProfit;
		public TextView tvProfitRatio;
		
		public TextView tvBuyPrice;
		public TextView tvCurPrice;
		public TextView tvRiseAndFail;
		
		public TextView tvTradeDay;
		public CPointProgressBar pbTradeDay;
		
		public CLossRatioView lossRatio;
		
		public Button btnNext;
	}
}
