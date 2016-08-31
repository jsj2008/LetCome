package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Print;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CScrollView extends ScrollView{

	public CScrollView(Context context) {
		this(context, null);
	}
	
	public CScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean result = super.onInterceptTouchEvent(ev);
		float eventY = ev.getY();
		float listTop = findViewById(R.id.listview_container).getTop() - getScrollY();
		float listBottom = findViewById(R.id.listview_container).getBottom() - getScrollY();
		Print.e("ccc", "ccc listTop = "+ listTop+" listBottom "+listBottom);
		if(eventY > listTop && eventY < listBottom){
			result = false;
			Print.e("ccc", "ccc result = "+ result);
		}
		return result;
	}

}
