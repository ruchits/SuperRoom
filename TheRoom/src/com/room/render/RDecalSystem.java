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
	
	public void draw(float[] viewProjMatrix, float[] spotLightPos, float[] spotLightVec)
	{
		switch(Global.CURRENT_DAY)
		{
			case 1:
			break;
			
			case 2:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec);
			break;
			
			case 3:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec);
				RModelLoader.getInstance().decalBoard.draw(viewProjMatrix,spotLightPos,spotLightVec);
			break;
			
			case 4:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec);
				RModelLoader.getInstance().decalBoard.draw(viewProjMatrix,spotLightPos,spotLightVec);
				RModelLoader.getInstance().decalCeilingMinor.draw(viewProjMatrix,spotLightPos,spotLightVec);
			break;
			
			case 5:
				RModelLoader.getInstance().decalWall.draw(viewProjMatrix,spotLightPos,spotLightVec);
				RModelLoader.getInstance().decalBoard.draw(viewProjMatrix,spotLightPos,spotLightVec);
				RModelLoader.getInstance().decalCeilingMajor.draw(viewProjMatrix,spotLightPos,spotLightVec);
			break;
		}
	}
	
	private RDecalSystem(){}
	private static RDecalSystem instance;
}
