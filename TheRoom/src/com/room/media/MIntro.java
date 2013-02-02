package com.room.media;

import com.room.Global;
import com.room.R;
import com.room.render.RRenderActivity;

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
import android.widget.VideoView;

public class MIntro extends Activity implements OnTouchListener{
	VideoView video = null;
	int stopPosition = 0;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		MMusic.stopBGmusic(Global.mainActivity);
		setContentView(R.layout.video);
		video = (VideoView) findViewById(R.id.video);
		video.setOnTouchListener(this);
		video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

	        @Override
	        public void onCompletion(MediaPlayer mp) {
	        	startActivity (new Intent(Global.mainActivity, RRenderActivity.class));
	        }
	    });
		
		video.canPause();
		video.setVideoPath("android.resource://com.room/raw/sample");
		video.start();
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
        builder.setMessage(R.string.skip_intro)
               .setCancelable(true)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   Global.RESUME_MUSIC = true;
                	   startActivity (new Intent(Global.mainActivity, RRenderActivity.class));
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