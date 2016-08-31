package com.gxq.tpm.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.BaseActivity;
import com.gxq.tpm.activity.WebActivity;
import com.gxq.tpm.activity.BaseActivity.LoginCallback;
import com.gxq.tpm.activity.cooperation.StockSearchActivity;
import com.gxq.tpm.adapter.ArrayListAdapter;
import com.gxq.tpm.adapter.HomePolicyAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.cooperation.AdInfo;
import com.gxq.tpm.mode.cooperation.PopupMsg;
import com.gxq.tpm.mode.cooperation.ProductPolicyList;
import com.gxq.tpm.mode.cooperation.ProductPolicyList.Policy;
import com.gxq.tpm.mode.cooperation.ReadMsg;
import com.gxq.tpm.mode.hq.GetHQInfo;
import com.gxq.tpm.mode.hq.GetHQInfo.HQInfo;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.CListViewAdapterHelper;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.NumberFormat;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.cache.AsyncImageLoaderProxy;
import com.gxq.tpm.tools.cache.AsyncImageLoaderProxy.ImageCallback;
import com.gxq.tpm.ui.CListView;
import com.gxq.tpm.ui.ImageCycleView;
import com.gxq.tpm.ui.ProductDialog;
import com.gxq.tpm.ui.ImageCycleView.ImageCycleViewListener;

public class HomeFragment extends FragmentBase implements View.OnClickListener {

	private static final String HQ_CODE_SZZS 	= "000001";
	private static final String HQ_CODE_SZCZ 	= "399001";
	private static final String HQ_CODE_CYB 	= "399006";
	
	private static final int NET_REQUEST_HQ = 0;
	private static final int NET_REQUEST_POPMSG = 1;
	
	private static final int ANIMATION_DOWM = 1;
	private static final int ANIMATION_UP = 2;
	
	private DispatcherTimer mHQTimer;
	private DispatcherTimer mPopMsgTimer;
	
	private ImageCycleView mAdView;
	private RelativeLayout mAdLayout;
		
	private TextView mTvSearch;
	private ImageView mIvListdown;
	
	private TextView mTvHq1, mTvDiff1, mTvRatio1, 
					mTvHq2, mTvDiff2, mTvRatio2, 
					mTvHq3, mTvDiff3, mTvRatio3; 
	
	private CListView mListView;
	private HomePolicyAdapter mAdapter;
	private PolicyAdapterHelper mAdapterHelper;
	private int mCurrentIndex = 0;
	private ProductPolicyList mPolicyList;
	private int mCurrentAnimal = -1;
		
	private String [] mPoolInfo = {HQ_CODE_CYB, HQ_CODE_SZCZ};
	
	private ProductDialog mSmallDialog;
	private Dialog mBigDialog;

	public HomeFragment() {
		this(R.id.tab_cooperation);
	}

	public HomeFragment(int markId) {
		super(markId);

		mHQTimer = initNewTimer(3, NET_REQUEST_HQ);
		mPopMsgTimer = initNewTimer(5 * 60 , NET_REQUEST_POPMSG);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		initAction(view);
	}

	private void initView(View view) {

		// 广告
		mAdView = (ImageCycleView) view.findViewById(R.id.image_cycle_view);
		
		mAdLayout = (RelativeLayout)view.findViewById(R.id.rl_stock_ad);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Util.getScreenWidth(getActivity())*144/360);
//		mAdLayout.setLayoutParams(params);

		mTvSearch = (TextView)view.findViewById(R.id.tv_search);
		mIvListdown = (ImageView) view.findViewById(R.id.iv_listdown);

		mTvHq1  = (TextView) view.findViewById(R.id.tv_hq_1);
		mTvDiff1  = (TextView) view.findViewById(R.id.tv_diff_1);
		mTvRatio1  = (TextView) view.findViewById(R.id.tv_ratio_1); 
		mTvHq2  = (TextView) view.findViewById(R.id.tv_hq_2);
		mTvDiff2  = (TextView) view.findViewById(R.id.tv_diff_2);
		mTvRatio2  = (TextView) view.findViewById(R.id.tv_ratio_2); 
		mTvHq3  = (TextView) view.findViewById(R.id.tv_hq_3);
		mTvDiff3  = (TextView) view.findViewById(R.id.tv_diff_3);
		mTvRatio3  = (TextView) view.findViewById(R.id.tv_ratio_3); 
		
