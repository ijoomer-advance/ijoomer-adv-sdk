package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To JomVideoMyFragment.
 *
 * @author tasol
 *
 */
@SuppressLint("ValidFragment")
public class JReviewArticleVideosActivity extends JReviewMasterActivity implements JReviewTagHandler {

	private ImageView addVideos;
	private GridView grdVideos;
	private String ARTICLE_NAME = "";
	private String IN_CATEGORYID = "";
	private String IN_ARTICLEID = "";
	private ArrayList<HashMap<String, String>> IN_ARTICLE_DETAILS;
	private ArrayList<HashMap<String, String>> IN_VIDEOLIST;
	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterVideo;
	private AQuery androidQuery;
	private JReviewDataProvider dataProvider;
	private IjoomerCaching iCaching;


	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_videos_grid;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		getIntentData();

		dataProvider = new JReviewDataProvider(this);
		iCaching = new IjoomerCaching(this);
		androidQuery = new AQuery(this);

		addVideos = ((ImageView) getHeaderView().findViewById(R.id.imgAddArticle));
		addVideos.setVisibility(View.VISIBLE);
		grdVideos = (GridView) findViewById(R.id.grdVideos);
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(ARTICLE_NAME);

		getArticleVideos();
	}

	@Override
	public void setActionListeners() {
		addVideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					if(IjoomerGlobalConfiguration.isJreviewVideoUploadEnable().equalsIgnoreCase("1")){
						if(IN_ARTICLE_DETAILS.get(0).get(VIDEO_COUNT)
								.equalsIgnoreCase(IjoomerGlobalConfiguration.getJreviewArticleVideoUploadLimit())){
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_videos), getString(R.string.jreview_upload_limit_notice)
									, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}else{
							try{
								loadNew(JReviewVideosUploadActivity.class, JReviewArticleVideosActivity.this, false, 
										CATEGORY_ID, IN_CATEGORYID, ARTICLEID, IN_ARTICLEID);
							}catch(Exception e){
							}
						}
					}else{
						if(IN_ARTICLE_DETAILS.get(0).get(USERNAME).
								equalsIgnoreCase(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
							if(IN_ARTICLE_DETAILS.get(0).get(VIDEO_COUNT)
									.equalsIgnoreCase(IjoomerGlobalConfiguration.getJreviewArticleVideoUploadLimit())){
								IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_videos), getString(R.string.jreview_upload_limit_notice)
										, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
										new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
							}else{
								try{
									loadNew(JReviewVideosUploadActivity.class, JReviewArticleVideosActivity.this, false, 
											CATEGORY_ID, IN_CATEGORYID, ARTICLEID, IN_ARTICLEID);
								}catch(Exception e){
								}
							}
						}else{
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_videos), getString(R.string.jreview_upload_notice)
									, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				} else {
					try {
						getSmartApplication().writeSharedPreferences(SP_DOLOGIN, true);
						loadNew(IjoomerLoginActivity.class, JReviewArticleVideosActivity.this, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}


	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		try{
			ARTICLE_NAME = getIntent().getStringExtra(ARTICLENAME);
			IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID);
			IN_CATEGORYID = getIntent().getStringExtra(CATEGORY_ID);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to get article details.
	 */
	private void getArticleVideos(){
		try{
			IN_ARTICLE_DETAILS = dataProvider.getArticleVideos(IN_CATEGORYID, IN_ARTICLEID);
			IN_VIDEOLIST = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(MEDIAVIDEOS)));
		}catch(Exception e){
			e.printStackTrace();
			IN_VIDEOLIST = null;
		}

		//prepare videos grid
		if(IN_VIDEOLIST!=null && IN_VIDEOLIST.size()>0){
			prepareList(IN_VIDEOLIST,false);
			adapterVideo = getListAdapter();
			grdVideos.setAdapter(adapterVideo);
		}
	}


	/**
	 * This method used to prepare list of article videos.
	 * @param data represented video data
	 * @param append represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jreview_article_video_grid_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					adapterVideo.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * Grid adapter for article videos.
	 */
	private SmartListAdapterWithHolder getListAdapter() {

		adapterVideo = new SmartListAdapterWithHolder(JReviewArticleVideosActivity.this, R.layout.jreview_article_video_grid_item, listData, new ItemView() {

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.jreviewarticlelnrVideo = (LinearLayout) v.findViewById(R.id.lnrVideo);
				holder.jreviewarticleimgVideoAvatar = (ImageView) v.findViewById(R.id.jreviewarticleimgVideoAvatar);
				holder.jreviewarticleimgVideoPlay = (ImageView) v.findViewById(R.id.jreviewarticleimgvideoplay);
				holder.jreviewarticletxtVideoTitle = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtVideoTitle);
				holder.jreviewarticletxtVideoBy = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtVideoBy);

				holder.jreviewarticletxtVideoDateLocation = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtVideoDateLocation);
				holder.jreviewarticleVideoLike = (LinearLayout) v.findViewById(R.id.jreviewarticlevideolike);
				holder.jreviewarticleVideoUnlike = (LinearLayout) v.findViewById(R.id.jreviewarticlevideounlike);
				holder.jreviewarticletxtVideoLikeCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtVideoLikeCount);
				holder.jreviewarticletxtVideoDislikeCount = (IjoomerTextView) v.findViewById(R.id.jreviewarticletxtVideoDislikeCount);
				holder.jreviewarticleimgVideoShare = (ImageView) v.findViewById(R.id.jreviewarticleimgVideoShare);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				androidQuery.id(holder.jreviewarticleimgVideoAvatar).image(row.get(ORIGINAL), true, true);

				holder.jreviewarticletxtVideoTitle.setText(row.get(TITLE));
				holder.jreviewarticletxtVideoBy.setText(getString(R.string.jreview_by)+" "+row.get(USERNAME));
				holder.jreviewarticletxtVideoLikeCount.setText(row.get(LIKECOUNT));
				holder.jreviewarticletxtVideoDislikeCount.setText(row.get(DISLIKECOUNT));
				holder.jreviewarticletxtVideoDateLocation.setText(row.get(CREATED));

				holder.jreviewarticleimgVideoPlay.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							Intent lVideoIntent = new Intent(null,getVideoPlayURI(row.get(VIDEOURL).toString()), JReviewArticleVideosActivity.this, IjoomerMediaPlayer.class);
							startActivity(lVideoIntent);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

				holder.jreviewarticletxtVideoTitle.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							loadNew(JReviewArticleVideosDetailsActivity.class, JReviewArticleVideosActivity.this, false, 
									CATEGORY_ID, IN_CATEGORYID, "IN_VIDEO_DETAILS", row, ARTICLEID, IN_ARTICLEID, 
									ARTICLENAME, ARTICLE_NAME);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				});

				holder.jreviewarticleimgVideoShare.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						try {
							loadNew(IjoomerShareActivity.class, JReviewArticleVideosActivity.this, false, 
									"IN_SHARE_CAPTION", row.get(TITLE), "IN_SHARE_DESCRIPTION", row.get(DESCRIPTION), 
									"IN_SHARE_THUMB", row.get(THUMB), "IN_SHARE_SHARELINK", row.get(VIDEOURL));
						} catch (Throwable e) {
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
		return adapterVideo;

	}
}
