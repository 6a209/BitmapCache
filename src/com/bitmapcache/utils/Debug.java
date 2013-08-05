package com.bitmapcache.utils;

import android.util.Log;


/**
 * just a debug
 * @author 6a209
 * Aug 2, 2013
 */
public class Debug{

	public static boolean sIsDebug = true;
	public static void d(String msg){
		if(sIsDebug){
			Log.d("bitmapcache", msg);
		}
	}
}