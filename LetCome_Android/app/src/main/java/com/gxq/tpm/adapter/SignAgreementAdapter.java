package com.gxq.tpm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.mode.UnsignAgreement;
import com.gxq.tpm.tools.TimeFormat;

public class SignAgreementAdapter extends ArrayListAdapter<UnsignAgreement.Agreement>{

	public interface OnItemClickListener {
		public void onItemClick(View v,Bundle bundle);
	}
	
	private OnItemClickListener mListener;
	
	public SignAgreementAdapter(Context context, OnItemClickListener l) {
		super(context);
		mListener = l;
	}

	@Override
	public View getItemView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.protocol_list_item, parent, false);
			holder = new ViewHolder();
			holder.mProtocolName = (TextView)convertView.findViewById(R.id.tv_protocol_name);
			holder.mProtocolTime = (TextView)convertView.findViewById(R.id.tv_protocol_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (getCount() > 0) {
			assignItemView(holder, getList().get(position));
		} else {
			assignItemView(holder, null);
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					UnsignAgreement.Agreement agreement = (UnsignAgreement.Agreement) getItem(position);
					Bundle bundle = new Bundle();
					bundle.putString(ProductIntent.EXTRA_ARTICLE_ID, agreement.id);
					bundle.putString(ProductIntent.EXTRA_ARTICLE_NAME, agreement.name);
					bundle.putInt(ProductIntent.EXTRA_SWITCH_ID, R.id.sign_agreement_fragment);
					mListener.onItemClick(v, bundle);
				}
			}
		});

		return convertView;
	}
	
	private void assignItemView(ViewHolder holder, UnsignAgreement.Agreement data) {
	    if (data != null) {
	    	holder.mProtocolName.setText(data.name + data.version);
	    	holder.mProtocolTime.setText(TimeFormat.milliToYearDate(data.publish_time));
	    }
    }

	public static class ViewHolder {
		public TextView mProtocolName;
		public TextView mProtocolTime;
	}

}
