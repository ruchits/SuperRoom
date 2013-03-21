package com.room.days;

import com.room.item.IItemManager;

public class Day5
{
	//put all day 5 flags here
	
	public static void reset()
	{		
		IItemManager.getInstance().clearInventory();		
		IItemManager.getInstance().addItemToInventory("journal");
		IItemManager.getInstance().addItemToInventory("photo");
		IItemManager.getInstance().addItemToInventory("knife");
		IItemManager.getInstance().addItemToInventory("cellphone_cracked");
	}
}