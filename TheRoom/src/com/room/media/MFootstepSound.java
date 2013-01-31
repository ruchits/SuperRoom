package com.room.media;

import com.room.Global;
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
			MMusic.playSound(Global.mainActivity, R.raw.footstep01);
			break;
		case 2:
			MMusic.playSound(Global.mainActivity, R.raw.footstep02);
			break;
		case 3:
			MMusic.playSound(Global.mainActivity, R.raw.footstep03);
			break;
		case 4:
			MMusic.playSound(Global.mainActivity, R.raw.footstep04);
			break;			
		}
	}
	
}
