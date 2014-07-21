package com.ijoomer.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * This Class Contains All Method Related To IjoomerButton.
 * 
 * @author tasol
 * 
 */
@SuppressLint({ "NewApi", "Override" })
public class IjoomerGridView extends GridView {

	private int old_count;
	private android.view.ViewGroup.LayoutParams params;
	private boolean isExpandFully = false;
	private int verticleSpacing = 4;
	private int noOfCollumns = 1;

	public int getNumColumns() {
		return noOfCollumns;
	}

	public void setNoOfCollumns(int noOfCollumns) {
		this.noOfCollumns = noOfCollumns;
	}

	public int getVerticleSpacing() {
		return verticleSpacing;
	}

	public void setVerticleSpacing(int verticleSpacing) {
		this.verticleSpacing = verticleSpacing;
	}

	public void setExpandFully(boolean isExpandFully) {
		this.isExpandFully = isExpandFully;
	}

	public IjoomerGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public IjoomerGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public IjoomerGridView(Context context) {
		super(context);
		init(null);
	}

	private void init(AttributeSet attrs) {
		if (attrs != null) {
			String namespace = "http://schemas.android.com/apk/res/android";
			noOfCollumns = attrs.getAttributeIntValue(namespace, "numColumns", 1);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		setVerticalSpacing(getVerticleSpacing());
		if (isExpandFully) {
			try {
				if (getCount() != old_count) {
					old_count = getCount();
					params = getLayoutParams();

					int len = (getCount() % getNumColumns() == 0 ? getCount() / getNumColumns() : getCount() / getNumColumns() + 1);
					params.height = 0;
					for (int i = 0; i < len; i++) {
						params.height = params.height + (old_count > 0 ? getChildAt(0).getHeight() + getVerticleSpacing() : 0);
					}
					params.height += 10;
					setLayoutParams(params);
				}
			} catch (Exception e) {
			}
		}
		super.onDraw(canvas);
	}

}
