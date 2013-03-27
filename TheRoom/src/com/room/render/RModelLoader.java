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
	public RModel decalDay1;
	public RModel decalPuzzleFlood;
	public RModel decalDeadMan;
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
			
			decalDay1 = loadBinaryModel("decal_day1.bin");
			decalDay1.enableAlpha(true);
			
			decalPuzzleFlood = loadBinaryModel("decal_puzzle_flood.bin");
			decalPuzzleFlood.enableAlpha(true);
			decalPuzzleFlood.enableUnlit(true);
			
			decalDeadMan = loadBinaryModel("decal_deadman.bin");
			decalDeadMan.enableAlpha(true);			
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


	private void addModelGroup(RModel model, 
			FloatBuffer vertices, FloatBuffer normals, FloatBuffer textures,
			ShortBuffer indices, String textureName, String groupName)
	{
		indices.position(0);				
		int numTriangles = indices.capacity()/3; //3 shorts per face				
		
		vertices.position(0);
		normals.position(0);
		textures.position(0);
		
		model.vertexBuffer.add(vertices);
		model.normalBuffer.add(normals);
		model.texBuffer.add(textures);
		model.indexBuffer.add(indices);
		model.numTriangles.add(numTriangles);
		model.textureID.add(textureName);
		
		model.numGroups++;
	}
	
	private RModel loadBinaryModel(String assetName)
	{
		RModel model = new RModel();
		AssetManager assetManager = Global.mainActivity.getAssets();
		//int FILE_HEADER_SIZE = 6;
		int GROUP_HEADER_SIZE = 8;
		
		try
		{
			InputStream is = assetManager.open("geometry/"+assetName);
	
			while(true)
			{
				byte[] groupHeader = new byte[GROUP_HEADER_SIZE];
				int status = is.read(groupHeader, 0, GROUP_HEADER_SIZE);
				if(status == -1) break;
				
				ByteBuffer groupHeaderByteBuffer = ByteBuffer.wrap(groupHeader);
				groupHeaderByteBuffer.order(ByteOrder.nativeOrder());
				ShortBuffer groupHeaderBuffer = groupHeaderByteBuffer.asShortBuffer();

				short groupNameLength = groupHeaderBuffer.get();
				short groupMtlLength = groupHeaderBuffer.get();
				short numFaces = groupHeaderBuffer.get();
				short numVerts = groupHeaderBuffer.get();
				
				byte[] groupNameBytes = new byte[groupNameLength];
				is.read(groupNameBytes,0,groupNameLength);
				String groupName = new String(groupNameBytes);
				
				byte[] groupMtlBytes = new byte[groupMtlLength];
				is.read(groupMtlBytes,0,groupMtlLength);
				String groupMtl = new String(groupMtlBytes);						
				
				byte[] indices = new byte[numFaces*2*3];
				is.read(indices, 0, numFaces*2*3);			
				
				ByteBuffer indicesByteBuffer = ByteBuffer.wrap(indices);
				indicesByteBuffer.order(ByteOrder.nativeOrder());
				ShortBuffer indicesBuffer = indicesByteBuffer.asShortBuffer();
				
				byte[] vertices = new byte[numVerts*4*3];
				is.read(vertices, 0, numVerts*4*3);			
				
				ByteBuffer verticesByteBuffer = ByteBuffer.wrap(vertices);
				verticesByteBuffer.order(ByteOrder.nativeOrder());
				FloatBuffer verticesBuffer = verticesByteBuffer.asFloatBuffer();	
				
				byte[] normals = new byte[numVerts*4*3];
				is.read(normals, 0, numVerts*4*3);			
				
				ByteBuffer normalsByteBuffer = ByteBuffer.wrap(normals);
				normalsByteBuffer.order(ByteOrder.nativeOrder());
				FloatBuffer normalsBuffer = normalsByteBuffer.asFloatBuffer();		
				
				byte[] textures = new byte[numVerts*4*2];
				is.read(textures, 0, numVerts*4*2);			
				
				ByteBuffer texturesByteBuffer = ByteBuffer.wrap(textures);
				texturesByteBuffer.order(ByteOrder.nativeOrder());
				FloatBuffer texturesBuffer = texturesByteBuffer.asFloatBuffer();
				
				ShortBuffer directIndicesBuffer =  ByteBuffer.allocateDirect(numFaces*2*3).order(ByteOrder.nativeOrder()).asShortBuffer();
				directIndicesBuffer.put(indicesBuffer);
				FloatBuffer directVerticesBuffer = ByteBuffer.allocateDirect(numVerts*4*3).order(ByteOrder.nativeOrder()).asFloatBuffer();
				directVerticesBuffer.put(verticesBuffer);
				FloatBuffer directNormalsBuffer = ByteBuffer.allocateDirect(numVerts*4*3).order(ByteOrder.nativeOrder()).asFloatBuffer();
				directNormalsBuffer.put(normalsBuffer);
				FloatBuffer directTexturesBuffer = ByteBuffer.allocateDirect(numVerts*4*2).order(ByteOrder.nativeOrder()).asFloatBuffer();
				directTexturesBuffer.put(texturesBuffer);

				addModelGroup(model,directVerticesBuffer,directNormalsBuffer,directTexturesBuffer,directIndicesBuffer,groupMtl,groupName);
			}

			is.close();
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
