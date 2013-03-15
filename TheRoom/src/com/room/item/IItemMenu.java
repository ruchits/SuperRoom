package com.room.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.MotionEvent;

import com.room.Global;
import com.room.R;
import com.room.item.IItems.Item;
import com.room.media.MSoundManager;
import com.room.puzzles.*;
import com.room.scene.SLayout.Box;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class IItemMenu extends SSceneActivity {
	public static Item itemInUse = null;
	
	private static final String DEFAULT_MSG = "No items in inventory";
	private static HashMap<Box, IItems.Item> itemList = null;
	private static boolean showDescription = false;
	private static Box selectedBox = null;
	
	private static Box descriptionBox = IItems.getInstance().getDescriptionBox();
	private static Rect descrDestination = new Rect((int)(descriptionBox.left*Global.SCREEN_WIDTH), (int)(descriptionBox.top*Global.SCREEN_HEIGHT),
			(int)(descriptionBox.right*Global.SCREEN_WIDTH), (int)(descriptionBox.bottom*Global.SCREEN_HEIGHT));
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Initialize background layout.
		setLayout(SLayoutLoader.getInstance().itemMenu);
		showInventory(false);
		showBackButton(true);
 	}

	@Override
	protected void onResume()
	{
		super.onResume();
		setBackgroundImage(R.drawable.items_menu);
		
		itemList = IItems.getInstance().getItemList();
		if(itemList == null || itemList.size() ==0) {
			setText(DEFAULT_MSG, Global.TextType.TEXT_CENTERED, false);
		}
		
		// Take the item menu to it's default state
		if (itemInUse==null) {
			selectedBox = null;
			showDescription = false;
		}
		else {	// Display the item that was previously selected, but de-select it.
			setText(itemInUse.getDescriprion(), Global.TextType.TEXT_ITEM_DESCR, false);
			itemInUse = null;
		}
	}

	@Override
    public void onBoxDown(SLayout.Box box, MotionEvent event)
    {
		if (itemList != null) {
			Iterator<Entry<Box, Item>> it = itemList.entrySet().iterator();
			
			while(it.hasNext()) {
				Map.Entry<Box, Item> pairs = (Map.Entry<Box, Item>) it.next();
				Box currBox = (Box) pairs.getKey();
				Item currItem = (Item) pairs.getValue();
			
				if(currBox.name.equals(box.name)) {
					MSoundManager.getInstance().playSoundEffect(R.raw.tick);
					//double click on the same box, i.e. use the item now.
					if ((selectedBox != null) && selectedBox.name.equals(box.name)) {
						useItem(currBox, currItem);
					}
					else {
						showDescription = true;
						selectedBox = currBox;
						setText(currItem.getDescriprion(), Global.TextType.TEXT_ITEM_DESCR, true);
						itemInUse = null;
					}
					break;
				}
			}
		}
    }
	
	@Override
	public void onDraw(Canvas canvas, Paint paint)
	{
		super.onDraw(canvas, paint);
		
		drawItemBoxes(canvas, paint);
		drawDescriptionBox(canvas, paint);	
	}
	
	private void drawItemBoxes(Canvas canvas, Paint paint) {
		// Draw different icons on the screen.
		if(itemList != null && itemList.size() > 0) {
			Iterator<Entry<Box, Item>> it = itemList.entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry<Box, Item> pairs = (Map.Entry<Box, Item>) it.next();
			    Box box = (Box) pairs.getKey();
			    Item item = (Item) pairs.getValue();
			    		
			    int iconLeft = (int)(box.left*Global.SCREEN_WIDTH);
			  	int iconRight = (int)(box.right*Global.SCREEN_WIDTH);
			    int iconTop = (int)(box.top*Global.SCREEN_HEIGHT);
			    int iconBottom = (int)(box.bottom*Global.SCREEN_HEIGHT);						
			    Rect icon = new Rect(iconLeft, iconTop, iconRight, iconBottom);
			    RectF iconF = new RectF(iconLeft, iconTop, iconRight, iconBottom);
			    		
			    if ((selectedBox != null) && selectedBox.name.equals(box.name)) {
			    	// set paint for when the box is selected.
			    }
			    else {
					int alpha = paint.getAlpha();
					paint.setColor(Color.BLACK);
					paint.setAlpha(50);
					canvas.drawRoundRect(iconF, Global.ROUND_EDGE_WIDTH, Global.ROUND_EDGE_WIDTH, paint);
					paint.setAlpha(alpha);
			    }
			    		
			    // Draw the bitmap for this icon.
			    Bitmap bm = item.getBitmap();
			    canvas.drawBitmap(bm, null, icon, paint);
			}
		}
	}
	
	private void drawDescriptionBox(Canvas canvas, Paint paint) {
		if(showDescription) {
    		if(selectedBox != null) {
    			Item item = itemList.get(selectedBox);
			
    			Bitmap bm = item.getBitmap();
    			canvas.drawBitmap(bm, null, descrDestination, paint);
    		}
    	}
	}
    
	private void useItem(Box box, Item item) {
		// TODO: Notify Render Activity of this item in use?
		
		itemInUse = item;
		
		if(item.getName().equals("cellphone"))
		{
			startActivity(new Intent(this, PCellphone.class));
		}
		else
			finish();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
