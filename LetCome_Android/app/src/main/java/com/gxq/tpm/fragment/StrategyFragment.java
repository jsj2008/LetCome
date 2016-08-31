package com.gxq.tpm.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.ArrayListAdapter;
import com.gxq.tpm.adapter.CViewPagerAdapter;
import com.gxq.tpm.adapter.StrategyBuyingAdapter;
import com.gxq.tpm.adapter.StrategyNoticingAdapter;
import com.gxq.tpm.adapter.StrategyProfitAdapter;
import com.gxq.tpm.adapter.StrategySellingAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.BaseRes.QueryParams;
import com.gxq.tpm.mode.strategy.AbstractStrategy;
import com.gxq.tpm.mode.strategy.ProductProfit;
import com.gxq.tpm.mode.strategy.StrategyBuyingOrders;
import com.gxq.tpm.mode.strategy.StrategyNoticingOrders;
import com.gxq.tpm.mode.strategy.StrategyBuyingOrders.StrategyBuying;
import com.gxq.tpm.mode.strategy.StrategyNoticingOrders.StrategyNoticing;
import com.gxq.tpm.mode.strategy.StrategySellingOrders;
import com.gxq.tpm.mode.strategy.StrategySellingOrders.StrategySelling;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.CListViewAdapterHelper;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.ui.CEmptyNoteView;
import com.gxq.tpm.ui.CListView;
import com.gxq.tpm.ui.CTabTitleSelector;

public class StrategyFragment extends FragmentBase {
	private final static int TAG_BUYING 	= 1;
	private final static int TAG_NOTICING 	= 2;
	private final static int TAG_SELLING 	= 3;
	
	private final static int COUNT = 3;
	
	private CTabTitleSelector mTabTitle;
	private ViewPager mViewPager;
	private CViewPagerAdapter mAdapter;
	
	private StrategyOrderFragment mBuyingFragment, mNoticingFragment, mSellingFragment;
	private boolean mBuyingCreated, mNoticingCreated, mSellingCreated;
	private boolean mGotoAccount;
	
	public StrategyFragment() {
		this(R.id.tab_strategy);
	}
	
