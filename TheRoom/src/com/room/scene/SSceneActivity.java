package com.room.scene;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SSceneActivity extends Activity
{
	public SSceneView view;
	public SLayout layout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        
        
        view = new SSceneView(this);
        setContentView(view);
    }
	
	private class SSceneView extends View
	{
		SSceneActivity activity;
		public SSceneView(Context context)
		{
			super(context);
			activity = (SSceneActivity)context;
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);
			activity.onDraw(canvas);
		}
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
    
    
	public void onDraw(Canvas canvas)
	{
		//Override this function
	}    
    
    public void onBoxTouched(String boxName)
    {
    	//Override this function
    }
    
}
