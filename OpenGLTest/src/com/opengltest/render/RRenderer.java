package com.opengltest.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.*;

public class RRenderer implements GLSurfaceView.Renderer
{
	//SINGLETON!
	public static RRenderer getInstance()
	{
		if(instance == null)
		{
			instance = new RRenderer();			
		}		
		return instance;
	}	
	
	public RSurfaceView glSurfaceView;
	
    public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
        // Set the background frame color
        GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        
        //turn on depth test
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);        
        
        //turn on culling
		GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);     
        		
		//init shaders + resources 
		RShaderLoader.getInstance().init();
		RTextureLoader.getInstance().init();
		
        // initialize a cube
        cube = new RCube();
    }

    public void onDrawFrame(GL10 unused)
    {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        float[] rotCamMat = new float[16];
        Matrix.setIdentityM(rotCamMat, 0);
        Matrix.rotateM(rotCamMat, 0, cameraRotation, 0,1,0);
        
        float[] camInitialPos = new float[] {0,2*RSurfaceView.zoom,-3*RSurfaceView.zoom,0};
        float[] camNewPos = new float[4];
        Matrix.multiplyMV(camNewPos, 0, rotCamMat, 0, camInitialPos, 0);
              
        float[] lightDir = camNewPos;
        
        // Set the camera position (View matrix)
        Matrix.setLookAtM
        	(
        		viewMatrix, 0,		//result, offset
        		camNewPos[0], camNewPos[1], camNewPos[2],			//eye point
        		0f, 0f, 0f,			//center of view
        		0f, 1.0f, 0.0f		//up vector
        	);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(viewProjMatrix, 0, projMatrix, 0, viewMatrix, 0);                      
        
        cube.draw(viewProjMatrix, lightDir);
        
        cameraRotation+=0.5;
    }

    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;

        // this populates the projection matrix
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projMatrix, 0, -ratio, ratio, -1, 1, 1f, 20f);        
    }    
	
	private RCube cube;
	
	private float[] projMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] viewProjMatrix = new float[16];
	
	private float cameraRotation = 0.0f;    
    
    private static RRenderer instance;
}
