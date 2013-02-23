//FLOOD_BOX 0.20859376 0.7140625 0.054166667 0.9513889   //left,right,top,bottom
//bRed 0.73515624 0.8078125 0.05138889 0.18194444
//bPink 0.73515624 0.8070313 0.20555556 0.3375
//bBlue 0.7359375 0.8078125 0.3625 0.49305555
//bGreen 0.73515624 0.80859375 0.51805556 0.6458333
//bYellow 0.73515624 0.8078125 0.66805553 0.7986111
//bSky 0.734375 0.8078125 0.825 0.95555556

package com.room.puzzles;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import com.room.R;
import com.room.Global;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;

public class PFlood extends SSceneActivity
{
    static int tilesPerSide = 12;
    
	int [] redbox = new int[4];
	int [] skybox = new int[4];
	private int padding_from_top;// = (int) coords[2];//( 0.054166667f * Global.SCREEN_HEIGHT );
    private int aBoxSideLen;// = (int) (Global.SCREEN_HEIGHT * ( 0.95555556f - 0.054166667f ))/12;//((( 0.7140625f - 0.20859376f ) * Global.SCREEN_WIDTH )/12);
    private int padding_from_left;// = (int) (0.71f * Global.SCREEN_WIDTH - aBoxSideLen*12);//( 0.20859376f * Global.SCREEN_WIDTH );
    private int[][] floodTiles;
	/*
    private String puzzle = 
    		"rrsbykyygkbp" +
            "pbggbbspssrs" +
    		"gsyrpyybprgb" +
            "sprbssggsysg" +
    		"yggygybbsypr" +
            "yrbgrgyspgpb" +
    		"rgssygypssby" +
            "ryrygggbyygs" +
    		"sbsbbspgbsrb" +
            "gsrrrbpsbgsr" +
    		"ppsrbyyygygy" +
            "pgrrbypgrrrr";
    */
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
    Rect r = new Rect();

	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setLayout(SLayoutLoader.getInstance().puzzleFlood);
		setBackgroundImage(R.drawable.puzzle_flood);
		floodTiles = fromPuzzleString(puzzle);
		getBoxCoords("bRed", redbox);
		getBoxCoords("bSky", skybox);
		padding_from_top = (int) redbox[2];
	    aBoxSideLen = (int) ( skybox[3] - redbox[2] )/tilesPerSide;
	    padding_from_left = (int) Global.SCREEN_WIDTH - redbox[1];
	    
	}
	
	static protected int[][] fromPuzzleString(String str) {
		int[][] puzzle = new int[tilesPerSide][tilesPerSide];
		for ( int i = 0; i < tilesPerSide; ++i )
		{
			for ( int j = 0; j < tilesPerSide; ++j )
			{
				puzzle[i][j] = str.charAt(i+j*tilesPerSide) - '0';
			}
	    }
	    return puzzle;
	}
	
	@Override
	public void onDraw(Canvas canvas, Paint paint)
	{	
		super.onDraw(canvas, paint);
		setBackgroundImage(R.drawable.puzzle_flood);
		Paint floodbox = new Paint();
		int color[] = { getResources().getColor(R.color.red),
	               getResources().getColor(R.color.pink),
	               getResources().getColor(R.color.blue),
	               getResources().getColor(R.color.green),
	               getResources().getColor(R.color.yellow),
	               getResources().getColor(R.color.sky) };
        for (int i = 0; i < tilesPerSide; i++) {
           for (int j = 0; j < tilesPerSide; j++) {
                 getRect(i, j, r);
                 floodbox.setColor(color[floodTiles[i][j]]);
                 canvas.drawRect(r, floodbox);
           }
        }
	}
    
	private void getRect(int x, int y, Rect rect) {
	     rect.set((int) (padding_from_left + x * aBoxSideLen), //left 
	    		  (int) (padding_from_top + y * aBoxSideLen),  //top 
	    		  (int) (padding_from_left + x * aBoxSideLen + aBoxSideLen), //right
	    		  (int) (padding_from_top + y * aBoxSideLen + aBoxSideLen)); //bottom
	}
	
	//looks retarded but works well in practice
	private void replaceTiles (int oldColor, int newColor, int i, int j, int[][]floodTiles )
	{
			//check top
			if ( i-1 >= 0 && floodTiles[i-1][j] == oldColor )
			{
				floodTiles[i-1][j] = newColor;
				replaceTiles ( oldColor, newColor, i-1, j, floodTiles );
			}
		    //check bottom
			if ( i+1 < tilesPerSide && floodTiles[i+1][j] == oldColor )
			{
				floodTiles[i+1][j] = newColor;
				replaceTiles(oldColor, newColor, i+1, j, floodTiles);
			}
			//check left
			if ( j-1 >= 0 && floodTiles[i][j-1] == oldColor )
			{
				floodTiles[i][j-1] = newColor;
				replaceTiles ( oldColor, newColor, i, j-1, floodTiles );
			}
			//check right
			if ( j+1 < tilesPerSide && floodTiles[i][j+1] == oldColor )
			{
				floodTiles[i][j+1] = newColor;
				replaceTiles ( oldColor, newColor, i, j+1, floodTiles );
			}
	}
	
	@Override
    public void onBoxTouched(String boxName)
    {
    	Log.d("BOXCLICK",boxName);
        int newColor = getBoxNum(boxName);
        if ( newColor < 0 ) return;
    	int oldColor = floodTiles[0][0];
    	floodTiles[0][0] = newColor;
    	replaceTiles (oldColor, newColor, 0, 0, floodTiles );
    	repaint();
    }
}
