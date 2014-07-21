package com.ijoomer.library.easyblog;

import android.app.Activity;
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
 * This Class Contains All Method Related To EasyBlogEntryDetailDataProvider.
 *
 * @author tasol
 *
 */
public class EasyBlogEntryDetailDataProvider extends IjoomerPagingProvider {
    private Context mContext;

    private final String EASYBLOGDETAILTABLENAME = "easyblogdetail";
    private final String GETITEMDETAIL = "getItemDetail";
    private final String ADDNEWCOMMENT = "addNewComment";
    private final String ADDNEWRATING = "addNewRating";
    private final String GETITEMCOMMENTS = "getItemComments";
    private final String BLOGID = "blogID";
    private final String COMMENT = "comment";
    private final String RATEVALUE = "rateValue";
    private final String ITEMS = "items";
    private ArrayList<HashMap<String, String>> data1;
    private boolean isCalling = false;

    /**
     * Constructor
     *
     * @param context
     *            represented {@link android.content.Context}
     */
    public EasyBlogEntryDetailDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    public boolean isCalling() {
        return isCalling;
    }

    /***
     * This method is used to get blog Detail
     *
     * @param id
     *            represented blog id
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getBlogDetail(final String id, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, EASYBLOG);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, GETITEMDETAIL);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(BLOGID, id);
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);

                if (IjoomerApplicationConfiguration.isCachEnable) {
                    data1 = new ArrayList<HashMap<String, String>>();
                    try {

                        data1 = new IjoomerCaching(mContext).getDataFromCache(EASYBLOGDETAILTABLENAME, "select * from '" + EASYBLOGDETAILTABLENAME + "' where reqObject='" + iw.getWSParameter() + "'");
                        if (data1 != null && data1.size() > 0) {
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    target.onProgressUpdate(100);
                                    target.onCallComplete(200, "", data1, null);

                                }
                            });

                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
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
                    try {
                        iw.getJsonObject().remove("code");
                        if (IjoomerApplicationConfiguration.isCachEnable) {
                            IjoomerCaching caching = new IjoomerCaching(mContext);
                            caching.setReqObject(iw.getWSParameter().toString());
                            return caching.cacheData(iw.getJsonObject(), false, EASYBLOGDETAILTABLENAME);
                        } else {
                            return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
                        }
                    } catch (Throwable e) {
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);

                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
                target.onProgressUpdate(100);

            }
        }.execute();
    }

    /**
     * This method used to add comment.
     * @param id represented blog id
     * @param comment represented comment
     * @param target represented {@link WebCallListener}
     */
    public void addComment(final String id,final String comment, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, EASYBLOG);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, ADDNEWCOMMENT);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(BLOGID, id);
                    taskData.put(COMMENT, comment);
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
                    } catch (Throwable e) {
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);

                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
                target.onProgressUpdate(100);

            }
        }.execute();
    }

    /**
     * This method used to add addRating.
     * @param id represented blog id
     * @param rate represented addRating
     * @param target represented {@link WebCallListener}
     */
    public void addRating(final String id, final String rate, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, EASYBLOG);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, ADDNEWRATING);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(BLOGID, id);
                    taskData.put(RATEVALUE, rate);
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
                    } catch (Throwable e) {
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);

                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
                target.onProgressUpdate(100);

            }
        }.execute();
    }

    /**
     * This method used to get blog comment.
     * @param id represented blog id
     * @param target represented {@link WebCallListener}
     */
    public void getBlogComment(final String id, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, EASYBLOG);
                    iw.addWSParam(EXTVIEW, ITEMS);
                    iw.addWSParam(EXTTASK, GETITEMCOMMENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(BLOGID, id);
                        taskData.put(PAGENO, getPageNo());
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
                            IjoomerCaching caching = new IjoomerCaching(mContext);
                            return caching.parseData(iw.getJsonObject());
                        } catch (Throwable e) {
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                    super.onPostExecute(result);
                    isCalling = false;
                    target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
                    target.onProgressUpdate(100);

                }
            }.execute();
        } else {
            target.onProgressUpdate(100);
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
        }
    }
}
