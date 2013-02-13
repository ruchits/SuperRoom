package com.room;

import com.room.media.MVideoActivity;
import com.room.utils.*;
import com.room.media.MSoundManager;
import com.room.puzzles.PExample;
import com.room.render.RModelLoader;
import com.room.render.RPOIManager;
import com.room.render.RRenderActivity;
import com.room.scene.SLayoutLoader;

import android.os.*;
import android.app.*;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);       
        //store reference to first activity for any asset loaders
        Global.mainActivity = this;
        
        //determine the width and height here
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		Global.SCREEN_WIDTH = displaymetrics.widthPixels;
		Global.SCREEN_HEIGHT = displaymetrics.heightPixels;				
		
        //ALL loaders should initialize here
        SLayoutLoader.getInstance().init();		
        RModelLoader.getInstance().init();
        RPOIManager.getInstance().init();
        MSoundManager.getInstance().init();
        
        //TBD - Right now these two loaders are initialized in RRenderer,
        //      We should remove that dependency and init them here as well.
		//RShaderLoader.getInstance().init();
		//RTextureLoader.getInstance().init();        
		        
        setContentView(R.layout.activity_main);

        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View optionsButton = findViewById(R.id.options_button);
        optionsButton.setOnClickListener(this);
        View creditsButton = findViewById(R.id.credits_button);
        creditsButton.setOnClickListener(this);
        View helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
        
        if(Global.DEBUG_SKIP_MENU)
        	startGame();
		        
	}

	@Override
	public void onClick(View v) {
		  MSoundManager.getInstance().playSoundEffect(R.raw.swords);
	      switch (v.getId()) {
	      case R.id.continue_button:
	         startGame(); //TODO: Save some state
	         break;
	      case R.id.new_button:
	         startGameWithIntro();
	         break;
	      case R.id.options_button:
	    	 showOptions(); 
	         break;
	      case R.id.credits_button:
	    	 showCredits();
	    	 break;
	      case R.id.help_button:
	    	 break;
	      case R.id.exit_button:
	    	 finish();
	         break;
	      }
	}
	
	private void startGame()
	{
	      Intent intent = new Intent(this, MVideoActivity.class);
	      startActivity(intent);
		  UTransitionUtil.overridePendingTransition(this, R.anim.fade_in, R.anim.fade_out);

	}
	
	private void showCredits() {
		  Intent intent = new Intent(this, Credits.class);
		  startActivity(intent);
	}
	
	private void showOptions() {
		  Intent intent = new Intent(this, Options.class);
		  startActivity(intent);
	}
	
	private void startGameWithIntro()
	{
		MVideoActivity.videoToPlay = MVideoActivity.DAY1_VIDEO;
		Intent intent = new Intent(this, MVideoActivity.class);
		startActivityForResult(intent, 1);
		//UTransitionUtil.overridePendingTransition(this, R.anim.fade_in, R.anim.fade_out);
	}

	
	@Override
	protected void onResume()
	{
		super.onResume();
		MSoundManager.getInstance().playMusic(R.raw.music_menu);
	}
	
	@Override
	protected void onDestroy()
	{		
		super.onDestroy();
		MSoundManager.getInstance().stopAllSounds();
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		UTransitionUtil.overridePendingTransition(this,R.anim.fade_in, R.anim.fade_out);
	}
}
