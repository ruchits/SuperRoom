package com.room.puzzles;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.room.Global;
import com.room.R;
import com.room.Global.TextType;
import com.room.media.MSoundManager;
import com.room.scene.SLayout;
import com.room.scene.SLayoutLoader;
import com.room.scene.SSceneActivity;
import com.room.utils.UBitmapUtil;
import com.room.utils.UPair;

public class PJournal extends SSceneActivity {
	
	private static final String DEFAULT_JOURNAL_ENTRY_MSG = "This page is missing";
	Bitmap prevBm, nextBm, page1Bm, page2Bm;
	RectF prevRect, nextRect, page1Rect, page2Rect;
	
	private int currentPage; // from 1 to 5
	private SparseArray<UPair<Integer, Integer>> journalEntries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setLayout(SLayoutLoader.getInstance().journal);
		setBackgroundImage(R.drawable.journal_bg);
		showInventory(false);
		
		prevBm = UBitmapUtil.loadBitmap(R.drawable.journal_prev, true);
		nextBm = UBitmapUtil.loadBitmap(R.drawable.journal_next, true);
    	
		prevRect = getBoxPixelCoords("prev");
		nextRect = getBoxPixelCoords("next");
		page1Rect = getBoxPixelCoords("page1");
		page2Rect = getBoxPixelCoords("page2");
		
		// Map entries to days.
		// TODO: 	This will be removed from here in the future.
		//			addJournalEntry() is responsible for adding any entry to a player's journal.
		journalEntries = new SparseArray<UPair<Integer, Integer>>();
		journalEntries.put(1, new UPair<Integer, Integer>(R.drawable.journal_day1_1, R.drawable.journal_day1_2));
		journalEntries.put(2, new UPair<Integer, Integer>(R.drawable.journal_day2_1, R.drawable.journal_day2_2));
		journalEntries.put(3, new UPair<Integer, Integer>(R.drawable.journal_day3_1, R.drawable.journal_day3_2));
		journalEntries.put(4, new UPair<Integer, Integer>(R.drawable.journal_day4_1, R.drawable.journal_day4_2));
		journalEntries.put(5, new UPair<Integer, Integer>(R.drawable.journal_day5_1, R.drawable.journal_day5_2));
		
		// Initialize the journal with page 1 entry.
		// TODO: Remove this. Demo purposes only - we need to show the entry on first day.
		currentPage = 1;
		page1Bm = UBitmapUtil.loadBitmap(journalEntries.get(currentPage).getLeft(), true);
		page2Bm = UBitmapUtil.loadBitmap(journalEntries.get(currentPage).getRight(), true);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	//Override this function, but super it to use the background image
	public void onDraw(Canvas canvas, Paint paint)
	{
		super.onDraw(canvas, paint);
		
		if(prevBm != null) {
			canvas.drawBitmap(prevBm, null, prevRect, paint);
		}
		
		if(nextBm != null) {
			canvas.drawBitmap(nextBm, null, nextRect, paint);
		}
		
		if (page1Bm!=null && page2Bm!=null) {
			canvas.drawBitmap(page1Bm, null, page1Rect, paint);
			canvas.drawBitmap(page2Bm, null, page2Rect, paint);
		}
	}
	
	@Override
    public void onBoxDown(SLayout.Box box, MotionEvent event)
    {
		MSoundManager.getInstance().playSoundEffect(R.raw.tick);
		int currentDay = Global.getCurrentDay();
		
		if (box.name.equals("prev")) {
			if (currentPage > 1) currentPage -= 1;
		}
		else if (box.name.equals("next")) {
			if (currentPage < currentDay) currentPage += 1;
		}
		
		if (journalEntries.get(currentPage) != null) {
			clearText(false);
			page1Bm = UBitmapUtil.loadBitmap(journalEntries.get(currentPage).getLeft(), true);
			page2Bm = UBitmapUtil.loadBitmap(journalEntries.get(currentPage).getRight(), true);
		}
		else {
			page1Bm = null;
			page2Bm = null;
			setText(DEFAULT_JOURNAL_ENTRY_MSG, TextType.TEXT_CENTERED, false);
		}
    }
	
	// Add an entry to the player's journal.
	public void addJournalEntry(int day) {
		//TODO: Add an entry to journal for the requested day.
	}
}
