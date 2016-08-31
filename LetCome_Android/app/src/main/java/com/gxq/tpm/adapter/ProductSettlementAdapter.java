package com.gxq.tpm.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.account.SettlementDetailActivity;
import com.gxq.tpm.activity.mine.DissentCommitActivity;
import com.gxq.tpm.fragment.SettlementFragment;
import com.gxq.tpm.mode.account.SettlementOrders.Settlement;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.TimeTimer;
import com.gxq.tpm.tools.TimeTimer.OnTimeTimerListener;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.ProductDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductSettlementAdapter extends ArrayListAdapter<Settlement>
   implements OnTimeTimerListener{

	private static final int SHOWDISSTEN_SECOND = 24 * 3600 * 1000;
	private long mCountDownsecond=0;
	public Set<Integer> midSet;
	private SettlementFragment mSettlementFragment;
	private TimeTimer mTimer;
	private boolean mIsStartCountDown=true;
	public ProductSettlementAdapter(Context context, ArrayList<Settlement> list) {
		super(context, list);
	}

	public ProductSettlementAdapter(Context context) {
		super(context);
	}
	public void setFragment(SettlementFragment mSettlementFragment){
		this.mSettlementFragment=mSettlementFragment;
		midSet=new HashSet<Integer>();
	}
	@Override
	public View getItemView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item_settlement, parent, false);
			holder = new ViewHolder();
			holder.mvLineTop=convertView.findViewById(R.id.view_line_top);
			holder.mvViewTop=convertView.findViewById(R.id.view_top);
			holder.mvLineBottom=convertView.findViewById(R.id.view_line_bottom);
			holder.mvViewDissent=convertView.findViewById(R.id.view_dissent);
			holder.mvPayCommit=convertView.findViewById(R.id.view_pay_commit);
			holder.mIToDetail=(ImageView) convertView.findViewById(R.id.img_detail);
			holder.mTvPno=(TextView)convertView.findViewById(R.id.tv_pno);
			holder.mTvCreateTime=(TextView)convertView.findViewById(R.id.tv_create_time);
			holder.mTvDealReplace=(TextView)convertView.findViewById(R.id.tv_deal_replace);
			holder.mTvFund=(TextView)convertView.findViewById(R.id.tv_fund);
			holder.mTvStockName=(TextView)convertView.findViewById(R.id.tv_stock_name);
			holder.mTvProfit=(TextView)convertView.findViewById(R.id.tv_profit);
			holder.mTvBuyPrice=(TextView)convertView.findViewById(R.id.tv_buy_price);
			holder.mTvSellPrice=(TextView)convertView.findViewById(R.id.tv_sell_price);
			holder.mTvAmount=(TextView)convertView.findViewById(R.id.tv_amount);
			holder.mTvDissentTime=(TextView)convertView.findViewById(R.id.tv_dissent_time);
			holder.mTvDissent=(TextView)convertView.findViewById(R.id.tv_dissent_show);
			holder.mTvUserProfit=(TextView)convertView.findViewById(R.id.tv_user_profit);
			holder.mTvBreakCost=(TextView)convertView.findViewById(R.id.tv_break_cost);
			holder.mTvShareProfit=(TextView)convertView.findViewById(R.id.tv_share_profit);
			holder.mTvStopProfitPoint=(TextView)convertView.findViewById(R.id.tv_stop_profit_point);
			holder.mTvStateName=(TextView)convertView.findViewById(R.id.tv_state_name);
			holder.mTvCountdownTime=(TextView)convertView.findViewById(R.id.tv_countdown_time);
			
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();		
		}
		
		if(position==0){
		    holder.mvLineTop.setVisibility(View.GONE);
		    holder.mvViewTop.setVisibility(View.GONE);
	    }else{
		    holder.mvLineTop.setVisibility(View.VISIBLE);
		    holder.mvViewTop.setVisibility(View.VISIBLE);
	    }
		
		if(position!=getCount()-1){
			holder.mvLineBottom.setVisibility(View.GONE);
		}else{
			holder.mvLineBottom.setVisibility(View.VISIBLE);
		}
		
		assignItemView(holder,getList().get(position));
		
		return convertView;
	}

	private void assignItemView(ViewHolder holder, final Settlement settlement){
		holder.mTvPno.setText(settlement.pno);
		holder.mTvCreateTime.setText(NumberFormat.timeMilliToMonthMinuteDefault(settlement.create_time));
		holder.mTvDealReplace.setText(settlement.dealer_replace);
		holder.mTvFund.setText(Util.transformString(R.string.settlement_list_invest_fund,
				NumberFormat.decimalFormat0(settlement.fund/10000)));
		holder.mTvStockName.setText(settlement.stock_name);
		//异议申请
		if(settlement.dissent_flag==1){
			holder.mvViewDissent.setVisibility(View.VISIBLE);
			holder.mTvDissentTime.setVisibility(View.VISIBLE);
			holder.mTvDissentTime.setText(showDisstenAplayTime());
			holder.mTvDissent.setText(Util.transformString(R.string.settlement_list_dissent_enable));
			holder.mTvDissent.setTextColor(Util.transformColor(R.color.tab_cur_selected));
			holder.mTvDissent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, DissentCommitActivity.class);
					intent.putExtra(ProductIntent.EXTRA_SEELEMENT, settlement);
					mContext.startActivity(intent);
				}
			});
		}else if(settlement.dissent_flag==2){
			holder.mvViewDissent.setVisibility(View.VISIBLE);
			holder.mTvDissentTime.setVisibility(View.GONE);
			holder.mTvDissent.setText(Util.transformString(R.string.settlement_list_dissent_underway));
			holder.mTvDissent.setTextColor(Util.transformColor(R.color.color_ff7814));
			holder.mTvDissent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mContext.showToastMsg(R.string.settlement_dissent_processing);
				}
			});
		}else{
			holder.mvViewDissent.setVisibility(View.GONE);
			holder.mTvDissentTime.setVisibility(View.GONE);
			holder.mvViewDissent.setVisibility(View.GONE);
		}
		//账单正在生成中  文字显示
		if(settlement.state==0){
			holder.mTvProfit.setText(Util.transformString(R.string.default_value));
			holder.mTvSellPrice.setText(NumberFormat.moneyUnit(Util.transformString(R.string.default_value)));
			holder.mTvAmount.setText(NumberFormat.stockAmountToString(Util.transformString(R.string.default_value)));
			int defaultcolor=Util.transformColor(R.color.text_color_info);
			holder.mTvUserProfit.setText(Util.transformString(R.string.default_value));
			holder.mTvUserProfit.setTextColor(defaultcolor);
			holder.mTvBreakCost.setText(Util.transformString(R.string.default_value));
			holder.mTvBreakCost.setTextColor(defaultcolor);
			holder.mTvShareProfit.setText(Util.transformString(R.string.default_value));
			holder.mTvShareProfit.setTextColor(defaultcolor);
			holder.mTvStopProfitPoint.setText(Util.transformString(R.string.default_value));
			holder.mTvStopProfitPoint.setTextColor(defaultcolor);
		}else{
			holder.mTvProfit.setText(NumberFormat.bigDecimalFormat(settlement.profit));
			holder.mTvSellPrice.setText(NumberFormat.moneyUnit(settlement.sell_deal_price_avg));
			holder.mTvAmount.setText(NumberFormat.stockAmountToString(settlement.amount));
			holder.mTvUserProfit.setText(NumberFormat.bigDecimalFormat(settlement.user_profit));
			holder.mTvUserProfit.setTextColor(Util.getColorByPlusMinus(settlement.user_profit));
			int color=Util.transformColor(R.color.color_999999);
			holder.mTvBreakCost.setText(NumberFormat.decimalFormat(settlement.break_cost));
			holder.mTvBreakCost.setTextColor(color);
			if(settlement.share_profit>0.0){
				holder.mTvShareProfit.setText(NumberFormat.decimalFormat(settlement.share_profit));
				holder.mTvStopProfitPoint.setText("0");
			}else{
				holder.mTvShareProfit.setText("0");
				holder.mTvStopProfitPoint.setText(NumberFormat.decimalFormat(settlement.share_profit));
			}
			holder.mTvShareProfit.setTextColor(color);
			holder.mTvStopProfitPoint.setTextColor(color);
		}
		holder.mTvBuyPrice.setText(NumberFormat.moneyUnit(settlement.buy_deal_price_avg));
		//限时付款按钮
		if(settlement.state==2&&!(midSet.contains(settlement.id))){
			holder.mvPayCommit.setBackgroundResource(R.drawable.view_rect_508cf0);
			holder.mTvStateName.setVisibility(View.VISIBLE);
			holder.mTvCountdownTime.setTextColor(Util.transformColor(R.color.white_color));
			holder.mTvStateName.setTextColor(Util.transformColor(R.color.white_color));
			if(settlement.dissent_flag!=2){
				holder.mTvStateName.setText(Util.transformString(R.string.settlement_limate_pay));
				try{
					long  endtime=Long.parseLong(settlement.pay_end_time);
					if(mCountDownsecond<=endtime){
						holder.mTvCountdownTime.setText(showPayTime(endtime));
						holder.mvPayCommit.setEnabled(true);
					}else{
						holder.mTvCountdownTime.setText(showTime(0));
						holder.mvPayCommit.setBackgroundResource(R.drawable.view_rect_e0e0e0);
						holder.mTvCountdownTime.setTextColor(Util.transformColor(R.color.color_bebebe));
						holder.mTvStateName.setTextColor(Util.transformColor(R.color.color_bebebe));
						holder.mvPayCommit.setEnabled(false);
					}
				}catch(Exception e){
					e.printStackTrace();
					holder.mvPayCommit.setEnabled(false);
					holder.mTvCountdownTime.setText(showDisstenAplayTime());
				}
			}else{
				
				holder.mTvStateName.setVisibility(View.GONE);
				holder.mTvCountdownTime.setText(settlement.state_name);
				holder.mvPayCommit.setEnabled(true);
			}
			holder.mvPayCommit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showCommitDialog(settlement.p_id);
				}
			});
		}else{
			String statename="";
			if(midSet.contains(settlement.id))
				statename=Util.transformString(R.string.settlement_already_pay);
			else
				statename=settlement.state_name;
			holder.mvPayCommit.setBackgroundResource(R.drawable.view_rect_e0e0e0);
			holder.mTvStateName.setVisibility(View.GONE);
			holder.mTvCountdownTime.setText(statename);
			holder.mTvCountdownTime.setTextColor(Util.transformColor(R.color.color_bebebe));
			holder.mvPayCommit.setEnabled(false);
		}
		holder.mIToDetail.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, SettlementDetailActivity.class);
				intent.putExtra(ProductIntent.EXTRA_ID, settlement.id);
				intent.putExtra(ProductIntent.EXTRA_TYPE, settlement.p_type);
				mContext.startActivity(intent);
			}
		});
    }

	private static class ViewHolder {
		public View mvLineTop;
		public View mvViewTop;
		public View mvLineBottom;
		public View mvPayCommit;
		public View mvViewDissent;
		public ImageView mIToDetail;
		public TextView mTvPno;
		public TextView mTvCreateTime;
		public TextView mTvDealReplace;
		public TextView mTvFund;
		public TextView mTvStockName;
		public TextView mTvProfit;
		public TextView mTvBuyPrice;
		public TextView mTvSellPrice;
		public TextView mTvAmount;
		public TextView mTvDissentTime;
		public TextView mTvDissent;
		public TextView mTvUserProfit;
		public TextView mTvBreakCost;
		public TextView mTvShareProfit;
		public TextView mTvStopProfitPoint;
		public TextView mTvStateName;
		public TextView mTvCountdownTime;
	}
    
	
	public void startCountDown(long nowTime){
		if(mIsStartCountDown){
			mIsStartCountDown=false;
			mTimer = new TimeTimer();
			mTimer.setOnTimeTimerListener(this);
			mCountDownsecond=getRemainTime(nowTime);
			mTimer.startTimer();
		}
	}
	
	@Override
	public void onTimeStep() {
		notifyDataSetChanged();
		mCountDownsecond=mCountDownsecond+1000;
	}
	public void stopCountDown(){
		if(!mIsStartCountDown){
			mIsStartCountDown=true;
		}
		mCountDownsecond=0;
		if(mTimer!=null)
		   mTimer.stopTimer();
	}
	private long getRemainTime(long nowTime){
		String time=TimeFormat.milliToHourSecond(nowTime);
		String times[] = new String[3];
		times=time.split(":");
		return (Long.parseLong(times[0])*3600
				            +Long.parseLong(times[1])*60
				            +Long.parseLong(times[2]))*1000;  
	}
	
	private String showDisstenAplayTime(){
		long showtime=(SHOWDISSTEN_SECOND - mCountDownsecond);
		return showTime(showtime);
	}
	private String showPayTime(long endtime){
		long showtime=(endtime - mCountDownsecond);
		return showTime(showtime);
	}
	private String showTime(long showtime){
		long hour = showtime/(60*60*1000); 
		long minute = (showtime - hour*60*60*1000)/(60*1000); 
		long second = (showtime - hour*60*60*1000 - minute*60*1000)/1000; 
		return grtTimeStr(hour)+":"+grtTimeStr(minute)+":"+grtTimeStr(second);
	}
	private String grtTimeStr(long time) {
	    if(time<10)
	    	return "0"+time;
	    else
	    	return ""+time;
    }

	private void showCommitDialog(final int p_id) {
		new ProductDialog.Builder(mContext)
				.setContentText(Util.transformString(R.string.settlement_commit_pay))
				.setPositiveButton(R.string.btn_cancel, null)
				.setNegativeButton(R.string.btn_confirm,
						new ProductDialog.ProductDialogListener() {
							
							@Override
							public void onDialogClick(int id) {
								mSettlementFragment.instructCommit(p_id);
							}
						}).create().show();
	}

	public void setDoSettle(int p_id) {
		midSet.add(p_id);
		notifyDataSetChanged();
	}
	
	public void clearDoSettleId() {
		if(midSet!=null&&midSet.size()>0)
			midSet.clear();
	}
}
