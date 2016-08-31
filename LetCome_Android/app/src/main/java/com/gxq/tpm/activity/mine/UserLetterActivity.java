package com.gxq.tpm.activity.mine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.adapter.ArrayListAdapter;
import com.gxq.tpm.adapter.CViewPagerAdapter;
import com.gxq.tpm.adapter.MsgAdapter;
import com.gxq.tpm.fragment.ViewPagerFragment;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.GetTime;
import com.gxq.tpm.mode.NeedNotice;
import com.gxq.tpm.mode.mine.MsgList;
import com.gxq.tpm.mode.mine.MsgList.Msg;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.CListViewAdapterHelper;
import com.gxq.tpm.ui.CListView;
import com.gxq.tpm.ui.CTabTitleWithNotice;

public class UserLetterActivity extends SuperActivity {
	private static final long COUNT = 15;
	
	private final int MESSAGE_TAG_ANNO 		= 1001;
	private final int MESSAGE_TAG_ANNO_INIT	= 1004;
	private final int MESSAGE_TAG_SYTEM 	= 1002;
	private final int MESSAGE_TAG_NOTICE	= 1003;
	
	private CTabTitleWithNotice mTitle;
	private ViewPager mViewPager;
	private CViewPagerAdapter mAdapter;
	
	private List<ViewPagerFragment> mFragments = new ArrayList<ViewPagerFragment>();
	private LetterPagerFragment mAnnoFragment, mSystemFragment, mNoticeFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_letter_message);
		
		mTitle = (CTabTitleWithNotice) findViewById(R.id.letter_title);
		mTitle.newTabTitle(R.string.letter_announcement);
		mTitle.newTabTitle(R.string.letter_system_info);
		mTitle.newTabTitle(R.string.letter_notice);
		
		mViewPager = (ViewPager) findViewById(R.id.letter_viewpager);
		
		mAnnoFragment = new LetterPagerFragment(this, mViewPager);
		mAnnoFragment.setValue(0, MESSAGE_TAG_ANNO);
		mSystemFragment = new LetterPagerFragment(this, mViewPager);
		mSystemFragment.setValue(500, MESSAGE_TAG_SYTEM);
		mNoticeFragment = new LetterPagerFragment(this, mViewPager);
		mNoticeFragment.setValue(3, MESSAGE_TAG_NOTICE);
		mFragments.add(mAnnoFragment);
		mFragments.add(mSystemFragment);
		mFragments.add(mNoticeFragment);
		
		mAdapter = new CViewPagerAdapter(mFragments);
		mViewPager.setAdapter(mAdapter);
		
		initAction();
		
		mTitle.setPosition(0);
	}
	
	private void setSelection(int position) {
		mTitle.showNotice(position, false);
		if (position == 0) {
			GetTime.doRequest(this, MESSAGE_TAG_ANNO);
		} else if (position == 1) {
			GetTime.doRequest(this, MESSAGE_TAG_SYTEM);
		} else if (position == 2) {
			GetTime.doRequest(this, MESSAGE_TAG_NOTICE);
		}
	}
	
	private void initAction() {
		mTitle.setOnTabTitleSelectListener(new CTabTitleWithNotice.OnTabTitleSelectListener() {
			@Override
			public void onSelection(int position) {
				mViewPager.setCurrentItem(position);
				setSelection(position);
			}
		});
		mTitle.setViewPager(mViewPager);
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		
		getTitleBar().setTitle("@" + getString(R.string.user_letter));
		getTitleBar().showBackImage();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mAdapter.onResume();
		mAdapter.onShow();
		
		GetTime.doRequest(this, MESSAGE_TAG_ANNO_INIT);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mAdapter.onPause();
		mAdapter.onHide();
	}
	
	private void requestList(long from_time, long to_time, int type, int tag) {
		MsgList.Params params = new MsgList.Params();
		params.limit = COUNT;
		/*params.to_time = to_time;
		if (from_time != -1) {
			params.from_time = from_time;
		}*/
		params.msg_type = type;
		MsgList.doRequest(params, UserLetterActivity.this, tag);
	}
	
	private void requestNeedNotice(String type, long time, int tag) {
		NeedNotice.Params params = new NeedNotice.Params();
		params.type = type;
		params.from_time = time;
		NeedNotice.doRequest(params, this, tag);
	}
	
	@Override
	public void netFinishOk(RequestInfo info, BaseRes result, int tag) {
		super.netFinishOk(info, result, tag);
		
		if (RequestInfo.MSG_LIST == info){
			if (MESSAGE_TAG_ANNO == tag) {
				mAnnoFragment.netFinishOk(info, result);
			} else if (MESSAGE_TAG_SYTEM == tag) {
				mSystemFragment.netFinishOk(info, result);
			} else if (MESSAGE_TAG_NOTICE == tag) {
				mNoticeFragment.netFinishOk(info, result);
			}
		} else if (RequestInfo.GET_TIME == info) {
			GetTime time = (GetTime) result;
			if (tag == MESSAGE_TAG_ANNO) {
				mUserPrefs.setAnnoMsgTime(time.time);
			} else if (tag == MESSAGE_TAG_SYTEM) {
				mUserPrefs.setSystemMsgTime(time.time);
			} else if (tag == MESSAGE_TAG_NOTICE) {
				mUserPrefs.setNoticeMsgTime(time.time);
			} else if (tag == MESSAGE_TAG_ANNO_INIT) {
				mUserPrefs.setAnnoMsgTime(time.time);
				
				requestNeedNotice(NeedNotice.ANNO_MSG, mUserPrefs.getAnnoMsgTime(), MESSAGE_TAG_ANNO);
				requestNeedNotice(NeedNotice.SYSTEM_MSG, mUserPrefs.getSystemMsgTime(), MESSAGE_TAG_SYTEM);
				requestNeedNotice(NeedNotice.NOTICE_MSG, mUserPrefs.getNoticeMsgTime(), MESSAGE_TAG_NOTICE);
			}
		} else if (RequestInfo.MSG_NEED_NOTICE == info) {
			NeedNotice needNotice = (NeedNotice) result;
			if (needNotice.records != null) {
				if (tag == MESSAGE_TAG_ANNO) {
					mTitle.showNotice(0, needNotice.records.get(NeedNotice.ANNO_MSG) > 0);
				} else if (tag == MESSAGE_TAG_SYTEM) {
					mTitle.showNotice(1, needNotice.records.get(NeedNotice.SYSTEM_MSG) > 0);
				} else if (tag == MESSAGE_TAG_NOTICE) {
					mTitle.showNotice(2, needNotice.records.get(NeedNotice.NOTICE_MSG) > 0);
				}
			}
		}
	}
	
	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		if (MESSAGE_TAG_ANNO == tag) {
			mAnnoFragment.netFinishError(info);
		} else if (MESSAGE_TAG_SYTEM == tag) {
			mSystemFragment.netFinishError(info);
		} else if (MESSAGE_TAG_NOTICE == tag) {
			mNoticeFragment.netFinishError(info);
		}
		if(msg!=null)
		   showToastMsg(msg);
		return 0;
	}
	
	public class LetterPagerFragment extends ViewPagerFragment {
		private CListView mListView;
		
		private MsgAdapterHelper mAdapterHelper;
		
		private int mType;
		private int mTag;
		
		public LetterPagerFragment(Context context, View container) {
			super(context, container);
		}

		@Override
		protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
			return inflater.inflate(R.layout.activity_letter_list, container, false);
		}
		
		@Override
		protected void onViewCreated(View view) {
			super.onViewCreated(view);
			
			mListView = (CListView) view.findViewById(R.id.msg_list);
			mAdapterHelper = new MsgAdapterHelper(UserLetterActivity.this, mListView);
		}
		
		@Override
		protected void request() {
			super.request();
			
			UserLetterActivity.this.requestList(-1, -1, mType, mTag);
		}
		
		public void setValue(int type, int tag) {
			this.mType = type;
			this.mTag = tag;
		}
		
		public void netFinishOk(RequestInfo info, BaseRes res) {
			if (RequestInfo.MSG_LIST == info) {
				MsgList msgList = (MsgList) res;
				mAdapterHelper.assignList(msgList.records);
			}
		}
		
		public void netFinishError(RequestInfo info) {
			if (RequestInfo.MSG_LIST == info) {
				mAdapterHelper.finish();
			}
		}
		
		private class MsgAdapterHelper extends CListViewAdapterHelper<Msg> {

			public MsgAdapterHelper(ICallBack callBack, CListView listView) {
				super(callBack, listView);
			}

			@Override
			protected ArrayListAdapter<Msg> getListViewAdapter() {
				return new MsgAdapter(UserLetterActivity.this);
			}

			@Override
			protected void requestList() {
				UserLetterActivity.this.requestList(-1, -1, mType, mTag);

				GetTime.doRequest(UserLetterActivity.this, mTag);
			}

			@Override
			protected void requestMore(String startId) {
				UserLetterActivity.this.requestList(-1, Long.parseLong(startId), mType, mTag);
			}

			@Override
			protected String getLastId() {
				if (mContents.size() > 0)
					return Long.toString(mContents.get(mContents.size() - 1).create_time);
				return "-1";
			}
			
		}
	}

}
