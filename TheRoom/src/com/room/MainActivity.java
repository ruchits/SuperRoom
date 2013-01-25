package com.room;

import com.room.media.Music;
import com.room.puzzles.PExample;
import com.room.render.RModelLoader;
import com.room.render.RRenderActivity;
import com.room.scene.SLayoutLoader;

import android.os.*;
import android.app.*;
import android.content.Intent;
import android.util.DisplayMetrics;
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
        
        //TBD - Right now these two loaders are initialized in RRenderer,
        //      We should remove that dependency and init them here as well.
		//RShaderLoader.getInstance().init();
		//RTextureLoader.getInstance().init();

        Music.loadSound(this, R.raw.swords);
		 Music.playBGmusic(this, R.raw.haunting);
        //startGame(); // TODO: DELETE this when the opening screen is needed and uncomment the below
		        
        setContentView(R.layout.activity_main);

        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.options_button);
        aboutButton.setOnClickListener(this);
        View creditsButton = findViewById(R.id.credits_button);
        creditsButton.setOnClickListener(this);
        View helpButton = findViewById(R.id.help_button);
        helpButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
		        
	}

	@Override
	public void onClick(View v) {
		Music.playSound(this, R.raw.swords);
	      switch (v.getId()) {
	      case R.id.continue_button:
	         startGame(); //TODO: Save some state
	         break;
	      case R.id.new_button:
	         startGame();
	         break;
	      case R.id.options_button:

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
	      Intent intent = new Intent(this, RRenderActivity.class);
	      startActivity(intent);
	}
	
	private void showCredits() {
		  Intent intent = new Intent(this, Credits.class);
		  startActivity(intent);
	}
}
