package com.gxq.tpm.prefs;

import com.gxq.tpm.tools.crypt.MD5;
import com.gxq.tpm.tools.crypt.SHA1;

import android.content.Context;

public class LockPrefs extends BasePrefs{
	private static final String PREFS_NAME = "lock_prefs";
	
	private static final int LEAVE_NUM		= 5;
	private static final String LEAVENUM 	= "leave_num";
	
	public static final int PWDLENGTH 		= 32;
	
	private static String OPEN;
	private static String CLOSE;
	
	private long mUid;
	private String mEncryptUid;
	
	protected LockPrefs(Context context, long uid) {
		super(context, PREFS_NAME + encrypt(Long.toString(uid)));
		mUid = uid;
		mEncryptUid = encrypt(Long.toString(uid));
		OPEN = encrypt("1");
		CLOSE = encrypt("0");
	}

	public static LockPrefs get(Context context, long uid) {
		return new LockPrefs(context, uid);
	}
	
	public long getUid() {
		return mUid;
	}
	
	public void setLeaveNum(int num){
		putInt(LEAVENUM, num);
		save();
	}

	public int getLeaveNum(){
		return getInt(LEAVENUM, LEAVE_NUM);
	}
	
	public void resetLeaveNum() {
		setLeaveNum(LEAVE_NUM);
		save();
	}
	
	/**
	 * @param src 校验手势密码
	 */
	public boolean checkGesture(String src) {
		String info = getString(mEncryptUid, EMPTY_STRING);
		if (info.length() == PWDLENGTH * 3) {
			return encrypt(src).equals(getInternalPassword(info));
		}
		return false;
	}
	
	/**
	 *  @Description    : 设置手势密码
	 */
	public void setGesture(String password) {
		String info = getString(mEncryptUid, EMPTY_STRING);
		if (info.length() == PWDLENGTH * 3) {
			save(mEncryptUid, encrypt(password), getInternalGestureOpen(info));
		} else {
			save(mEncryptUid, encrypt(password), CLOSE);
		}
	}
	
	/**
	 * @Description    : 手势密码是否设置
	 */
	public boolean gestureIsSetted() {
		String info = getString(mEncryptUid, EMPTY_STRING);
		if (info.length() == PWDLENGTH * 3) {
			return true;
		}
		return false;
	}
	
	/**
	 *  @Description    : 手势密码是否打开
	 */
	public boolean gestureIsOpen() {
		String info = getString(mEncryptUid, EMPTY_STRING);
		if (info.length() == PWDLENGTH * 3) {
			return OPEN.equals(info.subSequence(PWDLENGTH * 2, PWDLENGTH * 3));
		}
		return false;
	}
	
	/**
	 *  @Description    : 开启手势密码
	 */
	public boolean openGesture() {
		String info = getString(mEncryptUid, EMPTY_STRING);
		if (info.length() == PWDLENGTH * 3) {
			save(mEncryptUid, getInternalPassword(info), OPEN);
			return true;
		}
		return false;
	}

	/**
	 *  @Description    : 打开并设置手势密码
	 */
	public void openAndSetGesture(String password) {
		save(mEncryptUid, encrypt(password), OPEN);
	}
	
	/**
	 *  @Description    : 关闭手势密码
	 */
	public void closeGesture() {
		String info = getString(mEncryptUid, EMPTY_STRING);
		if (info.length() == PWDLENGTH * 3) {
			save(mEncryptUid, getInternalPassword(info), CLOSE);
		}
	}
	
	/**
	 *  @Description    : 删除手势密码
	 */
	public void removeGesture() {
		putString(mEncryptUid, "");
		save();
	}
	
	/**
	 *  @Description    : 修改手势密码
	 */
	public boolean updateGesture(String passwd) {
		String info = getString(mEncryptUid, EMPTY_STRING);
		if (info.length() == PWDLENGTH * 3) {
			save(mEncryptUid, encrypt(passwd), getInternalGestureOpen(info));
			return true;
		}
		return false;
	}
	
	private String getInternalPassword(String info) {
		return info.substring(PWDLENGTH * 1, PWDLENGTH * 2);
	}
	
	public String getInternalGestureOpen(String info) {
		return info.substring(PWDLENGTH * 2, PWDLENGTH * 3);
	}
	
	private void save(String uid, String password, String open) {
		putString(uid, uid + password + open);
		save();
	}
	
	private static String encrypt(String src) {
		String sha1 = SHA1.SHA1Digest(src);
		String md5 = sha1;
		md5 = MD5.md5(sha1);
		return md5;
	}
	
}
