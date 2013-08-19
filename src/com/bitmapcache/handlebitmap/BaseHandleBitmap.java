package com.bitmapcache.handlebitmap;

import android.graphics.Bitmap;


/**
 *  对图片做处理, 圆角, 缩放, 裁剪
 * @author 6a209
 * Aug 6, 2013
 */
public abstract class BaseHandleBitmap {
	
	public abstract Bitmap handleBitmap(Bitmap bitmap);
}