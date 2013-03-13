package com.room;

import com.room.render.RModelLoader;
import com.room.render.RPOIManager;
import com.room.days.*;
import com.room.item.IItems.Item;
import com.room.render.RRenderActivity;

import android.app.ProgressDialog;


public class Global
{
	public static MainActivity mainActivity;
	public static RRenderActivity renderActivity;
	public static float deviceDPIScale;
	
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
	
	public static int FONT_SIZE = 28;
	public static int FONT_SIZE_BIG = 32;
	public static int DEFAULT_TEXT_LENGTH = 36;
	public static final float FONT_THRESHOLD_DP = 16.0f;
	public static int ICON_WIDTH = 100;
	public static int ICON_HEIGHT = 100;
	public static int ROUND_EDGE_WIDTH = 16;
	public enum TextType { TEXT_ITEM_DESCR, TEXT_SUBTITLE, TEXT_CENTERED };
	public static Item itemInuse;
	
	public static int VMMemSize;
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
		
		switch(day)
		{
			case 1:
			Day1.reset();
			break;
			case 2:
			Day2.reset();
			break;
			case 3:
			Day3.reset();
			break;
			case 4:
			Day4.reset();
			break;
			case 5:
			Day5.reset();
			break;			
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
