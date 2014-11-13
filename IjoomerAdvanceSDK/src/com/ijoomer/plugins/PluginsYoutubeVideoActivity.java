package com.ijoomer.plugins;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.plugins.PluginsYoutubeDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains UnNormalizeFields PluginsYoutubeVideoActivity.
 * 
 * @author tasol
 * 
 */
public class PluginsYoutubeVideoActivity extends PluginsMasterActivity {

	private ListView lstVideo;
	private View listFooter;
	private AQuery aQuery;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private PluginsYoutubeDataProvider youtubeDataProvider = new PluginsYoutubeDataProvider(this);
	private String IN_PLAYLIST_ID;
	private ArrayList<String> ID_ARRAY;

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.plugins_youtube_video;
	}

	@Override
	public void initComponents() {
		lstVideo = (ListView) findViewById(R.id.lstYoutubeVideo);
		listFooter = LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstVideo.addFooterView(listFooter);
		ID_ARRAY = new ArrayList<String>();
		aQuery = new AQuery(PluginsYoutubeVideoActivity.this);
		getIntentData();

	}

	@Override
	public void prepareViews() {
		getYoutubeVideos();
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());

	}

	@Override
	public void setActionListeners() {
		lstVideo.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {
					if (!youtubeDataProvider.isCalling() && youtubeDataProvider.hasNextPage()) {
						listFooterVisible();
						youtubeDataProvider.getYoutubeVideos(IN_PLAYLIST_ID, new WebCallListenerWithCacheInfo() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit,
									boolean fromCache) {
								listFooterInvisible();
								if (data1 != null && data1.size() > 0) {
									prepareList(data1, true, fromCache, pageNo, pageLimit);
								}
							}
						});

					}
				}
			}
		});
		lstVideo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				try {
					loadNew(PluginsYoutubeMediaPlayer.class, PluginsYoutubeVideoActivity.this, false, "IN_ID_ARRAY", ID_ARRAY, "IN_INDEX", pos);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to visible list footer
	 */
	public void listFooterVisible() {
		listFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * This method used to gone list footer
	 */
	public void listFooterInvisible() {
		listFooter.setVisibility(View.GONE);
	}

	/**
	 * This method used to get youtube videos list.
	 */
	public void getYoutubeVideos() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		Log.e("PLaylist_id", IN_PLAYLIST_ID);
		youtubeDataProvider.getYoutubeVideos(IN_PLAYLIST_ID, new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
				if (data1 != null && data1.size() > 0) {
					prepareList(data1, false, fromCache, pageNo, pageLimit);
					listAdapterWithHolder = getListAdapter(listData);
					lstVideo.setAdapter(listAdapterWithHolder);
				} else if (responseCode == 599) {
					IjoomerUtilities.getCustomOkDialog(getScreenCaption(), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
							getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
								}
							});
				} else {
					IjoomerUtilities.getCustomOkDialog(getScreenCaption(), getString(getResources().getIdentifier("code" + 204, "string", getPackageName())),
							getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
								}
							});
				}

			}
		});
	}

	/**
	 * This method used to get video data.
	 * 
	 * @param value
	 *            represented video data
	 * @return represented {@link HashMap} list
	 */
	public HashMap<String, String> getVideodata(HashMap<String, String> value) {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(value.get("video"));
		} catch (JSONException e) {

			e.printStackTrace();
		}

		ArrayList<HashMap<String, String>> videoDataArray = new IjoomerCaching(PluginsYoutubeVideoActivity.this).parseData(jsonObject);
		return videoDataArray.get(0);

	}

	/**
	 * This method used to convert seconds to minutes.
	 * 
	 * @param duration
	 *            represented duration
	 * @return represented {@link String}
	 */
	public String convertSecondsToMinutes(String duration) {
		String minutes = new String();
		minutes = "" + Integer.parseInt(duration) / 60;
		int seconds = Integer.parseInt(duration) % 60;
		minutes = (seconds > 9) ? minutes + ":" + seconds : minutes + ":0" + seconds;
		return minutes;
	}

	/**
	 * This method used to get intent data.
	 */
	public void getIntentData() {
		IN_PLAYLIST_ID = getIntent().getStringExtra("IN_PLAYLIST_ID");

	}

	/**
	 * This method used to prepare list youtube videos.
	 * 
	 * @param data
	 *            represented video data
	 * @param append
	 *            represented data append
	 * @param fromCache
	 *            represented data from cache
	 * @param pageno
	 *            represented page no
	 * @param pagelimit
	 *            represented page limit
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append, boolean fromCache, int pageno, int pagelimit) {

		if (data != null) {

			if (!append) {
				listData.clear();
				ID_ARRAY.clear();
			} else {

				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = listAdapterWithHolder.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								listAdapterWithHolder.remove(listAdapterWithHolder.getItem(i));
								listData.remove(i);
								ID_ARRAY.remove(i);
							} catch (Exception e) {
							}
						}
					}
				}

			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.plugins_youtube_video_listitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolder.add(item);
					hashMap = getVideodata(hashMap);
					ID_ARRAY.add(hashMap.get(ID));
				} else {

					listData.add(item);
					hashMap = getVideodata(hashMap);
					ID_ARRAY.add(hashMap.get(ID));

				}
			}

		}

	}

	/**
	 * List adapter for youtube videos.
	 * 
	 * @param listData
	 *            represented video data
	 * @returna represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.plugins_youtube_video_listitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.imgVideoThumb = (ImageView) v.findViewById(R.id.imgVideoThumb);
				holder.txtVideoDuration = (IjoomerTextView) v.findViewById(R.id.txtVideoDuration);
				holder.txtVideoTitle = (IjoomerTextView) v.findViewById(R.id.txtVideoTitle);
				holder.txtVideoViews = (IjoomerTextView) v.findViewById(R.id.txtVideoViews);
				holder.txtVideoDescription = (IjoomerTextView) v.findViewById(R.id.txtVideoDescription);

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

				value = getVideodata(value);
				if (value.containsKey(SQDEFAULT))
					aQuery.id(holder.imgVideoThumb).image(value.get(SQDEFAULT), true, true, getDeviceWidth(), R.drawable.plugins_default_video_thumb);
				else
					aQuery.id(holder.imgVideoThumb).image(R.drawable.plugins_default_video_thumb);
				if (value.containsKey(DURATION))
					holder.txtVideoDuration.setText(convertSecondsToMinutes(value.get(DURATION)));
				else
					holder.txtVideoDuration.setVisibility(View.INVISIBLE);
				if (IjoomerApplicationConfiguration.showYoutubeVideoTitle && value.containsKey(TITLE))
					holder.txtVideoTitle.setText(value.get(TITLE));
				else
					holder.txtVideoTitle.setVisibility(View.INVISIBLE);
				if (IjoomerApplicationConfiguration.showYoutubeVideoViews && value.containsKey(VIEWCOUNT))
					holder.txtVideoViews.setText(value.get(VIEWCOUNT) + " " + getString(R.string.views));
				else
					holder.txtVideoViews.setVisibility(View.GONE);
				if (IjoomerApplicationConfiguration.showYoutubeVideoDescription && value.containsKey(DESCRIPTION))
					holder.txtVideoDescription.setText(value.get(DESCRIPTION));
				else
					holder.txtVideoDescription.setVisibility(View.INVISIBLE);

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}
}
