package com.room.render;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

public class RModel
{
	public RModel()
	{
		numGroups = 0;
		numTriangles = new ArrayList<Integer>();
		textureID = new ArrayList<Integer>();
		vertexBuffer = new ArrayList<FloatBuffer>();
		normalBuffer = new ArrayList<FloatBuffer>();
		texBuffer = new ArrayList<FloatBuffer>();
	}
	
    public void draw(float[] projViewMatrix, float[] spotLightPos, float[] spotLightVec)
    {    	
    	//Loops through all model groups and draws them:
    	for(int i=0; i<numGroups; ++i)
    	{    	    		
	        // Add program to OpenGL ES environment
	        GLES20.glUseProgram(RShaderLoader.getInstance().main_progId);
	
	        GLES20.glUniform3fv(RShaderLoader.getInstance().main_uSpotLightPos, 1, spotLightPos, 0);
	        GLES20.glUniform3fv(RShaderLoader.getInstance().main_uSpotLightVec, 1, spotLightVec, 0);
	        
	        vertexBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aPosition);
	      
			normalBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aNormal);		
			
			texBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().main_aTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().main_aTexCoords);
			
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID.get(i));
			GLES20.glUniform1i(RShaderLoader.getInstance().main_uTexId, 0);		
	
	        // Apply the projection and view transformation
	        GLES20.glUniformMatrix4fv(RShaderLoader.getInstance().main_uProjViewMatrix, 1, false, projViewMatrix, 0);        
	        
	        // Draw the triangles        
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numTriangles.get(i)*3);
    	}
    }  
	
	//these are only to be filled by the ModelLoader:
    public int numGroups;
    public ArrayList<Integer> numTriangles;
    public ArrayList<Integer> textureID;
	public ArrayList<FloatBuffer> vertexBuffer;
	public ArrayList<FloatBuffer> normalBuffer;
	public ArrayList<FloatBuffer> texBuffer;
}
