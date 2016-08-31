package com.gxq.tpm.ui.chart;

import java.util.ArrayList;
import java.util.List;

public class ListChartData<T> implements IChartData<T> {
	private List<T> datas;

	public ListChartData() {
		datas = new ArrayList<T>();
	}

	public ListChartData(List<T> data) {
		datas = new ArrayList<T>();
		datas.addAll(data);
	}

	@Override
	public int size() {
		return datas.size();
	}

	@Override
	public T get(int index) {
		return datas.get(index);
	}

	@Override
	public boolean hasData() {
		return datas != null && !datas.isEmpty();
	}

	@Override
	public void add(T data) {
		if (null == datas || datas.isEmpty()) {
			datas = new ArrayList<T>();
		}
		datas.add(data);
	}

	@Override
	public void clear() {
		if (hasData()) {
			datas.clear();
		}
	}
	
}
