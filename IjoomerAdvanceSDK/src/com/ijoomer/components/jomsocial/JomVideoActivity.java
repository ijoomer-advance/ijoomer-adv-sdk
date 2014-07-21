package com.ijoomer.components.jomsocial;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;

import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomVideoActivity.
 * 
 * @author tasol
 * 
 */
public class JomVideoActivity extends JomMasterActivity {

	private LinearLayout lnrVideoHeader;
	private LinearLayout lnrVideoSearch;
	private IjoomerTextView txtAllVideos;
	private IjoomerTextView txtMyVideos;
	private IjoomerTextView txtAddVideos;
	private IjoomerEditText editSearch;
	private IjoomerButton btnAddVideo;
	private ImageView imgSearch;

	private JomVideoAddFragment addVideoFragment;
	private JomVideoAllFragment allVideoFragment;
	private JomVideoMyFragment myVideoFragment;
	private JomVideoSearchFragment searchVideoFragment;
	public static HashMap<String, String> currentVideo;

	final private String ALLVIDEOS = "allvideos";
	final private String MYVIDEOS = "myvideos";
	final private String ADDVIDEO = "addvideo";
	private String IN_USERID;
	private String IN_GROUP_ID;
	private String IN_GROUP_ADD_VIDEO;
	private String IN_PROFILE;
	private String currentList = MYVIDEOS;
	public static boolean isVideoPlay;
	public static boolean ADDVIDEOFLAG = false;


	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_video;
	}

	@Override
	public void initComponents() {

		lnrVideoHeader = (LinearLayout) findViewById(R.id.lnrVideoHeader);
		lnrVideoSearch = (LinearLayout) findViewById(R.id.lnrVideoSearch);
		txtAllVideos = (IjoomerTextView) findViewById(R.id.txtAllVideos);
		txtMyVideos = (IjoomerTextView) findViewById(R.id.txtMyVideos);
		txtAddVideos = (IjoomerTextView) findViewById(R.id.txtAddVideos);
		editSearch = (IjoomerEditText) findViewById(R.id.editSearch);
		btnAddVideo = (IjoomerButton) findViewById(R.id.btnAddVideo);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);

		getIntentData();

	}

	@Override
	public void prepareViews() {

		if (!IN_PROFILE.equals("0")) {
			lnrVideoHeader.setVisibility(View.GONE);
			lnrVideoSearch.setVisibility(View.GONE);
			if (addVideoFragment == null) {
				addVideoFragment = new JomVideoAddFragment();
			}
			addFragment(R.id.lnrFragment, addVideoFragment);
		} else {
			txtMyVideos.setTextColor(getResources().getColor(R.color.jom_blue));

			if (!IN_GROUP_ADD_VIDEO.equals("0")) {
				btnAddVideo.setVisibility(View.VISIBLE);
			}

			if (!IN_GROUP_ID.equals("0")) {
				lnrVideoSearch.setVisibility(View.GONE);
				currentList = ALLVIDEOS;
				if (allVideoFragment == null) {
					allVideoFragment = new JomVideoAllFragment();
				}
				addFragment(R.id.lnrFragment, allVideoFragment);
			} else {
				IN_GROUP_ID = "0";
				IN_GROUP_ADD_VIDEO = "0";
				lnrVideoHeader.setVisibility(View.VISIBLE);
				if (myVideoFragment == null) {
					myVideoFragment = new JomVideoMyFragment();
				}
				addFragment(R.id.lnrFragment, myVideoFragment);
			}
		}
	}

	@Override
	protected void onResume() {

		if (isVideoPlay) {
			isVideoPlay = false;
			try {
				loadNew(JomVideoDetailsActivity.class, JomVideoActivity.this, false, "IN_USERID", IN_USERID, "IN_VIDEO_DETAILS", currentVideo, "IN_GROUPID", IN_GROUP_ID);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else if (!IN_GROUP_ID.equals("0")) {
			if (IjoomerApplicationConfiguration.isReloadRequired()) {
				IjoomerApplicationConfiguration.setReloadRequired(false);
				if (ADDVIDEOFLAG) {
                    ADDVIDEOFLAG=false;
					addFragment(R.id.lnrFragment, allVideoFragment);
				} else {
					allVideoFragment.update();
				}
			}
		} else {
			if (IjoomerApplicationConfiguration.isReloadRequired()) {
				IjoomerApplicationConfiguration.setReloadRequired(false);
				if (currentList.equals(ALLVIDEOS)) {
					if (ADDVIDEOFLAG) {
                        ADDVIDEOFLAG=false;
						addFragment(R.id.lnrFragment, allVideoFragment);
					} else {
						allVideoFragment.update();
					}
				} else if (currentList.equals(MYVIDEOS) || currentList.equals(ADDVIDEO)) {
					currentList = MYVIDEOS;
					setHeaderTextColor();
					if (ADDVIDEOFLAG) {
                        ADDVIDEOFLAG=false;
						addFragment(R.id.lnrFragment, myVideoFragment);
					} else {
						myVideoFragment.update();
					}
				}
				lnrVideoSearch.setVisibility(View.VISIBLE);
			}
		}
		super.onResume();
	}

	@Override
	public void setActionListeners() {

		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hideSoftKeyboard();
				if (editSearch.getText().toString().trim().length() > 0) {
					imgSearch.setClickable(false);
					if (searchVideoFragment == null) {
						searchVideoFragment = new JomVideoSearchFragment();
					}
					searchVideoFragment.setSerachKeyword(editSearch.getText().toString().trim());
					addFragment(R.id.lnrFragment, searchVideoFragment);
				} else {
					editSearch.setError(getString(R.string.validation_value_required));
				}
			}
		});
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable edit) {
				if (edit.length() == 0) {
					hideSoftKeyboard();
					setHeaderTextColor();
					if (currentList.equals(ALLVIDEOS)) {
						addFragment(R.id.lnrFragment, allVideoFragment);
					} else {
						addFragment(R.id.lnrFragment, myVideoFragment);
					}

				}
			}
		});

		txtAddVideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!currentList.equals(ADDVIDEO)) {
					currentList = ADDVIDEO;
					ADDVIDEOFLAG = true;
					setHeaderTextColor();
					lnrVideoSearch.setVisibility(View.GONE);
					ADDVIDEOFLAG = true;
					if (addVideoFragment == null) {
						addVideoFragment = new JomVideoAddFragment();
					}
					addFragment(R.id.lnrFragment, addVideoFragment);
				}
			}
		});

		btnAddVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!currentList.equals(ADDVIDEO)) {
					currentList = ADDVIDEO;
					ADDVIDEOFLAG = true;
					if (addVideoFragment == null) {
						addVideoFragment = new JomVideoAddFragment();
					}
					addFragment(R.id.lnrFragment, addVideoFragment);
				}
			}
		});

		txtMyVideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!currentList.equals(MYVIDEOS)) {
					currentList = MYVIDEOS;
					setHeaderTextColor();
					if (myVideoFragment == null) {
						myVideoFragment = new JomVideoMyFragment();
					}
					addFragment(R.id.lnrFragment, myVideoFragment);
				}
			}
		});

		txtAllVideos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!currentList.equals(ALLVIDEOS)) {
					currentList = ALLVIDEOS;
					setHeaderTextColor();
					if (allVideoFragment == null) {
						allVideoFragment = new JomVideoAllFragment();
					}
					addFragment(R.id.lnrFragment, allVideoFragment);

				}
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
	private void getIntentData() {
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");
		IN_GROUP_ID = getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ID");
		IN_GROUP_ADD_VIDEO = getIntent().getStringExtra("IN_GROUP_ADD_VIDEO") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ADD_VIDEO");
		IN_PROFILE = getIntent().getStringExtra("IN_PROFILE") == null ? "0" : getIntent().getStringExtra("IN_PROFILE");
	}

	/**
	 * This method used to set header color.jom_
	 */
	private void setHeaderTextColor() {
		if (currentList.equals(ALLVIDEOS)) {
			txtAllVideos.setTextColor(getResources().getColor(R.color.jom_blue));
			txtMyVideos.setTextColor(getResources().getColor(R.color.jom_txt_color));
			txtAddVideos.setTextColor(getResources().getColor(R.color.jom_txt_color));
		} else if (currentList.equals(MYVIDEOS)) {
			txtAllVideos.setTextColor(getResources().getColor(R.color.jom_txt_color));
			txtMyVideos.setTextColor(getResources().getColor(R.color.jom_blue));
			txtAddVideos.setTextColor(getResources().getColor(R.color.jom_txt_color));
		} else {
			txtAllVideos.setTextColor(getResources().getColor(R.color.jom_txt_color));
			txtMyVideos.setTextColor(getResources().getColor(R.color.jom_txt_color));
			txtAddVideos.setTextColor(getResources().getColor(R.color.jom_blue));
		}
	}

}
