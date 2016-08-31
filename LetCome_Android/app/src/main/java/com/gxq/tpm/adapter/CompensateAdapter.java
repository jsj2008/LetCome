package com.gxq.tpm.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.activity.mine.UserCompensateDetailActivity;
import com.gxq.tpm.mode.mine.UserCompensateOrders.CompensateDetail;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;

public class CompensateAdapter extends ArrayListAdapter<CompensateDetail>{
	private SuperActivity mContext;
	
	public CompensateAdapter(SuperActivity context) {
		super(context);
		this.mContext = context;
	}
	
	@Override
	public View getItemView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.settlement_correction_list_item, parent, false);
			holder = new ViewHolder();
			
			holder.tv_line_top=convertView.findViewById(R.id.view_line_top);
			holder.tv_line=convertView.findViewById(R.id.view_line);
			holder.tv_line_bottom=convertView.findViewById(R.id.view_bottom);
			
			holder.tv_settlement_time = (TextView) convertView.findViewById(R.id.tv_settlement_time);
			holder.tv_settlement_money = (TextView)convertView.findViewById(R.id.tv_settlement_money);
			holder.tv_settlement_type = (TextView)convertView.findViewById(R.id.tv_settlement_type);
			holder.tv_settlement_pay = (TextView)convertView.findViewById(R.id.tv_settlement_pay);
			holder.tv_settlement_operation = (TextView)convertView.findViewById(R.id.tv_settlement_operation);
		} else {
			holder = (ViewHolder) convertView.getTag();		
		}
		
		if(position==0){
			holder.tv_line_top.setVisibility(View.VISIBLE);
		}else{
			holder.tv_line_top.setVisibility(View.GONE);
		}
		
		if(position==getList().size()-1){
			holder.tv_line.setVisibility(View.GONE);
			holder.tv_line_bottom.setVisibility(View.VISIBLE);
		}else{
			holder.tv_line.setVisibility(View.VISIBLE);
			holder.tv_line_bottom.setVisibility(View.GONE);
		}
		
		if (getList() != null && getList().size() > 0){
			assignItemView(holder,getList().get(position));
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserCompensateDetailActivity.class);
				intent.putExtra(ProductIntent.EXTRA_DETAIL, getList().get(position));
				mContext.startActivity(intent);
			}
		});
		convertView.setTag(holder);
		return convertView;
	}

	private void assignItemView(ViewHolder holder, CompensateDetail detail) {
	    if (detail != null) {
	    	holder.tv_settlement_time.setText(TimeFormat.dateDefaultFormat(detail.create_time));
	    	holder.tv_settlement_type.setText(detail.type_name);
	    	
	    	double profit = Math.abs(detail.delta_profit);
	    	int color = Util.transformColor(R.color.gain_color);
	    	String format = "+###,##0.00";
	    	
	    	// 0 未支付 1 已支付 2 未补偿 3 已补偿 4支付中
	    	if (detail.state == 0) {
	    		holder.tv_settlement_pay.setVisibility(View.VISIBLE);
	    		holder.tv_settlement_operation.setVisibility(View.GONE);
	    		
	    		format = "-###,##0.00";
	    		color = Util.transformColor(R.color.loss_color);
	    	} else {
	    		holder.tv_settlement_pay.setVisibility(View.GONE);
	    		holder.tv_settlement_operation.setVisibility(View.VISIBLE);
	    		holder.tv_settlement_operation.setText(detail.state_name);
	    		holder.tv_settlement_operation.setTextColor(Util.transformColor(R.color.text_color_title));
	    		if (detail.state == 2) {
	    			holder.tv_settlement_operation.setTextColor(Util.transformColor(R.color.color_ff8c00));
	    		} else if (detail.state == 3) {
//	    			holder.tv_settlement_operation.setText(R.string.dissent_correction_state_paid);
	    		} else {
//	    			holder.tv_settlement_operation.setText(detail.state_name);
	    			format = "-###,##0.00";
	    			color = Util.transformColor(R.color.loss_color);
	    		}
	    	}
	    	holder.tv_settlement_money.setText(
	    			NumberFormat.moneySymbol(NumberFormat.decimalFormat(format, profit)));
	    	holder.tv_settlement_money.setTextColor(color);
	    }
    }

	private static class ViewHolder {
		public View tv_line_top;
		public View tv_line;
		public View tv_line_bottom;
		public TextView tv_settlement_time;
		public TextView tv_settlement_money;
		public TextView tv_settlement_type;
		public TextView tv_settlement_pay;
		public TextView tv_settlement_operation;
	}
	
}
