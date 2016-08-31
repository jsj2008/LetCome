package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Print;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

public class CStockSearchHistoryView extends ListView implements View.OnClickListener {
	private final static String TAG = "CStockSearchHistoryView";
	private final static int SNAP_VELOCITY = 600;  
	
	private OnStockSearchListener mListener;
	
	private View mItemView, mContainerSliderView, mContainerRemove;
	
    private Scroller mScroller;  
    private VelocityTracker mVelocityTracker;
    
    private int mTouchSlop;
    private boolean mIsSlide = false;
    private Point mSize;
    private int mDelta = 0;
    private int mDownX;
    private int mDownY;
    private int mMaxDistence;
    
    private int mSlidePosition = -1;

	public CStockSearchHistoryView(Context context) {
		this(context, null);
	}
	
	public CStockSearchHistoryView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CStockSearchHistoryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mSize = new Point();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(mSize); 
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Print.i(TAG, "mTouchSlop = " + mTouchSlop);
        
        mMaxDistence = context.getResources().getDimensionPixelSize(R.dimen.margin_xhdpi_75);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		 switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN: {
	            addVelocityTracker(event);
	            
	            if (!mScroller.isFinished()) {  
	            	return super.dispatchTouchEvent(event);
				}
	            
				mDownX = (int) event.getX();
				mDownY = (int) event.getY();
				
				int position = pointToPosition(mDownX, mDownY);
				if (position == mSlidePosition)
					break;
				mSlidePosition = position; 
	
				if (mSlidePosition == AdapterView.INVALID_POSITION ) {
					return super.dispatchTouchEvent(event);
				}
				
				clear();
				
				mItemView = getChildAt(mSlidePosition - getFirstVisiblePosition());
				mContainerSliderView = mItemView.findViewById(R.id.container_slider_view);
				mContainerRemove = mItemView.findViewById(R.id.container_remove);
				mContainerRemove.setOnClickListener(this);
				break;
	        }
	        case MotionEvent.ACTION_MOVE: {
	        	if (getCount() <= 1 || mContainerSliderView == null)
	        		break;
	            if (Math.abs(getScrollVelocity()) > SNAP_VELOCITY
	            		|| (Math.abs(event.getX() - mDownX) > mTouchSlop 
	                    		&& Math.abs(event.getY() - mDownY) < mTouchSlop)) {
	                mIsSlide = true;
	            }  
	            break;  
	        }  
	        case MotionEvent.ACTION_UP:  
	        	Print.i(TAG, "dispatchTouchEvent action up");
	            recycleVelocityTracker();  
	            break;  
		 }
		
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mIsSlide && mSlidePosition != AdapterView.INVALID_POSITION) {
            addVelocityTracker(ev); 
            
            final int action = ev.getAction();  
            int x = (int) ev.getX();  
            switch (action) {  
            case MotionEvent.ACTION_DOWN:  
                break;  
            case MotionEvent.ACTION_MOVE:  
                int deltaX = mDownX - x;
                mDownX = x;

                Print.i(TAG, "onTouchEvent action move deltaX = " + deltaX);
                mDelta += deltaX;
                
                if (mDelta < 0) {
                	mContainerSliderView.scrollTo(0, 0);
                	
                	mDelta = 0;
                	mContainerRemove.setVisibility(View.GONE);
                } else if (mDelta >= mMaxDistence) {
                	mDelta = mMaxDistence;
                	mContainerSliderView.scrollTo(mMaxDistence, 0);
                	mContainerRemove.setVisibility(View.VISIBLE);
                	mContainerRemove.setTranslationX(0);
                } else {
                	mContainerSliderView.scrollBy(deltaX, 0);
                	mContainerRemove.setVisibility(View.VISIBLE);
                	mContainerRemove.setTranslationX(mMaxDistence - mDelta);
                }
                break;
            case MotionEvent.ACTION_UP:
            	if (mDelta < mMaxDistence * 4 / 5) {
            		mContainerRemove.setVisibility(View.GONE);
            		scrollRight();
            	} else if (mDelta < mMaxDistence) {
            		scrollLeft();
                } 
                recycleVelocityTracker();  
                mIsSlide = false;  
                break;  
            }
            return true;
		}
		return super.onTouchEvent(ev);
	}
	
	private void scrollRight() {
		final int delta = mDelta;
		mScroller.startScroll(delta, 0, -delta, 0, Math.abs(delta));
		mDelta = 0;
        postInvalidate();
	}
	
	private void scrollLeft() { 
		final int delta = mMaxDistence - mDelta;  
		mScroller.startScroll(mDelta, 0, delta, 0, Math.abs(delta));
		mDelta = mMaxDistence;
        postInvalidate();
	}
	
	@Override  
    public void computeScroll() {
		if (mScroller.computeScrollOffset()) {  
			mContainerSliderView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			mContainerRemove.setTranslationX(mMaxDistence - mScroller.getCurrX());
			
            postInvalidate();  
  
            if (mScroller.isFinished()) {
            	mContainerSliderView.scrollTo(mDelta, 0);
            	mContainerRemove.setTranslationX(0);
            }  
        }  
    }  

	private void addVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}

		mVelocityTracker.addMovement(event);
	}
    
	private int getScrollVelocity() {  
        mVelocityTracker.computeCurrentVelocity(1000);  
        int velocity = (int) mVelocityTracker.getXVelocity();  
        return velocity;  
    }
    
    private void recycleVelocityTracker() {  
        if (mVelocityTracker != null) {  
        	mVelocityTracker.recycle();  
        	mVelocityTracker = null;  
        }  
    }
    
    private void clear() {
    	if (mContainerSliderView != null) {
			mDelta = 0;
			mContainerRemove.setOnClickListener(null);
			mContainerRemove.setVisibility(View.GONE);
			mContainerRemove = null;
			
			mContainerSliderView.scrollTo(0, 0);
			mContainerSliderView = null;
		}
    }
    
    @Override
    public void onClick(View v) {
    	if (mListener != null) {
    		mListener.onStockHistoryRemove(mSlidePosition);
    		
    		clear();
    		mSlidePosition = AdapterView.INVALID_POSITION;
    	}
    }
    
	public void setOnStockSearchListener(OnStockSearchListener listener) {
		mListener = listener;
	}
    
	public static interface OnStockSearchListener {
		public void onStockHistoryRemove(int index);
	}
	
}
