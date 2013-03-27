package com.room.media;

import com.room.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MPeepholeVideoActivity extends Activity
{
	private static final String videoPath = "android.resource://com.room/raw/peephole";
	private MFullScreenVideoView video = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);        
              
        setContentView(R.layout.video);
        video = (MFullScreenVideoView) findViewById(R.id.video);		
		
		video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

	        @Override
	        public void onCompletion(MediaPlayer mp)
	        {
	        	finish();
	        }
	    });
		
        video.setVideoPath(videoPath);
        video.start();		
	}
}
