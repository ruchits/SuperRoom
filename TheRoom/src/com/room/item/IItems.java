package com.room.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.room.Global;
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
	private Box descriptionBox = null;
	private static final String TAG = "com.room.item.Items";
	
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
			if (!box.name.startsWith("item")) {
				descriptionBox = box;
				it.remove();
			}
		}
		
		itemMap = new HashMap<Box, Item>();
		maxListSize = itemBoxes.size();
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
	
	public void addItem(Item item) {
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
				
				if(i.getName().equals(item.getName())) {
					// Do not add the same item again.
					return;
				}
			}
		}
		
		for(Box box: itemBoxes) {
			if (!itemMap.containsKey(box)) {
				itemMap.put(box, item);
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
	
	//TODO: init items uptill a certain stage aka from: 1....stage
	public void initItemsforStage(int stage) {
		return;
	}
	
	// Get the list of items in a player's inventory.
	public HashMap<Box, Item> getItemList() {
		return (itemMap.size() == 0)? null: itemMap;
	}
	
	// Get the description box on item menu screen.
	public Box getDescriptionBox() {
		return descriptionBox;
	}
}
