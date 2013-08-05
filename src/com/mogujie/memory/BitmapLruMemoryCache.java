package com.mogujie.memory;


import android.graphics.Bitmap;

/**
 * 图片池, 按内存大小存储，
 * 当图片池超过的时候，会删掉最先使用的~
 * @author 6a209
 */
class BitmapLruMemoryCache implements IMemoryCache{

	private BitmapLruCache<String, Bitmap> mLruCache;
	
	private BitmapLruMemoryCache(int size){
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
	
}