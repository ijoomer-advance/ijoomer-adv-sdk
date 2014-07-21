package com.ijoomer.components.jReview;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Activity Contains All Method Related To JReviewUploadVideos.
 *
 * @author tasol
 *
 */
public class JReviewVideosUploadActivity extends JReviewMasterActivity {

	private String IN_CATEGORYID;
	private String IN_ARTICLEID;

	private LinearLayout lnrUploadVideo;
	private LinearLayout lnrLinkVideo;
	private IjoomerTextView txtUploadVideo;
	private IjoomerTextView txtLinkVideo;
	private IjoomerEditText edtVideoFile;
	private IjoomerEditText edtVideoLink;
	private IjoomerEditText edtVideoTitle;
	private IjoomerEditText edtVideoDescription;
	private IjoomerButton btnBrowse;
	private IjoomerButton btnUpload;
	private IjoomerButton btnCancle;

	private JReviewDataProvider dataProvider;

	private String videoPath;
	private int PICK_VIDEO=2;
	private int TAKE_VIDEO=3;

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jreview_upload_videos;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		lnrUploadVideo = (LinearLayout) findViewById(R.id.lnrUploadVideo);
		lnrLinkVideo = (LinearLayout) findViewById(R.id.lnrLinkVideo);
		txtUploadVideo = (IjoomerTextView) findViewById(R.id.txtUploadVideo);
		txtLinkVideo = (IjoomerTextView) findViewById(R.id.txtLinkVideo);
		edtVideoFile = (IjoomerEditText) findViewById(R.id.edtVideoFile);
		edtVideoLink = (IjoomerEditText) findViewById(R.id.edtVideoLink);
		edtVideoTitle = (IjoomerEditText) findViewById(R.id.edtVideoTitle);
		edtVideoDescription = (IjoomerEditText) findViewById(R.id.edtVideoDescription);
		btnCancle = (IjoomerButton) findViewById(R.id.btnCancle);
		btnUpload = (IjoomerButton) findViewById(R.id.btnUpload);
		btnBrowse = (IjoomerButton) findViewById(R.id.btnBrowse);

		dataProvider = new JReviewDataProvider(this);

