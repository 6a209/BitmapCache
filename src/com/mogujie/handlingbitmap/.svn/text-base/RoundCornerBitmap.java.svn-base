package com.mogujie.handlingbitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;


/**
 * 生成圆角图片
 * @author 6a209
 * 12:09:30 PM Dec 14, 2012
 */
public class RoundCornerBitmap extends BaseHandlingBitmap{

	private static final int DEFAULT_CORNER_SIZE = 10;
	private int mCornerSize = DEFAULT_CORNER_SIZE;
	
	public void setCornerSize(int cornerSize){
		mCornerSize = cornerSize;
	}
	
	@Override
	public Bitmap handlingBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		if (null == bitmap)
			return null;
		int w = bitmap.getWidth(), h = bitmap.getHeight();
		if (w <= 0 || h <= 0) {
			return null;
		}
		
		// 生成圆角
		Bitmap rounder = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(rounder);
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setColor(Color.RED);
		canvas.drawRoundRect(new RectF(0, 0, w, h), mCornerSize, mCornerSize, p);
		p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		// 生成结果图片
		Bitmap out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas outCan = new Canvas(out);
		outCan.drawBitmap(bitmap, 0, 0, null);
		outCan.drawBitmap(rounder, 0, 0, p);
		bitmap.recycle();
		return out;
	}
	
}