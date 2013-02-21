package com.room;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import com.room.Global;
import com.room.media.MSoundManager;
import com.room.media.MVideoActivity;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UTransitionUtil;

public class MainMenu extends SSceneActivity {
	private Rect sceneBitmapSrc;
	private Rect sceneBitmapDst;
	private Bitmap mBitmap;
	Paint mPaint = new Paint();

	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.layout = SLayoutLoader.getInstance().mainMenu;
		
		Resources res = Global.mainActivity.getResources();
		mBitmap = BitmapFactory.decodeResource(res, R.drawable.main_menu);
		sceneBitmapSrc= new Rect(0,0,mBitmap.getWidth(), mBitmap.getHeight());
		sceneBitmapDst = new Rect(0,0,Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT);
		
		if(Global.DEBUG_SKIP_MENU)
        	startGame(false);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(mBitmap, sceneBitmapSrc, sceneBitmapDst, mPaint);
	}
    
	@Override
    public void onBoxTouched(String boxName)
    {	
		MSoundManager.getInstance().playSoundEffect(R.raw.swords);
    	if (boxName.equals("start")) {
    		startGame(true);
    	}
    	else if (boxName.equals("options")) {
    		showOptions();
    	}
    }
	
	private void startGame(boolean intro)
	{
		if (intro)  {
			MVideoActivity.videoToPlay = MVideoActivity.DAY1_VIDEO;
			Intent intent = new Intent(this, MVideoActivity.class);
			startActivity(intent);
			//UTransitionUtil.overridePendingTransition(this, R.anim.fade_in, R.anim.fade_out);
		}
		else {
			MVideoActivity.videoToPlay = null;
			Intent intent = new Intent(this, MVideoActivity.class);
		    startActivity(intent);
			UTransitionUtil.overridePendingTransition(this, R.anim.fade_in, R.anim.fade_out);
		}
	}
	
	private void showCredits() {
		  Intent intent = new Intent(this, Credits.class);
		  startActivity(intent);
	}
	
	private void showOptions() {
		  Intent intent = new Intent(this, Options.class);
		  startActivity(intent);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		MSoundManager.getInstance().playMusic(R.raw.music_menu);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		// TODO: Stop music_meny sound effect from MSoundManager. API missing!!
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	@Override
	protected void onDestroy()
	{		
		super.onDestroy();
		MSoundManager.getInstance().stopAllSounds();
	}
}
