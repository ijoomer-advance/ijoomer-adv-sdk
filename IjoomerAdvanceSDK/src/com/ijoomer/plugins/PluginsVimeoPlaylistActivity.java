package com.ijoomer.plugins;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.plugins.PluginsVimeoDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListenerWithCacheInfo;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

public class PluginsVimeoPlaylistActivity extends PluginsMasterActivity {
	
	private ListView lstPlayList;
	private View listFooter;
	private String [] arrayOfLinks;
	
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolder;
	private PluginsVimeoDataProvider vimeoDataProvider = new PluginsVimeoDataProvider(this);
	private AQuery aQuery;
	
	private String RSS = "rss";
	private String DESCRIPTION = "description";
	private String LINK= "link";
//	private String TITLE = "title";
	private String IN_USERNAME;
	private JSONObject IN_OBJ;
	
	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.plugins_vimeo_playlist;
	}

	@Override
	public void initComponents() {
		getIntentData();
		listFooter = LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstPlayList = (ListView) findViewById(R.id.lstVimeoPlayList);
		lstPlayList.addFooterView(listFooter, null, false);
		aQuery = new AQuery(PluginsVimeoPlaylistActivity.this);
		
	}


	@Override
	public void prepareViews() {
		getVimeoPlayLists();
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
	}

	@Override
	public void setActionListeners() {
	}
	
	
	
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
	 * This method used to get vimeo play list.
	 */
	public void getVimeoPlayLists() {
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		 vimeoDataProvider.getVimeoChannel(IN_USERNAME,new WebCallListenerWithCacheInfo() {

				@Override
				public void onProgressUpdate(int progressCount) {
					proSeekBar.setProgress(progressCount);
				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
					if (data2 != null ) {
						JSONObject json = (JSONObject) data2;
						String playListUrl = null;
						try {
							 playListUrl = json.getString(RSS);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						getVimeoPlayList(playListUrl.trim());
						
						
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
	
	public void getVimeoPlayList(String playListUrl){
		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		 vimeoDataProvider.getVimeoVideos(playListUrl,new WebCallListenerWithCacheInfo() {

				@Override
				public void onProgressUpdate(int progressCount) {
					proSeekBar.setProgress(progressCount);
				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2, int pageNo, int pageLimit, boolean fromCache) {
					if (data1 != null ) {
						if(data1.size()>1){
						prepareList(data1, fromCache, pageNo, pageLimit);
						}else{
							try{
								if(listAdapterWithHolder!=null){
									listAdapterWithHolder.clear();
								}
							IjoomerUtilities.getCustomOkDialog(getScreenCaption(), getString(getResources().getIdentifier("code" + 204, "string", getPackageName())),
									getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
										@Override
										public void NeutralMethod() {
										}
									});
							}catch (Exception e) {
								e.printStackTrace();
							}
						}
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
	 * This method used to get intent data.
	 */
	public void getIntentData() {
		try {
			IN_OBJ = new JSONObject(getIntent().getStringExtra("IN_OBJ"));
			IN_USERNAME = new JSONObject(IN_OBJ.getString(ITEMDATA)).getString("username");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	
	/**
	 * This method used to prepare list of vimeo videos.
	 * @param data represented video data
	 * @param fromCache represented data from cache
	 * @param pageno represented page no
	 * @param pagelimit represented page limit
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean fromCache, int pageno, int pagelimit) {
		if (data != null) {
			if(listAdapterWithHolder!=null){
				listAdapterWithHolder.clear();
			}else{
				listAdapterWithHolder = getListAdapter(listData);
				lstPlayList.setAdapter(listAdapterWithHolder);
			}
			int count =0;
			for (HashMap<String, String> hashData : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.plugins_youtube_playlistitem);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashData);
				item.setValues(obj);
				listAdapterWithHolder.add(item);
			}
			arrayOfLinks = new String[listData.size()];
			for (HashMap<String, String> hashData: data) {
				arrayOfLinks[count] = hashData.get("link");
				count = count + 1;
			}
		}
	}

	/**
	 * List adapter for vimeo videos.
	 * @param listData represented video data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */
	public SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(this, R.layout.plugins_vimeo_playlistitem, listData, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

				holder.imgPlaylistVimeoThumb = (ImageView) v.findViewById(R.id.imgPlaylistThumb);
				holder.txtPlaylistVimeoTitle = (IjoomerTextView) v.findViewById(R.id.txtPlaylistTitle);
			

				@SuppressWarnings("unchecked")
				final
				HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
				String[] thumb = value.get(DESCRIPTION).split("<img src=");
				thumb = thumb[1].split("alt");
				String imageUrl = null;
				if(thumb[0].contains("\"")){
					imageUrl = thumb[0].replaceAll("\"","");
				}
					try{
					aQuery.id(holder.imgPlaylistVimeoThumb).image(imageUrl.trim(), true, true, getDeviceWidth(), R.drawable.plugins_default_playlist_thumb);
					}catch (Exception e) {
						e.printStackTrace();
					}
					holder.txtPlaylistVimeoTitle.setText(value.get(TITLE));
					holder.imgPlaylistVimeoThumb.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							try {
								String[] url = value.get(LINK).split("/");
								int length = url.length;
								String link = url[length-1];
								Intent i = new Intent(PluginsVimeoPlaylistActivity.this,PluginsVimeoWebViewActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								i.putExtra("IN_URL",link);
								i.putExtra("IN_WIDTH",width);
								i.putExtra("IN_HEIGHT",height);
								i.putExtra("IN_POSITION",position);
								i.putExtra("IN_LINKS",arrayOfLinks);
								startActivity(i);
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
					});
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
