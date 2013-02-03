package com.room.render;

import com.room.Global;

public class RTopButtons
{
	//SINGLETON!!
	public static RTopButtons getInstance()
	{
		if(instance == null)
		{
			instance = new RTopButtons();
		}
		return instance;
	}
	
	private RTopButtons()
	{
		poiImage = new RScreenImage(
				RTextureLoader.getInstance().getTextureID("ui_poi"));
		poiImage.setSize(0.3f);
		poiImage.setPosition(0.75f, 0.6f);
		poiImage.setVisible(false);
	}
	
	public void setPOI(String poiName)
	{
		if(poiName != null)
		{
			poiImage.setVisible(true);
		}
		else
		{
			poiImage.setVisible(false);
		}
	}
	
	public void draw()
	{
		poiImage.draw();
	}
	
	private RScreenImage poiImage;
	
	private static RTopButtons instance;
}
