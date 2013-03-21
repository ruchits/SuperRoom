package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.R;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PCellphone extends SSceneActivity
{	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setLayout(SLayoutLoader.getInstance().puzzleCellphone);
		setForegroundImage(R.drawable.puzzle_cellphone_foreground);	
		setBackgroundImage(R.drawable.puzzle_cellphone);		
		showInventory(false);
		pattern = new boolean[9];
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
		
		int keyNum = -1;
		try{keyNum = Integer.parseInt(box.name);}
		catch(Exception e){}
		
		if(keyNum!=-1)	
		{
			MSoundManager.getInstance().playSoundEffect(R.raw.tick);
			
			if(pattern[keyNum]==false)
			{
				unhideForegroundImage(box.name);
				pattern[keyNum]=true;
			}
			else
			{
				hideForegroundImage(box.name);
				pattern[keyNum]=false;
			}			
		}
		
	}
	
	boolean pattern[];
}
