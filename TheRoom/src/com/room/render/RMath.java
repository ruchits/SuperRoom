package com.room.render;

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
}
