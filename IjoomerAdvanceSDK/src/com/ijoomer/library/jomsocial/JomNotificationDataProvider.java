package com.ijoomer.library.jomsocial;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerResponseValidator;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This Class Contains All Method Related To JomNotificationDataProvider.
 * 
 * @author tasol
 * 
 */
public class JomNotificationDataProvider extends IjoomerResponseValidator {
	private Context mContext;
	
	public static String NOTIFICATION_FRIENDS = "friend";
	public static String NOTIFICATION_MESSAGES = "message";
	private final String NOTIFICATION = "notification";
	private final String FRIEND = "friend";
	private final String APPROVEREQUEST = "approveRequest";
	private final String REJECTREQUEST = "rejectRequest";
	private final String CONNECTIONID = "connectionID";
	private final String USER = "user";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public JomNotificationDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * This method used to get notification list.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getNotifications(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<Object>>() {

			@Override
			protected ArrayList<Object> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, USER);
				iw.addWSParam(EXTTASK, NOTIFICATION);

				try {
				} catch (Throwable e) {
				}
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
					ArrayList<HashMap<String, String>> friendRequest = new ArrayList<HashMap<String, String>>();
					ArrayList<HashMap<String, String>> groupRequest = new ArrayList<HashMap<String, String>>();
					ArrayList<HashMap<String, String>> messageRequest = new ArrayList<HashMap<String, String>>();

					ArrayList<Object> result = new ArrayList<Object>();
					result.add(0, friendRequest);
					result.add(1, groupRequest);
					result.add(2, messageRequest);

					try {
						friendRequest = new IjoomerCaching(mContext).parseData(iw.getJsonObject().getJSONObject("notifications").getJSONArray("friends"));
					} catch (Throwable e) {
						e.printStackTrace();
					}
					try {
						JSONArray globalArr = iw.getJsonObject().getJSONObject("notifications").getJSONArray("global");
						int len = globalArr.length();
						for (int i = 0; i < len; i++) {
							groupRequest.add(new HashMap<String, String>(jsonToMap(globalArr.getJSONObject(i))));
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
					try {
						messageRequest = new IjoomerCaching(mContext).parseData(iw.getJsonObject().getJSONObject("notifications").getJSONArray("messages"));
					} catch (Throwable e) {
						e.printStackTrace();
					}

					try {

						if (friendRequest != null) {
							result.add(0, friendRequest);
						}
						if (groupRequest != null) {
							result.add(1, groupRequest);
						}
						if (messageRequest != null) {
							result.add(2, messageRequest);
						}

						return result;
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<Object> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
			}
		}.execute();
	}

	/**
	 * This method used to get friend request notification list.
	 * 
	 * @return ArrayList Object for notifications of friend request
	 */
	public ArrayList<HashMap<String, String>> getFriendNotifications() {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("friendrequest");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This method used to get group notification list.
	 * 
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> getGlobalNotifications() {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("global");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This method used to get message notification list.
	 * 
	 * @return {@link ArrayList<HashMap<String, String>>}
	 */
	public ArrayList<HashMap<String, String>> getMessageNotifications() {

		try {
			return new IjoomerCaching(mContext).getDataFromCache("messages");
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * This method used to approve friend request.
	 * 
	 * @param connectionID
	 *            represented requested used connection id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void approveFriendRequest(final String connectionID, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, FRIEND);
				iw.addWSParam(EXTTASK, APPROVEREQUEST);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(CONNECTIONID, connectionID);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	/**
	 * This method used to reject friend request.
	 * 
	 * @param connectionID
	 *            represented requested user connection id
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void rejectFriendRequest(final String connectionID, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, FRIEND);
				iw.addWSParam(EXTTASK, REJECTREQUEST);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(CONNECTIONID, connectionID);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);
				iw.WSCall(new ProgressListener() {

					@Override
					public void transferred(long num) {
						if (num >= 100) {
							target.onProgressUpdate(95);
						} else {
							target.onProgressUpdate((int) num);
						}
					}
				});

				if (validateResponse(iw.getJsonObject())) {
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
			}
		}.execute();
	}

	/**
	 * This method is used for mapping JSONObject
	 * 
	 * @param object
	 *            represented JSONObject
	 * @return {@link Map<String, String>}
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> jsonToMap(JSONObject object) throws JSONException {
		Map<String, String> map = new HashMap();
		Iterator keys = object.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			map.put(key, fromJson(object.get(key)).toString());
		}
		return map;
	}

	/**
	 * This method is used for converting JSONObject to object
	 * 
	 * @param json
	 * @return {@link Object}
	 * @throws JSONException
	 */
	private Object fromJson(Object json) throws JSONException {
		if (json == JSONObject.NULL) {
			return null;
		} else if (json instanceof JSONObject) {
			return jsonToMap((JSONObject) json);
		} else if (json instanceof JSONArray) {
			return toList((JSONArray) json);
		} else {
			return json;
		}
	}

	/**
	 * This method is used to converting JSONArray to List
	 * 
	 * @param array
	 *            represented JSONArray
	 * @return {@link List}
	 * @throws JSONException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List toList(JSONArray array) throws JSONException {
		List list = new ArrayList();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			list.add(fromJson(array.get(i)));
		}
		return list;
	}

}
