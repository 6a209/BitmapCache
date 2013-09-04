package com.bitmapcache.memory;


import android.graphics.Bitmap;

import java.lang.ref.SoftReference;

/**
 * 图片池, 按内存大小存储，
 * 当图片池超过的时候，会删掉最先使用的~
 * @author 6a209
 */
public class BitmapLruMemoryCache implements IMemoryCache{

	private BitmapLruCache<String, Bitmap > mLruCache;
	
	public BitmapLruMemoryCache(int size){
		mLruCache = new BitmapLruCache<String, Bitmap>(size);
	}
	
	public int getMaxMemorySize(){
		return mLruCache.maxSize();
	}
	
	@Override
	public Bitmap get(String url){
		if(null == url || 0 == url.length()){
			return null;
		}
		Bitmap bitmap = mLruCache.get(url);
		return bitmap;
	}
	
	@Override
	public void put(String url, Bitmap bitmap){
		if(null != bitmap){
			mLruCache.put(url, bitmap);
		}
	}
	
	/**
	 * free all memory
	 */
	@Override
	public void clear(){
		mLruCache.evictAll();
	}


    public static class BitmapLruCache<K, V> extends LruCache<K, V>{

		public BitmapLruCache(int maxSize) {
			super(maxSize);
		}

		@Override
		protected int sizeOf(K key, V v) {
			Bitmap value = (Bitmap)v;
			return value.getRowBytes() * value.getHeight();
		}

		@Override
		protected void entryRemoved(boolean evicted, K key, V old, V newValue){
			Bitmap oldValue = (Bitmap)old;
			oldValue.recycle();
		}
}
}