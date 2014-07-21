package com.ijoomer.library.jomsocial;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomWallDataProvider.
 *
 * @author tasol
 *
 */
public class JomWallDataProvider extends IjoomerPagingProvider {
    private Context mContext;

    private final String WALL = "wall";
    private final String ADD = "add";
    private final String UNIQUEID = "uniqueID";
    private final String MESSAGE = "message";
    private final String LIKE = "like";
    private final String REMOVE = "remove";
    private final String WALLID = "wallID";
    private final String UNLIKE = "unlike";
    private final String PRIVACY = "privacy";
    private final String GETCOMMENTS = "getComments";
    private final String GETLIKES = "getLikes";
    private final String COMMENT = "comment";
    private final String TYPE = "type";

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
    public JomWallDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to get activities list.
     *
     * @param target
     */
    public void getWallList(final String uniqueID, final String type, final WebCallListenerWithCacheInfo target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {
                IjoomerWebService iw = new IjoomerWebService();

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, WALL);
                    iw.addWSParam(EXTTASK, WALL);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(PAGENO, "" + getPageNo());
                        taskData.put(TYPE, type);
                        if (type.equals("wall") && !uniqueID.equals("0")) {
                            taskData.put(USERID, uniqueID);
                        } else if (type.equals("event") || type.equals("group") && !uniqueID.equals("0")) {
                            taskData.put(UNIQUEID, uniqueID);
                        }
                    } catch (Throwable e) {
                    }
                    iw.addWSParam(TASKDATA, taskData);

                    // data from cache
                    try {
                        IjoomerCaching caching = new IjoomerCaching(mContext);
                        caching.setReqObject(iw.getWSParameter().toString());
                        JSONObject cacheObject = new JSONObject(caching.getCachedRowData());
                        if (validateResponse(cacheObject)) {
                            final int pagelimit = Integer.parseInt(cacheObject.getString(PAGELIMIT));
                            final int pageno = getPageNo();
                            cacheObject.remove(PAGELIMIT);
                            cacheObject.remove(TOTAL);
                            final ArrayList<HashMap<String, String>> dataFromCache = caching.parseData(cacheObject);
                            ((Activity) mContext).runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    target.onProgressUpdate(100);
                                    target.onCallComplete(200, "", dataFromCache, null, pageno, pagelimit, true);
                                }
                            });
                        }
                    } catch (Exception e) {
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
                            setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);

                            // cache Data
                            IjoomerCaching cacheRowData = new IjoomerCaching(mContext);
                            cacheRowData.setReqObject(iw.getWSParameter().toString());
                            cacheRowData.cacheRowData(iw.getResponse());
                            //
                            return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
                        } catch (Throwable e) {
                        }
                    }else{
                    	setHasNextPage(false);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                    super.onPostExecute(result);
                    isCalling = false;
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), result, null, getPageNo() - 1, getPageLimit(), false);
                }
            }.execute();
        } else {
            target.onProgressUpdate(100);
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null, 0, 0, false);
        }

    }

    /**
     * This method is used to add wall
     *
     * @param userId
     *            represented user id
     * @param postMessage
     *            represented wall message
     * @param privacy
     *            represented privacy/access
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrPostOnWall(final String userId, final String postMessage, final String privacy, final String voiceFilePath, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {
            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, ADD);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(MESSAGE, postMessage);
                    taskData.put(PRIVACY, privacy);
                    taskData.put(COMMENT, "0");
                    if (!userId.equals("0")) {
                        taskData.put(UNIQUEID, userId);
                    }
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);

                if (voiceFilePath != null) {
                    iw.WSCall(voiceFilePath, new ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            if (num >= 100) {
                                target.onProgressUpdate(95);
                            } else {
                                target.onProgressUpdate((int) num);
                            }
                        }
                    });
                } else {
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
                }

                if (validateResponse(iw.getJsonObject())) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method is used to add activity
     *
     * @param postMessage
     *            represented post message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrPostOnActivities(final String postMessage, final String voiceFilePath, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, ADD);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(MESSAGE, postMessage);
                    taskData.put(COMMENT, "0");
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);

                if (voiceFilePath != null) {
                    iw.WSCall(voiceFilePath, new ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            if (num >= 100) {
                                target.onProgressUpdate(95);
                            } else {
                                target.onProgressUpdate((int) num);
                            }
                        }
                    });
                } else {
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
                }

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
     * This method is used to like wall
     *
     * @param wallId
     *            represented wall id
     * @param type
     *            represented type(user wall,group wall,event wall)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void likeWall(final String wallId, final String type, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, LIKE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(WALLID, wallId);
                    taskData.put(TYPE, type);
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
     * This method is used to remove wall
     *
     * @param wallId
     *            represented wall id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeWall(final String wallId, final String userId, final String type, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, REMOVE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, wallId);
                    taskData.put(USERID, userId);
                    taskData.put(TYPE, type);
                    taskData.put(COMMENT, "0");
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
     * This method is used to remove comment
     *
     * @param commentID
     *            represented comment id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeComment(final String commentID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, REMOVE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, commentID);
                    taskData.put(COMMENT, "1");
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
     * This method is used to unlike wall
     *
     * @param wallId
     *            represented wall id
     * @param type
     *            represented wall type (user wall,group wall,event wall)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void unlikeWall(final String wallId, final String type, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, UNLIKE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(WALLID, wallId);
                    taskData.put(TYPE, type);
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
     * This method is used to get comment list for selected wall
     *
     * @param wallId
     *            represented wall id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getCommentList(final String wallId, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, WALL);
                    iw.addWSParam(EXTTASK, GETCOMMENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(PAGENO, "" + getPageNo());
                        taskData.put(WALLID, wallId);
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
     * This method is used to get like list for selected wall
     *
     * @param wallId
     *            represented wall id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getLikeList(final String wallId, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, WALL);
                    iw.addWSParam(EXTTASK, GETLIKES);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(PAGENO, "" + getPageNo());
                        taskData.put(WALLID, wallId);
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
     * This method is used to add comment in wall
     *
     * @param wallId
     *            represented wall id
     * @param comment
     *            represented comment message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void writeComment(final String wallId, final String comment, final String voiceFilePath, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, WALL);
                iw.addWSParam(EXTTASK, ADD);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(MESSAGE, comment);
                    taskData.put(PRIVACY, "0");
                    taskData.put(COMMENT, "1");
                    taskData.put(UNIQUEID, wallId);
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);

                if (voiceFilePath != null) {
                    iw.WSCall(voiceFilePath, new ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            if (num >= 100) {
                                target.onProgressUpdate(95);
                            } else {
                                target.onProgressUpdate((int) num);
                            }
                        }
                    });
                } else {
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
                }

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
