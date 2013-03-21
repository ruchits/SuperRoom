package com.room;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OptionManager {
	
	public static final String MUSIC_ON = "music_on";
	public static final String SOUND_ON = "sound_on"; //sound effects
	public static final String MASTER_VOLUME = "mastervolume";
	public static final float MASTER_VOLUME_DEF = 1.0f;
	public static final String MUSIC_VOLUME = "musicvolume";
	public static final float MUSIC_VOLUME_DEF = 1.0f;
	public static final String SOUND_VOLUME = "soundvolume";
	public static final float SOUND_VOLUME_DEF = 1.0f;
	
	static SharedPreferences pref;
	static Editor editor;

	public static boolean isMusicEnabled()
	{
		return pref.getBoolean(MUSIC_ON,true);
	}
	
	public static boolean isSoundEnabled()
	{
		return pref.getBoolean(SOUND_ON,true);
	}
	
	public static void setMusic(boolean state)
	{
		editor.putBoolean(MUSIC_ON, state);
		editor.commit();
	}
	
	public static void setSound(boolean state)
	{
		editor.putBoolean(SOUND_ON, state);
		editor.commit();
	}
	
	public static float getMasterVolume()
	{
		return pref.getFloat(MASTER_VOLUME,MASTER_VOLUME_DEF);
	}
	
	public static float getMusicVolume()
	{
		return pref.getFloat(MUSIC_VOLUME,MUSIC_VOLUME_DEF);
	}
	
	public static float getSoundVolume()
	{
		return pref.getFloat(SOUND_VOLUME,SOUND_VOLUME_DEF);
	}
	
	public static void setMasterVolume(float volume)
	{
		editor.putFloat(MASTER_VOLUME, volume);
		editor.commit();
	}
	
	public static void setMusicVolume(float volume)
	{
		editor.putFloat(MUSIC_VOLUME, volume);
		editor.commit();
	}
	
	public static void setSoundVolume(float volume)
	{
		editor.putFloat(SOUND_VOLUME, volume);
		editor.commit();
	}	
}