package com.ijoomer.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.StateListDrawable;

import com.ijoomer.menubuilder.ColorDrawable;

public class IjoomerMultiPurposeSelector {

	public static final int TOGGLE = 1;

	public static final int DEFAULT = 2;

	int pressedDrawableResource = 0;
	int defaultDrawableResource = 0;

	int pressedBgColor = 0;
	int defaultBgColor = 0;

	int pressedTextColor = 0;
	int defaultTextColor = 0;

	int type = TOGGLE;

	Context mContext;

	public IjoomerMultiPurposeSelector(Context context) {
		this.mContext = context;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private StateListDrawable states;

	private ColorStateList textStates;

	public int getPressedDrawableResource() {
		return pressedDrawableResource;
	}

	public IjoomerMultiPurposeSelector setPressedDrawableResource(int pressedDrawableResource) {
		this.pressedDrawableResource = pressedDrawableResource;
		return this;
	}

	public int getDefaultDrawableResource() {
		return defaultDrawableResource;
	}

	public IjoomerMultiPurposeSelector setDefaultDrawableResource(int defaultDrawableResource) {
		this.defaultDrawableResource = defaultDrawableResource;
		return this;
	}

	public int getPressedBgColor() {
		return pressedBgColor;
	}

	public IjoomerMultiPurposeSelector setPressedBgColor(int pressedBgColor) {
		this.pressedBgColor = pressedBgColor;
		return this;
	}

	public int getDefaultBgColor() {
		return defaultBgColor;
	}

	public IjoomerMultiPurposeSelector setDefaultBgColor(int defaultBgColor) {
		this.defaultBgColor = defaultBgColor;
		return this;
	}

	public int getPressedTextColor() {
		return pressedTextColor;
	}

	public IjoomerMultiPurposeSelector setPressedTextColor(int pressedTextColor) {
		this.pressedTextColor = pressedTextColor;
		return this;
	}

	public int getDefaultTextColor() {
		return defaultTextColor;
	}

	public IjoomerMultiPurposeSelector setDefaultTextColor(int defaultTextColor) {
		this.defaultTextColor = defaultTextColor;
		return this;
	}

	public StateListDrawable getSelector() {

		switch (getType()) {
		case TOGGLE:
			states = new StateListDrawable();
			if (pressedDrawableResource != 0) {
				states.addState(new int[] { -android.R.attr.state_checked }, mContext.getResources().getDrawable(getDefaultDrawableResource()));
				states.addState(new int[] { android.R.attr.state_checked }, mContext.getResources().getDrawable(getPressedDrawableResource()));
			} else if (pressedBgColor != 0) {
				states.addState(new int[] { -android.R.attr.state_checked }, new ColorDrawable(getDefaultBgColor()));
				states.addState(new int[] { android.R.attr.state_checked }, new ColorDrawable(getPressedBgColor()));
			}

			break;

		case DEFAULT:
			states = new StateListDrawable();
			if (pressedDrawableResource != 0) {
				states.addState(new int[] { -android.R.attr.state_pressed }, mContext.getResources().getDrawable(getDefaultDrawableResource()));
				states.addState(new int[] { android.R.attr.state_pressed }, mContext.getResources().getDrawable(getPressedDrawableResource()));
			} else if (pressedBgColor != 0) {
				states.addState(new int[] { -android.R.attr.state_pressed }, new ColorDrawable(getDefaultBgColor()));
				states.addState(new int[] { android.R.attr.state_pressed }, new ColorDrawable(getPressedBgColor()));
			}
			break;

		default:
			break;
		}
		return states;
	}

	public ColorStateList getTextSelector() {

		switch (getType()) {
		case TOGGLE:
			textStates = new ColorStateList(new int[][] { new int[] { -android.R.attr.state_checked }, new int[] { android.R.attr.state_checked } }, new int[] {
					getDefaultTextColor(), getPressedTextColor() });

			break;

		case DEFAULT:
			textStates = new ColorStateList(new int[][] { new int[] { -android.R.attr.state_pressed }, new int[] { android.R.attr.state_pressed } }, new int[] {
					getDefaultTextColor(), getPressedTextColor() });

			break;

		default:
			break;
		}
		return textStates;
	}
}
