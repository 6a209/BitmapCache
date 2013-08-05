package com.bitmapcache.disc;

import java.io.File;
import java.io.FileFilter;

import android.util.Log;


/**
 * delete the old file by time 
 * @author 6a209
 * 2:34:02 PM Dec 13, 2012
 */
public class DelFileByTime extends BaseDelFileMethod{

	static final int ONE_DAY = 24 * 60 * 60 * 2;
	static final int MAX_DEL = 200;

	private int mMaxDel = MAX_DEL;
	private long mTimeDuration = ONE_DAY * 2;
	
	/**
	 * use default
	 */
	public DelFileByTime(){
		
	}
	
	/**
	 * 
	 * @param maxDel the max value
	 * @param duration a millisecond value
	 */
	public DelFileByTime(int maxDel, long duration){
		mMaxDel = maxDel;
		mTimeDuration = duration;
	}
	
	@Override
	public void delFile(final String dir) {
		if(!hasSDCard()){
			return;
		}
		new Thread(){
			@Override
			public void run(){
				File file = new File(dir);
				File [] list = file.listFiles(new TimeFlitter());
				if(null != list){
					int max = Math.min(mMaxDel, list.length);
					for(int i = 0; i < max; i++){
						list[i].delete();
					}
				}
			}
		}.start();
	}
	
	
	/**
	 * filter the file before the mTimeDuration
	 * @author 6a209
	 * 上午10:16:22 2012-1-12
	 */
	private class TimeFlitter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			long modified = pathname.lastModified();
			long curTime = System.currentTimeMillis();
			if(curTime - modified > mTimeDuration){
				return true;
			}
			return false;
		}
	}
	

	
}