package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPhotoGalaryActivity;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * Activity class for SobiproGalleryActivity view
 * 
 * @author tasol
 * 
 */
public class JReviewArticleGalleryActivity extends JReviewMasterActivity {
	private String IN_ARTICLENAME;
	private String IN_CATEGORYID;
	private String IN_ARTICLEID;

	final private int PICK_IMAGE = 0;
	final private int PICK_IMAGE_MULTIPLE = 1;
	final private int TAKE_IMAGE = 2;
	private int IN_INDEX = 0;

	private ImageView addPhotos;
	private ViewPager viewPager;
	private LinearLayout lnrImgs;
	private PhotoAdapter photoAdapter;

	private ArrayList<HashMap<String, String>> IN_ARTICLE_DETAILS;
	private ArrayList<HashMap<String, String>> IN_ARTICLE_IMAGES;

	private AQuery androidQuery;
	private JReviewDataProvider dataProvider;
	private IjoomerCaching iCaching;

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jreview_gallery_fragment;
	}

	@Override
	public void initComponents() {
		getIntentData();

		dataProvider = new JReviewDataProvider(this);
		iCaching = new IjoomerCaching(this);
		androidQuery = new AQuery(this);

		addPhotos = ((ImageView) getHeaderView().findViewById(R.id.imgaddphotos));
		addPhotos.setVisibility(View.VISIBLE);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		lnrImgs = (LinearLayout) findViewById(R.id.lnrImgs);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(IjoomerApplicationConfiguration.isReloadRequired()){
			getArticleImages();
			IjoomerApplicationConfiguration.setReloadRequired(false);
		}
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(IN_ARTICLENAME);
		getArticleImages();
	}

	@Override
	public void setActionListeners() {
		addPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "").length() > 0) {
					if(IjoomerGlobalConfiguration.isJreviewPhotoUploadEnable().equalsIgnoreCase("1")){
						if(IN_ARTICLE_DETAILS.get(0).get(PHOTO_COUNT)
								.equalsIgnoreCase(IjoomerGlobalConfiguration.getJreviewArticlePhotoUploadLimit())){
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_photos), getString(R.string.jreview_upload_limit_notice)
									, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}else{
							IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

								@Override
								public void onPhoneGallery() {
									if (IjoomerApplicationConfiguration.isUploadMultiplePhotos()) {
										Intent intent = new Intent(JReviewArticleGalleryActivity.this, IjoomerPhotoGalaryActivity.class);
										startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
									} else {
										Intent intent = new Intent();
										intent.setType("image/*");
										intent.setAction(Intent.ACTION_GET_CONTENT);
										startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
									}
								}

								@Override
								public void onCapture() {
									final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
									intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
									startActivityForResult(intent, TAKE_IMAGE);
								}
							});
						}
					}else{
						if(IN_ARTICLE_DETAILS.get(0).get(USERNAME).
								equalsIgnoreCase(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""))){
							if(IN_ARTICLE_DETAILS.get(0).get(PHOTO_COUNT)
									.equalsIgnoreCase(IjoomerGlobalConfiguration.getJreviewArticlePhotoUploadLimit())){
								IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_photos), getString(R.string.jreview_upload_limit_notice)
										, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
										new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {

									}
								});
							}else{
								IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

									@Override
									public void onPhoneGallery() {
										if (IjoomerApplicationConfiguration.isUploadMultiplePhotos()) {
											Intent intent = new Intent(JReviewArticleGalleryActivity.this, IjoomerPhotoGalaryActivity.class);
											startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
										} else {
											Intent intent = new Intent();
											intent.setType("image/*");
											intent.setAction(Intent.ACTION_GET_CONTENT);
											startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
										}
									}

									@Override
									public void onCapture() {
										final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
										startActivityForResult(intent, TAKE_IMAGE);
									}
								});
							}
						}else{
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_photos), getString(R.string.jreview_upload_notice)
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
						loadNew(IjoomerLoginActivity.class, JReviewArticleGalleryActivity.this, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PICK_IMAGE_MULTIPLE:
				IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_starts), getString(R.string.upload_photo),
						getString(R.string.photo_upload_starts));
				startUpload(0, data.getStringExtra("data").split("\\|"), IN_ARTICLEID, IN_CATEGORYID);
				break;
			case PICK_IMAGE:
				IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_starts), getString(R.string.upload_photo),
						getString(R.string.photo_upload_starts));
				startUpload(0, (getAbsolutePath(data.getData()) + "|").split("\\|"), IN_ARTICLEID, IN_CATEGORYID);
				break;
			case TAKE_IMAGE:
				IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_starts), getString(R.string.upload_photo),
						getString(R.string.photo_upload_starts));
				startUpload(0, (getImagePath() + "|").split("\\|"), IN_ARTICLEID, IN_CATEGORYID);
				break;
			}
		}
	}

	/**
	 * Class methods.
	 */

	/**
	 * This method is used to get intent data.
	 */
	private void getIntentData() {
		try {
			IN_CATEGORYID = getIntent().getStringExtra(CATEGORY_ID) == null ? "0" : getIntent().getStringExtra(CATEGORY_ID);
			IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID) == null ? "0" : getIntent().getStringExtra(ARTICLEID);
			IN_ARTICLENAME = getIntent().getStringExtra(ARTICLENAME) == null ? "" : getIntent().getStringExtra(ARTICLENAME);
			IN_INDEX = getIntent().getIntExtra("IN_INDEX", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is used to get article details.
	 */
	private void getArticleImages(){
		try{
			IN_ARTICLE_DETAILS = dataProvider.getArticlePhotos(IN_CATEGORYID, IN_ARTICLEID);
			IN_ARTICLE_IMAGES = iCaching.parseData(new JSONArray(IN_ARTICLE_DETAILS.get(0).get(MEDIAIMAGES)));
		}catch(Exception e){
			IN_ARTICLE_IMAGES = null;
			e.printStackTrace();
		}

		IN_INDEX = 0;
		photoAdapter = new PhotoAdapter(getSupportFragmentManager());
		if (IN_ARTICLE_IMAGES !=null) {
			prepareBottomView();
			viewPager.setAdapter(photoAdapter);
			viewPager.setCurrentItem(IN_INDEX);
		}
	}

	/**
	 * This method is used to set Gallery bottom scrollable thumbnail view.
	 */
	@SuppressLint("NewApi")
	public void prepareBottomView() {
		lnrImgs.removeAllViews();
		for (int i = 0; i < IN_ARTICLE_IMAGES.size(); i++) {
			ImageView img = new ImageView(this);
			img.setBackgroundColor(Color.WHITE);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(convertSizeToDeviceDependent(50),
					convertSizeToDeviceDependent(50));
			param.setMargins(2, 2, 2, 2);
			img.setScaleType(ScaleType.FIT_XY);
			img.setId(i);
			img.setTag(i);
			img.setPadding(2, 2, 2, 2);

			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					viewPager.setCurrentItem((Integer) v.getTag());
				}
			});

			androidQuery.id(img).image(IN_ARTICLE_IMAGES.get(i).get(ORIGINAL),true,true,200,0);

			lnrImgs.addView(img, param);
		}
	}

	/**
	 * Custom Photo Adapter for gallery view.
	 */

	private class PhotoAdapter extends FragmentStatePagerAdapter {

		public PhotoAdapter(FragmentManager fm) {
			super(fm);
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			JReviewArticleImageFragment fragment = new JReviewArticleImageFragment(IN_ARTICLE_IMAGES.get(pos).get(ORIGINAL));
			return fragment;
		}

		@Override
		public int getCount() {
			return IN_ARTICLE_IMAGES.size();
		}

	}

	/**
	 * This method used to start upload photo.
	 *
	 * @param index
	 *            represented photo index
	 * @param paths
	 *            represented photo path
	 * @param albumID
	 *            represented album id
	 */
	private void startUpload(final int index, final String[] paths, final String articleID,final String categoryID) {

		if (index != (paths.length)) {
			dataProvider.uploadPhoto(articleID, categoryID, paths[index], new WebCallListener() {

				@Override
				public void onProgressUpdate(final int progressCount) {

				}

				@Override
				public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					if (responseCode == 200) {
						String ticker = (index + 1) + "/" + paths.length + IjoomerUtilities.mSmartAndroidActivity.getString(R.string.photo_uploaded);
						IjoomerUtilities.addToNotificationBar(ticker, IjoomerUtilities.mSmartAndroidActivity.getString(R.string.upload_photo), ticker);
						startUpload(index + 1, paths, articleID, categoryID);

						if(IjoomerUtilities.mSmartAndroidActivity instanceof JReviewArticleGalleryActivity){
							IjoomerApplicationConfiguration.setReloadRequired(true);
							((JReviewArticleGalleryActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
						}
						getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, true);
						getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
					} else {
						if (errorMessage != null && errorMessage.length() > 0) {
							IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.photo_upload_failure),
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.upload_photo), errorMessage);
						} else {
							IjoomerUtilities.addToNotificationBar(
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.photo_upload_failure),
									IjoomerUtilities.mSmartAndroidActivity.getString(R.string.upload_photo),
									IjoomerUtilities.mSmartAndroidActivity.getString(IjoomerUtilities.mSmartAndroidActivity.getResources().getIdentifier("code" + responseCode,
											"string", IjoomerUtilities.mSmartAndroidActivity.getPackageName())));
						}
					}
				}
			});
		}
	}
}