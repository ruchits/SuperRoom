package com.opengltest;

import com.opengltest.render.*;
import android.os.*;
import android.app.*;
import android.view.*;

public class MainActivity extends Activity
{
	private RSurfaceView mGLView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
        // Full screen settings
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new RSurfaceView(this);
        
        setContentView(mGLView);		
	}

}
