package com.bitmapcache.core;

import com.bitmapcache.disc.IFileCache;
import com.bitmapcache.disc.UnLimitFileCache;
import com.bitmapcache.memory.BitmapLruMemoryCache;
import com.bitmapcache.memory.IMemoryCache;


public class Configuration {
	
	final int mMemoryCacheSize;
	final String mBitmapCacheDir;
	final IMemoryCache mMemoryCache;
	final IFileCache mFileCaceh;
	final int mFileThreadCount;
	final int mNetThreadCount;
	final boolean mEnableLog;
	
	public Configuration(Builder builder){
		mMemoryCacheSize = builder.mMemoryCacheSize;
		mBitmapCacheDir = builder.mBitmapCacheDir;
		mMemoryCache = builder.mMemoryCache;
		mFileCaceh = builder.mFileCache;
		mFileThreadCount = builder.mFileCacheThreadCount;
		mNetThreadCount = builder.mNetThreadCount;
		mEnableLog = builder.mEnableLog;
		
	}
	
	public static class Builder{
		
		private int mMemoryCacheSize = 1024 * 1024 * 4;
		private String mBitmapCacheDir = "bitmapcache";
		private int mFileCacheThreadCount = 3;
		private int mNetThreadCount = 3;
		private boolean mEnableLog = false;
		private IMemoryCache mMemoryCache = new BitmapLruMemoryCache(mMemoryCacheSize);
		private IFileCache mFileCache = UnLimitFileCache.instance(mBitmapCacheDir);



		public Builder memoeryCacheSize(int size){
			mMemoryCacheSize = size;
			return this;
		}
		
		public Builder bitmapCacheDir(String dir){
			mBitmapCacheDir = dir;
			return this;
		}
		
		public Builder memoryPool(IMemoryCache pool){
			mMemoryCache = pool;
			return this;
		}
		
		public Builder fileCacheThreadCount(int count){
			mFileCacheThreadCount = count;
			return this;
		}
		
		public Builder memoryThreadCount(int count){
			mNetThreadCount = count;
			return this;
		}

        public Configuration builder(){
            return  new Configuration(this);
        }
	}
}