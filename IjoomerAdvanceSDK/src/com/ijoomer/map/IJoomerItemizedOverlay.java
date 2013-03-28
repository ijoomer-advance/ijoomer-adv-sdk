package com.ijoomer.map;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * This Class Contains All Method Related To Map Itemized Overlay.
 * 
 * @author tasol
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class IJoomerItemizedOverlay extends ItemizedOverlay {

	private ArrayList<IjoomerMapOverlayItem> mOverlays = new ArrayList<IjoomerMapOverlayItem>();
	private MapView mapView;
	private IjoomerMapPinListener target;
	private View mapBubbleView;

	/**
	 * Constructor.
	 * 
	 * @param defaultMarker
	 *            represented drawable marker on map
	 */
	protected IJoomerItemizedOverlay(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
	}

	/**
	 * Constructor.
	 * 
	 * @param defaultMarker
	 *            represented drawable marker on map
	 * @param mapView
	 *            represented map.
	 */
	protected IJoomerItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker));
		this.mapView = mapView;
	}

	@Override
	public void draw(Canvas c, MapView m, boolean shadow) {
		super.draw(c, m, false);
	}

	@Override
	protected OverlayItem createItem(int i) {
		if (i == (mOverlays.size() - 1)) {
			mapView.getController().animateTo(mOverlays.get(i).getPoint());
		}
		return mOverlays.get(i);
	}

	/**
	 * This method used to put pin on map.
	 * 
	 * @param gp
	 *            represented geo point on map
	 * @param data
	 *            represented data.
	 */
	public void putPin(GeoPoint gp, Object data) {
		IjoomerMapOverlayItem ijoomerMapOverlayItem = new IjoomerMapOverlayItem(gp, data);
		mOverlays.add(ijoomerMapOverlayItem);
		populate();
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		try {
			if (mapBubbleView != null) {
				mapView.removeView(mapBubbleView);
				MapView.LayoutParams params = new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, mOverlays.get(index).getPoint(),
						MapView.LayoutParams.BOTTOM_CENTER | MapView.LayoutParams.CENTER_VERTICAL);
				mapView.addView(mapBubbleView, params);
			}
			if (mapBubbleView != null && target != null) {
				target.onPinClick(index, mapBubbleView, mOverlays.get(index).getPinData());
			}
			mapView.getController().animateTo(mOverlays.get(index).getPoint());
		} catch (Throwable e) {
		}
		return true;
	}

	/**
	 * This method used to set pin listener on map.
	 * 
	 * @param mapPinListener
	 *            represented {@link IjoomerMapPinListener}
	 */
	public void setOnPinListener(IjoomerMapPinListener mapPinListener) {
		target = mapPinListener;
	}

	/**
	 * This method used to set bubble view on map.
	 * 
	 * @param v
	 *            represented view
	 */
	public void setMapBubbleView(View v) {
		mapBubbleView = v;
	}

	/**
	 * This method used to hide bubble view on map.
	 */
	public void hideBubble() {
		if (mapBubbleView != null) {
			mapView.removeView(mapBubbleView);
		}
	}

	/**
	 * This method used to clear all overlay on map.
	 */
	public void cleanOverlay() {
		mOverlays.clear();
	}
}
