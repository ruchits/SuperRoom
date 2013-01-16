package com.room.render;

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
		
		camPos[0] = 0;
		camPos[1] = 15;
		camPos[2] = 0;
		camLookAt[0] = -1;
		camLookAt[1] = 15;
		camLookAt[2] = -1;	
		camUp[0] = 0;
		camUp[1] = 1;
		camUp[2] = 0;
		camCurrentAngle = 0;
    }

    public void onDrawFrame(GL10 unused)
    {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
              
        float[] lightDir = {camPos[0]-camLookAt[0],camPos[1]-camLookAt[1],camPos[2]-camLookAt[2]};
        
        // Set the camera position (View matrix)
        Matrix.setLookAtM
        	(
        		viewMatrix, 0,		//result, offset
        		camPos[0], camPos[1], camPos[2],			//eye point
        		camLookAt[0], camLookAt[1], camLookAt[2],	//center of view
        		camUp[0], camUp[1], camUp[2]				//up vector
        	);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(viewProjMatrix, 0, projMatrix, 0, viewMatrix, 0);                      
        
        RModelLoader.getInstance().modelRoom.draw(viewProjMatrix, lightDir);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
        
        float ratio = 1;
        
        if(width > height)
        {
        	ratio = (float) height / width; 
        	Matrix.frustumM(projMatrix, 0, -1, 1, -ratio, ratio, 1f, 100f);
        }
        else
        {
        	ratio = (float) width / height;
        	Matrix.frustumM(projMatrix, 0, -ratio, ratio, -1, 1, 1f, 100f);
        }
    }    
	   
    
    public void cameraForward(float distance)
    {
        float[] camLookAtXZ = 
        	{ camLookAt[0]-camPos[0], 0, camLookAt[2]-camPos[2] };
        
        RMath.normalize(camLookAtXZ);
        
    	camLookAtXZ[0] *= distance;
    	camLookAtXZ[1] *= distance;
    	camLookAtXZ[2] *= distance;
    	
    	camPos[0] += camLookAtXZ[0];
    	camPos[1] += camLookAtXZ[1];
    	camPos[2] += camLookAtXZ[2];
    
    	camLookAt[0] += camLookAtXZ[0];
    	camLookAt[1] += camLookAtXZ[1];
    	camLookAt[2] += camLookAtXZ[2];    	
    }

    // tbd willc - Not implemented yet:
    /*public void cameraStrafe(float d)
    {
    	D3DXVECTOR3 cameraLookAtVector = cameraLookAt - cameraPos;
    	D3DXVECTOR3 cameraLookAtVector90Rotate (cameraLookAtVector.z,0,-cameraLookAtVector.x);
    	D3DXVec3Normalize(&cameraLookAtVector90Rotate,&cameraLookAtVector90Rotate);
    	cameraLookAtVector90Rotate *= d;
    	cameraPos += cameraLookAtVector90Rotate;
    	cameraLookAt += cameraLookAtVector90Rotate;
    }*/

    public void cameraTurn(float degrees)
    {
    	float[] yRot = new float[16];
        Matrix.setIdentityM(yRot, 0);
        Matrix.rotateM(yRot, 0, degrees, 0,1,0);    	

        float[] camLookAtVector = {
        		camLookAt[0]-camPos[0],
        		camLookAt[1]-camPos[1],
        		camLookAt[2]-camPos[2],
        		0
        	};

        float[] rotatedCameraLookAtVector = new float[4];
        		
        Matrix.multiplyMV(rotatedCameraLookAtVector, 0,
        		yRot, 0, camLookAtVector, 0);

    	camLookAt[0] = rotatedCameraLookAtVector[0] + camPos[0];
    	camLookAt[1] = rotatedCameraLookAtVector[1] + camPos[1];
    	camLookAt[2] = rotatedCameraLookAtVector[2] + camPos[2];

    	camCurrentAngle += degrees;
    }    
    
    public float[] camPos = new float[3];
    public float[] camLookAt = new float[3];
    public float[] camUp = new float[3];
    
    public float getCamCurrentAngle()
    {
    	return camCurrentAngle;
    }
    
    private float camCurrentAngle;
    
	private float[] projMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] viewProjMatrix = new float[16]; 
    
    private static RRenderer instance;
}
