package com.room.utils;

import com.room.Global;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class UBitmapUtil {
	private static final UPair<Integer, Integer> LOW_RES = new UPair<Integer, Integer>(480, 320); //TODO: right value?
	private static final UPair<Integer, Integer> MED_RES = new UPair<Integer, Integer>(720, 480); //480p
	
	private static Resources res = Global.mainActivity.getResources();
	
	public static Bitmap loadScaledBitmap(int resID, int width, int height)
	{
		return Bitmap.createScaledBitmap(
				BitmapFactory.decodeResource(res, resID),
				width, height, true);
	}
	
	public static Bitmap decodeSampledBitmapForResolution(int resID, Global.ResType type) {
		switch (type) {
		case LOW_RES:
			return decodeSampledBitmapFromResource(res, resID, LOW_RES.getLeft(), LOW_RES.getRight());
			
		case MED_RES:
			return decodeSampledBitmapFromResource(res, resID, MED_RES.getLeft(), MED_RES.getRight());
			
		case HI_RES:
			// keep original resolution.
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			return BitmapFactory.decodeResource(res, resID,options);	
		}
		return null;
	}
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	
	// Scale and keep aspect ratio 
    static public Bitmap scaleToFitWidth(Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), false);
    }

    // Scale and keep aspect ratio     
    static public Bitmap scaleToFitHeight(Bitmap b, int height) {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, false);  
    }

    // Scale and keep aspect ratio 
    static public Bitmap scaleToFill(Bitmap b, int width, int height) {
        float factorH = height / (float) b.getHeight();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse), (int) (b.getHeight() * factorToUse), false);  
    }

    // Scale and don't keep aspect ratio
    static public Bitmap strechToFill(Bitmap b, int width, int height) {
        float factorH = height / (float) b.getHeight();
        float factorW = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorW), (int) (b.getHeight() * factorH), false);  
    }

}
