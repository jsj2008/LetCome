package com.gxq.tpm.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.mine.DissentOrders.DissentDetail;
import com.gxq.tpm.tools.TimeFormat;
import com.gxq.tpm.tools.Util;

public class DissentRecordDetailActivity extends SuperActivity {
	private DissentDetail mDetail;
	
	private TextView mTvPid;
	private TextView mTvCreateTime;
	private TextView mTvNeedCorrect;
	private TextView mTvResult;
	
	private TextView mTvPoint;
	private TextView mTvPointMessage;
	
	private View mComentContainer;
	private TextView mTvCommentMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dissent_record_detail);
		initView();

		mDetail = (DissentDetail) getIntent().getSerializableExtra(ProductIntent.EXTRA_DETAIL);
		assignDetail();

	}

	protected void initView() {
		mTvPid = (TextView) findViewById(R.id.tv_dissent_pid);
		mTvCreateTime = (TextView) findViewById(R.id.tv_dissent_create_time);
		mTvResult = (TextView) findViewById(R.id.tv_dissent_result);
		mTvNeedCorrect = (TextView) findViewById(R.id.tv_dissent_need_correct);
		mTvPoint = ((TextView) findViewById(R.id.tv_dissent_point));
		mTvPointMessage = ((TextView) findViewById(R.id.tv_dissent_point_message));
		
		mComentContainer = findViewById(R.id.dissent_comment_container);
		mTvCommentMessage =  ((TextView) findViewById(R.id.tv_dissent_comment_message));
		
	}
	
	@Override
	protected void initBar() {
		super.initBar();
		getTitleBar().setTitle(R.string.user_dissent_record_detail);
		getTitleBar().showBackImage();
	}

	private void assignDetail() {
		String info = "";
		String needCorect = getString(R.string.number_default_value);
		
		mTvPid.setText(mDetail.pno);
		mTvCreateTime.setText(TimeFormat.dateDefaultAllFormat(mDetail.create_time));
		mTvPoint.setText(mDetail.point_name);
		
		switch (mDetail.state) {
		case 0:
			info = getString(R.string.dissent_state_wait);
			mComentContainer.setVisibility(View.GONE);
			break;
		case 1:
			info = getString(R.string.dissent_state_process);
			mComentContainer.setVisibility(View.GONE);
			break;
		case 2:
			info = getString(R.string.dissent_state_finsh_y);
			needCorect = getString(R.string.dissent_state_correct_n);
			mTvNeedCorrect.setTextColor(Util.transformColor(R.color.gain_color));
			mTvCommentMessage.setText(mDetail.comment);
			break;
		case 3:
			info = Util.transformString(R.string.dissent_state_finsh_y);
			needCorect=Util.transformString(R.string.dissent_state_correct_y);
			mTvNeedCorrect.setTextColor(Util.transformColor(R.color.loss_color));
			mTvCommentMessage.setText(mDetail.comment);
			break;
		default:
			info = Util.transformString(R.string.dissent_state_wait);
			mComentContainer.setVisibility(View.GONE);
			break;
		}
		mTvResult.setText(info);
    	mTvNeedCorrect.setText(needCorect);
    	mTvPointMessage.setText(mDetail.reason);
	}
	
}
