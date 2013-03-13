package com.room;
import com.room.R;
import com.room.media.MSoundManager;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
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
	      
	      Preference bgPref = (Preference) findPreference(OPT_MUSIC);
	      bgPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
	             public boolean onPreferenceClick(Preference preference) {
	            	if (!isMusicEnabled())
	            		MSoundManager.getInstance().stopMusic();
	            	//TODO: Add some bg music for Options page.
	            	
					return false;
	             }
	         });
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
