package com.room.render;


import android.content.*;
import android.opengl.*;
import android.util.Log;
import android.view.MotionEvent;

public class RSurfaceView extends GLSurfaceView
{
    public RSurfaceView(Context context)
    {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        RRenderer.getInstance().glSurfaceView = this;
        
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(RRenderer.getInstance());
    }
            
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
    	RTouchController.getInstance().processTouchEvent(e);
        return true;
    }
    
    /** Show an event in the LogCat view, for debugging */
    /*private void dumpEvent(MotionEvent event)
    {
	    String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
	    		"POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
	    
	    StringBuilder sb = new StringBuilder();
	    
	    int action = event.getAction();
	    int actionCode = action & MotionEvent.ACTION_MASK;
	    
	    sb.append("event ACTION_" ).append(names[actionCode]);
	    
	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN
	    		|| actionCode == MotionEvent.ACTION_POINTER_UP)
	    {
		    sb.append("(pid " ).append(
		    		action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
		    
		    sb.append(")" );
	    }
	    
	    sb.append("[" );
	    
	    for (int i = 0; i < event.getPointerCount(); i++)
	    {
		    sb.append("#" ).append(i);
		    sb.append("(pid " ).append(event.getPointerId(i));
		    sb.append(")=" ).append((int) event.getX(i));
		    sb.append("," ).append((int) event.getY(i));
		    
		    if (i + 1 < event.getPointerCount())
		    	sb.append(";" );
	    }
	    
	    sb.append("]" );
	    
	    Log.d("TOUCH", sb.toString());
    }    */
    
    private float prevX = 0;
    private float prevY = 0;
}
