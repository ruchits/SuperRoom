package com.room.scene;

import java.util.ArrayList;

import com.room.Global;
import com.room.Global.TextType;
import com.room.R;
import com.room.item.IItemMenu;
import com.room.item.IItems;
import com.room.item.IItems.Item;
import com.room.scene.SLayout.Box;
import com.room.utils.UPair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SSceneActivity extends Activity
{
	private static Bitmap inventory;
	private static Bitmap backbutton;
	
	private static final Box inventoryBox = new Box("inventory", 0f, 0.0800f, 0f, 0.133f);
	private static final Box backBtnBox = new Box("backBtn", 0.9200f, 1.0f, 0f, 0.133f);
	
	//left, top, right, bottom
	private static final Rect invDestination = new Rect(Math.round(inventoryBox.left*Global.SCREEN_WIDTH), Math.round(inventoryBox.top*Global.SCREEN_HEIGHT),
			Math.round(inventoryBox.right*Global.SCREEN_WIDTH), Math.round(inventoryBox.bottom*Global.SCREEN_HEIGHT));
	private static final Rect backbtnDestination = new Rect(Math.round(backBtnBox.left*Global.SCREEN_WIDTH), Math.round(backBtnBox.top*Global.SCREEN_HEIGHT), 
			Math.round(backBtnBox.right*Global.SCREEN_WIDTH), Math.round(backBtnBox.bottom*Global.SCREEN_HEIGHT));
	private static final RectF invDestinationF = new RectF(inventoryBox.left*Global.SCREEN_WIDTH, inventoryBox.top*Global.SCREEN_HEIGHT,
			inventoryBox.right*Global.SCREEN_WIDTH, inventoryBox.bottom*Global.SCREEN_HEIGHT);
	private static final RectF backbtnDestinationF = new RectF(backBtnBox.left*Global.SCREEN_WIDTH, backBtnBox.top*Global.SCREEN_HEIGHT, 
			backBtnBox.right*Global.SCREEN_WIDTH, backBtnBox.bottom*Global.SCREEN_HEIGHT);
	
	
	private static Bitmap itemTextBitmap = Bitmap.createBitmap(500, 200, Bitmap.Config.ARGB_8888);
	private static Bitmap subtitleBitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
	private static Bitmap centeredTextBitmap = Bitmap.createBitmap(500, 70, Bitmap.Config.ARGB_8888);
	
	private static final Box subtitleBox = new Box("subtitle", 0.18020834f, 0.8385417f, 0.85f, 0.9907407f);
	private static final Rect subtitleRect = new Rect(Math.round(subtitleBox.left*Global.SCREEN_WIDTH), Math.round(subtitleBox.top*Global.SCREEN_HEIGHT),
			Math.round(subtitleBox.right*Global.SCREEN_WIDTH), Math.round(subtitleBox.bottom*Global.SCREEN_HEIGHT));
	
	private static final Box centerBox = new Box("center", 0.321875f, 0.703125f, 0.4037037f, 0.5611111f);
	private static final Rect centerRect = new Rect(Math.round(centerBox.left*Global.SCREEN_WIDTH), Math.round(centerBox.top*Global.SCREEN_HEIGHT),
			Math.round(centerBox.right*Global.SCREEN_WIDTH), Math.round(centerBox.bottom*Global.SCREEN_HEIGHT));
	
	private static Rect itemTextRect;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);                
        backgroundImageResID = -1;
        foregroundImageResID = -1;
        unhiddenForegroundBoxes = new ArrayList<String>();
        view = new SSceneView(this);
        setContentView(view);
          
        Resources res = Global.mainActivity.getResources();
        if(inventory == null)
        	inventory = BitmapFactory.decodeResource(res, R.drawable.backpack);
        if(backbutton == null)
        	backbutton = BitmapFactory.decodeResource(res, R.drawable.back);
		
		Box textBox = IItems.getInstance().getTextBox();
		itemTextRect = new Rect(Math.round(textBox.left*Global.SCREEN_WIDTH), Math.round(textBox.top*Global.SCREEN_HEIGHT),
				Math.round(textBox.right*Global.SCREEN_WIDTH), Math.round(textBox.bottom*Global.SCREEN_HEIGHT));
		
    }
	
	private class SSceneView extends View
	{		
		public SSceneView(Context context)
		{
			super(context);
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			activity = (SSceneActivity)context;
			textCanvas = new Canvas();
			
			setStrokePaint(); // Set strokePaint for subtitle track.
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
			activity.onDraw(canvas, paint);
			
			// Draw the Icons
			if(activity.showInventoryIcon) {
				drawIconBG(canvas, paint, SSceneActivity.invDestinationF);
				
				Bitmap inventory;
				if (IItemMenu.itemInUse != null) {
					inventory = IItemMenu.itemInUse.getBitmap();
				}
				else {
					inventory = SSceneActivity.inventory;
				}
				
				canvas.drawBitmap(inventory, null, SSceneActivity.invDestination, paint);
			}
			
			if (activity.showBackButton) {
    			drawIconBG(canvas, paint, SSceneActivity.backbtnDestinationF);
				Bitmap backButton = SSceneActivity.backbutton;
				canvas.drawBitmap(backButton, null, SSceneActivity.backbtnDestination, paint);
			}
			
			// Draw text
			drawText(canvas);
		}
		
		private void drawText(Canvas canvas) {
			// Draw text if any.
			String text = activity.sceneText;
			TextType type = activity.sceneTextType;
			
			if (text != null) {
				setTextPaint(type); // Set up the paint object for this text type.		
				int deltaHeight = (int) (paint.descent() - paint.ascent());
				int x=0, y=deltaHeight, textLen=0, bmWidth = 0;
				
				paint.getTextBounds(text, 0, text.length(), textBound);
				int stringWidth = textBound.right - textBound.left;	
				
				switch (type) {
				case TEXT_ITEM_DESCR:
					itemTextBitmap.eraseColor(Color.TRANSPARENT); // Clear the text bitmap
					textCanvas.setBitmap(itemTextBitmap);
					bmWidth = itemTextBitmap.getWidth();
					break;
	    		
				case TEXT_SUBTITLE:
					subtitleBitmap.eraseColor(Color.TRANSPARENT);
					textCanvas.setBitmap(subtitleBitmap);
					bmWidth = subtitleBitmap.getWidth();
	    			break;
	    		
				case TEXT_CENTERED:
					centeredTextBitmap.eraseColor(Color.TRANSPARENT);
					textCanvas.setBitmap(centeredTextBitmap);
					bmWidth = centeredTextBitmap.getWidth();
					break;
				}
				
				// # of characters that can fit in the bitmap?
				float ratio = (stringWidth < bmWidth) ? 1.0f: (float)bmWidth/(float)stringWidth;
				textLen = Math.round(ratio*text.length());
				
				if (paint.getTextAlign() == Align.CENTER) {
					x = bmWidth/2;
				}
				else if (paint.getTextAlign() == Align.RIGHT) {
					x = bmWidth;
				}
				
				// Split a long string into textLen blocks for text wrapping.
				String temp = text;
				while(temp.length() > 0) {
					// returns the trimmed string, and the end index in original string.
					UPair<String, Integer> res = SSceneActivity.trimString(temp, textLen, false);
				
					if (type == TextType.TEXT_SUBTITLE) {
						textCanvas.drawText(res.getLeft(), x, y, strokePaint);
					}
					
					textCanvas.drawText(res.getLeft(), x, y, paint);
					y += deltaHeight;
				
					int numCharRead = res.getRight();
					if(numCharRead == temp.length())
						break;
					else
						temp = temp.substring(numCharRead, temp.length());
				}
				
				switch (type) {
				case TEXT_ITEM_DESCR:
					float factor = (float)(itemTextRect.right - itemTextRect.left)/itemTextBitmap.getWidth();
					canvas.drawBitmap(itemTextBitmap,
							new Rect(0,0,itemTextBitmap.getWidth(),itemTextBitmap.getHeight()),							
							new Rect(itemTextRect.left, itemTextRect.top,itemTextRect.right,itemTextRect.top+(int)(itemTextBitmap.getHeight()*factor)),
							paint);
					
					break;
					
				case TEXT_SUBTITLE:
					factor = (float)(subtitleRect.right - subtitleRect.left)/subtitleBitmap.getWidth();
					canvas.drawBitmap(subtitleBitmap,
							new Rect(0,0,subtitleBitmap.getWidth(),subtitleBitmap.getHeight()),							
							new Rect(subtitleRect.left, subtitleRect.top,subtitleRect.right,subtitleRect.top+(int)(subtitleBitmap.getHeight()*factor)),
							paint);
					
	    			break;
	    		
				case TEXT_CENTERED:
					factor = (float)(centerRect.right - centerRect.left)/centeredTextBitmap.getWidth();
					canvas.drawBitmap(centeredTextBitmap,
							new Rect(0,0,centeredTextBitmap.getWidth(),centeredTextBitmap.getHeight()),							
							new Rect(centerRect.left, centerRect.top,centerRect.right,centerRect.top+(int)(centeredTextBitmap.getHeight()*factor)),
							paint);
					break;
				}
			}
		}
		
		private void drawIconBG(Canvas canvas, Paint paint, RectF rect) {
			paint.setColor(Color.WHITE);
			int alpha = paint.getAlpha();
			paint.setAlpha(50);
			float cx= rect.left + (rect.right-rect.left)/2;
			float cy = rect.top + (rect.bottom-rect.top)/2;
			float radius = Math.min((rect.right-rect.left)/2, (rect.bottom-rect.top)/2);
			canvas.drawCircle(cx, cy, radius, paint);
			paint.setAlpha(alpha);
		}		
		
		private void setStrokePaint() {
			// Set up strokePaint for subtitle track.
			strokePaint = new Paint();
			strokePaint.setARGB(255, 0, 0, 0);
			strokePaint.setTextAlign(Paint.Align.CENTER);
			strokePaint.setTextSize(Global.FONT_SIZE);
			strokePaint.setTypeface(Typeface.MONOSPACE);
			strokePaint.setStyle(Paint.Style.STROKE);
			strokePaint.setStrokeWidth(2);
			strokePaint.setFilterBitmap(true);
			strokePaint.setDither(true);
			strokePaint.setAntiAlias(true);
		}
		
		private void setTextPaint(Global.TextType type) {
			//common settings for all.
			paint.setColor(Color.BLACK); 
			paint.setTextSize(Global.FONT_SIZE);
			paint.setTypeface(Typeface.MONOSPACE);
			paint.setTextAlign(Align.CENTER);
			
			switch (type) {
				case TEXT_ITEM_DESCR:
					// Add settings particular to this textType.
					break;
				case TEXT_SUBTITLE:
					// Add settings particular to this textType.
					paint.setColor(Color.WHITE); 
					break;
				case TEXT_CENTERED:
					// Add settings particular to this textType.
					paint.setTextSize(Global.FONT_SIZE_BIG);
					paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
					break;
			}
		}
		
		private SSceneActivity activity;
		private Paint paint;
		private Paint strokePaint;
		private Canvas textCanvas;
		private Rect textBound = new Rect(); // empty Rect that will be used to find text width.
	}
	
	public void repaint()
	{
	    if(view!=null) view.invalidate();
	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	if(layout == null)
    		return true;
    	
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		
		if (actionCode == MotionEvent.ACTION_DOWN)
		{
			selectedBox = null;			
			selectedBox = layout.getBoxAtPixel(event.getX(), event.getY());
			if(selectedBox != null) {
				boolean success;
				if (IItemMenu.itemInUse != null && showInventoryIcon) {
					success = onBoxDownWithItemSel(selectedBox, event);
					if (!success)
						setText(DEFAULT_ITEMUSE_TEXT, TextType.TEXT_SUBTITLE, true);
					
					deselectItem();
				}		
				else
					onBoxDown(selectedBox, event);
			}
			
			if(showInventoryIcon) {
				// Handle touch events to inventory icon and back button on SceneLayout.
				boolean result = getTouchAt(event.getX(), event.getY(), invDestination);
				if (result) {
					Intent intent = new Intent(this, IItemMenu.class);
					startActivity(intent);
				}
			}
			if (showBackButton) {
				boolean result = getTouchAt(event.getX(), event.getY(), backbtnDestination);
				if (result)
					finish();
 			}
		}
		else if (actionCode == MotionEvent.ACTION_MOVE)
		{
			if (selectedBox != null)
				onBoxMove(selectedBox, event);
		}
		else if (actionCode == MotionEvent.ACTION_UP)
		{
			if (selectedBox != null)
				onBoxRelease(selectedBox, event);
		}
		
        return true;
    }
    
    public void onBoxRelease(Box box, MotionEvent event) {
    	//Override this function
    }
    
    public void onBoxMove(Box box, MotionEvent event) {
    	//Override this function
    }
    
    private boolean getTouchAt(float x, float y, Rect dest)
    {
		if(dest.left <= x && x <= dest.right
			&& dest.top <= y && y <= dest.bottom)
		{
			return true;
		}
		
		return false;
	}
    
	//Override this function, but super it to use the background image
	public void onDraw(Canvas canvas, Paint paint)
	{		
		if(backgroundImageResID!=-1)
		{
			Bitmap backgroundBmp = SSceneBitmapProvider.getInstance().decodeImage(backgroundImageResID);
			canvas.drawBitmap(backgroundBmp, null,
					new Rect(0,0,Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT), paint);
		}
		else
		{
			paint.setColor(Color.WHITE);
			canvas.drawRect(new Rect(0,0,Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT), paint);
		}
		
		if(foregroundImageResID != -1)
		{
			for(String boxName:unhiddenForegroundBoxes)
			{
				ArrayList<Box> boxesWithName = layout.getBoxesWithName(boxName);
				
				for(Box b:boxesWithName)
				{
					Bitmap foregroundBmp = SSceneBitmapProvider.getInstance().decodeImage(foregroundImageResID);
					canvas.drawBitmap(foregroundBmp,
							new Rect((int)(b.left * foregroundBmp.getWidth()), (int)(b.top * foregroundBmp.getHeight()),
									(int)(b.right * foregroundBmp.getWidth()), (int)(b.bottom * foregroundBmp.getHeight())),							
							new RectF(b.left * Global.SCREEN_WIDTH, b.top * Global.SCREEN_HEIGHT,
									b.right * Global.SCREEN_WIDTH, b.bottom * Global.SCREEN_HEIGHT),
							paint);
				}				
			}
		}
	}    
    
    public void onBoxDown(SLayout.Box box, MotionEvent event)
    {
    	//Override this function
    }
    
    public boolean onBoxDownWithItemSel(SLayout.Box box, MotionEvent event) {
    	//Override this function
    	return false;
    }
    
    public void setBackgroundImage(int resourceID)
    {
    	backgroundImageResID = resourceID;
    }
    
    public void setLayout(SLayout layout)
    {
    	this.layout = layout;
    }
    
    protected void setText(String string, TextType type, boolean draw) {
    	this.sceneText = string;
    	this.sceneTextType = type;
    	if (draw)
    		repaint();
    }
    
    protected void clearText(boolean draw) {
    	this.sceneText = null;
    	this.sceneTextType = null;
    	if(draw)
    		repaint();
    }
    
    /* Trim the string into substrings of size ~ length.
     * Soft - 	true: soft constraint on length of string. It will try to find the best fit substring >= length.
     * 		  	false: [TODO] hard constraint on length of string. Have to find a substring within the given length.
     */
    protected static UPair<String, Integer> trimString(String string, int length, boolean soft) {
	    if(string == null || string.trim().equals("")){
	        return null;
	    }

	    int endIndex = 0;	    
	    if(string.length() > length) {
	        if(!soft) {
	        	StringBuffer sb = new StringBuffer(string.substring(0, length));
	        	endIndex = sb.lastIndexOf(" ");
	        	if (endIndex == -1)
	        		return new UPair<String, Integer>(sb.toString(), sb.length());
	        	else
	        		return new UPair<String, Integer>(sb.substring(0, endIndex), endIndex+1);
	        }
	        else {
	        	StringBuffer sb = new StringBuffer(string);
	            endIndex = sb.indexOf(" ", length);
	            if (endIndex == -1)
	            	return new UPair<String, Integer>(string, string.length());
	            else
	            	return new UPair<String, Integer>(sb.substring(0, endIndex), endIndex+1);
	        }  
	    }
	    
	    return new UPair<String, Integer>(string, string.length());
	}
    
    protected void showInventory(boolean b) {
    	showInventoryIcon = b;
    }
    
    protected void showBackButton(boolean b) {
    	showBackButton = b;
    }
    
    // return left, right, top, bottom in coords array
    public RectF getBoxPixelCoords(String boxName)
    {
		return layout.getBoxPixelCoords(boxName);
    }
    
    public void setForegroundImage(int resID)
    {
    	foregroundImageResID = resID;
    }
    
    public void unhideForegroundImage(String boxName)
    {
    	if(!unhiddenForegroundBoxes.contains(boxName))
    	{
    		unhiddenForegroundBoxes.add(boxName);
    		repaint();
    	}
    }
    
    public void hideForegroundImage(String boxName)
    {
    	unhiddenForegroundBoxes.remove(boxName);
    	repaint();
    }    
    
    public void resetForegroundImage()
    {
    	unhiddenForegroundBoxes.clear();
    }
    
	protected void deselectItem() {
		IItemMenu.itemInUse = null;
	}
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
	public SSceneView view;
	protected SLayout layout;
	
    private int backgroundImageResID;
    private int foregroundImageResID;
    
    private SLayout.Box selectedBox;
    
    private String sceneText = null;
    private TextType sceneTextType;
    private boolean showInventoryIcon = true;
    private boolean showBackButton = true;
    protected static final String DEFAULT_ITEMUSE_TEXT = "Can't Use this Item here!";

    private ArrayList<String> unhiddenForegroundBoxes;        
    
}
