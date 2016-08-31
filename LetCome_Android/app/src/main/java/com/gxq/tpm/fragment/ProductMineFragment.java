package com.gxq.tpm.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.activity.mine.DissentRecordCompensateActivity;
import com.gxq.tpm.activity.mine.UserLgbAccountActivity;
import com.gxq.tpm.activity.mine.UserLetterActivity;
import com.gxq.tpm.activity.mine.UserServiceActivity;
import com.gxq.tpm.activity.mine.UserSettingsActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.GetTime;
import com.gxq.tpm.mode.NeedNotice;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.mine.ProdQuery;
import com.gxq.tpm.mode.mine.QualificationLevel;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.UserPrefs;
import com.gxq.tpm.tools.GlobalInfo;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CItemBar;
import com.gxq.tpm.ui.ProductDialog;

public class ProductMineFragment extends FragmentBase implements
		CItemBar.OnItemBarClickListener{
	private TextView mTvNickName, mTvUid;
	private ImageView mIvUserHead;
	
	private CItemBar mUserAuthItem;
	private CItemBar mUserDissentRecord_Comppensate;
	
	private CItemBar mLgbAccountItem;
	private CItemBar mWeixinItem;
	private CItemBar mLetterItem;
	
	private ProdQuery mProd;
	private NeedNotice mNeedNotice;
	
	private UserPrefs mUserPrefs;
	
	public ProductMineFragment() {
		this(R.id.tab_mine);
	}
	
	public ProductMineFragment(int markId) {
		super(markId);
		
		mUserPrefs = App.getUserPrefs();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_product_mine, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initUI(view);
	}
	
	private void initUI(View view) {
		mTvNickName = (TextView) view.findViewById(R.id.tv_nickname);
		mTvUid = (TextView) view.findViewById(R.id.tv_uid);
		
		mIvUserHead = (ImageView) view.findViewById(R.id.user_head_imageview);
		
		mUserAuthItem = (CItemBar) view.findViewById(R.id.user_auth);
		mUserAuthItem.setOnItemBarClickListener(this);
		
		mUserDissentRecord_Comppensate=(CItemBar) view.findViewById(R.id.user_dissentrecord_compensate);
		mUserDissentRecord_Comppensate.setOnItemBarClickListener(this);
		
		mLgbAccountItem = ((CItemBar) view.findViewById(R.id.user_lgb_account));
		mLgbAccountItem.setOnItemBarClickListener(this);
		
		mWeixinItem = ((CItemBar) view.findViewById(R.id.user_weixin));
		mWeixinItem.setOnItemBarClickListener(this);
		
		mLetterItem = ((CItemBar) view.findViewById(R.id.user_letter));
		mLetterItem.setTitle("@" + getString(R.string.user_letter));
		mLetterItem.setOnItemBarClickListener(this);
		
		((CItemBar) view.findViewById(R.id.user_service)).setOnItemBarClickListener(this);
		((CItemBar) view.findViewById(R.id.user_setting)).setOnItemBarClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (isHidden()) return;

		request();
		setUserInfo();
	}
	
	private void request() {
		requestUserAuth();
		
		requestProd();
		requestNeedNotice();
	}
	
	private void setUserInfo() {
		UserInfo userInfo = mUserPrefs.getUserInfo();
		if (userInfo != null) {
			mTvNickName.setText(userInfo.nick_name);
			mTvUid.setText(getString(R.string.user_uid, userInfo.uid));
			
			GlobalInfo.setNetworkImage(mIvUserHead, userInfo.pic, R.drawable.item_head90);
		} else {
			UserInfo.Params params = new UserInfo.Params();
			params.uid = mUserPrefs.getUid();
			
			UserInfo.doRequest(params, this);
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if (!hidden) {
			request();
			
			setUserInfo();
		}
	}
	
	private void requestUserAuth() {
		QualificationLevel.doRequest(this);
	}
	
	private void requestProd() {
		UserInfo info = mUserPrefs.getUserInfo();
		if (info != null && info.bind_cpb_tblid > 0) {
			ProdQuery.doRequest(null, this);
		}
	}
	
	private void requestNeedNotice() {
		NeedNotice.Params params = new NeedNotice.Params(); 
		params.type = NeedNotice.MSG;
		params.from_time = mUserPrefs.getMineMsgTime();
		NeedNotice.doRequest(params, this);
	}
	
	private void requestTime() {
		showWaitDialog(RequestInfo.GET_TIME);
		GetTime.doRequest(this);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		super.netFinishOk(info, result, tag);
		
		if (RequestInfo.QUALIFICATION_LEVEL == info) {
			QualificationLevel level = (QualificationLevel) result;
			mUserAuthItem.setContent(level.name);
		} else if (RequestInfo.PROD_QUERY == info) {
			ProdQuery prod = (ProdQuery) result;
			
			mProd = prod;
			String value = NumberFormat.moneySymbol(
					NumberFormat.decimalFormat("###,##0.00", prod.available));
			mLgbAccountItem.setContent(value);
		} else if (RequestInfo.MSG_NEED_NOTICE == info) {
			NeedNotice needNotice = (NeedNotice) result;
			mNeedNotice = needNotice;
			
			changeStatus();
		} else if (RequestInfo.GET_TIME == info) {
			GetTime time = (GetTime) result;
			mUserPrefs.setMineMsgTime(time.time);
			
			Intent intent = new Intent(getActivity(), UserLetterActivity.class);
			startActivity(intent);
		} else if (RequestInfo.USER_INFO == info) {
			UserInfo userInfo = (UserInfo) result;
			mUserPrefs.setUserInfo(userInfo);
			setUserInfo();
		}
	}
	
	private void changeStatus() {
		if (mNeedNotice != null && mNeedNotice.records != null) {
			if (mNeedNotice.records.get(NeedNotice.MSG) > 0) {
				mLetterItem.setContentDrawable(R.drawable.item_new);
			} else {
				mLetterItem.setContentDrawable(0);
			}
		}
	}
	
	@Override
	public void onItemClick(CItemBar v) {
		Intent intent = null;
		switch(v.getId()) {
		case R.id.user_auth:
			 Util.gotoAuth(getActivity());
		     break;
		case R.id.user_dissentrecord_compensate:
			intent = new Intent(getActivity(), DissentRecordCompensateActivity.class);
			 startActivity(intent);
		     break;
		case R.id.user_lgb_account:
			UserInfo info = mUserPrefs.getUserInfo();
			if (info != null) {
				intent = new Intent(getActivity(), UserLgbAccountActivity.class);
				intent.putExtra(ProductIntent.EXTRA_INSP_PROD, mProd);
				startActivity(intent);
			}
			break;
		case R.id.user_weixin:
			new ProductDialog.Builder(getActivity())
				.setContentText(R.string.user_weixin_dialog_content)
				.setPositiveButton(R.string.btn_cancel, null)
				.setNegativeButton(R.string.btn_open, new ProductDialog.ProductDialogListener() {
					@Override
					public void onDialogClick(int id) {
						openWeiXin();
					}
				})
				.create().show();
			break;
		case R.id.user_letter:
			requestTime();
			break;
		case R.id.user_service:
			intent = new Intent(getActivity(), UserServiceActivity.class);
			startActivity(intent);
			break;
		case R.id.user_setting:
			intent = new Intent(getActivity(), UserSettingsActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	private void openWeiXin() {
		ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
		String text = getString(R.string.user_weixin_public_number);
		cm.setPrimaryClip(ClipData.newPlainText(text, text));
		
		Intent intent = new Intent();
		ComponentName cmp = new ComponentName("com.tencent.mm",
				"com.tencent.mm.ui.LauncherUI");
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setComponent(cmp);
		
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			networkResultErr(getString(R.string.user_weixin_dialog_error_tips));
		}
	}

}
