package com.ijoomer.common.classes;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.ijoomer.custom.interfaces.IjoomerKeys;

public class IjoomerRequestDataProvider implements IjoomerKeys {

	public static LocationManager mlocManager;
	private static String longitude;
	private static String latitude;
	public static IjoomerLocationManager mLIjoomerLocationManager;

	public String getLatitude() {
		if (latitude != null) {
			return latitude;
		}
		Location loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null) {
			loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				return "" + loc.getLatitude();
			}
		} else {
			return "" + loc.getLatitude();
		}
		return "0";
	}

	public void setLatitude(String latitide) {
		IjoomerRequestDataProvider.latitude = latitide;
	}

	public String getLongitude() {
		if (longitude != null) {
			return longitude;
		}
		Location loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null) {
			loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				return "" + loc.getLongitude();
			}
		} else {
			return "" + loc.getLongitude();
		}
		return "0";
	}

	public void setLongitude(String longitude) {
		IjoomerRequestDataProvider.longitude = longitude;
	}

	public IjoomerRequestDataProvider(Context mContext) {

		if (mlocManager == null) {
			try {
				mlocManager = (LocationManager) mContext.getSystemService(Activity.LOCATION_SERVICE);
				mLIjoomerLocationManager = new IjoomerLocationManager();
				((Activity) mContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {

							mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLIjoomerLocationManager);
						} catch (Throwable e) {
							e.printStackTrace();
						}
						try {
							mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLIjoomerLocationManager);
						} catch (Throwable e) {
							e.printStackTrace();
						}

					}
				});
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	}

	public String getDeviceUDID(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	private class IjoomerLocationManager implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			setLatitude("" + location.getLatitude());
			setLongitude("" + location.getLongitude());
			System.out.println("Latitude : " + location.getLatitude());
			mlocManager.removeUpdates(mLIjoomerLocationManager);
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	}
}
