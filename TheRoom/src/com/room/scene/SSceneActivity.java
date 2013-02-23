package com.room.scene;

import com.room.Global;
import com.room.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
        backgroundImage = null;
        view = new SSceneView(this);
        setContentView(view);
    }
	
	private class SSceneView extends View
	{		
		public SSceneView(Context context)
		{
			super(context);
			paint = new Paint();
			activity = (SSceneActivity)context;			  
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
			activity.onDraw(canvas, paint);
		}
		private SSceneActivity activity;
		private Paint paint;
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
		}
		
        return true;
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
			paint.setColor(0x000000);
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
    		Resources res = Global.mainActivity.getResources();
    		backgroundImage = BitmapFactory.decodeResource(res, resourceID);
    	}
    }
    
    public void setLayout(SLayout layout)
    {
    	this.layout = layout;
    }
    
    // return left, right, top, bottom in coords array
    public void getBoxCoords(String boxName,  int []coords)
    {
		layout.getBoxCoords(boxName, coords);
    }

    public int getBoxNum(String boxName)
    {
    	return layout.getBoxNum(boxName);
    }
    
	public SSceneView view;
	protected SLayout layout;        
    private Bitmap backgroundImage;
    
}
