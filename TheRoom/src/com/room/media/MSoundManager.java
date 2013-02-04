package com.room.media;

import com.room.Options;
import com.room.R;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class MMusic {
   private static MediaPlayer mMainSound = null;
   private static SoundPool mSoundPool_BG = null;
   private static SoundPool mSoundPool_SE = null; //sound effects
   private static HashMap<Integer, Integer> mSoundHashMap_BG = null;
   private static HashMap<Integer, Integer> mSoundHashMap_SE = null;
   static Random mRandom;
   
   private static Timer mTimer = null;
   private static TimerTask mTimerTask = null;
   static Iterator it = null;

   public static void playSound(Context context, int resource)
   {
	   if ( Options.getSoundEffect(context) && mSoundHashMap_SE != null && mSoundHashMap_SE.containsKey(resource) )
	   {
           mSoundPool_SE.play(mSoundHashMap_SE.get(resource), 0.3f, 0.3f, 1, 0, 1f);
           Log.e("MMusic", "Played sound");
       }
   }
   
   public static void playTimedSound(final Context context)
   {
	   if (Options.getBGMusic(context)) {
		   loadAll_BG(context);
		   mTimer = new Timer();
		   it = (Iterator) mSoundHashMap_BG.entrySet().iterator();
		   mTimerTask = new TimerTask() {
			   public void run() {
				   if (!it.hasNext()){
					   it = (Iterator) mSoundHashMap_BG.entrySet().iterator();
				   }
				   playSound(context, (Integer)((HashMap.Entry)it.next()).getKey());
			   }
		   };
		   mTimer.schedule(mTimerTask, 15000, 15000);
	  }
   }

   public static void playBGmusic(Context context, int resource)
   {
	   if (Options.getBGMusic(context)){
		   Log.e("MMusic", "Play background music");
		   if ( mMainSound != null ) mMainSound.release();
		   mMainSound = MediaPlayer.create(context, resource);
		   mMainSound.setLooping(true);
		   mMainSound.start();
	   }
   }
   
   public static boolean isPlaying(Context context)
   {
	   if ( mMainSound != null )
	   {
	       return mMainSound.isPlaying();
	   }
	   return false;
   }
   
   //The title is bit misleading but it was made that way to be consistent with playBGmusic
   public static void loadSEmusic(Context context)
   {
	   if (Options.getSoundEffect(context)) loadAll_SE(context);
   }
   
   public static void updateSE(Context context, int x, int y, int z)
   {
	   if ( Options.getSoundEffect(context) )
	   {
		   //TODO
	   }
   }
   
   public static void stopBGmusic(Context context) {
	   if (mMainSound != null) {
 	 	   mMainSound.stop();
   	       mMainSound.release();
	       mMainSound = null;
	   }
   }
   
   private static void loadAll_BG(Context context)
   {
	   mSoundPool_BG = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
	   if ( mSoundHashMap_BG == null ) mSoundHashMap_BG = new HashMap<Integer, Integer>();
	   mSoundHashMap_BG.put(R.raw.dark_laugh,mSoundPool_BG.load(context, R.raw.dark_laugh, 1));
	   mSoundHashMap_BG.put(R.raw.mummy,mSoundPool_BG.load(context, R.raw.mummy, 1));
	   mSoundHashMap_BG.put(R.raw.pterodactyl_scream,mSoundPool_BG.load(context, R.raw.pterodactyl_scream, 1));
	   mSoundHashMap_BG.put(R.raw.scary,mSoundPool_BG.load(context, R.raw.scary, 1));
	   mSoundHashMap_BG.put(R.raw.elevator,mSoundPool_BG.load(context, R.raw.elevator, 1));
   }
   
   private static void loadAll_SE(Context context)
   {
	   mSoundPool_SE = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
	   if ( mSoundHashMap_SE == null ) mSoundHashMap_SE = new HashMap<Integer, Integer>();
	   mSoundHashMap_SE.put(R.raw.water_drop,mSoundPool_SE.load(context, R.raw.water_drop, 1));
	   mSoundHashMap_SE.put(R.raw.swords,mSoundPool_SE.load(context, R.raw.swords, 1));
	   mSoundHashMap_SE.put(R.raw.footstep01,mSoundPool_SE.load(context, R.raw.footstep01, 1));
	   mSoundHashMap_SE.put(R.raw.footstep02,mSoundPool_SE.load(context, R.raw.footstep02, 1));
	   mSoundHashMap_SE.put(R.raw.footstep03,mSoundPool_SE.load(context, R.raw.footstep03, 1));
	   mSoundHashMap_SE.put(R.raw.footstep04,mSoundPool_SE.load(context, R.raw.footstep04, 1));	   
   }
   
}
