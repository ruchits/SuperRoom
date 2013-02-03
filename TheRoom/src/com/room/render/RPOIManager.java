package com.room.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.res.AssetManager;
import android.util.Log;

import com.room.Global;
import com.room.scene.SLayout;

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
	
	private static class POIArea
	{
		RMath.V2 o = new RMath.V2();
		RMath.V2 v1 = new RMath.V2();
		RMath.V2 v2 = new RMath.V2();
		String name = "";
	}
			
	public void init()
	{
		areas = new ArrayList<POIArea>(); 
		loadPOIFile("points_of_interest.poi");
	}
	
	public String checkPOI(float playerPosX, float playerPosY, float playerDirX, float playerDirY)
	{
		for(POIArea area:areas)
		{
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
	
	private static ArrayList<POIArea> areas;
	
	private void loadPOIFile(String assetName)
	{
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
				
				POIArea poiArea = new POIArea();
				poiArea.name = st.nextToken();
				poiArea.o.x = Float.parseFloat(st.nextToken());
				poiArea.o.y = Float.parseFloat(st.nextToken());
				poiArea.v1.x = Float.parseFloat(st.nextToken()) - poiArea.o.x;
				poiArea.v1.y = Float.parseFloat(st.nextToken()) - poiArea.o.y;
				poiArea.v2.x = Float.parseFloat(st.nextToken()) - poiArea.o.x;
				poiArea.v2.y = Float.parseFloat(st.nextToken()) - poiArea.o.y;
				
				areas.add(poiArea);
			}
		}
		catch(Exception e)
		{
			Log.d("loadPOIFile failed!", assetName);
		}
	}
	
	
	private static RPOIManager instance;

}
