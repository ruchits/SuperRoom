package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.R;
import com.room.Global.TextType;
import com.room.scene.SLayout;
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
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
		setText(box.desc,TextType.TEXT_SUBTITLE,true);
	}

}
