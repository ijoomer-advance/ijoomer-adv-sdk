package com.ijoomer.components.jomsocial;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomGroupDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomGroupActivity.
 * 
 * @author tasol
 * 
 */
public class JomGroupActivity extends JomMasterActivity {

	private IjoomerTextView txtGroupAll;
	private IjoomerTextView txtGroupMy;
	private IjoomerTextView txtGroupPending;
	private ImageView imgSearch;
	private IjoomerEditText editGroupSearch;
	private IjoomerButton btnGroupCreate;

	private JomGroupDataProvider provider;
	private JomGroupMyFragment myGroupsFragment;
	private JomGroupAllFragment allGroupsFragment;
	private JomGroupPendingFragment pendingGroupsFragment;
	private JomGroupSearchFragment searchGroupsFragment;

	final private String ALLGROUP = "allGroup";
	final private String MYGROUP = "myGroup";
	final private String PENDINGGROUP = "pendingGroup";
	private String currentList = MYGROUP;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_group;
	}

	@Override
	public void initComponents() {
		provider = new JomGroupDataProvider(this);
		txtGroupAll = (IjoomerTextView) findViewById(R.id.txtGroupAll);
		txtGroupMy = (IjoomerTextView) findViewById(R.id.txtGroupMy);
		txtGroupPending = (IjoomerTextView) findViewById(R.id.txtGroupPending);
		editGroupSearch = (IjoomerEditText) findViewById(R.id.editGroupSearch);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		btnGroupCreate = (IjoomerButton) findViewById(R.id.btnGroupCreate);
	}

	@Override
	public void prepareViews() {
		if (!IjoomerGlobalConfiguration.isGroupCreate()) {
			btnGroupCreate.setVisibility(View.GONE);
		}
		txtGroupMy.setTextColor(getResources().getColor(R.color.jom_blue));

		if (myGroupsFragment == null) {
			myGroupsFragment = new JomGroupMyFragment();
		}
		addFragment(R.id.lnrFragment, myGroupsFragment);
	}

	@Override
	protected void onResume() {

		if (IjoomerApplicationConfiguration.isReloadRequired()) {
			IjoomerApplicationConfiguration.setReloadRequired(false);
			if (currentList == MYGROUP) {
				myGroupsFragment.update();
			} else if (currentList == ALLGROUP) {
				allGroupsFragment.update();
			} else if (currentList == PENDINGGROUP) {
				pendingGroupsFragment.update();
			} else {
				searchGroupsFragment.update();
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
				if (editGroupSearch.getText().toString().trim().length() > 0) {
					if (searchGroupsFragment == null) {
						searchGroupsFragment = new JomGroupSearchFragment();
					}
					searchGroupsFragment.setSerachKeyword(editGroupSearch.getText().toString());
					addFragment(R.id.lnrFragment, searchGroupsFragment);
				} else {
					editGroupSearch.setError(getString(R.string.validation_value_required));
				}
			}
		});
		editGroupSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable edit) {
				if (edit.length() == 0) {
					if (currentList == MYGROUP) {
						if (myGroupsFragment == null) {
							myGroupsFragment = new JomGroupMyFragment();
						}
						addFragment(R.id.lnrFragment, myGroupsFragment);

					} else if (currentList == ALLGROUP) {
						if (allGroupsFragment == null) {
							allGroupsFragment = new JomGroupAllFragment();
						}
						addFragment(R.id.lnrFragment, allGroupsFragment);
					} else {
						txtGroupMy.setTextColor(getResources().getColor(R.color.jom_txt_color));
						txtGroupAll.setTextColor(getResources().getColor(R.color.jom_blue));
						txtGroupPending.setTextColor(getResources().getColor(R.color.jom_txt_color));

						if (allGroupsFragment == null) {
							allGroupsFragment = new JomGroupAllFragment();
						}
						addFragment(R.id.lnrFragment, allGroupsFragment);

					}

				}
			}
		});

		btnGroupCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				provider.addOrEditGroupFieldList("0", new WebCallListener() {
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
								loadNew(JomGroupCreateActivity.class, JomGroupActivity.this, false, "IN_FIELD_LIST", data1, "IN_GROUP_ID", "0");
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

		txtGroupMy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (currentList != MYGROUP) {
					currentList = MYGROUP;
					txtGroupMy.setTextColor(getResources().getColor(R.color.jom_blue));
					txtGroupAll.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupPending.setTextColor(getResources().getColor(R.color.jom_txt_color));
					if (myGroupsFragment == null) {
						myGroupsFragment = new JomGroupMyFragment();
					}
					addFragment(R.id.lnrFragment, myGroupsFragment);
				}
			}
		});

		txtGroupAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentList != ALLGROUP) {
					currentList = ALLGROUP;
					txtGroupMy.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupAll.setTextColor(getResources().getColor(R.color.jom_blue));
					txtGroupPending.setTextColor(getResources().getColor(R.color.jom_txt_color));
					if (allGroupsFragment == null) {
						allGroupsFragment = new JomGroupAllFragment();
					}
					addFragment(R.id.lnrFragment, allGroupsFragment);
				}
			}

		});

		txtGroupPending.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentList != PENDINGGROUP) {
					currentList = PENDINGGROUP;
					txtGroupMy.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupAll.setTextColor(getResources().getColor(R.color.jom_txt_color));
					txtGroupPending.setTextColor(getResources().getColor(R.color.jom_blue));
					if (pendingGroupsFragment == null) {
						pendingGroupsFragment = new JomGroupPendingFragment();
					}
					addFragment(R.id.lnrFragment, pendingGroupsFragment);
				}
			}
		});

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class method
	 */

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

}
