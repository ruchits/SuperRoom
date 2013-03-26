package com.room.days;

import com.room.item.IItemManager;

public class Day2
{
	//put all day 2 flags here
	
	public static void reset()
	{		
		IItemManager.getInstance().clearInventory();		
		IItemManager.getInstance().addItemToInventory("journal");
		IItemManager.getInstance().addItemToInventory("photo");
		IItemManager.getInstance().addItemToInventory("knife");
		IItemManager.getInstance().addItemToInventory("cellphone_cracked");
		IItemManager.getInstance().addItemToInventory("gear1");
	}
}