package com.room.scene;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.room.Global;
import com.room.utils.UPair;


public class SSceneBitmapProvider {
	private static Queue <UPair<Integer, Bitmap>> sPool;
	private static int POOL_SIZE = 2;
	private static BitmapFactory.Options sOptions = new BitmapFactory.Options();

	private static SSceneBitmapProvider instance = null;
	public static SSceneBitmapProvider getInstance() {
		if(instance == null) {
			instance = new SSceneBitmapProvider();
		}
		return instance;
	}
	
	public void init() {
		sPool = new LinkedList<UPair<Integer, Bitmap>>();
	}
	
	public Bitmap decodeImage(int resourceID) {
		if(sPool != null) {
			Iterator<UPair<Integer, Bitmap>> it = sPool.iterator();
			while(it.hasNext()) {
				UPair<Integer, Bitmap> bm = (UPair<Integer, Bitmap>) it.next();
				if(bm.getLeft() == resourceID) {
					return bm.getRight();
				}
			}
		}
		
		// Remove an entry if size exceeds
		int poolSize = sPool.size();
		if (poolSize == POOL_SIZE) {
			// remove the first entry.
			UPair<Integer, Bitmap> entry = sPool.remove();
			entry.getRight().recycle();
		}
		
		sOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		UPair<Integer, Bitmap> entry = new UPair<Integer, Bitmap>(resourceID, BitmapFactory.decodeResource(Global.mainActivity.getResources(), resourceID, sOptions));
		sPool.add(entry);
		return entry.getRight();
	}
}
