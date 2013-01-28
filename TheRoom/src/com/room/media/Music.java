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

public class Music {
   private static MediaPlayer mMainSound = null;
   private static SoundPool mSoundPool_BG = null;
   private static SoundPool mSoundPool_SE = null; //sound effects
   private static HashMap<Integer, Integer> mSoundHashMap_BG = null;
   private static HashMap<Integer, Integer> mSoundHashMap_SE = null;
   static Random mRandom;
   
   private static Timer mTimer = null;
   private static TimerTask mTimerTask = null;
   static Iterator it = null;
   
   public static void loadSound(Context context, int resource)
   {
	   if ( mSoundPool_BG == null ) mSoundPool_BG = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
	   if ( mSoundHashMap_BG == null ) mSoundHashMap_BG = new HashMap<Integer, Integer>();
	   mSoundHashMap_BG.put(resource, mSoundPool_BG.load(context, resource, 1));

	   /*
	   mSoundPool_BG.setOnLoadCompleteListener(new OnLoadCompleteListener() {
          @Override
           public void onLoadComplete(SoundPool soundPool, int sampleId,
                   int status) {
               loaded = true;
           }
       });
*/       
   }
   
   // You have to call loadSound before
   // It uses mSoundPool_BG, which name could be misleading. 
   // All the sounds loaded on mSoundPool_BG are just those who are not "hinting" sounds
   public static void playSound(Context context, int resource)
   {
	   if ( Options.getBGMusic(context) && mSoundHashMap_BG != null && mSoundHashMap_BG.containsKey(resource) )
	   {
           mSoundPool_BG.play(mSoundHashMap_BG.get(resource), 0.3f, 0.3f, 1, 0, 1f);
           Log.e("Test", "Played sound");
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
		   mMainSound = MediaPlayer.create(context, resource);
		   mMainSound.setLooping(true);
		   mMainSound.start();
	   }
   }
   
   //The title is bit misleading but it was made that way to be consistent with playBGmusic
   public static void playSEmusic(Context context)
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
   
   //stopSound: stop every sound and release resources
   public static void stopSound(Context context) { 
      if (mMainSound != null) {
    	  mMainSound.stop();
    	  mMainSound.release();
    	  mMainSound = null;
      }
      unloadAll(context);
   }
   
   private static void unloadAll(Context context)
   {
	    if ( mSoundHashMap_BG != null ) {
	    	it = mSoundHashMap_BG.entrySet().iterator();
	    	while (it.hasNext()) {
	    		if ( mSoundPool_BG != null ) {
	    			HashMap.Entry pairs = (HashMap.Entry)it.next();
	    			mSoundPool_BG.unload((Integer) pairs.getValue());
	    		}
	    		it.remove();
	    	}
		    mSoundHashMap_BG = null;
		    mSoundPool_BG = null;
	    }
	    if ( mSoundHashMap_SE != null ) {
	    	it = mSoundHashMap_SE.entrySet().iterator();
	    	while (it.hasNext()) {
	    		if ( mSoundPool_SE != null ) {
	    			HashMap.Entry pairs = (HashMap.Entry)it.next();
	    			mSoundPool_SE.unload((Integer) pairs.getValue());
	    		}
	    		it.remove();
	    	}
		    mSoundHashMap_SE = null;
		    mSoundPool_SE = null;
	    }
		if ( mTimer != null ){
		   mTimer.cancel();
		   mTimer = null;
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
   }
   
}
