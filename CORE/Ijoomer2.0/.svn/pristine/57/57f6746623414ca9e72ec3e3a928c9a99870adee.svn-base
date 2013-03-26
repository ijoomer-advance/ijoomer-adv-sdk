package com.ijoomer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ListView;

public class IjoomerListView extends ListView {

	private static final int SWIPE_MIN_DISTANCE = 50;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private GestureDetector gDetector;
	private boolean isFling = false;

	public IjoomerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public IjoomerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IjoomerListView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context mContext) {
		gDetector = new GestureDetector(new MyGesture());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		gDetector.onTouchEvent(ev);

		if (isFling) {
			return true;
		} else {
			return gDetector.onTouchEvent(ev);
		}
	}

	private class MyGesture extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				isFling = true;
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				isFling = true;
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}

}
