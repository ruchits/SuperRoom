package com.room.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.room.Global;

import android.content.res.AssetManager;
import android.util.Log;

//tbd willc - using OBJ format is slow in debug mode.
//            if you're loading lots of data here, don't use debug!

public class RModelLoader
{
        private static final String TAG = "com.render.RModelLoader";

	//SINGLETON!
	public static RModelLoader getInstance()
	{
		if(instance == null)
		{
			instance = new RModelLoader();
			//instance.init();
		}		
		return instance;
	}
		
	public RModel modelRoom;
	public RModel modelDoorBathroomStage1;
	public RModel modelDoorBathroomStage2;
	public RModel decalWall;
	public RModel decalBoard;
	public RModel decalCeilingMinor;
	public RModel decalCeilingMajor;
	public RModel decalNumber;
	public RModel modelPOI;
	
	public static ArrayList<RMath.Line> Walls;
	
	private ArrayList<RMath.Line> boundaryStage1;
	private ArrayList<RMath.Line> boundaryStage2;
	private ArrayList<RMath.Line> boundaryProps;

	public void init()
	{
		if(Global.DEBUG_NO_PROPS)
		{
			modelRoom = loadModel("room_empty.obj");	
		}
		else
		{		
			modelRoom = loadModel("room_with_props.obj");
		}
		
		modelDoorBathroomStage1 = loadModel("door_bathroom_stage1.obj");
		modelDoorBathroomStage2 = loadModel("door_bathroom_stage2.obj");
		
		if(Global.DEBUG_SHOW_POI_BOXES)
			modelPOI = loadModel("points_of_interest.obj");
		
		if(!Global.DEBUG_NO_DECALS)
		{
			decalWall = loadModel("decal_wall.obj");
			decalWall.enableAlpha(true);
			
			decalBoard = loadModel("decal_board.obj");
			decalBoard.enableAlpha(true);
			
			decalCeilingMajor = loadModel("decal_ceiling_major.obj");
			decalCeilingMajor.enableAlpha(true);
	
			decalCeilingMinor = loadModel("decal_ceiling_minor.obj");
			decalCeilingMinor.enableAlpha(true);	
			
			decalNumber = loadModel("decal_number.obj");
			decalNumber.enableAlpha(true);
		}

		boundaryStage1 = loadBoundaries("collision_stage1.boundary");
		boundaryStage2 = loadBoundaries("collision_stage2.boundary");				
		
		updateBoundaries();
		
	}
	
	public void updateBoundaries()
	{
		if(Global.CURRENT_DAY < 3)
		{
			Walls = boundaryStage1;
		}
		else
		{
			Walls = boundaryStage2;
		}
	}

	private class OBJFace
	{
		int v1,vt1,vn1;
		int v2,vt2,vn2;
		int v3,vt3,vn3;
	}
	
