package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.Global;
import com.room.R;
import com.room.Global.TextType;
import com.room.days.Day1;
import com.room.item.IItemManager;
import com.room.item.IItemManager.Item;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PUrn extends SSceneActivity
{	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzleUrn);
		setBackgroundImage(R.drawable.puzzle_urn);
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
		setText(box.desc,TextType.TEXT_SUBTITLE,true);
		
		if(Global.getCurrentDay() == 1)				
		{
			if(!Day1.hasPickedUpKnife)
			{
				IItemManager.getInstance().addItemToInventory("knife");
				showItemPickUpModal("knife");		
				Day1.hasPickedUpKnife = true;
			}
		}
	}
}
