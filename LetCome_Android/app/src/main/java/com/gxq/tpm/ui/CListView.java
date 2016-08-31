package com.gxq.tpm.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letcome.R;

public class CListView extends ListView implements OnScrollListener {
	private final static int RELEASE_TO_REFRESH = 0; // 放手刷新
	private final static int PULL_TO_REFRESH 	= 1; // 下拉刷新
	private final static int REFRESHING			= 2; // 刷新
	private final static int DONE 				= 3;

	private final static int RELEASE_TO_MORE 	= 5; // 放手刷新
	private final static int PULL_UP_MORE 		= 6; // 下拉刷新

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;
	
	private LinearLayout mHeadView, mFootView;
	private Animation mCycleAnimation, mReverseAnimation;

	private ImageView mIvHeadArrow, mIvFootArrow;
	private ProgressBar mPbHead, mPbFoot;
	private TextView mTvHeadTips, mTvFootTips;
	
	private int mHeadContentHeight, mFootContentHeight;
	
	private int mHeadState, mMoreState;
	private int mFirstItemIndex, mRemainItemsCount;
	
	private boolean mMoreEnable = true;
	private boolean mRefreshEnable = true;

	private int mStartDownY;
	
	private boolean mRecored; // 保证触摸的y值只记录一次
	private boolean mReverse; // 图标是否旋转 
	
	private OnRefreshListener mRefreshListener;

//	private int moreDownY;
	private OnMoreListener moreListener;
	
//	private TextView footLastUpdated;
//	private boolean isMoreBack;
	
