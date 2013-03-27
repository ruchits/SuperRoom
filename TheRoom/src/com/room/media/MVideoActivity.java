package com.room.media;

import com.room.Global;
import com.room.R;
import com.room.render.RRenderActivity;
import com.room.utils.UTransitionUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MVideoActivity extends Activity implements OnTouchListener{
	private MFullScreenVideoView video = null;
	private int stopPosition = 0;
	
	public static final String DAY1_VIDEO = "android.resource://com.room/raw/day1";
	public static final String DAY2_VIDEO = "android.resource://com.room/raw/sample";
	public static final String DAY3_VIDEO = "android.resource://com.room/raw/sample";
	public static final String DAY4_VIDEO = "android.resource://com.room/raw/sample";
	public static final String DAY5_VIDEO = "android.resource://com.room/raw/sample";
	public static final String ENDING_VIDEO = "android.resource://com.room/raw/sample";
	public static String videoToPlay = null;
	
	private boolean isTransitionedFromMenu = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		MSoundManager.getInstance().stopAndReleaseMusic();
		setContentView(R.layout.video);
		video = (MFullScreenVideoView) findViewById(R.id.video);
		video.setOnTouchListener(this);
		video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

	        @Override
	        public void onCompletion(MediaPlayer mp) {
	        	startActivity (new Intent(Global.mainActivity, RRenderActivity.class));
	        	UTransitionUtil.overridePendingTransition(MVideoActivity.this,R.anim.fade_in, R.anim.fade_out);
	        }
	    });
		
		video.canPause();
		
		if(videoToPlay!=null)
		{
			video.setVideoPath(videoToPlay+"");
			videoToPlay = null;
			video.start();
		}
		else
		{
			startActivity (new Intent(Global.mainActivity, RRenderActivity.class));
		}
		
		isTransitionedFromMenu = true;
	}
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if(!isTransitionedFromMenu)
		{			
			if(videoToPlay!=null)
			{
				video.setVideoPath(videoToPlay+"");
	        	videoToPlay = null;
				video.start();
			}
			else
			{
				finish();
				UTransitionUtil.overridePendingTransition(this,R.anim.fade_in, R.anim.fade_out);
			}
		}
		
		isTransitionedFromMenu = false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
    	stopPosition = video.getCurrentPosition();
    	video.pause();
    	Log.e("MIntro","onTouch| stopPosition: " + stopPosition);
		showDialog(1);
		return false;
	}
	
    @Override
    protected Dialog onCreateDialog(int id) { //id is ignored

        Dialog dialog = null;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.skip_video)
               .setCancelable(true)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   startActivity (new Intent(Global.mainActivity, RRenderActivity.class));                	   
                	   //UTransitionUtil.overridePendingTransition(MVideoActivity.this,R.anim.fade_in, R.anim.fade_out);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.e("MIntro","onClick| stopPosition: " + stopPosition);
					video.seekTo(stopPosition);
					video.start();
				}
			});
        dialog = builder.create();
        return dialog;
    }
    
}