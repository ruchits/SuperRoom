package com.room.render;

import java.util.HashMap;

import com.room.R;
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
	
	public int invalidTextureID;
	
	public void init()
	{
		invalidTextureID = loadTexture(R.drawable.invalid);
		
		textureID = new HashMap<String,Integer>();
		textureID.put("bathroom_floor_tile", loadTexture(R.drawable.bathroom_floor_tile));
		textureID.put("bathroom_wall_tile", loadTexture(R.drawable.bathroom_wall_tile));
		textureID.put("floor_hardwood", loadTexture(R.drawable.floor_hardwood));
		textureID.put("wall_board", loadTexture(R.drawable.wall_board));
		textureID.put("wall_plaster", loadTexture(R.drawable.wall_plaster));
		textureID.put("prop_barrel", loadTexture(R.drawable.prop_barrel));
		textureID.put("prop_bed", loadTexture(R.drawable.prop_bed));
		textureID.put("prop_body", loadTexture(R.drawable.prop_body));
		textureID.put("prop_drawer", loadTexture(R.drawable.prop_drawer));
		textureID.put("prop_pharoah", loadTexture(R.drawable.prop_pharoah));
		textureID.put("prop_sink", loadTexture(R.drawable.prop_sink));
		textureID.put("prop_soldier", loadTexture(R.drawable.prop_soldier));
		textureID.put("prop_toilet", loadTexture(R.drawable.prop_toilet));
		textureID.put("prop_tpaper", loadTexture(R.drawable.prop_tpaper));
		textureID.put("prop_warrior", loadTexture(R.drawable.prop_warrior));
	}
	
	public int getTextureID(String textureName)
	{
		Integer id = textureID.get(textureName);
		
		if(id == null)
			return invalidTextureID;
		
		return id;
	}
	
	private int loadTexture(int resourceID)
	{
		Bitmap img = null;
		int texture[] = new int[1];
		try
		{
			img = BitmapFactory.decodeResource(RRenderer.getInstance().glSurfaceView.getResources(), resourceID);
			
			GLES20.glGenTextures(1, texture, 0);	
			
			//select the texture
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
			
			//set its mipmap settings
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
			
			//set its wrapping settings
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        	GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        	//determine texture format automatically
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
			
			//generate mipmaps
			GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_NICEST);
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			
			Log.d("ResourceLoader", "Loaded texture "+img.getHeight()+"x"+img.getWidth());
			
			//deseelct the texture
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
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
	
	
	private HashMap<String,Integer> textureID;
	private static RTextureLoader instance;
}
