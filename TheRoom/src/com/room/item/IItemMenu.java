package com.room.item;

import java.util.ArrayList;
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
import com.room.item.IItemManager.Item;
import com.room.media.MSoundManager;
import com.room.puzzles.*;
import com.room.scene.SLayout.Box;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;

public class IItemMenu extends SSceneActivity
{	
	public static Item itemInUse = null;
	private static Item selectedItem = null;
	
	private static final String DEFAULT_MSG = "No items in inventory.";
	private static int ICON_ROUND_EDGE_WIDTH = 16;
		
	private static boolean isLayoutInitialized = false;
	private static RectF enlargedImgDestination;
	private static int numItemsToDisplay;
	
	private ArrayList<String> itemList;	
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Initialize background layout.
		setLayout(SLayoutLoader.getInstance().itemMenu);
		setBackgroundImage(R.drawable.items_menu);
		showInventory(false);
		showBackButton(true);				
		
        if(!isLayoutInitialized)
        {
        	SLayout itemMenuLayout = SLayoutLoader.getInstance().itemMenu;
        	enlargedImgDestination = itemMenuLayout.getBoxPixelCoords("imgBox");        	
        	numItemsToDisplay = itemMenuLayout.getAllBoxesContainingName("item").size();        	        	
			isLayoutInitialized = true;
        }		
 	}

	@Override
	protected void onResume()
	{
		super.onResume();

		itemList = IItemManager.getInstance().getInventory();
		
		if(itemList.size() == 0)
		{
			setText(DEFAULT_MSG, Global.TextType.TEXT_CENTERED, false);
		}
		
		// Take the item menu to it's default state
		if (itemInUse==null)
		{
			selectedItem = null;
			setText("", Global.TextType.TEXT_ITEM_DESCR, false);
		}
		else
		{	// Display the item that was previously selected, but de-select it.
			setText(itemInUse.getDescription(), Global.TextType.TEXT_ITEM_DESCR, false);
			itemInUse = null;
		}
	}

	@Override
    public void onBoxDown(SLayout.Box box, MotionEvent event)
    {
		if(box.name.contains("item"))
		{
			int itemNum = Integer.parseInt(box.name.substring(4));
			
			if(itemNum>=itemList.size())
				return;
			
			MSoundManager.getInstance().playSoundEffect(R.raw.tick);
			
			String itemID = itemList.get(itemNum);
			
			if (selectedItem != null && itemID.equals(selectedItem.getID()))
			{
				useItem(selectedItem);
			}
			else
			{
				selectedItem = IItemManager.getInstance().getItemFromPool(itemID);
				setText(selectedItem.getDescription(), Global.TextType.TEXT_ITEM_DESCR, true);
				itemInUse = null;
			}
		}
    }
	
	@Override
	public void onDraw(Canvas canvas, Paint paint)
	{
		super.onDraw(canvas, paint);
		
		drawItemBoxes(canvas, paint);
		drawEnlargedImage(canvas, paint);	
	}
	
	private void drawItemBoxes(Canvas canvas, Paint paint)
	{
		int itemIndex = 0;
		SLayout itemMenuLayout = SLayoutLoader.getInstance().itemMenu;
		
		for(String itemID:itemList)
		{			
			if(itemIndex>=numItemsToDisplay)
				break;
			
			RectF iconF = itemMenuLayout.getBoxPixelCoords("item"+itemIndex);
		    		
		    if ((selectedItem != null) && selectedItem.getID().equals(itemID))
		    {
		    	// set paint for when the box is selected.
		    }
		    else
		    {
				int alpha = paint.getAlpha();
				paint.setColor(Color.BLACK);
				paint.setAlpha(50);
				canvas.drawRoundRect(iconF, ICON_ROUND_EDGE_WIDTH, ICON_ROUND_EDGE_WIDTH, paint);
				paint.setAlpha(alpha);
		    }
		    		
		    // Draw the bitmap for this icon.
		    Item item = IItemManager.getInstance().getItemFromPool(itemID);
		    Bitmap bm = item.getThumbnailBitmap();
		    canvas.drawBitmap(bm, null, iconF, paint);
		    ++itemIndex;
		}
	}
	
	private void drawEnlargedImage(Canvas canvas, Paint paint)
	{
		if(selectedItem != null)
		{
			Bitmap bm = UBitmapUtil.loadBitmap(selectedItem.getResID(), false);
			canvas.drawBitmap(bm, null, enlargedImgDestination, paint);
			bm.recycle();
		}
	}
    
	private void useItem(Item item)
	{
		// TODO: Notify Render Activity of this item in use?
		
		itemInUse = item;
		
		if(item.getID().equals("cellphone") || item.getID().equals("cellphone_cracked"))
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
