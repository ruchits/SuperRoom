package com.room.scene;

import com.room.Global;
import com.room.Global.TextType;
import com.room.R;
import com.room.item.IItemMenu;
import com.room.item.IItems;
import com.room.scene.SLayout.Box;
import com.room.utils.UBitmapUtil;
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
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);                
        backgroundImage = null;
        view = new SSceneView(this);
        setContentView(view);
        
        Resources res = Global.mainActivity.getResources();
		inventory = BitmapFactory.decodeResource(res, R.drawable.backpack);
		backbutton = BitmapFactory.decodeResource(res, R.drawable.back);
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
			
			// Set strokePaint for subtitle track.
			setStrokePaint();
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
			activity.onDraw(canvas, paint);
			
			// Draw the Icons
			if(activity.showInventoryIcon) {
    			drawIconBG(canvas, paint, SSceneActivity.invDestinationF);
				Bitmap inventory = SSceneActivity.inventory;
				canvas.drawBitmap(inventory, null, SSceneActivity.invDestination, paint);
			}
			if (activity.showBackButton) {
    			drawIconBG(canvas, paint, SSceneActivity.backbtnDestinationF);
				Bitmap backButton = SSceneActivity.backbutton;
				canvas.drawBitmap(backButton, null, SSceneActivity.backbtnDestination, paint);
			}
			
			// Draw text if any.
			String text = activity.text;
			TextType type = activity.textType;
			
			if (text != null) {
				// Set up the paint object for this text type.
				setTextPaint(type);
				int x, y, gap;
				
				switch (type) {
				case TEXT_ITEM_DESCR:	
					// Get the description box.
					Box descriptionBox = IItems.getInstance().getDescriptionBox();
					
					gap = (int) (paint.descent() - paint.ascent());
					int spaceBtnBoxnText = 2*gap;
					x = (int) (descriptionBox.left*Global.SCREEN_WIDTH);
					y = (int) (descriptionBox.bottom*Global.SCREEN_HEIGHT + spaceBtnBoxnText);
				
					// Draw the description string below the box.
					// Split a long string into TEXT_LENGTH blocks for text wrapping.
					String temp = text;
					while(temp.length() > 0) {
						// Trim the string to fit one line.
						// TODO: This logic needs to be more robust. Especially once we add hard constraint for trimming.
						UPair<String, Integer> res = SSceneActivity.trimString(temp, Global.TEXT_LENGTH, true);
					
						canvas.drawText(res.getLeft(), x, y, paint);
						y += gap;
					
						int numCharRead = res.getRight();
						if(numCharRead == temp.length())
							break;
						else
							temp = temp.substring(numCharRead, temp.length());		
					}		
					break;
	    		
				case TEXT_SUBTITLE:
					gap = (int) (paint.descent() - paint.ascent());
					x=(int) Global.SCREEN_WIDTH/2;
					y=(int) Global.SCREEN_HEIGHT-gap;
					canvas.drawText(text, x, y, strokePaint);
					canvas.drawText(text, x, y, paint);
	    			break;
	    		
				case TEXT_CENTERED:
					x=(int) Global.SCREEN_WIDTH/2;
					y=(int) Global.SCREEN_HEIGHT/2;
					canvas.drawText(text, x, y, paint);
					break;
					
				default:
					//do nothing - must specify type to display text.
				}
				
			}
		}
		
		private void drawIconBG(Canvas canvas, Paint paint, RectF rect) {
			paint.setColor(Color.WHITE);
			paint.setAlpha(50);
			float cx= rect.left + (rect.right-rect.left)/2;
			float cy = rect.top + (rect.bottom-rect.top)/2;
			float radius = Math.min((rect.right-rect.left)/2, (rect.bottom-rect.top)/2);
			canvas.drawCircle(cx, cy, radius, paint);
			paint.reset();
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
		}
		
		private void setTextPaint(Global.TextType type) {
			switch (type) {
				case TEXT_ITEM_DESCR:
					paint.setColor(Color.BLACK); 
					paint.setTextSize(Global.FONT_SIZE);
					paint.setTypeface(Typeface.MONOSPACE);
					paint.setTextAlign(Align.LEFT);
					break;
				case TEXT_SUBTITLE:
					paint.setColor(Color.WHITE); 
					paint.setTextSize(Global.FONT_SIZE);
					paint.setTypeface(Typeface.MONOSPACE);
					paint.setTextAlign(Align.CENTER);
					break;
				case TEXT_CENTERED:
					paint.setColor(Color.BLACK);
					paint.setTextSize(Global.FONT_SIZE_BIG);
					paint.setTypeface(Typeface.MONOSPACE);
					paint.setTextAlign(Align.CENTER);
					break;
				default:
					paint.setColor(Color.BLACK); 
					paint.setTextSize(Global.FONT_SIZE);
			}
		}
		
		private SSceneActivity activity;
		private Paint paint;
		private Paint strokePaint;
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
			String boxName = null;			
			boxName = layout.getBoxAtPixel(event.getX(), event.getY());
			if(boxName != null)
				onBoxTouched(boxName);
			
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
		
        return true;
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
		if(backgroundImage!=null)
		{
			canvas.drawBitmap(backgroundImage,
					new Rect(0,0,backgroundImage.getWidth(),backgroundImage.getHeight()),
					new Rect(0,0,Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT), paint);
		}
		else
		{
			paint.setColor(Color.WHITE);
			canvas.drawRect(new Rect(0,0,Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT), paint);
		}
	}    
    
    public void onBoxTouched(String boxName)
    {
    	//Override this function
    }
    
    public void setBackgroundImage(int resourceID)
    {
    	if(resourceID == -1)
    	{
    		backgroundImage = null;
    	}
    	else
    	{
    		backgroundImage = UBitmapUtil.decodeSampledBitmapForResolution(resourceID, Global.ResType.HI_RES); 
    	}
    }
    
    public void setLayout(SLayout layout)
    {
    	this.layout = layout;
    }
    
    protected void setText(String string, TextType type, boolean draw) {
    	this.text = string;
    	this.textType = type;
    	if (draw)
    		repaint();
    }
    
    protected void clearText(boolean draw) {
    	this.text = null;
    	this.textType = null;
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

	    StringBuffer sb = new StringBuffer(string);
	    int endIndex = 0;
	    
	    if(sb.length() >= length){
	        if(!soft) {
	        	endIndex = length;
	        	return new UPair<String, Integer>(sb.substring(0, endIndex), endIndex);
	        }
	        else {
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
    
	public SSceneView view;
	protected SLayout layout;        
    private Bitmap backgroundImage;
    
    private String text = null;
    private TextType textType;
    private boolean showInventoryIcon = true;
    private boolean showBackButton = true;
    
}
