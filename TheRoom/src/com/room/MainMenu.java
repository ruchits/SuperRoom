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
import com.room.item.IItems;
import com.room.media.MSoundManager;
import com.room.media.MVideoActivity;
import com.room.puzzles.PFlood;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UTransitionUtil;

public class MainMenu extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().mainMenu);
		setBackgroundImage(R.drawable.main_menu);
		
		if(Global.DEBUG_SKIP_MENU)
			showDaySelection();
		
		IItems.getInstance().init();
		addToItemMenu(); // test function to populate items menu.
		
		showInventory(true);
		showBackButton(false);
	}
	
    
	@Override
    public void onBoxTouched(String boxName)
    {	
		MSoundManager.getInstance().playSoundEffect(R.raw.swords);
    	if (boxName.equals("start"))
    	{
    		showDaySelection();
    	}
    	else if (boxName.equals("options"))
    	{
    		showOptions();
    	}
    	else if (boxName.equals("credits"))
    	{
  		    startActivity(new Intent(this, PFlood.class)); //TEMPORARY
    		//showCredits();
    	}      	
    	else if (boxName.equals("quit"))
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
	
	// Test function to populate Item Menu.
	private void addToItemMenu() {
		// Create mock items first.
		IItems.Item key = new IItems.Item("Key", "This item can be used to unlock doors.", R.drawable.key_icon);
		IItems.Item knife = new IItems.Item("Knife", "A sharp object for sharp people. Use it with caution!", R.drawable.knife_icon);
		IItems.getInstance().addItem(key);
		IItems.getInstance().addItem(knife);
	}
	private void clearItemMenu() {
		IItems.getInstance().removeItem("Key");
		IItems.getInstance().removeItem("Knife");
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
		MSoundManager.getInstance().stopAllSounds();
	}
}
