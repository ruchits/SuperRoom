package com.room.render;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.room.Global;
import com.room.media.MFootstepSound;
import com.room.media.MSoundManager;

import android.opengl.*;
import android.util.Log;

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

	public static final float PLAYER_HEIGHT = 15;
	public static final float PLAYER_START_X = 8.3532915f;
	public static final float PLAYER_START_Y = -14.7741165f; //this is really the z axis
	public static final float PLAYER_MAX_PITCH = 85;
	public static final float PLAYER_MIN_PITCH = -85;
	public static final float FLASHLIGHT_MAX_PITCH = 85;
	public static final float FLASHLIGHT_MIN_PITCH = -70;
	public static final float PLAYER_WALK_SPEED = 15; //units per second
	public static final float PLAYER_PITCH_SPEED = 50; //degrees per second
	public static final float PLAYER_YAW_SPEED = 90; //degrees per second
	public static final float PLAYER_HEADBOB_SPEED = 11f;
	public static final float PLAYER_HEADBOB_VERTICAL_MAGNITUDE = 0.02f;
	public static final float PLAYER_HEADBOB_HORIZONTAL_MAGNITUDE = 0.02f;

	public static final float FLASHLIGHT_HEIGHT_MIN = 8;
	public static final float FLASHLIGHT_HEIGHT_MAX = PLAYER_HEIGHT-2;
    public static final float FLASHLIGHT_VERTICAL_SPEED = FLASHLIGHT_HEIGHT_MAX-FLASHLIGHT_HEIGHT_MIN;
    private static final RMath.V2 FlASHLIGHT_P1= new RMath.V2(1, FLASHLIGHT_HEIGHT_MAX);
    private static final RMath.V2 FlASHLIGHT_P2= new RMath.V2(25, FLASHLIGHT_HEIGHT_MIN);


    public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //turn on depth test
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);

        //set blend function, but disable it until its needed
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        //turn on culling
		GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);

		// TBD - make it so that the resources DONT have to be reinitialized everytime surface changes!!
		RShaderLoader.getInstance().init();
		RTextureLoader.getInstance().init();

		//starting location:
		camPos[0] = PLAYER_START_X;
		camPos[1] = PLAYER_HEIGHT;
		camPos[2] = PLAYER_START_Y;
		camForward[0] = 1;
		camForward[1] = 0;
		camForward[2] = -1;
		camUp[0] = 0;
		camUp[1] = 1;
		camUp[2] = 0;
		camCurrentYaw = 0;
		camCurrentPitch = 0;
		lastDrawTime = System.currentTimeMillis();
		headbobCtr = 0;
		lastStep = 0;
		isVisible = false;

	    fpsCtrBaseFrame = 0;
	    fpsCtrBaseTime = lastDrawTime;
	    fpsCtrDisplay = 0;

		//set starting camera angle
		cameraPitch(-23.71069f);
		cameraYaw(16.102112f);
		
        Global.renderActivity.loadingDialog.dismiss(); 
		Global.renderActivity.showTextScene();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);

        Global.GL_WIDTH = width;
        Global.GL_HEIGHT = height;

        //temp speed hack for BB10:
		if(Global.HALF_RES_RENDER)
		{
			Global.GL_WIDTH = (int)(Global.SCREEN_WIDTH * 0.50f);
			Global.GL_HEIGHT = (int)(Global.SCREEN_HEIGHT * 0.50f);
		}

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

    public void onDrawFrame(GL10 unused)
    {
    	long currentTime = System.currentTimeMillis();
    	float deltaTimeSeconds = (currentTime - lastDrawTime)/1000f;
    	lastDrawTime = currentTime;

    	//update FPS counter
    	if ((currentTime - fpsCtrBaseTime) > 1000)
    	{
    		fpsCtrDisplay = (frameCtr-fpsCtrBaseFrame)*1000.0f/(currentTime - fpsCtrBaseTime);
    		fpsCtrBaseTime = currentTime;
    		fpsCtrBaseFrame=frameCtr;
    		if(Global.DEBUG_SHOW_FPS)
    			Log.d("FPS", fpsCtrDisplay+"");
    	}

    	//process all controller inputs
    	processControllerInput(deltaTimeSeconds);

        // Redraw background color, and clear depth buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //update the flashlight height
        updateFlashlightHeight(deltaTimeSeconds);

        //check if we are in a poi
        checkPOI();

        //update location sensitive sounds
        updateLocationSensitiveSounds();

        //Calculate the point the camera is currently looking at
        float[] camLookAt = {camPos[0]+camForward[0],camPos[1]+camForward[1],camPos[2]+camForward[2]};

        //calculate the headbob offsets
        RMath.V2 headBobOffsets = updateHeadbobOffsets();

    	//get the left vector by taking the negative recip of the forward XZ components
    	float[] camLeft = { -camForward[2], 0, camForward[0], 0 };
    	RMath.normalize(camLeft);

        // Set the camera position (View matrix)
        Matrix.setLookAtM
        	(
        		viewMatrix, 0,		//result, offset
        		camPos[0]+(camLeft[0]*headBobOffsets.x),	//eye point x
        		camPos[1]+headBobOffsets.y,					//eye point y
        		camPos[2]+(camLeft[2]*headBobOffsets.x),	//eye point z
        		camLookAt[0], camLookAt[1], camLookAt[2],	//center of view
        		camUp[0], camUp[1], camUp[2]				//up vector
        	);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(viewProjMatrix, 0, projMatrix, 0, viewMatrix, 0);

        //This is the position of the flashlight
        float[] spotLightPos = {
        		camPos[0],
        		currentFLHeight,
        		camPos[2]
        		};

        //This is the direction of the flashlight
        //first take the XZ component of camForward
        float[] spotLightVec = { camForward[0], 0, camForward[2], 0 };

    	//flashlight_min_pitch + flashlight_range * %_of_current_player_pitch
    	float degrees =  FLASHLIGHT_MIN_PITCH
    			+ (FLASHLIGHT_MAX_PITCH - FLASHLIGHT_MIN_PITCH)
    			* ((getCamCurrentPitch() - PLAYER_MIN_PITCH)/(PLAYER_MAX_PITCH - PLAYER_MIN_PITCH))
    			;

    	//pitch the flashlight
    	float[] axisRot = new float[16];
        Matrix.setIdentityM(axisRot, 0);
        Matrix.rotateM(axisRot, 0, degrees, camLeft[0],camLeft[1],camLeft[2]);
        Matrix.multiplyMV(spotLightVec, 0, axisRot, 0, spotLightVec, 0);

        //generate a random number flicker the flashlight
        float spotLightVariation = RMath.getRandFlashlightFlicker();

        //Draw objects
        if(isVisible)
        	RDrawLogic.getInstance().draw(viewProjMatrix,spotLightPos,spotLightVec,spotLightVariation);
        
        ++frameCtr;
    }

    private void processControllerInput(float deltaTimeSeconds)
    {
    	//get left controller state:
    	if(RTouchController.getInstance().isLeftStickActive())
    	{
	    	cameraMove(RTouchController.getInstance().getLeftValue()*deltaTimeSeconds*PLAYER_WALK_SPEED,
					RTouchController.getInstance().getLeftAngle());
	    	headbobCtr+=PLAYER_HEADBOB_SPEED*RTouchController.getInstance().getLeftValue()*deltaTimeSeconds;
    	}
    	//get keyboard state
    	else if(RKeyController.WKeyDown)
    	{
	    	cameraMove(deltaTimeSeconds*PLAYER_WALK_SPEED,0);
	    	headbobCtr+=PLAYER_HEADBOB_SPEED*deltaTimeSeconds;
    	}
    	else if(RKeyController.SKeyDown)
    	{
	    	cameraMove(deltaTimeSeconds*PLAYER_WALK_SPEED,180);
	    	headbobCtr+=PLAYER_HEADBOB_SPEED*deltaTimeSeconds;
    	}
    	else if(RKeyController.AKeyDown)
    	{
	    	cameraMove(deltaTimeSeconds*PLAYER_WALK_SPEED,90);
	    	headbobCtr+=PLAYER_HEADBOB_SPEED*deltaTimeSeconds;
    	}
    	else if(RKeyController.DKeyDown)
    	{
	    	cameraMove(deltaTimeSeconds*PLAYER_WALK_SPEED,270);
	    	headbobCtr+=PLAYER_HEADBOB_SPEED*deltaTimeSeconds;
    	}
    	//is no movement, then land the current step (headbob)
    	else
    	{
    		float periods = headbobCtr/RMath.PI_TIMES_2;
    		float periodsRem = 1 - (periods - (float)Math.floor(periods));

    		if(periodsRem != 1 && periodsRem > 0.05f)
    			headbobCtr += Math.min(PLAYER_HEADBOB_SPEED*deltaTimeSeconds, periodsRem*RMath.PI_TIMES_2);
    	}

    	//get right controller state:
    	if(RTouchController.getInstance().isRightStickActive())
    	{
			cameraYaw(-RTouchController.getInstance().getRightVX()*deltaTimeSeconds*PLAYER_YAW_SPEED);
			cameraPitch(RTouchController.getInstance().getRightVY()*deltaTimeSeconds*PLAYER_PITCH_SPEED);
    	}
    	else if(RKeyController.QKeyDown)
    	{
    		cameraYaw(deltaTimeSeconds*PLAYER_YAW_SPEED);
    	}
    	else if(RKeyController.EKeyDown)
    	{
    		cameraYaw(-deltaTimeSeconds*PLAYER_YAW_SPEED);
    	}
    }

    private void updateFlashlightHeight(float deltaTimeSeconds)
    {
    	//calc height every 5 frames
        if(frameCtr %5 == 0) {
        	/* Assumption: targetFLHeight always lies between FLASHLIGHT_HEIGHT_MIN and FLASHLIGHT_HEIGHT_MAX
        	 * getFlashLightHeight will take care of this and make sure the value returned is valid.
        	 */
        	targetFLHeight = getFlashLightHeight();

        }

    	float jump = FLASHLIGHT_VERTICAL_SPEED*deltaTimeSeconds;

    	if(currentFLHeight < (targetFLHeight - jump))
    		currentFLHeight +=  jump;
    	else if (currentFLHeight > (targetFLHeight + jump))
    		currentFLHeight -=  jump;
    	else
    		currentFLHeight = targetFLHeight;

        //Log.e(TAG, "targetFLHeight= " + targetFLHeight + " currentHeight= " + currentFLHeight);
    }

    private void checkPOI()
    {
    	if(frameCtr %5 == 1)
    	{
    		String poiName = RPOIManager.getInstance().checkPOI(camPos[0], camPos[2], camForward[0], camForward[2]);
    		RTopButtons.getInstance().setPOI(poiName);
    	}
    }

    private void updateLocationSensitiveSounds()
    {
    	if(frameCtr %5 == 2)
    	{
    		MSoundManager.getInstance().updateLocation(camPos[0], camPos[2]);
    	}
    }

    private RMath.V2 updateHeadbobOffsets()
    {
        //Calculate the headbob to offset the camPos
    	RMath.V2 offset = new RMath.V2();
    	offset.y = PLAYER_HEADBOB_VERTICAL_MAGNITUDE * (float)Math.cos(headbobCtr);
        offset.x = (float)Math.sin(headbobCtr/2);

    	//check if we should play a footstep sound
    	if(offset.x > 0.8 && lastStep != 1)
    	{
    		MFootstepSound.playRandomStep();
    		lastStep = 1;
    	}
    	else if(offset.x < -0.8 && lastStep != -1)
    	{
    		MFootstepSound.playRandomStep();
    		lastStep = -1;
    	}

    	offset.x *= PLAYER_HEADBOB_HORIZONTAL_MAGNITUDE;

    	return offset;
    }

    public void cameraMove(float distance, float degrees)
    {
        float[] camLookAtXZ =
        	{ camForward[0], 0, camForward[2], 0};

    	float[] yRot = new float[16];
        Matrix.setIdentityM(yRot, 0);
        Matrix.rotateM(yRot, 0, degrees, 0,1,0);
        Matrix.multiplyMV(camLookAtXZ, 0, yRot, 0, camLookAtXZ, 0);

        RMath.normalize(camLookAtXZ);

    	camLookAtXZ[0] *= distance;
    	camLookAtXZ[1] *= distance;
    	camLookAtXZ[2] *= distance;

        // Do I collide with the line camPos and camPos+CamLookAtXZ?
        // If yes, then project the vector and figure out new coordinates for camPos.
        RMath.Line current = new RMath.Line(new RMath.V2(camPos[0], camPos[2]), new RMath.V2((camPos[0] + camLookAtXZ[0]), (camPos[2] + camLookAtXZ[2])));
        //Log.d(TAG, current.toString());

        // get the Intersecting point with one of the test walls.
        RMath.V2 newPosition = checkBounds(current);
        if (newPosition == null) {
        	camPos[0] += camLookAtXZ[0];
            camPos[1] += camLookAtXZ[1];
            camPos[2] += camLookAtXZ[2];
        }
        else {
            camPos[0] = newPosition.x;
            camPos[1] += camLookAtXZ[1];
            camPos[2] = newPosition.y;
        }
    }

    public void cameraPitch(float degrees)
    {
    	if(camCurrentPitch+degrees > PLAYER_MAX_PITCH)
    	{
    		degrees = PLAYER_MAX_PITCH - camCurrentPitch;
    	}
    	else if(camCurrentPitch+degrees < PLAYER_MIN_PITCH)
    	{
    		degrees = PLAYER_MIN_PITCH - camCurrentPitch;
    	}

    	//get the left vector by taking the negative recip of the forward XZ components
    	float[] camLeft = { -camForward[2], 0, camForward[0], 0 };

    	float[] axisRot = new float[16];
        Matrix.setIdentityM(axisRot, 0);
        Matrix.rotateM(axisRot, 0, degrees, camLeft[0],camLeft[1],camLeft[2]);

        float[] camLookAtVector = {
        		camForward[0],
        		camForward[1],
        		camForward[2],
        		0
        	};

        float[] rotatedCameraLookAtVector = new float[4];

        Matrix.multiplyMV(rotatedCameraLookAtVector, 0,
        		axisRot, 0, camLookAtVector, 0);

        camForward[0] = rotatedCameraLookAtVector[0];
        camForward[1] = rotatedCameraLookAtVector[1];
        camForward[2] = rotatedCameraLookAtVector[2];

        camCurrentPitch+=degrees;
    }

    public void cameraYaw(float degrees)
    {
    	float[] yRot = new float[16];
        Matrix.setIdentityM(yRot, 0);
        Matrix.rotateM(yRot, 0, degrees, 0,1,0);

        float[] camLookAtVector = {
        		camForward[0],
        		camForward[1],
        		camForward[2],
        		0
        	};

        float[] rotatedCameraLookAtVector = new float[4];

        Matrix.multiplyMV(rotatedCameraLookAtVector, 0,
        		yRot, 0, camLookAtVector, 0);

        camForward[0] = rotatedCameraLookAtVector[0];
        camForward[1] = rotatedCameraLookAtVector[1];
        camForward[2] = rotatedCameraLookAtVector[2];

    	camCurrentYaw += degrees;
    }


    /* Check if the new position lies within the bounds of the game.
     * If yes, then return null. If no, then return a projection along the wall
     * it intersects with.
     */
    public RMath.V2 checkBounds(RMath.Line current) {
        float result = 0;
        RMath.V2 intersection = null;
        boolean corner = false;
        ArrayList<RMath.Line> crossWalls = new ArrayList<RMath.Line>();

        // Get the cross-product first. If negative, only then check for intersection and block the cam.
        // If positive, then that means that we are trying to stay inside the room. So don't block!
        for(RMath.Line Wall: RModelLoader.activeWallBoundary) {
                if(RMath.crossProduct(Wall.toVector(), current.toVector()) < 0) {
                        crossWalls.add(Wall);
                }
        }
        for(RMath.Line Wall: RModelLoader.activePropBoundary) {
            if(RMath.crossProduct(Wall.toVector(), current.toVector()) < 0) {
                    crossWalls.add(Wall);
            }
        }

        RMath.V2 projectPosition = null;
        RMath.Line projectLine = null;

        // Check for intersection with every wall in the room.
        for (RMath.Line Wall: crossWalls) {
        	if(intersection == null) {
        		intersection = current.getLineIntersectionP(Wall);
                if (intersection != null) {
                	result = RMath.projectVector(current.toVector(), Wall.toVector());
                    //Log.e(TAG, "INTERSECT Wall: " + Wall.toString());

                    // Get the projected point/line. We need to check this against other walls.
                    RMath.V2 wallVector = Wall.toVector();
                    RMath.normalize(wallVector);

                    // Instead of sliding along the wall, we will slide along the line passing through
                    // our current position(parallel to the wall). That way, we can avoid issues related
                    // to non-orthogonal walls.
                    float new_x = result*wallVector.x + current.begin.x;
                    float new_y = result*wallVector.y + current.begin.y;
                    /*
                    float new_x = result*wallVector.x + intersection.x;
                    float new_y = result*wallVector.y + intersection.y;
                    */
                    projectPosition = new RMath.V2(new_x, new_y);
                    projectLine = new RMath.Line(current.begin, projectPosition);

                    // check the intersection of the newPosition with every other wall in the arraylist.
                    for (RMath.Line cornerWall: crossWalls) {
                    	if (cornerWall != Wall) {
                    		RMath.V2 temp = projectLine.getLineIntersectionP(cornerWall);
                            if (temp != null) {
                            	// this is the corner case. Intersect with two wall segments at the same time.
                            	// 1. Position the view at the intersection of the wall instead? => Rounding issues
                                // 2. for now keep the intersection the same as the camPos to avoid round issues.
                                corner = true;
                                intersection.x = camPos[0];
                                intersection.y = camPos[2];
                                //Log.e(TAG, "CORNER Wall: " + cornerWall.toString());
                                break;
                            }
                    	}
                    }
                    break;
                }
        	}
        }

        if (intersection != null && corner) {
                return intersection;
        }
        else if(intersection != null) {
                return projectPosition;
        }
        else
                return null;
    }

    /*
     * Consider the two points as: (1, FLASHLIGHT_HEIGHT_MAX) and (15, FLASHLIGHT_HEIGHT_MIN)
     */
    private float getFlashLightHeight() {
        RMath.V2 distanceVector;
        /* Get the distance with the crossWalls. Get the smallest distance
         * and based on the distance obtained decide the flashlight height.
         */
        RMath.V2 forward = new RMath.V2(camForward[0], camForward[2]);
        RMath.normalize(forward);
        RMath.Line current = new RMath.Line(new RMath.V2(camPos[0], camPos[2]), new RMath.V2((camPos[0] + forward.x), (camPos[2] + forward.y)));
        RMath.V2 currentVector = current.toVector();
        // Get the cross-product first. If negative, only then check for intersection and block the cam.
        ArrayList<RMath.Line> crossWalls = new ArrayList<RMath.Line>();
        for(RMath.Line Wall: RModelLoader.activeWallBoundary) {
                if(RMath.crossProduct(Wall.toVector(), currentVector) < 0) {
                        crossWalls.add(Wall);
                }
        }

        float minDistance = Float.POSITIVE_INFINITY;
        for (RMath.Line Wall: crossWalls) {
                distanceVector = current.getDistance(Wall);
                if ( (distanceVector.x >= 0 && distanceVector.x < minDistance) && (distanceVector.y <=1 && distanceVector.y >= 0)) minDistance = distanceVector.x;
        }

        float magnitude = 1;
        if (minDistance != Float.POSITIVE_INFINITY && minDistance >= 0) {
                magnitude = currentVector.x * currentVector.x +
                                        currentVector.y * currentVector.y;
                magnitude = (float)Math.sqrt(magnitude);
        }

        float distance = minDistance*magnitude;
        //Log.e(TAG, "distance= " + distance);

        if (distance == Float.POSITIVE_INFINITY) {
                return FLASHLIGHT_HEIGHT_MIN;
        }

        float scale = (distance -  FlASHLIGHT_P1.x)/(FlASHLIGHT_P2.x -  FlASHLIGHT_P1.x);
        float height = FlASHLIGHT_P1.y + (scale * (FlASHLIGHT_P2.y -  FlASHLIGHT_P1.y));

        if (height < FLASHLIGHT_HEIGHT_MIN) return FLASHLIGHT_HEIGHT_MIN;
        else if (height > FLASHLIGHT_HEIGHT_MAX) return FLASHLIGHT_HEIGHT_MAX;
        else return height;
    }

    public float[] camPos = new float[3];		//position
    public float[] camForward = new float[3];	//vector
    public float[] camUp = new float[3];		//vector

    public float getCamCurrentYaw()
    {
    	return camCurrentYaw;
    }

    public float getCamCurrentPitch()
    {
    	return camCurrentPitch;
    }
    
    public void resetFlashlightHeight()
    {
    	currentFLHeight = 5;
    }
    
    public void resetLastDrawTime()
    {
    	lastDrawTime = System.currentTimeMillis();
    }
    
    public void setVisible(boolean visibility)
    {
    	isVisible = visibility;
    }
    
    private boolean isVisible;
    private float currentFLHeight = FLASHLIGHT_HEIGHT_MIN;
    private float targetFLHeight = 0;

    public int frameCtr = 0;

    private int fpsCtrBaseFrame;
    private long fpsCtrBaseTime;
    public float fpsCtrDisplay;

    private float camCurrentYaw;
    private float camCurrentPitch;
    private long lastDrawTime; //in millis
    public float headbobCtr;
    private int lastStep; 	//used to time footstep sound with headbob
    							//store -1 or 1 if last step was a left or right step

	private float[] projMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] viewProjMatrix = new float[16];

    private static final String TAG = "com.render.RRenderer";
    private static RRenderer instance;
}
