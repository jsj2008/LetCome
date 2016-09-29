package com.gxq.tpm.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.letcome.R;

public class CItemBar extends RelativeLayout implements View.OnClickListener {
	private ImageView mLeftView;
	private TextView mTitleView;
	
	private TextView mContentView;
	private TextView mContentSubView;
	private ImageView mRightView;
	
	private OnItemBarClickListener mListener;
	
	public CItemBar(Context context) {
		this(context, null);
	}

	public CItemBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CItemBar);
		Resources resource = getResources();
		
		int imageResId = a.getResourceId(R.styleable.CItemBar_itemTitleImage, 0);
		
		String titleText = a.getString(R.styleable.CItemBar_itemTitleText);
		int itemTitleTextSize = a.getDimensionPixelSize(R.styleable.CItemBar_itemTitleTextSize, -1);
		int itemTitleTextColor = a.getColor(R.styleable.CItemBar_itemTitleTextColor,
				resource.getColor(R.color.text_color_title));

		String contentText = a.getString(R.styleable.CItemBar_itemContentText);
		int itemContentTextSize = a.getDimensionPixelSize(R.styleable.CItemBar_itemContentTextSize, -1);
		int itemContentTextColor = a.getColor(R.styleable.CItemBar_itemContentTextColor,
				resource.getColor(R.color.text_color_main));
		
		String contentSubText = a.getString(R.styleable.CItemBar_itemContentSubText);
		
		boolean show = a.getBoolean(R.styleable.CItemBar_itemShowDetail, false);
		boolean imgshow = a.getBoolean(R.styleable.CItemBar_itemShowRightImg,true);
		
		a.recycle();
		
		if (!isInEditMode()) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.custom_item_bar, this, true);
			mLeftView = (ImageView) findViewById(R.id.iv_title);
			
			setTitleImage(imageResId);
			
			mTitleView = (TextView) view.findViewById(R.id.tv_title);
			setTitle(titleText);
			setTitleSize(itemTitleTextSize);
			setTitleColor(itemTitleTextColor);
			
			mContentView = (TextView) view.findViewById(R.id.tv_content);
			setContent(contentText);
			setContentSize(itemContentTextSize);
			setContentColor(itemContentTextColor);
			
			mContentSubView = (TextView) view.findViewById(R.id.tv_content_sub);
			setContentSub(contentSubText);
			
			mRightView = (ImageView) findViewById(R.id.iv_content);
			
			setOnClickListener(this);
			setShowDetail(show,imgshow);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onItemClick((CItemBar)v);
		}
	}
	
	public void setOnItemBarClickListener(OnItemBarClickListener listener) {
		this.mListener = listener;
	}
	
	public void setTitleImage(int resId) {
		if (resId == 0) {
			mLeftView.setVisibility(View.GONE);
		} else {
			mLeftView.setVisibility(View.VISIBLE);
			mLeftView.setImageResource(resId);
		}
	}

	public void setTitleImage(Bitmap bm) {
		if (bm == null) {
			mLeftView.setVisibility(View.GONE);
		} else {
			mLeftView.setVisibility(View.VISIBLE);
			mLeftView.setImageBitmap(bm);
		}
	}

	public void setTitle(CharSequence text) {
		if (text == null) {
			mTitleView.setVisibility(View.GONE);
		} else {
			mTitleView.setText(text);
			mTitleView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setTitle(int resId) {
		if (resId <= 0) {
			mTitleView.setVisibility(View.GONE);
		} else {
			mTitleView.setText(resId);
			mTitleView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setTitleSize(float size) {
		if (size > 0) {
			mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
	}
	
	public void setTitleColor(int color) {
		mTitleView.setTextColor(color);
	}
	
	public void setContent(int resId) {
		if (resId == 0) {
			mContentView.setVisibility(View.GONE);
		} else {
			mContentView.setText(resId);
			mContentView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setContent(CharSequence text) {
		if (text == null) {
			mContentView.setVisibility(View.GONE);
		} else {
			mContentView.setText(text);
			mContentView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setContentSize(float size) {
		if (size > 0) {
			mContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
		}
	}
	
	public void setContentColor(int color) {
		mContentView.setTextColor(color);
	}
	
	public void setContentDrawable(int resId) {
		if (resId == 0) {
			mContentView.setVisibility(View.GONE);
		} else {
			mContentView.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0);
			mContentView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setContentSub(CharSequence text) {
		if (text == null) {
			mContentSubView.setVisibility(View.GONE);
		} else {
			mContentSubView.setText(text);
			mContentSubView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setContentSubSize(float size) {
		if (size > 0){
			mContentSubView.setTextSize(size);
		}
	}
	
	public void setContentSubColor(int color) {
		if (color > 0)
			mContentSubView.setTextColor(color);
	}
	
	public void setShowDetail(boolean show,boolean imgshow) {
		setEnabled(show);
		mRightView.setVisibility(show&&imgshow ? View.VISIBLE : View.GONE);
	}
	
	public static interface OnItemBarClickListener {
		public void onItemClick(CItemBar v);
	}
	
}
