package com.gxq.tpm.mode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseArrayParse<T extends BaseRes> extends BaseParse<T> {

	@Override
	public T parse(String str) {
		try {
			return parse(new JSONArray(str));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected T parse(JSONObject jsonObject) {
		return null;
	}

	protected abstract T parse(JSONArray jsonArray);
	
}
