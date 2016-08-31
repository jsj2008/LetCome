package com.gxq.tpm.tools;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.letcome.App;
import com.gxq.tpm.tools.cache.AsyncImageLoaderProxy;
import com.gxq.tpm.tools.cache.AsyncImageLoaderProxy.ImageCallback;

public class LoaderWeiBookImage implements ImageCallback {
	public ImageView imageView;
	private int mResId;

	public LoaderWeiBookImage() {
	}
	
	public void setLoaderWeiBookImage(ImageView imageView, String weiboImg, int resId) {
		this.imageView = imageView;
		this.mResId = resId;
		
		AsyncImageLoaderProxy loader = new AsyncImageLoaderProxy(App.instance());
		loader.setCache2File(true);
		loader.downloadImage(weiboImg, this);
	}

	@Override
	public void onImageLoaded(Bitmap bitmap, String imageUrl) {
		if (null != bitmap && null != imageView) {
			imageView.setImageBitmap(bitmap);
		} else if (imageView != null && mResId != 0){
			imageView.setImageResource(mResId);
		}
	}

}
