package com.gxq.tpm.mode;

import java.io.Serializable;

public class BaseRes implements Serializable{

	private static final long serialVersionUID = -450857240238031914L;
	
	public final static int RETURN_TYPE		= 0;
	public final static String RESULT_OK	= "Y";
	public final static String RESULT_FAIL	= "N";
	public String result;
	public int error_code;
	public String error_msg;
	public String retVal;
	public String http_x_qfgj_contentmd5;
	public int http_x_qfgj_dup_login;
	public String session_id;
	public long request_id;
	public long response_time;

	public String getRetVal() {
		return retVal;
	}

	public void setRetVal(String retVal) {
		this.retVal = retVal;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public static class ByteArrayRes extends BaseRes {

		private static final long serialVersionUID = 8899652955866241037L;
		
		private byte[] mByteArray;
		
		public ByteArrayRes(byte[] byteArray) {
			this.mByteArray = byteArray;
		}
		
		public byte[] getByteArray() {
			return mByteArray;
		}
	}
	
	public static class QueryParams implements Serializable {
		private static final long serialVersionUID = 988584546293749526L;

		public int limit;
		public int offset;
		public int start_id;
	}
	
}
