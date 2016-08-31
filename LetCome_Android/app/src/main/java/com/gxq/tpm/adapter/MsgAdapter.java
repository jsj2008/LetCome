package com.gxq.tpm.adapter;

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
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.mode.mine.MsgList;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.WebHelper;
import com.gxq.tpm.tools.WebHelper.WebParams;

public class MsgAdapter extends ArrayListAdapter<MsgList.Msg> {
	protected static final String Tag = "MsgAdapter";
	
	public MsgAdapter(Context context) {
	    this(context, null);
    }
	
	public MsgAdapter(Context context, List<MsgList.Msg> list) {
	    super(context, list);
    }

	@Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.msg_item, parent, false);
			holder = new ViewHolder();
			holder.view_msg_top_line = convertView.findViewById(R.id.view_line_top);
			holder.tv_msg_title = (TextView) convertView.findViewById(R.id.tv_msg_title);
			holder.tv_msg_time = (TextView) convertView.findViewById(R.id.tv_msg_time);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position==0){
			holder.view_msg_top_line.setVisibility(View.VISIBLE);
		}else{
			holder.view_msg_top_line.setVisibility(View.GONE);
		}
		
		if (getList() != null && getList().size() > 0) {
			assignItemView(holder, getList().get(position));
		} else {
			assignItemView(holder, null);
		}
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (getList() == null || getList().size() == 0) {
					return;
				} else {
					MsgList.Msg msg = getList().get(position);
					long msgId = msg.msg_id;
//					jumpUrl("消息详情", ServiceConfig.getServiceH5() + RequestInfo.H5_MESSAGE_DETAIL.getOperationType()+"?name="+name+"&time="+time+"&msgId="+msgId+"&uid="+uid+"&session_id=" + sessionId);
					jumpUrl(Util.transformString(R.string.title_letter_detail), 
							WebHelper.addParam(RequestInfo.LETTER_DETAIL)
							.addParam(WebParams.MSGID, Long.toString(msgId)).getUrl());
					if (msg.read_status == 0) {
						msg.read_status = 1;
					}
				}
			}
		});
		
		convertView.setTag(holder);
	    return convertView;
    }

	private void assignItemView(ViewHolder holder, MsgList.Msg data) {
		if (data != null) {
			switch (data.read_status) {
			case 0: // 未读
				setTime(holder.tv_msg_time, data.create_time, false);
				setTitle(holder.tv_msg_title, data.message_title, false);
				break;
			case 1: // 已读
				setTime(holder.tv_msg_time, data.create_time, true);
				setTitle(holder.tv_msg_title, data.message_title, true);
				break;
			}
	    }
    }
	
	private void setTitle(TextView tvTitle, String title, boolean isRead) {
		tvTitle.setText(title);
		if (isRead) {
			tvTitle.setTextColor(Util.transformColor(R.color.text_color_info));
		} else {
			tvTitle.setTextColor(Util.transformColor(R.color.text_color_title));
		}
	}
	
	private void setTime(TextView tvTime, long millisecond, boolean isRead) {
		tvTime.setText(TimeFormat.milliToYearDate(millisecond));
		if (isRead) {
			tvTime.setTextColor(Util.transformColor(R.color.text_color_info));
		} else {
			tvTime.setTextColor(Util.transformColor(R.color.text_color_sub));
		}
	}
	
	private void jumpUrl(String title, String url){
		Intent intent = new Intent(mContext, WebActivity.class);
		intent.putExtra(ProductIntent.EXTRA_TITLE, title);
		intent.putExtra(ProductIntent.EXTRA_URL, url);
		intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
		mContext.startActivity(intent);
	}

	public static class ViewHolder {
		public View view_msg_top_line;
		public TextView tv_msg_title;
		public TextView tv_msg_time;
	}
	
}
