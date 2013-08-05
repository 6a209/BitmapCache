package com.bitmapcache.disc;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;


public interface IFileCache {
	
	public void put(String url, Bitmap bitmap, CompressFormat format);
	public void put(String url, Bitmap bitmap);
	public Bitmap get(String url);
	public void clear();
}