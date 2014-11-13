package com.ijoomer.common.classes;

import java.util.Calendar;

import org.json.JSONObject;

import android.content.Context;

/**
 * This Class Contains All Method Related To IjoomerResponseValidator.
 * 
 * @author tasol
 * 
 */
public class IjoomerResponseValidator extends IjoomerRequestDataProvider {

	private static JSONObject notificationData;
	private String errorMessage;
	private int responseCode = 108;

	/**
	 * Constructor
	 * 
	 * @param mContext
	 *            {@link Context}
	 */
	public IjoomerResponseValidator(Context mContext) {
		super(mContext);
		try {
			if (notificationData == null) {
				notificationData = new JSONObject();

				notificationData.put("friendNotification", "0");
				notificationData.put("messageNotification", "0");
				notificationData.put("groupNotification", "0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method used to get notification data.
	 * 
	 * @return represented {@link JSONObject}
	 */
	public JSONObject getNotificationData() {
		return notificationData;
	}

	/**
	 * This method used to get error message.
	 * 
	 * @return represented {@link String}
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * This method used to set message.
	 * 
	 * @param errorMessage
	 *            represented error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * This method used to get response code.
	 * 
	 * @return represented {@link Integer}
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * This method used to set reponse code.
	 * 
	 * @param responseCode
	 *            represented response code
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * This method used to validate json response.
	 * 
	 * @param data
	 *            represented json object
	 * @return represented {@link Boolean}
	 */
	public boolean validateResponse(JSONObject data) {
		long startTime;
		long endTime;
		startTime = Calendar.getInstance().getTimeInMillis();
		if (data.has("php_server_error")) {
			try {
				System.err.println("WSPHP_SERVER_WARNINGS/ERRORS : " + data.getString("php_server_error"));
				data.remove("php_server_error");
			} catch (Exception e) {
			}
		}
		if (data.has("notification")) {
			try {
				JSONObject obj = data.getJSONObject("notification");
				if (obj.has("friendNotification")) {
					notificationData.put("friendNotification", obj.get("friendNotification"));
				} else {
					notificationData.put("friendNotification", "0");
				}
				if (obj.has("messageNotification")) {
					notificationData.put("messageNotification", obj.get("messageNotification"));
				} else {
					notificationData.put("messageNotification", "0");
				}
				if (obj.has("globalNotification")) {
					notificationData.put("globalNotification", obj.get("globalNotification"));
				} else {
					notificationData.put("globalNotification", "0");
				}

			} catch (Exception e) {
			}
		} else {
			try {
				notificationData.put("friendNotification", "0");
				notificationData.put("messageNotification", "0");
				notificationData.put("globalNotification", "0");
			} catch (Exception e) {
			}
		}
		if (data.has("code")) {
			try {
				if (data.has("message")) {
					setErrorMessage(data.getString("message"));
				}
				int code = Integer.parseInt(data.getString("code"));
				removeUnnacessaryFields(data);
				setResponseCode(code);
				if (code == 200 || code == 703) {
					endTime = Calendar.getInstance().getTimeInMillis();
					System.out.println("Validation Complete in: " + (endTime - startTime));
					return true;
				} else {
					endTime = Calendar.getInstance().getTimeInMillis();
					System.out.println("Validation Complete in: " + (endTime - startTime));
					return false;
				}
			} catch (Throwable e) {
				setResponseCode(108);
			}
		} else {
			setResponseCode(108);
		}
		removeUnnacessaryFields(data);
		endTime = Calendar.getInstance().getTimeInMillis();
		System.out.println("Validation Complete in: " + (endTime - startTime));
		return false;
	}

	/**
	 * This method used to remove unnecessary filed from response.
	 * 
	 * @param data
	 *            represented json object
	 */
	private void removeUnnacessaryFields(JSONObject data) {
		data.remove("code");
		data.remove("message");
		data.remove("notification");
	}
}
