package com.gxq.tpm.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.mine.DissentRecordDetailActivity;
import com.gxq.tpm.mode.mine.DissentOrders.DissentDetail;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;

public class DissentAdapter extends ArrayListAdapter<DissentDetail>{

	public DissentAdapter(Context context) {
		super(context);
	}
	
	public DissentAdapter(Context context, ArrayList<DissentDetail> list) {
		super(context, list);
	}

	@Override
	public View getItemView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.dissent_list_item, parent, false);
			holder = new ViewHolder();
			
			holder.tv_line_top=convertView.findViewById(R.id.view_line_top);
			holder.tv_line=convertView.findViewById(R.id.view_line);
			holder.tv_line_bottom=convertView.findViewById(R.id.view_bottom);
			
			holder.tv_dissent_pid = (TextView) convertView.findViewById(R.id.tv_dissent_pid);
			holder.tv_dissent_point = (TextView) convertView.findViewById(R.id.tv_dissent_point);
			holder.tv_dissent_result = (TextView) convertView.findViewById(R.id.tv_dissent_result);
			//holder.tv_dissent_need_correct = (TextView) convertView.findViewById(R.id.tv_dissent_need_correct);
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
		
		if (getList() != null && getList().size() > 0) {
			assignItemView(holder,getList() .get(position));
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getList() != null && getList().size() > 0) {
					DissentDetail dissentDetail = getList().get(position);
					Intent intent = new Intent(mContext, DissentRecordDetailActivity.class);
					intent.putExtra(ProductIntent.EXTRA_DETAIL, dissentDetail);
					
					mContext.startActivity(intent);
				}
			}
		});
		convertView.setTag(holder);
		return convertView;
	}

	private void assignItemView(ViewHolder holder, DissentDetail detail) {
		if (detail!=null) {
	    	holder.tv_dissent_pid.setText(TimeFormat.dateDefaultFormat(detail.create_time));
	    	holder.tv_dissent_point.setText(detail.point_name);

			String info = "";
			holder.tv_dissent_result.setTextColor(Util
					.transformColor(R.color.color_222222));
			switch (detail.state) {
			case 0:
				info = Util.transformString(R.string.dissent_state_wait);
				holder.tv_dissent_result.setTextColor(Util
						.transformColor(R.color.color_fa4b32));
				break;
			case 1:
				info = Util.transformString(R.string.dissent_state_process);
				break;
			case 2:
				info = Util.transformString(R.string.dissent_state_no_correct);
				break;
			case 3:
				info = Util.transformString(R.string.dissent_state_has_correct);
				break;
			default:
				info = Util.transformString(R.string.dissent_state_wait);
				holder.tv_dissent_result.setTextColor(Util
						.transformColor(R.color.color_fa4b32));
				break;
			}
			holder.tv_dissent_result.setText(info);
		}
    }
	
	private static class ViewHolder {
		public View tv_line_top;
		public View tv_line;
		public View tv_line_bottom;
		public TextView tv_dissent_pid;
		public TextView tv_dissent_point;
		public TextView tv_dissent_result;
	}
}
