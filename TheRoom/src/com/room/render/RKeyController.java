package com.room.render;

import android.view.KeyEvent;

import com.room.Global;

public class RKeyController
{
	//SINGLETON!!
	public static RKeyController getInstance()
	{
		if(instance == null)
		{
			instance = new RKeyController();
		}
		return instance;
	}
	
    public static boolean WKeyDown = false;
    public static boolean AKeyDown = false;
    public static boolean SKeyDown = false;
    public static boolean DKeyDown = false;
    public static boolean QKeyDown = false;
    public static boolean EKeyDown = false;    
    
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	//TEMPORARY! Use the volume keys to change days
    	
    	switch(keyCode)
    	{
    		case KeyEvent.KEYCODE_R:
	    	case KeyEvent.KEYCODE_VOLUME_DOWN:
	    		if(Global.CURRENT_DAY > Global.FIRST_DAY)
	    		{
	    			Global.CURRENT_DAY --;
	    			RModelLoader.getInstance().updateBoundaries();
	    		}
	    	return true;
	    	
	    	case KeyEvent.KEYCODE_F:
	    	case KeyEvent.KEYCODE_VOLUME_UP:
	    		if(Global.CURRENT_DAY < Global.LAST_DAY)
	    		{
	    			Global.CURRENT_DAY ++;
	    			RModelLoader.getInstance().updateBoundaries();
	    		}
	    	return true;
	    	
			case KeyEvent.KEYCODE_W:
				WKeyDown = true;
	    	return true;
	    	
			case KeyEvent.KEYCODE_A:
				AKeyDown = true;
	    	return true;
	    	
			case KeyEvent.KEYCODE_S:
				SKeyDown = true;
	    	return true;
	    	
			case KeyEvent.KEYCODE_D:
				DKeyDown = true;
	    	return true;	
	    	
			case KeyEvent.KEYCODE_Q:
				QKeyDown = true;
	    	return true;
	    	
			case KeyEvent.KEYCODE_E:
				EKeyDown = true;
	    	return true;	    	
	    	
    	}
    	return false;
    }    
    
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
    	//TEMPORARY! Use the volume keys to change days
    	
    	switch(keyCode)
    	{
			case KeyEvent.KEYCODE_W:
				WKeyDown = false;
	    	return true;
	    	
			case KeyEvent.KEYCODE_A:
				AKeyDown = false;
	    	return true;
	    	
			case KeyEvent.KEYCODE_S:
				SKeyDown = false;
	    	return true;
	    	
			case KeyEvent.KEYCODE_D:
				DKeyDown = false;
	    	return true;		
	    	
			case KeyEvent.KEYCODE_Q:
				QKeyDown = false;
	    	return true;
	    	
			case KeyEvent.KEYCODE_E:
				EKeyDown = false;
	    	return true;		    	
    	}
    	return false;
    }    
    
    private static RKeyController instance;
}
