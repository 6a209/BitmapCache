package com.bitmapcache.handlebitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class CircleBitmap extends BaseHandleBitmap{

	@Override
	public Bitmap handleBitmap(Bitmap bitmap) {
		
		if (null == bitmap)
			return null;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		if (w <= 0 || h <= 0) {
			return null;
		}
		int r = (w < h ? w : h) / 2;
		
		Bitmap circle = Bitmap.createBitmap(2 * r, 2 * r, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(circle);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.RED);
		canvas.drawCircle(r, r, r, p);
		p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		// 生成结果图片
		Bitmap out = Bitmap.createBitmap(2 * r, 2 * r, Bitmap.Config.ARGB_8888);
		Canvas outCan = new Canvas(out);
		outCan.drawBitmap(bitmap, 0, 0, null);
		outCan.drawBitmap(circle, 0, 0, p);
		circle.recycle();
		bitmap.recycle();
		
		return out;
	}
	
}