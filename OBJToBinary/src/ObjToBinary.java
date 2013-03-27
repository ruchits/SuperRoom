import java.io.BufferedReader;
//import java.io.DataOutputStream;
import com.google.common.io.LittleEndianDataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;



public class ObjToBinary
{
	String infile = "C:\\workspace\\TheRoom\\assets\\geometry\\decal_day1.obj";
	String outfile = "C:\\workspace\\TheRoom\\assets\\geometry\\decal_day1.bin";
	
	String inpath = "C:\\repo\\obj\\";
	String outpath = "C:\\workspace\\TheRoom\\assets\\geometry\\";
	
	public static void main(String args[])
	{
		new ObjToBinary();
	}
	
	
	ObjToBinary()
	{
		loadModel(inpath+"decal_board.obj",outpath+"decal_board.bin");
		loadModel(inpath+"decal_ceiling_major.obj",outpath+"decal_ceiling_major.bin");
		loadModel(inpath+"decal_ceiling_minor.obj",outpath+"decal_ceiling_minor.bin");
		loadModel(inpath+"decal_day1.obj",outpath+"decal_day1.bin");
		loadModel(inpath+"decal_deadman.obj",outpath+"decal_deadman.bin");
		loadModel(inpath+"decal_puzzle_flood.obj",outpath+"decal_puzzle_flood.bin");
		loadModel(inpath+"decal_wall.obj",outpath+"decal_wall.bin");
		loadModel(inpath+"door_bathroom_stage1.obj",outpath+"door_bathroom_stage1.bin");
		loadModel(inpath+"door_bathroom_stage2.obj",outpath+"door_bathroom_stage2.bin");
		loadModel(inpath+"model_prop_cloth_covered.obj",outpath+"model_prop_cloth_covered.bin");
		loadModel(inpath+"model_props.obj",outpath+"model_props.bin");
		loadModel(inpath+"model_props_deadman.obj",outpath+"model_props_deadman.bin");
		loadModel(inpath+"model_props_deadman_hair.obj",outpath+"model_props_deadman_hair.bin");
		loadModel(inpath+"model_props_deadwoman.obj",outpath+"model_props_deadwoman.bin");
		loadModel(inpath+"model_props_deadwoman_hair.obj",outpath+"model_props_deadwoman_hair.bin");
		loadModel(inpath+"model_props_statues_active.obj",outpath+"model_props_statues_active.bin");
		loadModel(inpath+"model_props_statues_neutral.obj",outpath+"model_props_statues_neutral.bin");
		loadModel(inpath+"model_room.obj",outpath+"model_room.bin");
		
		//loadModel(path+"model_props.obj",path+"model_props.bin");
		//loadModel(path+"model_props_deadwoman.obj",path+"model_props_deadwoman.bin");
		//loadModel(path+"box.obj",path+"box.bin");
		//loadModel(infile,outfile);
	}
	
	private class OBJFace
	{
		short ov1, ov2, ov3;
	}	
	
	private class OBJVertex
	{
		short vIndex;
		short vtIndex;
		short vnIndex;
		short mapIndex;
	}

