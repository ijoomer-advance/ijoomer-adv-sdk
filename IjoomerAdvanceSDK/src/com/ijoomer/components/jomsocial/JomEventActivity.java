package com.ijoomer.components.jomsocial;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomEventDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomEventActivity.
 * 
 * @author tasol
 * 
 */
public class JomEventActivity extends JomMasterActivity {

	private LinearLayout lnrEventHeader;
	private IjoomerTextView txtEventAll;
	private IjoomerTextView txtEventMy;
	private IjoomerTextView txtEventPast;
	private IjoomerTextView txtEventPending;
	private ImageView imgSearch;
	private IjoomerEditText editEventSearch;
	private IjoomerButton btnEventCreate;

	private JomEventDataProvider provider;

	private JomEventAllFragment allEventsFragment;
	private JomEventMyFragment myEventsFragment;
	private JomEventPastFragment pastEventsFragment;
	private JomEventPendingFragment pendingEventsFragment;
	private JomEventSearchFragment searchEventsFragment;
	private JomEventGroupFragment groupEventsFragment;

	final private String ALLEVENT = "allevent";
	final private String MYEVENT = "myevent";
	final private String PASTEVENT = "pastevent";
	final private String PENDINGEVENT = "pendingevent";
	@SuppressWarnings("unused")
	private String IN_USERID;
	private String IN_GROUP_ID;
	private String IN_GROUP_CREATE_EVENT;
	private String currentList = MYEVENT;


