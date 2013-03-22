package com.room.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.room.Global;
import com.room.Global.TextType;
import com.room.R;
import com.room.item.IItemManager;
import com.room.item.IItemMenu;
import com.room.item.IItemManager.Item;
import com.room.media.MSoundManager;
import com.room.scene.SLayout.Box;
import com.room.utils.UBitmapUtil;
import com.room.utils.UPair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SSceneActivity extends Activity
{	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);                
        backgroundImageResID = -1;
        foregroundImageResID = -1;
        unhiddenForegroundBoxes = new ArrayList<String>();
        boxBitmapTable = new HashMap<String,Bitmap>();
        view = new SSceneView(this);
        setContentView(view);
          
        if(!isLayoutInitialized)
        {
        	inventory = UBitmapUtil.loadBitmap(R.drawable.icon_items,false);
        	backbutton = UBitmapUtil.loadBitmap(R.drawable.icon_back,false);
        	
        	SLayout sceneActivityLayout = SLayoutLoader.getInstance().sceneActivity;        	
        	inventoryBtnDestination = sceneActivityLayout.getBoxPixelCoords("inventoryBtn");
        	backBtnDestination = sceneActivityLayout.getBoxPixelCoords("backBtn");
        	subtitleTextDestination = sceneActivityLayout.getBoxPixelCoords("subtitleTextArea");
        	centerTextDestination = sceneActivityLayout.getBoxPixelCoords("centerTextArea");
        	itemPickupImgDestination = sceneActivityLayout.getBoxPixelCoords("itemPickupImg");
        	
        	SLayout itemMenuLayout = SLayoutLoader.getInstance().itemMenu;
        	itemTextDestination = itemMenuLayout.getBoxPixelCoords("textBox");	

            linearGradientShader = new LinearGradient(0, 0,0,Global.SCREEN_HEIGHT,0x30000000,0xE0000000,TileMode.CLAMP);
            linearGradientRect = new Rect(0,0,Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT);
            
			isLayoutInitialized = true;
        }
		
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
			
						
			if(activity.itemPickedUp!=null)
			{
				paint.setShader(linearGradientShader);
				canvas.drawRect(linearGradientRect, paint);
				paint.setShader(null);
				
				setText(activity.itemPickedUp.getName()+SSceneActivity.ITEM_PICKUP_TEXT,TextType.TEXT_SUBTITLE,false);
				Bitmap itemBmp = UBitmapUtil.loadBitmap(activity.itemPickedUp.getResID(), false);
				canvas.drawBitmap(itemBmp,null,itemPickupImgDestination,paint);
				itemBmp.recycle();
			}
			
			else
			{
				// Draw the Icons
				if(activity.showInventoryIcon)
				{
					drawIconBG(canvas, paint, SSceneActivity.inventoryBtnDestination);
				
					Bitmap inventory;
					if (IItemMenu.itemInUse != null)
					{
						inventory = IItemMenu.itemInUse.getThumbnailBitmap();
					}
					else
					{
						inventory = SSceneActivity.inventory;
					}
				
					canvas.drawBitmap(inventory, null, SSceneActivity.inventoryBtnDestination, paint);
				}
				if (activity.showBackButton)
				{
	    			drawIconBG(canvas, paint, SSceneActivity.backBtnDestination);
					Bitmap backButton = SSceneActivity.backbutton;
					canvas.drawBitmap(backButton, null, SSceneActivity.backBtnDestination, paint);
				}			
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
					float factor = (float)(itemTextDestination.right - itemTextDestination.left)/itemTextBitmap.getWidth();
					canvas.drawBitmap(itemTextBitmap,
							null,							
							new RectF(itemTextDestination.left,
									itemTextDestination.top,
									itemTextDestination.right,
									itemTextDestination.top+itemTextBitmap.getHeight()*factor),
							paint);
					
					break;
					
				case TEXT_SUBTITLE:
					factor = (float)(subtitleTextDestination.right - subtitleTextDestination.left)/subtitleBitmap.getWidth();
					canvas.drawBitmap(subtitleBitmap,
							null,							
							new RectF(subtitleTextDestination.left,
									subtitleTextDestination.top,
									subtitleTextDestination.right,
									subtitleTextDestination.top+subtitleBitmap.getHeight()*factor),
							paint);
					
	    			break;
	    		
				case TEXT_CENTERED:
					factor = (float)(centerTextDestination.right - centerTextDestination.left)/centeredTextBitmap.getWidth();
					canvas.drawBitmap(centeredTextBitmap,
							null,							
							new RectF(centerTextDestination.left,
									centerTextDestination.top,
									centerTextDestination.right,
									centerTextDestination.top+centeredTextBitmap.getHeight()*factor),
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
			//if item modal is up, then kill it
			if(this.itemPickedUp != null)
			{
				this.itemPickedUp = null;
				setText("",TextType.TEXT_SUBTITLE,false);
				MSoundManager.getInstance().playSoundEffect(R.raw.tick);
				repaint();
				return true;
			}
			
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
				
				repaint();
			}
			
			if(showInventoryIcon) {
				// Handle touch events to inventory icon and back button on SceneLayout.
				boolean result = getTouchAt(event.getX(), event.getY(), inventoryBtnDestination);
				if (result) {
					MSoundManager.getInstance().playSoundEffect(R.raw.tick);
					Intent intent = new Intent(this, IItemMenu.class);
					startActivity(intent);
				}
			}
			if (showBackButton) {
				boolean result = getTouchAt(event.getX(), event.getY(), backBtnDestination);
				if (result)
				{
					MSoundManager.getInstance().playSoundEffect(R.raw.tick);
					deselectItem();
					finish();				
				}
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
    
    private boolean getTouchAt(float x, float y, RectF dest)
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
		//draw background
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
		
		//draw any revealed foreground
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
							b.getPixelCoords(),
							paint);
				}				
			}
		}
		
		//draw any bitmap mapped boxes
		Iterator<Map.Entry<String,Bitmap>> boxBitmapTableIt
			= boxBitmapTable.entrySet().iterator();
		
		while(boxBitmapTableIt.hasNext())
		{
			Map.Entry<String,Bitmap> entry = boxBitmapTableIt.next();
			
			Box box = layout.getBoxWithName(entry.getKey());
			
			if(box!=null)
			{
				canvas.drawBitmap(entry.getValue(),
						null,							
						box.getPixelCoords(),
						paint);
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
    
	@Override
	protected void onResume()
	{
		super.onResume();
		if(this.sceneText!=null && this.sceneText.equals(DEFAULT_ITEMUSE_TEXT))
			this.sceneText = null;
	}    
    
    public void setBackgroundImage(int resourceID)
    {
    	backgroundImageResID = resourceID;
    }
    
    public void setLayout(SLayout layout)
    {
    	this.layout = layout;
    }
    
    protected void showItemPickUpModal(String itemID)
    {
    	MSoundManager.getInstance().playSoundEffect(R.raw.tick);
    	Item item = IItemManager.getInstance().getItemFromPool(itemID);
    	itemPickedUp = item;
    	repaint();
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
	
	public void mapBoxBitmap(String boxName, Bitmap bmp)
	{
		boxBitmapTable.put(boxName, bmp);
	}
	
	public void upmapBoxBitmap(String boxName)
	{
		boxBitmapTable.remove(boxName);
	}	
	
	public void upmapAllBoxBitmaps()
	{
		boxBitmapTable.clear();
	}
	
	public SSceneView view;
	protected SLayout layout;
	
    private int backgroundImageResID;
    private int foregroundImageResID;
    private ArrayList<String> unhiddenForegroundBoxes;
    
    private HashMap<String,Bitmap> boxBitmapTable;
    
    private SLayout.Box selectedBox;
    private Item itemPickedUp = null;
    
    private String sceneText = null;
    private TextType sceneTextType;
    private boolean showInventoryIcon = true;
    private boolean showBackButton = true;        
    
    private static final String DEFAULT_ITEMUSE_TEXT = "Can't use this item here.";
    private static final String ITEM_PICKUP_TEXT = " added to iventory.";
    
    //scene activity top layout vars    
	private static boolean isLayoutInitialized = false;	
	private static Bitmap inventory;
	private static Bitmap backbutton;	
	private static Bitmap itemTextBitmap = Bitmap.createBitmap(500, 200, Bitmap.Config.ARGB_8888);
	private static Bitmap subtitleBitmap = Bitmap.createBitmap(800, 100, Bitmap.Config.ARGB_8888);
	private static Bitmap centeredTextBitmap = Bitmap.createBitmap(500, 70, Bitmap.Config.ARGB_8888);	
	private static RectF inventoryBtnDestination;
	private static RectF backBtnDestination;	
	private static RectF subtitleTextDestination;	
	private static RectF centerTextDestination;	
	private static RectF itemTextDestination;
	private static RectF itemPickupImgDestination;
    private static LinearGradient linearGradientShader;
    private static Rect linearGradientRect;
    
}
