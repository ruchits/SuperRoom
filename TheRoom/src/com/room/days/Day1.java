package com.room.days;

import com.room.item.IItems;

public class Day1
{
	//put all day 1 flags here
	public static boolean isDeadManRevealed;
	public static boolean isCellphoneRinging;
	
	public static void reset()
	{
		isDeadManRevealed = false;
		isCellphoneRinging = false;
		
		IItems.getInstance().clearItems();
		IItems.getInstance().addItem("notebook");
		IItems.getInstance().addItem("photo");
	}
}