	public StrategyFragment(int markId) {
		super(markId);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mBuyingFragment = new StrategyOrderFragment(getActivity(), container, TAG_BUYING);
		mNoticingFragment = new StrategyOrderFragment(getActivity(), container, TAG_NOTICING);
		mSellingFragment = new StrategyOrderFragment(getActivity(), container, TAG_SELLING);
		
		return inflater.inflate(R.layout.fragment_strategy, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initView(view);
		initAction(view);
	}
	
	private void initView(View view) {
		mTabTitle = (CTabTitleSelector) view.findViewById(R.id.tab_title);
		mTabTitle.setLayoutId(R.layout.strategy_tab_title);
		mTabTitle.setSeparateLayoutId(R.layout.tab_title_vertical_sep);
		
		mTabTitle.newTabTitle(R.string.strategy_buying);
		mTabTitle.newTabTitle(R.string.strategy_noticing);
		mTabTitle.newTabTitle(R.string.strategy_selling);
		
		mViewPager = (ViewPager) view.findViewById(R.id.strategy_viewpager);
		
		List<ViewPagerFragment> fragments = new ArrayList<ViewPagerFragment>();
		fragments.add(mBuyingFragment);
		fragments.add(mNoticingFragment);
		fragments.add(mSellingFragment);
		mAdapter = new CViewPagerAdapter(fragments);
		mViewPager.setAdapter(mAdapter);
		
		setSelection(0);
		showWaitDialog(null);
	}
	
	private void initAction(View view) {
		mTabTitle.setOnTabTitleSelectListener(new CTabTitleSelector.OnTabTitleSelectListener() {
			@Override
			public void onSelection(int position) {
				setSelection(position);
			}
		});
		mTabTitle.setViewPager(mViewPager);
	}	
	
	private void setSelection(int position) {
		mTabTitle.setPosition(position);
		mViewPager.setCurrentItem(position);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (isHidden()) {
			return;
		}

		if (getArguments() != null) {
			int refresh = getArguments().getInt(ProductIntent.EXTRA_REFRESH);
			if (refresh >= 0) {
				switch (refresh) {
				case ProductIntent.REFRESH_NEW_BUYING:
					showWaitDialog(RequestInfo.PRODUCT_BUYING);
					setSelection(0);
					requestStrategyBuying(0, COUNT);
					break;
				case ProductIntent.REFRESH_BUYING:
					showWaitDialog(RequestInfo.PRODUCT_BUYING);
					refreshFragment(TAG_BUYING);
					break;
				case ProductIntent.REFRESH_NEW_NOTICING:
					showWaitDialog(RequestInfo.PRODUCT_TODO);
					setSelection(1);
					refreshFragment(TAG_BUYING);
					requestStrategyNoticing(0, COUNT);
					break;
				case ProductIntent.REFRESH_NEW_SELLING:
					showWaitDialog(RequestInfo.PRODUCT_SELLING);
					setSelection(2);
					refreshFragment(TAG_NOTICING);
					requestStrategySelling(0, COUNT);
					break;
				case ProductIntent.REFRESH_SELLING:
					showWaitDialog(RequestInfo.PRODUCT_SELLING);
					refreshFragment(TAG_SELLING);
					break;
				case ProductIntent.REFRESH_NEW_ACCOUNT:
					mGotoAccount = true;
					
					showWaitDialog(RequestInfo.PRODUCT_SELLING);
					refreshFragment(TAG_SELLING);
					
					break;
				}
			}
			getArguments().remove(ProductIntent.EXTRA_REFRESH);
		}
		mAdapter.onResume();
	}
	
	private void refreshFragment(int fragment) {
		if (fragment == TAG_BUYING) {
			if (mBuyingCreated) {
				requestStrategyBuying(0, mBuyingFragment.mAdapter.getCount());
			} else {
				requestStrategyBuying(0, COUNT);
			}
		} else if (fragment == TAG_NOTICING) {
			if (mNoticingCreated) {
				requestStrategyNoticing(0, mNoticingFragment.mAdapter.getCount());
			} else {
				requestStrategyNoticing(0, COUNT);
			}
		} else if (fragment == TAG_SELLING) {
			if (mSellingCreated) {
				requestStrategySelling(0, mSellingFragment.mAdapter.getCount());
			} else {
				requestStrategySelling(0, COUNT);
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	
		mAdapter.onPause();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if (!hidden) {
			mAdapter.onShow();
		} else {
			mAdapter.onHide();
		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (info == RequestInfo.PRODUCT_BUYING) {
			mBuyingFragment.netFinishOK(info, res);
			checkTitle(true, false, false);
		} else if (info == RequestInfo.PRODUCT_TODO) {
			mNoticingFragment.netFinishOK(info, res);
			checkTitle(false, true, false);
		} else if (info == RequestInfo.PRODUCT_SELLING) {
			mSellingFragment.netFinishOK(info, res);
			checkTitle(false, false, true);
			
			gotoAccount();
		} else if (info == RequestInfo.PRODUCT_PROFIT) {
			if (tag == TAG_BUYING) {
				mBuyingFragment.netFinishOK(info, res);
			} else if (tag == TAG_NOTICING) {
				mNoticingFragment.netFinishOK(info, res);
			} else if (tag == TAG_SELLING) {
				mSellingFragment.netFinishOK(info, res);
			}
		}
	}

	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (info == RequestInfo.PRODUCT_BUYING) {
			mBuyingFragment.netFinishError(info);
			checkTitle(true, false, false);
		} else if (info == RequestInfo.PRODUCT_TODO) {
			mNoticingFragment.netFinishError(info);
			checkTitle(false, true, false);
		} else if (info == RequestInfo.PRODUCT_SELLING) {
			mSellingFragment.netFinishError(info);
			checkTitle(false, false, true);
			
			gotoAccount();
		}
		return super.netFinishError(info, what, msg, tag);
	}
	
	private void checkTitle(boolean buyingCreated, boolean noticingCreated, boolean sellingCreated) {
		if (buyingCreated) {
			if (mBuyingCreated) return;
			mBuyingCreated = buyingCreated;
		}
		if (noticingCreated) {
			if (mNoticingCreated) return;
			mNoticingCreated = noticingCreated;
		}
		
		if (sellingCreated) {
			if (mSellingCreated) return;
			mSellingCreated = sellingCreated;
		}
		if (mBuyingCreated && mNoticingCreated && mSellingCreated) {
			hideWaitDialog(null);
			if (mBuyingFragment.mAdapter.getCount() > 0) {
				setSelection(0);
			} else if (mNoticingFragment.mAdapter.getCount() > 0) {
				setSelection(1);
			} else if (mSellingFragment.mAdapter.getCount() > 0) {
				setSelection(2);
			} else {
				setSelection(0);
			}
		}
	}
	
	private void gotoAccount() {
		if (mGotoAccount) {
			mGotoAccount = false;

			Bundle bundle = new Bundle();
			bundle.putBoolean(ProductIntent.EXTRA_REFRESH, true);
			((SuperActivity) getActivity()).changeFragment(R.id.tab_account, bundle);
		}
	}
	
	private void requestStrategy(int id, int count, int tag) {
		if (tag == TAG_BUYING) {
			requestStrategyBuying(id, count);
		} else if (tag == TAG_NOTICING) {
			requestStrategyNoticing(id, count);
		} else if (tag == TAG_SELLING) {
			requestStrategySelling(id, count);
		}
	}
	
	private void requestStrategyBuying(int id, int count) {
		QueryParams params = new QueryParams();
		params.start_id = id;;
		params.limit = count;
		StrategyBuyingOrders.doRequest(params, this);
	}
	
	private void requestStrategyNoticing(int id, int count) {
		QueryParams params = new QueryParams();
		params.start_id = id;;
		params.limit = count;
		StrategyNoticingOrders.doRequest(params, this);
	}
	
	private void requestStrategySelling(int id, int count) {
		QueryParams params = new QueryParams();
		params.start_id = id;;
		params.limit = count;
		StrategySellingOrders.doRequest(params, this);
	}
	
	private void requestProfit(String id, int tag) {
		ProductProfit.Params params = new ProductProfit.Params();
		params.p_id_arr = id;
		ProductProfit.doRequest(params, StrategyFragment.this, tag);
	}
	
	private class StrategyOrderFragment extends ViewPagerFragment implements
			View.OnClickListener, CEmptyNoteView.OnRefreshListener {
		private View mContainerList;
		private CEmptyNoteView mContainerEmpty;
		private CListView mListView;
		
		private CListViewAdapterHelper<? extends AbstractStrategy> mAdapterHelper;
		private ArrayListAdapter<? extends AbstractStrategy> mAdapter;
		private int mTag;
		
		private DispatcherTimer mProfitTimer;

		public StrategyOrderFragment(Context context, View container, int tag) {
			super(context, container);
			this.mTag = tag;
			
			mProfitTimer = new DispatcherTimer(this, 3);
		}

		@Override
		protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
			return inflater.inflate(R.layout.fragment_strategy_tab, container, false);
		}
		
		@Override
		protected void onViewCreated(View view) {
			super.onViewCreated(view);
			mContainerList = view.findViewById(R.id.container_list);
			mListView = (CListView) view.findViewById(R.id.strategy_list);
			
			if (mTag == TAG_BUYING) {
				mAdapterHelper = new StrategyBuyingAdapterHelper(StrategyFragment.this, mListView);
			} else if (mTag == TAG_NOTICING) {
				mAdapterHelper = new StrategyNoticingAdapterHelper(StrategyFragment.this, mListView);
			} else if (mTag == TAG_SELLING) {
				mAdapterHelper = new StrategySellingAdapterHelper(StrategyFragment.this, mListView);
			}
			mAdapter = mAdapterHelper.getAdapter();
			
			mContainerEmpty = (CEmptyNoteView) view.findViewById(R.id.list_empty_container);
			mContainerEmpty.setRefreshEnable(true);
			mContainerEmpty.setOnRefreshListener(this);
			
			mContainerEmpty.setOnClickListener(this);
		}
		
		@Override
		protected void request() {
			super.request();
			
			requestStrategy(0, COUNT, mTag);
		}
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.tv_next_step) {
				((SuperActivity) getActivity()).changeFragment(R.id.tab_cooperation, null);
			}
		}
		
		@Override
		public void onRefresh() {
			requestStrategy(0, COUNT, mTag);
		}
		
		@Override
		protected void timerControl(boolean start) {
			mProfitTimer.timerTaskControl(start);
		}
		
		@Override
		public void onAlarmClock(int type) {
			super.onAlarmClock(type);
			
			if (mAdapter.getCount() == 0)
				return ;
			
			int[] array = new int[mAdapter.getCount()];
			for (int index = 0; index < mAdapter.getCount(); index++) {
				array[index] = mAdapter.getList().get(index).id;
			}
			
			requestProfit(Util.join(",", array), mTag);
		}

		public void netFinishOK(RequestInfo info, BaseRes res) {
			if (info == RequestInfo.PRODUCT_BUYING || 
					info == RequestInfo.PRODUCT_TODO ||
					info == RequestInfo.PRODUCT_SELLING) {
				if (info == RequestInfo.PRODUCT_BUYING) {
					StrategyBuyingOrders orders = (StrategyBuyingOrders) res;
					((StrategyBuyingAdapterHelper)mAdapterHelper).assignList(orders.records);
				} else if (info == RequestInfo.PRODUCT_TODO) {
					StrategyNoticingOrders orders = (StrategyNoticingOrders) res;
					((StrategyNoticingAdapterHelper)mAdapterHelper).assignList(orders.records);
				} else if (info == RequestInfo.PRODUCT_SELLING) {
					StrategySellingOrders orders = (StrategySellingOrders) res;
					((StrategySellingAdapterHelper)mAdapterHelper).assignList(orders.records);
				}
				
				checkList();
				
				if (mShow && mResume) {
					timerControl(true);
				}
			} else if (info == RequestInfo.PRODUCT_PROFIT) {
				ProductProfit profit = (ProductProfit) res;
				((StrategyProfitAdapter<?>)mAdapter).assignProfit(profit);
				
				checkList();
			}
		}
		
		public void netFinishError(RequestInfo info) {
			mAdapterHelper.finish();
			checkList();
		}
		
		private void checkList() {
			mContainerEmpty.onRefreshComplete();
			if (mAdapter.getCount() == 0) {
				mContainerList.setVisibility(View.GONE);
				mContainerEmpty.setVisibility(View.VISIBLE);
			} else {
				mContainerList.setVisibility(View.VISIBLE);
				mContainerEmpty.setVisibility(View.GONE);
			}
		}
	}
	
