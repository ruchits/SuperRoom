package com.room.puzzles;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.room.Global;
import com.room.R;
import com.room.media.MSoundManager;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;

public class PFlood extends SSceneActivity
{
	private static final int tilesPerSide = 12;
	private static final char nonExistingColor = (char)-1;
	private static final int MAXCLICK = 3; //TODO: change to 22 later
	private static final int numSymbols = 6;
	private static final int hintMessageDuration = 7000;
	
	private RectF tileArea;
	private RectF lifeBarArea;
	private float tileWidth;
	private float tileHeight;
	private float lifeBarWidth;
	private float lifeBarHeight;
	private char[][] floodTiles;
	private ArrayList<Bitmap> tileImages;
	private ArrayList<Bitmap> lifeBarImages;
	private char oldColor;
	private int clickCounter;
	int numTilesFilled;

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
		
		init_puzzle();
		tileArea = getBoxPixelCoords("tileArea");
		lifeBarArea = getBoxPixelCoords("lifeBar");
		lifeBarHeight = (lifeBarArea.bottom - lifeBarArea.top)/MAXCLICK;
		lifeBarWidth = (lifeBarArea.right - lifeBarArea.left);
		tileHeight =  ( tileArea.bottom - tileArea.top )/tilesPerSide;
		tileWidth =  ( tileArea.right - tileArea.left )/tilesPerSide;
		
		//are we really going to have 22 different bar images?~_~
		lifeBarImages = UBitmapUtil.populateBitmaps("lifebar_", MAXCLICK, lifeBarWidth, lifeBarHeight); 
		tileImages = UBitmapUtil.populateBitmaps("puzzle_flood_tile", numSymbols, tileWidth, tileHeight);
	}
	
	private void init_puzzle() {
		Random rand = new Random();
		floodTiles = fromPuzzleString(puzzles[rand.nextInt(puzzles.length)]);
		clickCounter = 0;
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
		Log.e("PFlood", "clickCounter:"+clickCounter);
		
		for (int i = 0; i < Math.min(clickCounter, MAXCLICK); ++i) //for now 
		{
			canvas.drawBitmap(lifeBarImages.get(i),
					lifeBarArea.left,
					lifeBarArea.top + i * lifeBarHeight,
					paint);
		}

		for (int i = 0; i < tilesPerSide; i++)
		{
			for (int j = 0; j < tilesPerSide; j++)
			{
				canvas.drawBitmap(tileImages.get(floodTiles[i][j]),
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
			if (oldColor != newColor) {
				clickCounter++;
				floodTiles[0][0] = nonExistingColor;		
				replaceTiles (0, 0);
				fillTiles(nonExistingColor,newColor);
				numTilesFilled = numTilesOfColor(newColor);
				repaint();
			}

			if ( numTilesFilled >= tilesPerSide * tilesPerSide )
			{
				goToNextStage();
			}
			if ( clickCounter == MAXCLICK )
			{
				handleFailure();
			}
		}
	}

	private void handleFailure() {
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, R.string.hint_flood, hintMessageDuration);
		toast.show();
		MSoundManager.getInstance().playSoundEffect(R.raw.swords); //change later
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	 init_puzzle();
	        	 repaint();
	         } 
	    }, 3000); 
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
