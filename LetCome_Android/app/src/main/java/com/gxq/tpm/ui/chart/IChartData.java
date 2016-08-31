package com.gxq.tpm.ui.chart;

public interface IChartData<T> {
	int size();

	T get(int index);

	boolean hasData();

	void add(T data);

	void clear();
}
