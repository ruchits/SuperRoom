package com.room.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.room.Global;

import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;

public class RShaderLoader
{
	//SINGLETON!
	public static RShaderLoader getInstance()
	{
		if(instance == null)
		{
			instance = new RShaderLoader();
			//instance.init();
		}		
		return instance;
	}
	
	//shader program ids
	public int main_progId;
	public int mainWithAlpha_progId;
	public int screenImage_progId;
	
	//main variable maps
	//===================
	
	//mapped attributes
	public int main_aPosition;
	public int main_aNormal;
	public int main_aTexCoords;
	
	//mapped uniforms
	public int main_uProjViewMatrix;
	public int main_uTexId;
	public int main_uSpotLightVec;
	public int main_uSpotLightPos;
	public int main_uSpotLightVariation;
	
	//mainWithAlpha variable maps
	//===================
	
	//mapped attributes
	public int mainWithAlpha_aPosition;
	public int mainWithAlpha_aNormal;
	public int mainWithAlpha_aTexCoords;
	
	//mapped uniforms
	public int mainWithAlpha_uProjViewMatrix;
	public int mainWithAlpha_uTexId;
	public int mainWithAlpha_uTexAlphaId;
	public int mainWithAlpha_uSpotLightVec;
	public int mainWithAlpha_uSpotLightPos;
	public int mainWithAlpha_uSpotLightVariation;
		
	
	//screenImage variable maps
	//==========================
	
	//mapped attributes
	public int screenImage_aPosition;
	public int screenImage_aTexCoords;
	
	//mapped uniforms
	public int screenImage_uTexId;
	public int screenImage_uTexAlphaId;
	public int screenImage_uPosition;
	public int screenImage_uSize;
	
	
	public void init()
	{
		main_progId = loadProgram(assetToString("main.vert"), assetToString("main.frag"));
		mainWithAlpha_progId = loadProgram(assetToString("main.vert"), assetToString("mainWithAlpha.frag"));
		screenImage_progId = loadProgram(assetToString("screenImage.vert"), assetToString("screenImage.frag"));
		
		//bind main attributes
		main_aPosition = GLES20.glGetAttribLocation(main_progId, "aPosition");
		main_aTexCoords = GLES20.glGetAttribLocation(main_progId, "aTexCoords");
		main_aNormal = GLES20.glGetAttribLocation(main_progId, "aNormal");
		
		//bind main uniforms
		main_uProjViewMatrix = GLES20.glGetUniformLocation(main_progId, "uProjViewMatrix");
		main_uTexId = GLES20.glGetUniformLocation(main_progId, "uTexId");
		main_uSpotLightVec = GLES20.glGetUniformLocation(main_progId, "uSpotLightVec");
		main_uSpotLightPos = GLES20.glGetUniformLocation(main_progId, "uSpotLightPos");
		main_uSpotLightVariation = GLES20.glGetUniformLocation(main_progId, "uSpotLightVariation");
		
		//bind mainWithAlpha attributes
		mainWithAlpha_aPosition = GLES20.glGetAttribLocation(mainWithAlpha_progId, "aPosition");
		mainWithAlpha_aTexCoords = GLES20.glGetAttribLocation(mainWithAlpha_progId, "aTexCoords");
		mainWithAlpha_aNormal = GLES20.glGetAttribLocation(mainWithAlpha_progId, "aNormal");
		
		//bind mainWithAlpha uniforms
		mainWithAlpha_uProjViewMatrix = GLES20.glGetUniformLocation(mainWithAlpha_progId, "uProjViewMatrix");
		mainWithAlpha_uTexId = GLES20.glGetUniformLocation(mainWithAlpha_progId, "uTexId");
		mainWithAlpha_uTexAlphaId = GLES20.glGetUniformLocation(mainWithAlpha_progId, "uTexAlphaId");
		mainWithAlpha_uSpotLightVec = GLES20.glGetUniformLocation(mainWithAlpha_progId, "uSpotLightVec");
		mainWithAlpha_uSpotLightPos = GLES20.glGetUniformLocation(mainWithAlpha_progId, "uSpotLightPos");
		mainWithAlpha_uSpotLightVariation = GLES20.glGetUniformLocation(mainWithAlpha_progId, "uSpotLightVariation");		
		
		//bind screenImage attributes
		screenImage_aPosition = GLES20.glGetAttribLocation(screenImage_progId, "aPosition");
		screenImage_aTexCoords = GLES20.glGetAttribLocation(screenImage_progId, "aTexCoords");
		
		//bind screenImage uniforms
		screenImage_uTexId = GLES20.glGetUniformLocation(screenImage_progId, "uTexId");
		screenImage_uTexAlphaId = GLES20.glGetUniformLocation(screenImage_progId, "uTexAlphaId");
		screenImage_uPosition = GLES20.glGetUniformLocation(screenImage_progId, "uPosition");
		screenImage_uSize = GLES20.glGetUniformLocation(screenImage_progId, "uSize");
	}
	
	private int loadShader(int type, String shaderCode)
	{
	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);
	    
	    int[] compileStatus = new int[1];
	    
	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);
	    
	    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
	    
		if (compileStatus[0] == 0)
		{			
			Log.d("Shader Error", "Compilation failed!\n"+GLES20.glGetShaderInfoLog(shader));
			return 0;
		}	    
	    
	    return shader;
	}
	
	private int loadProgram(String vertexSource, String fragmentSource)
	{
		int vertexShader;
		int fragmentShader; 
		int program;
		
		int[] linkStatus = new int[1];
		
		vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
		if (vertexShader == 0)
		{
			Log.d("Load Program", "Vertex Shader Failed");
			return 0;
		}
		
		fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if(fragmentShader == 0)
		{
			Log.d("Load Program", "Fragment Shader Failed");
			return 0;
		}
		
		program = GLES20.glCreateProgram();
		
		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);
		
		GLES20.glLinkProgram(program);
		
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
		
		if (linkStatus[0] <= 0)
		{
			Log.d("Shader Error", "Linking failed!");
			return 0;
		}
		
		GLES20.glDeleteShader(vertexShader);
		GLES20.glDeleteShader(fragmentShader);
		return program;
	}	
	
	private String assetToString(String assetName)
	{
		String file="";

		AssetManager assetManager = Global.mainActivity.getAssets();
		
		try
		{
			InputStream is = assetManager.open("shaders/"+assetName);			
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			while(true)
			{
				String line = in.readLine();
				if(line==null) break;
				
				file+=line+"\n";
			}
			
			in.close();
			is.close();			
		}
		catch(Exception e)
		{
			Log.d("Model Loader", assetName+" failed to load!");
		}
		
		return file;
	}
	
	private static RShaderLoader instance;
}
