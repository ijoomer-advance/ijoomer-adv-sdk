package com.ijoomer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * This Class Contains All Method Related To IjoomerFacebookWebview.
 * 
 * @author tasol
 * 
 */
public class IjoomerFacebookWebview extends WebView {
	public IjoomerFacebookWebview(Context context) {
		super(context);
	}

	public IjoomerFacebookWebview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public IjoomerFacebookWebview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		try {
			super.onWindowFocusChanged(hasWindowFocus);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
