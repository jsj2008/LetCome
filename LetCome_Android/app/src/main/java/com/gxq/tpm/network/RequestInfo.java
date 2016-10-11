package com.gxq.tpm.network;

import com.letcome.BuildConfig;

enum Link {PLATFORM, H5,UPLOAD}

enum Type {POST, GET}

enum Certify {YES, NO}

public enum RequestInfo {
	// 用户 -- 登录相关接口
	FAVORITES_REFRESH("/product/myfavorites", Link.PLATFORM, Type.GET, Certify.NO, "刷新我的收藏"),
	FAVORITES_MORE("/product/myfavorites", Link.PLATFORM, Type.GET, Certify.NO, "翻页我的收藏"),
	MY_PRODUCTS_REFRESH("/product/myproducts", Link.PLATFORM, Type.GET, Certify.NO, "刷新我的产品"),
	MY_PRODUCTS_MORE("/product/myproducts", Link.PLATFORM, Type.GET, Certify.NO, "翻页我的产品"),
	WATER_FALLS_REFRESH("/waterfalls", Link.PLATFORM, Type.GET, Certify.NO, "刷新瀑布流"),
	WATER_FALLS_MORE("/waterfalls", Link.PLATFORM, Type.GET, Certify.NO, "翻页瀑布流"),
	LOGIN("/user/login", Link.PLATFORM, Type.POST, Certify.NO, "登录"),
	SIGNUP("/user/register", Link.PLATFORM, Type.POST, Certify.NO, "注册"),
	UPLOAD("/image/upload", Link.UPLOAD, Type.POST, Certify.NO, "上传图片"),
	UPDATEPRODUCT("/product/update", Link.PLATFORM, Type.POST, Certify.NO, "更新产品"),
	CATEGORIES("/categories", Link.PLATFORM, Type.GET, Certify.NO, "获取产品目录"),
	DOFAVORITE("/product/favorite", Link.PLATFORM, Type.GET, Certify.NO, "点赞"),
	UNFAVORITE("/product/unfavorite", Link.PLATFORM, Type.GET, Certify.NO, "取消收藏");
	private String mOperationType;
	private Link mLink;
	private Type mRequestType;
	private Certify mNeedCertify;
	private String mName;

	private RequestInfo(String operationType, Link link, String name) {
		this(operationType, link, Type.GET, Certify.NO, name);
	}
	
	private RequestInfo(String operationType, Link link,
			Type requestType, Certify needCertify, String name) {
		this.mOperationType = operationType;
		this.mLink = link;
		this.mRequestType = requestType;
		this.mNeedCertify = needCertify;
		this.mName = name;
	}
	
	public String getOperationType() {
		return mOperationType;
	}
	
	public String getUrl() {
		if (mLink == Link.PLATFORM || mLink == Link.UPLOAD) {
			return BuildConfig.SERVICE_PLATFORM + getOperationType();
		} else if (mLink == Link.H5) {
			return BuildConfig.SERVICE_H5 + mOperationType;
		} else {
			return mOperationType;
		}
	}

	public String getName() {
		return mName;
	}

	public boolean isPost() {
		return mRequestType == Type.POST;
	}
	public boolean isUpload() {
		return mLink == Link.UPLOAD;
	}
	
	public boolean needCertify() {
		return mNeedCertify == Certify.YES;
	}
	
}
