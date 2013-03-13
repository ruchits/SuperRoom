package com.room.render;

import com.room.Global;
import com.room.R;
import com.room.media.MSoundManager;
import com.room.utils.UTransitionUtil;

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
    	Global.renderActivity = this;
    	
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);           
        Global.progDailog = ProgressDialog.show(this, "Please wait", "Loading game ...", true);
        
        view = new RRenderView(this);
        setContentView(view);                
    }
    
    private class RRenderView extends GLSurfaceView
    {
        public RRenderView(Context context)
        {
            super(context);
            
            //set GL resolution
            getHolder().setFixedSize(Global.GL_WIDTH, Global.GL_HEIGHT);
            
            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);            
            
            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(RRenderer.getInstance());
        }    
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
    	boolean processed = RTopButtons.getInstance().processTouchEvent(e);
    	
    	if(!processed)
    		RTouchController.getInstance().processTouchEvent(e);
    	
        return true;
    }    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	switch(keyCode)
    	{
	    	case KeyEvent.KEYCODE_BACK:
	    		showDialog(1); //we're hard coding the id since there will be only one dialog
	    	return true;
    	}
    	return RKeyController.getInstance().onKeyDown(keyCode, event);
    }    
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
    	return RKeyController.getInstance().onKeyUp(keyCode, event);
    }    

    @Override
    protected Dialog onCreateDialog(int id) { //id is ignored
        Dialog dialog = null;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.return_home)
               .setCancelable(true)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   finish();
                	   UTransitionUtil.overridePendingTransition(RRenderActivity.this,R.anim.fade_in, R.anim.fade_out);
                   }
               })
               .setNegativeButton(R.string.cancel, null);
        dialog = builder.create();
        return dialog;
    }
    
	@Override
	protected void onResume()
	{
		super.onResume();
		
		//tbd - replace with real bg music here:
		MSoundManager.getInstance().playMusic(R.raw.music_game);
	}
	
	@Override
	protected void onDestroy()
	{		
		super.onDestroy();
		MSoundManager.getInstance().stopAndReleaseLocationSensitiveSounds();
	}		
}



