package com.ijoomer.library.plugins;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.common.classes.RssFeed;
import com.ijoomer.common.classes.RssItem;
import com.ijoomer.common.classes.RssReader;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class PluginsVimeoDataProvider extends IjoomerPagingProvider{
    private Context mContext;
    private ArrayList<HashMap<String, String>> data1;
    private String VIMEOPLAYLIST = "vimeoPlayList";
    private boolean isCalling = false;
    private String DESCRIPTION = "description";
    private String LINK= "link";
    private String TITLE = "title";
    private String reqObject;
    private JSONObject json = null;
    private JSONArray jArray = null;
    private JSONObject jSonObject = null;

    public boolean isCalling() {
        return isCalling;
    }

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public PluginsVimeoDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to get Vimeo videos.
     *
     * @param channelUrl
     *            represents url of channel
     * @param target
     *            represented {@link WebCallListenerWithCacheInfo}
     */
    public void getVimeoVideos(final String channelUrl, final WebCallListenerWithCacheInfo target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {
                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                    URL url = null;
                    try {
                        url = new URL(channelUrl.trim());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (IjoomerApplicationConfiguration.isCachEnable) {
                        data1 = new ArrayList<HashMap<String, String>>();
                        try {
                            data1 = new IjoomerCaching(mContext).getDataFromCache(VIMEOPLAYLIST, "select * from " + VIMEOPLAYLIST);
                            if (data1 != null && data1.size() > 1) {
                                ((Activity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        target.onProgressUpdate(100);
                                        target.onCallComplete(getResponseCode(), getErrorMessage(), data1, null, 0, 0, false);
                                    }
                                });
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                    RssFeed feed = null;
                    try {
                        feed = RssReader.read(url);
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    data1 = new ArrayList<HashMap<String,String>>();
                    jArray = new JSONArray();
                    if(feed.getRssItems()!=null && feed.getRssItems().size()>0){
                        ArrayList<RssItem> rssItems = feed.getRssItems();
                        for (RssItem rss : rssItems) {
                            jSonObject = new JSONObject();
                            try {
                                jSonObject.put(DESCRIPTION,rss.getDescription());
                                jSonObject.put(LINK,rss.getLink());
                                jSonObject.put(TITLE,rss.getTitle());
                                jArray.put(jSonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (IjoomerApplicationConfiguration.isCachEnable) {
                        IjoomerCaching caching = new IjoomerCaching(mContext);
                        caching.setReqObject(channelUrl.trim());
                        try {
                            caching.cacheData(jArray, false, VIMEOPLAYLIST);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).getDataFromCache(VIMEOPLAYLIST, "select * from " + VIMEOPLAYLIST);
                    } else {
                        try {
                            return new IjoomerCaching(mContext).parseData(jArray);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                    super.onPostExecute(result);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), result, null, 0, 0, false);
                    target.onProgressUpdate(100);
                    isCalling = false;
                }
            }.execute();
        } else {
            isCalling = false;
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null, 0, 0, false);
            target.onProgressUpdate(100);
        }
    }

    /**
     * This method used to get Vimeo Channels.
     *
     * @param userID
     *            represents user id
     * @param target
     *            represented {@link WebCallListenerWithCacheInfo}
     */
    public void getVimeoChannel(final String userID, final WebCallListenerWithCacheInfo target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {
            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                reqObject = "http://vimeo.com/api/v2/"+userID+"/channels.json";
                JSONArray jsonArray = getJSONFromUrl(reqObject);
                try {
                    json = jsonArray.getJSONObject(0);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                target.onCallComplete(getResponseCode(), getErrorMessage(), null, json, 0, 0, false);
                target.onProgressUpdate(100);
                isCalling = false;
            }
        }.execute();
    }

    /**
     * This method used to get json from url.
     *
     * @param url
     *            represented url
     * @return represented {@link JSONObject}
     */
    public JSONArray getJSONFromUrl(String url) {

        JSONArray jArray = null;
        String line = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            final HttpGet request = new HttpGet(url.toString());
            final org.apache.http.HttpResponse response = httpClient.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new HttpResponseException(statusCode, "HTTP Error");
            }
            final HttpEntity entity = response.getEntity();
            line = EntityUtils.toString(entity, HTTP.UTF_8);
            jArray = new JSONArray(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jArray;

    }


}
