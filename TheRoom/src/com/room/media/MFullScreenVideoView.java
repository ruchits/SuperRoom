package com.room.media;

import com.room.Global;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class MFullScreenVideoView extends VideoView
{
    public MFullScreenVideoView(Context context)
    {
    	super(context);
    }
    
    public MFullScreenVideoView(Context context, AttributeSet attrs)
    {
    	super(context,attrs);
    }   
    
    public MFullScreenVideoView(Context context, AttributeSet attrs, int defstyle)
    {
    	super(context,attrs,defstyle);
    }      

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
    	setMeasuredDimension(Global.SCREEN_WIDTH,Global.SCREEN_HEIGHT);
    }
}
