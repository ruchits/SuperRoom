package com.room.render;

import java.util.Random;
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
	
	static class Line
	{
		V2 begin, end;
		public Line(float x1, float y1, float x2, float y2) {
			begin = new V2(x1, y1);
			end = new V2(x2, y2);
		}
		public Line(V2 begin, V2 end) {
			this.begin = begin;
	      	this.end   = end;
		}
		public String toString() {
			return "Line from (" + begin.x + "," + begin.y + ") to (" + end.x + "," + end.y +")";
		}
		
		// Cartesian formula to get line intersection.
		public V2 getLineIntersectionC(Line l) {
			float d = ((this.begin.x - this.end.x) * (l.begin.y - l.end.y)) - ((this.begin.y - this.end.y) * (l.begin.x - l.end.x));
			if(d == 0) // If parallel.
				return null;
			else {
				float a = (this.begin.x * this.end.y) - (this.begin.y * this.end.x);
			    float b = (l.begin.x * l.end.y) - (l.begin.y * l.end.x);
			   
			    float x = (a * (l.begin.x - l.end.x)) - ((this.begin.x - this.end.x) * b);
			    float y = (a * (l.begin.y - l.end.y)) - ((this.begin.y - this.end.y) * b); 
			    x /= d;
			    y /= d;
			    
			    // Check the bounds to make sure the point lies on the two line segments.
			    if (x < Math.min(this.begin.x, this.end.x) || x > Math.max(this.begin.x, this.end.x))
			    	return null;
			    if (x < Math.min(l.begin.x, l.end.x) || x > Math.max(l.begin.x, l.end.x))
			    	return null;
			    if (y < Math.min(this.begin.y, this.end.y) || y > Math.max(this.begin.y, this.end.y))
			    	return null;
			    if (y < Math.min(l.begin.y, l.end.y) || y > Math.max(l.begin.y, l.end.y))
			    	return null;
			    
			    return new V2(((a * (l.begin.x - l.end.x)) - ((this.begin.x - this.end.x) * b)) / d,
	                    ((a * (l.begin.y - l.end.y)) - ((this.begin.y - this.end.y) * b)) / d);
			}
				
		}
		
		/* We will use the parametric equation to solve for the line intersection.
		 * The benefit of this method is that we don't have to worry about running into
		 * float precision issues + this is cooler!
		 * For a line with two points: A and B
		 * Parametric equation is of the form:
		 * 				x = A.x + s * Vab.x
		 * 				y = A.y + t * Vab.y 
		 */
		public V2 getLineIntersectionP(Line l) {
			float d, n, s, t;
			
			V2 Vthis = this.toVector();
			V2 Vl = l.toVector();
			
			d = (Vthis.x*Vl.y) - (Vthis.y*Vl.x);
			if (d == 0) // if parallel
				return null;
			
			n = (this.begin.y - l.begin.y) * (Vl.x) - (this.begin.x - l.begin.x) * (Vl.y);
			s = n/d;
			
			n = (l.begin.x - this.begin.x) * (Vthis.y) - (l.begin.y - this.begin.y) * (Vthis.x);
			t = n/d;
			//Log.d(TAG, "s= " + s + ", d= " + d);
			
			if ((s<0 || s>1) || (t<0 || t>1)) {
				return null;
			}
			else {
				float x = this.begin.x + (Vthis.x * s);
				float y = this.begin.y + (Vthis.y * s);
				//Log.d(TAG, "(x, y) = " + x + ", " + y);
				V2 result = new V2(x, y);
				return result;
			}
		}
		
		public V2 getDistance(Line l) {
            float d, n, s, t;

            V2 Vthis = this.toVector();
            V2 Vl = l.toVector();

            d = (Vthis.x*Vl.y) - (Vthis.y*Vl.x);
            if (d == 0) // if parallel
                    return null;

            n = (this.begin.y - l.begin.y) * (Vl.x) - (this.begin.x - l.begin.x) * (Vl.y);
            s = n/d;

            n = (l.begin.x - this.begin.x) * (Vthis.y) - (l.begin.y - this.begin.y) * (Vthis.x);
			t = n/d;
			
            return new V2(s, t);
		}

		// direction: begin -> end
		public V2 toVector() {
			return new V2(end.x-begin.x, end.y-begin.y);
		}
	}
	
	// crossProduct: v1.v2 = v1.x*v2.y - v1.y*v2.x
	public static float crossProduct(V2 v1, V2 v2) {
			if(v1==null || v2==null)
				throw new IllegalArgumentException("The vectors can't be null!");
			
			return (v1.x*v2.y - v1.y*v2.x);
	}

	// dotProduct: v1.v2 = v1.x*v2.x + v1.y*v2.y
	public static float dotProduct(V2 v1, V2 v2) {
		if(v1==null || v2==null)
			throw new IllegalArgumentException("The vectors can't be null!");
		
		return (v1.x*v2.x +v1.y*v2.y);
	}

	//projection of v on axis= v.dotProduct(normalize(axis))
	public static float projectVector(V2 v, V2 axis) {
		normalize(axis);
		return dotProduct(v,axis);	
	}
    
	public static void normalize(V2 vector)
	{
		float len = vector.x * vector.x + 
				vector.y * vector.y;
		len = (float)Math.sqrt(len);
		vector.x /= len;
		vector.y /= len;
	}
	
	public static void normalize(float[] vector)
	{
		float len = vector[0] * vector[0] +
				vector[1] * vector[1] + 
				vector[2] * vector[2];
		len = (float)Math.sqrt(len);
		vector[0] /= len;
		vector[1] /= len;
		vector[2] /= len;
	}	
	
	public static float pixelToGLX(float x)
	{
		return (x/Global.SCREEN_WIDTH)*2 -1;
	}
	
	public static float pixelToGLY(float y)
	{
		return -((y/Global.SCREEN_HEIGHT)*2 -1);
	}	
	
	public static int getRandInt(int inclusiveMin, int inclusiveMax)
	{
		return rand.nextInt(inclusiveMax-inclusiveMin+1) + inclusiveMin;
	}
	
	public static float getRandFlashlightFlicker()
	{
		float num = (float) Math.abs(rand.nextGaussian()*0.5);
		if(num > 1) num = 0;
		num*=0.05f;
		num = 1-num;
		return num;
	}
	
	public static float RAD_TO_DEG = 57.295779513082320876798154814105f;
	public static float DEG_TO_RAD = 0.01745329251994329576923690768489f;
	public static float PI_TIMES_4 = 12.566370614359172953850573533118f;
	public static float PI_TIMES_2 = 6.283185307179586476925286766559f;
	public static float PI = 3.1415926535897932384626433832795f;
	private static Random rand;
	private static final String TAG = "com.render.RRenderer";
	
	
	static
	{
		rand = new Random();
	}
	
}
