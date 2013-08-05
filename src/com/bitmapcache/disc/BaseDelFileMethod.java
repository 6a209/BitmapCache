package com.bitmapcache.disc;

import java.io.File;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;


public abstract class BaseDelFileMethod{
	
	protected Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(null != mOnFinishListener){
				mOnFinishListener.onFinish();
			}
		}
	};
	
	public interface OnFinishListener{
		void onFinish();
	}
	
	OnFinishListener mOnFinishListener;
	
	
	public void setOnFinishListener(OnFinishListener l){
		mOnFinishListener = l;
	}
	
	public abstract void delFile(String dir);
	
	public void delAllFiles(final String dir){
		if(!hasSDCard()){
			return;
		}
		new Thread(){
			@Override
			public void run(){
				File file = new File(dir);
				File [] list = file.listFiles();
				if(null != list){
					for(int i = 0; i < list.length; i++){
						list[i].delete();
					}
				}
			}
		}.start();
		
	}
	
	protected boolean hasSDCard(){
		return Environment.getExternalStorageState().equals(
			android.os.Environment.MEDIA_MOUNTED);
	}
}