	private class StrategyBuyingAdapterHelper extends CListViewAdapterHelper<StrategyBuying> {

		public StrategyBuyingAdapterHelper(ICallBack callBack, CListView listView) {
			super(callBack, listView);
		}

		@Override
		protected ArrayListAdapter<StrategyBuying> getListViewAdapter() {
			return new StrategyBuyingAdapter(getActivity());
		}

		@Override
		protected void requestList() {
			requestStrategyBuying(0, COUNT);
		}

		@Override
		protected void requestMore(String startId) {
			requestStrategyBuying(Integer.parseInt(startId), COUNT);
		}

		@Override
		protected String getLastId() {
			if (mContents.size() > 0) {
				return Long.toString(mContents.get(mContents.size() - 1).id);
			}
			return "0";
		}
	}
	
	private class StrategyNoticingAdapterHelper extends CListViewAdapterHelper<StrategyNoticing> {

		public StrategyNoticingAdapterHelper(ICallBack callBack, CListView listView) {
			super(callBack, listView);
		}

		@Override
		protected ArrayListAdapter<StrategyNoticing> getListViewAdapter() {
			return new StrategyNoticingAdapter(getActivity());
		}

		@Override
		protected void requestList() {
			requestStrategyNoticing(0, COUNT);
		}

		@Override
		protected void requestMore(String startId) {
			requestStrategyNoticing(Integer.parseInt(startId), COUNT);
		}

		@Override
		protected String getLastId() {
			if (mContents.size() > 0) {
				return Long.toString(mContents.get(mContents.size() - 1).id);
			}
			return "0";
		}
	}
	
	private class StrategySellingAdapterHelper extends CListViewAdapterHelper<StrategySelling> {

		public StrategySellingAdapterHelper(ICallBack callBack, CListView listView) {
			super(callBack, listView);
		}

		@Override
		protected ArrayListAdapter<StrategySelling> getListViewAdapter() {
			return new StrategySellingAdapter(getActivity());
		}

		@Override
		protected void requestList() {
			requestStrategySelling(0, COUNT);
		}

		@Override
		protected void requestMore(String startId) {
			requestStrategySelling(Integer.parseInt(startId), COUNT);
		}

		@Override
		protected String getLastId() {
			if (mContents.size() > 0) {
				return Long.toString(mContents.get(mContents.size() - 1).id);
			}
			return "0";
		}
	}
	
}
