package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.Global.TextType;
import com.room.Global;
import com.room.R;
import com.room.days.Day1;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PPhone extends SSceneActivity
{
	String dialedNumber;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzlePhone);
		setBackgroundImage(R.drawable.puzzle_phone);
		setForegroundImage(R.drawable.puzzle_phone_foreground);
		
		MSoundManager.getInstance().removeLocationSensitiveSound(R.raw.deadman_cellphone);
		Day1.isCellphoneRinging = false;
		
		dialedNumber = ""; 
	}
	
	@Override	
	protected void onResume()
	{
		super.onResume();
		if(Global.getCurrentDay() == 1)
		{			
			if(Day1.isCellphoneRinging)
				MSoundManager.getInstance().playLongSoundEffect(R.raw.phone_ring,true);
			else if(dialedNumber.length()>=5)
				MSoundManager.getInstance().playLongSoundEffect(R.raw.phone_busy,true);
			else
				MSoundManager.getInstance().playLongSoundEffect(R.raw.phone_dialtone,true);			
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MSoundManager.getInstance().stopLongSoundEffect();
	}	
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
    {	
    	Log.d("BOXCLICK",box.name);
    	
    	if(Global.getCurrentDay() != 1)
    	{
    		setText("The line has been cut.",TextType.TEXT_SUBTITLE,true);
    		return;
    	}
    	
    	unhideForegroundImage(box.name);
    	
		switch (box.name.charAt(0))
		{
			case '0':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_0);
			break;
			case '1':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_1);
			break;
			case '2':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_2);
			break;
			case '3':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_3);
			break;
			case '4':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_4);
			break;
			case '5':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_5);
			break;
			case '6':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_6);
			break;
			case '7':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_7);
			break;
			case '8':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_8);
			break;
			case '9':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_9);
			break;
			case '*':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_star);
			break;
			case '#':
				MSoundManager.getInstance().playSoundEffect(R.raw.phone_pound);
		}
		
		if(dialedNumber.length()<5)
		{
			dialedNumber+=box.name;
		}
		setText(dialedNumber, TextType.TEXT_SUBTITLE, true);
		
		if(dialedNumber.length() == 5)
			checkNumber();
    }
	
	@Override
	public void onBoxRelease(SLayout.Box box, MotionEvent event)
    {	
		hideForegroundImage(box.name);
    }
	
	public void checkNumber()
	{
		if(dialedNumber.equals("37837"))
		{
			MSoundManager.getInstance().playLongSoundEffect(R.raw.phone_ring,true);
			MSoundManager.getInstance().addLocationSensitiveSound(R.raw.deadman_cellphone, 16, 25, 15, 60);
			Day1.isCellphoneRinging = true;
		}
		else
		{
			MSoundManager.getInstance().playLongSoundEffect(R.raw.phone_busy,true);
		}
	}
	
}
