package com.ijoomer.map;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/**
 * This Class Contains All Method Related To Map Managing.
 * 
 * @author tasol
 * 
 */
public class IJoomerMapManager {

	private static MapView mapView;

	/**
	 * Default Constructor.
	 */
	private IJoomerMapManager() {

	}

	/**
	 * This method used to get map overlay.
	 * 
	 * @param defaultMarker
	 *            represented drawable marker shown on map
	 * @param mapView
	 *            represented map
	 * @return
	 */
	public static IJoomerItemizedOverlay getMapOverlay(Drawable defaultMarker, MapView mapView) {
		IJoomerItemizedOverlay overlay = new IJoomerItemizedOverlay(defaultMarker, mapView);
		mapView.getOverlays().add(overlay);
		IJoomerMapManager.mapView = mapView;
		return overlay;
	}

	/**
	 * This method used to get geo point on map.
	 * 
	 * @param longiTude
	 *            represented latitude
	 * @param latiTude
	 *            represented longitude
	 * @return
	 */
	public static GeoPoint getGeopoint(String latiTude, String longiTude) {

		try {
			return new GeoPoint((int) (Float.parseFloat(latiTude) * 1E6), (int) (Float.parseFloat(longiTude) * 1E6));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method used to clean map.
	 */
	public static void cleanMap() {
		if (mapView != null) {
			mapView.getOverlays().clear();
			((Activity) mapView.getContext()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mapView.postInvalidate();
				}
			});
		}
	}

	/**
	 * This method used to remove overlay on map.
	 * 
	 * @param index
	 *            represented index of overlay.
	 */
	public static void removeOverlayAt(int index) {
		if (mapView != null) {
			mapView.getOverlays().remove(index);
			((Activity) mapView.getContext()).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mapView.postInvalidate();
				}
			});
		}
	}

	public static GeoPoint getMapCenterGeopoint() {

		if (mapView != null) {

			Rect currentMapBoundsRect = new Rect();

			mapView.getDrawingRect(currentMapBoundsRect);

			return mapView.getProjection().fromPixels(currentMapBoundsRect.centerX(), currentMapBoundsRect.centerY());
		}
		return null;
	}

	public static GeoPoint getMapMinGeopoint() {

		if (mapView != null) {
			Rect currentMapBoundsRect = new Rect();
			mapView.getDrawingRect(currentMapBoundsRect);
			return mapView.getProjection().fromPixels(currentMapBoundsRect.left, currentMapBoundsRect.top);
		}
		return null;
	}

	public static GeoPoint getMapMaxGeopoint() {

		if (mapView != null) {
			Rect currentMapBoundsRect = new Rect();
			mapView.getDrawingRect(currentMapBoundsRect);
			return mapView.getProjection().fromPixels(currentMapBoundsRect.right, currentMapBoundsRect.bottom);
		}
		return null;
	}
}
