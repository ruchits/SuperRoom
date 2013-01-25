package com.room.render;

import com.room.Global;
import com.room.R;
import com.room.media.Music;

import android.content.*;
import android.opengl.*;
import android.os.*;
import android.app.*;
import android.view.*;

public class RRenderActivity extends Activity
{
	public RRenderView view;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);           
        
        view = new RRenderView(this);
        setContentView(view);        
    }
    
    private class RRenderView extends GLSurfaceView
    {
        public RRenderView(Context context)
        {
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(RRenderer.getInstance());
        }    
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
    	RTouchController.getInstance().processTouchEvent(e);
        return true;
    }    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	//TEMPORARY! Use the volume keys to change days
    	
    	switch(keyCode)
    	{
	    	case KeyEvent.KEYCODE_VOLUME_DOWN:
	    		if(Global.CURRENT_DAY > Global.FIRST_DAY)
	    			Global.CURRENT_DAY --;
	    	break;
	    	
	    	case KeyEvent.KEYCODE_VOLUME_UP:
	    		if(Global.CURRENT_DAY < Global.LAST_DAY)
	    			Global.CURRENT_DAY ++;	    		
	    	break;
    	}
    	return true;
    }    
    
	@Override
	protected void onResume() {
	      super.onResume();
	      Music.loadAll(this);
	      Music.playBGmusic(this, R.raw.wind);
	      Music.playTimedSound(this);
	}
    
	@Override
	protected void onPause() {
	      super.onPause();
	      Music.stopSound(this);
	}
	
	@Override
	protected void onStop() {
	      super.onStop();
	      Music.stopSound(this);
	}
	
}



