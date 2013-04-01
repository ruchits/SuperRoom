package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.R;
import com.room.Global.TextType;
import com.room.media.MSoundManager;
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
		
		if(box.name.equals("knob"))
		{
			MSoundManager.getInstance().playLongSoundEffect(R.raw.door_locked, false);
		}
		
		setText(box.desc,TextType.TEXT_SUBTITLE,true);
		MSoundManager.getInstance().playSoundEffect(R.raw.tick);
	}
}
