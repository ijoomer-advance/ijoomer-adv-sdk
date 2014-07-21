package com.smart.android.framework;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.StateSet;

import com.ijoomer.customviews.IjoomerRadioButton;
import com.smart.framework.SmartApplication;

public class SmartTabItem extends IjoomerRadioButton {

	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
	private static final int[] CHECKED_PRESSED_STATE_SET = { android.R.attr.state_pressed };
	private int OnDrawable, OffDrawable, OffPressedDrawable;
	private String nameSpace;

	public SmartTabItem(Context context) {
		super(context);
	}

	public SmartTabItem(Context context, AttributeSet attr) {
		super(context, attr);
		initComponent(attr);
	}

	public SmartTabItem(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		initComponent(attr);
	}

	@SuppressWarnings("deprecation")
	private void initComponent(AttributeSet attr) {

		try {
			nameSpace = "http://schemas.android.com/apk/res/" + SmartApplication.REF_SMART_APPLICATION.getPackageName();
			OnDrawable = attr.getAttributeResourceValue(nameSpace, "OnDrawable", 0);
			OffDrawable = attr.getAttributeResourceValue(nameSpace, "OffDrawable", 0);
			OffPressedDrawable = attr.getAttributeResourceValue(nameSpace, "OffPressedDrawable", 0);

			StateListDrawable mStateContainer = new StateListDrawable();
			StateListDrawable mStateContainer1 = new StateListDrawable();
			this.setButtonDrawable(mStateContainer1);
			Drawable checkedDrawable = getResources().getDrawable(OnDrawable);
			Drawable defaultDrawable = getResources().getDrawable(OffDrawable);
			Drawable defaultPressedDrawable = getResources().getDrawable(OffPressedDrawable);
			mStateContainer.addState(CHECKED_STATE_SET, checkedDrawable);
			mStateContainer.addState(CHECKED_PRESSED_STATE_SET, defaultPressedDrawable);
			mStateContainer.addState(StateSet.WILD_CARD, defaultDrawable);
			this.setBackgroundDrawable(mStateContainer);
            setChecked(false);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
