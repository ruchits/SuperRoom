package com.room.media;

import com.room.Global;
import com.room.Options;
import com.room.R;
import com.room.render.RDecalSystem;

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

public class MSoundManager
{
	//SINGLETON!!
	public static MSoundManager getInstance()
	{
		if(instance == null)
		{
			instance = new MSoundManager();
		}
		return instance;
	}
	
   public void init()
   {
	   mSoundPool_BG = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
	   if ( mSoundHashMap_BG == null ) mSoundHashMap_BG = new HashMap<Integer, Integer>();
	   mSoundHashMap_BG.put(R.raw.dark_laugh,mSoundPool_BG.load(Global.mainActivity, R.raw.dark_laugh, 1));
	   mSoundHashMap_BG.put(R.raw.mummy,mSoundPool_BG.load(Global.mainActivity, R.raw.mummy, 1));
	   mSoundHashMap_BG.put(R.raw.pterodactyl_scream,mSoundPool_BG.load(Global.mainActivity, R.raw.pterodactyl_scream, 1));
	   mSoundHashMap_BG.put(R.raw.scary,mSoundPool_BG.load(Global.mainActivity, R.raw.scary, 1));
	   mSoundHashMap_BG.put(R.raw.elevator,mSoundPool_BG.load(Global.mainActivity, R.raw.elevator, 1));

	   mSoundPool_SE = new SoundPool( 20, AudioManager.STREAM_MUSIC, 0);
	   if ( mSoundHashMap_SE == null ) mSoundHashMap_SE = new HashMap<Integer, Integer>();
	   mSoundHashMap_SE.put(R.raw.water_drop,mSoundPool_SE.load(Global.mainActivity, R.raw.water_drop, 1));
	   mSoundHashMap_SE.put(R.raw.swords,mSoundPool_SE.load(Global.mainActivity, R.raw.swords, 1));
	   mSoundHashMap_SE.put(R.raw.footstep01,mSoundPool_SE.load(Global.mainActivity, R.raw.footstep01, 1));
	   mSoundHashMap_SE.put(R.raw.footstep02,mSoundPool_SE.load(Global.mainActivity, R.raw.footstep02, 1));
	   mSoundHashMap_SE.put(R.raw.footstep03,mSoundPool_SE.load(Global.mainActivity, R.raw.footstep03, 1));
	   mSoundHashMap_SE.put(R.raw.footstep04,mSoundPool_SE.load(Global.mainActivity, R.raw.footstep04, 1));	   
   }	
	
   public void playSound(int resource)
   {
	   if ( Options.isSoundEffectsEnabled() && mSoundHashMap_SE != null && mSoundHashMap_SE.containsKey(resource) )
	   {
           mSoundPool_SE.play(mSoundHashMap_SE.get(resource), 0.3f, 0.3f, 1, 0, 1f);
           Log.e("MMusic", "Played sound");
       }
   }
   
   public void playTimedSound()
   {
	   if (Options.isMusicEnabled()) {
		   mTimer = new Timer();
		   it = (Iterator) mSoundHashMap_BG.entrySet().iterator();
		   mTimerTask = new TimerTask() {
			   public void run() {
				   if (!it.hasNext()){
					   it = (Iterator) mSoundHashMap_BG.entrySet().iterator();
				   }
				   playSound((Integer)((HashMap.Entry)it.next()).getKey());
			   }
		   };
		   mTimer.schedule(mTimerTask, 15000, 15000);
	  }
   }

   public void playBGmusic(int resource)
   {
	   if (Options.isMusicEnabled()){
		   Log.e("MMusic", "Play background music");
		   if ( mMainSound != null ) mMainSound.release();
		   mMainSound = MediaPlayer.create(Global.mainActivity, resource);
		   mMainSound.setLooping(true);
		   mMainSound.start();
	   }
   }
   
   public boolean isPlaying()
   {
	   if ( mMainSound != null )
	   {
	       return mMainSound.isPlaying();
	   }
	   return false;
   }
   
   public void updateSE(int x, int y)
   {
	   if ( Options.isSoundEffectsEnabled() )
	   {
		   //TODO
	   }
   }
   
   public void stopBGmusic()
   {
	   if (mMainSound != null) {
 	 	   mMainSound.stop();
   	       mMainSound.release();
	       mMainSound = null;
	   }
   }
   
   private MediaPlayer mMainSound = null;
   private SoundPool mSoundPool_BG = null;
   private SoundPool mSoundPool_SE = null; //sound effects
   private HashMap<Integer, Integer> mSoundHashMap_BG = null;
   private HashMap<Integer, Integer> mSoundHashMap_SE = null;
   private Random mRandom;
   
   private Timer mTimer = null;
   private TimerTask mTimerTask = null;
   private Iterator it = null;
   
   private static MSoundManager instance;   
}
