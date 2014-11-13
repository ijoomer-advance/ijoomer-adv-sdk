package com.ijoomer.common.classes;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.ijoomer.custom.interfaces.IjoomerKeys;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartApplication;

/**
 * This Class Contains All Method Related To IjoomerRequestDataProvider.
 * 
 * @author tasol
 * 
 */
public class IjoomerRequestDataProvider implements IjoomerKeys,
		IjoomerSharedPreferences {

	private String longitude;
	private String latitude;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 */
	public IjoomerRequestDataProvider(Context mContext) {
		try {
			setLatitude(((SmartActivity) mContext).getLatitude());
			setLongitude(((SmartActivity) mContext).getLongitude());
		} catch (Throwable e) {
			setLatitude("0");
			setLongitude("0");
		}
	}

	/**
	 * This method used to get latitude of login user.
	 * 
	 * @return represented {@link String}
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * This method used to set Latitude of login user.
	 * 
	 * @param latitide
	 *            represented latitude
	 */
	public void setLatitude(String latitide) {
		this.latitude = latitide;
	}

	/**
	 * This method used to get longitude of login user.
	 * 
	 * @return represented {@link String}
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * This method used to set longitude of login user.
	 * 
	 * @param latitide
	 *            represented longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * This method use to get Device UDID.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @return represented {@link String}
	 */
	public String getDeviceUDID(Context mContext) {
		String udid = SmartApplication.REF_SMART_APPLICATION
				.readSharedPreferences().getString(SP_GCM_REGID, "");
		if (udid.length() > 0) {
			return udid;
		}
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

}
