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
import android.view.MotionEvent;

import com.room.Global;
import com.room.item.IItems;
import com.room.media.MSoundManager;
import com.room.media.MVideoActivity;
import com.room.puzzles.PFlood;
import com.room.puzzles.PPhone;
import com.room.puzzles.PStatues;
import com.room.scene.SLayout;
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
  		    startActivity(new Intent(this, PStatues.class)); //TEMPORARY
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
	
	// Test function to populate Item Menu.
	private void addToItemMenu() {
		// Create mock items first.
		IItems.Item key = new IItems.Item("Key", "This item can be used to unlock doors.", R.drawable.key_icon);
		IItems.Item knife = new IItems.Item("Knife", "A sharp object for sharp people. Use it with caution!", R.drawable.knife_icon);
		IItems.Item phone = new IItems.Item("Phone", "This will save your ass when in need. Go easy on the battery!", R.drawable.phone_icon);
		IItems.Item book = new IItems.Item("Book", "A book for one of the puzzles. Not yet figured out it's use, btu makes for a good prop.", R.drawable.book_icon);
		IItems.Item notepad = new IItems.Item("Notepad", "No idea what this is doing here, looked like a cool icon.", R.drawable.notepad_icon);
		IItems.Item painting = new IItems.Item("Painting", "Beautiful family - had to put this one in.", R.drawable.painting_icon);
		IItems.getInstance().addItem(key);
		IItems.getInstance().addItem(knife);
		IItems.getInstance().addItem(phone);
		IItems.getInstance().addItem(book);
		IItems.getInstance().addItem(notepad);
		IItems.getInstance().addItem(painting);
	}
	private void clearItemMenu() {
		IItems.getInstance().removeItem("Key");
		IItems.getInstance().removeItem("Knife");
		IItems.getInstance().removeItem("Phone");
		IItems.getInstance().removeItem("Book");
		IItems.getInstance().removeItem("Notepad");
		IItems.getInstance().removeItem("Painting");
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
