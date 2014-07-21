package com.ijoomer.library.plugins;

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
 * This Class Contains All Method Related To PluginsFriendsDataProvider.
 *
 * @author tasol
 *
 */
public class PluginsFriendsDataProvider extends IjoomerPagingProvider {
    private Context mContext;

    private final String FRIEND = "friend";
    private final String FRIENDS = "friends";

    private boolean isCalling = false;

    /**
     * This method used to check provider execute any request call.
     *
     * @return {@link boolean}
     */
    public boolean isCalling() {
        return isCalling;
    }

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public PluginsFriendsDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to get friend list.
     *
     * @param userId
     *            represented user id (0 - for login user)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getFriendsList(final String userId, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, FRIEND);
                    iw.addWSParam(EXTTASK, FRIENDS);

                    JSONObject taskData = new JSONObject();
                    try {
                        if (!userId.equals("0")) {
                            taskData.put(USERID, userId);
                        }
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

                    if (validateResponse(iw.getJsonObject())) {
                        try {
                            setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                            return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
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

    /**
     * This method used to get friend list from database.
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getFriendFromDB() {
        return new IjoomerCaching(mContext).getDataFromCache(FRIENDS, "select * from " + FRIENDS + " order by " + USER_NAME);
    }

}
