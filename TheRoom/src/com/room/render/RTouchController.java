package com.room.render;

import android.view.MotionEvent;

public class RTouchController
{
	//SINGLETON!!
	public static RTouchController getInstance()
	{
		if(instance == null)
		{
			instance = new RTouchController();
		}
		return instance;
	}
	
	private RTouchController()
	{
		rightKnob = new RScreenImage(
				RTextureLoader.getInstance().getTextureID("joystick_knob"));
		rightKnob.setSize(STICK_SIZE);
		rightKnob.setVisible(false);
		
		rightRing = new RScreenImage(
				RTextureLoader.getInstance().getTextureID("joystick_ring"));
		rightRing.setSize(STICK_SIZE);
		rightRing.setVisible(false);
		
		leftKnob = new RScreenImage(
				RTextureLoader.getInstance().getTextureID("joystick_knob"));
		leftKnob.setSize(STICK_SIZE);
		leftKnob.setVisible(false);
		
		leftRing = new RScreenImage(
				RTextureLoader.getInstance().getTextureID("joystick_ring"));
		leftRing.setSize(STICK_SIZE);
		leftRing.setVisible(false);		
	}
	
	public void draw()
	{
		rightRing.draw();
		rightKnob.draw();
		leftRing.draw();
		leftKnob.draw();		
	}
	
	public float getLeftValue()
	{
		return leftValue;
	}
	
	public float getLeftAngle()
	{
		return leftAngle;
	}
		
	public float getRightVX()
	{
		return rightVX;
	}
	
	public float getRightVY()
	{
		return rightVY;
	}
	
	public void processTouchEvent(MotionEvent event)
	{
		//safety check - if we are getting more than 2 points, then dont do anything
		if(event.getPointerCount()>2)
			return;
		
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		
		int pid = -1;
		int code = -1;
		int index = -1;
		

		
		if (actionCode == MotionEvent.ACTION_UP
		|| actionCode == MotionEvent.ACTION_DOWN)
		{
			pid = event.getPointerId(0);
			code = actionCode;
		}		
		else if (actionCode == MotionEvent.ACTION_POINTER_DOWN)
	    {
	    	pid = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
	    	code = MotionEvent.ACTION_DOWN;
	    }
		else if (actionCode == MotionEvent.ACTION_POINTER_UP)
	    {
	    	pid = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
	    	code = MotionEvent.ACTION_UP;
	    }
		else if(actionCode == MotionEvent.ACTION_MOVE)
		{
			//process and return!
			code = MotionEvent.ACTION_MOVE;
			
		    for (int i = 0; i < event.getPointerCount(); i++)
		    {
		    	pid = event.getPointerId(i);
				float x = RMath.pixelToGLX(event.getX(i));
				float y = RMath.pixelToGLY(event.getY(i));	
				
		    	if(pid == leftPid)
	    		{
		    		float vx = x-leftX;
		    		float vy = y-leftY;		    		
		    		float length = (float)Math.sqrt(vx*vx+vy*vy);
		    		
		    		if(length > STICK_RADIUS)
		    		{		    		
		    			//normalize v
			    		vx/=length;
			    		vy/=length;
			    		
			    		//set it to stick radius
			    		vx*=STICK_RADIUS;
			    		vy*=STICK_RADIUS;
			    		
			    		leftValue = 1.0f;
		    		}
		    		else
		    		{
		    			leftValue = length/STICK_RADIUS;
		    		}
		    		
		    		leftAngle = (float)Math.atan2(vy, vx)*RMath.RAD_TO_DEG-90;
		    		leftKnob.setPosition(leftX + vx, leftY + vy);		    		
	    		}
		    	else if(pid == rightPid)
		    	{		    	
		    		float vx = x-rightX;
		    		float vy = y-rightY;		    		
		    		float length = (float)Math.sqrt(vx*vx+vy*vy);
		    		
		    		if(length > STICK_RADIUS)
		    		{		    		
		    			//normalize v
			    		vx/=length;
			    		vy/=length;
			    		
			    		//set it to stick radius
			    		vx*=STICK_RADIUS;
			    		vy*=STICK_RADIUS;
		    		}
		    		
		    		rightVX = vx/STICK_RADIUS;
		    		rightVY = vy/STICK_RADIUS;
		    		rightKnob.setPosition(rightX + vx, rightY + vy);		    		
		    	}
		    }				
			
			return;
		}
		else
		{
			//nothing to do - return!
			return;
		}
		
		//------------
		//At this point, it's either a UP or DOWN event: 
		
	    for (int i = 0; i < event.getPointerCount(); i++)
	    {
	    	if(event.getPointerId(i) == pid)
	    		index = i;
	    }		
	    if (index == -1) return;
		float x = RMath.pixelToGLX(event.getX(index));
		float y = RMath.pixelToGLY(event.getY(index));	    
	    
		if(code == MotionEvent.ACTION_DOWN)
		{
			//left side of screen pressed
			if( x < 0 )
			{
				activateLeftStick(pid,x,y);
			}
			//right side of screen pressed
			else
			{
				activateRightStick(pid,x,y);
			}
		}
		else if(code == MotionEvent.ACTION_UP)
		{
			if(pid == leftPid)
			{
				deactivateLeftStick();
			}
			else if(pid == rightPid)
			{
				deactivateRightStick();
			}
		}
		
	}
	
	public boolean isLeftStickActive()
	{
		return leftKnob.isVisible();
	}
	
	public boolean isRightStickActive()
	{
		return rightKnob.isVisible();
	}
	
	private void activateLeftStick(int pid, float glx, float gly)
	{
		if(leftPid == -1)
		{
			leftPid = pid;
			leftKnob.setPosition(glx,gly);
			leftRing.setPosition(glx,gly);	
			leftKnob.setVisible(true);
			leftRing.setVisible(true);
			leftX = glx;
			leftY = gly;
		}
	}
	
	private void deactivateLeftStick()
	{
		leftPid = -1;
		leftValue = 0;
		leftKnob.setVisible(false);
		leftRing.setVisible(false);
	}
	
	private void activateRightStick(int pid, float glx, float gly)
	{
		if(rightPid == -1)
		{
			rightPid = pid;
			rightKnob.setPosition(glx,gly);
			rightRing.setPosition(glx,gly);			
			rightKnob.setVisible(true);
			rightRing.setVisible(true);
			rightX = glx;
			rightY = gly;			
		}
	}	
	
	private void deactivateRightStick()
	{
		rightPid = -1;
		rightVX = 0;
		rightVY = 0;
		rightKnob.setVisible(false);
		rightRing.setVisible(false);
	}	
	
	private float leftX;
	private float leftY;
	private int leftPid = -1;
	//left stick is based on value + angle
	private float leftValue;
	private float leftAngle;
	
	private float rightX;
	private float rightY;
	private int rightPid = -1;
	//right stick is based on (vx,vy)	
	private float rightVX;
	private float rightVY;
	
	private RScreenImage rightKnob;
	private RScreenImage rightRing;
	private RScreenImage leftKnob;
	private RScreenImage leftRing;	
	
	private static final float STICK_RADIUS = 0.10f;
	private static final float STICK_SIZE = 0.25f;
	
	private static RTouchController instance;
}
