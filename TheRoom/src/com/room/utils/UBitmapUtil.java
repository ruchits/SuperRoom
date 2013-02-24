package com.room.utils;

import com.room.Global;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class UBitmapUtil {
	
	public static Bitmap loadScaledBitmap(int resID, int width, int height)
	{
		Resources res = Global.mainActivity.getResources();
		return Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(res, resID),
				width, height, true);
	}

}
