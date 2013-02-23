import java.awt.*;
import java.util.*;

import javax.swing.*;


public class BoundaryDraw extends JFrame
{
	float width = 1000;
	float height = 1000;
	float maxsize = 70;
	ArrayList<RMath.Line> boundaryList;
	
	public BoundaryDraw(ArrayList<RMath.Line> boundaryList)
	{
		this.boundaryList = boundaryList;
		setSize((int)width,(int)height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void paint(Graphics g)
	{
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,(int)width,(int)height);
		
		for(int i=0; i<boundaryList.size();++i)
		{
			RMath.Line line = boundaryList.get(i);
			float x1 = ((line.x1/maxsize) * (width/2))+(width/2);
			float x2 = ((line.x2/maxsize) * (width/2))+(width/2);
			float y1 = ((line.y1/maxsize) * (height/2))+(height/2);
			float y2 = ((line.y2/maxsize) * (height/2))+(height/2);
			float mx = (x1+x2)/2;
			float my = (y1+y2)/2;
			
			g.setColor(new Color(100,100,100));
			g.drawLine((int)x1, (int)y1, (int)mx, (int)my);
			
			g.setColor(new Color(255,255,255));
			g.drawLine((int)mx, (int)my, (int)x2, (int)y2);
		}
	}

}
