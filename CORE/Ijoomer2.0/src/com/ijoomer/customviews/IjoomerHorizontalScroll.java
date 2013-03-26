package com.ijoomer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class IjoomerHorizontalScroll extends HorizontalScrollView {

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

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

}
