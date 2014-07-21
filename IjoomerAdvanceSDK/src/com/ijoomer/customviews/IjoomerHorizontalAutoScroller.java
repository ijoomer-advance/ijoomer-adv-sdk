package com.ijoomer.customviews;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class IjoomerHorizontalAutoScroller extends HorizontalScrollView {

	private int scrollPos = 0;
	private int oldscrollpos = -1;
	private TimerTask scrollerSchedule;
	private Timer scrollTimer = null;

	private int LEFTTORIGHT = 1;
	private int RIGHTTOLEFT = 2;
	private int current = LEFTTORIGHT;

	private LinearLayout lnrItemHolder;
	private ItemClickListener itemClickListener;

	private int scrollDuration = 100;

	public int getScrollDuration() {
		return scrollDuration;
	}

	public void setScrollDuration(int scrollDuration) {
		this.scrollDuration = scrollDuration;
	}

	private ItemClickListener getItemClickListener() {
		return itemClickListener;
	}

	public void setItemClickListener(ItemClickListener itemClickListener) {
		this.itemClickListener = itemClickListener;
	}

	public interface ItemClickListener {
		void onItemClick(View v, int position);
	}

	public void addItem(final View item) {
		lnrItemHolder.addView(item);
		final int currentItemIndex = lnrItemHolder.getChildCount() - 1;
		item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("ItemIndex :" + currentItemIndex);
				if (getItemClickListener() != null) {
					itemClickListener.onItemClick(item, currentItemIndex);
				}
			}
		});
	}

	public IjoomerHorizontalAutoScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public IjoomerHorizontalAutoScroller(Context context) {
		super(context);
		init();
	}

	private void addItemHolder() {
		if (lnrItemHolder == null) {
			lnrItemHolder = new LinearLayout(getContext());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lnrItemHolder.setLayoutParams(params);
			this.addView(lnrItemHolder);
		}
	}

	private void init() {

		setHorizontalScrollBarEnabled(false);
		addItemHolder();
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				stopAutoScroll();
				final Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						((Activity) getContext()).runOnUiThread(new Runnable() {

							@Override
							public void run() {
								t.cancel();
								startAutoScrolling();
							}
						});
					}
				}, 2000, 1000);
				return false;
			}
		});

	}

	@Override
	public void removeAllViews() {
		
		lnrItemHolder.removeAllViews();
	}

	public void startAutoScrolling() {
		if (scrollTimer == null) {
			scrollTimer = new Timer();
			final Runnable Timer_Tick = new Runnable() {
				public void run() {

					if (current == LEFTTORIGHT) {
						moveScrollViewLeftToRight();
					} else {
						moveScrollViewRightToLeft();
					}
				}
			};

			if (scrollerSchedule != null) {
				scrollerSchedule.cancel();
				scrollerSchedule = null;
			}
			scrollerSchedule = new TimerTask() {
				@Override
				public void run() {
					((Activity) getContext()).runOnUiThread(Timer_Tick);
				}
			};

			scrollTimer.schedule(scrollerSchedule, getScrollDuration(), getScrollDuration());
		}
	}

	public void stopAutoScroll() {
		if (scrollTimer != null) {
			scrollTimer.cancel();
			scrollTimer = null;
		}
	}

	public void moveScrollViewLeftToRight() {

		scrollPos = (int) (getScrollX() + 1.0);
		scrollTo(scrollPos, 0);
		if (scrollPos != oldscrollpos) {
			oldscrollpos = scrollPos;
		} else {
			current = RIGHTTOLEFT;
			return;
		}

	}

	public void moveScrollViewRightToLeft() {
		scrollPos = (int) (getScrollX() - 1.0);
		scrollTo(scrollPos, 0);
		if (scrollPos != oldscrollpos) {
			oldscrollpos = scrollPos;
		} else {
			current = LEFTTORIGHT;
			return;
		}
	}

}