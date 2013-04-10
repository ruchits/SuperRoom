package com.room;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.room.Global;
import com.room.media.MSoundManager;
import com.room.media.MVideoActivity;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;

public class DaySelection extends SSceneActivity
{
	private Bitmap lockImage;
	private RectF[] lockArea = new RectF[Global.LAST_DAY];
	private float lockWidth, lockHeight;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().daySelection);
		
		showInventory(false);
		showBackButton(true);
		
		//no icon will be drown to lock_1 area
		for ( int i = 0; i < Global.LAST_DAY; ++i )
		{
			lockArea[i] = getBoxPixelCoords("lock_"+Integer.toString(i+1));
		}
		lockWidth = lockArea[0].right - lockArea[0].left;
		lockHeight = lockArea[0].bottom - lockArea[0].top;
		lockImage = UBitmapUtil.loadScaledBitmap(R.drawable.item_key,
				(int) lockWidth, (int) lockHeight,false);
		
		if(Global.DEBUG_SKIP_MENU)
        	startGame(Global.getCurrentDay(), true);
	}
    
	@Override
	public void onDraw(Canvas canvas, Paint paint)
	{
		super.onDraw(canvas, paint);
		int highestStage = OptionManager.getHighestStage();
		for ( int i = highestStage; i < Global.LAST_DAY; ++i )
		{
			canvas.drawBitmap(lockImage,
					lockArea[i].left, lockArea[i].top, paint);
		}
	}
	
	@Override
    public void onBoxDown(SLayout.Box box, MotionEvent event)
    {	
		int dayNum = Integer.parseInt(box.name);
		
		if ( dayNum > OptionManager.getHighestStage() ) {
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(200);
		}
		else {
			MSoundManager.getInstance().playSoundEffect(R.raw.swords);
			startGame(dayNum, false);
		}
    }
	
	private void startGame(int dayNum, boolean skipVideo)
	{
		if(skipVideo)
		{
			MVideoActivity.videoToPlay = null;
		}		
		else switch(dayNum)
		{
			case 1:
				MVideoActivity.videoToPlay = MVideoActivity.DAY1_VIDEO;		
			break;
			case 2:
				MVideoActivity.videoToPlay = MVideoActivity.DAY2_VIDEO;		
			break;
			case 3:
				MVideoActivity.videoToPlay = MVideoActivity.DAY3_VIDEO;		
			break;
			case 4:
				MVideoActivity.videoToPlay = MVideoActivity.DAY4_VIDEO;		
			break;
			case 5:
				MVideoActivity.videoToPlay = MVideoActivity.DAY5_VIDEO;		
			break;		
		}
		
		Global.setDay(dayNum);
		
		Intent intent = new Intent(this, MVideoActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		setBackgroundImage(R.drawable.day_selection);
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
	}
}
