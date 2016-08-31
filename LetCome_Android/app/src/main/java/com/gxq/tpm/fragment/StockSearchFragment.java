package com.gxq.tpm.fragment;

import java.util.ArrayList;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.adapter.HomePolicyAdapter;
import com.gxq.tpm.adapter.StockSearchAdapter;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.Stock;
import com.gxq.tpm.mode.cooperation.ProductPolicyList;
import com.gxq.tpm.mode.cooperation.ProductPolicyList.Policy;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.tools.TextWatcherAdapter;
import com.gxq.tpm.tools.Util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StockSearchFragment extends FragmentBase implements View.OnClickListener, 
		/*OnStockSearchListener,*/ OnItemClickListener {
	private EditText mEtSearch;
	private ImageView mIvClean;
	private TextView mTvCancel, mTvStockTotal;
	private View /*mContainerSearchHistory,*/ mContainerSearchResult, mContainerStockPolicy;
	
//	private CStockSearchHistoryView mStockSearchHistory;
	private ListView mLvSearch;
	private ListView mLvPolicy;
	
//	private SearchStock mSearchStock;
	
//	private StockHistoryAdapter mStockHistoryAdapter;
	private StockSearchAdapter mStockSearchAdapter;
	private HomePolicyAdapter mHomePolicyAdapter;
	private Stock mStock;
	private ArrayList<Policy> mPolicies = new ArrayList<ProductPolicyList.Policy>();
	
	private int stock_total;
	private boolean mIsAutoComple = false;
//	private DispatcherTimer mHqTimer;
	
//	private boolean mShowCancel;
	
	public StockSearchFragment() {
		this(R.id.tab_strategy);
	}
	
	public StockSearchFragment(int markId) {
		super(markId);
		
//		mHqTimer = new DispatcherTimer(this, 3000);
//		mSearchStock = App.instance().getSearchStock();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stock_search, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (getArguments() != null) {
			stock_total = getArguments().getInt(ProductIntent.EXTRA_STOCK_TOTAL);
//			mShowCancel = getArguments().getBoolean(ProductIntent.EXTRA_SHOW_CANCEL);
		}
		
		mEtSearch = (EditText) view.findViewById(R.id.et_search);
		mEtSearch.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String text = mEtSearch.getText().toString();
				search(text);
			}
		});

		mIvClean = (ImageView) view.findViewById(R.id.iv_clean);
		mIvClean.setOnClickListener(this);
		
		mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
		mTvCancel.setOnClickListener(this);
//		mTvCancel.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
		
