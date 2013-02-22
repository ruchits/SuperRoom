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

public class PUrn extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzleUrn);
		setBackgroundImage(R.drawable.puzzle_urn);
	}
	
	@Override
    public void onBoxTouched(String boxName)
    {	
    	Log.d("BOXCLICK",boxName);
    }
}
