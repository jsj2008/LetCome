package com.gxq.tpm.ui;

import java.util.ArrayList;
import java.util.List;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SignAgreementActivity;
import com.gxq.tpm.mode.AgreementBase;
import com.gxq.tpm.mode.SignAgreement;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AgreementSignContainer extends LinearLayout implements View.OnClickListener {
	private LinearLayout mContainer;
	private TextView mButton;
	
	private List<AgreementBase> mAgreementList = new ArrayList<AgreementBase>();
	private int mCheckedSize;
	
	private OnAgreementSignedListener mListener;
	private Handler mHandler = new Handler();
	
	public AgreementSignContainer(Context context) {
		this(context, null);
	}
	
	public AgreementSignContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initUI(context);
	}

	private void initUI(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		
		View view = inflater.inflate(R.layout.multi_agreement_dialog, this);
		mContainer = (LinearLayout) view.findViewById(R.id.container);
		
		mButton = (TextView) view.findViewById(R.id.confirm);
		mButton.setOnClickListener(this);
		
//		checkButtonStatus();
	}
	
	private void checkButtonStatus() {
		if (mCheckedSize == mAgreementList.size()) {
			mButton.setTextColor(Util.transformColor(R.color.text_color_dialog_btn));
			mButton.setEnabled(true);
		} else {
			mButton.setTextColor(Util.transformColor(R.color.text_color_info));
			mButton.setEnabled(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (mCheckedSize == mAgreementList.size()) {
			SignAgreement.Params params = new SignAgreement.Params();
//			params.id = /*mAgreement.id+""*/ids;
			List<String> ids = new ArrayList<String>();
			for (AgreementBase agreement : mAgreementList) {
				ids.add(agreement.id);
			}
			params.id = Util.join(",", ids);
			SignAgreement.doRequest(params, null);
			
			mButton.setTextColor(Util.transformColor(R.color.text_color_info));
			mButton.setEnabled(false);
			
			if (mListener != null) {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mListener.onAgreementSigned();
					}
				}, 2000);
			}
		}
	}
	
	public void addAgreement(AgreementBase agreement) {
		mAgreementList.add(agreement);
		
		LayoutInflater inflater = LayoutInflater.from(getContext());
		CCheckBox checkBox = (CCheckBox) inflater.inflate(R.layout.multi_agreement_dialog_item, this, false);
		
		AgreementListener listener = new AgreementListener(agreement);
		checkBox.setRightText(agreement.name, listener);
		checkBox.setCheckListener(listener);
		
		mContainer.addView(checkBox);
	}
	
	public void setOnAgreementSignedListener(OnAgreementSignedListener listener) {
		mListener = listener;
	}

	private class AgreementListener implements View.OnClickListener, CCheckBox.OnCheckListener {
		AgreementBase agreement;
		
		public AgreementListener(AgreementBase agreement) {
			this.agreement = agreement;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), SignAgreementActivity.class);
			intent.putExtra(ProductIntent.EXTRA_UNSIGN_AGREEMENT, agreement);
			getContext().startActivity(intent);
		}

		@Override
		public void checkListener(boolean isChecked) {
			if (isChecked) {
				mCheckedSize++;
			} else {
				mCheckedSize--;
			}
			checkButtonStatus();
		}
	}
	
	public static interface OnAgreementSignedListener {
		public void onAgreementSigned();
	}
	
}
