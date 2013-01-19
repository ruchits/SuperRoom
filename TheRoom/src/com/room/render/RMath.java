package com.room.render;

import com.room.Global;

public class RMath
{
	static class V3
	{
		float x,y,z;
		public V3(){}
		public V3(float x, float y, float z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	static class V2
	{
		float x,y;
		public V2(){}
		public V2(float x, float y)
		{
			this.x = x;
			this.y = y;
		}		
	}
	
	public static void normalize(float[] vector)
	{
		float len = vector[0] * vector[0] +
				vector[1] * vector[1] + 
				vector[2] * vector[2];
		len = (float)Math.sqrt(len);		
	}	
	
	public static float pixelToGLX(float x)
	{
		return (x/Global.SCREEN_WIDTH)*2 -1;
	}
	
	public static float pixelToGLY(float y)
	{
		return -((y/Global.SCREEN_HEIGHT)*2 -1);
	}	
	
	public static float RAD_TO_DEG = 57.295779513082320876798154814105f;
	public static float DEG_TO_RAD = 0.01745329251994329576923690768489f;
}
