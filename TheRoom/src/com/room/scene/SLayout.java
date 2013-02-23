package com.room.scene;
import java.util.ArrayList;

import android.util.Log;

import com.room.Global;

public class SLayout
{
	static class Box
	{
		public String name=null;
		public float left,right,top,bottom;
		public Box(){}
	}
		
	public void addBox(Box b)
	{
		boxes.add(b);
	}		
	
	public String getBoxAtPixel(float x, float y)
	{
		float fx = x/Global.SCREEN_WIDTH;
		float fy = y/Global.SCREEN_HEIGHT;
		return getBoxAt(fx,fy);
	}
	
	private String getBoxAt(float x, float y)
	{
		for(int i=0; i<boxes.size(); ++i)
		{
			Box box = boxes.get(i);
			
			if(box.left <= x && x <= box.right
				&& box.top <= y && y <= box.bottom)
			{
				return box.name;
			}
		}
		
		return null;
	}	
	
	public void getBoxCoords (String boxName, int []coords)
	{
		for(int i=0; i<boxes.size(); ++i)
		{
			Box box = boxes.get(i);
			if ( box.name.equals(boxName) )
			{
				coords[0] = (int) (box.left * Global.SCREEN_WIDTH);
				coords[1] = (int) (box.right * Global.SCREEN_WIDTH);
				coords[2] = (int) (box.top * Global.SCREEN_HEIGHT);
				coords[3] = (int) (box.bottom * Global.SCREEN_HEIGHT);
				Log.e("SLayout", "left[" + coords[0] + "], right[" + coords[1] + "]");
			}
		}
	}
	
	public int getBoxNum (String boxName)
	{
		for(int i=0; i<boxes.size(); ++i)
		{
			Box box = boxes.get(i);
			if ( box.name.equals(boxName) )
			{
				return i;
			}
		}
		return -1;
	}
	private ArrayList<Box> boxes = new ArrayList<Box>();
}
