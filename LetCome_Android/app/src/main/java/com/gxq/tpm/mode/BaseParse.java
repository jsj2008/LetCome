package com.gxq.tpm.mode;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseParse<T extends BaseRes> {
	
	public T parse(String str) {
		try {
			return parse(new JSONObject(str));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected abstract T parse(JSONObject jsonObject);
	
//	public void parseHead(JSONObject jsonObject, BaseRes head) {
//		if (head == null) return;
//		
//		head.error_code = jsonObject.has("error_code") ? parseInteger(jsonObject, "error_code") : 0;
//		head.error_msg = jsonObject.has("error_msg") ? parseString(jsonObject, "error_msg") : " ";
//	}
	
	protected String parseString(JSONObject object, String name) {
		try {
			return object.getString(name);
		} catch (JSONException e) {
			return "";
		}
	}
	
	protected int parseInteger(JSONObject object, String name) {
		try {
			return object.getInt(name);
		} catch (JSONException e) {
			return 0;
		}
	}
	
	protected long parseLong(JSONObject object, String name) {
		try {
			return object.getLong(name);
		} catch (JSONException e) {
			return 0;
		}
	}

	protected double parseDouble(JSONObject object, String name) {
		try {
			return object.getDouble(name);
		} catch (JSONException e) {
			return 0.0;
		}
	}
	
	protected JSONObject parseObject(JSONObject object, String name) {
		try {
			return object.getJSONObject(name);
		} catch (JSONException e) {
			return null;
		}
	}
	
}
