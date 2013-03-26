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
		IItemManager.getInstance().addItemToInventory("gear1");
		IItemManager.getInstance().addItemToInventory("key");
		IItemManager.getInstance().addItemToInventory("gear2");		
		IItemManager.getInstance().addItemToInventory("gear3");
		IItemManager.getInstance().addItemToInventory("brick");
	}
}