		mListView = (CListView) view.findViewById(R.id.list);
		mAdapterHelper = new PolicyAdapterHelper(HomeFragment.this, mListView);
		mAdapter = (HomePolicyAdapter) mAdapterHelper.getAdapter();
		setListViewHeightBasedOnChildren(mListView);
	}

	private void initAction(View view) {		
		mTvSearch.setOnClickListener(HomeFragment.this);
		
		mIvListdown.setOnClickListener(HomeFragment.this);
	}

	@Override
	public void onResume() {
		super.onResume();

		if (isHidden()) {
			return;
		}
		
		mCurrentAnimal = -1;
		
		requestData();
		
		mHQTimer.timerTaskControl(true);
		mPopMsgTimer.timerTaskControl(true);
	}

	@Override
	public void onPause() {
		super.onPause();

		mHQTimer.timerTaskControl(false);
		mPopMsgTimer.timerTaskControl(false);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			requestData();

			mHQTimer.timerTaskControl(true);
			mPopMsgTimer.timerTaskControl(true);
		} else {
			mHQTimer.timerTaskControl(false);
			mPopMsgTimer.timerTaskControl(false);
		}
	}

	private void requestData() {
		requestAd(); // 查询广告
		requestPolicyList(0);
	}

	private void requestAd() {
		AdInfo.Params params = new AdInfo.Params();
		params.type = "homepage_ad";
		AdInfo.doRequest(params, HomeFragment.this);
	}
	
	private void requestPolicyList(int start_id){
		mCurrentIndex = 0;
		ProductPolicyList.Params params = new ProductPolicyList.Params();
		params.stock_code = "0";
		params.start_id = start_id;
		params.limit = 1;
		ProductPolicyList.doRequest(params, HomeFragment.this);
	}

	@Override
	public void onAlarmClock(int operationType) {
		switch (operationType) {
		case NET_REQUEST_HQ:
			String mInfo = "";
			for (String info : mPoolInfo) {
				mInfo += info +";";
			}
			GetHQInfo.doRequest(mInfo, HomeFragment.this);
			GetHandicapInfo.doRequestSH(HQ_CODE_SZZS, HomeFragment.this);
			break;
		case NET_REQUEST_POPMSG:
			if (App.getUserPrefs().hasUserLogin()) {
				PopupMsg.doRequest(HomeFragment.this);
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_search:
			Intent intent = new Intent(getActivity(),StockSearchActivity.class);
			if(null != mPolicyList){
				intent.putExtra(ProductIntent.EXTRA_STOCK_TOTAL, mPolicyList.stock_total);
			}
			startActivity(intent);
			break;
		case R.id.iv_listdown:
			if(null != mPolicyList && null != mPolicyList.records){
				mCurrentAnimal = ANIMATION_UP;
				if(mCurrentIndex == mPolicyList.records.size() - 1){
					requestPolicyList(0);
				}else if(mCurrentIndex < mPolicyList.records.size()){
					mCurrentIndex ++;
					assignPolicyList(mCurrentIndex);
				}
			}else{
				requestPolicyList(0);
			}
			
			break;
		}
	}

	private void assignHqInfo(GetHQInfo hqInfo){
		ArrayList<HQInfo> infos = hqInfo.records;
		for (int i = 0; i < infos.size(); i++) {
			HQInfo info = infos.get(i);
			float newPrice = info.New;
			float yClose = info.YClose;
			if (info.stockcode.equals(HQ_CODE_SZZS)) {
				assignHqInfo(newPrice, yClose, mTvHq1, mTvDiff1, mTvRatio1);
			} else if(info.stockcode.equals(HQ_CODE_CYB)) {
				assignHqInfo(newPrice, yClose, mTvHq3, mTvDiff3, mTvRatio3);
			} else if(info.stockcode.equals(HQ_CODE_SZCZ)) {
				assignHqInfo(newPrice, yClose, mTvHq2, mTvDiff2, mTvRatio2);
			}
		}
	}
	
	private void assignHqInfo(float newPrice, float yClose, TextView tvHq, 
			TextView tvDiff, TextView tvDiffRatio) {
		if (newPrice > 0) {
			float diff = newPrice - yClose;
			int color = Util.getColorByPlusMinus1(diff);
			
			tvHq.setText(NumberFormat.decimalFormat(newPrice));
			tvDiff.setText(NumberFormat.decimalFormatWithSymbol(diff));
			tvDiffRatio.setText(NumberFormat.percentWithSymbol(diff / yClose));
			
			tvHq.setTextColor(color);
			tvDiff.setTextColor(color);
			tvDiffRatio.setTextColor(color);
		} else {
			int color = Util.transformColor(R.color.text_color_sub);
			
			tvHq.setText(R.string.default_value);
			tvDiff.setText(R.string.default_value);
			tvDiffRatio.setText(R.string.default_value);
			
			tvHq.setTextColor(color);
			tvDiff.setTextColor(color);
			tvDiffRatio.setTextColor(color);
		}
	}
	
	private void assignPolicyList(int index){
		if(null == mPolicyList.records || mPolicyList.records.size() == 0){
			return;
		}
		mAdapterHelper.clearList();
		mAdapter.setPolicyItemList(mPolicyList.records);
		mAdapterHelper.assignList(mPolicyList.records.get(index).policies);
		if(mCurrentAnimal == ANIMATION_DOWM){
			animationDown();
		}else if(mCurrentAnimal == ANIMATION_UP){
			animationUp();
		}
	}
	
	private void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
				
		if (RequestInfo.GET_AD == info) {
			AdInfo adInfo = (AdInfo) result;
			List<AdInfo.Record> infoList = adInfo.records;
			if (null != infoList && infoList.size() > 0) {
				mAdLayout.setVisibility(View.VISIBLE);
				mAdView.setImageResources(infoList, mAdCycleViewListener);
			}
		} else if (RequestInfo.GET_HQ == info) {
			GetHQInfo hqInfo = (GetHQInfo) result;
			if(null != hqInfo){
				assignHqInfo(hqInfo);
			}
		} else if (RequestInfo.GET_HANDICAP == info) {
			GetHandicapInfo handicapInfo = (GetHandicapInfo) result;
			assignHqInfo(handicapInfo.New, handicapInfo.YClose, mTvHq1, mTvDiff1, mTvRatio1);
		} else if (RequestInfo.PRODUCT_POLICY_LIST == info){
			ProductPolicyList policyList = (ProductPolicyList) result;
			if(null != policyList){
				mTvSearch.setHint(getString(R.string.cooperation_strategy_search_hint, policyList.stock_total+""));
				
				mPolicyList = policyList;
				assignPolicyList(0);
			}
			setListViewHeightBasedOnChildren(mListView);
		}else if(RequestInfo.MSG_POPUP_MESSAGE == info){
			PopupMsg msg = (PopupMsg) result;
			if(null != mSmallDialog && mSmallDialog.isShowing()){
				return;
			}
			if(null != mBigDialog && mBigDialog.isShowing()){
				return;
			}
			if (msg.popup == 1){
				mSmallDialog = Util.showMsgDialg(msg, getActivity());
				readMsg(msg);
			} else if(msg.popup == 2){
				mBigDialog = Util.showMsgWindow(msg, getActivity());
				readMsg(msg);
			}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if(info == RequestInfo.PRODUCT_POLICY_LIST){
			mAdapterHelper.finish();
		}
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void readMsg(PopupMsg msg){
		ReadMsg.Params params = new ReadMsg.Params();
		params.msg_id = msg.id;
		ReadMsg.doRequest(params, HomeFragment.this);
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(AdInfo.Record record, int position,
				View imageView) {
			if(null == record.link || record.link.equals("")){
				return;
			}
			if(record.iscert == 1 && !App.getUserPrefs().hasUserLogin()){
				Toast.makeText(getActivity(), R.string.product_home_login_toast, Toast.LENGTH_SHORT).show();
				((BaseActivity) getActivity()).showLoginActivity(new LoginCallback() {
					@Override
					public void login() {
					}
					@Override
					public void cancel() {
					}
					@Override
					public boolean isStrategy() {
						return false;
					}
				});
			}else{
				if (record.link.contains(GlobalConstant.EXTERN_BROWSER)) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					Uri uri = Uri.parse(record.link);
			        intent.setData(uri);
			        startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(), WebActivity.class);
					if (null != record.title && !TextUtils.isEmpty(record.title)) {
						intent.putExtra(ProductIntent.EXTRA_TITLE, record.title);
					} else {
						intent.putExtra(ProductIntent.EXTRA_TITLE, "");
					}
					intent.putExtra(ProductIntent.EXTRA_URL, record.link);
					intent.putExtra(ProductIntent.EXTRA_NEED_SESSION, true);
					startActivity(intent);
				}
			}
			
		}

		@Override
		public void displayImage(String imageURL, final ImageView imageView) {
			AsyncImageLoaderProxy loader = new AsyncImageLoaderProxy(App.instance());
			loader.setCache2File(true);
			loader.downloadImage2(imageURL, true, new ImageCallback() {
					
				@Override
				public void onImageLoaded(Bitmap bitmap, String imageUrl) {
					if(bitmap == null){
						return;
					}
					imageView.setImageBitmap(bitmap);
				}
			});
		}

	};
	
	private void animationDown(){
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_down);
        mListView.startAnimation(animation);
	}
	
	private void animationUp(){ 
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_up);
        mListView.startAnimation(animation);
	}
	
	private class PolicyAdapterHelper extends CListViewAdapterHelper<Policy>{

		public PolicyAdapterHelper(ICallBack callBack, CListView listView) {
			super(callBack, listView);
			
		}

		@Override
		protected ArrayListAdapter<Policy> getListViewAdapter() {
			return new HomePolicyAdapter(mContext);
		}

		@Override
		protected void requestList() {
			mCurrentAnimal = ANIMATION_DOWM;
			if(mCurrentIndex == 0){
				requestPolicyList(0);
			}else if(mCurrentIndex > 0){
				mCurrentIndex --;
				assignPolicyList(mCurrentIndex);
			}
		}

		@Override
		protected void requestMore(String startId) {
			mCurrentAnimal = ANIMATION_UP;
			if(mCurrentIndex == mPolicyList.records.size() - 1){
				requestPolicyList(0);
			}else if(mCurrentIndex < mPolicyList.records.size()){
				mCurrentIndex ++;
				assignPolicyList(mCurrentIndex);
			}
		}

		@Override
		protected String getLastId() {
			if (mContents.size() > 0) {
//				return Integer.toString(mContents.get(mContents.size() - 1).id);
			}
			return "0";
		}
		
	}
	
}
