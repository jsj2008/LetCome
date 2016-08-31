package com.gxq.tpm.activity.launch;

import com.letcome.App;
import com.gxq.tpm.GlobalConstant;
import com.gxq.tpm.ProductIntent;
import com.letcome.R;
import com.gxq.tpm.activity.SuperActivity;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.UserInfo;
import com.gxq.tpm.mode.launch.LogOut;
import com.gxq.tpm.mode.launch.Login;
import com.gxq.tpm.network.NetInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.prefs.LockPrefs;
import com.gxq.tpm.tools.GlobalInfo;
import com.gxq.tpm.tools.SingletonToast;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.crypt.SHA1;
import com.gxq.tpm.ui.LockPatternView;
import com.gxq.tpm.ui.ProductDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LockPatternActivity extends SuperActivity implements
        OnClickListener, LockPatternView.OnCompleteListener {
	public static final int GESTURE_LOGIN    		= 0;
	public static final int GESTURE_SET      		= 2;
	public static final int GESTURE_MODIFY   		= 3;
	public static final int GESTURE_CLOSE    		= 4;
	
	private static final int LENGTH_SHORT     		= 0;
	private static final int DRAW_AGAIN       		= 1;
	private static final int DRAW_FALSE       		= 2;
	private static final int NOT_EQUAL 				= 3;
	private static final int LOCK_SUCCESS     		= 4;
	private static final int DRAW_FIVE_MORE   		= 5;
	private static final int REQUEST_CODE     		= 5567;
	
	private LockPatternView mLockPatternView;
	private RelativeLayout mContainerTitleBar, mContainerBtnLeft;
	private TextView mTvTitleBar, mTextHint, mTextWarning, mTvChangeAccount, mTvForgetGesture;
	
	private Animation mShake;
	
	private LockPrefs mLockPrefs;

	private int mGestureType; // 进入状态，登录、设置、修改和删除
	private int mDetailType; // 具体状态
	
	private String mPassword;
	
	private long mCurrentTime, mLastTime = 0;
	private boolean mFirstTime;
	private ProductDialog mDialog;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// Bitmap bitmap = null;
			switch (msg.what) {
			case LENGTH_SHORT:
				mTextHint.setVisibility(View.INVISIBLE);
				mTextWarning.setVisibility(View.VISIBLE);
				mTextWarning.setText(R.string.lock_gesture_draw_warning);
				break;
			case DRAW_AGAIN:
				mTextHint.setVisibility(View.VISIBLE);
				mTextWarning.setVisibility(View.INVISIBLE);
				mTextHint.setText(R.string.lock_gesture_draw_agian);
				break;
			case DRAW_FALSE:
				int times = mLockPrefs.getLeaveNum();

				if (--times < 0)
					times = 0;

				mTextHint.setVisibility(View.INVISIBLE);
				mTextWarning.setVisibility(View.VISIBLE);
				mTextWarning.setText(getString(R.string.lock_gesture_wrong_warning, times));
				mTextWarning.startAnimation(mShake);
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

				long[] pattern = {30, 30, 30, 30};
				vibrator.vibrate(pattern, -1);
				mLockPrefs.setLeaveNum(times);
				mLockPrefs.save();
				break;
			case NOT_EQUAL:
				mTextHint.setVisibility(View.INVISIBLE);
				mTextWarning.setVisibility(View.VISIBLE);
				mTextWarning.setText(R.string.lock_gesture_not_same_warning);
				break;
			case LOCK_SUCCESS:
				mTextHint.setVisibility(View.INVISIBLE);
				mTextWarning.setVisibility(View.INVISIBLE);
				
				mLockPrefs.setLeaveNum(5);
				mLockPrefs.save();
				
				if (GESTURE_SET == mDetailType) {
					if (GESTURE_SET == mGestureType) {
						String uid = Long.toString(mUserPrefs.getUid());
						if (uid != null) {
							// 首次设置手势密码
							mLockPrefs.openAndSetGesture(mPassword); // id（登录后返回），开关状态“1”（设置手势密码时一定为1）
						} 
					} else if (GESTURE_MODIFY == mGestureType) {
						// 重新设置手势密码
						mLockPrefs.updateGesture(mPassword);
					}

					act_finish();
				} else if (GESTURE_MODIFY == mDetailType) {
					mDetailType = GESTURE_SET;
					checkStatus();
				} else if (GESTURE_CLOSE == mDetailType) {
					act_finish();
				} else if (GESTURE_LOGIN == mDetailType) {
					if (NetInfo.instance(LockPatternActivity.this).isAvailable()) {
						showWaitDialog(RequestInfo.USER_INFO);
						userInfoRequest();
					} else {
						act_finish();
					}
				}
				break;
			case DRAW_FIVE_MORE:
				new ProductDialog.Builder(LockPatternActivity.this)
					.setContentText(R.string.lock_gesture_5wrong_warning, R.drawable.layer_remind)
					.setPositiveButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
						
						@Override
						public void onDialogClick(int id) {
							if (mLockPrefs != null) {
								mLockPrefs.removeGesture();
								mLockPrefs.resetLeaveNum();
							}
							
							Intent intent = new Intent(LockPatternActivity.this, LockLoginActivity.class);
							intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
							startActivity(intent);
							act_finish();
						}
					}).create().show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	protected void initBar() {
		super.initBar();

		Intent lockIntent = getIntent();
		mGestureType = mDetailType = lockIntent.getIntExtra(ProductIntent.EXTRA_TYPE, 0);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_lockpattern);
		
		mLockPrefs = mUserPrefs.getLockPrefs();
		mShake = AnimationUtils.loadAnimation(this, R.anim.shake);

		initUI();
		checkStatus();
	}
	
	private void initUI() {
		mContainerTitleBar = (RelativeLayout) findViewById(R.id.titleBar);
		
		mContainerBtnLeft = (RelativeLayout) findViewById(R.id.btn_left);
		mContainerBtnLeft.setOnClickListener(this);
		
		mTvTitleBar = (TextView) findViewById(R.id.tv_titleBar);
		mTextHint = (TextView) findViewById(R.id.text_hint);
		mTextWarning = (TextView) findViewById(R.id.text_warning);

		mLockPatternView = (LockPatternView) this.findViewById(R.id.lockview);
		mLockPatternView.setOnCompleteListener(this);
		
		mTvChangeAccount = (TextView) findViewById(R.id.tv_change_account);
		mTvChangeAccount.setOnClickListener(this);
		
		mTvForgetGesture = (TextView) findViewById(R.id.tv_forget_gesture);
		mTvForgetGesture.setOnClickListener(this);
	}

	private void checkStatus() {
		mTextWarning.setVisibility(View.INVISIBLE);
		switch (mDetailType) {
		case GESTURE_SET:
			mContainerTitleBar.setVisibility(View.VISIBLE);
			mContainerBtnLeft.setVisibility(View.VISIBLE);
			mTvTitleBar.setText(R.string.lock_set_gesture);
			
			mTextHint.setVisibility(View.VISIBLE);
			mTextHint.setText(Util.transformString(R.string.lock_gesture_draw) +
					Util.transformString(R.string.lock_gesture_draw_limit));
			
			mTvChangeAccount.setVisibility(View.INVISIBLE);
			mTvForgetGesture.setVisibility(View.INVISIBLE);
			break;
		case GESTURE_LOGIN:
			mContainerTitleBar.setVisibility(View.VISIBLE);
			mContainerBtnLeft.setVisibility(View.INVISIBLE);
			mTvTitleBar.setText(R.string.lock_gesture);
			
			mTextHint.setVisibility(View.VISIBLE);
			mTextHint.setText(Util.transformString(R.string.lock_gesture_draw) +
					Util.transformString(R.string.lock_gesture_draw_limit));
			
			mTvChangeAccount.setVisibility(View.VISIBLE);
			mContainerBtnLeft.setVisibility(View.INVISIBLE);
			break;
		case GESTURE_CLOSE:
		case GESTURE_MODIFY:
			mContainerTitleBar.setVisibility(View.VISIBLE);
			mContainerBtnLeft.setVisibility(View.VISIBLE);
			mTvTitleBar.setText(R.string.lock_check_gesture);
			
			mTextHint.setVisibility(View.VISIBLE);
			mTextHint.setText(R.string.lock_gesture_check);
			
			mTvChangeAccount.setVisibility(View.INVISIBLE);
			mTvForgetGesture.setVisibility(View.VISIBLE);
			break;
		}
	}


	public void act_finish() {
		App.LockStartTime(); // 重置手势锁屏，hunan
		setResult(RESULT_OK, getIntent());
		this.finish();
	}
	
	@Override
	public void onLeftClick(View v) {
		setResult(RESULT_CANCELED);
		super.onLeftClick(v);
	}

	@Override
	public void onBackPressed() {
		if (mGestureType != GESTURE_LOGIN) {
			setResult(RESULT_CANCELED);
			this.finish();
		} else {
			mCurrentTime = System.currentTimeMillis();
			if (mCurrentTime - mLastTime > 10 * 1000)
				mFirstTime = true;
			if (mFirstTime) {
				SingletonToast.getInstance().makeText(this, "再按一次，退出程序", 2000).show();
				mLastTime = System.currentTimeMillis();
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				long[] pattern = {30, 30, 30, 30};
				vibrator.vibrate(pattern, -1);
				mFirstTime = false;
			} else {
				sendNotify(ProductIntent.ACTION_EXIT);
				// 重新设置时间
				mUserPrefs.setCurrentTime(-App.GESTURE_LOCK_TIME);
			}
		}
	}
	
	public void onComplete(boolean success) {
		if (success) {
			if (mGestureType == GESTURE_SET) {
				setPassword();
			} else if (mGestureType == GESTURE_CLOSE) {
				checkPassword();
			} else if (mGestureType == GESTURE_MODIFY) {
				if (mDetailType == GESTURE_MODIFY) {
					checkPassword();
					mPassword = null;
				} else if (mDetailType == GESTURE_SET) {
					setPassword();
				}
			} else if (mGestureType == GESTURE_LOGIN) {
				checkPassword();
			}
		} else {
			mHandler.sendEmptyMessage(LENGTH_SHORT);
			mLockPatternView.checkError();
		}
	}
	
	private void setPassword() {
		if (mPassword == null) {
			mPassword = mLockPatternView.getPassword();
			mHandler.sendEmptyMessage(DRAW_AGAIN);
			mLockPatternView.reset();
		} else {
			String password = mLockPatternView.getPassword();
			if (mPassword.equals(password)) {
				mHandler.sendEmptyMessage(LOCK_SUCCESS);
			} else {
				mLockPatternView.checkError();
				mHandler.sendEmptyMessage(NOT_EQUAL);
			}
		}
	}
	
	private void checkPassword() {
		mPassword = mLockPatternView.getPassword();
		if (mLockPrefs.checkGesture(mPassword)) {
			mLockPatternView.reset();
			mHandler.sendEmptyMessage(LOCK_SUCCESS);
		} else {
			if (mLockPrefs.getLeaveNum() == 1) {
				mLockPatternView.reset();
				mHandler.sendEmptyMessage(DRAW_FIVE_MORE);
			} else {
				mLockPatternView.checkError();
				mHandler.sendEmptyMessage(DRAW_FALSE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_change_account:
			LogOut.doRequest(null, LockPatternActivity.this);
			// showWaitDialog(null);
			localLogout();
			break;
		case R.id.btn_left:
			finish();
			break;
		case R.id.tv_forget_gesture:
			if (GESTURE_MODIFY == mDetailType || GESTURE_CLOSE == mDetailType) {
				mDialog = new ProductDialog.Builder(this)
					.setContentView(R.layout.lock_dialog_content)
					.setPositiveButton(R.string.btn_cancel, null)
					.setNegativeButton(R.string.btn_confirm, new ProductDialog.ProductDialogListener() {
						@Override
						public void onDialogClick(int id) {
							EditText et = (EditText) mDialog.findViewById(R.id.dialog_edit);
							if (Util.checkPwd(et.getText().toString()))
								loginRequest(et.getText().toString());
						}
					}).create();
				
				mDialog.show();
			} else if (GESTURE_LOGIN == mDetailType) {
				if (mLockPrefs != null) {
					mLockPrefs.removeGesture();
					mLockPrefs.resetLeaveNum();
				}
				Intent intent = new Intent(LockPatternActivity.this, LockLoginActivity.class);
				intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
	}

	private void localLogout() {
		GlobalInfo.cleanLogin();
		Intent intent = new Intent(LockPatternActivity.this, ProductLoginActivity.class);
		intent.putExtra(ProductIntent.EXTRA_ACTIVITY_FROM, ProductIntent.FROM_LAUNCH);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode) {
			act_finish();
		}
	}
	private void loginRequest(String pwd) {
		showWaitDialog(RequestInfo.LOGIN);
		Login.Params params = new Login.Params();
		params.login_id = mUserPrefs.getUid() + "";
		pwd = GlobalConstant.isEncrypt ? SHA1.SHA1Digest(pwd) : pwd;
		params.password = pwd;
		params.login_type = 6;
		Login.doRequest(params, this);
	}

	private void userInfoRequest() {
		UserInfo.Params params = new UserInfo.Params();
		params.uid = App.getUserPrefs().getUid();
		UserInfo.doRequest(params, LockPatternActivity.this, 5);
		showWaitDialog(RequestInfo.USER_INFO);
	}

	@Override
	public void netFinishOk(RequestInfo info, BaseRes res, int tag) {
		if (RequestInfo.LOGIN == info) {
			Login log = (Login) res;
			mUserPrefs.setKEY(log.encryptedKey);
			mUserPrefs.setUid(log.uid);
			mUserPrefs.setIsNeedVerify(false);
			mUserPrefs.save();
			if (GESTURE_MODIFY == mGestureType && GESTURE_MODIFY == mDetailType) {
				mDetailType = GESTURE_SET;
				checkStatus();
			} else if (GESTURE_CLOSE == mDetailType){
				act_finish();
			}
		} else if (RequestInfo.USER_INFO == info) {
			if (isFinishing())
				return;
			UserInfo userInfo = (UserInfo) res;
			App.getUserPrefs().setUserInfo(userInfo);
			App.getUserPrefs().save();
			act_finish();
		}
	}

	@Override
	public int netFinishError(RequestInfo info, int what, String msg, int tag) {
		 if (RequestInfo.USER_INFO == info){
			 act_finish();
			 return 0;
		 } else if (RequestInfo.PWD_CHECK == info) {
			 return ERROR_TOAST;
		 }
//		 else if (RequestInfo.LOGIN.getOperationType().equals(operationType)){
//			 if(what==79002)// 失败次数过多，需要验证码
//				msg="密码错误";
//		 }
		 return super.netFinishError(info, what, msg, tag);
	}
}
