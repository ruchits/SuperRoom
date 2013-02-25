package com.room.scene;
import java.util.ArrayList;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.room.Global;

public class SLayout
{
	public static class Box
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
	
	public RectF getBoxPixelCoords (String boxName)
	{
		for(Box box:boxes)
		{
			if ( box.name.equals(boxName) )
			{
				RectF rect = new RectF();
				
				rect.left = box.left * Global.SCREEN_WIDTH;
				rect.right = box.right * Global.SCREEN_WIDTH;
				rect.top = box.top * Global.SCREEN_HEIGHT;
				rect.bottom = box.bottom * Global.SCREEN_HEIGHT;
				return rect;
			}
		}
		
		return null;
	}
	
	public ArrayList<Box> getAllBoxes() {
		return boxes;
	}

	private ArrayList<Box> boxes = new ArrayList<Box>();
}
