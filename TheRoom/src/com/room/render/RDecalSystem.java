package com.room.render;

import com.room.Global;

public class RDecalSystem
{
	//SINGLETON!!
	public static RDecalSystem getInstance()
	{
		if(instance == null)
		{
			instance = new RDecalSystem();
		}
		return instance;
	}
	
	public void draw(float[] viewProjMatrix, float[] spotLightPos, float[] spotLightVec, float spotLightVariation)
	{
		if(Global.DEBUG_NO_DECALS)
			return;
		
		switch(Global.getCurrentDay())
		{
			case 1:
				RModelLoader.getInstance().decalNumber.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
			break;
			
			case 2:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
			break;
			
			case 3:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
				RModelLoader.getInstance().decalBoard.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
				RModelLoader.getInstance().decalPuzzleFlood.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
			break;
			
			case 4:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
				RModelLoader.getInstance().decalBoard.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
				RModelLoader.getInstance().decalCeilingMinor.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
			break;
			
			case 5:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
				RModelLoader.getInstance().decalBoard.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
				RModelLoader.getInstance().decalCeilingMajor.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
			break;
		}
	}
	
	private RDecalSystem(){}
	private static RDecalSystem instance;
}
