package com.gxq.tpm.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gxq.tpm.mode.strategy.AbstractStrategy;
import com.gxq.tpm.mode.strategy.ProductProfit;
import com.gxq.tpm.mode.strategy.ProductProfit.Profit;

import android.content.Context;

public abstract class StrategyProfitAdapter<T extends AbstractStrategy> extends ArrayListAdapter<T> {
	protected ProductProfit mProfit;
	public StrategyProfitAdapter(Context context) {
		super(context);
	}

	protected Profit findProfit(int id) {
		if (mProfit == null || mProfit.records == null) return null;
		
		for (Profit profit : mProfit.records) {
			if (profit.id == id) {
				return profit;
			}
		}
		return null;
	}
	
	public void assignProfit(ProductProfit productProfit) {
		mProfit = productProfit;
		
		List<T> records = new ArrayList<T>();
		for (T record : getList()) {
			Profit profit = findProfit(record.id);
			if (profit != null && !matchState(profit.state)) {
				records.add(record);
			}
		}
		if (records.size() > 0)
			getList().removeAll(records);
		notifyDataSetChanged();
	}
	
	protected boolean matchState(int state) {
		return true;
	}
	
}
