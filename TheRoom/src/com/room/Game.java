package com.room;

import com.room.render.*;
import android.os.*;
import android.app.*;
import android.view.*;

public class Game extends Activity {
	
	private RSurfaceView mGLView;
	public static Game instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new RSurfaceView(this);
        
        setContentView(mGLView);	
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
}
