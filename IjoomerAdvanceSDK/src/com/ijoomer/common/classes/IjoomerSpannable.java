package com.ijoomer.common.classes;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * This Class Contains All Method Related To IjoomerSpannable.
 * 
 * @author tasol
 * 
 */
public class IjoomerSpannable extends ClickableSpan {

	private int color = -1;
	private float fontSize = -1;
	private boolean isUnderline = true;

	/**
	 * Constructor
	 */
	public IjoomerSpannable() {
	}

	/**
	 * Constructor
	 */
	public IjoomerSpannable(int color) {
		this.color = color;
	}

	/**
	 * Constructor
	 */
	public IjoomerSpannable(float fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Constructor
	 */
	public IjoomerSpannable(boolean isUnderline) {
		this.isUnderline = isUnderline;
	}

	/**
	 * Constructor
	 */
	public IjoomerSpannable(int color, boolean isUnderline) {
		this.isUnderline = isUnderline;
		this.color = color;
	}

	/**
	 * Constructor
	 */
	public IjoomerSpannable(int color, float fontSize) {
		this.color = color;
		this.fontSize = fontSize;
	}

	/**
	 * Overrides methods 
	 */
	@Override
	public void updateDrawState(TextPaint ds) {

		if (color != -1) {
			ds.setColor(color);
		}
		if (fontSize > 0) {
			ds.setTextSize(fontSize);
		}

		ds.setUnderlineText(isUnderline);

	}

	@Override
	public void onClick(View widget) {

	}
}