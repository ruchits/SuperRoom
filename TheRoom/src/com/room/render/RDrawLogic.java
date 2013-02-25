package com.room.render;

import com.room.Global;

public class RDrawLogic
{
	//SINGLETON!!
	public static RDrawLogic getInstance()
	{
		if(instance == null)
		{
			instance = new RDrawLogic();
		}
		return instance;
	}
	
	public void draw(float[] viewProjMatrix, float[] spotLightPos, float[] spotLightVec, float spotLightVariation)
	{        
        RModelLoader.getInstance().modelRoom.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
                
        if(!Global.DEBUG_NO_PROPS)
        {
        	RModelLoader.getInstance().modelProps.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
        	
    		switch(Global.getCurrentDay())
    		{
    			case 1:
    				RModelLoader.getInstance().modelPropsStatuesNeutral.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropClothCovered.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelDoorBathroomStage1.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);    				
    			break;

    			case 2:
    				RModelLoader.getInstance().modelPropsStatuesActive.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelDoorBathroomStage1.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadMan.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadManHair.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    			break;

    			case 3:
    				RModelLoader.getInstance().modelPropsStatuesActive.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelDoorBathroomStage2.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadMan.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadManHair.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);    				
    			break;
    			
    			case 4:
    				RModelLoader.getInstance().modelPropsStatuesActive.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelDoorBathroomStage2.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);    				
    				RModelLoader.getInstance().modelPropsDeadWoman.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadWomanHair.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);    				
    				RModelLoader.getInstance().modelPropsDeadMan.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadManHair.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);    				
    			break;
    			
    			case 5:
    				RModelLoader.getInstance().modelPropsStatuesActive.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelDoorBathroomStage2.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadWoman.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadWomanHair.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);    				
    				RModelLoader.getInstance().modelPropsDeadMan.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
    				RModelLoader.getInstance().modelPropsDeadManHair.draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);    				
    			break;    			    		
    		}   
        }
        
        if(Global.DEBUG_SHOW_POI_BOXES)
        	RModelLoader.getInstance().modelPOI.draw(viewProjMatrix, spotLightPos, spotLightVec,spotLightVariation);
        
        RDecalSystem.getInstance().draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
        RTouchController.getInstance().draw();
        RTopButtons.getInstance().draw();

	}
	
	private RDrawLogic(){}
	private static RDrawLogic instance;
}
