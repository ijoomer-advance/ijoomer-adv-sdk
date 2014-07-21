package com.ijoomer.library.sobipro;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To
 * SobiproAdvanceSearchFieldsDataProvider.
 *
 * @author tasol
 *
 */

public class SobiproAdvanceSearchFieldsDataProvider extends IjoomerPagingProvider {

    private Context mContext;
    private final String SEARCH_FIELDS = "isobipro";
    private final String GET_SEARCH_FIELDS = "getsearchField";
    private final String SECTION_ID = "sectionID";
    private final String FORM = "form";

    /**
     * Constructor
     *
     * @param mContext
     *            represented {@link Context}
     */

    public SobiproAdvanceSearchFieldsDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    /**
     * This Method is used to get dynamic fields for search.
     *
     * @param sid
     *            represented section id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getAdvanceSearchFields(final String sid, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, SOBIPRO);
                iw.addWSParam(EXTVIEW, SEARCH_FIELDS);
                iw.addWSParam(EXTTASK, GET_SEARCH_FIELDS);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(SECTION_ID, sid);
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
                    try {
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
