package com.mogujie.memory;
import android.graphics.Bitmap;


/**
 * override the size of and entryRemove - -
 * @author 6a209
 * Jul 31, 2013
 * @param <K>
 * @param <V>
 */
public class BitmapLruCache<K, V> extends LruCache<K, V>{

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