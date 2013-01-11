package com.opengltest.render;

import com.opengltest.R;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class RTextureLoader
{
	// SINGLETON!!
	public static RTextureLoader getInstance()
	{
		if(instance == null)
		{
			instance = new RTextureLoader();			
		}		
		return instance;
	}	
		
	public int idDirt;
	
	public void init()
	{
		idDirt = loadTexture(R.drawable.dirt);
	}
	
	private int loadTexture(int resourceID)
	{
		Bitmap img = null;
		int texture[] = new int[1];
		try
		{
			img = BitmapFactory.decodeResource(RRenderer.getInstance().glSurfaceView.getResources(), resourceID);
			
			GLES20.glGenTextures(1, texture, 0);			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
			
			//TBD willc - can we turn on bilinear filters here? 
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, img, 0);
			Log.d("ResourceLoader", "Loaded texture "+img.getHeight()+"x"+img.getWidth());
		}
		catch (Exception e)
		{
			Log.d("ResourceLoader", e.toString()+ ":" + e.getMessage());
		}
		img.recycle();
		return texture[0];		
	}	
	
	private RTextureLoader()
	{
	
	}
	
	private static RTextureLoader instance;
}
