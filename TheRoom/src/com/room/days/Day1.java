package com.room.days;

import com.room.R;
import com.room.item.IItemManager;
import com.room.media.MSoundManager;

public class Day1
{
	//put all day 1 flags here
	public static boolean isDeadManRevealed;
	public static boolean isCellphoneRinging;
	public static boolean hasDeadManCoverBeenExamined;
	public static boolean isCellphoneRevealed;
	public static boolean hasPickedUpKnife;
	public static boolean hasPickedUpCellphone;
	
	public static void reset()
	{
		isDeadManRevealed = false;
		isCellphoneRinging = false;
		hasDeadManCoverBeenExamined = false;
		isCellphoneRevealed = false;
		hasPickedUpKnife = false;
		hasPickedUpCellphone = false;
		
		
		IItemManager.getInstance().clearInventory();
		IItemManager.getInstance().addItemToInventory("journal");
		IItemManager.getInstance().addItemToInventory("photo");
		
		MSoundManager.getInstance().addLocationSensitiveSound(R.raw.sink_waterdrop, -18.6f,-56.6f,10,40);
		MSoundManager.getInstance().addLocationSensitiveSound(R.raw.fakedoor_knock, -20,40,10,20);
		MSoundManager.getInstance().addLocationSensitiveSound(R.raw.urn_ohm, 3,33,10,20);	
	}
}