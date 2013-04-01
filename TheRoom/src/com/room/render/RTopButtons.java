package com.room.render;

import android.content.Intent;
import android.view.MotionEvent;

import com.room.Global;
import com.room.R;
import com.room.media.MSoundManager;


public class RTopButtons
{
	//SINGLETON!!
	public static RTopButtons getInstance()
	{
		if(instance == null)
		{
			instance = new RTopButtons();
		}
		return instance;
	}
	
	private RTopButtons()
	{
		poiImage = new RScreenImage(
				RTextureLoader.getInstance().getTextureID("ui_poi"));
		poiImage.setSize(0.5f);
		poiImage.setPosition(0.8f, 0.7f);
		
		poiImage.setVisible(false);
		
		
		cheatImage = new RScreenImage(
				RTextureLoader.getInstance().getTextureID("ui_cheat"));
		cheatImage.setSize(0.3f);
		cheatImage.setPosition(-0.85f, 0.75f);
		
		cheatImage.setVisible(true);		
	}
	
	public boolean processTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
				
		//only care about DOWN events
		if (actionCode != MotionEvent.ACTION_DOWN)
		{
			return false;
		}
		
		float glX = RMath.pixelToGLX(event.getX());
		float glY = RMath.pixelToGLY(event.getY());
		
		if(poiImage.containsPoint(glX, glY))
		{
			MSoundManager.getInstance().playSoundEffect(R.raw.tick);
			Class c = RPOIManager.getInstance().getActivityForPOI(poiName);
			Intent intent = new Intent(Global.mainActivity, c);
		    Global.renderActivity.startActivity(intent);
			return true;
		}
		
		if(cheatImage.containsPoint(glX, glY))
		{
			int nextDay = (Global.getCurrentDay()==5)? 1:Global.getCurrentDay()+1;
			Global.setDay(nextDay);
			return true;
		}		
		
		return false;
	}
	
	public void setPOI(String poiName)
	{
		if(poiName != null)
		{
			poiImage.setVisible(true);
			this.poiName = poiName;
		}
		else
		{
			poiImage.setVisible(false);
			this.poiName = null;
		}
	}
	
	public void draw()
	{
		poiImage.draw();
		cheatImage.draw();
	}
	
	private RScreenImage poiImage;
	private RScreenImage cheatImage;
	private String poiName;
	
	private static RTopButtons instance;
}
