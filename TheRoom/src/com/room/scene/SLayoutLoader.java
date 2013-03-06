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
	
	public SLayout mainMenu;
	public SLayout daySelection;
	public SLayout itemMenu;
	
	public SLayout puzzleFlood;
	public SLayout puzzleExample;
	public SLayout puzzleBathroomDoor;
	public SLayout puzzleDeadMan;
	public SLayout puzzleDeadWoman;
	public SLayout puzzleFakeDoor;
	public SLayout puzzlePhone;
	public SLayout puzzleSink;
	public SLayout puzzleStatues;
	public SLayout puzzleUrn;
	
	public void init()
	{
		mainMenu = loadLayout("main_menu.layout");
		daySelection = loadLayout("day_selection.layout");
		itemMenu = loadLayout("items_menu.layout");
		
		puzzleFlood = loadLayout("puzzle_flood.layout");
		puzzleExample = loadLayout("puzzle_example.layout");
		puzzleBathroomDoor = loadLayout("puzzle_door_bathroom.layout");
		puzzleDeadMan = loadLayout("puzzle_deadman.layout");
		puzzleDeadWoman = loadLayout("puzzle_deadwoman.layout");
		puzzleFakeDoor = loadLayout("puzzle_door_fake.layout");
		puzzlePhone = loadLayout("puzzle_phone.layout");
		puzzleSink = loadLayout("puzzle_sink.layout");
		puzzleStatues = loadLayout("puzzle_statues.layout");
		puzzleUrn = loadLayout("puzzle_urn.layout");
	}
	
	private SLayout loadLayout(String assetName)
	{
		SLayout layout = new SLayout();
		
		AssetManager assetManager = Global.mainActivity.getAssets();
		
		try
		{
			InputStream is = assetManager.open("layouts/"+assetName);			
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
				
				if(st.hasMoreTokens())
					box.desc = st.nextToken("");
				
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
