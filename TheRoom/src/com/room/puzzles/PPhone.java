package com.room.puzzles;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.room.R;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PPhone extends SSceneActivity
{
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzlePhone);
		//setBackgroundImage(TAG, R.drawable.puzzle_phone);
	}
	
	@Override	
	protected void onResume() {
		super.onResume();
		setBackgroundImage(R.drawable.puzzle_phone);
	}
	
	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
    {	
    	Log.d("BOXCLICK",box.name);

			switch (box.name.charAt(0))
			{
				case '0':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_0);
				break;
				case '1':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_1);
				break;
				case '2':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_2);
				break;
				case '3':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_3);
				break;
				case '4':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_4);
				break;
				case '5':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_5);
				break;
				case '6':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_6);
				break;
				case '7':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_7);
				break;
				case '8':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_8);
				break;
				case '9':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_9);
				break;
				case '*':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_star);
				break;
				case '#':
					MSoundManager.getInstance().playSoundEffect(R.raw.phone_sharp);
			}
    }
}
