package com.smart.framework;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.src.R;
import com.smart.android.framework.PullToRefreshListView;

public class SmartGestureListener extends SimpleOnGestureListener implements
		OnTouchListener {

	private Context context;
	GestureDetector gDetector;
	private ListView lstCustomListView;
	private boolean isFling = false;;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private int swipeDeleteIjoomerButtonID;
	private OnSwipeDelete local;

	public SmartGestureListener() {
		super();
	}

	public SmartGestureListener(Context context, ListView lstCustomListView,
			int swipeDeleteIjoomerButtonID, OnSwipeDelete target) {
		this(context, lstCustomListView, null, swipeDeleteIjoomerButtonID, target);

	}

	public SmartGestureListener(Context context, ListView lstCustomListView,
			GestureDetector gDetector, final int swipeDeleteButttonID,
			final OnSwipeDelete target) {

		this.swipeDeleteIjoomerButtonID = swipeDeleteButttonID;
		this.local = target;

		if (gDetector == null)
			gDetector = new GestureDetector(context, this);

		this.context = context;
		this.gDetector = gDetector;
		this.lstCustomListView = lstCustomListView;

		this.lstCustomListView.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (!isFling) {

					Log.e("tag", "onScroll");
					int childern = SmartGestureListener.this.lstCustomListView
							.getChildCount();
					Log.e("tag", "count : " + childern);
					for (int i = 0; i < childern; i++) {
						View v = SmartGestureListener.this.lstCustomListView
								.getChildAt(i);
						Log.e("tag", "inside for loop");
						if (v != null) {
							IjoomerButton btnDelete = (IjoomerButton) v
									.findViewById(swipeDeleteButttonID);
							if (btnDelete != null) {
								Log.e("tag", "IjoomerButton found");
								if (btnDelete.getVisibility() == View.VISIBLE) {
									btnDelete.setVisibility(View.INVISIBLE);
								} else {
									btnDelete.setVisibility(View.INVISIBLE);
								}

							} else {
								Log.e("tag", "IjoomerButton not found");
							}
						}
						{
							Log.e("tag", "view not found");
						}
					}
				} else {
					isFling = false;
				}
			}
		});
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		super.onFling(e1, e2, velocityX, velocityY);

		try {
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				isFling = true;
				if (lstCustomListView != null) {
					final int adapterIndex = lstCustomListView.pointToPosition(
							(int) e1.getX(), (int) e1.getY());
					int firstViewItemIndex = lstCustomListView
							.getFirstVisiblePosition();
					int viewIndex = adapterIndex - firstViewItemIndex;
					Log.e("indexes", adapterIndex + " : " + firstViewItemIndex
							+ " : " + viewIndex);

					View v = lstCustomListView.getChildAt(viewIndex);
					IjoomerButton btnDelete = (IjoomerButton) v
							.findViewById(this.swipeDeleteIjoomerButtonID);

					// flip in animation applied to the IjoomerButton

					if (btnDelete.getVisibility() == View.INVISIBLE) {

						btnDelete.setVisibility(View.VISIBLE);
						Animation a = AnimationUtils.loadAnimation(context,
								R.anim.slide_left_in);
						a.reset();
						btnDelete.clearAnimation();
						btnDelete.startAnimation(a);

						btnDelete.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {

								if (lstCustomListView instanceof PullToRefreshListView) {
									local.actionSwipeDelete(adapterIndex - 1);
								} else {
									local.actionSwipeDelete(adapterIndex);
								}
							}
						});
					} else {
						btnDelete.setVisibility(View.INVISIBLE);
					}

				}
				return true;
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				isFling = true;

				if (lstCustomListView != null) {
					final int adapterIndex = lstCustomListView.pointToPosition(
							(int) e1.getX(), (int) e1.getY());
					int firstViewItemIndex = lstCustomListView
							.getFirstVisiblePosition();
					final int viewIndex = adapterIndex - firstViewItemIndex;
					Log.e("indexes", adapterIndex + " : " + firstViewItemIndex
							+ " : " + viewIndex);

					View v = lstCustomListView.getChildAt(viewIndex);
					IjoomerButton btnDelete = (IjoomerButton) v
							.findViewById(swipeDeleteIjoomerButtonID);

					if (btnDelete.getVisibility() == View.INVISIBLE) {

						btnDelete.setVisibility(View.VISIBLE);

						Animation a = AnimationUtils.loadAnimation(context,
								R.anim.slide_left_in);
						a.reset();
						btnDelete.clearAnimation();
						btnDelete.startAnimation(a);

						btnDelete.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								if (lstCustomListView instanceof PullToRefreshListView) {
									local.actionSwipeDelete(adapterIndex - 1);
								} else {
									local.actionSwipeDelete(adapterIndex);
								}
							}
						});
					} else {
						btnDelete.setVisibility(View.INVISIBLE);
					}

				}
				return true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return super.onSingleTapConfirmed(e);
	}

	public boolean onTouch(View v, MotionEvent event) {
		Log.e("gesture", "onTouch");
		return gDetector.onTouchEvent(event);
	}

	public GestureDetector getDetector() {
		return gDetector;
	}

}
