package com.gxq.tpm.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.os.Handler;

import com.letcome.App;
import com.letcome.R;

public class FileDownload {
	
	public static void startDownLoadApk(final String url, final DownloadListener listener) {
		final DownloadAdapter adapter = new DownloadAdapter(listener);
		
		new Thread() {
			@Override
			public void run() {
				downloadFile(url, adapter);
			}
		}.start();
	}
	
	public static void downloadFile(String url, DownloadListener listener) {
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		String path = Environment.getExternalStorageDirectory() + App.instance().getString(R.string.cache_path);
		File file = new File(path + fileName);
		if (file.exists()) {
			file.delete();
		}
		if (!file.getParentFile().exists())  {
			file.getParentFile().mkdirs();
		}

		try {
			URL myURL = new URL(url);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			
			int sum = conn.getContentLength();
			int num = 0;
			
			InputStream input = null;
			OutputStream output = null;
			int length = 0;
			byte[] bytes = new byte[1024];
			try {
				input = conn.getInputStream();
				output = new FileOutputStream(file);
				while ((length = input.read(bytes)) != -1) {
					output.write(bytes, 0, length);
					num += length;
					listener.percent(num * 100 / sum);
				}
				listener.done(file.getAbsolutePath());
			} catch (IOException ex) {
				listener.failed();
			} finally {
				if (input != null) 
					try {
						input.close();
					} catch (Exception e) {}
				if (output != null) 
					try {
						output.close();
					} catch (Exception e) {}
			}
		} catch (Exception e) {
			listener.failed();
		}
	}
	
	public static String downloadFile(String url) {
		try {
			URL myURL = new URL(url);
			URLConnection conn = myURL.openConnection();
			conn.connect();
			
			InputStream input = null;
			OutputStream output = null;
			int length = 0;
			byte[] bytes = new byte[1024];
			try {
				input = conn.getInputStream();
				output = new ByteArrayOutputStream();
				while ((length = input.read(bytes)) != -1) {
					output.write(bytes, 0, length);
				}
				return output.toString();
			} catch (IOException ex) {
				return "";
			} finally {
				if (input != null) 
					try {
						output.close();
					} catch (Exception e) {}
				if (output != null) 
					try {
						input.close();
					} catch (Exception e) {}
			}
		} catch (Exception e) {
			return "";
		}
	}
	
	private static class DownloadAdapter implements DownloadListener {
		private DownloadListener mListener;
		private Handler mHandler;
		
		public DownloadAdapter(DownloadListener listener) {
			this.mListener = listener;
			mHandler = new Handler();
		}

		@Override
		public void percent(final int percent) {
			if (mListener != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mListener.percent(percent);;
					}
				});
			}
		}

		@Override
		public void done(final String filePath) {
			if (mListener != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mListener.done(filePath);
					}
				});
			}
		}

		@Override
		public void failed() {
			if (mListener != null) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						mListener.failed();
					}
				});
			}
		}
		
	}
	
	public static interface DownloadListener {
		public void percent(int percent);
		public void done(String filePath);
		public void failed();
	}
	
}
