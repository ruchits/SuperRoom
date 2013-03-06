package com.room.days;

import com.room.item.IItems;

public class Day5
{
	//put all day 5 flags here
	
	public static void reset()
	{		
		IItems.getInstance().clearItems();		
		IItems.getInstance().addItem("notebook");
		IItems.getInstance().addItem("photo");
	}
}