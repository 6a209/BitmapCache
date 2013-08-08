package com.bitmapcache.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bitmapcache.core.BitmapLoader.OnLoadOverListener;

/**
 * the bitmap from web
 * @author 6a209
 * Aug 6, 2013
 */
public class WebImageView extends ImageView{

	public WebImageView(Context context){
		this(context, null);
	}
	
	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setImageUrl(String url){
		BitmapLoader loader = BitmapLoader.instance();
		Bitmap bitmap = loader.getBitmap(url);
		if(null == bitmap || bitmap.isRecycled()){
			loader.reqBitmap(url, new OnLoadOverListener() {
				
				@Override
				public void onLoadOver(String url, Bitmap bitmap) {
					if(null != bitmap){
						setImageBitmap(bitmap);
					}					
				}
			});
		}
	}
	
}