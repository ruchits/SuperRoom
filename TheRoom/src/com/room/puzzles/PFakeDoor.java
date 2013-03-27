package com.room.puzzles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.Global.TextType;
import com.room.R;
import com.room.media.MPeepholeVideoActivity;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PFakeDoor extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzleFakeDoor);
		setBackgroundImage(R.drawable.puzzle_door_fake);
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{
		if(box.name.equals("hole"))
		{
			startActivity(new Intent(this,MPeepholeVideoActivity.class));
			return;
		}		
		
		Log.d("BOXCLICK",box.name);		
		setText(box.desc,TextType.TEXT_SUBTITLE,true);
	}
}
