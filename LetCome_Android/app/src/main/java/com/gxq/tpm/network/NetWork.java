package com.gxq.tpm.network;

import android.os.Build;

import com.google.gson.Gson;
import com.gxq.tpm.mode.BaseParse;
import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.BaseRes.ByteArrayRes;
import com.gxq.tpm.tools.Print;
import com.letcome.App;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NetWork {
	private final static String TAG 				= "SNetWork";
	private final static String UA					= "User-Agent";
	private final static String HEAD_UA				= "x-qfgj-ua";
	private final static String HEAD_SID			= "x-qfgj-sid";
	private final static String HEAD_UID			= "x-qfgj-uid";
	private final static String HEAD_UDID			= "x-qfgj-did";
	private final static String HEAD_REFER			= "x-qfgj-refer";
	private final static String HEAD_RID			= "x-qfgj-rid";
	private final static String HEAD_RTIME			= "x-qfgj-rtime";
	private final static String HEAD_CONTENT		= "x-qfgj-contentmd5";
	private final static String HEAD_SIGNATURE		= "x-qfgj-signature";
	private final static String HEAD_SIGN			= "x-qfgj-sign";
	private final static String HEAD_CONTENT_TYPE	= "Content-Type";
	
	public final static String PARAM_ACCEPT			= "Accept";
	public final static String PARAM_VALUE_IMAGE	= "image/*";
	
	private final static String HEAD_SEP			= ":";
	private final static String HEAD_LINE_SEP		= "\n";
	
	private final static String UA_PROJECT_NAME		= "tpz";
	
	private final static String CHARSET				= "UTF-8";
	private final static String CONTENT_TYPE_JSON	= "application/json; charset=UTF-8";
	
	public final static int OUT_TIME_DEFAULT 		= 30; // 默认超时时间
	
	private static NetWork netWork;

	private DefaultHttpClient client;
	private HttpParams httpParams;
	
	private RequestInfo mInfo;
	
	private int timeOut = OUT_TIME_DEFAULT;

	private NetWork() {
		httpParams = new BasicHttpParams();
	}

	public static NetWork instance() {
		// wujun 解决报单实例的错误，没有数据返回。
		// if (netWork == null) {
		netWork = new NetWork();
		// }
		return netWork;
	}

	private void setOutTime(int second) {
		HttpConnectionParams.setConnectionTimeout(httpParams, 1000 * second);
		HttpConnectionParams.setSoTimeout(httpParams, 1000 * second);
		Print.i(TAG, "time out=" + second);
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int second) {
		this.timeOut = second;
	}
	
	public void setConnectionTimeout(int second) {
		HttpConnectionParams.setConnectionTimeout(httpParams, 1000 * second);
	}

	public void setSoTimeout(int second) {
		HttpConnectionParams.setSoTimeout(httpParams, 1000 * second);
	}

	/**
	 *
	 * @Description : post请求
	 */
	public BaseRes postRequest(RequestInfo info, Object params, Class<? extends BaseRes> cls) {
		try {
			String url = info.getUrl();
			
			Map<String, String> paramsMap = getParamMap(params);
			HttpResponse resp = postRequest(info, url, paramsMap);
		
			String result = getBaseReturn(info, resp);

			return getBaseRes(result, cls);
		} catch (Exception e) {
			Print.e(TAG, e.getMessage());
		}
		return null;
	}
	
	/**
	 *
	 * @Description : post请求
	 */
	public BaseRes postRequest(RequestInfo info, Object params, BaseParse<? extends BaseRes> parse) {
		try {
			String url = info.getUrl();
			
			Map<String, String> paramsMap = getParamMap(params);
			HttpResponse resp = postRequest(info, url, paramsMap);
			 
			String result = getBaseReturn(info, resp);
			 
			return getBaseRes(result, parse);
		} catch (Exception e) {
			Print.e(TAG, e.getMessage());
		}
		return null;
	}
	
	private HttpResponse postRequest(RequestInfo info, String url, Map<String, String> paramsMap) throws Exception {
		this.mInfo = info;
		Print.i(TAG, "url = " + url);
		
		setOutTime(timeOut);
		client = new DefaultHttpClient();
		client.setParams(httpParams);

		HttpPost httpPost = new HttpPost(url);
		
		String json = null;
		
		if (paramsMap != null) {
			Gson gson = new Gson();
			json = gson.toJson(paramsMap);
		}
		
		addHttpHead(httpPost, json, info.needCertify(), paramsMap);

		if (json != null){// 请求参数处理
			httpPost.setHeader(HEAD_CONTENT_TYPE, CONTENT_TYPE_JSON);
			httpPost.setEntity(new StringEntity(json, CHARSET));

			Print.i(TAG, new String(getContentAsBytes(httpPost.getEntity().getContent()), CHARSET));
		}

		HttpResponse resp = client.execute(httpPost);
		Print.w(TAG, "StatusCode=" + resp.getStatusLine().getStatusCode());

		return resp;
	}
	
	/**
	 *
	 * @Description : get请求
	 */
	public BaseRes getRequest(RequestInfo info, Object params, Class<? extends BaseRes> cls) {
		String url = info.getUrl();
		try {
			String accept = "";		
			Map<String, String> paramsMap = getParamMap(params);
			if (paramsMap != null && paramsMap.size() > 0) {
				for (String key : paramsMap.keySet()) {
					if (PARAM_ACCEPT.equals(key)) {
						accept = paramsMap.get(key);
						paramsMap.remove(key);
						break;
					}
				}
				
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (String key : paramsMap.keySet()) {
					list.add(new BasicNameValuePair(key, paramsMap.get(key)));
				}

				String urlparam = new String(
						getContentAsBytes(new UrlEncodedFormEntity(list,
								HTTP.UTF_8).getContent()), CHARSET);
				url += "?";
				url += urlparam;
			}
			
			HttpResponse resp = getRequest(info, url, paramsMap); 
			
			if (PARAM_VALUE_IMAGE.equals(accept)) {
				byte[] byteArray = getContentAsBytes(resp);
				return new ByteArrayRes(byteArray);
			} else {
				String result = getBaseReturn(info, resp);
				return getBaseRes(result, cls);
			}
		} catch (Exception e) {
			Print.e(TAG, e.getMessage());
		}
		return null;
	}
	
	/**
	 *
	 * @Description : get请求
	 */
	public BaseRes getRequest(RequestInfo info, Object params,
			BaseParse<? extends BaseRes> parse) {
		try {
			String url = info.getUrl();
			Map<String, String> paramsMap = getParamMap(params);
			HttpResponse resp = getRequest(info, url, paramsMap);
			
			String result = getBaseReturn(info, resp);
			
			return getBaseRes(result, parse);
		} catch (Exception e) {
			Print.e(TAG, e.getMessage());
		}
		return null;
	}
	
	private HttpResponse getRequest(RequestInfo info, String url, Map<String, String> paramsMap)
			throws Exception {
		Print.i(TAG, "url = " + url);
		
		setOutTime(timeOut);
		client = new DefaultHttpClient();
		client.setParams(httpParams);
		
		HttpGet httpGet = new HttpGet(url);
		addHttpHead(httpGet, null, false, paramsMap);
		
		Print.i(TAG, "url=" + url);

		HttpResponse resp = client.execute(httpGet);
		return resp;
	}

	private void addHttpHead(HttpRequestBase httpRequest, String json, final boolean needCetify, Map<String, String> paramsMap) {
//		UserPrefs pref = App.getUserPrefs();
		httpRequest.setHeader(UA, UA_PROJECT_NAME + " v_" + App.instance().getVersionName()
				+ " (Android " + Build.VERSION.RELEASE + ")") ;
		httpRequest.addHeader(HEAD_UA, UA_PROJECT_NAME + " v_" + App.instance().getVersionName()
				+ " (Android " + Build.VERSION.RELEASE + ")") ;
//		httpRequest.addHeader(HEAD_SID, pref.getSession());
//		httpRequest.addHeader(HEAD_UID, "" + pref.getUid());
//		httpRequest.addHeader(HEAD_UDID, pref.getOpenUdid());
		
		String channel = App.instance().getChannel();
		httpRequest.addHeader(HEAD_REFER, channel);

//		long Rid = pref.getRid();
//		if (Rid == Long.MAX_VALUE)
//			Rid = 1L;
//		else
//			Rid++;
//		pref.setRid(Rid);
//		httpRequest.addHeader(HEAD_RID, "" + Rid);
		String rTime = "" + System.currentTimeMillis()/1000;
		httpRequest.addHeader(HEAD_RTIME, rTime);

		addHttpHead(httpRequest, paramsMap, rTime);
		
		String md5 = "";
		String signature = "";
//		if (needCetify) {
//			md5 = MD5.md5(json);
//
//			String strToSign;
//			String CanonicalURI = mInfo.getOperationType();
//			String HTTPRequestMethod = "POST";
//			String CanonicalHeaders = HEAD_SID + HEAD_SEP + pref.getSession() + HEAD_LINE_SEP +
////			        "x-qfgj-uid" + ":" + pref.getUid() + "\n" +
//			        HEAD_UDID + HEAD_SEP + pref.getOpenUdid() + HEAD_LINE_SEP +
//			        HEAD_RID + HEAD_SEP + Rid + HEAD_LINE_SEP +
//			        HEAD_RTIME + HEAD_SEP + rTime + HEAD_LINE_SEP +
//			        HEAD_CONTENT + HEAD_SEP + md5 + HEAD_LINE_SEP;
//			strToSign = CanonicalURI + HEAD_LINE_SEP + HTTPRequestMethod + HEAD_LINE_SEP + HEAD_LINE_SEP + CanonicalHeaders;
//			Print.i(TAG, "CanonicalHeaders=" + CanonicalHeaders);
//
//			signature = HmacSHA1.signature("12345ba035678685a46adee051dca85be88eakey", strToSign);
//			Print.i(TAG, "signature="+signature);
//		}
//		pref.save();

		httpRequest.addHeader(HEAD_CONTENT, md5);
		httpRequest.addHeader(HEAD_SIGNATURE, signature.trim());

//		Print.i(TAG, "uid="+pref.getUid());
	}
	
	private void addHttpHead(HttpRequestBase httpRequest, Map<String, String> paramsMap, String rTime) {
		String paramStr = "";
		
		if (paramsMap != null) {
			List<String> keyList = new ArrayList<String>();
			keyList.addAll(paramsMap.keySet());
			Collections.sort(keyList);

			for (int index = 0; index < keyList.size(); index++) {
				if (index > 0) {
					paramStr += "&";
				}
				String key = keyList.get(index);
				paramStr += key + "=" + paramsMap.get(key);
			}

		}
//		paramStr += "_"+ App.getUserPrefs().getKEY() + "_" + rTime;
//		Print.d(TAG, HEAD_SIGN + ":" + paramStr);
//		httpRequest.addHeader(HEAD_SIGN, MD5.md5(paramStr));
	}

	private String getBaseReturn(RequestInfo info, HttpResponse resp) throws Exception {
		String result = getContentAsText(resp);
		result = result.replace("\"errorcode\"", "\"error_code\"");
		result = result.replace("\"errormsg\"", "\"error_msg\"");
		result = result.trim();
		result = result.replaceAll(",\"[a-zA-z]+\":\"\"", "");

		Print.d(TAG, "operationType=" + info.getOperationType() + "\n result = " + result);
		return result;
	}
	
	private String getContentAsText(HttpResponse res) throws Exception {
		final char[] buffer = new char[4096];
		StringBuilder builder = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(
				res.getEntity().getContent(), CHARSET);
		int read;
		while ((read = reader.read(buffer)) > 0) {
			builder.append(buffer, 0, read);
		}
		// res.getEntity().consumeContent();
		reader.close();
		return builder.toString();
	}
	
	/**
	 */
	private BaseRes getBaseRes(String result, Class<? extends BaseRes> cls) {
		if (result == null || result.length() == 0)
			return null;
		
		BaseRes resObj = null;

		Gson gson = new Gson();
		try {
			resObj = gson.fromJson(result, cls);
		} catch (Exception e) {
			Print.i(TAG, e.getMessage());
			resObj = gson.fromJson(result, BaseRes.class);
		}
		return resObj;
	}
	
	private BaseRes getBaseRes(String result, BaseParse<? extends BaseRes> parse) {
		if (result == null || result.length() == 0)
			return null;
		
		if (result.startsWith("\\ufeff")) {
			result = result.substring(1);
		}
		
		BaseRes baseRes = getBaseRes(result, BaseRes.class);
		if (baseRes == null || baseRes.error_code != NetworkResultInfo.SUCCESS.getValue()) {
			return baseRes;
		}
		
		return parse.parse(result);
	}
	
	private Map<String, String> getParamMap(Object params) {
		if (params == null)
			return null;
		
		try {
			Map<String, String> paramsMap = new HashMap<String, String>();
			Gson gson = new Gson();
			JSONObject jsonObj = new JSONObject(gson.toJson(params));
			for (Iterator<?> iter = jsonObj.keys(); iter.hasNext();) {
				// 先遍历整个  对象
				String key = (String) iter.next();
				String value = jsonObj.getString(key);
				paramsMap.put(key, value);
			}
			for(String key : paramsMap.keySet()) {
				Print.d(TAG, "params " + key + ":" + paramsMap.get(key));
			}
			return paramsMap;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private byte[] getContentAsBytes(HttpResponse resp)
			throws IllegalStateException, IOException {
		byte[] buffer = new byte[4096];
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(4096);
		InputStream input = resp.getEntity().getContent();
		int read;
		while ((read = input.read(buffer)) > 0) {
			byteStream.write(buffer, 0, read);
		}
		return byteStream.toByteArray();
	}

	private byte[] getContentAsBytes(InputStream input)
			throws IllegalStateException, IOException {
		byte[] buffer = new byte[4096];
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(4096);
		int read;
		while ((read = input.read(buffer)) > 0) {
			byteStream.write(buffer, 0, read);
		}
		return byteStream.toByteArray();
	}

}
