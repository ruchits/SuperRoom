package com.room.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.room.Global;

import android.opengl.GLES20;

public class RScreenImage
{
    private FloatBuffer vertexBuffer;
    private FloatBuffer texBuffer;
    private ShortBuffer indexBuffer;
    
    private static float vertices[] = {
		-1,1,0,		//top left
		-1,-1,0,	//bottom left
		1,-1,0,		//bottom right
		1,1,0		//top right
    };	
    
    private static float texCoords[] = {
		0,0,
		0,1,
		1,1,
		1,0, 
    };
    
    private short indices[] = {
    	0, 1, 2, 0, 2, 3		
    };
    
	
	public RScreenImage(RTextureLoader.TextureID textureID)
	{
		this.textureID = textureID;
		
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
	}
	
	public void draw()
	{
		if(!visible) return;
		
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(RShaderLoader.getInstance().screenImage_progId);
        
        vertexBuffer.position(0);
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().screenImage_aPosition, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().screenImage_aPosition);
      
		texBuffer.position(0);
		GLES20.glVertexAttribPointer(RShaderLoader.getInstance().screenImage_aTexCoords, 2, GLES20.GL_FLOAT, false, 0, texBuffer);
		GLES20.glEnableVertexAttribArray(RShaderLoader.getInstance().screenImage_aTexCoords);
		
		//bind rgb texture
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);		
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID.rgb);	
		GLES20.glUniform1i(RShaderLoader.getInstance().screenImage_uTexId, 0);

		//bind alpha texture
		//NOTE - Screen images must have an alpha channel mapped to GLTEXTURE1
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID.alpha);				
		GLES20.glUniform1i(RShaderLoader.getInstance().screenImage_uTexAlphaId, 1);

		GLES20.glUniform2fv(RShaderLoader.getInstance().screenImage_uPosition, 1, position, 0);
		GLES20.glUniform2fv(RShaderLoader.getInstance().screenImage_uSize, 1, size, 0);
		
		//enable blending, and disable depth testing
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);				
		
        // Draw the indices
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

		//revert blending, and depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_BLEND);
	}
	
	public void setSize(float size)
	{
		float screenRatio = (float)Global.SCREEN_WIDTH/Global.SCREEN_HEIGHT;
		this.size[0] = size/2;
		this.size[1] = (size*screenRatio)/2;
		
		//this.size[0] = size;
		//this.size[1] = size;
	}
	
	public void setPosition(float x, float y)
	{
		this.position[0] = x;
		this.position[1] = y;
	}
	
	public void setVisible(boolean v)
	{
		visible = v;
	}
	
	public boolean isVisible()
	{
		return visible;
	}
	
	private boolean visible = true;
	private float[] position = {0,0};
	private float[] size = {1,1};
	private RTextureLoader.TextureID textureID;
}
