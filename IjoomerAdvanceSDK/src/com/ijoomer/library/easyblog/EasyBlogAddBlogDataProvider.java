package com.ijoomer.library.easyblog;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To EasyBlogAddBlogDataProvider.
 *
 * @author tasol
 *
 */

public class EasyBlogAddBlogDataProvider extends IjoomerPagingProvider {

    private Context mContext;
    private final String ADDBLOGFIELD = "addBlogField";
    private final String FORM = "form";
    private final String VALUE = "value";
    private final String FIELDS = "fields";
    private final String NAME = "name";
    private final String BLOGID = "blogID";
    private static String imagerPath = "";
    private static String ITEMS = "items";
    private static String IMAGE = "image";

    /**
     * Constructor
     *
     * @param mContext
     *            represented {@link android.content.Context}
     */

    public EasyBlogAddBlogDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    /**
     * This method provide image path
     *
     * @return {@link String}
     */
    public static String getImagerPath() {
        return imagerPath;
    }

    /**
     * This method is used to add new blog
     *
     * @param entryFields
     *            represented blog fields
     * @param target
     *            {@link WebCallListener}
     */
    public void addBlog(final String blogId,final ArrayList<HashMap<String, String>> entryFields, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, EASYBLOG);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, ADDBLOGFIELD);

                JSONObject taskData = new JSONObject();
                ArrayList<HashMap<String, String>> filePath = new ArrayList<HashMap<String, String>>();
                try {
                    if(!blogId.equals("0")){
                        taskData.put(BLOGID, blogId);
                    }
                    taskData.put(FORM, "0");

                    JSONArray ja = new JSONArray();
                    for (HashMap<String, String> hashMap : entryFields) {
                        if (hashMap.containsKey(IMAGE)) {
                            HashMap<String, String> data = new HashMap<String, String>();
                            if (hashMap.get(IMAGE) != null && hashMap.get(IMAGE).length() > 0) {
                                data.put(hashMap.get(NAME), hashMap.get(IMAGE));
                                filePath.add(data);
                            }
                        } else {
                            if (hashMap.get(VALUE) != null && hashMap.get(VALUE).length() > 0) {
                                JSONObject value = new JSONObject();
                                value.put(hashMap.get(NAME), hashMap.get(VALUE));
                                ja.put(value);
                            }
                        }
                    }
                    taskData.put(FIELDS, ja);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                iw.addWSParam(TASKDATA, taskData);
                if (filePath.size() > 0) {
                    iw.WSCall(filePath, new ProgressListener() {

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

    /**
     * This Method is used to get dynamic fields for add blog.
     *
     * @param blogId
     *            represented blog id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getBlogField(final String blogId,final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();

                iw.addWSParam(EXTNAME, EASYBLOG);
                iw.addWSParam(EXTVIEW, ITEMS);
                iw.addWSParam(EXTTASK, ADDBLOGFIELD);

                JSONObject taskData = new JSONObject();
                try {
                    if(!blogId.equals("0")){
                        taskData.put(BLOGID, blogId);
                    }
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
