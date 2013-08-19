package com.bitmapcache.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bitmapcache.core.BitmapLoader.OnLoadOverListener;
import com.bitmapcache.handlebitmap.BaseHandleBitmap;

/**
 * the bitmap from web
 * @author 6a209
 * Aug 6, 2013
 */
public class WebImageView extends ImageView{

	Drawable mDefaultDrawable;
	
	public WebImageView(Context context){
		this(context, null);
	}
	
	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setDefaultImage(int resId){
		mDefaultDrawable = getResources().getDrawable(resId);
	}
	
	public void setImageUrl(String url){
		setImageUrl(url, null, null);
	}
	
	public void setIamgeUrl(String url, Bitmap stubBitmap){
		setImageUrl(url, stubBitmap, null);
	}
	
	public void setImageUrl(String url, BaseHandleBitmap handleBitmap){
		setImageUrl(url, null, handleBitmap);
	}
	
	public void setImageUrl(String url, Bitmap stubBitmap, BaseHandleBitmap handleBitmap){
		BitmapLoader loader = BitmapLoader.instance();
		Bitmap bitmap = loader.getBitmap(url);
		if(null == bitmap || bitmap.isRecycled()){
			if(null != mDefaultDrawable){
				setImageDrawable(mDefaultDrawable);
			}
			loader.reqBitmap(url, new OnLoadOverListener() {
				@Override
				public void onLoadOver(String url, Bitmap bitmap) {
					if(null != bitmap){
						setImageBitmap(bitmap);
					}					
				}
			}, handleBitmap);
		}else{
			setImageBitmap(bitmap);
		}
	}
	
	
	
}