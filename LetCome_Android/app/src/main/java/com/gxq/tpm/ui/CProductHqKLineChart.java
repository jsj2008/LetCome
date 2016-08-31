package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.hq.GetFifteenKLine;
import com.gxq.tpm.mode.hq.GetFiveKLine;
import com.gxq.tpm.mode.hq.GetHandicapInfo;
import com.gxq.tpm.mode.hq.GetKLine;
import com.gxq.tpm.mode.hq.GetMinuteInfo;
import com.gxq.tpm.mode.hq.GetSixtyKLine;
import com.gxq.tpm.mode.hq.GetThirtyKLine;
import com.gxq.tpm.network.NetworkResultInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.DispatcherTimer;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CProductHqKLineChart extends LinearLayout implements ICallBack, 
		CSliderView.OnSliderViewSelectListener, DispatcherTimer.OnDispatcherTimerListener{
	private final static int NET_REQUEST_MINUTE 	= 1;
	private final static int NET_REQUEST_5K_LINE 	= 2;
	private final static int NET_REQUEST_15K_LINE 	= 3;
	private final static int NET_REQUEST_30K_LINE 	= 4;
	private final static int NET_REQUEST_60K_LINE 	= 5;
	private final static int NET_REQUEST_DAY_K_LINE = 6;
	
	private CSliderView mSliderView;
	private int[] mTabHead = new int[] { R.string.tab_minute, R.string.tab_m5,
			R.string.tab_m15, R.string.tab_m30, R.string.tab_m60, R.string.tab_d1 }; 
	
	private StockMinuteChart mMinuteChartView;
	private ProductMinuteKLineChart mFiveMinuteView;
	private ProductMinuteKLineChart mFifteenKLineView;
	private ProductMinuteKLineChart mThirtyKLineView;
	private ProductMinuteKLineChart mSixtyKLineView;
	private ProductKLineChart mDayKLineView;
	
	private View[] mTabViewGroup;
	private FrameLayout mContainerChart;
	
	private DispatcherTimer mMinuteTimer, m5KLineTimer, m15KLineTimer, m30KLineTimer, m60KLineTimer, mDayKLineTimer;
	private boolean[] mShowed = new boolean[6];
	private DispatcherTimer[] mTimers;
	
	private String mStockCode;
	private boolean mVisibility;
	private boolean mResume;
	private boolean mSingleDay;

	public CProductHqKLineChart(Context context) {
		this(context, null);
	}
	
	public CProductHqKLineChart(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CProductHqKLineChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mMinuteTimer = new DispatcherTimer(this, 30, NET_REQUEST_MINUTE);
		m5KLineTimer = new DispatcherTimer(this, 30, NET_REQUEST_5K_LINE);
		m15KLineTimer = new DispatcherTimer(this, 30, NET_REQUEST_15K_LINE);
		m30KLineTimer = new DispatcherTimer(this, 30, NET_REQUEST_30K_LINE);
		m60KLineTimer = new DispatcherTimer(this, 30, NET_REQUEST_60K_LINE);
		mDayKLineTimer = new DispatcherTimer(this, 30, NET_REQUEST_DAY_K_LINE);
		
		mTimers = new DispatcherTimer[] {
					mMinuteTimer, m5KLineTimer, m15KLineTimer, m30KLineTimer, m60KLineTimer, mDayKLineTimer};
		initUI();
	}

	private void initUI() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.product_hq_kline_chart, this, true);
		
		mSliderView = (CSliderView) view.findViewById(R.id.hq_kline_chart_head);
		
		int selected = 0;
		int num = mTabHead.length;
		View[] viewTab = new View[num];
		for (int i = 0; i < num; i++) {
			TextView tv = (TextView) inflater.inflate(R.layout.hq_kline_chart_head, mSliderView, false);
			tv.setText(mTabHead[i]);
			viewTab[i] = tv;
		}
		mSliderView.addChild(viewTab, selected);
		mSliderView.setOnSliderViewSelectListener(this);
		
		mMinuteChartView = new StockMinuteChart(getContext());
		mFiveMinuteView = new ProductMinuteKLineChart(getContext());
		mFifteenKLineView = new ProductMinuteKLineChart(getContext());
		mThirtyKLineView = new ProductMinuteKLineChart(getContext());
		mSixtyKLineView = new ProductMinuteKLineChart(getContext());
		mDayKLineView = new ProductKLineChart(getContext());

		mTabViewGroup = new View[] { mMinuteChartView, mFiveMinuteView,
				mFifteenKLineView, mThirtyKLineView, mSixtyKLineView, mDayKLineView };
		
		mContainerChart = (FrameLayout) findViewById(R.id.container_chart);
		for (int index = 0; index < mTabViewGroup.length; index++) {
			mContainerChart.addView(mTabViewGroup[index]);
		}
		setSelect(0);
	}
	
	private void setSelect(int position) {
		for (int index = 0; index < mTabViewGroup.length; index++) {
			mTabViewGroup[index].setVisibility(position == index ? View.VISIBLE : View.GONE);
		}
		
		if (!mShowed[position]) {
			mShowed[position] = true;
			
			if (mResume && mVisibility) {
				mTimers[position].timerTaskControl(true);
			}
		}
	}
	
	public void setStockCode(String stockCode) {
		this.mStockCode = Util.checkS(stockCode);
	}
	
	public void assignHandicapInfo(GetHandicapInfo handicapInfo) {
		mMinuteChartView.assignHandicapInfo(handicapInfo);
		mFiveMinuteView.assignHandicapInfo(handicapInfo);
		mFifteenKLineView.assignHandicapInfo(handicapInfo);
		mThirtyKLineView.assignHandicapInfo(handicapInfo);
		mSixtyKLineView.assignHandicapInfo(handicapInfo);
		mDayKLineView.assignHandicapInfo(handicapInfo);
	}
	
	@Override
	public void onSliderViewSelect(int position) {
		setSelect(position);
	}
	
	@Override
	public void onAlarmClock(int type) {
		if (type == NET_REQUEST_MINUTE) {
			GetMinuteInfo.doRequest(mStockCode, this);
		} else if (type == NET_REQUEST_5K_LINE) {
			GetFiveKLine.doRequest(mStockCode, this);
		} else if (type == NET_REQUEST_15K_LINE) {
			GetFifteenKLine.doRequest(mStockCode, this);
		} else if (type == NET_REQUEST_30K_LINE) {
			GetThirtyKLine.doRequest(mStockCode, this);
		} else if (type == NET_REQUEST_60K_LINE) {
			GetSixtyKLine.doRequest(mStockCode, this);
		} else if (type == NET_REQUEST_DAY_K_LINE) {
			GetKLine.doRequest(mStockCode, this, mSingleDay ? GetKLine.SINGLE_DAY : GetKLine.MULTI_DAY);
		}
	}
	
	@Override
	public void callBack(RequestInfo info, BaseRes result, int tag) {
		if (result != null && result.error_code == NetworkResultInfo.SUCCESS.getValue()) {
			if (info == RequestInfo.GET_MINUTE) {
				GetMinuteInfo minuteInfo = (GetMinuteInfo) result;
				mMinuteChartView.assignMinuteData(minuteInfo);
			} else if (info == RequestInfo.GET_5K) {
				GetFiveKLine kline = (GetFiveKLine) result;
				mFiveMinuteView.assignKData(kline);
			} else if (info == RequestInfo.GET_15K) {
				GetFifteenKLine kline = (GetFifteenKLine) result;
				mFifteenKLineView.assignKData(kline);
			} else if (info == RequestInfo.GET_30K) {
				GetThirtyKLine kline = (GetThirtyKLine) result;
				mThirtyKLineView.assignKData(kline);
			} else if (info == RequestInfo.GET_60K) {
				GetSixtyKLine kline = (GetSixtyKLine) result;
				mSixtyKLineView.assignKData(kline);
			} else if (info == RequestInfo.GET_K) {
				GetKLine kline = (GetKLine) result;
				if (tag == GetKLine.SINGLE_DAY) {
					mDayKLineView.assignKDataSingle(kline);
				} else if (tag == GetKLine.MULTI_DAY) {
					mDayKLineView.assignKData(kline);
					mSingleDay = true;
					GetKLine.doRequest(mStockCode, this, GetKLine.SINGLE_DAY);
				}
			}
		}
	}
	
	public void onResume() {
		mResume = true;
		
		if (mVisibility) {
			start();
			
			mSingleDay = false;
		}
	}
	
	private void start() {
		for (int i = 0; i < mShowed.length; i++) {
			if (mShowed[i]) {
				mTimers[i].timerTaskControl(true);
			}
		}
	}
	
	public void onPause() {
		mResume = false;
		
		mMinuteTimer.timerTaskControl(false);
		m5KLineTimer.timerTaskControl(false);
		m15KLineTimer.timerTaskControl(false);
		m30KLineTimer.timerTaskControl(false);
		m60KLineTimer.timerTaskControl(false);
		mDayKLineTimer.timerTaskControl(false);
	}
	
	public void show() {
		mVisibility = true;
		setVisibility(View.VISIBLE);
		
		if (mResume) {
			start();
			mSingleDay = false;
		}
	}
	
	public void hide() {
		setVisibility(View.GONE);
		
		mMinuteTimer.timerTaskControl(false);
		m5KLineTimer.timerTaskControl(false);
		m15KLineTimer.timerTaskControl(false);
		m30KLineTimer.timerTaskControl(false);
		m60KLineTimer.timerTaskControl(false);
		mDayKLineTimer.timerTaskControl(false);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return true;
	}
	
}
