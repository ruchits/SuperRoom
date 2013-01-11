package com.opengltest.render;

import java.nio.*;

import android.opengl.GLES20;

class RCube
{
	
    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texBuffer;
    private ShortBuffer indexBuffer;
    
    static float vertices[] = {
		1,1,1, -1,1,1, -1,-1,1, 1,-1,1,		//front
		1,1,1, 1,-1,1,  1,-1,-1, 1,1,-1,	//right
		1,-1,-1, -1,-1,-1, -1,1,-1, 1,1,-1,	//back
		-1,1,1, -1,1,-1, -1,-1,-1, -1,-1,1,	//left
		1,1,1, 1,1,-1, -1,1,-1, -1,1,1,		//top
		1,-1,1, -1,-1,1, -1,-1,-1, 1,-1,-1,	//bottom                                   
    	};    
    
    static float normals[] = {
		0,0,1, 0,0,1, 0,0,1, 0,0,1,		//front
		1,0,0, 1,0,0,  1,0,0, 1,0,0,	//right
		0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1,	//back
		-1,0,0, -1,0,0, -1,0,0, -1,0,0,	//left
		0,1,0, 0,1,0, 0,1,0, 0,1,0,		//top
		0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0,	//bottom                                   
    	};      
    
    static float texCoords[] = {
		1,0, 0,0, 0,1, 1,1, 
		0,0, 0,1, 1,1, 1,0,
		1,1, 0,1, 0,0, 1,0,
		0,0, 1,0, 1,1, 0,1,
		0,1, 0,0, 1,0, 1,1,
		0,0, 1,0, 1,1, 0,1,
    };
     

    private short indices[] = {
			0,1,2, 0,2,3,
			4,5,6, 4,6,7,
			8,9,10, 8,10,11,
			12,13,14, 12,14,15,
			16,17,18, 16,18,19,
			20,21,22, 20,22,23,
    		};      

    public RCube()
    {
        // initialize vertex byte buffer for shape coordinates
        // (# of coordinate values * 4 bytes per float)    	
    	vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    	vertexBuffer.put(vertices).position(0);

        // initialize byte buffer for the draw list
        // (# of coordinate values * 2 bytes per short)   
    	indexBuffer = ByteBuffer.allocateDirect(indices.length * 4).order(ByteOrder.nativeOrder()).asShortBuffer();
    	indexBuffer.put(indices).position(0);    	
        
        // initialize byte buffer for the texcoords
        // (# of coordinate values * 4 bytes per float)           
		texBuffer = ByteBuffer.allocateDirect(texCoords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer.put(texCoords).position(0);    	
		
        // initialize byte buffer for the normals
        // (# of coordinate values * 4 bytes per float)           
		normalBuffer = ByteBuffer.allocateDirect(normals.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		normalBuffer.put(normals).position(0);  		
          
    }
    
    public void draw(float[] projViewMatrix, float[] lightDir)
    {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(RShaderLoader.getInstance().progId);

        GLES20.glUniform3fv(RShaderLoader.getInstance().uLightDir, 1, lightDir, 0);
        
        vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().aPosition);
      
		normalBuffer.position(0);
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().aNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().aNormal);		
		
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().uTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().uTexCoords);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, RTextureLoader.getInstance().idDirt);
		GLES20.glUniform1i(RShaderLoader.getInstance().uTexLoc, 0);		

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(RShaderLoader.getInstance().uProjViewMatrix, 1, false, projViewMatrix, 0);        
        
        // Draw the indices
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(positionHandle);
    }      
}
