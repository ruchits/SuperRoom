package com.room.utils;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class UBitmapPool {
	private static final String TAG = "com.room.utils.UBitmapPool";

    private final ArrayList<Bitmap> bPool;
    private final int bPoolLimit;

    // pool can only provide Bitmap of one size.
    // TBD: Do we need to support multiple sizes in the same pool?
    private final int bWidth, bHeight;

    // Construct a BitmapPool which creates bitmap with the specified size.
    public UBitmapPool(int width, int height, int poolLimit) {
        bWidth = width;
        bHeight = height;
        bPoolLimit = poolLimit;
        bPool = new ArrayList<Bitmap>(poolLimit);
    }

    // Construct a BitmapPool which caches bitmap with any size;
    public UBitmapPool(int poolLimit) {
        bWidth = -1;
        bHeight = -1;
        bPoolLimit = poolLimit;
        bPool = new ArrayList<Bitmap>(poolLimit);
    }

    // Get a Bitmap from the pool.
    public synchronized Bitmap getBitmap() {
        int size = bPool.size();
        return size > 0 ? bPool.remove(size - 1) : null;
    }

    // Get a Bitmap from the pool with the specified size.
    public synchronized Bitmap getBitmap(int width, int height) {
        for (int i = 0; i < bPool.size(); i++) {
            Bitmap b = bPool.get(i);
            if (b.getWidth() == width && b.getHeight() == height) {
                return bPool.remove(i);
            }
        }
        return null;
    }

    // Put a Bitmap into the pool, if the Bitmap has a proper size. Otherwise
    // the Bitmap will be recycled. If the pool is full, an old Bitmap will be
    // recycled.
    public void returnBitmap(Bitmap bitmap) {
        if (bitmap == null) return;
        if ((bitmap.getWidth() != bWidth) ||
                (bitmap.getHeight() != bHeight)) {
            bitmap.recycle();
            return;
        }
        synchronized (this) {
            if (bPool.size() >= bPoolLimit) bPool.remove(0);
            bPool.add(bitmap);
        }
    }

    public synchronized void clear() {
        bPool.clear();
    }

}

