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
		vertexBuffer = new ArrayList<FloatBuffer>();
		normalBuffer = new ArrayList<FloatBuffer>();
		texBuffer = new ArrayList<FloatBuffer>();
	}
	
    public void draw(float[] projViewMatrix, float[] lightDir)
    {    	
    	//Loops through all model groups and draws them:
    	for(int i=0; i<numGroups; ++i)
    	{    	    		
	        // Add program to OpenGL ES environment
	        GLES20.glUseProgram(RShaderLoader.getInstance().progId);
	
	        GLES20.glUniform3fv(RShaderLoader.getInstance().uLightDir, 1, lightDir, 0);
	        
	        vertexBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().aPosition);
	      
			normalBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().aNormal);		
			
			texBuffer.get(i).position(0);
			GLES20.glVertexAttribPointer(RShaderLoader.getInstance().uTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer.get(i));
			GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().uTexCoords);
			
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, RTextureLoader.getInstance().idDirt);
			GLES20.glUniform1i(RShaderLoader.getInstance().uTexLoc, 0);		
	
	        // Apply the projection and view transformation
	        GLES20.glUniformMatrix4fv(RShaderLoader.getInstance().uProjViewMatrix, 1, false, projViewMatrix, 0);        
	        
	        // Draw the triangles        
	        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, numTriangles.get(i)*3);
    	}
    }  
	
	//these are only to be filled by the ModelLoader:
    public int numGroups;
    public ArrayList<Integer> numTriangles;
	public ArrayList<FloatBuffer> vertexBuffer;
	public ArrayList<FloatBuffer> normalBuffer;
	public ArrayList<FloatBuffer> texBuffer;
}
