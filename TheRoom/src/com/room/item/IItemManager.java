package com.room.item;

import java.util.ArrayList;
import java.util.HashMap;

import com.room.R;
import com.room.utils.UBitmapUtil;

import android.graphics.Bitmap;

public class IItemManager
{
	private HashMap<String, Item> itemPool;		//pool of all unique items, initialized on startup
	private ArrayList<String> inventoryItems;	//list of current itemIDs in inventory
	
	private static IItemManager instance = null;
	
	public static IItemManager getInstance()
	{
		if(instance == null)
		{
			instance = new IItemManager();
		}
		return instance;
	}
	
	public void init()
	{
		inventoryItems = new ArrayList<String>();
		
		//Add all items to item pool.
		itemPool = new HashMap<String, Item>();
		itemPool.put("key", new IItemManager.Item("key","Tarnished Key", "A tarnished key.", R.drawable.item_key));
		itemPool.put("knife", new IItemManager.Item("knife","Rusted Knife", "An dull, rusted blade.", R.drawable.item_knife));
		itemPool.put("cellphone", new IItemManager.Item("cellphone","Tainted Cellphone", "A mobile phone. There's no signal here.", R.drawable.item_cellphone));
		itemPool.put("cellphone_cracked", new IItemManager.Item("cellphone_cracked","Cracked Cellphone", "A defunct mobile phone. The screen's cracked.", R.drawable.item_cellphone_cracked));
		itemPool.put("journal", new IItemManager.Item("journal","Journal", "A worn journal with missing pages.", R.drawable.item_journal));
		itemPool.put("photo", new IItemManager.Item("photo", "Old Polaroid", "Do I... know these people?", R.drawable.item_picture));
		itemPool.put("gear1", new IItemManager.Item("gear1", "Bronze Gear", "A damaged gear made of bronze.", R.drawable.item_gear1));
		itemPool.put("gear2", new IItemManager.Item("gear2", "Silver Gear", "An old gear made of silver.", R.drawable.item_gear2));
		itemPool.put("gear3", new IItemManager.Item("gear3", "Golden Gear", "A shiny gear plated in gold.", R.drawable.item_gear3));
		itemPool.put("brick", new IItemManager.Item("brick", "Red Brick", "A heavy brick of clay.", R.drawable.item_brick));
	}
	
	public static class Item
	{
		private String id;
		private String name;
		private String description;
		private Bitmap thumbnailBmp;
		private int resID;
	
		public Item(String id, String name, String description, int resourceID)
		{
			this.id = id;
			this.name = name;
			this.description = description;
			this.resID = resourceID;
		
			// decode the resource into bitmap icon.
			thumbnailBmp = UBitmapUtil.loadScaledBitmap(resourceID, 128,128,false);
		}

		public String getName()
		{
			return this.name;
		}
		
		public String getID()
		{
			return this.id;
		}		
		
		public int getResID()
		{
			return resID;
		}
	
		public String getDescription()
		{
			return this.description;
		}
	
		// get the original size bitmap for this item.
		public Bitmap getThumbnailBitmap()
		{
			return thumbnailBmp;
		}
	}
	
	public void addItemToInventory(String itemID)
	{
		if(inventoryItems.contains(itemID))
			return;
		
		inventoryItems.add(itemID);
	}
	
	public void removeItemFromInventory(String itemID)
	{
		inventoryItems.remove(itemID);
	}
	
	public boolean hasItemInInventory(String itemID)
	{
		return inventoryItems.contains(itemID);
	}
	
	public void clearInventory()
	{
		inventoryItems.clear();
	}
	
	// Get the list of items in a player's inventory.
	public ArrayList<String> getInventory()
	{
		return inventoryItems;
	}
	
	public Item getItemFromPool(String itemID)
	{
		return itemPool.get(itemID);
	}
}
