package com.room.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.content.res.AssetManager;
import android.util.Log;

import com.room.Global;
import com.room.puzzles.*;

public class RPOIManager
{
	//SINGLETON!
	public static RPOIManager getInstance()
	{
		if(instance == null)
		{
			instance = new RPOIManager();
		}		
		return instance;
	}
	
	private class POIActivity
	{
		Class activity;
		boolean activated;
		
		//co-ordinates of the POI
		RMath.V2 o = new RMath.V2();
		RMath.V2 v1 = new RMath.V2();
		RMath.V2 v2 = new RMath.V2();
		String name = "";		
	}
			
	public void init()
	{				
		poiActivityMap = new HashMap<String,POIActivity>();
		loadPOIFile("points_of_interest.poi");
		poiActivityMap.get("POI_phone").activity = PPhone.class;
		poiActivityMap.get("POI_urn").activity = PUrn.class;
		poiActivityMap.get("POI_deadman").activity = PDeadMan.class;
		poiActivityMap.get("POI_statues").activity = PStatues.class;
		poiActivityMap.get("POI_fakedoor").activity = PFakeDoor.class;
		poiActivityMap.get("POI_bathroomdoor").activity = PBathroomDoor.class;
		poiActivityMap.get("POI_sink").activity = PSink.class;
		poiActivityMap.get("POI_deadwoman").activity = PDeadWoman.class;
		poiActivityMap.get("POI_flood").activity = PFlood.class;
	}
	
	public void setDefaultsForCurrentDay()
	{
		disableAllPOIs();
		
		switch(Global.getCurrentDay())
		{
			case 1:
				setPOIState("POI_phone", true);
				setPOIState("POI_urn", true);
				setPOIState("POI_deadman", true);
				setPOIState("POI_statues", true);
				setPOIState("POI_fakedoor", true);
				setPOIState("POI_bathroomdoor", true);
			break;
			case 2:
				setPOIState("POI_phone", true);
				setPOIState("POI_urn", true);
				setPOIState("POI_deadman", true);
				setPOIState("POI_statues", true);
				setPOIState("POI_fakedoor", true);
				setPOIState("POI_bathroomdoor", true);
			break;			
			case 3:
				setPOIState("POI_phone", true);
				setPOIState("POI_urn", true);
				setPOIState("POI_deadman", true);
				setPOIState("POI_statues", true);
				setPOIState("POI_fakedoor", true);
				setPOIState("POI_sink", true);
				//setPOIState("POI_bathroomdoor", true);
				setPOIState("POI_flood", true);
			break;	
			case 4:
				setPOIState("POI_phone", true);
				setPOIState("POI_urn", true);
				setPOIState("POI_deadman", true);
				setPOIState("POI_statues", true);
				setPOIState("POI_fakedoor", true);
				setPOIState("POI_sink", true);
				setPOIState("POI_deadwoman", true);
			break;	
			case 5:
				setPOIState("POI_phone", true);
				setPOIState("POI_urn", true);
				setPOIState("POI_deadman", true);
				setPOIState("POI_statues", true);
				setPOIState("POI_fakedoor", true);
				setPOIState("POI_sink", true);
				setPOIState("POI_deadwoman", true);
			break;				
		}
	}
	
	public String checkPOI(float playerPosX, float playerPosY, float playerDirX, float playerDirY)
	{
		Iterator<POIActivity> e = poiActivityMap.values().iterator();
		while(e.hasNext())
		{
			POIActivity area = e.next();
			
			if(!area.activated)
				continue;
			
			//this algorithm assumes the areas to be orthogonal (or close to orthogonal)
			float u = (playerPosX - area.o.x)/area.v1.x;
			float v = (playerPosY - area.o.y)/area.v2.y;
			
			if(0<=u && u<=1 && 0<=v && v<=1)
			{
				if((u<0.5 && playerDirX>=0) || (u>=0.5 && playerDirX<=0))
				{
					if((v<0.5 && playerDirY>=0) || (v>=0.5 && playerDirY<=0))
					{
						return area.name;
					}
				}
				
				break;
			}
		}
		
		return null;
	}
	
	public Class getActivityForPOI(String inName)
	{
		Class c = poiActivityMap.get(inName).activity;
		return c;
	}
	
	public void setPOIState(String poiName, boolean enable)
	{
		POIActivity poiActivity = poiActivityMap.get(poiName);
		if(poiActivity!=null)
			poiActivity.activated = enable;
	}		
	
	private void disableAllPOIs()
	{
		Iterator<POIActivity> e = poiActivityMap.values().iterator();
		while(e.hasNext())
		{
			e.next().activated = false;
		}
	}
	
	
	private void loadPOIFile(String assetName)
	{
		AssetManager assetManager = Global.mainActivity.getAssets();
		
		try
		{
			InputStream is = assetManager.open("poi/"+assetName);			
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			while(true)
			{
				String line = in.readLine();
				if(line==null) break;
								
				StringTokenizer st = new StringTokenizer(line);
				
				POIActivity poiArea = new POIActivity();
				poiArea.name = st.nextToken();
				poiArea.o.x = Float.parseFloat(st.nextToken());
				poiArea.o.y = Float.parseFloat(st.nextToken());
				poiArea.v1.x = Float.parseFloat(st.nextToken()) - poiArea.o.x;
				poiArea.v1.y = Float.parseFloat(st.nextToken()) - poiArea.o.y;
				poiArea.v2.x = Float.parseFloat(st.nextToken()) - poiArea.o.x;
				poiArea.v2.y = Float.parseFloat(st.nextToken()) - poiArea.o.y;
				poiArea.activated = false;
				poiActivityMap.put(poiArea.name, poiArea);
			}
		}
		catch(Exception e)
		{
			Log.d("loadPOIFile failed!", assetName);
		}
	}
	
	private HashMap<String,POIActivity> poiActivityMap;
	
	private static RPOIManager instance;

}
