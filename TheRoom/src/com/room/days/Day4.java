package com.room.days;

import com.room.item.IItems;

public class Day4
{
	//put all day 4 flags here
	
	public static void reset()
	{		
		IItems.getInstance().clearItems();		
		IItems.getInstance().addItem("journal");
		IItems.getInstance().addItem("photo");
	}
}