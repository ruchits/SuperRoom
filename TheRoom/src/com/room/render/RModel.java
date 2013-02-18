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
	        // Add program to OpenGL ES environment
	        GLES20.glUseProgram(RShaderLoader.getInstance().main_progId);
	
	        GLES20.glUniform3fv(RShaderLoader.getInstance().main_uSpotLightPos, 1, spotLightPos, 0);
	        GLES20.glUniform3fv(RShaderLoader.getInstance().main_uSpotLightVec, 1, spotLightVec, 0);
	        GLES20.glUniform1f(RShaderLoader.getInstance().main_uSpotLightVariation, spotLightVariation);
	        
	        vertexBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aPosition);
	      
			normalBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aNormal);		
			
			texBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aTexCoords);
						

			
			StringTokenizer st = new StringTokenizer(textureID.get(i),"X");			
			String textureName = st.nextToken();
			
			while(st.hasMoreTokens())
			{
				String token = st.nextToken();
				
				if(token.equals("day"))
				{
					textureName+="_day"+Global.CURRENT_DAY;
				}
			}
			
			RTextureLoader.TextureID textID = RTextureLoader.getInstance().getTextureID(textureName);
			
			//activate texture 0
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);		
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textID.rgb);			
			GLES20.glUniform1i(RShaderLoader.getInstance().main_uTexId, 0);
							
			if(alphaEnabled)
			{
				GLES20.glUniform1i(RShaderLoader.getInstance().main_uUseAlpha, 1);
				
				//activate texture 1
				GLES20.glActiveTexture(GLES20.GL_TEXTURE1);				
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textID.alpha);
				GLES20.glUniform1i(RShaderLoader.getInstance().main_uTexAlphaId, 1);				

			}
			else
			{
				GLES20.glUniform1i(RShaderLoader.getInstance().main_uUseAlpha, 0);
			}
			
	        // Apply the projection and view transformation
	        GLES20.glUniformMatrix4fv(RShaderLoader.getInstance().main_uProjViewMatrix, 1, false, projViewMatrix, 0);        
	        
	        if(alphaEnabled)
	    		GLES20.glEnable(GLES20.GL_BLEND);
	        
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
	
    public void enableAlpha(boolean b)
    {
    	alphaEnabled = b;
    }
    
    public void enableCull(boolean b)
    {
    	cullEnabled = b;
    }    
    
    private boolean alphaEnabled = false;
    private boolean cullEnabled = true;
    
	//these are only to be filled by the ModelLoader:
    public int numGroups;
    public ArrayList<Integer> numTriangles;
    public ArrayList<String> textureID;
	public ArrayList<FloatBuffer> vertexBuffer;
	public ArrayList<FloatBuffer> normalBuffer;
	public ArrayList<FloatBuffer> texBuffer;
}
