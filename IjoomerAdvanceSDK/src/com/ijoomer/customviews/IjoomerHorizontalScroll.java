package com.ijoomer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * This Class Contains All Method Related To IjoomerHorizontalScroll.
 * 
 * @author tasol
 * 
 */
public class IjoomerHorizontalScroll extends HorizontalScrollView {

	
	/**
	 * Overrides methods
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
	public IjoomerHorizontalScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public IjoomerHorizontalScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public IjoomerHorizontalScroll(Context context) {
		super(context);
		init(context);
	}

	void init(Context context) {
		setHorizontalFadingEdgeEnabled(false);

		setVerticalFadingEdgeEnabled(false);

		setVerticalScrollBarEnabled(false);

		setHorizontalScrollBarEnabled(false);

	}

}
