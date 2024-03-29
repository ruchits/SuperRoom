package com.room;

import com.room.media.MSoundManager;

import android.os.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.app.*;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class Credits extends Activity implements OnTouchListener
{	
    private FrameLayout fl;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits);
        fl = (FrameLayout)findViewById(R.id.credits_screen);
        if ( fl == null ) Log.e("Credits", "fl is null");
        if ( fl != null ) fl.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if ( event.getAction() == MotionEvent.ACTION_UP ) {
			int screen_width = v.getWidth();
			int screen_height = v.getHeight();
		    int x = (int)event.getX();
		    int y = (int)event.getY();
		    Log.e("Credits", "x: " + x + ", y: " + y + " | where screen_width = " + screen_width + ", screen_height = " + screen_height);
		    if ( x < screen_width * 1 / 8 && y > screen_height * 5 / 6 ) {
				MSoundManager.getInstance().playSoundEffect(R.raw.swords);
		    	super.onBackPressed();
		    }
		}
		return true;
	}
}