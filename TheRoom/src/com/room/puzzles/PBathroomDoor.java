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

public class PBathroomDoor extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzleBathroomDoor);
		setBackgroundImage(R.drawable.puzzle_door_bathroom);
	}
	
	@Override
    public void onBoxTouched(String boxName)
    {	
    	Log.d("BOXCLICK",boxName);
    }
}