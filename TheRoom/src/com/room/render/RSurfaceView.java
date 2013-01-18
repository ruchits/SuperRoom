package com.room.render;


import android.content.*;
import android.opengl.*;
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
           
    //tbd willc - implement better camera!!!    
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction())
        {
            case MotionEvent.ACTION_MOVE:

                float dx = x - prevX;
                float dy = y - prevY;
                
                RRenderer.getInstance().cameraTurn(-dx*0.5f);
                RRenderer.getInstance().cameraForward(-dy*0.2f);
                
                requestRender();
        }

        prevX = x;
        prevY = y;
        return true;
    }
    
    private float prevX = 0;
    private float prevY = 0;
}
