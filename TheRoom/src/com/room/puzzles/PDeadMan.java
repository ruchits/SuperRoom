package com.room.puzzles;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.Global;
import com.room.R;
import com.room.Global.TextType;
import com.room.days.Day1;
import com.room.item.IItemManager;
import com.room.item.IItemMenu;
import com.room.item.IItemManager.Item;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;

public class PDeadMan extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
				
		cutBmp = UBitmapUtil.loadBitmap(R.drawable.puzzle_deadman_cut, true);
		phoneBmp = UBitmapUtil.loadBitmap(R.drawable.puzzle_deadman_phone, true);
		
		updateLayoutAndBackground();
	}
	
	private void updateLayoutAndBackground()
	{
		if(Global.getCurrentDay()==1)
		{
			if(!Day1.isDeadManRevealed)
			{
				setBackgroundImage(R.drawable.puzzle_deadman_covered);
				setLayout(SLayoutLoader.getInstance().puzzleDeadManCovered);	
				return;
			}
			else if(Day1.hasPickedUpCellphone)
			{
				setBackgroundImage(R.drawable.puzzle_deadman);
				setLayout(SLayoutLoader.getInstance().puzzleDeadMan);
				mapBoxBitmap("body", cutBmp);
				return;
			}			
			else if(Day1.isCellphoneRevealed)
			{
				setBackgroundImage(R.drawable.puzzle_deadman);
				setLayout(SLayoutLoader.getInstance().puzzleDeadMan);
				mapBoxBitmap("body", phoneBmp);
				return;
			}
			else
			{
				setBackgroundImage(R.drawable.puzzle_deadman);
				setLayout(SLayoutLoader.getInstance().puzzleDeadMan);
				return;
			}			
		}
		else
		{
			setBackgroundImage(R.drawable.puzzle_deadman);
			setLayout(SLayoutLoader.getInstance().puzzleDeadMan);
			mapBoxBitmap("body", cutBmp);
			return;
		}
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
		
		if(Global.getCurrentDay() == 1)				
		{
			if(box.name.equals("cloth"))
			{
				if(!Day1.hasDeadManCoverBeenExamined)
				{
					Day1.hasDeadManCoverBeenExamined = true;
					setText(box.desc,TextType.TEXT_SUBTITLE,true);
					return;
				}			
				else
				{
					Day1.isDeadManRevealed = true;
					setText("",TextType.TEXT_SUBTITLE,true);
					MSoundManager.getInstance().playLongSoundEffect(R.raw.deadman_reveal_breathing, false);
					updateLayoutAndBackground();
					return;
				}
			}
			
			if(box.name.equals("body"))
			{
				if(Day1.isCellphoneRevealed && !Day1.hasPickedUpCellphone)
				{
					IItemManager.getInstance().addItemToInventory("cellphone");
					showItemPickUpModal("cellphone");	
					Day1.hasPickedUpCellphone = true;
					MSoundManager.getInstance().removeLocationSensitiveSound(R.raw.deadman_cellphone);
					Day1.isCellphoneRinging = false;
					updateLayoutAndBackground();
					return;
				}
				else if(Day1.isCellphoneRinging)
				{
					setText("A vibration is coming from his wound.",TextType.TEXT_SUBTITLE,true);
					return;
				}
			}
						
		}
		
		setText(box.desc,TextType.TEXT_SUBTITLE,true);
		MSoundManager.getInstance().playSoundEffect(R.raw.tick);
	
	}
	
	@Override
	public boolean onBoxDownWithItemSel(SLayout.Box box, MotionEvent event)
	{
		Item itemInUse = IItemMenu.itemInUse;
		
		if(Global.getCurrentDay() == 1)				
		{
			if (Day1.isCellphoneRinging && box.name.equals("body") && itemInUse.getID().equals("knife"))
			{
				MSoundManager.getInstance().playLongSoundEffect(R.raw.deadman_cut, false);
				Day1.isCellphoneRevealed = true;
				updateLayoutAndBackground();
				return true;
			}	
		}

		return false;
	}
	
	private Bitmap cutBmp;
	private Bitmap phoneBmp;
	
}
