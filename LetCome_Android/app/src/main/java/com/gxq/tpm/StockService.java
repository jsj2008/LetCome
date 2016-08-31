package com.gxq.tpm;

import java.util.List;

import com.gxq.tpm.mode.BaseRes;
import com.gxq.tpm.mode.IniPatch;
import com.gxq.tpm.mode.StockList;
import com.gxq.tpm.mode.UpdatePathGet;
import com.gxq.tpm.mode.StockList.StockListParse;
import com.gxq.tpm.mode.cooperation.AdInfo;
import com.gxq.tpm.network.NetworkResultInfo;
import com.gxq.tpm.network.RequestInfo;
import com.gxq.tpm.network.NetworkProxy.ICallBack;
import com.gxq.tpm.tools.FileDownload;
import com.gxq.tpm.tools.Util;
import com.gxq.tpm.tools.cache.AsyncImageLoaderProxy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StockService extends Service implements ICallBack {
	 
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		IniPatch.Params param = new IniPatch.Params();
		param.version = App.instance().getVersionName();
		
		IniPatch patch = App.getUserPrefs().getIniPatch();
		if (patch == null) {
			param.ini_version = "";
		} else {
			param.ini_version = patch.ini_version;
		}
		
		IniPatch.doRequest(this, param);
		
		UpdatePathGet.Params params = new UpdatePathGet.Params();
		params.name = "stock_list";
		UpdatePathGet.doRequest(params, this);
		
		requestAd();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void requestAd() {
		AdInfo.Params params = new AdInfo.Params();
		params.type = "homepage_ad";
		AdInfo.doRequest(params, this);
	}

	@Override
	public void callBack(RequestInfo info, BaseRes result, int tag) {
		BaseRes res;
		if (result != null) {
			res = (BaseRes) result;
			if (res.error_code == NetworkResultInfo.SUCCESS.getValue()) {
				if (info == RequestInfo.UPDATE_INIPATH) {
					IniPatch patch = (IniPatch) res;
					if (patch != null) {
						if (BaseRes.RESULT_OK.equalsIgnoreCase(patch.result) 
								&& BaseRes.RESULT_OK.equalsIgnoreCase(patch.is_update)) {
							ServiceConfig.init(patch);
		
							App.getUserPrefs().setIniPatch(patch);
						}
					}
				} else if (info == RequestInfo.UPDATE_PATH_GET) {
					final UpdatePathGet update = (UpdatePathGet) result;
					if (update == null || update.res_data == null) return;
					if (Util.needUpdate(update.res_data.version, 
							App.getUserPrefs().getStockListVersion())) {
						new Thread() {
							public void run() {
								String content = FileDownload.downloadFile(update.res_data.url);
								
								StockListParse parse = new StockListParse();
								StockList stockList = parse.parse(content);
								App.getDBManager().updateStockList(stockList);
								App.getUserPrefs().setStockListVersion(update.res_data.version);
							}
						}.start();
						
					}
				}else if(info == RequestInfo.GET_AD){
					AdInfo adInfo = (AdInfo) result;
					if(null != adInfo && adInfo.records != null && adInfo.records.size() > 0){
						List<AdInfo.Record> infoList = adInfo.records;
						AsyncImageLoaderProxy loader = new AsyncImageLoaderProxy(App.instance());
						loader.setCache2File(true);
						for (int i = 0; i < infoList.size(); i++) {
							loader.downloadCache2memory(infoList.get(i).pic, null);
						}
					}					
				}
			}
		}
	}
	
	

}
