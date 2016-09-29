package com.gxq.tpm.ui;

import com.letcome.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CTitleBar extends RelativeLayout implements View.OnClickListener {

	private View mContainer, mLeftController, mRightController;
	private ImageView mIvLeft;
	private TextView mTvRightText, mTvTitle;
	private RelativeLayout mTitleView;
	
	private OnTitleBarClickListener mListener;
	
	public CTitleBar(Context context) {
		this(context, null);
	}
	
	public CTitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		View.inflate(getContext(), R.layout.title_bar, this);
		
		mContainer = findViewById(R.id.titleBar);
		
		mLeftController = findViewById(R.id.rl_titleBar_left);
		mIvLeft = (ImageView) findViewById(R.id.iv_titleBar_left);
		mLeftController.setOnClickListener(this);

		mTitleView = (RelativeLayout)findViewById(R.id.titleBar_titleview);

		mRightController = findViewById(R.id.rl_titleBar_right);
		mTvRightText = (TextView) findViewById(R.id.tv_titleBar_right);
		mRightController.setOnClickListener(this);

		mTvTitle = (TextView) findViewById(R.id.titleBar_title);
		
		setVisibility(View.GONE);
	}

	public void setTitleView(View view){
		hideLeft();
		hideRight();
		showTitleBar();
		mTitleView.addView(view);
		mTitleView.setVisibility(VISIBLE);
		mTvTitle.setVisibility(GONE);
	}
	
	public void setTitle(CharSequence title) {
		showTitleBar();

		hideLeft();
		hideRight();
		
		mTvTitle.setVisibility(View.VISIBLE);
		mTvTitle.setText(title);
	}
	
	public void setTitle2(CharSequence title) {
		setTitle(title);
		mContainer.setBackgroundResource(getResources().getColor(R.color.tab_cur_selected));
	}

	public void setTitle(int resId) {
		showTitleBar();

		hideLeft();
		hideRight();

		mTvTitle.setVisibility(View.VISIBLE);
		mTvTitle.setText(resId);
	}
	
	public void setTitle2(int resId) {
		setTitle(resId);
		mTvTitle.setTextColor(getResources().getColor(R.color.white_color));
		mContainer.setBackgroundResource(R.color.tab_cur_selected);
	}
	
	private void showTitleBar() {
		setVisibility(View.VISIBLE);
	}

	public void hideLeft() {
		mLeftController.setVisibility(View.GONE);
	}

	public void hideRight() {
		mRightController.setVisibility(View.GONE);
	}

	public void hideTitleBar() {
		setVisibility(View.GONE);
	}
	
	public void setLeftImage(int resId) {
		showTitleBar();
		
		mLeftController.setVisibility(View.VISIBLE);
		mIvLeft.setImageResource(resId);
	}
	
	public void showBackImage() {
		showTitleBar();
		
		mLeftController.setVisibility(View.VISIBLE);
	}
	
	public void setRightText(int resId) {
		showTitleBar();
		
		mRightController.setVisibility(View.VISIBLE);
		mTvRightText.setText(resId);
	}
	
	public void setRightText(CharSequence title) {
		showTitleBar();
		
		mRightController.setVisibility(View.VISIBLE);
		mTvRightText.setText(title);
	}
	
	public void setOnTitleBarClickListener(OnTitleBarClickListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_titleBar_left:
			if (mListener != null) {
				mListener.onLeftClick(v);
			}
			break;
		case R.id.rl_titleBar_right:
			if (mListener != null) {
				mListener.onRightClick(v);
			}
			break;
		}
	}
	
	public static interface OnTitleBarClickListener {
		public void onLeftClick(View view);
		public void onRightClick(View view);
	}

}
