package com.ijoomer.library.k2;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.components.k2.K2TagHolder;
import com.ijoomer.weservice.CacheCallListener;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To k2MainDataProvider.
 *
 * @author Tasol
 *
 */
public class k2MainDataProvider extends IjoomerPagingProvider implements K2TagHolder {

    private Context mContext;

    private boolean isCalling;

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public k2MainDataProvider(Context context) {
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
     * This method used to get categories.
     *
     * @param menuId
     *            represented menu id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getCategories(final String menuId, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<Object>>() {
                IjoomerWebService iw = new IjoomerWebService();

                @Override
                protected ArrayList<Object> doInBackground(Void... params) {

                    iw.reset();

                    iw.addWSParam(EXTNAME, K2);
                    iw.addWSParam(EXTVIEW, ITEMS);
                    iw.addWSParam(EXTTASK, ITEMS);

                    JSONObject taskData = new JSONObject();

                    try {
                        taskData.put(PAGENO, getPageNo());
                        taskData.put(MENUID, menuId);
                    } catch (Throwable e1) {
                        e1.printStackTrace();
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

                    ArrayList<Object> data = new ArrayList<Object>();
                    if (validateResponse(iw.getJsonObject())) {
                        try {
                            setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));

                            IjoomerCaching caching = new IjoomerCaching(mContext);
                            caching.setReqObject(iw.getWSParameter().toString());
                            caching.addExtraColumn(MENUID, menuId);
                            caching.addExtraColumn(PAGELIMIT, iw.getJsonObject().getString(PAGELIMIT));
                            try {
                                caching.cacheData(iw.getJsonObject().getJSONArray(MAINCATEGORIES), false, MAINCATEGORIES);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                caching.cacheData(iw.getJsonObject().getJSONArray(CATEGORIES), false, CATEGORIES);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                caching.cacheData(iw.getJsonObject().getJSONArray(ITEMS), false, ITEMS);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }

                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    return data;
                }

                @Override
                protected void onPostExecute(ArrayList<Object> result) {
                    super.onPostExecute(result);
                    isCalling = false;
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
                }
            }.execute();
        } else {
            isCalling = false;
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
            target.onProgressUpdate(100);
        }

    }

    public void getItemsDetail(final String menuId, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String,String>>>() {
            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String,String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, K2);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, ITEMS);

                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(MENUID, menuId);
                } catch (Throwable e1) {
                    e1.printStackTrace();
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

                        IjoomerCaching caching = new IjoomerCaching(mContext);
                        try {
                            return caching.parseData(iw.getJsonObject().getJSONArray(ITEMS));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String,String>> result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();

    }

    /**
     * This method used to add comment.
     *
     * @param itemId
     *            represented item id
     * @param menuId
     *            represented menu id
     * @param userName
     *            represented commented user name
     * @param commentText
     *            represented comment text
     * @param commentEmail
     *            represented comment email
     * @param commentUrl
     *            represented comment url
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrPostComment(final String itemId, final String menuId, final String userName, final String commentText, final String commentEmail, final String commentUrl, final WebCallListener target) {
        new AsyncTask<Void, Void, Object>() {

            @Override
            protected Object doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, K2);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, ADDCOMMENT);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(COMMENTURL, commentUrl);
                    taskData.put(COMMENTEMAIL, commentEmail);
                    taskData.put(COMMENTTEXT, commentText);
                    taskData.put(USERNAME, userName);
                    taskData.put(ITEMID, itemId);

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

                JSONArray commenListJson = new JSONArray();
                if (validateResponse(iw.getJsonObject())) {
                    ArrayList<HashMap<String, String>> commentList = getCommentArray(itemId, menuId);
                    try {
                        JSONArray tempJsonArray = new JSONArray(commentList.get(0).get(COMMENTS));
                        commenListJson.put(iw.getJsonObject());
                        for (int i = 0; i < tempJsonArray.length(); i++) {
                            commenListJson.put(tempJsonArray.getJSONObject(i));
                        }
                    } catch (Throwable e) {
                        commenListJson.put(iw.getJsonObject());
                    }
                    IjoomerCaching ic = new IjoomerCaching(mContext);
                    ic.updateTable("update " + ITEMS + " set " + COMMENTS + " = '" + commenListJson.toString() + "' where id = " + itemId + " and " + MENUID + " = " + menuId);

                }
                return commenListJson;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
            }
        }.execute();
    }


    /**
     * This method used to rating.
     * @param rating represented rating count
     * @param menuId represented menu id
     * @param itemId represented item id
     * @param target represented {@link WebCallListener}
     */
    public void rating(final String rating, final String menuId, final String itemId, final WebCallListener target) {
        new AsyncTask<Void, Void, Object>() {

            @Override
            protected Object doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, K2);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, ADDRATING);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(ITEMID, itemId);
                    taskData.put(USERRATING, rating);
                    taskData.put(ITEMID, itemId);

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
                    IjoomerCaching ic = new IjoomerCaching(mContext);
                    ic.updateTable("update " + ITEMS + " set " + RATINGS + " = '" + iw.getJsonObject().toString() + "' where id = " + itemId + " and " + MENUID + " = " + menuId);

                }
                return iw.getJsonObject();
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), null, result);
            }
        }.execute();
    }


    /**
     * This method used to get comment list.
     * @param itemId represented item id
     * @param menuId represented menu id
     * @return represented list {@link HashMap}
     */
    private ArrayList<HashMap<String, String>> getCommentArray(String itemId, String menuId) {
        IjoomerCaching ic = new IjoomerCaching(mContext);
        return ic.getDataFromCache(ITEMS, "select comments from " + ITEMS + " where id = " + itemId + " and " + MENUID + " = " + menuId);
    }

    /**
     * This method used to get mainCategories by name from database.
     * @param parentId represented parent id(optional - 0)
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getMainCategoryByName(final String parentId, final String menuId, final CacheCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    if (parentId.equals("0")) {
                        result = ic.getDataFromCache(MAINCATEGORIES, "select * from " + MAINCATEGORIES + " where " + MENUID + " = " + menuId + " order by " + NAME);
                    } else {
                        result = ic.getDataFromCache(MAINCATEGORIES, "select * from " + MAINCATEGORIES + " where parent = " + parentId + " and " + MENUID + " = " + menuId + " order by " + NAME);
                    }
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();
    }

    /**
     * This method used to get mainCategories by id from database.
     * @param parentId represented parent id(optional - 0)
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getMainCategoryById(final String parentId, final String menuId, final CacheCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    if (parentId.equals("0")) {
                        result = ic.getDataFromCache(MAINCATEGORIES, "select * from " + MAINCATEGORIES + " where " + MENUID + " = " + menuId + " order by " + ID);
                    } else {
                        result = ic.getDataFromCache(MAINCATEGORIES, "select * from " + MAINCATEGORIES + " where parent = " + parentId + " and " + MENUID + " = " + menuId + " order by " + ID);
                    }
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();
    }

    /**
     * This method used to get sub categories by name from database.
     * @param parentCategoryID represented parent category id
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getSubCategoryByName(final String parentCategoryID, final String menuId, final CacheCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    result = ic.getDataFromCache(CATEGORIES, "select * from " + CATEGORIES + " where parent = " + parentCategoryID + " and " + MENUID + " = " + menuId + " order by " + NAME);
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();
    }

    /**
     * This method used to get sub categories by id from database.
     * @param parentCategoryID represented parent category id
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getSubCategoryById(final String parentCategoryID, final String menuId, final CacheCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    result = ic.getDataFromCache(CATEGORIES, "select * from " + CATEGORIES + " where parent = " + parentCategoryID + " and " + MENUID + " = " + menuId + " order by " + ID);
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();

    }


    /**
     * This method used to get category details from database.
     * @param categoryId represented category id
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getCategoryDetail(final String categoryId, final String menuId, final CacheCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    result = ic.getDataFromCache(CATEGORIES, "select * from " + CATEGORIES + " where id = " + categoryId + " and " + MENUID + " = " + menuId );
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();
    }


    /**
     * This method used to get item details from database.
     * @param itemId represented item id
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getItemDetail(final String itemId, final String menuId, final CacheCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    result = ic.getDataFromCache(ITEMS, "select * from " + ITEMS + " where id = " + itemId + " and " + MENUID + " = " + menuId);
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();
    }

    /**
     * This method used to get item by name from database.
     * @param categoryID represented category id
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getItemByName(final String categoryID, final String menuId, final CacheCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    if (categoryID.equals("0")) {
                        result = ic.getDataFromCache(ITEMS, "select * from " + ITEMS + " where " + MENUID + " = " + menuId + " order by " + TITLE);
                    } else {
                        result = ic.getDataFromCache(ITEMS, "select * from " + ITEMS + " where catid = " + categoryID + " and " + MENUID + " = " + menuId + " order by " + TITLE);
                    }
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();
    }


    /**
     * This method used to get item by id from database.
     * @param categoryID represented category id
     * @param menuId represented menu id
     * @param target represented {@link CacheCallListener}
     */
    public void getItemById(final String categoryID, final String menuId, final CacheCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerCaching ic = new IjoomerCaching(mContext);
                ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
                try {
                    if (categoryID.equals("0")) {
                        result = ic.getDataFromCache(ITEMS, "select * from " + ITEMS + " where " + MENUID + " = " + menuId + " order by " + ID);
                    } else {
                        result = ic.getDataFromCache(ITEMS, "select * from " + ITEMS + " where catid = " + categoryID + " and " + MENUID + " = " + menuId + " order by " + ID);
                    }
                } catch (Throwable e) {
                }
                return result;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (result != null && result.size() > 0) {
                    target.onCallComplete(200, getErrorMessage(), result, null);
                    target.onProgressUpdate(100);
                } else {
                    target.onCallComplete(-1, getErrorMessage(), result, null);
                }
            }
        }.execute();
    }

}
