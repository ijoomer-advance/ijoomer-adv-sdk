package com.ijoomer.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * This Class Contains All Method Related To Map Overlay Item.
 * 
 * @author tasol
 * 
 */
public class IjoomerMapOverlayItem extends OverlayItem {

	private Object itemData;

	/**
	 * Constructor.
	 * 
	 * @param point
	 *            represented geo point on map
	 * @param title
	 *            represented title
	 * @param snippet
	 *            represented snippet
	 */
	public IjoomerMapOverlayItem(GeoPoint point, String title, String snippet) {
		super(point, title, snippet);
	}

	/**
	 * Constructor.
	 * 
	 * @param point
	 *            represented geo point on map
	 * @param data
	 *            represented data
	 */
	public IjoomerMapOverlayItem(GeoPoint point, Object data) {
		super(point, "", "");
		itemData = data;
	}

	/**
	 * This method used to get map pin data.
	 * 
	 * @return
	 */
	public Object getPinData() {
		return itemData;
	}
}
