package com.ijoomer.customviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class IjoomerViewPager extends ViewPager {

	private boolean scrollable = true;

	public boolean isScrollable() {
		return scrollable;
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	public IjoomerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IjoomerViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.scrollable) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (this.scrollable) {
			return super.onInterceptTouchEvent(event);
		}

		return false;
	}
}
