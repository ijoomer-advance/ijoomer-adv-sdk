package com.ijoomer.plugins;

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
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.plugins.PluginsYoutubeDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains UnNormalizeFields PluginsYoutubePlaylistActivity.
 * 
 * @author tasol
 * 
 */
public class PluginsYoutubePlaylistActivity extends PluginsMasterActivity {

	private ListView lstPlayList;
	private View listFooter;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private PluginsYoutubeDataProvider youtubeDataProvider = new PluginsYoutubeDataProvider(this);
	private AQuery aQuery;

	private String IN_USERNAME;
	private JSONObject IN_OBJ;

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.plugins_youtube_playlist;
	}

	@Override
	public void initComponents() {
		getIntentData();
		listFooter = LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstPlayList = (ListView) findViewById(R.id.lstYoutubePlayList);
		lstPlayList.addFooterView(listFooter, null, false);
		aQuery = new AQuery(PluginsYoutubePlaylistActivity.this);

	}

	@Override
	public void prepareViews() {
		getYoutubePlayLists();
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
	}

	@Override
	public void setActionListeners() {
		lstPlayList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 0) {

					if (!youtubeDataProvider.isCalling() && youtubeDataProvider.hasNextPage()) {
						listFooterVisible();
						youtubeDataProvider.getYoutubePlayLists(IN_USERNAME, new WebCallListenerWithCacheInfo() {

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

		lstPlayList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) listData.get(pos).getValues().get(0);
				try {
					setScreenCaption(value.get(TITLE));
					loadNew(PluginsYoutubeVideoActivity.class, PluginsYoutubePlaylistActivity.this, false, "IN_PLAYLIST_ID", value.get("id"));
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
	 * This method used to get youtube play list.
	 */
	public void getYoutubePlayLists() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

		youtubeDataProvider.getYoutubePlayLists(IN_USERNAME, new WebCallListenerWithCacheInfo() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
				if (data1 != null && data1.size() > 0) {
					prepareList(data1, false, fromCache, pageNo, pageLimit);
					listAdapterWithHolder = getListAdapter(listData);
					lstPlayList.setAdapter(listAdapterWithHolder);
				}else if(responseCode==599)
				{
					IjoomerUtilities.getCustomOkDialog(getScreenCaption(), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
							getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
								}
							});
				}
				else {
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

		try {
			IN_OBJ = new JSONObject(getIntent().getStringExtra("IN_OBJ"));
			IN_USERNAME = new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("username");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method used to prepare list youtube playlist.
	 * 
	 * @param data
	 *            represented playlist data
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
				if (data.size() == 1) {
					HashMap<String, String> value = (HashMap<String, String>) data.get(0);
					try {
						setScreenCaption(value.get(TITLE));
						loadNew(PluginsYoutubeVideoActivity.class, PluginsYoutubePlaylistActivity.this, false, "IN_PLAYLIST_ID", value.get("id"));
					} catch (Exception e) {

						e.printStackTrace();
					}

				}
			} else {
				if (!fromCache) {
					int startIndex = ((pageno - 1) * pagelimit);
					int endIndex = listAdapterWithHolder.getCount();
					if (startIndex > 0) {
						for (int i = endIndex; i >= startIndex; i--) {
							try {
								listAdapterWithHolder.remove(listAdapterWithHolder.getItem(i));
								listData.remove(i);
							} catch (Exception e) {
							}
						}
					}
				}
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.plugins_youtube_playlistitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolder.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * List adapter for youtube playlist.
	 * 
	 * @param listData
	 *            represented playlist data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.plugins_youtube_playlistitem, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.imgPlaylistThumb = (ImageView) v.findViewById(R.id.imgPlaylistThumb);

				holder.txtPlaylistTitle = (IjoomerTextView) v.findViewById(R.id.txtPlaylistTitle);
				holder.txtVideoCount = (IjoomerTextView) v.findViewById(R.id.txtVideoCount);
				holder.txtVideoLabel = (IjoomerTextView) v.findViewById(R.id.txtVideoLabel);

				@SuppressWarnings("unchecked")
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				if (value.containsKey(HQDEFAULT))
					aQuery.id(holder.imgPlaylistThumb).image(value.get(HQDEFAULT), true, true, getDeviceWidth(), R.drawable.plugins_default_playlist_thumb);
				else
					aQuery.id(holder.imgPlaylistThumb).image(R.drawable.plugins_default_playlist_thumb);
				if (value.containsKey(TITLE))
					holder.txtPlaylistTitle.setText(value.get(TITLE));
				if (value.containsKey(SIZE)) {
					holder.txtVideoCount.setText(value.get(SIZE));
					if (Integer.parseInt(value.get(SIZE)) == 1)
						holder.txtVideoLabel.setText(getText(R.string.video_lbl));
					else
						holder.txtVideoLabel.setText(getText(R.string.videos_lbl));
				}

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