	public CListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return; 
		}

		LayoutInflater inflater = LayoutInflater.from(context);
		setFadingEdgeLength(0);
		setCacheColorHint(context.getResources().getColor(R.color.transparent));
		
		initHeadView(inflater);
		
		initFootView(inflater);

		mCycleAnimation = AnimationUtils.loadAnimation(context, R.anim.listview_rotate);
		mReverseAnimation = AnimationUtils.loadAnimation(context, R.anim.listview_reverse);

		setOnScrollListener(this);
		mHeadState = mMoreState = DONE;
	}
	
	private void initHeadView(LayoutInflater inflater) {
		mHeadView = (LinearLayout) inflater.inflate(R.layout.list_head, this, false);
		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getMeasuredHeight();
		mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
		mHeadView.invalidate();
		addHeaderView(mHeadView, null, false);
		
		mIvHeadArrow = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
		mPbHead = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
		mTvHeadTips = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
	}

	private void initFootView(LayoutInflater inflater) {
		mFootView = (LinearLayout) inflater.inflate(R.layout.list_foot, this, false);
		measureView(mFootView);
		mFootContentHeight = mFootView.getMeasuredHeight();
		mFootView.setPadding(0, -1 * mFootContentHeight, 0, 0);
		mFootView.invalidate();
		addFooterView(mFootView, null, false);
		
		mIvFootArrow = (ImageView) mFootView.findViewById(R.id.foot_arrowImageView);
		mPbFoot = (ProgressBar) mFootView.findViewById(R.id.foot_progressBar);
		mTvFootTips = (TextView) mFootView.findViewById(R.id.foot_tipsTextView);
	}
	
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScroll(AbsListView v, int firstVisiableItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisiableItem;
		mRemainItemsCount = (totalItemCount -1) - firstVisiableItem - visibleItemCount;
//		Print.d("CListView", "FirstItemIndex = " + mFirstItemIndex);
//		Print.d("CListView", "visibleItemCount = " + visibleItemCount);
//		Print.d("CListView", "totalItemCount = " + totalItemCount);
//		Print.d("CListView", "RemainItemsCount = " + mRemainItemsCount);
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
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
			if (mFirstItemIndex == 0 && mRefreshEnable) {
				refreshHeadView(event);
			} else if (mRemainItemsCount <= 0 && mMoreEnable) {
				refreshFootView(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mFirstItemIndex == 0 && mRefreshEnable) {
				refreshHeadView(event);
			} else if (mRemainItemsCount <= 0 && mMoreEnable) {
				refreshFootView(event);
			}
			mRecored = false;
			mReverse = false;
			break;
		}
		
		return super.onTouchEvent(event);
	}

	private void refreshHeadView(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int currentY = (int) event.getY();

			if (mHeadState != REFRESHING && mRecored) {
				// done状态下
				if (mHeadState == DONE) {
					if (currentY - mStartDownY > 0) {
						mHeadState = PULL_TO_REFRESH;
						changeHeaderViewByState();
					}
				}
	
				if (mHeadState == PULL_TO_REFRESH) {
					setSelection(0);
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
					setSelection(0);
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
					mHeadView.setPadding(0, (currentY - mStartDownY) / RATIO
							- mHeadContentHeight, 0, 0);
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
				}
			}
			break;
		default:
			break;
		}
	}

	private void refreshFootView(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int currentY = (int) event.getY();
			
			if (mMoreState != REFRESHING && mRecored) {
				if (mMoreState == RELEASE_TO_MORE) {
					setSelection(getAdapter().getCount() - 1);
					if (currentY - mStartDownY < 0 &&
							(mStartDownY - currentY) / RATIO < mFootContentHeight) {
						mMoreState = PULL_UP_MORE;
						changeFootViewByState();
					} else if (currentY - mStartDownY >= 0) {
						mMoreState = DONE;
						changeFootViewByState();
					}
				}

				if (mMoreState == PULL_UP_MORE) {
					setSelection(getAdapter().getCount() - 1);
					if ((mStartDownY - currentY) / RATIO >= mFootContentHeight) {
						mMoreState = RELEASE_TO_MORE;
						mReverse = true;
						changeFootViewByState();
					} else if (currentY - mStartDownY >= 0) {
						mMoreState = DONE;
						changeFootViewByState();
					}
				}

				if (mMoreState == DONE) {
					if (currentY - mStartDownY < 0) {
						mMoreState = PULL_UP_MORE;
						changeFootViewByState();
					}
				}

				if (mMoreState == PULL_UP_MORE || mMoreState == RELEASE_TO_MORE) {
					mFootView.setPadding(0, (mStartDownY - currentY) / RATIO
							- mFootContentHeight, 0, 0);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mMoreState == PULL_UP_MORE) {
				mMoreState = DONE;
				changeFootViewByState();
			}

			if (mMoreState == RELEASE_TO_MORE) {
				mMoreState = REFRESHING;
				changeFootViewByState();
				
				if (moreListener != null) {
					moreListener.onMore();
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 *
	 *  @Description    : 当状态改变时候，调用该方法，以更新界面
	 */
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
//			LOG.v(TAG, "当前状态，下拉刷新");
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
			mHeadView.setPadding(0, 0, 0, 0);
			mPbHead.setVisibility(View.VISIBLE);
			mIvHeadArrow.clearAnimation();
			mIvHeadArrow.setVisibility(View.GONE);
			mTvHeadTips.setText(getResources().getString(R.string.refreshing));
//			lastUpdatedTextView.setVisibility(View.VISIBLE);

//			LOG.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
			mPbHead.setVisibility(View.GONE);
			mIvHeadArrow.clearAnimation();
			mIvHeadArrow.setImageResource(R.drawable.list_arrow);
			mTvHeadTips.setText(getResources().getString(R.string.drop_down_refresh));
//			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void changeFootViewByState() {
		switch (mMoreState) {
		case RELEASE_TO_MORE:
			mIvFootArrow.setVisibility(View.VISIBLE);
			mPbFoot.setVisibility(View.GONE);
			mTvFootTips.setVisibility(View.VISIBLE);
//			footLastUpdated.setVisibility(View.VISIBLE);

			mIvFootArrow.clearAnimation();
			mIvFootArrow.startAnimation(mCycleAnimation);

			mTvFootTips.setText(getResources().getString(R.string.loosen_more));
			break;
		case PULL_UP_MORE:
			mPbFoot.setVisibility(View.GONE);
			mTvFootTips.setVisibility(View.VISIBLE);
//			footLastUpdated.setVisibility(View.VISIBLE);
			mIvFootArrow.clearAnimation();
			mIvFootArrow.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			
			mTvFootTips.setText(getResources().getString(R.string.drop_up_more));
			if (mReverse) {
				mReverse = false;
				mIvFootArrow.clearAnimation();
				mIvFootArrow.startAnimation(mReverseAnimation);
			}
			break;
		case REFRESHING:
			mFootView.setPadding(0, 0, 0, 0);
			mPbFoot.setVisibility(View.VISIBLE);
			mIvFootArrow.clearAnimation();
			mIvFootArrow.setVisibility(View.GONE);
			mTvFootTips.setText(getResources().getString(R.string.refreshing));
//			footLastUpdated.setVisibility(View.VISIBLE);
			break;
		case DONE:
			mFootView.setPadding(0, -1 * mFootContentHeight, 0, 0);
			mPbFoot.setVisibility(View.GONE);
			mIvFootArrow.clearAnimation();
			mIvFootArrow.setImageResource(R.drawable.list_arrow);
			mTvFootTips.setText(getResources().getString(R.string.drop_up_more));
//			footLastUpdated.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	public void setRefreshEnable(boolean isRefreshEnable) {
		this.mRefreshEnable = isRefreshEnable;
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
	}
	
	public void setMoreEnable(boolean moreEnable) {
		this.mMoreEnable = moreEnable;
	}

	public void setMoreListener(OnMoreListener moreListener) {
		this.moreListener = moreListener;
	}

	public void onRefreshComplete() {
		mHeadState = DONE;
		changeHeaderViewByState();
	}

	public void onMoreComplete() {
		mMoreState = DONE;
		changeFootViewByState();
	}

	public static interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnMoreListener {
		public void onMore();
	}

}
