package com.room.media;

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

public class Music {
   private static MediaPlayer mMainSound = null;
   private static SoundPool mSoundPool;
   private static HashMap<Integer, Integer> mSoundHashMap = null;
   static Random mRandom;
   
   private static Timer mTimer = null;
   private static TimerTask mTimerTask = null;
   static Iterator it = null;
   
   public static void playSound(Context context, int resource)
   {
	   if ( mSoundHashMap != null && mSoundHashMap.containsKey(resource) )
	   {
           mSoundPool.play(mSoundHashMap.get(resource), 0.3f, 0.3f, 1, 0, 1f);
           Log.e("Test", "Played sound");
       }
   }
   
   public static void unloadAll(Context context)
   {
	    it = mSoundHashMap.entrySet().iterator();
	    while (it.hasNext()) {
	    	if ( mSoundPool != null ) {
	    		HashMap.Entry pairs = (HashMap.Entry)it.next();
				   mSoundPool.unload((Integer) pairs.getValue());
			   }
	        it.remove();
	    }
		if ( mTimer != null ){
		   mTimer.cancel();
		}
	    mSoundHashMap = null;
	    mTimer = null;
   }

   
   public static void loadSound(Context context, int resource)
   {
	   if ( mSoundPool == null ) mSoundPool = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
	   if ( mSoundHashMap == null ) mSoundHashMap = new HashMap<Integer, Integer>();
	   mSoundHashMap.put(resource, mSoundPool.load(context, resource, 1));

	   /*
	   mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
          @Override
           public void onLoadComplete(SoundPool soundPool, int sampleId,
                   int status) {
               loaded = true;
           }
       });
*/       
   }
   
   public static void loadAll(Context context)
   {
	   mSoundPool = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
	   if ( mSoundHashMap == null ) mSoundHashMap = new HashMap<Integer, Integer>();
	   mSoundHashMap.put(R.raw.dark_laugh,mSoundPool.load(context, R.raw.dark_laugh, 1));
	   mSoundHashMap.put(R.raw.mummy,mSoundPool.load(context, R.raw.mummy, 1));
	   mSoundHashMap.put(R.raw.pterodactyl_scream,mSoundPool.load(context, R.raw.pterodactyl_scream, 1));
	   mSoundHashMap.put(R.raw.scary,mSoundPool.load(context, R.raw.scary, 1));
	   mSoundHashMap.put(R.raw.elevator,mSoundPool.load(context, R.raw.elevator, 1));
   }
   
   public static void playTimedSound(final Context context)
   {
	   mTimer = new Timer();
	   it = (Iterator) mSoundHashMap.entrySet().iterator();
	    
	   mTimerTask = new TimerTask() {
		   public void run() {
			   if (!it.hasNext()){
				   it = (Iterator) mSoundHashMap.entrySet().iterator();
			   }
			   playSound(context, (Integer)((HashMap.Entry)it.next()).getKey());
			   //playSound(context,R.raw.mockingbird);
		   }
	   };
	   mTimer.schedule(mTimerTask, 15000, 15000);
   }

   
   public static void playBGmusic(Context context, int resource)
   {
	// TODO: Get user's option for music
	   mMainSound = MediaPlayer.create(context, resource);
	   mMainSound.setLooping(true);
	   mMainSound.start();
   }
   
   //stopSound: stop every sound and release resources
   public static void stopSound(Context context) { 
      if (mMainSound != null) {
    	  mMainSound.stop();
    	  mMainSound.release();
    	  mMainSound = null;
      }
      unloadAll(context);
   }
}
