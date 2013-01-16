package com.room.render;

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
		}		
		return instance;
	}
	
	//default rendering shader program ID
	public int progId;
	
	//mapped attributes
	public int aPosition;
	public int aNormal;
	
	//mapped uniforms
	public int uProjViewMatrix;
	public int uTexLoc;
	public int uTexCoords;
	public int uLightDir;
	
	public void init()
	{
		progId = loadProgram(vertexShaderCode, fragmentShaderCode);
		
		//bind attributes
		aPosition = GLES20.glGetAttribLocation(progId, "aPosition");
		uTexCoords = GLES20.glGetAttribLocation(progId, "aTexCoords");
		aNormal = GLES20.glGetAttribLocation(progId, "aNormal");
		
		//bind uniforms
		uProjViewMatrix = GLES20.glGetUniformLocation(progId, "uProjViewMatrix");
		uTexLoc = GLES20.glGetUniformLocation(progId, "uTexId");
		uLightDir = GLES20.glGetUniformLocation(progId, "uLightDir");	
	}
	
	private String vertexShaderCode =
	        "uniform mat4 uProjViewMatrix;" +
	        "attribute vec4 aPosition;" +
	        "attribute vec3 aNormal;" +
	        "varying vec3 vNormal;" +
			"attribute vec2 aTexCoords;" +
			"varying vec2 vTexCoords;" +	        		
	        "void main() {" +
	        "  vTexCoords = aTexCoords;" +
	        "  vNormal = aNormal;" +
	        "  gl_Position = uProjViewMatrix * aPosition;" +
	        "}";

	private String fragmentShaderCode =
		    "precision mediump float;" +
		    "uniform vec3 uLightDir;" +
		    "uniform sampler2D uTexId;" +
		    "varying vec3 vNormal;" +
		    "varying vec2 vTexCoords;" +		    
		    "void main() {" +
		    "  vec3 light = uLightDir;" +
		    "  vec3 lightNorm = normalize(light);" +
		    "  float lightWeight = max(dot(vNormal,lightNorm),0.0);" +
		    "  vec4 texColor = texture2D(uTexId, vTexCoords);" +		    
		    "  gl_FragColor = vec4(texColor.rgb * lightWeight, texColor.a);" +
		    "}";
		
	
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
	
	private static RShaderLoader instance;
}
