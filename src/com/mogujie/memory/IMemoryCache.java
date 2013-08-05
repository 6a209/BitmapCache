package com.mogujie.memory;

import android.graphics.Bitmap;



public interface IMemoryCache {
	
	public Bitmap get(String url);
	public void put(String url, Bitmap bitmap);
	public void clear();
}