	/**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.jom_event_list;
	}

	@Override
	public void initComponents() {
		lnrEventHeader = (LinearLayout) findViewById(R.id.lnrEventHeader);
		txtEventAll = (IjoomerTextView) findViewById(R.id.txtEventAll);
		txtEventMy = (IjoomerTextView) findViewById(R.id.txtEventMy);
		txtEventPast = (IjoomerTextView) findViewById(R.id.txtEventPast);
		txtEventPending = (IjoomerTextView) findViewById(R.id.txtEventPending);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		editEventSearch = (IjoomerEditText) findViewById(R.id.editEventSearch);
		btnEventCreate = (IjoomerButton) findViewById(R.id.btnEventCreate);
		provider = new JomEventDataProvider(this);
		getIntentData();

	}

	@Override
	public void prepareViews() {
		if (!IN_GROUP_ID.equals("0")) {

			if (IN_GROUP_CREATE_EVENT.equals("1")) {
				btnEventCreate.setVisibility(View.VISIBLE);
			}
			txtEventMy.setVisibility(View.GONE);
			txtEventPending.setVisibility(View.GONE);
			if (txtEventAll.getVisibility() == View.VISIBLE || txtEventMy.getVisibility() == View.VISIBLE || txtEventPast.getVisibility() == View.VISIBLE
					|| txtEventPending.getVisibility() == View.VISIBLE) {
				lnrEventHeader.setVisibility(View.VISIBLE);
			}
			editEventSearch.setVisibility(View.GONE);
			imgSearch.setVisibility(View.GONE);
			currentList = ALLEVENT;
			txtEventAll.setTextColor(getResources().getColor(R.color.jom_blue));

			if (groupEventsFragment == null) {
				groupEventsFragment = new JomEventGroupFragment();
			}
			addFragment(R.id.lnrFragment, groupEventsFragment);
		} else {
			btnEventCreate.setVisibility(View.VISIBLE);
			lnrEventHeader.setVisibility(View.VISIBLE);
			txtEventMy.setTextColor(getResources().getColor(R.color.jom_blue));

			if (myEventsFragment == null) {
				myEventsFragment = new JomEventMyFragment();
			}
			addFragment(R.id.lnrFragment, myEventsFragment);
		}

		if (!IjoomerGlobalConfiguration.isEventCreate()) {
			btnEventCreate.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (IjoomerApplicationConfiguration.isReloadRequired()) {
			IjoomerApplicationConfiguration.setReloadRequired(false);

			if (currentList.equals(ALLEVENT)) {
				if (!IN_GROUP_ID.equals("0")) {
					groupEventsFragment.update();
				} else {
					allEventsFragment.update();
				}
			} else if (currentList.equals(MYEVENT)) {
				myEventsFragment.update();
			} else if (currentList.equals(PASTEVENT)) {
				pastEventsFragment.update();
			} else if (currentList.equals(PENDINGEVENT)) {
				pendingEventsFragment.update();
			}
		}

	}

	@Override
	public void setActionListeners() {

		imgSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				if (editEventSearch.getText().toString().trim().length() > 0) {
					searchEventsFragment = new JomEventSearchFragment();
					searchEventsFragment.setSerachKeyword(editEventSearch.getText().toString());
					addFragment(R.id.lnrFragment, searchEventsFragment);
				} else {
					editEventSearch.setError(getString(R.string.validation_value_required));
				}
			}
		});
		editEventSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable edit) {
				if (edit.length() == 0) {
					if (currentList.equals(ALLEVENT)) {
						if (!IN_GROUP_ID.equals("0")) {
							if (groupEventsFragment == null) {
								groupEventsFragment = new JomEventGroupFragment();
							}
							addFragment(R.id.lnrFragment, groupEventsFragment);
						} else {
							if (allEventsFragment == null) {
								allEventsFragment = new JomEventAllFragment();
							}
							addFragment(R.id.lnrFragment, allEventsFragment);
						}
					} else if (currentList.equals(MYEVENT)) {
						if (myEventsFragment == null) {
							myEventsFragment = new JomEventMyFragment();
						}
						addFragment(R.id.lnrFragment, myEventsFragment);
					} else if (currentList.equals(PASTEVENT)) {
						if (pastEventsFragment == null) {
							pastEventsFragment = new JomEventPastFragment();
						}
						addFragment(R.id.lnrFragment, pastEventsFragment);
					} else if (currentList.equals(PENDINGEVENT)) {
						if (pendingEventsFragment == null) {
							pendingEventsFragment = new JomEventPendingFragment();
						}
						addFragment(R.id.lnrFragment, pendingEventsFragment);
					}
				}
			}
		});

		btnEventCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				provider.addOrEditEventFieldList("0", IN_GROUP_ID, new WebCallListener() {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(provider.getNotificationData());
							try {
								loadNew(JomEventCreateActivity.class, JomEventActivity.this, false, "IN_FIELD_LIST", data1, "IN_EVENT_ID", "0", "IN_GROUP_ID", IN_GROUP_ID);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							responseErrorMessageHandler(responseCode, false);
						}
					}
				});
			}
		});

		txtEventMy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!currentList.equals(MYEVENT)) {
					txtEventPast.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventPending.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventAll.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventMy.setTextColor(getResources().getColor(R.color.jom_blue));
					currentList = MYEVENT;
					if (myEventsFragment == null) {
						myEventsFragment = new JomEventMyFragment();
					}
					addFragment(R.id.lnrFragment, myEventsFragment);

				}
			}
		});

		txtEventAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!currentList.equals(ALLEVENT)) {
					txtEventPast.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventPending.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventAll.setTextColor(getResources().getColor(R.color.jom_blue));
					txtEventMy.setTextColor(getResources().getColor(R.color.jom_txt_color));

					currentList = ALLEVENT;
					if (!IN_GROUP_ID.equals("0")) {
						if (groupEventsFragment == null) {
							groupEventsFragment = new JomEventGroupFragment();
						}
						addFragment(R.id.lnrFragment, groupEventsFragment);
					} else {
						if (allEventsFragment == null) {
							allEventsFragment = new JomEventAllFragment();
						}
						addFragment(R.id.lnrFragment, allEventsFragment);
					}
				}
			}

		});

		txtEventPast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!currentList.equals(PASTEVENT)) {
					txtEventPast.setTextColor(getResources().getColor(R.color.jom_blue));
					txtEventPending.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventAll.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventMy.setTextColor(getResources().getColor(R.color.jom_txt_color));

					currentList = PASTEVENT;
					if (pastEventsFragment == null) {
						pastEventsFragment = new JomEventPastFragment();
					}
					addFragment(R.id.lnrFragment, pastEventsFragment);

				}
			}

		});

		txtEventPending.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!currentList.equals(PENDINGEVENT)) {
					txtEventPending.setTextColor(getResources().getColor(R.color.jom_blue));
					txtEventPast.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventAll.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtEventMy.setTextColor(getResources().getColor(R.color.jom_txt_color));

					currentList = PENDINGEVENT;
					if (pendingEventsFragment == null) {
						pendingEventsFragment = new JomEventPendingFragment();
					}
					addFragment(R.id.lnrFragment, pendingEventsFragment);

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
		IN_GROUP_CREATE_EVENT = getIntent().getStringExtra("IN_GROUP_CREATE_EVENT") == null ? "1" : getIntent().getStringExtra("IN_GROUP_CREATE_EVENT");
	}

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.event), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						if (IjoomerGlobalConfiguration.isEventCreate()) {
							btnEventCreate.setVisibility(View.VISIBLE);
						}
						
					}
				});
	}

}
