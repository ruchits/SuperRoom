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
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;

public class PUrn extends SSceneActivity
{	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		knifeBmp = UBitmapUtil.loadBitmap(R.drawable.puzzle_urn_knife, true);		
		
		setLayout(SLayoutLoader.getInstance().puzzleUrn);
		setBackgroundImage(R.drawable.puzzle_urn);		
		
		if(Global.getCurrentDay() == 1)
		{
			if(!Day1.hasPickedUpKnife)
			{
				mapBoxBitmap("knife", knifeBmp);
			}
		}
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
				
		if(Global.getCurrentDay() == 1)				
		{
			if(box.name.equals("knife") && !Day1.hasPickedUpKnife)
			{
				IItemManager.getInstance().addItemToInventory("knife");
				showItemPickUpModal("knife");	
				upmapBoxBitmap("knife");
				Day1.hasPickedUpKnife = true;
				return;
			}
		}

		setText(box.desc,TextType.TEXT_SUBTITLE,true);
		MSoundManager.getInstance().playSoundEffect(R.raw.tick);
	}
	
	private Bitmap knifeBmp;
	
}