	private void addModelGroup(RModel model, ArrayList<RMath.V3> vertices,
			ArrayList<RMath.V3> normals, ArrayList<RMath.V2> textures,
			ArrayList<OBJFace> faces, String textureName)
	{
		FloatBuffer vertexBuffer;
		FloatBuffer normalBuffer;
		FloatBuffer texBuffer;
		int numTriangles;
		
        // #faces * 3(3 verts in a tri) * 3(x,y,z per vert) * 4 (4 bytes per float)    	
		vertexBuffer = ByteBuffer.allocateDirect(faces.size() * 3 * 3 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		normalBuffer = ByteBuffer.allocateDirect(faces.size() * 3 * 3 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer = ByteBuffer.allocateDirect(faces.size() * 3 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		for(int i=0; i<faces.size(); ++i)
		{
			OBJFace face = faces.get(i);
			
			RMath.V3 v1 = vertices.get(face.v1-1);
			RMath.V3 v2 = vertices.get(face.v2-1);
			RMath.V3 v3 = vertices.get(face.v3-1);
			RMath.V3 vn1 = normals.get(face.vn1-1);
			RMath.V3 vn2 = normals.get(face.vn2-1);
			RMath.V3 vn3 = normals.get(face.vn3-1);
			RMath.V2 vt1 = textures.get(face.vt1-1);
			RMath.V2 vt2 = textures.get(face.vt2-1);
			RMath.V2 vt3 = textures.get(face.vt3-1);
			
			vertexBuffer.put(v1.x);
			vertexBuffer.put(v1.y);
			vertexBuffer.put(v1.z);
			vertexBuffer.put(v2.x);
			vertexBuffer.put(v2.y);
			vertexBuffer.put(v2.z);
			vertexBuffer.put(v3.x);
			vertexBuffer.put(v3.y);
			vertexBuffer.put(v3.z);		
			
			normalBuffer.put(vn1.x);
			normalBuffer.put(vn1.y);
			normalBuffer.put(vn1.z);
			normalBuffer.put(vn2.x);
			normalBuffer.put(vn2.y);
			normalBuffer.put(vn2.z);
			normalBuffer.put(vn3.x);
			normalBuffer.put(vn3.y);
			normalBuffer.put(vn3.z);		
			
			texBuffer.put(vt1.x);
			texBuffer.put(vt1.y);
			texBuffer.put(vt2.x);
			texBuffer.put(vt2.y);
			texBuffer.put(vt3.x);
			texBuffer.put(vt3.y);
		}
				
		vertexBuffer.position(0);
		normalBuffer.position(0);
		texBuffer.position(0);
		
		numTriangles = faces.size();
		
		model.vertexBuffer.add(vertexBuffer);
		model.normalBuffer.add(normalBuffer);
		model.texBuffer.add(texBuffer);
		model.numTriangles.add(numTriangles);
		model.textureID.add(textureName);
		
		model.numGroups++;
	}
	
	//loads OBJ models
	private RModel loadModel(String assetName)
	{
		RModel model = new RModel();
		
		ArrayList<RMath.V3> vertices = new ArrayList<RMath.V3>();
		ArrayList<RMath.V3> normals = new ArrayList<RMath.V3>();
		ArrayList<RMath.V2> textures = new ArrayList<RMath.V2>();
		ArrayList<OBJFace> faces = new ArrayList<OBJFace>();
		String textureName = null;
		
		boolean groupStart = false;
				
		AssetManager assetManager = Global.mainActivity.getAssets();
		
		try
		{
			InputStream is = assetManager.open(assetName);			
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			while(true)
			{
				String line = in.readLine();
				if(line==null) break;
				
				StringTokenizer st = new StringTokenizer(line, " /\t");
				if(st.countTokens()==0) continue;
				
				String type = st.nextToken();
					
				if(type.equals("usemtl"))
				{
					textureName = st.nextToken();
				}
				else if(type.equals("f"))
				{
					OBJFace f = new OBJFace();
					f.v1 = Integer.parseInt(st.nextToken());
					f.vt1 = Integer.parseInt(st.nextToken());
					f.vn1 = Integer.parseInt(st.nextToken());
					f.v2 = Integer.parseInt(st.nextToken());
					f.vt2 = Integer.parseInt(st.nextToken());
					f.vn2 = Integer.parseInt(st.nextToken());
					f.v3 = Integer.parseInt(st.nextToken());
					f.vt3 = Integer.parseInt(st.nextToken());
					f.vn3 = Integer.parseInt(st.nextToken());
					faces.add(f);
				}

				else if(groupStart && type.equals("g"))
				{
					//String groupName = st.nextToken();
					
					//end the group if it has started
					addModelGroup(model,vertices,normals,textures,faces,textureName);
					faces.clear();
					textureName = null;
				}
				
				else if(type.equals("g"))
				{
					//String groupName = st.nextToken();
					groupStart = true;
				}				
				
				else if(type.equals("v"))
				{
					RMath.V3 p = new RMath.V3(
							Float.parseFloat(st.nextToken()),
							Float.parseFloat(st.nextToken()),
							Float.parseFloat(st.nextToken()));
					vertices.add(p);
				}
				else if(type.equals("vn"))
				{
					RMath.V3 p = new RMath.V3(
							Float.parseFloat(st.nextToken()),
							Float.parseFloat(st.nextToken()),
							Float.parseFloat(st.nextToken()));
					normals.add(p);
				}		
				else if(type.equals("vt"))
				{
					RMath.V2 p = new RMath.V2(
							Float.parseFloat(st.nextToken()),
							Float.parseFloat(st.nextToken()));
					textures.add(p);
				}				
				
			}
			
			in.close();
			is.close();
			
			//if there is a group remaining, add it to the model:
			if(groupStart)
			{
				//end the group if it has started
				groupStart = false;
				addModelGroup(model,vertices,normals,textures,faces,textureName);
				faces.clear();
				textureName = null;				
			}			
		}
		catch(Exception e)
		{
			Log.d("Model Loader", assetName+" failed to load!");
		}
	
		return model;
	}

	/* loads Boundaries from *.boundary file.
	 * Minimal error checking, make sure items in boundary file is valid.
     */
	private ArrayList<RMath.Line> loadBoundaries(String assetName)
	{
		ArrayList<RMath.Line> boundary = new ArrayList<RMath.Line>();
		
		AssetManager assetManager = Global.mainActivity.getAssets();
        try
        {
        	InputStream is = assetManager.open(assetName);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            while(true)
            {
            	String line = in.readLine();
                if(line==null) break;

                StringTokenizer st = new StringTokenizer(line, " /\t");
                if(st.countTokens()==0) continue;

                RMath.Line l = new RMath.Line(
                		new RMath.V2(Float.parseFloat(st.nextToken()),Float.parseFloat(st.nextToken())),
                        new RMath.V2(Float.parseFloat(st.nextToken()), Float.parseFloat(st.nextToken())));
                boundary.add(l);
            }
            in.close();
        	is.close();
        }
        catch(Exception e)
        {
        	Log.e(TAG, "Boudary Loader: " + assetName + " failed to load!");
        }
        
        return boundary;
	}
	
	private static RModelLoader instance;
}
