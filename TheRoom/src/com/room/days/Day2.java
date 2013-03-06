package com.room.days;

import com.room.item.IItems;

public class Day2
{
	//put all day 2 flags here
	
	public static void reset()
	{		
		IItems.getInstance().clearItems();		
		IItems.getInstance().addItem("notebook");
		IItems.getInstance().addItem("photo");
	}
}