package com.opengltest.render;


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
        
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        RRenderer.getInstance().glSurfaceView = this;
        
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(RRenderer.getInstance());
    }
    
    float mPreviousX = 0;
    float mPreviousY = 0;
    public static float zoom = 1;
    
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

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                zoom+=dy*0.01;
                
                if (zoom < 0.8f)
                	zoom = 0.8f;
                
                if (zoom > 2f)
                	zoom = 2f;                
                
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }    
}
