package com.bitmapcache.disc;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;



/**
 * 图片的文件缓存 - - ！
 * 可以按时间，按文件个数，以及文件大小 设定限制 
 * @author 6a209
 */
public class UnLimitFileCache extends BaseFileCache{
	
	private static final String DEFAULT_DIR = "/Bitmap-Cache";
	private static final int DEFAULT_QUALITY = 75;
	private String mDirPath = Environment.getExternalStorageDirectory() + DEFAULT_DIR;
	private static UnLimitFileCache sBitmapFileCache;
	private int mQuality = DEFAULT_QUALITY;
	
	private BaseDelFileMethod mDelFileMethod;
	
	private UnLimitFileCache(String fileDir){
		super(fileDir);
//		mDirPath =  Environment.getExternalStorageDirectory() + File.separator + sdcardDir;
		mDelFileMethod = new DelFileByTime();
		init(mDirPath);
	}
	
	public static UnLimitFileCache instance(String sdcardDir){
		if(null == sBitmapFileCache){
			if(null == sdcardDir || sdcardDir.length() == 0){
				sdcardDir = DEFAULT_DIR;
			}
			sBitmapFileCache = new UnLimitFileCache(sdcardDir);
		}
		return sBitmapFileCache;
	}
	
	public void setDelFileMethod(BaseDelFileMethod method){
		mDelFileMethod = method;
	}
	
	private void init(String dir){
		File file = new File(dir);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	
	public String getSDCardDir(){
		return mDirPath;
	}
	
	public void delFile(){
		mDelFileMethod.delFile(mDirPath);
	}
	
	public void delAllFiles(){
		mDelFileMethod.delAllFiles(mDirPath);
	}
	
	public void setCompressQuality(int quality){
		mQuality = quality;
	}
	
	public Bitmap get(String url){
		if(null == url || 0 == url.length()){
			return null;
		}
		if(!hasSDCard()){
			return null;
		}
		File file = new File(mDirPath, toMD5(url));
		if(!file.exists()){
			return null;
		}
		return BitmapFactory.decodeFile(mDirPath + File.separator + toMD5(url));
	}
	
	
	public void put(String url, Bitmap bitmap){
		if(null == url || 0 == url.length()){
			return;
		}
		put(url, bitmap, null);
	}
	
	@Override
	public void put(String url, Bitmap bitmap, CompressFormat format){
		File file = new File(mDirPath, toMD5(url));
		if(file.exists() || null == bitmap || bitmap.isRecycled()){
			return;
		}
		try{
			if(null == format){
				format = CompressFormat.JPEG;
			}
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(format, mQuality, fos);
			fos.flush();
			fos.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	boolean hasSDCard(){
		return Environment.getExternalStorageState().equals(
			android.os.Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * url => md5
	 * @param str
	 * @return
	 */
	private String toMD5(String str) {
		if(null == str || 0 == str.length()){
			return null;
		}
		MessageDigest messageDigest = null;
		try{
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		}catch (Exception e){
			e.printStackTrace();
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for(int i = 0; i < byteArray.length; i++){
			if(Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

}
