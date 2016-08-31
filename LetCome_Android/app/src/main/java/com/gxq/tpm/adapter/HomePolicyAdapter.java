package com.gxq.tpm.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.cooperation.CooperationStrategyClaimActivity;
import com.gxq.tpm.mode.cooperation.ProductPolicyList.Policy;
import com.gxq.tpm.mode.cooperation.ProductPolicyList.PolicyItem;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Print;
import com.gxq.tpm.tools.Util;

public class HomePolicyAdapter extends ArrayListAdapter<Policy>{
	
	private ArrayList<PolicyItem> mPolicyItems = new ArrayList<PolicyItem>();

	public HomePolicyAdapter(Context context) {
		super(context);
	}
	
	public HomePolicyAdapter(Context context, List<Policy> list) {
		super(context, list);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item_policy, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.mTvDealerReplace = (TextView) convertView.findViewById(R.id.tv_dealer_replace);
			viewHolder.mTvFund = (TextView) convertView.findViewById(R.id.tv_fund);
			viewHolder.mTvFundStr = (TextView) convertView.findViewById(R.id.tv_fund_str);
			viewHolder.mTvStockName = (TextView) convertView.findViewById(R.id.tv_stock_name);
			viewHolder.mTvNext = (TextView) convertView.findViewById(R.id.tv_next_step);
			viewHolder.mTvEndTime = (TextView) convertView.findViewById(R.id.tv_end_time);
			viewHolder.mDivider = convertView.findViewById(R.id.divider);
			viewHolder.mSpaceDivider = convertView.findViewById(R.id.space_divider);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		assignItem(viewHolder, position);
		
//		viewHolder.mDivider.setVisibility( position == getCount() - 1 ? View.GONE : View.VISIBLE);
		
		viewHolder.mTvNext.setTag(R.id.key_2, position);
		viewHolder.mTvNext.setOnClickListener(onClickListener);
		
		return convertView;
	}
	
	private void assignItem(ViewHolder viewHolder, int position){
		Policy policy = getList().get(position);
		if(null == policy){
			return;
		}
		viewHolder.mTvDealerReplace.setText(policy.investor_code);
		viewHolder.mTvFund.setText(mContext.getString(R.string.ten_thousand, NumberFormat.decimalFormat0(policy.fund/10000)));
		String fund_str = "";
		switch (policy.fund_state) {
		case 0:
			fund_str = Util.transformString(R.string.cooperation_strategy_policy_item_fund_state_0);
			break;
		case 1:
			fund_str = Util.transformString(R.string.cooperation_strategy_policy_item_fund_state_1);
			break;
		case 2:
			fund_str = Util.transformString(R.string.cooperation_strategy_policy_item_fund_state_2);
			break;
		}
		viewHolder.mTvFundStr.setText(fund_str);
		viewHolder.mTvStockName.setText(policy.stock_name);
		viewHolder.mTvEndTime.setText(policy.end_time_str);
		viewHolder.mTvNext.setEnabled(policy.state == 1);
		if(mPolicyItems.size() > 1){
			int index = 0;
			for (int i = 0; i < mPolicyItems.size(); i++) {
				PolicyItem policyItem = mPolicyItems.get(i);
				index += policyItem.policies.size() - 1;
				if(index == position && index != getList().size() - 1 ){
					viewHolder.mSpaceDivider.setVisibility(View.VISIBLE);
					return;
				}else{
					viewHolder.mSpaceDivider.setVisibility(View.GONE);
				}
			}
		}
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {			
			Policy policy = (Policy) getItem((Integer)v.getTag(R.id.key_2));
			Intent intent = new Intent(mContext, CooperationStrategyClaimActivity.class);
			intent.putExtra(ProductIntent.EXTRA_POLICY, policy);
			mContext.startActivity(intent);
		}
	};
	
	public void setPolicyItemList(ArrayList<PolicyItem> policyItems){
		mPolicyItems.clear();
		mPolicyItems.addAll(policyItems);
	}
	
	private class ViewHolder{
		public TextView mTvDealerReplace;
		public TextView mTvFund;
		public TextView mTvFundStr;
		public TextView mTvStockName;
		public TextView mTvNext;
		public TextView mTvEndTime;
		public View mDivider;
		public View mSpaceDivider;
	}

}
