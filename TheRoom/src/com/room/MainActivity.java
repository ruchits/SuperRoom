package com.room;


import android.os.*;
import android.app.*;
import android.content.Intent;
import android.view.*;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        
//        instance = this;

        startGame(); // TODO: DELETE this when the opening screen is needed and uncomment the below
/*        
        setContentView(R.layout.activity_main);

        // new game, continue, credits, exit, options
        
        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.options_button);
        aboutButton.setOnClickListener(this);
        View creditsButton = findViewById(R.id.credits_button);
        creditsButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
*/        
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
	    	 
	    	 break;
	      case R.id.help_button:
	    	  
	    	 break;
	      case R.id.exit_button:
	         finish();
	         break;
	      }
	}
	
	private void startGame() {
	      Intent intent = new Intent(this, Game.class);
	      startActivity(intent);
	}
}
