package com.room.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
import com.room.scene.SLayout.Box;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class IItemMenu extends SSceneActivity {
	private static HashMap<Box, IItems.Item> itemList = null;
	private boolean showDescription = false;
	private Box showBox = null;
	private Box descriptionBox = null;
	private static final String DEFAULT_MSG = "No items in inventory";
	
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
		descriptionBox = IItems.getInstance().getDescriptionBox();
		
		if(itemList == null || itemList.size() ==0) {
			setText(DEFAULT_MSG, Global.TextType.TEXT_CENTERED, false);
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
				Item item = (Item) pairs.getValue();
			
				if(currBox.name.equals(box.name)) {
					MSoundManager.getInstance().playSoundEffect(R.raw.tick);
					//double click on the same box, i.e. use the item now.
					if ((showBox != null) && showBox.name.equals(box.name)) {
						useItem(currBox);
					}
					else {
						showDescription = true;
						showBox = currBox;
						setText(item.getDescriprion(), Global.TextType.TEXT_ITEM_DESCR, true);
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
	    		
	    		if ((showBox != null) && showBox.name.equals(box.name)) {
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
		
	    	if(showDescription) {
			
	    		if(showBox != null) {
	    			Item item = itemList.get(showBox);
				
	    			// Draw the bitmap for this icon.
	    			int iconLeft = (int)(descriptionBox.left*Global.SCREEN_WIDTH);
	    			int iconRight = (int)(descriptionBox.right*Global.SCREEN_WIDTH);
	    			int iconTop = (int)(descriptionBox.top*Global.SCREEN_HEIGHT);
	    			int iconBottom = (int)(descriptionBox.bottom*Global.SCREEN_HEIGHT);
	    			Rect dest = new Rect(iconLeft, iconTop, iconRight, iconBottom);
				
	    			Bitmap bm = item.getBitmap();
	    			canvas.drawBitmap(bm, null, dest, paint);
	    		}
	    	}
		}
	}
    
	private void useItem(Box box) {
		Item item = itemList.get(box);
		Global.itemInuse = item;
		// TODO: Notify Render Activity of this item in use?
		
		finish();	
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		showDescription = false;
		showBox = null;
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onDestroy()
	{		
		super.onDestroy();
	}
}
