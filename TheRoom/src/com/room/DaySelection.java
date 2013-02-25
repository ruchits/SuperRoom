package com.room;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import com.room.Global;
import com.room.media.MSoundManager;
import com.room.media.MVideoActivity;
import com.room.render.RModelLoader;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UTransitionUtil;

public class DaySelection extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().daySelection);
		setBackgroundImage(R.drawable.day_selection);
		
		if(Global.DEBUG_SKIP_MENU)
        	startGame(Global.getCurrentDay(), true);
	}
    
	@Override
    public void onBoxTouched(String boxName)
    {	
		MSoundManager.getInstance().playSoundEffect(R.raw.swords);
		
		int dayNum = Integer.parseInt(boxName);
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
