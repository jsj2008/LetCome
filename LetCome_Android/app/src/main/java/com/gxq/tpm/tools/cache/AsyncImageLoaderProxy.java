package com.gxq.tpm.tools.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;

import com.letcome.App;
import com.letcome.R;

public class AsyncImageLoaderProxy {
	// 保存正在下载的图片URL集合，避免重复下载用
	private static HashSet<String> sDownloadingSet;
	// 软引用内存缓存
	private static Map<String, SoftReference<Bitmap>> sImageCache;
	// 图片三种获取方式管理者，网络URL获取、内存缓存获取、外部文件缓存获取
	private static LoaderImpl impl;
	// 图片获取完成通知UI线程
	private Handler handler;
	// 线程池相关
	private static ExecutorService sExecutorService;

	/**
	 * 异步加载图片完毕的回调接口
	 */
	public interface ImageCallback {
		/**
		 * 回调函数
		 *
		 * @param bitmap
		 *            : may be null!
		 * @param imageUrl
		 */
		public void onImageLoaded(Bitmap bitmap, String imageUrl);
	}

	static {
		sDownloadingSet = new HashSet<String>();
		sImageCache = new HashMap<String, SoftReference<Bitmap>>();
		impl = new LoaderImpl(sImageCache);
	}

	public AsyncImageLoaderProxy(Context context) {
		handler = new Handler();
		startThreadPoolIfNecessary();

		String defaultDir = context.getCacheDir().getAbsolutePath();
		setCachedDir(defaultDir);
	}

	/**
	 * 是否缓存图片至文件系统  默认不缓存
	 */
	public void setCache2File(boolean flag) {
		impl.setCache2File(flag);
	}

	/**
	 * 设置缓存路径，setCache2File(true)时有效
	 */
	public void setCachedDir(String dir) {
		impl.setCachedDir(dir);
	}

	/** 开启线程池 */
	public static void startThreadPoolIfNecessary() {
		if (sExecutorService == null || sExecutorService.isShutdown()
				|| sExecutorService.isTerminated()) {
			sExecutorService = Executors.newFixedThreadPool(3);
		}
	}

	/**
	 * 异步下载图片，并缓存到memory中
	 *
	 * @param url
	 * @param callback
	 *            see ImageCallback interface
	 */
	public void downloadImage(final String url, final ImageCallback callback) {
		downloadImage(url, true, callback);
	}

	/**
	 *
	 *  @Description    : 缓存文件到指定路径；当缓存超过5M时，将清空缓存。
	 *  @return         : void
	 *  @Creation Date  : 2014-6-27 上午9:42:25
	 *  @Author         : HuNan
	 *  @Update Date    :
	 *  @Update Author  : HuNan
	 */
	public void downloadCache2Sd(final String url,final ImageCallback callback) {
		impl.setCachedDir(Environment.getExternalStorageDirectory() + App.instance().getString(R.string.cache_path));
		impl.setCache2File(true);
		downloadImage(url, true, callback);
	}

	/**
	 *
	 *  @Description    : 缓存文件到指定路径；程序不会主动清除缓存
	 *  @return         : void
	 *  @Creation Date  : 2014-6-27 上午9:42:25
	 *  @Author         : HuNan
	 *  @Update Date    :
	 *  @Update Author  : HuNan
	 */
	public void downloadCacheForever(final String url,final ImageCallback callback) {
		impl.setCachedDir(Environment.getExternalStorageDirectory() + App.instance().getString(R.string.cache_forever_path));
		impl.setCache2File(true);
		downloadImage(url, true, callback);
	}

	/**
	 *
	 *  @Description    : 图片缓存到内存，只做soft缓存
	 *  @return         : void
	 *  @Creation Date  : 2014-6-27 下午12:13:41
	 *  @Author         : HuNan
	 *  @Update Date    :
	 *  @Update Author  : HuNan
	 */
	public void downloadCache2memory(final String url,final ImageCallback callback) {
		impl.setCache2File(false);
		downloadImage(url, true, callback);
	}
	/**
	 *
	 * @param url
	 * @param cache2Memory
	 *            是否缓存至memory中
	 * @param callback
	 */
	public void downloadImage(final String url, final boolean cache2Memory,
			final ImageCallback callback) {
		if (sDownloadingSet.contains(url)) {// 防止重复下载图片
			return;
		}

		Bitmap bitmap = impl.getBitmapFromMemory(url);
		if (bitmap != null) {
			if (callback != null) {
				callback.onImageLoaded(bitmap, url);
			}
		} else {
			// 从网络端下载图片
			sDownloadingSet.add(url);
			sExecutorService.submit(new Runnable() {
				@Override
				public void run() {
					final Bitmap bitmap = impl.getBitmapFromUrl(url, cache2Memory);
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (callback != null)
								callback.onImageLoaded(bitmap, url);
							sDownloadingSet.remove(url);
						}
					});
				}
			});
		}
	}
	
	public void downloadImage2(final String url, final boolean cache2Memory,
			final ImageCallback callback) {

		Bitmap bitmap = impl.getBitmapFromMemory(url);
		if (bitmap != null) {
			if (callback != null) {
				callback.onImageLoaded(bitmap, url);
			}
		} else {
			// 从网络端下载图片
			sDownloadingSet.add(url);
			sExecutorService.submit(new Runnable() {
				@Override
				public void run() {
					final Bitmap bitmap = impl.getBitmapFromUrl(url,
							cache2Memory);
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (callback != null)
								callback.onImageLoaded(bitmap, url);
							sDownloadingSet.remove(url);
						}
					});
				}
			});
		}
	}


	/**
	 * 预加载下一张图片，缓存至memory中
	 *
	 * @param url
	 */
	public void preLoadNextImage(final String url) {
		// 将callback置为空，只将bitmap缓存到memory即可。
		downloadImage(url, null);
	}
}
