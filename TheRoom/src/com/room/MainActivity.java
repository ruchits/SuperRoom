package com.room;

import com.room.item.IItems;
import com.room.media.MSoundManager;
import com.room.render.RModelLoader;
import com.room.render.RPOIManager;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneBitmapProvider;

import android.os.*;
import android.app.*;
import android.content.Context;
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

        setContentView(R.layout.activity_main);
        ActivityManager am= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        Global.VMMemSize = am.getMemoryClass();
        
        // Get the screen's density scale
        Global.deviceDPIScale = this.getResources().getDisplayMetrics().density;
        
        new LoadTask(this, null).execute();
	}

	 class LoadTask extends AsyncTask<Object, Void, String>
	 {
	     Context context;
	     LoadTask(Context context,String userid)
	     {
	               this.context=context;
	     }
	     @Override
	     protected void onPreExecute()
	     {
	         super.onPreExecute();
	     }

	     @Override
	     protected String doInBackground(Object... params)
	     {
	         //determine the width and height here
	 		 DisplayMetrics displaymetrics = new DisplayMetrics();
	 		 getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	 		 Global.SCREEN_WIDTH = displaymetrics.widthPixels;
	 		 Global.SCREEN_HEIGHT = displaymetrics.heightPixels;

	         Global.GL_WIDTH = Global.SCREEN_WIDTH;
	         Global.GL_HEIGHT = Global.SCREEN_HEIGHT;

	         //temp speed hack for BB10:
	 		 if(Global.HALF_RES_RENDER)
	 		 {
	 			Global.GL_WIDTH = (int)(Global.SCREEN_WIDTH * 0.50f);
				Global.GL_HEIGHT = (int)(Global.SCREEN_HEIGHT * 0.50f);
	 		 }

	         //ALL loaders should initialize here
	         SLayoutLoader.getInstance().init();
	         RModelLoader.getInstance().init();
	         RPOIManager.getInstance().init();
	         MSoundManager.getInstance().init();
	         IItems.getInstance().init();
	         SSceneBitmapProvider.getInstance().init();
	         //TBD - Right now these two loaders are initialized in RRenderer,
	         //      We should remove that dependency and init them here as well.
	 		 //RShaderLoader.getInstance().init();
	 		 //RTextureLoader.getInstance().init();
			 return null;
	     }
	     @Override
	     protected void onPostExecute(String result)
	     {
	         super.onPostExecute(result);
	         Intent intent = new Intent(this.context, MainMenu.class);
		     startActivityForResult(intent, 1);
	     }
	}

	@Override
	public void onClick(View v) {
		// do nothing here, since this is just loading activity.
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}
}
