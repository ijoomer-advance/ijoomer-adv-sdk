package com.ijoomer.oauth;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerUsersDataProvider.
 * 
 * @author tasol
 */
public class IjoomerUsersDataProvider extends IjoomerPagingProvider {
	private Context mContext;

	private final String USER = "user";
	private final String WHOSONLINE = "whosOnline";

	/**
	 * Constructor
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public IjoomerUsersDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * This method used to get jomsocial currently online user list.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getOnlineUsers(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, USER);
				iw.addWSParam(EXTTASK, WHOSONLINE);

				JSONObject taskData = new JSONObject();
				try {
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
					return new IjoomerCaching(mContext).cacheData(iw.getJsonObject(), true, WHOSONLINE);
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
	 * This method used to get TermsNConditions.
	 * 
	 * @param requestObject
	 *            represented TermsNCondition request
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getTermsNCondition(final String requestObject, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				try {
					iw.addWSParam(new JSONObject(requestObject));
				} catch (JSONException e) {
					e.printStackTrace();
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
					return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
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

}
