package com.gxq.tpm.ui;

import com.letcome.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductDialog implements View.OnClickListener {
	public final static int POSITIVE_BUTTON = 1;
	public final static int Negative_BUTTON = 2;
	public final static int Neutral_BUTTON 	= 3;
	public final static int CLOSE_BUTTON	= 4;
	
	private Context mContext;
	private Dialog mDialog;
	
	private View mContainerTitleContent, mContainerBtn, mContainerBtn2, mContainerBtn3;
	private TextView mTvTitle, mTvTitleContent;
	private TextView mTvContent, mButton1, mButton2, mButton3;
	private ImageView mIvClose;
	private RelativeLayout mContainer;
	
	private ProductDialogListener mPositiveListener, mNegativeListener, mNeutralListener, mCloseListener;
	
	public ProductDialog(Context context) {
		this(context, R.layout.product_large_dialog);
	}
	
	public ProductDialog(Context context, int layoutId) {
		this.mContext = context;
		mDialog = new Dialog(mContext, R.style.DialogStyle);
		mDialog.setContentView(layoutId);
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		mContainer = (RelativeLayout) mDialog.findViewById(R.id.container_content);
		
		mIvClose = (ImageView) mDialog.findViewById(R.id.iv_close);
		mIvClose.setOnClickListener(this);
		mTvContent = (TextView) mDialog.findViewById(R.id.tv_content);
		
		mContainerTitleContent = mDialog.findViewById(R.id.container_title_content);
		mTvTitle = (TextView) mDialog.findViewById(R.id.tv_title);
		mTvTitleContent = (TextView) mDialog.findViewById(R.id.tv_title_content);
		
		mContainerBtn = mDialog.findViewById(R.id.container_button);
		mButton1 = (TextView) mDialog.findViewById(R.id.tv_button1);
		mContainerBtn2 = mDialog.findViewById(R.id.container_button2);
		mButton2 = (TextView) mDialog.findViewById(R.id.tv_button2);
		mContainerBtn3 = mDialog.findViewById(R.id.container_button3);
		mButton3 = (TextView) mDialog.findViewById(R.id.tv_button3);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_button1) {
			if (mPositiveListener != null) {
				mPositiveListener.onDialogClick(POSITIVE_BUTTON);
			}
			mDialog.dismiss();
		} else if (v.getId() == R.id.tv_button2) {
			if (mNegativeListener != null) {
				mNegativeListener.onDialogClick(Negative_BUTTON);
			}
			mDialog.dismiss();
		} else if (v.getId() == R.id.tv_button3) {
			if (mNeutralListener != null) {
				mNeutralListener.onDialogClick(Neutral_BUTTON);
			}
			mDialog.dismiss();
		} else if (v.getId() == R.id.iv_close) {
			if (mCloseListener != null) {
				mCloseListener.onDialogClick(CLOSE_BUTTON);
			}
			mDialog.dismiss();
		}
	}

	public void setContentView(View view) {
		mContainer.addView(view);
	}
	
	public void setContentView(int layoutResId) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mContainer.addView(inflater.inflate(layoutResId, mContainer, false));
	}
	
	public void setContentText(CharSequence text) {
		mTvContent.setText(text);
		mTvContent.setVisibility(View.VISIBLE);
	}
	
	public void setContentText(int resId) {
		mTvContent.setText(resId);
		mTvContent.setVisibility(View.VISIBLE);
	}
	
	public void setContentText(CharSequence text, int drawResId) {
		mTvContent.setText(text);
		Drawable drawable = mContext.getResources().getDrawable(drawResId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		mTvContent.setCompoundDrawables(drawable, null, null, null);
		mTvContent.setVisibility(View.VISIBLE);
	}
	
	public void setContentText(int resId, int drawResId) {
		mTvContent.setText(resId);
		Drawable drawable = mContext.getResources().getDrawable(drawResId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		mTvContent.setCompoundDrawables(drawable, null, null, null);
		mTvContent.setVisibility(View.VISIBLE);
	}
	
	public void setContent(int titleResId, int drawableResId, int resId) {
		mTvTitle.setText(titleResId);
		Drawable drawable = mContext.getResources().getDrawable(drawableResId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		mTvTitle.setCompoundDrawables(drawable, null, null, null);
		
		mTvTitleContent.setText(resId);
		
		mContainerTitleContent.setVisibility(View.VISIBLE);
	}
	
	public void setContent(int titleResId, CharSequence text) {
		mTvTitle.setText(titleResId);
		mTvTitleContent.setText(text);
		mContainerTitleContent.setVisibility(View.VISIBLE);
	}
	
	public void setContent(int titleResId, int resId) {
		mTvTitle.setText(titleResId);
		mTvTitleContent.setText(resId);
		
		mContainerTitleContent.setVisibility(View.VISIBLE);
	}
	
	public void setContent(int titleResId, int drawResId, CharSequence text) {
		mTvTitle.setText(titleResId);
		Drawable drawable = mContext.getResources().getDrawable(drawResId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		mTvTitle.setCompoundDrawables(drawable, null, null, null);
		
		mTvTitleContent.setText(text);
		mContainerTitleContent.setVisibility(View.VISIBLE);
	}
	
	public View findViewById(int resId) {
		return mDialog.findViewById(resId);
	}
	
	public void setCancelable(OnCancelListener listener) {
		mDialog.setCancelable(true);
		mDialog.setCanceledOnTouchOutside(true);
		
		mDialog.setOnCancelListener(listener);
	}
	
	public void setOnDismissListener(OnDismissListener listener) {
		mDialog.setOnDismissListener(listener);
	}
	
	public void setPositiveButton(int resId, ProductDialogListener listener) {
		mContainerBtn.setVisibility(View.VISIBLE);
		
		mButton1.setText(resId);
		mButton1.setOnClickListener(this);
		
		mPositiveListener = listener;
	}
	
	public void setPositiveButton(CharSequence button, ProductDialogListener listener) {
		mContainerBtn.setVisibility(View.VISIBLE);
		
		mButton1.setText(button);
		mButton1.setOnClickListener(this);
		
		mPositiveListener = listener;
	}
	
	public void setNegativeButton(int resId, ProductDialogListener listener) {
		mContainerBtn2.setVisibility(View.VISIBLE);
		
		mButton2.setText(resId);
		mButton2.setOnClickListener(this);
		
		mNegativeListener = listener;
	}
	
	public void setNegativeButton(CharSequence button, ProductDialogListener listener) {
		mContainerBtn2.setVisibility(View.VISIBLE);
		
		mButton2.setText(button);
		mButton2.setOnClickListener(this);
		
		mNegativeListener = listener;
	}

	public void setNeutralButton(int resId, ProductDialogListener listener) {
		mContainerBtn3.setVisibility(View.VISIBLE);
		
		mButton3.setText(resId);
		mButton3.setOnClickListener(this);
		
		mNeutralListener = listener;
	}
	
	public void setNeutralButton(CharSequence button, ProductDialogListener listener) {
		mContainerBtn3.setVisibility(View.VISIBLE);
		
		mButton3.setText(button);
		mButton3.setOnClickListener(this);
		
		mNeutralListener = listener;
	}
	
	public void setOnCloseListener(ProductDialogListener listener) {
		mIvClose.setVisibility(View.VISIBLE);
		mCloseListener = listener;
	}
	
	public void show() {
		mDialog.show();		
	}
	
	public boolean isShowing() {
		if (mDialog != null) {
			return mDialog.isShowing();
		}
		return false;
	}

	public void dismiss() {
		mDialog.dismiss();
	}

	public void enableButton(int id, boolean enabled) {
		TextView button = null;
		if (id == POSITIVE_BUTTON) {
			button = mButton1;
		} else if (id == Negative_BUTTON) {
			button = mButton2;
		} else if (id == Neutral_BUTTON) {
			button = mButton3;
		}
		if (button != null)
			button.setEnabled(enabled);
	}
	
	public static class Builder {
		private ProductDialog mProductDialog;
		
		public Builder(Context context) {
			this.mProductDialog = new ProductDialog(context);
		}
		
		public Builder(Context context, boolean small) {
			this.mProductDialog = new ProductDialog(context,
					small ? R.layout.product_small_dialog : R.layout.product_large_dialog);
		}
		
		public Builder setContentView(View view) {
			mProductDialog.setContentView(view);
			return this;
		}
		
		public Builder setContentView(int layoutResId) {
			mProductDialog.setContentView(layoutResId);
			return this;
		}
		
		public Builder setContentText(CharSequence text) {
			mProductDialog.setContentText(text);
			return this;
		}
		
		public Builder setContentText(int resId) {
			mProductDialog.setContentText(resId);
			return this;
		}
		
		public Builder setContentText(CharSequence text, int drawResId) {
			mProductDialog.setContentText(text, drawResId);
			return this;
		}
		
		public Builder setContentText(int resId, int drawResId) {
			mProductDialog.setContentText(resId, drawResId);
			return this;
		}
		
		public Builder setContent(int titleResId, int drawableResId, int resId) {
			mProductDialog.setContent(titleResId, drawableResId, resId);
			return this;
		}
		
		public Builder setContent(int titleResId, int drawableResId, CharSequence text) {
			mProductDialog.setContent(titleResId, drawableResId, text);
			return this;
		}
		
		public Builder setContent(int titleResId, int resId) {
			mProductDialog.setContent(titleResId, resId);
			return this;
		}
		
		public Builder setContent(int titleResId, CharSequence text) {
			mProductDialog.setContent(titleResId, text);
			return this;
		}
		
		public Builder setCancelable(OnCancelListener listener) {
			mProductDialog.setCancelable(listener);
			return this;
		}
		
		public Builder setOnDismissListener(OnDismissListener listener) {
			mProductDialog.setOnDismissListener(listener);
			return this;
		}
		
		public Builder setPositiveButton(int resId, ProductDialogListener listener) {
			mProductDialog.setPositiveButton(resId, listener);
			return this;
		}
		
		public Builder setPositiveButton(CharSequence button, ProductDialogListener listener) {
			mProductDialog.setPositiveButton(button, listener);
			return this;
		}
		
		public Builder setNegativeButton(int resId, ProductDialogListener listener) {
			mProductDialog.setNegativeButton(resId, listener);
			return this;
		}
		
		public Builder setNegativeButton(CharSequence button, ProductDialogListener listener) {
			mProductDialog.setNegativeButton(button, listener);
			return this;
		}
		
		public Builder setNeutralButton(int resId, ProductDialogListener listener) {
			mProductDialog.setNeutralButton(resId, listener);
			return this;
		}
		
		public Builder setNeutralButton(CharSequence button, ProductDialogListener listener) {
			mProductDialog.setNeutralButton(button, listener);
			return this;
		}
		
		public Builder setOnCloseListener(ProductDialogListener listener) {
			mProductDialog.setOnCloseListener(listener);
			return this;
		}
		
		public ProductDialog create() {
			return mProductDialog;
		}
	}
	
	public static interface ProductDialogListener {
		public void onDialogClick(int id);
	}
	
}
