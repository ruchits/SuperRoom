
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Main
{
	String infile = "C:\\workspace\\TheRoom\\assets\\poi\\points_of_interest.obj";
	String outfile = "C:\\workspace\\TheRoom\\assets\\poi\\points_of_interest.poi";
	
	
	//if this is true, then we expect the obj file
	// to be a file ONLY CONTAINING orthogonal BOXES
	public static boolean createPointOfInterestFile = true;
	
	public ArrayList<RMath.Line> boundaryList = new ArrayList<RMath.Line>();
	public ArrayList<String> groupNames = new ArrayList<String>();
	
	float threshold = 1.0f;
	
	public static void main(String args[])
	{
		new Main();
	}
	
	public Main()
	{
		loadModel(infile);
		new BoundaryDraw(boundaryList);
		
		if(createPointOfInterestFile)
			outputPOIFile(outfile);
		else
			outputBoundaryFile(outfile);
		
	}
	
	private void outputPOIFile(String filename)
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			
			for(int i=0,j=0; i<groupNames.size() ; i++,j+=2)
			{
				RMath.Line line1 = boundaryList.get(j);
				RMath.Line line2 = boundaryList.get(j+1);
				String groupName = groupNames.get(i);
				
				out.println(groupName+" "
						+line1.x1+" "+line1.y1+" "+
						+line1.x2+" "+line1.y2+" "+
						+line2.x2+" "+line2.y2);
			}
			
			out.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void outputBoundaryFile(String filename)
	{
		try
		{
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			
			for(int i=0; i<boundaryList.size() ; ++i)
			{
				RMath.Line line = boundaryList.get(i);
				out.println(line.x1+" "+line.y1+" "+line.x2+" "+line.y2);
			}
			
			out.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private class OBJFace
	{
		int v1,vt1,vn1;
		int v2,vt2,vn2;
		int v3,vt3,vn3;
	}
	
	private boolean addLineIfValid(RMath.V3 p1, RMath.V3 p2)
	{
		if(-threshold <= p1.y && p1.y <= threshold
				&&-threshold <= p2.y && p2.y <= threshold)
		{
			RMath.Line line = new RMath.Line();
			line.x1 = p1.x;
			line.y1 = p1.z;
			line.x2 = p2.x;
			line.y2 = p2.z;
			
			line.z1 = p1.y;
			line.z2 = p2.y;
			line.length = line.calcLength();
			boundaryList.add(line);
			return true;
		}
		return false;
	}
	
	private void addModelGroup(ArrayList<RMath.V3> vertices, ArrayList<OBJFace> faces, String groupName)
	{
		groupNames.add(groupName);
		
		for(int i=0; i<faces.size(); ++i)
		{
			OBJFace face = faces.get(i);
			
			RMath.V3 v1 = vertices.get(face.v1-1);
			RMath.V3 v2 = vertices.get(face.v2-1);
			RMath.V3 v3 = vertices.get(face.v3-1);
														
			if(createPointOfInterestFile)
			{
				boolean l1 = addLineIfValid(v2,v3);
				boolean l2 = addLineIfValid(v2,v1);
				break;
			}
			else
			{
				addLineIfValid(v1,v2);
				addLineIfValid(v2,v3);				
				addLineIfValid(v3,v1);	
			}

		}
		
		
	}
	
	//loads OBJ models
	private void loadModel(String assetName)
	{
		ArrayList<RMath.V3> vertices = new ArrayList<RMath.V3>();
		ArrayList<RMath.V3> normals = new ArrayList<RMath.V3>();
		ArrayList<RMath.V2> textures = new ArrayList<RMath.V2>();
		ArrayList<OBJFace> faces = new ArrayList<OBJFace>();
		String textureName = null;
		
		boolean groupStart = false;
				
		//AssetManager assetManager = Global.mainActivity.getAssets();
		
		try
		{		
			BufferedReader in = new BufferedReader(new FileReader(assetName));
			
			String groupName = "";
			
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
					//end the group if it has started
					addModelGroup(vertices,faces,groupName);
					faces.clear();
					textureName = null;
					groupName = st.nextToken();
				}
				
				else if(type.equals("g"))
				{
					groupName = st.nextToken();
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
			//is.close();
			
			//if there is a group remaining, add it to the model:
			if(groupStart)
			{
				//end the group if it has started
				groupStart = false;
				addModelGroup(vertices,faces,groupName);
				faces.clear();
				textureName = null;				
			}			
		}
		catch(Exception e)
		{
			System.out.println("Model Loader"+ assetName+" failed to load!");
		}
	
	}	
}
