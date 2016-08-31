package com.gxq.tpm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.letcome.R;

public class DissentPointAdapter extends ArrayListAdapter<String> implements View.OnClickListener {
	private int mPosition = -1;
	private int mNormalColor;
	private int mSelectedColor;
	private int mTextColor;
	private int mTextSelectedColor;
	
	public DissentPointAdapter(Context context) {
		super(context);
		mNormalColor = mContext.getResources().getColor(R.color.white_color);
		mSelectedColor = mContext.getResources().getColor(R.color.product_bg);
		mTextColor = mContext.getResources().getColor(R.color.text_color_title);
		mTextSelectedColor = mContext.getResources().getColor(R.color.text_color_sub);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.dissent_index_item, parent, false);
			
			viewHolder = new ViewHolder();
			viewHolder.container = convertView.findViewById(R.id.container_content);
			viewHolder.ivItemChoose = (ImageView) convertView.findViewById(R.id.iv_item_choose);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.position = position;
		
		if (position == mPosition) {
			viewHolder.container.setBackgroundColor(mSelectedColor);
			viewHolder.ivItemChoose.setVisibility(View.VISIBLE);
			viewHolder.tvContent.setTextColor(mTextSelectedColor);
		} else {
			viewHolder.container.setBackgroundColor(mNormalColor);
			viewHolder.ivItemChoose.setVisibility(View.INVISIBLE);
			
			viewHolder.tvContent.setTextColor(mTextColor);
		}
		viewHolder.tvContent.setText(getList().get(position));
		convertView.setOnClickListener(this);
		
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		ViewHolder viewHolder = (ViewHolder) v.getTag();
		mPosition = viewHolder.position;
		notifyDataSetChanged();
	}
	
	public void setSelected(int position) {
		mPosition = position;
	}
	
	public int getSelected() {
		return mPosition;
	}
	
	private class ViewHolder {
		public int position;
		public View container;
		public ImageView ivItemChoose;
		public TextView tvContent;
	}
	
}
