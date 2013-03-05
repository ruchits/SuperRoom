package com.room.puzzles;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.R;
import com.room.Global;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PDeadMan extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzleDeadMan);
		setBackgroundImage(R.drawable.puzzle_deadman);
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
	}
}
