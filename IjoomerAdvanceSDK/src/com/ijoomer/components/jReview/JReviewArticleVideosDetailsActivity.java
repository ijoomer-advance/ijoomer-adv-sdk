package com.ijoomer.components.jReview;

import java.util.HashMap;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.custom.interfaces.ReportListner;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.media.player.IjoomerMediaPlayer;
import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To JomVideoDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class JReviewArticleVideosDetailsActivity extends JReviewMasterActivity {

	private IjoomerTextView txtVideoTitle;
	private IjoomerTextView txtVideoBy;
	private IjoomerTextView txtVideoDateLocation;
	private IjoomerTextView txtVideoLikeCount;
	private IjoomerTextView txtVideoDislikeCount;
	private IjoomerTextView txtVideoDesciption;
	private IjoomerTextView txtVideoShare;
	private IjoomerTextView txtVideoReport;
	private ImageView imgVideoAvatar;

	private AQuery androidQuery;
	private HashMap<String, String> IN_VIDEO_DETAILS;
	private String ARTICLE_NAME = "";

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jreview_article_video_details;
	}

	@Override
	public void initComponents() {
		androidQuery = new AQuery(this);

		txtVideoTitle = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoTitle);
		txtVideoLikeCount = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoLikeCount);
		txtVideoDislikeCount = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoDislikeCount);
		txtVideoBy = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoBy);
		txtVideoDateLocation = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoDateLocation);
		txtVideoDesciption = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoDesciption);
		txtVideoShare = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoShare);
		txtVideoReport = (IjoomerTextView) findViewById(R.id.jreviewarticletxtVideoReport);
		imgVideoAvatar = (ImageView) findViewById(R.id.jreviewarticleimgVideoAvatar);

		getIntentData();
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(ARTICLE_NAME);
		txtVideoTitle.setText(IN_VIDEO_DETAILS.get(TITLE).toString());
		txtVideoBy.setText(IN_VIDEO_DETAILS.get(USERNAME).toString());

		txtVideoDateLocation.setText(IN_VIDEO_DETAILS.get(CREATED));

		txtVideoBy.setText(getString(R.string.jreview_by)+" "+IN_VIDEO_DETAILS.get(USERNAME));

		txtVideoDesciption.setText(IN_VIDEO_DETAILS.get(DESCRIPTION).toString());

		txtVideoLikeCount.setText(IN_VIDEO_DETAILS.get(LIKECOUNT));
		txtVideoDislikeCount.setText(IN_VIDEO_DETAILS.get(DISLIKECOUNT));
	}

	@Override
	public void setActionListeners() {
		imgVideoAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Intent lVideoIntent = new Intent(null,getVideoPlayURI(IN_VIDEO_DETAILS.get(VIDEOURL).toString()), JReviewArticleVideosDetailsActivity.this, IjoomerMediaPlayer.class);
					startActivity(lVideoIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		txtVideoLikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		txtVideoDislikeCount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		txtVideoShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					loadNew(IjoomerShareActivity.class, JReviewArticleVideosDetailsActivity.this, false, "IN_SHARE_CAPTION", IN_VIDEO_DETAILS.get(TITLE).toString(), "IN_SHARE_DESCRIPTION",
							IN_VIDEO_DETAILS.get(DESCRIPTION).toString(), "IN_SHARE_THUMB", IN_VIDEO_DETAILS.get(THUMB).toString(), "IN_SHARE_SHARELINK",
							IN_VIDEO_DETAILS.get(VIDEOURL));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

		txtVideoReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				IjoomerUtilities.getJreviewArticleVideosReportDialog(new ReportListner() {

					@Override
					public void onClick(String repotType, String message) {
						tingOnUI("Under Construction!!!!");
					}
				});
			}
		});

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		//categoryId = getIntent().getStringExtra(CATEGORY_ID) == null ? "0" : getIntent().getStringExtra(CATEGORY_ID);
		IN_VIDEO_DETAILS = (HashMap<String, String>) getIntent().getSerializableExtra("IN_VIDEO_DETAILS") == null ? new HashMap<String, String>()
				: (HashMap<String, String>) getIntent().getSerializableExtra("IN_VIDEO_DETAILS");
		//articleId = getIntent().getStringExtra(ARTICLEID) == null ? "0" : getIntent().getStringExtra(ARTICLEID);
		ARTICLE_NAME = getIntent().getStringExtra(ARTICLENAME);

		androidQuery.id(imgVideoAvatar).image(IN_VIDEO_DETAILS.get(ORIGINAL), true, true, getDeviceWidth(), 0);
	}
}