	//loads OBJ models
	private void loadModel(String assetName, String outputName)
	{
		ArrayList<RMath.V3> vertices = new ArrayList<RMath.V3>();
		ArrayList<RMath.V3> normals = new ArrayList<RMath.V3>();
		ArrayList<RMath.V2> textures = new ArrayList<RMath.V2>();

		
		try
		{		
			//============================
			//read the v, vn, vt tables
			//============================
			
			BufferedReader in = new BufferedReader(new FileReader(assetName));
			
			while(true)
			{
				String line = in.readLine();
				if(line==null) break;
				
				StringTokenizer st = new StringTokenizer(line, " \t");
				if(st.countTokens()==0) continue;
				
				String type = st.nextToken();				
				
				if(type.equals("v"))
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
				/*else if(type.equals("f"))
				{
					for(int i=0; i<3; ++i)
					{
						String vertexString = st.nextToken();
						if(!uniqueVertices.containsKey(vertexString))
						{
							StringTokenizer st2 = new StringTokenizer(vertexString,"/\t");
							OBJVertex objVertex = new OBJVertex();
							objVertex.vIndex = Short.parseShort(st2.nextToken());
							objVertex.vtIndex = Short.parseShort(st2.nextToken());
							objVertex.vnIndex = Short.parseShort(st2.nextToken());
							uniqueVertices.put(vertexString, objVertex);
						}
					}
				}*/
				
			}
			
			in.close();		
			
			//============================			
			//write the v, vn, vt tables
			//============================			

			LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(new FileOutputStream(outputName));

			/*out.writeShort(vertices.size());
			out.writeShort(normals.size());
			out.writeShort(textures.size());
			
			for(RMath.V3 v:vertices)				
			{
				out.writeFloat(v.x);
				out.writeFloat(v.y);
				out.writeFloat(v.z);
			}
			
			for(RMath.V3 n:normals)				
			{
				out.writeFloat(n.x);
				out.writeFloat(n.y);
				out.writeFloat(n.z);
			}
			
			for(RMath.V2 t:textures)				
			{
				out.writeFloat(t.x);
				out.writeFloat(t.y);
			}	*/		
			
			

			
			//============================
			//read g tables and mtl references
			//============================
			
			in = new BufferedReader(new FileReader(assetName));
			
			String groupName = null;
			String groupMtl = null;
			ArrayList<OBJFace> faces = null;			
			HashMap<String,OBJVertex> uniqueVertices = null;
			
			
			while(true)
			{
				String line = in.readLine();
				if(line==null) break;
				
				StringTokenizer st = new StringTokenizer(line, " \t");
				if(st.countTokens()==0) continue;
				
				String type = st.nextToken();				
				
				if(type.equals("g"))
				{
					if(faces!=null)
					{
						//add current group
						addModelGroup(groupName,groupMtl,faces,uniqueVertices,
								vertices, normals, textures, out);
					}
					
					faces = new ArrayList<OBJFace>();
					uniqueVertices = new HashMap<String,OBJVertex>();
					groupName = st.nextToken();
				}
				else if(type.equals("usemtl"))
				{
					groupMtl = st.nextToken();
				}
				else if(type.equals("f"))
				{
					OBJFace face = new OBJFace();
					
					for(int i=0; i<3; ++i)
					{
						String vertexString = st.nextToken();
						if(!uniqueVertices.containsKey(vertexString))
						{
							StringTokenizer st2 = new StringTokenizer(vertexString,"/\t");
							OBJVertex objVertex = new OBJVertex();
							objVertex.vIndex = (short)(Short.parseShort(st2.nextToken())-1);
							objVertex.vtIndex = (short)(Short.parseShort(st2.nextToken())-1);
							objVertex.vnIndex = (short)(Short.parseShort(st2.nextToken())-1);
							objVertex.mapIndex = (short)(uniqueVertices.size());
							uniqueVertices.put(vertexString, objVertex);
						}
						
						if(i==0)
							face.ov1 = uniqueVertices.get(vertexString).mapIndex;
						else if(i==1)
							face.ov2 = uniqueVertices.get(vertexString).mapIndex;
						else if(i==2)
							face.ov3 = uniqueVertices.get(vertexString).mapIndex;						
						
					}
					
					faces.add(face);
				}				
			}
						
			if(faces!=null)
			{
				//add remaining group
				addModelGroup(groupName,groupMtl,faces,uniqueVertices,
						vertices, normals, textures, out);
			}
			
			in.close();	
			
			
			out.close();
			
		}
		catch(Exception e)
		{
			System.out.println("Model Loader"+ assetName+" failed to load!");
		}
		
	}
	
	private void addModelGroup(String groupName, String groupMtl,
			ArrayList<OBJFace> faces, HashMap<String,OBJVertex> uniqueVertices,
			ArrayList<RMath.V3> vertices, ArrayList<RMath.V3> normals, ArrayList<RMath.V2> textures,
			LittleEndianDataOutputStream out) throws Exception
	{
		//group header:
		//short - groupName.length()
		//short - groupMtl.length()
		//short - faces.size()
		//short - uniqueVerts.size()
		
		//group data:
		//groupName
		//groupMtl
		//faces (3 shorts per face)
		//uniqueVerts (v table)
		//uniqueVerts (vn table)
		//uniqueVerts (vt table)
		
		//write group header:
		out.writeShort(groupName.length());
		out.writeShort(groupMtl.length());
		out.writeShort(faces.size());
		out.writeShort(uniqueVertices.size());
		
		//write data:
		out.writeBytes(groupName);
		out.writeBytes(groupMtl);
		
		for(OBJFace f:faces)
		{
			out.writeShort(f.ov1);
			out.writeShort(f.ov2);
			out.writeShort(f.ov3);
		}
		
		Iterator<OBJVertex> iter = uniqueVertices.values().iterator();		
		float vTable[] = new float[uniqueVertices.size()*3];
		
		while(iter.hasNext())
		{
			OBJVertex objVertex = iter.next();
			RMath.V3 vertex = vertices.get(objVertex.vIndex);
			
			vTable[objVertex.mapIndex*3+0] = vertex.x;
			vTable[objVertex.mapIndex*3+1] = vertex.y;
			vTable[objVertex.mapIndex*3+2] = vertex.z;
		}
		
		iter = uniqueVertices.values().iterator();
		float vnTable[] = new float[uniqueVertices.size()*3];
		
		while(iter.hasNext())
		{
			OBJVertex objVertex = iter.next();
			RMath.V3 normal = normals.get(objVertex.vnIndex);
			
			vnTable[objVertex.mapIndex*3+0] = normal.x;
			vnTable[objVertex.mapIndex*3+1] = normal.y;
			vnTable[objVertex.mapIndex*3+2] = normal.z;			
		}	
		
		iter = uniqueVertices.values().iterator();
		float vtTable[] = new float[uniqueVertices.size()*2];
		
		while(iter.hasNext())
		{
			OBJVertex objVertex = iter.next();
			RMath.V2 texture = textures.get(objVertex.vtIndex);
			
			vtTable[objVertex.mapIndex*2+0] = texture.x;
			vtTable[objVertex.mapIndex*2+1] = texture.y;
		}			
		
		
		for(int i=0; i<vTable.length; ++i)
			out.writeFloat(vTable[i]);
		
		for(int i=0; i<vnTable.length; ++i)
			out.writeFloat(vnTable[i]);
		
		for(int i=0; i<vtTable.length; ++i)
			out.writeFloat(vtTable[i]);		
		
	}
}
