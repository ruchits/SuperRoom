package com.room.render;

import java.io.InputStream;
import java.util.HashMap;

import com.room.Global;
import com.room.R;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.ETC1;
import android.opengl.ETC1Util;
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
			//instance.init();
		}		
		return instance;
	}	
	
	public static class TextureID
	{
		public int rgb;
		public int alpha;
		public TextureID()
		{
			rgb = -1;
			alpha = -1;
		}
	}
	
	public TextureID invalidTextureID;
	
	public void init()
	{
		invalidTextureID = loadCompressedTexture("invalid",false,false,false);
		
		textureID = new HashMap<String,TextureID>();
		
		//walls & floors
		textureID.put("bathroom_floor_tile", loadCompressedTexture("bathroom_floor_tile",false,false,false));
		textureID.put("bathroom_wall_tile", loadCompressedTexture("bathroom_wall_tile",false,false,false));
		textureID.put("floor_hardwood", loadCompressedTexture("floor_hardwood",false,false,false));
		textureID.put("wall_board", loadCompressedTexture("wall_board",false,false,false));
		textureID.put("wall_plaster",loadCompressedTexture("wall_plaster",false,false,false));
		
		//doors
		textureID.put("door_bathroom", loadCompressedTexture("door_bathroom",false,false,false));
		textureID.put("door_fake", loadCompressedTexture("door_fake",false,false,false));		
		
		if(!Global.DEBUG_NO_PROPS)	
		{		
			//props
			textureID.put("prop_urn_body", loadCompressedTexture("prop_urn_body",false,false,false));
			textureID.put("prop_urn_ash", loadCompressedTexture("prop_urn_ash",false,false,false));
			textureID.put("prop_urn_sticks", loadCompressedTexture("prop_urn_sticks",false,false,false));
			
			textureID.put("prop_bed", loadCompressedTexture("prop_bed",false,false,false));			
			textureID.put("prop_drawer", loadCompressedTexture("prop_drawer",false,false,false));
			textureID.put("prop_phone_receiver", loadCompressedTexture("prop_phone_receiver",false,false,false));
			textureID.put("prop_phone_base", loadCompressedTexture("prop_phone_base",false,false,false));
			
			textureID.put("prop_cloth", loadCompressedTexture("prop_cloth",true,true,false));
			textureID.put("prop_deadman", loadCompressedTexture("prop_deadman",false,false,false));			
			textureID.put("prop_deadwoman", loadCompressedTexture("prop_deadwoman",false,false,false));
						
			textureID.put("prop_sink", loadCompressedTexture("prop_sink",false,false,false));
			textureID.put("prop_toilet", loadCompressedTexture("prop_toilet",false,false,false));
			textureID.put("prop_tpaper", loadCompressedTexture("prop_tpaper",false,false,false));
			textureID.put("prop_mirror", loadCompressedTexture("prop_mirror",true,true,false));
									
			textureID.put("prop_frame1", loadCompressedTexture("prop_frame1",false,false,false));
			
			textureID.put("prop_portrait_girl_day1", loadCompressedTexture("prop_portrait_girl_day1",true,true,false));
			textureID.put("prop_portrait_girl_day2", loadCompressedTexture("prop_portrait_girl_day2",true,true,false));
			textureID.put("prop_portrait_girl_day3", loadCompressedTexture("prop_portrait_girl_day3",true,true,false));
			textureID.put("prop_portrait_girl_day4", loadCompressedTexture("prop_portrait_girl_day4",true,true,false));
			textureID.put("prop_portrait_girl_day5", loadCompressedTexture("prop_portrait_girl_day5",true,true,false));
			
			textureID.put("prop_portrait_boy_day1", loadCompressedTexture("prop_portrait_boy_day1",true,true,false));
			textureID.put("prop_portrait_boy_day2", loadCompressedTexture("prop_portrait_boy_day2",true,true,false));
			textureID.put("prop_portrait_boy_day3", loadCompressedTexture("prop_portrait_boy_day3",true,true,false));
			textureID.put("prop_portrait_boy_day4", loadCompressedTexture("prop_portrait_boy_day4",true,true,false));
			textureID.put("prop_portrait_boy_day5", loadCompressedTexture("prop_portrait_boy_day5",true,true,false));
			
			textureID.put("prop_samurai", loadCompressedTexture("prop_samurai",false,false,false));
			textureID.put("prop_feudal", loadCompressedTexture("prop_feudal",true,true,false));
			textureID.put("prop_guan", loadCompressedTexture("prop_guan",true,true,false));
		}

		//ui
		textureID.put("joystick_knob", loadCompressedTexture("joystick_knob",true,true,true));
		textureID.put("joystick_ring", loadCompressedTexture("joystick_ring",true,true,true));
		textureID.put("ui_poi", loadCompressedTexture("ui_poi",true,true,true));
		
		if(!Global.DEBUG_NO_DECALS)
		{		
			//decals
			textureID.put("decal_wall_day2", loadCompressedTexture("decal_wall_day2",false,true,true));
			textureID.put("decal_wall_day3", loadCompressedTexture("decal_wall_day3",false,true,true));
			textureID.put("decal_wall_day4", loadCompressedTexture("decal_wall_day4",false,true,true));
			textureID.put("decal_wall_day5", loadCompressedTexture("decal_wall_day5",false,true,true));
			textureID.put("decal_board_day3", loadCompressedTexture("decal_board_day3",false,true,true));
			textureID.put("decal_board_day4", loadCompressedTexture("decal_board_day4",false,true,true));
			textureID.put("decal_board_day5", loadCompressedTexture("decal_board_day5",false,true,true));		
			textureID.put("decal_small_crack", loadCompressedTexture("decal_small_crack",true,true,true));
			textureID.put("decal_big_crack", loadCompressedTexture("decal_big_crack",true,true,true));
			textureID.put("decal_number", loadCompressedTexture("decal_number",true,true,true));
			textureID.put("decal_puzzle_flood", loadCompressedTexture("decal_puzzle_flood",true,true,true));
		}
		
	}
	
	public TextureID getTextureID(String textureName)
	{
		if(textureName == null)
			return invalidTextureID;
		
		TextureID id = textureID.get(textureName);
		
		if(id == null)
			return invalidTextureID;
		
		return id;
	}
	
	private TextureID loadTexture(int resourceID, boolean clampU, boolean clampV)
	{
		TextureID textureID = new TextureID();
		
		Bitmap img = null;
		int texture[] = new int[1];
		try
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			img = BitmapFactory.decodeResource(Global.mainActivity.getResources(), resourceID, options);
			//Log.e(TAG, "bitmap: resourceID= " + resourceID + "  width | height= " + img.getWidth() + " | " + img.getHeight());
			
			GLES20.glGenTextures(1, texture, 0);	
			
			//select the texture
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
			
			//set its mipmap settings
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
			
			//set its wrapping settings
			if(clampU)
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			else
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
			
			if(clampV)
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			else
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        	//determine texture format automatically
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
			
			//generate mipmaps
			GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_NICEST);
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			
			//deselect the texture
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		}
		catch (Exception e)
		{
			Log.d("TextureLoader", e.toString()+ ":" + e.getMessage());
		}
		img.recycle();
		
		textureID.rgb = texture[0];
		return textureID;		
	}	
	
	private TextureID loadCompressedTexture(String textureName, boolean clampU, boolean clampV, boolean hasAlpha)
	{
		TextureID textureID = new TextureID();		
		
		int texture[] = new int[2];
		try
		{													
			//create and select the texture
			GLES20.glGenTextures(2, texture, 0);	
			
			//activate the texture 0 (RGB)
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
			loadCompressedTextureIntoCurrentChannel("textures/"+textureName+"_mip_",".pkm", clampU, clampV);
			
			//activate the texture 1 (alpha)
			if(hasAlpha)
			{
				GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[1]);
				loadCompressedTextureIntoCurrentChannel("textures/"+textureName+"_mip_","_alpha.pkm", clampU, clampV);
			}					
		}
		catch (Exception e)
		{
			Log.d("TextureLoader", e.toString()+ ":" + e.getMessage());
		}
		
		textureID.rgb = texture[0];
		textureID.alpha = texture[1];
		return textureID;		
	}
	
	private void loadCompressedTextureIntoCurrentChannel(String assetPrefix, String assetSuffix, boolean clampU, boolean clampV)
	{
		//set its mipmap settings
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
			
		//set its wrapping settings
		if(clampU)
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		else
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
		
		if(clampV)
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		else
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
		
		try
		{
			AssetManager assetManager = Global.mainActivity.getAssets();
			
			//read the first mip level
			InputStream is = assetManager.open(assetPrefix+"0"+assetSuffix);	
			ETC1Util.ETC1Texture etcTexture = ETC1Util.createTexture(is);
			is.close();
			
			int width = etcTexture.getWidth();
			int height = etcTexture.getHeight();			
			int numberOfMipmaps = 1;
			
			while((width > 1) || (height > 1))
	        {
				numberOfMipmaps ++;
				if(width > 1) width >>= 1;
				if(height > 1) height >>= 1;
	        }
			
			GLES20.glCompressedTexImage2D(
					GLES20.GL_TEXTURE_2D,
					0,
					ETC1.ETC1_RGB8_OES,
					etcTexture.getWidth(),
					etcTexture.getHeight(),
					0,
					etcTexture.getData().capacity(),
					etcTexture.getData()
					);
			
			
			//load other mip levels
			for(int mipLevel = 1; mipLevel < numberOfMipmaps; mipLevel++)
			{
				is = assetManager.open(assetPrefix+mipLevel+assetSuffix);
				etcTexture = ETC1Util.createTexture(is);
				is.close();
	
				GLES20.glCompressedTexImage2D(
						GLES20.GL_TEXTURE_2D,
						mipLevel,
						ETC1.ETC1_RGB8_OES,
						etcTexture.getWidth(),
						etcTexture.getHeight(),
						0,
						etcTexture.getData().capacity(),
						etcTexture.getData()
						);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private RTextureLoader()
	{
	
	}
	
	private static final String TAG = "com.render.RTextureLoader";
	private HashMap<String,TextureID> textureID;
	private static RTextureLoader instance;
}
