package com.ijoomer.library.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

/**
 * This Class Contains All Method Related To JomAdvancedSearchDataProvider.
 *
 * @author tasol
 *
 */


public class JomAdvancedSearchDataProvider extends IjoomerPagingProvider {
	private Context mContext;

	private final String OPERATOR = "operator";
	private final String AVATARONLY = "avatarOnly";

	private final String USER = "user";
	private final String ADVANCESEARCH = "advanceSearch";
	private final String FORM = "form";
	private final String ADVANCESEARCHDATA = "advanceSearchData";
	private final String FIELDID = "fieldid";
	private final String FIELD = "field";
	private final String CONDITION = "condition";
	private final String FIELDTYPE = "fieldType";
	private final String VALUE = "value";

	private boolean isCalling = false;

	/**
	 * Constructor
	 *
	 * @param context
	 *            represented {@link Context}
	 */

	public JomAdvancedSearchDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * This method used to check provider execute any request call.
	 *
	 * @return {@link boolean}
	 */
	public boolean isCalling() {
		return isCalling;
	}

	/**
	 * This method is used get list of all the criteria for advance search
	 *
	 * @param target
	 *         represents {@link WebCallListener}
	 */


	public void getAdvanceSearchData(final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(EXTNAME, JOMSOCIAL);
				iw.addWSParam(EXTVIEW, USER);
				iw.addWSParam(EXTTASK, ADVANCESEARCH);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(FORM, "1");
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
					return new IjoomerCaching(mContext).cacheData(iw.getJsonObject(), true,ADVANCESEARCHDATA);
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
	 * This method is used get result of the criteria selected for advance search
	 *
	 * @param matchCriteria
	 *            represents match all criteria or match any criteria
	 *
	 * @param avatarOnly
	 *            represents member with profile picture or member without profile pic
	 * @param searchParams
	 * 			represents the criteria which user has selected for search
	 * @param target
	 *            represents {@link WebCallListener}
	 */

	public void advanceSearchPostData(final String matchCriteria, final String avatarOnly ,final ArrayList<HashMap<String, String>> searchParams,final WebCallListener target) {
		if (hasNextPage()) {
			isCalling = true;
			new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

				@Override
				protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
					IjoomerWebService iw = new IjoomerWebService();
					iw.reset();
					iw.addWSParam(EXTNAME, JOMSOCIAL);
					iw.addWSParam(EXTVIEW, USER);
					iw.addWSParam(EXTTASK, ADVANCESEARCH);

					JSONObject taskData = new JSONObject();
					JSONObject jaData = new JSONObject();

					try {
						taskData.put(OPERATOR,matchCriteria);
						taskData.put(AVATARONLY,avatarOnly);
						taskData.put(FORM, "0");

						JSONArray ja = new JSONArray();
						for (HashMap<String, String> param : searchParams) {

							jaData.put("fieldid", param.get(FIELDID));
							jaData.put("field", param.get(FIELD));
							jaData.put("condition", param.get(CONDITION));
							jaData.put("fieldType", param.get(FIELDTYPE));
							String multipleValues[] = param.get(VALUE).split(",");

							if(multipleValues.length>1){
								JSONArray values  = new JSONArray();
								for (String string : multipleValues) {
									values.put(string);
								}
								jaData.put("value", values);
							}else{
								jaData.put("value", param.get(VALUE));
							}


						}
						ja.put(jaData);

						taskData.put("formData",ja);
						taskData.put(PAGENO, "" + getPageNo());
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

					IjoomerCaching ic = new IjoomerCaching(mContext);
					if (validateResponse(iw.getJsonObject())) {
						try {
							setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
							iw.getJsonObject().remove(PAGELIMIT);
							iw.getJsonObject().remove(TOTAL);
							return ic.parseData(iw.getJsonObject().getJSONArray("member"));
						} catch (Throwable e) {
						}
					}
					return null;
				}

				@Override
				protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
					super.onPostExecute(result);
					isCalling = false;
					target.onProgressUpdate(100);
					target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				}
			}.execute();
		} else {
			target.onProgressUpdate(100);
			target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
		}
	}

}