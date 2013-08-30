package com.bitmapcache.disc;

import java.io.File;

import android.graphics.Bitmap;


/**
 * the file cache
 * @author 6a209
 * Aug 2, 2013
 */
public abstract class BaseFileCache implements IFileCache{

	protected String mFilePath;
	
	public BaseFileCache(String fileDir){
		if(null == fileDir){
			throw new IllegalArgumentException("fileDir not null");
		}
		File file = new File(fileDir);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public void put(String url, Bitmap bitmap) {
		
	}
	
	@Override
	public Bitmap get(String url) {
		return null;
	}
	
	@Override
	public void clear(){
		final File dir = new File(mFilePath);
		if(!dir.exists()){
			return;
		}
		new Thread(){
			@Override
			public void run(){
				File [] list = dir.listFiles();
				if(null != list){
					for(int i = 0; i < list.length; i++){
						list[i].delete();
					}
				}
			}
		}.start();
	}
	
}