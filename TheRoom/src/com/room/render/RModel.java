package com.room.render;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.room.Global;

import android.opengl.GLES20;

public class RModel
{
	public RModel()
	{
		numGroups = 0;
		numTriangles = new ArrayList<Integer>();
		textureID = new ArrayList<String>();
		vertexBuffer = new ArrayList<FloatBuffer>();
		normalBuffer = new ArrayList<FloatBuffer>();
		texBuffer = new ArrayList<FloatBuffer>();
	}
	
    public void draw(float[] projViewMatrix, float[] spotLightPos, float[] spotLightVec, float spotLightVariation)
    {    	
    	//Loops through all model groups and draws them:
    	for(int i=0; i<numGroups; ++i)
    	{    	    		
			FloatBuffer vertexBuff = vertexBuffer.get(i);
			FloatBuffer normalBuff = normalBuffer.get(i);
			FloatBuffer texBuff = texBuffer.get(i);
			
			vertexBuff.position(0);
			normalBuff.position(0);
			texBuff.position(0);
			
			StringTokenizer st = new StringTokenizer(textureID.get(i),"X");			
			String textureName = st.nextToken();
			
			while(st.hasMoreTokens())
			{
				String token = st.nextToken();
				
				if(token.equals("day"))
				{
					textureName+="_day"+Global.getCurrentDay();
				}
			}
			
			RTextureLoader.TextureID textID = RTextureLoader.getInstance().getTextureID(textureName);
				
			if(alphaEnabled)
			{
				if(unlitEnabled)
				{
					setupMainUnlitWithAlphaShader(projViewMatrix,
							vertexBuff,normalBuff,texBuff, textID.rgb, textID.alpha);
				}
				else
				{
					setupMainWithAlphaShader(projViewMatrix,spotLightPos,spotLightVec,spotLightVariation,
							vertexBuff,normalBuff,texBuff, textID.rgb, textID.alpha);					
				}

				GLES20.glEnable(GLES20.GL_BLEND);
			}
			else
			{
				setupMainShader(projViewMatrix,spotLightPos,spotLightVec,spotLightVariation,
						vertexBuff,normalBuff,texBuff, textID.rgb);
			}
			
	        if(!cullEnabled)
	        	GLES20.glDisable(GLES20.GL_CULL_FACE);
	        
	        // Draw the triangles        
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numTriangles.get(i)*3);
	        
	        if(alphaEnabled)
	    		GLES20.glDisable(GLES20.GL_BLEND);
	        
	        if(!cullEnabled)
	        	GLES20.glEnable(GLES20.GL_CULL_FACE);	        
    	}
    }  
    
    
    private void setupMainShader(float[] projViewMatrix, float[] spotLightPos, float[] spotLightVec, float spotLightVariation,
    		FloatBuffer vertexBuff, FloatBuffer normalBuff, FloatBuffer texBuff, int rgbTextureID)
    {       
        GLES20.glUseProgram(RShaderLoader.getInstance().main_progId);

        GLES20.glUniform3fv(RShaderLoader.getInstance().main_uSpotLightPos, 1, spotLightPos, 0);
        GLES20.glUniform3fv(RShaderLoader.getInstance().main_uSpotLightVec, 1, spotLightVec, 0);
        GLES20.glUniform1f(RShaderLoader.getInstance().main_uSpotLightVariation, spotLightVariation);
        
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aPosition);
      
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aNormal);		
		
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aTexCoords);	
		
		//activate texture 0
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, rgbTextureID);			
		GLES20.glUniform1i(RShaderLoader.getInstance().main_uTexId, 0);		
		
		// Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(RShaderLoader.getInstance().main_uProjViewMatrix, 1, false, projViewMatrix, 0);   		
    }
    
    private void setupMainWithAlphaShader(float[] projViewMatrix, float[] spotLightPos, float[] spotLightVec, float spotLightVariation,
    		FloatBuffer vertexBuff, FloatBuffer normalBuff, FloatBuffer texBuff, int rgbTextureID, int alphaTextureID)
    {       
        GLES20.glUseProgram(RShaderLoader.getInstance().mainWithAlpha_progId);

        GLES20.glUniform3fv(RShaderLoader.getInstance().mainWithAlpha_uSpotLightPos, 1, spotLightPos, 0);
        GLES20.glUniform3fv(RShaderLoader.getInstance().mainWithAlpha_uSpotLightVec, 1, spotLightVec, 0);
        GLES20.glUniform1f(RShaderLoader.getInstance().mainWithAlpha_uSpotLightVariation, spotLightVariation);
        
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().mainWithAlpha_aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().mainWithAlpha_aPosition);
      
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().mainWithAlpha_aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().mainWithAlpha_aNormal);		
		
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().mainWithAlpha_aTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().mainWithAlpha_aTexCoords);	
		
		//activate texture 0
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, rgbTextureID);			
		GLES20.glUniform1i(RShaderLoader.getInstance().mainWithAlpha_uTexId, 0);	
		
		//activate texture 1
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);				
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, alphaTextureID);
		GLES20.glUniform1i(RShaderLoader.getInstance().mainWithAlpha_uTexAlphaId, 1);	
		
		// Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(RShaderLoader.getInstance().mainWithAlpha_uProjViewMatrix, 1, false, projViewMatrix, 0); 
    }  
    
    private void setupMainUnlitWithAlphaShader(float[] projViewMatrix,
    		FloatBuffer vertexBuff, FloatBuffer normalBuff, FloatBuffer texBuff, int rgbTextureID, int alphaTextureID)
    {       
        GLES20.glUseProgram(RShaderLoader.getInstance().mainUnlitWithAlpha_progId);
        
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().mainUnlitWithAlpha_aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().mainUnlitWithAlpha_aPosition);
      
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().mainUnlitWithAlpha_aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().mainUnlitWithAlpha_aNormal);		
		
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().mainUnlitWithAlpha_aTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuff);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().mainUnlitWithAlpha_aTexCoords);	
		
		//activate texture 0
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, rgbTextureID);			
		GLES20.glUniform1i(RShaderLoader.getInstance().mainUnlitWithAlpha_uTexId, 0);	
		
		//activate texture 1
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);				
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, alphaTextureID);
		GLES20.glUniform1i(RShaderLoader.getInstance().mainUnlitWithAlpha_uTexAlphaId, 1);	
		
		// Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(RShaderLoader.getInstance().mainUnlitWithAlpha_uProjViewMatrix, 1, false, projViewMatrix, 0); 
    }      
	
    public void enableAlpha(boolean b)
    {
    	alphaEnabled = b;
    }
    
    public void enableCull(boolean b)
    {
    	cullEnabled = b;
    }    
    
    
    public void enableUnlit(boolean b)
    {
    	unlitEnabled = b;
    }    
        
    
    private boolean alphaEnabled = false;
    private boolean cullEnabled = true;
    private boolean unlitEnabled = false;
    
	//these are only to be filled by the ModelLoader:
    public int numGroups;
    public ArrayList<Integer> numTriangles;
    public ArrayList<String> textureID;
	public ArrayList<FloatBuffer> vertexBuffer;
	public ArrayList<FloatBuffer> normalBuffer;
	public ArrayList<FloatBuffer> texBuffer;
}
