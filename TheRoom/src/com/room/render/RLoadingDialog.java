package com.room.render;

import com.room.R;
import com.room.media.MFullScreenVideoView;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class RLoadingDialog extends Dialog
{
	private static final String loadingVideoPath = "android.resource://com.room/raw/loading";
	
	public RLoadingDialog(Context context)
	{
		super(context,R.style.FullscreenDialogTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		
		setCancelable(false);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.video);
        loadingVideo = (MFullScreenVideoView) findViewById(R.id.video);
		
		loadingVideo.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {                    
		    @Override
		    public void onPrepared(MediaPlayer mp) {
		        mp.setLooping(true);
		    }
		});
		
		loadingVideo.setVideoPath(loadingVideoPath);

		super.onCreate(savedInstanceState);
    }	
    
    @Override
    public void onStart()
    {
    	loadingVideo.start();
    }
    
    @Override
    public void onStop()
    {
    	loadingVideo.stopPlayback();
    	
    }
    
	private MFullScreenVideoView loadingVideo = null;    	
}
