package com.gxq.tpm.ui;

import com.letcome.R;
import com.gxq.tpm.tools.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CEmptyNoteView extends LinearLayout implements View.OnClickListener {
	private final static int RELEASE_TO_REFRESH = 0; // 放手刷新
	private final static int PULL_TO_REFRESH 	= 1; // 下拉刷新
	private final static int REFRESHING			= 2; // 刷新
	private final static int DONE 				= 3;
	
	private final static int RATIO = 3;
	
	private View mHeadView, mContainerContent;
	private Animation mCycleAnimation, mReverseAnimation;

	private ImageView mIvHeadArrow;
	private ProgressBar mPbHead;
	private TextView mTvHeadTips;
	
	private TextView mTvSrc;
	private TextView mTvBtn;
	
	private boolean mRefreshEnable = false;
	private OnRefreshListener mRefreshListener;
	
	private int mHeadState = DONE;
	private boolean mRecored, mReverse; // 保证触摸的y值只记录一次
	private int mStartDownY;
	private int mHeadContentHeight;
	
	private View.OnClickListener mListener; 
	
	public CEmptyNoteView(Context context) {
		this(context, null);
	}

	public CEmptyNoteView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CEmptyNoteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CEmptyNoteView);;
		String srcText = a.getString(R.styleable.CEmptyNoteView_src_text);
		String btnText = a.getString(R.styleable.CEmptyNoteView_btn_text);
		
		a.recycle();
		
		View view = inflate(context, R.layout.empty_note_view, this);
		mContainerContent = view.findViewById(R.id.container_content);
		mTvSrc = (TextView) view.findViewById(R.id.tv_none_des);
		mTvBtn = (TextView) view.findViewById(R.id.tv_next_step);

		mHeadView = (LinearLayout) view.findViewById(R.id.container_head);
		mIvHeadArrow = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
		mPbHead = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
		mTvHeadTips = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);

		setSrcText(srcText);
		setBtnText(btnText);
		
		mCycleAnimation = AnimationUtils.loadAnimation(context, R.anim.listview_rotate);
		mReverseAnimation = AnimationUtils.loadAnimation(context, R.anim.listview_reverse);
		
		mHeadContentHeight = Util.transformDimen(R.dimen.margin_xhdpi_50);
	}
	
	public void setOnClickListener(View.OnClickListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onClick(v);
		}
	}
	
	public void setSrcText(String text) {
		if (text != null) {
			mTvSrc.setText(text);
		}
	}
	
	public void setBtnText(String text) {
		if (text != null) {
			mTvBtn.setText(text);
			mTvBtn.setOnClickListener(this);
			mTvBtn.setVisibility(View.VISIBLE);
		} else {
			mTvBtn.setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!mRecored) {
				mStartDownY = (int) event.getY();
				mRecored = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (!mRecored) {
				mStartDownY = (int) event.getY();
				mRecored = true;
			}
			if (mRefreshEnable) {
				refreshHeadView(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mRefreshEnable) {
				refreshHeadView(event);
			}
			mRecored = false;
			mReverse = false;
			break;
		}
		
		if (super.onTouchEvent(event)) {
			return true;
		} else {
			return true;
		}
	}
	
	private void refreshHeadView(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int currentY = (int) event.getY();
			if (mHeadState != REFRESHING && mRecored) {
				if (mHeadState == DONE) {
					if (currentY - mStartDownY > 0) {
						mHeadState = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
				}
				if (mHeadState == PULL_TO_REFRESH) {
					// 下拉到可以进入RELEASE_TO_REFRESH的状态
					if ((currentY - mStartDownY) / RATIO >= mHeadContentHeight) {
						mHeadState = RELEASE_TO_REFRESH;
						mReverse = true;
						changeHeaderViewByState();
					} else if (currentY - mStartDownY <= 0) { // 上推到顶了
						mHeadState = DONE;
						changeHeaderViewByState();
					}
				}
				// 可以松手去刷新了
				if (mHeadState == RELEASE_TO_REFRESH) {
					// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
					if (((currentY - mStartDownY) / RATIO < mHeadContentHeight)
							&& (currentY - mStartDownY) > 0) {
						mHeadState = PULL_TO_REFRESH;
						changeHeaderViewByState();
					} else if (currentY - mStartDownY <= 0) { // 一下子推到顶了
						mHeadState = DONE;
						changeHeaderViewByState();
					} else {// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						// 不用进行特别的操作，只用更新paddingTop的值就行了
					}
				}
				
				// 更新headView的paddingTop
				if (mHeadState == RELEASE_TO_REFRESH || mHeadState == PULL_TO_REFRESH) {
					mHeadView.setVisibility(View.VISIBLE);
					int translationY = (currentY - mStartDownY) / RATIO;
					mHeadView.setTranslationY(translationY - mHeadContentHeight);
					mContainerContent.setTranslationY(translationY);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mHeadState == PULL_TO_REFRESH) {
				mHeadState = DONE;
				changeHeaderViewByState();
			}
			if (mHeadState == RELEASE_TO_REFRESH) {
				mHeadState = REFRESHING;
				changeHeaderViewByState();
				
				if (mRefreshListener != null) {
					mRefreshListener.onRefresh();
				} else {
					onRefreshComplete();
				}
			}
			break;
		}
	}
	
	private void changeHeaderViewByState() {
		switch (mHeadState) {
		case PULL_TO_REFRESH:
			mPbHead.setVisibility(View.GONE);
			mTvHeadTips.setVisibility(View.VISIBLE);
			mIvHeadArrow.clearAnimation();
			mIvHeadArrow.setVisibility(View.VISIBLE);
			mTvHeadTips.setText(getResources().getString(R.string.drop_down_refresh));
			
			// 是由RELEASE_To_REFRESH状态转变来的
			if (mReverse) {
				mReverse = false;
				mIvHeadArrow.clearAnimation();
				mIvHeadArrow.startAnimation(mReverseAnimation);
			}		
			break;
		case RELEASE_TO_REFRESH:
			mIvHeadArrow.setVisibility(View.VISIBLE);
			mPbHead.setVisibility(View.GONE);
			mTvHeadTips.setVisibility(View.VISIBLE);

			mIvHeadArrow.clearAnimation();
			mIvHeadArrow.startAnimation(mCycleAnimation);

			mTvHeadTips.setText(getResources().getString(R.string.loosen_refresh));
			break;
		case REFRESHING:
			mHeadView.setTranslationY(0);
			mContainerContent.setTranslationY(0);
			mPbHead.setVisibility(View.VISIBLE);
			mIvHeadArrow.clearAnimation();
			mIvHeadArrow.setVisibility(View.GONE);
			mTvHeadTips.setText(getResources().getString(R.string.refreshing));

//			LOG.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			mHeadView.setVisibility(View.GONE);
			mPbHead.setVisibility(View.GONE);
			mIvHeadArrow.clearAnimation();
			mIvHeadArrow.setImageResource(R.drawable.list_arrow);
			mTvHeadTips.setText(getResources().getString(R.string.drop_down_refresh));
			break;
		}
	}
	
	public void onRefreshComplete() {
		mHeadState = DONE;
		changeHeaderViewByState();
	}

	public void setRefreshEnable(boolean isRefreshEnable) {
		this.mRefreshEnable = isRefreshEnable;
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}

	public static interface OnRefreshListener {
		public void onRefresh();
	}
	
}
