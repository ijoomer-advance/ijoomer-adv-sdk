package com.ijoomer.library.plugins;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To PluginsContactUsDataProvider.
 *
 * @author tasol
 *
 */

public class PluginsContactUsDataProvider extends IjoomerPagingProvider {

    private Context mContext;

    private String CONTACTUS = "contactUs";
    private String CONTACTID = "toID";
    private String CONTACTEMAIL = "email";
    private String CONTACTNAME = "name";
    private String FORM = "form";
    private String MENU_ITEM_ID = "menuID";
    private String CONTACTSUBJECT = "subject";
    private String CONTACTMESSAGE = "message";

    /**
     * Constructor
     *
     * @param mContext
     *            represented {@link Context}
     */
    public PluginsContactUsDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }


    /**
     * This method used to send contact info.
     * @param form represented form (0-submit contact,1-get contact )
     * @param menuID represented menu id
     * @param id represented id
     * @param name represented name
     * @param email represented email
     * @param subject represented subject
     * @param message represented message
     * @param target represented {@link WebCallListener}
     */
    public void sendContact(final String form, final String menuID, final String id, final String name, final String email, final String subject, final String message,
                            final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(TASK, CONTACTUS);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(CONTACTID, id);
                    taskData.put(FORM, form);
                    taskData.put(MENU_ITEM_ID, menuID);
                    taskData.put(CONTACTNAME, name);
                    taskData.put(CONTACTEMAIL, email);
                    taskData.put(CONTACTSUBJECT, subject);
                    taskData.put(CONTACTMESSAGE, message);
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
                target.onProgressUpdate(100);
            };

        }.execute();

    }


    /**
     * This method used to get contact info.
     * @param form represented form (0-submit contact,1-get contact )
     * @param id represented id
     * @param menuID represented menu id
     * @param target represented {@link WebCallListener}
     */
    public void getContactInfo(final String form, final String id, final String menuID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {
            @Override
            protected void onPreExecute() {

                if (IjoomerApplicationConfiguration.isCachEnable) {
                    ArrayList<HashMap<String, String>> data1 = new ArrayList<HashMap<String, String>>();
                    try {

                        data1 = new IjoomerCaching(mContext).getDataFromCache(CONTACTUS, "select * from '" + CONTACTUS + "'");
                        if (data1 != null && data1.size() > 0) {
                            target.onProgressUpdate(100);
                            target.onCallComplete(200, "", data1, null);

                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            };

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(TASK, CONTACTUS);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(CONTACTID, id);
                    taskData.put(FORM, form);
                    taskData.put(MENU_ITEM_ID, menuID);

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
                    try {
                        if (IjoomerApplicationConfiguration.isCachEnable()) {
                            new IjoomerCaching(mContext).cacheData(iw.getJsonObject().getJSONObject("contact"), false, CONTACTUS);
                            return new IjoomerCaching(mContext).getDataFromCache(CONTACTUS, "select * from '" + CONTACTUS + "'");
                        } else {

                            return new IjoomerCaching(mContext).parseData(iw.getJsonObject().getJSONObject("contact"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
                target.onProgressUpdate(100);
            };

        }.execute();

    }

}
