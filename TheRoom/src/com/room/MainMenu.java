package com.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.room.Global;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class MainMenu extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().mainMenu);
		
		if(Global.DEBUG_SKIP_MENU)
			showDaySelection();
		
		// hide the scene icons.
		showInventory(false);
		showBackButton(false);
	}
	
    
	@Override
    public void onBoxDown(SLayout.Box box, MotionEvent event)
    {	
		MSoundManager.getInstance().playSoundEffect(R.raw.swords);
    	if (box.name.equals("start"))
    	{
    		showDaySelection();
    	}
    	else if (box.name.equals("options"))
    	{
    		showOptions();
    	}
    	else if (box.name.equals("credits"))
    	{
    		//showCredits();
    	}      	
    	else if (box.name.equals("quit"))
    	{
    		finish();
    	}    	
    }
	
	private void showDaySelection()
	{
		Intent intent = new Intent(this, DaySelection.class);
	    startActivity(intent);
	}
	
	private void showCredits() {
		  Intent intent = new Intent(this, Credits.class);
		  startActivity(intent);
	}
	
	private void showOptions() {
		  Intent intent = new Intent(this, Options.class);
		  startActivity(intent);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		setBackgroundImage(R.drawable.main_menu);
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
		MSoundManager.getInstance().stopAllSounds();
	}
}
