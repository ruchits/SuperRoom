//FLOOD_BOX 0.20859376 0.7140625 0.054166667 0.9513889   //left,right,top,bottom
//bRed 0.73515624 0.8078125 0.05138889 0.18194444
//bPink 0.73515624 0.8070313 0.20555556 0.3375
//bBlue 0.7359375 0.8078125 0.3625 0.49305555
//bGreen 0.73515624 0.80859375 0.51805556 0.6458333
//bYellow 0.73515624 0.8078125 0.66805553 0.7986111
//bSky 0.734375 0.8078125 0.825 0.95555556

package com.room.puzzles;

import java.util.Random;
import java.util.StringTokenizer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import com.room.R;
import com.room.media.MSoundManager;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;

public class PFlood extends SSceneActivity
{
	private static final int tilesPerSide = 12;
	private static final char nonExistingColor = (char)-1;

	private RectF tileArea;
	private float tileWidth;
	private float tileHeight;
	private char[][] floodTiles;
	private Bitmap tileImages[];
	private char oldColor;

	private String puzzle =
	       "005245443521" +
	       "123322515505" +
	       "354014421032" +
	       "510255335453" +
	       "433434225410" +
	       "402303451312" +
	       "035543415524" +
	       "040433324435" +
	       "525225132502" +
	       "350002152350" +
	       "115024443434" +
	       "130024130000";

	private String puzzle2 =
	       "542332512255" +
	       "245334551352" +
	       "243551415440" +
	       "554110023335" +
	       "525534540514" +
	       "032311504503" +
	       "451523150151" +
	       "000010521305" +
	       "213121045222" +
	       "053522402433" +
	       "313022345432" +
	       "220325340454";

	private String [] puzzles = { puzzle, puzzle2 };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setLayout(SLayoutLoader.getInstance().puzzleFlood);
		setBackgroundImage(R.drawable.puzzle_flood);
		
		Random rand = new Random();
		floodTiles = fromPuzzleString(puzzles[rand.nextInt(puzzles.length)]);		
		
		tileArea = getBoxPixelCoords("tileArea");
		tileHeight =  ( tileArea.bottom - tileArea.top )/tilesPerSide;
		tileWidth =  ( tileArea.right - tileArea.left )/tilesPerSide;
		
		tileImages = new Bitmap[]{
				UBitmapUtil.loadScaledBitmap(R.drawable.puzzle_flood_tile0, (int)tileWidth, (int)tileHeight),
				UBitmapUtil.loadScaledBitmap(R.drawable.puzzle_flood_tile1, (int)tileWidth, (int)tileHeight),
				UBitmapUtil.loadScaledBitmap(R.drawable.puzzle_flood_tile2, (int)tileWidth, (int)tileHeight),
				UBitmapUtil.loadScaledBitmap(R.drawable.puzzle_flood_tile3, (int)tileWidth, (int)tileHeight),
				UBitmapUtil.loadScaledBitmap(R.drawable.puzzle_flood_tile4, (int)tileWidth, (int)tileHeight),
				UBitmapUtil.loadScaledBitmap(R.drawable.puzzle_flood_tile5, (int)tileWidth, (int)tileHeight)
				};
	}
	
	

	private static char[][] fromPuzzleString(String str)
	{
		char[][] puzzle = new char[tilesPerSide][tilesPerSide];

		for ( int i = 0; i < tilesPerSide; ++i )
		{
			for ( int j = 0; j < tilesPerSide; ++j )
			{
				puzzle[i][j] = (char)Integer.parseInt(str.charAt(i+j*tilesPerSide)+"");				
			}
		}
		return puzzle;
	}

	@Override
	public void onDraw(Canvas canvas, Paint paint)
	{
		super.onDraw(canvas, paint);		
		
		for (int i = 0; i < tilesPerSide; i++)
		{
			for (int j = 0; j < tilesPerSide; j++)
			{
				canvas.drawBitmap(tileImages[floodTiles[i][j]],
						tileArea.left + i * tileWidth,
						tileArea.top + j * tileHeight,
						paint);
			}
		}
	}

	private void replaceTiles (int i, int j)
	{
		if ( oldColor == nonExistingColor ) return;
		//check left
		if ( j-1 >= 0 && floodTiles[i][j-1] == oldColor )
		{
			floodTiles[i][j-1] = nonExistingColor;
			replaceTiles ( i, j-1 );
		}
		//check right
		if ( j+1 < tilesPerSide && floodTiles[i][j+1] == oldColor )
		{
			floodTiles[i][j+1] = nonExistingColor;
			replaceTiles ( i, j+1 );
		}
		//check top
		if ( i-1 >= 0 && floodTiles[i-1][j] == oldColor )
		{
			floodTiles[i-1][j] = nonExistingColor;
			replaceTiles ( i-1, j );
		}
		//check bottom
		if ( i+1 < tilesPerSide && floodTiles[i+1][j] == oldColor )
		{
			floodTiles[i+1][j] = nonExistingColor;
			replaceTiles( i+1, j );
		}
	}

	@Override
	public void onBoxTouched(String boxName)
	{
		StringTokenizer st = new StringTokenizer(boxName,"_");
		
		if(st.nextToken().equals("button"))
		{			
			MSoundManager.getInstance().playSoundEffect(R.raw.tick);

			char newColor = (char)Integer.parseInt(st.nextToken());

			if ( newColor < 0 )
				return;

			oldColor = floodTiles[0][0];
			floodTiles[0][0] = nonExistingColor;		
			replaceTiles (0, 0);
			fillTiles(nonExistingColor,newColor);
			int numTilesFilled = numTilesOfColor(newColor);
			repaint();

			if ( numTilesFilled >= tilesPerSide * tilesPerSide )
			{
				//MSoundManager.getInstance().playSoundEffect(R.raw.open_door);
				goToNextStage();
			}			
		}
	}

	private void fillTiles(char findColor, char replaceColor)
	{
		for ( int i = 0; i < tilesPerSide; ++i )
		{
			for ( int j = 0; j < tilesPerSide; ++j )
			{
				if ( floodTiles[i][j] == findColor )
				{
					floodTiles[i][j] = replaceColor;
				}
			}
		}		
	}
	
	private int numTilesOfColor(char color)
	{
		int count = 0;
		for ( int i = 0; i < tilesPerSide; ++i )
		{
			for ( int j = 0; j < tilesPerSide; ++j )
			{
				if ( floodTiles[i][j] == color )
				{
					++count;
				}
			}
		}		
		return count;
	}	

	private void goToNextStage()
	{
		//TODO: play end scene, and goto next stage properly
		finish();
	}

}
