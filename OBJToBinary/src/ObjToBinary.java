import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ObjToBinary
{
	String infile = "C:\\workspace\\TheRoom\\assets\\geometry\\model_props.obj";
	String outfile = "C:\\workspace\\TheRoom\\assets\\geometry\\model_props.bin";
	
	String path = "C:\\workspace\\TheRoom\\assets\\geometry\\";
	
	public static void main(String args[])
	{
		new ObjToBinary();
	}
	
	
	ObjToBinary()
	{
		loadModel(path+"decal_board.obj",path+"decal_board.bin");
		loadModel(path+"decal_ceiling_major.obj",path+"decal_ceiling_major.bin");
		loadModel(path+"decal_ceiling_minor.obj",path+"decal_ceiling_minor.bin");
		loadModel(path+"decal_number.obj",path+"decal_number.bin");
		loadModel(path+"decal_puzzle_flood.obj",path+"decal_puzzle_flood.bin");
		loadModel(path+"decal_wall.obj",path+"decal_wall.bin");
		loadModel(path+"door_bathroom_stage1.obj",path+"door_bathroom_stage1.bin");
		loadModel(path+"door_bathroom_stage2.obj",path+"door_bathroom_stage2.bin");
		loadModel(path+"model_prop_cloth_covered.obj",path+"model_prop_cloth_covered.bin");
		loadModel(path+"model_props.obj",path+"model_props.bin");
		loadModel(path+"model_props_deadman.obj",path+"model_props_deadman.bin");
		loadModel(path+"model_props_deadman_hair.obj",path+"model_props_deadman_hair.bin");
		loadModel(path+"model_props_deadwoman.obj",path+"model_props_deadwoman.bin");
		loadModel(path+"model_props_deadwoman_hair.obj",path+"model_props_deadwoman_hair.bin");
		loadModel(path+"model_props_statues_active.obj",path+"model_props_statues_active.bin");
		loadModel(path+"model_props_statues_neutral.obj",path+"model_props_statues_neutral.bin");
		loadModel(path+"model_props_test.obj",path+"model_props_test.bin");
		loadModel(path+"model_room.obj",path+"model_room.bin");		
	}
	
	private class OBJFace
	{
		short v1,vt1,vn1;
		short v2,vt2,vn2;
		short v3,vt3,vn3;
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
				
				StringTokenizer st = new StringTokenizer(line, " /\t");
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
				
			}
			
			in.close();		
			
			//============================			
			//write the v, vn, vt tables
			//============================			

			DataOutputStream out = new DataOutputStream(new FileOutputStream(outputName));

			out.writeShort(vertices.size());
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
			}			
			
			

			
			//============================
			//read g tables and mtl references
			//============================
			
			in = new BufferedReader(new FileReader(assetName));
			
			String groupName = null;
			String groupMtl = null;
			ArrayList<OBJFace> faces = null;
			
			while(true)
			{
				String line = in.readLine();
				if(line==null) break;
				
				StringTokenizer st = new StringTokenizer(line, " /\t");
				if(st.countTokens()==0) continue;
				
				String type = st.nextToken();				
				
				if(type.equals("g"))
				{
					if(faces!=null)
					{
						//add current group
						addModelGroup(groupName,groupMtl,faces,out);
					}
					
					faces = new ArrayList<OBJFace>();
					groupName = st.nextToken();
				}
				else if(type.equals("usemtl"))
				{
					groupMtl = st.nextToken();
				}
				else if(type.equals("f"))
				{
					OBJFace f = new OBJFace();
					f.v1 = Short.parseShort(st.nextToken());
					f.vt1 = Short.parseShort(st.nextToken());
					f.vn1 = Short.parseShort(st.nextToken());
					f.v2 = Short.parseShort(st.nextToken());
					f.vt2 = Short.parseShort(st.nextToken());
					f.vn2 = Short.parseShort(st.nextToken());
					f.v3 = Short.parseShort(st.nextToken());
					f.vt3 = Short.parseShort(st.nextToken());
					f.vn3 = Short.parseShort(st.nextToken());
					faces.add(f);
				}				
			}
						
			if(faces!=null)
			{
				//add remaining group
				addModelGroup(groupName,groupMtl,faces,out);
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
			ArrayList<OBJFace> faces, DataOutputStream out) throws Exception
	{
		//group header:
		//short - groupName.length()
		//short - groupMtl.length()
		//short - faces.size()
		
		//group data:
		//groupName
		//groupMtl
		//faces
		
		//write group header:
		out.writeShort(groupName.length());
		out.writeShort(groupMtl.length());
		out.writeShort(faces.size());
		
		//write data:
		out.writeBytes(groupName);
		out.writeBytes(groupMtl);
		
		for(OBJFace f:faces)
		{
			out.writeShort(f.v1);
			out.writeShort(f.v2);
			out.writeShort(f.v3);
			out.writeShort(f.vn1);
			out.writeShort(f.vn2);
			out.writeShort(f.vn3);
			out.writeShort(f.vt1);
			out.writeShort(f.vt2);
			out.writeShort(f.vt3);
		}
		
		
	}
}
