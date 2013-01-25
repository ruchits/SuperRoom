package com.room.scene;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.content.res.AssetManager;
import android.util.Log;

import com.room.Global;

public class SLayoutLoader
{
	//SINGLETON!
	public static SLayoutLoader getInstance()
	{
		if(instance == null)
		{
			instance = new SLayoutLoader();
		}
		return instance;
	}

	public SLayout puzzleExample;
	
	public void init()
	{
		puzzleExample = loadLayout("puzzle_example.layout");
	}
	
	private SLayout loadLayout(String assetName)
	{
		SLayout layout = new SLayout();
		
		AssetManager assetManager = Global.mainActivity.getAssets();
		
		try
		{
			InputStream is = assetManager.open(assetName);			
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			while(true)
			{
				String line = in.readLine();
				if(line==null) break;
								
				StringTokenizer st = new StringTokenizer(line);
				
				SLayout.Box box = new SLayout.Box();
				box.name = st.nextToken();
				box.left = Float.parseFloat(st.nextToken());
				box.right = Float.parseFloat(st.nextToken());
				box.top = Float.parseFloat(st.nextToken());
				box.bottom = Float.parseFloat(st.nextToken());
				
				layout.addBox(box);
				
			}
		}
		catch(Exception e)
		{
			Log.d("Layout Loader", assetName);
		}
		
		return layout;
	}
	
	private SLayoutLoader(){}
	private static SLayoutLoader instance;
}
