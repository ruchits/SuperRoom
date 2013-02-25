package com.room;

import com.room.render.RModelLoader;
import com.room.render.RPOIManager;
import com.room.item.IItems.Item;
import com.room.render.RRenderActivity;
import com.room.utils.UPair;

import android.app.ProgressDialog;


public class Global
{
	public static MainActivity mainActivity;
	public static RRenderActivity renderActivity;
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;
	public static int GL_WIDTH = 0;
	public static int GL_HEIGHT = 0;	
	private static int CURRENT_DAY = 5;
	public static int FIRST_DAY = 1;
	public static int LAST_DAY = 5;
	public static boolean HALF_RES_RENDER = true;

	public static boolean DEBUG_SKIP_MENU = false;
	public static boolean DEBUG_NO_DECALS = false;
	public static boolean DEBUG_NO_PROPS = false;
	public static boolean DEBUG_SHOW_POI_BOXES = false;
	public static boolean DEBUG_SHOW_FPS = true;
	
	public static int FONT_SIZE = 24;
	public static int FONT_SIZE_BIG = 60;
	public static int TEXT_LENGTH = 32;
	public static int ICON_WIDTH = 120;
	public static int ICON_HEIGHT = 120;
	public enum TextType { TEXT_ITEM_DESCR, TEXT_SUBTITLE, TEXT_CENTERED };
	public static Item itemInuse;
	
	public enum ResType { LOW_RES, MED_RES, HI_RES};
	
	public static ProgressDialog progDailog;

	public static int getCurrentDay()
	{
		return CURRENT_DAY;		
	}
	
	public static void setDay(int day)
	{
		if(FIRST_DAY <= day && day <= LAST_DAY)
		{
			CURRENT_DAY = day;
			RModelLoader.getInstance().updateBoundaries();
			RPOIManager.getInstance().setDefaultsForCurrentDay();
		}						
	}
	
	public static void gotoNextDay()
	{
		setDay(Global.CURRENT_DAY+1);
	}
	
	public static void gotoPrevDay()
	{
		setDay(Global.CURRENT_DAY-1);
	}	
}
