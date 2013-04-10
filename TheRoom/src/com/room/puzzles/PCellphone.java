package com.room.puzzles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.Global;
import com.room.Global.TextType;
import com.room.OptionManager;
import com.room.R;
import com.room.item.IItemMenu;
import com.room.media.MSoundManager;
import com.room.media.MVideoActivity;
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
		
		if(Global.getCurrentDay() == 1)
		{
			setForegroundImage(R.drawable.puzzle_cellphone_foreground);	
			setBackgroundImage(R.drawable.puzzle_cellphone);
		}
		else
		{
			setBackgroundImage(R.drawable.puzzle_cellphone_cracked);
		}
		
		showInventory(false);
		pattern = new boolean[9];
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		Log.d("BOXCLICK",box.name);
		
		if(Global.getCurrentDay() != 1)
		{
			setText("It's no longer operational.", TextType.TEXT_SUBTITLE, true);
			return;
		}
		
		MSoundManager.getInstance().playSoundEffect(R.raw.tick);
		
		if(box.name.equals("ok"))
		{
			if(isValidPattern())
			{
				//temp - we shouldnt need to deselect the item manually!
				IItemMenu.itemInUse = null;
				
				//end day 1
				Global.setDay(2);
				if ( OptionManager.getHighestStage() < 2 ) {
					 OptionManager.setHighestStage(2);
				}
				MVideoActivity.videoToPlay = MVideoActivity.DAY2_VIDEO;
				Intent intent = new Intent(this,MVideoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(intent);
		        
		        //do we need to call finish here?
				finish();
			}
			else
			{
				resetPattern();
			}
		}		
		
		int keyNum = -1;
		try{keyNum = Integer.parseInt(box.name);}
		catch(Exception e){}				
		
		if(keyNum!=-1)	
		{						
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
			return;
		}	
	}
	
	private void resetPattern()
	{
		for(int i=0; i<pattern.length; ++i)
			pattern[i] = false;
		resetForegroundImage();
	}
	
	private boolean isValidPattern()
	{
		return
			pattern[0] == true &&
			pattern[1] == false &&
			pattern[2] == true &&
			pattern[3] == true &&
			pattern[4] == true &&
			pattern[5] == false &&
			pattern[6] == false &&
			pattern[7] == false &&
			pattern[8] == true;
	}
	
	boolean pattern[];
}
