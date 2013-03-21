package com.room.scene;
import java.util.ArrayList;
import android.graphics.RectF;

import com.room.Global;

public class SLayout
{
	public static class Box
	{
		public String name=null;
		public String desc=null;
		public float left,right,top,bottom;
		public Box(){}
		public Box(Box b)
		{
			this.name = b.name+"";
			this.desc = b.desc+"";
			this.left = b.left;
			this.right = b.right;
			this.top = b.top;
			this.bottom = b.bottom;				
		}
		public Box(String name, float left, float right, float top, float bottom) {
			this.name = name;
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;
		}
	}
		
	public void addBox(Box b)
	{
		boxes.add(b);
	}		
	
	public Box getBoxAtPixel(float x, float y)
	{
		float fx = x/Global.SCREEN_WIDTH;
		float fy = y/Global.SCREEN_HEIGHT;
		return getBoxAt(fx,fy);
	}
	
	private Box getBoxAt(float x, float y)
	{
		for(int i=0; i<boxes.size(); ++i)
		{
			Box box = boxes.get(i);
			
			if(box.left <= x && x <= box.right
				&& box.top <= y && y <= box.bottom)
			{
				return box;
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
	
	public ArrayList<Box> getAllBoxes()
	{
		//cant return the boxes directly if you're going to start removing items in it		
		//return boxes;
		
		ArrayList<Box> copyOfBoxes = new ArrayList<Box>();
		
		for(Box b:boxes)
			copyOfBoxes.add(new Box(b));
		
		return copyOfBoxes;		
	}
	
	
	public ArrayList<Box> getAllBoxesContainingName(String substr)
	{
		ArrayList<Box> copyOfBoxes = new ArrayList<Box>();
		
		for(Box b:boxes)
		{
			if(b.name.contains(substr))
				copyOfBoxes.add(new Box(b));	
		}
					
		return copyOfBoxes;		
	}	
	
	public ArrayList<Box> getBoxesWithName(String name)
	{
		ArrayList<Box> retBoxes = new ArrayList<Box>();
		for(Box b:boxes)
		{
			if(b.name.equals(name))
				retBoxes.add(b);
		}
		return retBoxes;
	}
	
	public Box getBoxWithName(String name)
	{
		for(Box b:boxes)
		{
			if(b.name.equals(name))
				return b;
		}
		return null;
	}		

	private ArrayList<Box> boxes = new ArrayList<Box>();
}