//		mContainerSearchHistory = view.findViewById(R.id.container_search_history);
//		mStockSearchHistory = (CStockSearchHistoryView) view.findViewById(R.id.stock_search_history);
//		mStockSearchHistory.setEmptyView(view.findViewById(R.id.tv_no_browse_history));
//		mStockSearchHistory.setOnItemClickListener(this);
//		mStockSearchHistory.setOnStockSearchListener(this);
//		
//		mStockHistoryAdapter = new StockHistoryAdapter(getActivity());
//		mStockHistoryAdapter.setList(mSearchStock.getStocks());
//		mStockSearchHistory.setAdapter(mStockHistoryAdapter);
		mTvStockTotal = (TextView) view.findViewById(R.id.tv_stock_total);
		if(0 != stock_total){
			mTvStockTotal.setVisibility(View.VISIBLE);
			String strSuccess = getString(R.string.cooperation_strategy_search_hint, stock_total+"");
			int start = strSuccess.indexOf(stock_total+"");
			int end = start + (stock_total+"").length();
			mTvStockTotal.setText(Util.strChangeColor(strSuccess, start, end, Util.transformColor(R.color.color_ff7814)));
		}else{
			mTvStockTotal.setVisibility(View.GONE);
		}
		
		mContainerSearchResult = view.findViewById(R.id.contaienr_search_result);
		mLvSearch = (ListView) view.findViewById(R.id.listview);
		mLvSearch.setEmptyView(view.findViewById(R.id.tv_search_empty));
		mLvSearch.setOnItemClickListener(this);
		
		mStockSearchAdapter = new StockSearchAdapter(getActivity());
		mLvSearch.setAdapter(mStockSearchAdapter);
		
		mContainerStockPolicy = view.findViewById(R.id.container_stock_policy);
		mLvPolicy = (ListView) view.findViewById(R.id.listview_policy);
		mLvPolicy.setOnItemClickListener(this);
		
		mHomePolicyAdapter = new HomePolicyAdapter(getActivity());
		mLvPolicy.setAdapter(mHomePolicyAdapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (isHidden()) return;
		
//		mHqTimer.timerTaskControl(true);
//		search(mEtSearch.getText().toString());
	}

	@Override
	public void onPause() {
		super.onPause();
//		mHqTimer.timerTaskControl(false);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
//		mHqTimer.timerTaskControl(!hidden);
		
		if (!hidden) {
			search(mEtSearch.getText().toString());
		}
	}
	
	@Override
	public void onAlarmClock(int operationType) {
		super.onAlarmClock(operationType);
		
//		if (mSearchStock.getStocks().size() > 0) {
//			List<String> stockCodeList = new ArrayList<String>();
//			for (Stock stock : mSearchStock.getStocks()) {
//				stockCodeList.add(stock.getStockCode());
//			}
//			GetHQInfo.doRequest(Util.join(";", stockCodeList), this);
//		}
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		super.netFinishOk(info, res, tag);
		
//		if (info == RequestInfo.GET_HQ) {
//			GetHQInfo getHQInfo = (GetHQInfo) res;
//			mStockHistoryAdapter.assignHQ(getHQInfo.records);
//			mStockHistoryAdapter.notifyDataSetChanged();
//		}
		if(info == RequestInfo.PRODUCT_POLICY_LIST){
			ProductPolicyList productPolicyList = (ProductPolicyList) res;
			if(null != productPolicyList && null != productPolicyList.records && productPolicyList.records.size() > 0){
				mContainerSearchResult.setVisibility(View.GONE);
				mContainerStockPolicy.setVisibility(View.VISIBLE);
				assignPolicyList(productPolicyList);
			}else{
				Toast.makeText(mContext, "今天无投资人提供"+mStock.getStockName()+"TM策略", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
//		if(info == RequestInfo.PRODUCT_POLICY_LIST){
//			Toast.makeText(mContext, "今天无投资人提供"+mStock.getStockName()+"TM策略", Toast.LENGTH_SHORT).show();
//		}
		return super.netFinishError(info, what, msg, tag);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.iv_clean:
			mEtSearch.setText("");
			break;
		case R.id.tv_cancel:
			getActivity().finish();
			break;
		}
	}

//	@Override
//	public void onStockHistoryRemove(int index) {
//		mSearchStock.removeStock(index);
//		mStockHistoryAdapter.notifyDataSetChanged();
//	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		/*if (parent == mStockSearchHistory) {
			if (position == mStockHistoryAdapter.getCount() - 1) {
				mSearchStock.clearAll();
				App.instance().saveSearchStock();
				
				mStockHistoryAdapter.notifyDataSetChanged();
				search(null);
			} else {
				Stock stock = mStockHistoryAdapter.getList().get(position);
				if (stock != null) {
					mSearchStock.addStock(stock);
					App.instance().saveSearchStock();
					
					startActivity(stock);
				}
			}
		} else */
		if (parent == mLvSearch){
			Stock stock = mStockSearchAdapter.getList().get(position);
			if (stock != null) {
//				if (mSearchStock.addStock(stock)) {
//					App.instance().saveSearchStock();
//				}
//				startActivity(stock);
				mStock = stock;
				requestPolicyList(stock.getStockCode(), 0);
			}
		} else if(parent == mLvPolicy){
//			PolicyItem policyItem = mHomePolicyAdapter.getList().get(position);
			//nextstep
		}
	}
	
	private void requestPolicyList(String stock_code, int start_id){
		ProductPolicyList.Params params = new ProductPolicyList.Params();
		params.stock_code = stock_code;
		params.start_id = start_id;
		ProductPolicyList.doRequest(params, StockSearchFragment.this);
	}
	
	private void assignPolicyList(ProductPolicyList productPolicyList){
		mPolicies.clear();
		for (int i = 0; i < productPolicyList.records.size(); i++) {
			mPolicies.addAll(productPolicyList.records.get(i).policies);
		}
		mHomePolicyAdapter.setPolicyItemList(productPolicyList.records);
		mHomePolicyAdapter.setList(mPolicies);
		mIsAutoComple = true;
		mEtSearch.setText(mPolicies.get(0).stock_code);
		Selection.setSelection(mEtSearch.getText(), mEtSearch.getText().length());
	}
	
//	private void startActivity(Stock stock) {
//		Intent intent = new Intent();
////		if (mShowCancel) {
////			intent = new Intent(mContext, StrategyCooperationActivity.class);
////			intent.putExtra(ProductIntent.EXTRA_PRODUCT, mProduct);
////			intent.putExtra(ProductIntent.EXTRA_STOCK, stock);
////			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////			mContext.startActivity(intent);
////		} else {
////			intent = new Intent(mContext, StrategyCooperationActivity.class);
////			intent.putExtra(ProductIntent.EXTRA_PRODUCT, mProduct);
////			intent.putExtra(ProductIntent.EXTRA_STOCK, stock);
////			mContext.startActivity(intent);
////		}
//		intent.putExtra(ProductIntent.EXTRA_STOCK, stock);
//		if(getArguments().getString(ProductIntent.EXTRA_ACTIVITY_FROM,"").equals("from_choose")){
//			getActivity().setResult(Activity.RESULT_OK, intent);
//			getActivity().finish();
//		}else{
//			intent.setClass(mContext, StockChooseActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			mContext.startActivity(intent);
//		}
//		
//	}

	private void search(String value) {
		if(mIsAutoComple){
			mIsAutoComple = false;
			return;
		}
		mContainerStockPolicy.setVisibility(View.GONE);
		if (value == null || value.length() == 0) {
			mIvClean.setVisibility(View.GONE);
			
//			mContainerSearchHistory.setVisibility(View.VISIBLE);
//			mStockHistoryAdapter.notifyDataSetChanged();
			
			mContainerSearchResult.setVisibility(View.GONE);
			
			mTvStockTotal.setVisibility(View.VISIBLE);
		} else {
			mIvClean.setVisibility(View.VISIBLE);

//			mContainerSearchHistory.setVisibility(View.GONE);
			mContainerSearchResult.setVisibility(View.VISIBLE);
			
			mStockSearchAdapter.queryText(value);
			
			mTvStockTotal.setVisibility(View.GONE);
		}
	}

}
