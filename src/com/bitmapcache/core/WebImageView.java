package com.bitmapcache.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
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
    String mUrl;
	
	public WebImageView(Context context){
		this(context, null);
	}
	
	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    @Override
    public void onDraw(Canvas canvs){
         Drawable drawable = getDrawable();
        if(null == drawable){
            return;
        }
        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bd = (BitmapDrawable)drawable;
            Bitmap bitmap = bd.getBitmap();
            if(bitmap.isRecycled()){
               setImageUrl(mUrl);
               return;
            }
        }
        super.onDraw(canvs);
    }
	
	public void setDefaultImage(int resId){
		mDefaultDrawable = getResources().getDrawable(resId);
	}
	
	public void setImageUrl(String url){
		setImageUrl(url, -1, null);
	}

	public void setImageUrl(String url, int stubResId){
		setImageUrl(url, stubResId, null);
	}
	
	public void setImageUrl(String url, BaseHandleBitmap handleBitmap){
		setImageUrl(url, -1, handleBitmap);
	}
	
	protected void setImageUrl(String url, int stubId, BaseHandleBitmap handleBitmap){
        mUrl = url;
        if(-1 != stubId){

            Drawable drawable = getResources().getDrawable(stubId);
            if(null != drawable){
                mDefaultDrawable = drawable;
            }
        }

		BitmapLoader loader = BitmapLoader.instance();
		Bitmap bitmap = loader.getBitmap(url);
		if(null == bitmap || bitmap.isRecycled()){
			if(null != mDefaultDrawable){
				setImageDrawable(mDefaultDrawable);
			}
			loader.reqBitmap(url, new OnLoadOverListener() {
				@Override
				public void onLoadOver(String url, Bitmap bitmap) {
					if(null != bitmap && url.equals(mUrl)){
						setImageBitmap(bitmap);
					}					
				}
			}, handleBitmap);
		}else{
			setImageBitmap(bitmap);
		}
	}
}