package com.ijoomer.library.jomsocial;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomReportVoiceDataProvider.
 *
 * @author tasol
 *
 */
public class JomReportVoiceDataProvider extends IjoomerPagingProvider {

    @SuppressWarnings("unused")
    private Context mContext;

    private final String WALL = "wall";
    private final String REPORT = "report";
    private final String KEY = "key";
    private final String MESSAGE = "message";


    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public JomReportVoiceDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to report group or group discussion.
     *
     * @param groupID
     *            represented group id
     * @param discussionID
     *            represented group discussion id(0- for group
     *            report,discussionID- for group discussion)
     * @param message
     *            represented report message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void reportGroupOrDiscussion(final String voicePath, final String message, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, REPORT);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(KEY, voicePath);
                    taskData.put(MESSAGE, message);
                } catch (Throwable e) {
                    e.printStackTrace();
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

}