		getIntentData();
	}

	@Override
	public void prepareViews() {
		edtVideoDescription.setText("");
		edtVideoFile.setText("");
		edtVideoLink.setText("");
		edtVideoTitle.setText("");

		edtVideoFile.setHint(String.format(getString(R.string.videos_select_file), IjoomerGlobalConfiguration.getVideoUploadSize()));
		txtLinkVideo.setTextColor(Color.parseColor(getString(R.color.jreview_dark_brown)));
		lnrLinkVideo.setVisibility(View.VISIBLE);
		lnrUploadVideo.setVisibility(View.GONE);
		edtVideoTitle.setVisibility(View.GONE);
		edtVideoDescription.setVisibility(View.GONE);
	}

	@Override
	public void setActionListeners() {
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				if (lnrUploadVideo.getVisibility() == View.VISIBLE) {
					boolean validationFlag = true;

					if (edtVideoFile.getText().toString().trim().length() <= 0) {
						validationFlag = false;
						edtVideoFile.setError(getString(R.string.validation_value_required));
					}
					if (edtVideoTitle.getText().toString().trim().length() <= 0) {
						edtVideoTitle.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					}

					if (validationFlag) {
						startVideoUpload(IN_ARTICLEID, IN_CATEGORYID, edtVideoFile.getText().toString().trim(), edtVideoTitle.getText().toString().trim(), edtVideoDescription.getText()
								.toString().trim());
					} else{
						if (edtVideoLink.getText().toString().trim().length() <= 0) {
							edtVideoLink.setError(getString(R.string.validation_value_required));
						} else {
							startVideoLinking(IN_ARTICLEID, IN_CATEGORYID, edtVideoLink.getText().toString().trim());
						}
					}
				}
			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		txtUploadVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				txtUploadVideo.setTextColor(Color.parseColor(getString(R.color.jreview_dark_brown)));
				txtLinkVideo.setTextColor(Color.parseColor(getString(R.color.jreview_txt_color)));
				lnrLinkVideo.setVisibility(View.GONE);
				lnrUploadVideo.setVisibility(View.VISIBLE);
				edtVideoTitle.setVisibility(View.VISIBLE);
				edtVideoDescription.setVisibility(View.VISIBLE);
			}
		});

		txtLinkVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txtLinkVideo.setTextColor(Color.parseColor(getString(R.color.jreview_dark_brown)));
				txtUploadVideo.setTextColor(Color.parseColor(getString(R.color.jreview_txt_color)));
				lnrLinkVideo.setVisibility(View.VISIBLE);
				lnrUploadVideo.setVisibility(View.GONE);
				edtVideoTitle.setVisibility(View.GONE);
				edtVideoDescription.setVisibility(View.GONE);
			}
		});

		btnBrowse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

					@Override
					public void onPhoneGallery() {
						try {
							Intent intent = new Intent();
							intent.setType("video/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
						} catch (Exception e) {
						}
					}

					@Override
					public void onCapture() {
						try {
							Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
							startActivityForResult(takeVideoIntent, TAKE_VIDEO);
						} catch (Exception e) {
						}
					}
				});
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == PICK_VIDEO) {
				videoPath = ((IjoomerSuperMaster) this).getAbsolutePath(data.getData());
				edtVideoFile.setText(videoPath);

			} else if (requestCode == TAKE_VIDEO) {
				videoPath = ((IjoomerSuperMaster) this).getAbsolutePath(data.getData());
				edtVideoFile.setText(videoPath);
				if ((new File(videoPath).length() / (1024 * 1024)) > IjoomerGlobalConfiguration.getVideoUploadSize()) {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(R.string.video_select_size_limit_exceeded), getString(R.string.ok),
							R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {
							edtVideoFile.setText(null);
						}
					});
				}
			}
		}
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_CATEGORYID = getIntent().getStringExtra(CATEGORY_ID);
		IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID);
	}

	/**
	 * This method used to start upload video.
	 * @param articleId represented article id
	 * @param categoryId represented category id
	 * @param videoFilePath represented video file path
	 * @param videoTitle represented video title
	 * @param description represented video description
	 */
	private void startVideoUpload(final String articleId, final String categoryId, final String videoFilePath, final String videoTitle, final String description) {
		IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_starts),
				IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video), IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_starts));

		dataProvider.uploadVideo(articleId, categoryId, videoFilePath, videoTitle, description, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						clearVideoField();
						IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_successfully),
								IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video),
								IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_successfully));

						if(IjoomerUtilities.mSmartAndroidActivity instanceof JReviewArticleVideosActivity){
							IjoomerApplicationConfiguration.setReloadRequired(true);
							((JReviewArticleVideosActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
						}
					} else {
						if (errorMessage != null && errorMessage.length() > 0) {
							IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_failure),
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video), errorMessage);
						} else {
							IjoomerUtilities.addToNotificationBar(
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_failure),
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video),
									IjoomerUtilities.mSmartAndroidActivity.getString(IjoomerUtilities.mSmartAndroidActivity.getResources().getIdentifier("code" + responseCode,
											"string", IjoomerUtilities.mSmartAndroidActivity.getPackageName())));
						}
					}
				} catch (Throwable e) {
				}
			}

		});
	}

	/**
	 * This method used to upload video link.
	 * @param articleId represented article id
	 * @param categoryId represented category id
	 * @param videoUrl represented video link url
	 * @param videoCaption represented video caption
	 */
	private void startVideoLinking(final String articleId, final String categoryId, final String videoUrl) {

		IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_starts),
				IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video), IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_starts));

		dataProvider.linkVideo(articleId, categoryId, videoUrl, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						clearVideoField();
						IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_linked_successfully),
								IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video),
								IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_linked_successfully));

						if(IjoomerUtilities.mSmartAndroidActivity instanceof JReviewArticleVideosActivity){
							IjoomerApplicationConfiguration.setReloadRequired(true);
							((JReviewArticleVideosActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
						}
					} else {
						if (errorMessage != null && errorMessage.length() > 0) {
							IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_failure),
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video), errorMessage);

						} else {
							IjoomerUtilities.addToNotificationBar(
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_failure),
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video),
									IjoomerUtilities.mSmartAndroidActivity.getString(IjoomerUtilities.mSmartAndroidActivity.getResources().getIdentifier("code" + responseCode,
											"string", IjoomerUtilities.mSmartAndroidActivity.getPackageName())));
						}
					}
				} catch (Throwable e) {
				}
			}
		});
	}

	private void clearVideoField(){
		edtVideoDescription.setText("");
		edtVideoFile.setText("");
		edtVideoLink.setText("");
		edtVideoTitle.setText("");
	}

}
