package com.room.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.room.Global;
import com.room.R;
import com.room.item.IItems.Item;
import com.room.scene.SLayoutLoader;
import com.room.scene.SLayout.Box;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class IItems {
	// the itemMap cannot contain more items than the size of itemBoxes
	private static HashMap<Box, Item> itemMap;
	private static int maxListSize;
	private static ArrayList<Box> itemBoxes;
	private static Box descriptionBox = null;
	private static Box textBox = null;
	private static final String TAG = "com.room.item.Items";
	private HashMap<String, Item> itemPool;
	
	private static IItems instance = null;
	
	public static IItems getInstance()
	{
		if(instance == null)
		{
			instance = new IItems();
		}
		return instance;
	}
	
	public void init() {
		// Get all the boxes in this layout except for the boxes that do not start with substring "item"
		itemBoxes = SLayoutLoader.getInstance().itemMenu.getAllBoxes();
		
		Iterator<Box> it = itemBoxes.iterator();	
		while(it.hasNext()) {
			Box box = it.next();
			if (box.name.equals("descrBox")) {
				descriptionBox = box;
				it.remove();
			}
			else if (box.name.equals("textBox")) {
				textBox = box;
				it.remove();
			}  
		}
		
		itemMap = new HashMap<Box, Item>();
		maxListSize = itemBoxes.size();
		
		//Add all items to item pool.
		itemPool = new HashMap<String, Item>();
		itemPool.put("key", new IItems.Item("key", "This item can be used to unlock doors.", R.drawable.key_icon));
		itemPool.put("knife", new IItems.Item("knife", "An extremely sharp bladed instrument, for surgical use.", R.drawable.knife_icon));
		itemPool.put("cellphone", new IItems.Item("cellphone", "A mobile phone. There's no signal here.", R.drawable.phone_icon));
		itemPool.put("journal", new IItems.Item("journal", "A worn journal with missing pages.", R.drawable.book_icon));
		itemPool.put("notepad", new IItems.Item("notepad", "No idea what this is doing here, looked like a cool icon.", R.drawable.notepad_icon));
		itemPool.put("photo", new IItems.Item("photo", "Do I... know these people?", R.drawable.painting_icon));
	}
	
	public static class Item {
		private String name;
		private String description;
		private Bitmap mBitmap;
		private int resID;
	
		public Item(String name, String description, int resourceID) {
			this.name = name;
			this.description = description;
			this.resID = resourceID;
		
			// decode the resource into bitmap icon.
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			mBitmap = BitmapFactory.decodeResource(Global.mainActivity.getResources(), resID, options);
		}

		public String getName() {
			return this.name;
		}
	
		public String getDescriprion() {
			return this.description;
		}
	
		// get the original size bitmap for this item.
		public Bitmap getBitmap() {
			return mBitmap;
		}
	
		// get the bitmap, bmWidth x bmHeight, for this item.
		public Bitmap getBitmap(int bmWidth, int bmHeight) {
			return Bitmap.createScaledBitmap(this.mBitmap, bmWidth, bmHeight, true);
		}
		
		// get a mutable bitmap in case you want to manipulate it after.
		public Bitmap getMutableBitmap() {
			boolean mutable = true;
			Bitmap mutableBitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, mutable);
			return mutableBitmap;
		}
		
	}
	
	public void addItem(String itemName) {
		/* Box is our key in the map. Go over each box in Arraylist and find an empty one.
		 * 1. Can not add the same item twice
		 * 2. Can not exceed MAX_MAP_SIZE
		 */
		
		//TODO: Throw an exception when the size exceeds the limit. For now, just don't add the item.
		if(itemMap.size() == maxListSize) return;
		if(itemMap != null) {
			Iterator<Entry<Box, Item>> it = itemMap.entrySet().iterator();
			while(it.hasNext()) {
				Entry<Box, Item> entry = (Entry<Box, Item>) it.next();
				Item i = entry.getValue();
				
				if(i.getName().equals(itemName)) {
					// Do not add the same item again.
					return;
				}
			}
		}
		
		for(Box box: itemBoxes) {
			if (!itemMap.containsKey(box)) {
				itemMap.put(box, itemPool.get(itemName));
				break;
			}
		}
	}
	
	public void removeItem(String itemName) {
		if(itemMap != null) {
			Iterator<Entry<Box, Item>> it = itemMap.entrySet().iterator();
			while(it.hasNext()) {
				Entry<Box, Item> entry = (Entry<Box, Item>) it.next();
				Item i = entry.getValue();
				
				if(i.getName().equals(itemName)) {
					it.remove();
					break;					
				}
			}
		}
	}
	
	public boolean hasItem(String itemName)
	{
		return itemMap.containsKey(itemName);
	}
	
	public void clearItems()
	{
		itemMap.clear();
	}
	
	// Get the list of items in a player's inventory.
	public HashMap<Box, Item> getItemList() {
		return (itemMap.size() == 0)? null: itemMap;
	}
	
	// Get the description box on item menu screen.
	public Box getDescriptionBox() {
		return descriptionBox;
	}
	
	// Get the text Box.
	public Box getTextBox() {
		return textBox;
	}
}
