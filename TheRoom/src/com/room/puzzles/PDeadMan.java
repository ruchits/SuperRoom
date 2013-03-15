package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.Global;
import com.room.R;
import com.room.Global.TextType;
import com.room.days.Day1;
import com.room.item.IItemMenu;
import com.room.item.IItems.Item;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PDeadMan extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		updateLayoutAndBackground();
	}
	
	private void updateLayoutAndBackground()
	{
		if(Global.getCurrentDay()==1 && !Day1.isDeadManRevealed)
		{
			setBackgroundImage(R.drawable.puzzle_deadman_covered);
			setLayout(SLayoutLoader.getInstance().puzzleDeadManCovered);			
		}
		else
		{
			setBackgroundImage(R.drawable.puzzle_deadman);
			setLayout(SLayoutLoader.getInstance().puzzleDeadMan);
		}
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
		
		if(box.name.equals("cloth"))
		{
			if(!Day1.hasDeadManCoverBeenExamined)
			{
				Day1.hasDeadManCoverBeenExamined = true;
				setText(box.desc,TextType.TEXT_SUBTITLE,true);
			}			
			else
			{
				Day1.isDeadManRevealed = true;
				setText("",TextType.TEXT_SUBTITLE,true);
				MSoundManager.getInstance().playLongSoundEffect(R.raw.deadman_reveal_breathing, false);
				updateLayoutAndBackground();
			}
		}
		else
		{
			setText(box.desc,TextType.TEXT_SUBTITLE,true);
		}	
	}
	
	@Override
	public void onBoxDownWithItemSel(SLayout.Box box, MotionEvent event) {
		Item itemInUse = IItemMenu.itemInUse;
		if (box.name.equals("body") && itemInUse.getName().equals("knife")) {
			// TODO: Open up the body, and retrieve the phone
			
			notifyItemused(); // notify ItemMeny that item has been used.
		}
		else {
			setText(DEFAULT_ITEMUSE_TEXT, TextType.TEXT_SUBTITLE, true);
		}
	}
	
}
