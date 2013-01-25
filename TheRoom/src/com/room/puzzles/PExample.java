package com.room.puzzles;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import com.room.R;
import com.room.Global;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PExample extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//MUST specify a layout:
		this.layout = SLayoutLoader.getInstance().puzzleExample;
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{		
		//This is just an example, we should actually preallocate these vars:
		Resources res = Global.mainActivity.getResources();
		Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.puzzle_example);
		Paint paint = new Paint();
		Rect src = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
		Rect dest = new Rect(0,0,Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT);
		canvas.drawBitmap(bitmap,src,dest,paint);
	}    
    
	@Override
    public void onBoxTouched(String boxName)
    {	
    	Log.d("BOXCLICK",boxName);
    }
}
