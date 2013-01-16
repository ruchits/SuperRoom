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
}
