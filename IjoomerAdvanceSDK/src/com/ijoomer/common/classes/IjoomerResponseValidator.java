package com.ijoomer.common.classes;

import java.util.Calendar;

import org.json.JSONObject;

import android.content.Context;

public class IjoomerResponseValidator extends IjoomerRequestDataProvider {

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

	private int responseCode = 108;
	private String errorMessage;
	private static JSONObject notificationData;;

	public JSONObject getNotificationData() {
		return notificationData;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public boolean validateResponse(JSONObject data) {
		long startTime;
		long endTime;
		startTime = Calendar.getInstance().getTimeInMillis();
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

	private void removeUnnacessaryFields(JSONObject data) {
		data.remove("code");
		data.remove("message");
		data.remove("full");
		data.remove("notification");
	}
}
