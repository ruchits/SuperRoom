package com.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.room.Global;
import com.room.media.MSoundManager;
import com.room.media.MVideoActivity;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class DaySelection extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().daySelection);
		
		showInventory(false);
		showBackButton(false);		
		
		if(Global.DEBUG_SKIP_MENU)
        	startGame(Global.getCurrentDay(), true);
	}
    
	@Override
    public void onBoxDown(SLayout.Box box, MotionEvent event)
    {	
		MSoundManager.getInstance().playSoundEffect(R.raw.swords);
		
		int dayNum = Integer.parseInt(box.name);
		startGame(dayNum, false);    	
    }
	
	private void startGame(int dayNum, boolean skipVideo)
	{
		if(skipVideo)
		{
			MVideoActivity.videoToPlay = null;
		}		
		else switch(dayNum)
		{
			case 1:
				MVideoActivity.videoToPlay = MVideoActivity.DAY1_VIDEO;		
			break;
			case 2:
				MVideoActivity.videoToPlay = MVideoActivity.DAY2_VIDEO;		
			break;
			case 3:
				MVideoActivity.videoToPlay = MVideoActivity.DAY3_VIDEO;		
			break;
			case 4:
				MVideoActivity.videoToPlay = MVideoActivity.DAY4_VIDEO;		
			break;
			case 5:
				MVideoActivity.videoToPlay = MVideoActivity.DAY5_VIDEO;		
			break;		
		}
		
		Global.setDay(dayNum);
		
		Intent intent = new Intent(this, MVideoActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		setBackgroundImage(R.drawable.day_selection);
		MSoundManager.getInstance().playMusic(R.raw.music_menu);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		// TODO: Stop music_meny sound effect from MSoundManager. API missing!!
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onDestroy()
	{		
		super.onDestroy();
	}
}
