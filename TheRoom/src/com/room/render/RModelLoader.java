package com.room.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.room.Global;

import android.content.res.AssetManager;
import android.util.Log;


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
	public RModel modelProps;
	public RModel modelPropsDeadMan;
	public RModel modelPropsDeadWoman;
	public RModel modelPropsDeadManHair;
	public RModel modelPropsDeadWomanHair;
	public RModel modelPropsStatuesActive;
	public RModel modelPropsStatuesNeutral;
	public RModel modelDoorBathroomStage1;
	public RModel modelDoorBathroomStage2;
	public RModel modelPropClothCovered;
	public RModel decalWall;
	public RModel decalBoard;
	public RModel decalCeilingMinor;
	public RModel decalCeilingMajor;
	public RModel decalNumber;
	public RModel decalPuzzleFlood;
	public RModel modelPOI;
	
	public static ArrayList<RMath.Line> activeWallBoundary;
	public static ArrayList<RMath.Line> activePropBoundary;
	
	private ArrayList<RMath.Line> boundaryStage1;
	private ArrayList<RMath.Line> boundaryStage2;
	private ArrayList<RMath.Line> boundaryProps;

	public void init()
	{
		
		
		if(!Global.DEBUG_NO_PROPS)	
		{
			modelProps = loadBinaryModel("model_props.bin");
			modelPropsDeadMan = loadBinaryModel("model_props_deadman.bin");
			modelPropsDeadWoman = loadBinaryModel("model_props_deadwoman.bin");
			modelPropsStatuesActive = loadBinaryModel("model_props_statues_active.bin");
			modelPropsStatuesNeutral = loadBinaryModel("model_props_statues_neutral.bin");
			modelPropClothCovered = loadBinaryModel("model_prop_cloth_covered.bin");
			modelPropsDeadManHair = loadBinaryModel("model_props_deadman_hair.bin");
			modelPropsDeadManHair.enableCull(false);			
			modelPropsDeadWomanHair = loadBinaryModel("model_props_deadwoman_hair.bin");
			modelPropsDeadWomanHair.enableCull(false);			
		}
		
		modelRoom = loadBinaryModel("model_room.bin");
		modelDoorBathroomStage1 = loadBinaryModel("door_bathroom_stage1.bin");
		modelDoorBathroomStage2 = loadBinaryModel("door_bathroom_stage2.bin");
		
		if(Global.DEBUG_SHOW_POI_BOXES)
			modelPOI = loadBinaryModel("points_of_interest.bin");
		
		if(!Global.DEBUG_NO_DECALS)
		{
			decalWall = loadBinaryModel("decal_wall.bin");
			decalWall.enableAlpha(true);
			
			decalBoard = loadBinaryModel("decal_board.bin");
			decalBoard.enableAlpha(true);
			
			decalCeilingMajor = loadBinaryModel("decal_ceiling_major.bin");
			decalCeilingMajor.enableAlpha(true);
	
			decalCeilingMinor = loadBinaryModel("decal_ceiling_minor.bin");
			decalCeilingMinor.enableAlpha(true);	
			
			decalNumber = loadBinaryModel("decal_number.bin");
			decalNumber.enableAlpha(true);
			
			decalPuzzleFlood = loadBinaryModel("decal_puzzle_flood.bin");
			decalPuzzleFlood.enableAlpha(true);
			decalPuzzleFlood.enableUnlit(true);
		}

		boundaryStage1 = loadBoundaries("collision_stage1.boundary");
		boundaryStage2 = loadBoundaries("collision_stage2.boundary");				
		boundaryProps = loadBoundaries("collision_props.boundary");
		
		updateBoundaries();
		
	}
	
	public void updateBoundaries()
	{
		if(Global.getCurrentDay() < 3)
		{
			activeWallBoundary = boundaryStage1;
		}
		else
		{
			activeWallBoundary = boundaryStage2;
		}
		
		activePropBoundary = boundaryProps;
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
	
	private void addModelGroup(RModel model, FloatBuffer vertices,
			FloatBuffer normals, FloatBuffer textures,
			ShortBuffer faces, String textureName)
	{
		faces.position(0);		
		
		FloatBuffer vertexBuffer;
		FloatBuffer normalBuffer;
		FloatBuffer texBuffer;
		int numTriangles = faces.capacity()/9; //9 shorts per face				
		
        // #faces * 3(3 verts in a tri) * 3(x,y,z per vert) * 4 (4 bytes per float)    	
		vertexBuffer = ByteBuffer.allocateDirect(numTriangles * 3 * 3 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		normalBuffer = ByteBuffer.allocateDirect(numTriangles * 3 * 3 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		texBuffer = ByteBuffer.allocateDirect(numTriangles * 3 * 2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		
		for(int i=0; i<numTriangles; ++i)
		{
			OBJFace face = new OBJFace();
			face.v1 = faces.get();
			face.v2 = faces.get();
			face.v3 = faces.get();
			face.vn1 = faces.get();
			face.vn2 = faces.get();
			face.vn3 = faces.get();
			face.vt1 = faces.get();
			face.vt2 = faces.get();
			face.vt3 = faces.get();			
			
			vertices.position((face.v1-1)*3); //3 floats per vertex
			vertexBuffer.put(vertices.get());
			vertexBuffer.put(vertices.get());
			vertexBuffer.put(vertices.get());			
			
			vertices.position((face.v2-1)*3); //3 floats per vertex
			vertexBuffer.put(vertices.get());
			vertexBuffer.put(vertices.get());
			vertexBuffer.put(vertices.get());	
			
			vertices.position((face.v3-1)*3); //3 floats per vertex
			vertexBuffer.put(vertices.get());
			vertexBuffer.put(vertices.get());
			vertexBuffer.put(vertices.get());				
			
			normals.position((face.vn1-1)*3); //3 floats per vertex
			normalBuffer.put(normals.get());
			normalBuffer.put(normals.get());
			normalBuffer.put(normals.get());				
			
			normals.position((face.vn2-1)*3); //3 floats per vertex
			normalBuffer.put(normals.get());
			normalBuffer.put(normals.get());
			normalBuffer.put(normals.get());			
			
			normals.position((face.vn3-1)*3); //3 floats per vertex
			normalBuffer.put(normals.get());
			normalBuffer.put(normals.get());
			normalBuffer.put(normals.get());			
			
			textures.position((face.vt1-1)*2); //2 floats per vertex
			texBuffer.put(textures.get());
			texBuffer.put(textures.get());			
			
			textures.position((face.vt2-1)*2); //2 floats per vertex
			texBuffer.put(textures.get());
			texBuffer.put(textures.get());				
			
			textures.position((face.vt3-1)*2); //2 floats per vertex
			texBuffer.put(textures.get());
			texBuffer.put(textures.get());				
		}
				
		vertexBuffer.position(0);
		normalBuffer.position(0);
		texBuffer.position(0);
		
		model.vertexBuffer.add(vertexBuffer);
		model.normalBuffer.add(normalBuffer);
		model.texBuffer.add(texBuffer);
		model.numTriangles.add(numTriangles);
		model.textureID.add(textureName);
		
		model.numGroups++;
	}
	
	private RModel loadBinaryModel(String assetName)
	{
		RModel model = new RModel();
		AssetManager assetManager = Global.mainActivity.getAssets();
		int FILE_HEADER_SIZE = 6;
		int GROUP_HEADER_SIZE = 6;
		
		try
		{
			InputStream is = assetManager.open("geometry/"+assetName);
			
			byte[] header = new byte[FILE_HEADER_SIZE];
			is.read(header, 0, FILE_HEADER_SIZE);			
			
			ByteBuffer headerByteBuffer = ByteBuffer.wrap(header);
			headerByteBuffer.order(ByteOrder.BIG_ENDIAN);
			ShortBuffer headerBuffer = headerByteBuffer.asShortBuffer();

			short numVertices = headerBuffer.get();
			short numNormals = headerBuffer.get();
			short numTextures = headerBuffer.get();
			
			byte[] vertices = new byte[numVertices*4*3];
			is.read(vertices,0,numVertices*4*3);
			
			ByteBuffer verticesByteBuffer = ByteBuffer.wrap(vertices);
			verticesByteBuffer.order(ByteOrder.BIG_ENDIAN);
			FloatBuffer verticesBuffer = verticesByteBuffer.asFloatBuffer();
			
			byte[] normals = new byte[numNormals*4*3];
			is.read(normals,0,numNormals*4*3);
			
			ByteBuffer normalsByteBuffer = ByteBuffer.wrap(normals);
			normalsByteBuffer.order(ByteOrder.BIG_ENDIAN);
			FloatBuffer normalsBuffer = normalsByteBuffer.asFloatBuffer();
			
			byte[] textures = new byte[numTextures*4*2];
			is.read(textures,0,numTextures*4*2);
			
			ByteBuffer texturesByteBuffer = ByteBuffer.wrap(textures);
			texturesByteBuffer.order(ByteOrder.BIG_ENDIAN);
			FloatBuffer texturesBuffer = texturesByteBuffer.asFloatBuffer();			
			
			
			while(true)
			{
				byte[] groupHeader = new byte[GROUP_HEADER_SIZE];
				int status = is.read(groupHeader, 0, GROUP_HEADER_SIZE);
				if(status == -1) break;
				
				ByteBuffer groupHeaderByteBuffer = ByteBuffer.wrap(groupHeader);
				groupHeaderByteBuffer.order(ByteOrder.BIG_ENDIAN);
				ShortBuffer groupHeaderBuffer = groupHeaderByteBuffer.asShortBuffer();

				short groupNameLength = groupHeaderBuffer.get();
				short groupMtlLength = groupHeaderBuffer.get();
				short numFaces = groupHeaderBuffer.get();
				
				byte[] groupNameBytes = new byte[groupNameLength];
				is.read(groupNameBytes,0,groupNameLength);
				String groupName = new String(groupNameBytes);
				
				byte[] groupMtlBytes = new byte[groupMtlLength];
				is.read(groupMtlBytes,0,groupMtlLength);
				String groupMtl = new String(groupMtlBytes);						
				
				byte[] faces = new byte[numFaces*2*9];
				is.read(faces, 0, numFaces*2*9);			
				
				ByteBuffer facesByteBuffer = ByteBuffer.wrap(faces);
				facesByteBuffer.order(ByteOrder.BIG_ENDIAN);
				ShortBuffer facesBuffer = facesByteBuffer.asShortBuffer();
				
				addModelGroup(model,verticesBuffer,normalsBuffer,texturesBuffer,facesBuffer,groupMtl);
			}

			is.close();
		}
		catch(Exception e)
		{
			Log.d("Model Loader", assetName+" failed to load!");
		}
		
		return model;
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
			InputStream is = assetManager.open("geometry/"+assetName);			
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
        	InputStream is = assetManager.open("collision/"+assetName);
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
