package com.room.scene;

import android.os.Bundle;
import android.view.MotionEvent;

import com.room.Global;
import com.room.Global.TextType;
import com.room.R;
import com.room.media.MSoundManager;
import com.room.render.RRenderer;


public class STextScene extends SSceneActivity
{
	private String[] text;
	private int currentText;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().textScene);
		showInventory(false);
		showBackButton(false);		
		
		currentText = 0;
		
		switch(Global.getCurrentDay())
		{
		case 1:
			text = new String[]{"Ughh... was I drinking last night?","... where am I?"};
			break;
		case 2:
			text = new String[]{"What...","What is this place?","My stomach is burning... ughh..."};
			break;
		case 3:
			text = new String[]{"I can't remember how I got here...","What day is it?","The searing pain from my abdomen continues to grow,","yet I see nor feel any wound."};
			break;			
		case 4:
			text = new String[]{"The air is getting heavier in here.","I better get out...","...before my guts spill out of me."};
			break;
		case 5:
			text = new String[]{"I can't stand this any longer.","I'm losing my mind.","The pain is unbearable...","..."};
			break;			
		}
		
		setText(text[currentText],TextType.TEXT_SUBTITLE,false);
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
	{	
		MSoundManager.getInstance().playSoundEffect(R.raw.tick);
		
		++currentText;
		
		if(currentText>=text.length)
		{
			finish();
			return;
		}
		
		setText(text[currentText],TextType.TEXT_SUBTITLE,true);
		
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		RRenderer.getInstance().setVisible(true);
		RRenderer.getInstance().resetFlashlightHeight();
	}
}
