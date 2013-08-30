package com.bitmapcache.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bitmapcache.handlebitmap.BaseHandleBitmap;
import com.bitmapcache.utils.Debug;


/**
 *  
 * @author 6a209	
 * Nov 27, 2012 9:53:50 AM
 */
public class BitmapLoader{
	
	public static final String URL_KEY = "url";
	public static final String CORNER_TAIL = "?type=c";
	private static final String TAG = "BitmapLoader";

	private Configuration mConfiguration;
	
	
	private BlockingQueue<Runnable> mNetQueue = new LinkedBlockingQueue<Runnable>(
			200);
	private BlockingQueue<Runnable> mFileQueue = new LinkedBlockingQueue<Runnable>(
			100);
	
	private ThreadPoolExecutor mNetExecutor;

	private ThreadPoolExecutor mFileExecutor;
	private boolean mIsInit = false;
	private List<String> mReqingUrls = new ArrayList<String>();
	private static BitmapLoader sBitmapLoader;
	
	public interface OnLoadOverListener{
		void onLoadOver(String url, Bitmap bitmap);
	}
	
	OnLoadOverListener mOnLoadOverListener;
	
	public static class BitmapCacheHandler extends Handler{
		
		OnLoadOverListener mLoadOverListener;
		public BitmapCacheHandler(OnLoadOverListener listener) {
			mLoadOverListener = listener;
		}
		/**
		 * 屏蔽掉重写此方法
		 */
		@Override
		public final void handleMessage(Message msg){
			String url = msg.getData().getString(BitmapLoader.URL_KEY);
			Bitmap bitmap = (Bitmap)msg.obj;
			sBitmapLoader.removeReqUrl(url);
			if(null == bitmap){
				Debug.d("handleMessage bitmap is nul");
				return;
			}
			sBitmapLoader.put(url, bitmap);
			if(null != mLoadOverListener){
				mLoadOverListener.onLoadOver(url, bitmap);
			}
		}
		
	}	
	
	private BitmapLoader(){
	}	
	
	public static BitmapLoader instance(){
		if(null == sBitmapLoader){
			sBitmapLoader = new BitmapLoader();
		}
		return sBitmapLoader;
	}
	
	/**
	 * must init
	 * @param configuration
	 */
	public void init(Configuration configuration){
		mIsInit = true;
		mConfiguration = configuration;
		mNetExecutor = new ThreadPoolExecutor(mConfiguration.mNetThreadCount, mConfiguration.mNetThreadCount, 30,
				TimeUnit.SECONDS, mNetQueue,new ThreadPoolExecutor.DiscardOldestPolicy());
		mFileExecutor = new ThreadPoolExecutor(mConfiguration.mFileThreadCount, mConfiguration.mFileThreadCount, 20, 
				TimeUnit.SECONDS, mFileQueue,new ThreadPoolExecutor.DiscardOldestPolicy());
	}
	
	public static BitmapLoader getInstance(){
		return sBitmapLoader;
	}
	
	public boolean isInit(){
		return mIsInit;
	}
	
	public Bitmap getBitmap(String url){
		checkInit();
		// from memory
		Bitmap bitmap = mConfiguration.mMemoryCache.get(url);	
		if(null == bitmap || bitmap.isRecycled()){
			return null;
		}
		return bitmap;
	}
	
	public Bitmap getBitmapFromFile(String url){
		checkInit();
		Bitmap bitmap = mConfiguration.mFileCaceh.get(url);
		if(null != bitmap && !bitmap.isRecycled()){
			put(url, bitmap);
		}
		return bitmap;
	}
	
	/**
	 * only put it to cache- -
	 * @param url
	 */
	public void reqBitmap(String url){
		reqBitmap(url, null, null);
	}
	
	public void reqBitmap(String url, OnLoadOverListener listener){
		reqBitmap(url, listener, null);
	}
	
	public void reqBitmap(String url, OnLoadOverListener listener, BaseHandleBitmap handleBitmap){
		reqBitmap(url, listener, handleBitmap, null);
	}
	
	public void reqBitmap(final String url, final OnLoadOverListener listener, 
			final BaseHandleBitmap handleBitmap, final Options options){
		checkInit();
		if(url == null || url.length() == 0){
			return;
		}
		
//		if(mReqingUrls.contains(url)){
//			Debug.d("***** is in contain url ***** the url is >> " + url);
//			return;
//		}
		final BitmapCacheHandler handler = new BitmapCacheHandler(listener);
		mReqingUrls.add(url);
	
		// from file
		mFileExecutor.execute(new Runnable() {
			@Override
			public void run(){
				Bitmap bitmap = mConfiguration.mFileCaceh.get(url);
				if(null == bitmap){
					// form net
					mNetExecutor.execute(new Runnable() {
						@Override
						public void run() {
							Bitmap bitmapNet = getBitmapFromNet(url, options);
							sendMessage(bitmapNet, handler, url, handleBitmap);
						}
					});
				}else{
					sendMessage(bitmap, handler, url, handleBitmap);
				}
			}
		});
	}
	
	
	public void recycleAll(){
//		mBitmapMemoryPool.freeAll();
	}
	
	public void clearFile(){
		mConfiguration.mFileCaceh.clear();
	}
	
	private void checkInit(){
		if(mConfiguration == null) {
			throw new IllegalStateException("not init");
		}
	}
	
	private void sendMessage(Bitmap bitmap, Handler handler, String url, BaseHandleBitmap handleBitmap){
		if(null != handleBitmap){
			bitmap = handleBitmap.handleBitmap(bitmap);
		}
		mConfiguration.mFileCaceh.put(url, bitmap, CompressFormat.PNG);
		Message msg = handler.obtainMessage();
		msg.obj = bitmap;
		Bundle bundle = new Bundle();
		bundle.putString(URL_KEY, url);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}
	
	private void put(String url, Bitmap bitmap){
		mConfiguration.mMemoryCache.put(url, bitmap);
	}
	
	private void removeReqUrl(String url){
		mReqingUrls.remove(url);
	}
	
	private Bitmap getBitmapFromNet(String strUrl, Options options) {
		
		if(null == options){
			options = new BitmapFactory.Options();
			options.inDither = false;
			options.inPurgeable = true;
			options.inSampleSize = 0;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;	
		}
		Bitmap bitmap = null;
		try {
			HttpGet httpGet = new HttpGet(strUrl);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(httpGet);
			InputStream is = response.getEntity().getContent();
			bitmap = BitmapFactory.decodeStream(is, null, options);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public String dump(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("=====");
		buffer.append(TAG);
		buffer.append("=====\n");
		buffer.append("the reqing url size ");
		buffer.append(mReqingUrls.size());
		buffer.append("\n");
		// memory
//		buffer.append(mBitmapMemoryPool.dump());
		// file
		return buffer.toString();
	}
}
