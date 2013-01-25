import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class FileIO
{
	public static String readFileAsString(String filePath)
	{
		try
		{
			byte[] buffer = new byte[(int) new File(filePath).length()];
			BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));
			f.read(buffer);
			return new String(buffer);
		}
		catch(Exception e)
		{
			System.out.println("FileIO error - "+filePath);
			return null;
		}

	}

	public static BufferedImage readImage(String filePath)
	{
		try
		{
			return ImageIO.read(new File(filePath));
		}
		catch(Exception e)
		{
			System.out.println("FileIO error - "+filePath);
			return null;
		}
	}
}