package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.R;
import com.room.Global.TextType;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PDeadWoman extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzleDeadWoman);
		setBackgroundImage(R.drawable.puzzle_deadwoman);
	}	
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
	}
	
}
