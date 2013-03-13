package com.room.media;

import com.room.R;
import com.room.render.RMath;

public class MFootstepSound
{	
	public static void playRandomStep()
	{
		int soundNum = RMath.getRandInt(1, 4);
		switch(soundNum)
		{
		case 1:
			MSoundManager.getInstance().playSoundEffect(R.raw.footstep01);
			break;
		case 2:
			MSoundManager.getInstance().playSoundEffect(R.raw.footstep02);
			break;
		case 3:
			MSoundManager.getInstance().playSoundEffect(R.raw.footstep03);
			break;
		case 4:
			MSoundManager.getInstance().playSoundEffect(R.raw.footstep04);
			break;			
		}
	}
	
}
