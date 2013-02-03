package com.room.utils;

import java.lang.reflect.Method;

import android.app.Activity;

public class UTransitionUtil {
	
	private static Method overridePendingTransition;
	static {
		try {
			overridePendingTransition = Activity.class.getMethod("overridePendingTransition", new Class[] {Integer.TYPE, Integer.TYPE}); //$NON-NLS-1$
		}
		catch (NoSuchMethodException e) {
			overridePendingTransition = null;
		}
	}

	/**
	* Calls Activity.overridePendingTransition if the method is available (>=Android 2.0)
	* @param activity the activity that launches another activity
	* @param animEnter the entering animation
	* @param animExit the exiting animation
	*/
	public static void overridePendingTransition(Activity activity, int animEnter, int animExit) {
		if (overridePendingTransition!=null) {
			try {
				overridePendingTransition.invoke(activity, animEnter, animExit);
			} catch (Exception e) {
				// do nothing
			}
		}
	}

}
