package com.android.emptycanvas;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	EmptyView emptyview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		emptyview = new EmptyView(this);
		setContentView(emptyview);
		//setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private class EmptyView extends View {
		public EmptyView(Context context) {
			super(context);
		}

		@Override protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
			// draw on the canvas here.
			
			// will need to pre-allocate Paint, 
			//since object creation might slow down onDraw
		    Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			
			// make the entire canvas white for now.
			paint.setColor(Color.WHITE);
			canvas.drawPaint(paint);
			
			// draw a yellow circle
		    paint.setColor(Color.YELLOW);
			canvas.drawCircle(200, 200, 40, paint);
		}
	}
}
