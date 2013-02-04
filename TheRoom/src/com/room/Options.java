package com.room;
import com.room.R;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Options extends PreferenceActivity {

	   private static final String OPT_MUSIC = "bgmusic";
	   private static final boolean OPT_MUSIC_DEF = true;
	   private static final String OPT_SOUNDS = "soundeffect";
	   private static final boolean OPT_SOUNDS_DEF = true;   

	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      addPreferencesFromResource(R.xml.options);
	   }

	   public static boolean isMusicEnabled() {
	      return PreferenceManager.getDefaultSharedPreferences(Global.mainActivity)
	            .getBoolean(OPT_MUSIC, OPT_MUSIC_DEF);
	   }
	   
	   public static boolean isSoundEffectsEnabled() {
	      return PreferenceManager.getDefaultSharedPreferences(Global.mainActivity)
	            .getBoolean(OPT_SOUNDS, OPT_SOUNDS_DEF);
	   }
}
