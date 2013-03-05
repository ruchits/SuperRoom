package com.room.puzzles;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.room.R;
import com.room.Global;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;

public class PStatues extends SSceneActivity
{
	public static final int NUM_STATUES = 3;
	public static final int ROTATIONS = 4;
	private static final int MAXCLICK = 6;
	private static final int HINT_MSG_DURATION = 10000;
	
	private RectF[] symbolArea = new RectF[NUM_STATUES];
	private RectF[] statueArea = new RectF[NUM_STATUES];
	private RectF submitArea;
	private ArrayList<Bitmap> symbolImages;
	private ArrayList<Bitmap> statueImages;
	private Bitmap submitImage;
	private float statueWidth, statueHeight;
	private float symbolWidth, symbolHeight;
	private float submitWidth, submitHeight;
	
	private enum SymbolState {
		WHITE, BLACK, EMPTY
	}
	private int clickCounter;
	private SymbolState[] symbols = new SymbolState[NUM_STATUES];
	private int[] answers = new int[NUM_STATUES];
	private int[] guesses = new int[NUM_STATUES];
	
	@Override	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().puzzleStatues);
		setBackgroundImage(R.drawable.puzzle_statues2);
		for ( int i = 0; i < NUM_STATUES; ++i )
		{
			symbolArea[i] = getBoxPixelCoords("symbol_"+i);
			statueArea[i] = getBoxPixelCoords("statue_"+i);
		}
		submitArea = getBoxPixelCoords("submit");
		statueWidth = statueArea[0].right - statueArea[0].left;
		statueHeight = statueArea[0].bottom - statueArea[0].top;
		symbolWidth = symbolArea[0].right - symbolArea[0].left;
		symbolHeight = symbolArea[0].bottom - symbolArea[0].top;
		submitWidth = submitArea.right - submitArea.left;
		submitHeight = submitArea.bottom - submitArea.top;

		statueImages = UBitmapUtil.populateBitmaps("puzzle_statue_", NUM_STATUES*ROTATIONS, (int)statueWidth, (int)statueHeight);
		symbolImages = UBitmapUtil.populateBitmaps("puzzle_symbol_", 2, (int)symbolWidth, (int)symbolHeight);
		submitImage = UBitmapUtil.loadScaledBitmap(R.drawable.puzzle_submit, (int)submitWidth, (int)submitHeight);
		init_puzzle();
	}
	
	private void init_puzzle() {	
		Random rand = new Random();
		answers[0] = 1;
		answers[1] = 6;
		answers[2] = 11;
		for ( int i = 0; i < NUM_STATUES; ++i )
		{
			//answers.put(i, i*ROTATIONS+rand.nextInt(ROTATIONS));
			guesses[i] = i*ROTATIONS; //all set as front initially
			symbols[i] = SymbolState.EMPTY;
		}
		clickCounter = 0;
	}
	
	@Override
	public void onDraw(Canvas canvas, Paint paint)
	{
		super.onDraw(canvas, paint);
		for (int i = 0; i < NUM_STATUES; ++i)
		{
			canvas.drawBitmap(statueImages.get(guesses[i]),
					statueArea[i].left,
					statueArea[i].top,
					paint);

			if ( symbols[i] != SymbolState.EMPTY)
			{
				canvas.drawBitmap(symbolImages.get(symbols[i].ordinal()),
						symbolArea[i].left,
						symbolArea[i].top,
						paint);
			}
		}
		canvas.drawBitmap(submitImage,
				submitArea.left,
				submitArea.top,
				paint);
	}

	@Override
	public void onBoxDown(SLayout.Box box, MotionEvent event)
    {	
    	Log.d("BOXCLICK",box.name);
		StringTokenizer st = new StringTokenizer(box.name,"_");
		
		String clickedBox = st.nextToken();
		if(clickedBox.equals("statue"))
		{
			int selStatue = Integer.parseInt(st.nextToken());
			guesses[selStatue] = ( guesses[selStatue] - selStatue * ROTATIONS + 1 ) % ROTATIONS + selStatue * ROTATIONS;
			repaint();
			MSoundManager.getInstance().playSoundEffect(R.raw.wood_whack); //change later?
		}
		else if (clickedBox.equals("submit"))
		{
			boolean result = updateSymbolStates();
			repaint();
			clickCounter++;
		    if (result)
		    {
		    	handleSuccess();
		    }
		    else if (clickCounter == MAXCLICK)
		    {
		    	handleFailure();
		    }
		}
    }
	
	private boolean updateSymbolStates()
	{
		Log.e("PStatues", "updateSymbolStates");
		int numBlack = 0;
		int j = 0;
		int[] whiteList = new int[NUM_STATUES];
		int[] blackList = new int[NUM_STATUES];
		for (int i = 0; i < NUM_STATUES; ++i)
		{
			if (answers[i] == guesses[i])
			{
				symbols[j++] = SymbolState.BLACK;
				numBlack++;
				blackList[i] = 1;
				Log.e("PStatues", "Black in "+ (j-1));
			}
		}
		for (int i = 0; i < NUM_STATUES; ++i)
		{	
			if (blackList[i] == 1) continue;
			for (int k = 0; k < NUM_STATUES; ++k)
			{
				if (k == i || blackList[k] == 1 || whiteList[k] == 1) continue;
				if (answers[k] == guesses[i]+(k-i)*ROTATIONS)
				{
					Log.e("PStatues","answers["+k+"]=guesses["+i+"]=="+answers[k]);
					symbols[j++] = SymbolState.WHITE;
					whiteList[k] = 1;
					Log.e("PStatues", "White in "+ (j-1));
					break;
				}
			}
		}
		for (; j < NUM_STATUES; ++j )
		{
			symbols[j] = SymbolState.EMPTY;
			Log.e("PStatues", "Empty in "+ j);
		}
		return (numBlack == NUM_STATUES);
	}

	private void handleSuccess() {
		finish();
	}

	private void handleFailure() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(300);
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, R.string.hint_statue, HINT_MSG_DURATION);
		toast.show();
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() {
	         public void run() { 
	        	 init_puzzle();
	        	 MSoundManager.getInstance().playSoundEffect(R.raw.swords); //change later
	        	 repaint();
	         } 
	    }, 3000); 
	}
}
