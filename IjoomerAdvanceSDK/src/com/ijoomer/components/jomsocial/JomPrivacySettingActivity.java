package com.ijoomer.components.jomsocial;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.MyCustomAdapter;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomPrivacyDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomPrivacySettingActivity.
 * 
 * @author tasol
 * 
 */
public class JomPrivacySettingActivity extends JomMasterActivity {

	private ListView lstPreferences;
	private IjoomerButton btnBack;
	private IjoomerButton btnEditSave;
	private SeekBar proSeekBar;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	ArrayList<HashMap<String, String>> fields;
	ArrayList<HashMap<String, String>> groups;
	private SmartListAdapterWithHolder privacyListAdapter;

	private JomPrivacyDataProvider provider;


	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_privacy_dynamic_view;
	}

	@Override
	public void initComponents() {
		lstPreferences = (ListView) findViewById(R.id.lstPreferences);
		btnBack = (IjoomerButton) findViewById(R.id.btnBack);
		btnEditSave = (IjoomerButton) findViewById(R.id.btnEditSave);
		provider = new JomPrivacyDataProvider(this);
		btnEditSave.setText(getString(R.string.save));
	}

	@Override
	public void prepareViews() {
		lstPreferences.setAdapter(null);
		getPreferencesSetting();
	}

	@Override
	public void setActionListeners() {
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnEditSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				savePreferencesSettings();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get user preferences.
	 */
	private void getPreferencesSetting() {
		proSeekBar = null;
		if (!provider.isPrivacySettingExists()) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		provider.getPrivacySetting(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (proSeekBar != null) {
					proSeekBar.setProgress(progressCount);
				}
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					prepareList();
					privacyListAdapter = getListAdapter();
					lstPreferences.setAdapter(privacyListAdapter);
				} else {
					responseErrorMessageHandler(responseCode, true);
				}
			}
		});
	}

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_loading_privacy_setting),
				getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to save preferences.
	 */
	private void savePreferencesSettings() {

		ArrayList<HashMap<String, String>> submitFields = new ArrayList<HashMap<String, String>>();
		int size = listData.size();
		for (int i = 0; i < size; i++) {
			@SuppressWarnings("unchecked")
			HashMap<String, String> row = (HashMap<String, String>) listData.get(i).getValues().get(0);

			if (row.size() > 1 && !(row.get("type").equals("label"))) {
				submitFields.add(row);
			}

		}

		final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_privacy_setting));
		provider.submitPrivacySetting(submitFields, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {

				} else {
					responseErrorMessageHandler(responseCode, false);
				}
			}
		});

	}

	
	/**
	 * List adapter for user preferences.
	 * @return
	 */
	private SmartListAdapterWithHolder getListAdapter() {

		SmartListAdapterWithHolder adapter = new SmartListAdapterWithHolder(this, R.layout.jom_privacy_dynamic_view_item, listData, new ItemView() {

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {
				holder.lnrGgroup = (LinearLayout) v.findViewById(R.id.lnrGgroup);
				holder.lnrLabel = (LinearLayout) v.findViewById(R.id.lnrLabel);
				holder.lnrEdit = (LinearLayout) v.findViewById(R.id.lnrEdit);
				holder.lnrEditArea = (LinearLayout) v.findViewById(R.id.lnrEditArea);
				holder.lnrEditClickable = (LinearLayout) v.findViewById(R.id.lnrEditClickable);
				holder.lnrSpin = (LinearLayout) v.findViewById(R.id.lnrSpin);
				holder.lnrIjoomerCheckBox = (LinearLayout) v.findViewById(R.id.lnrCheckbox);
				holder.lnrComplex = (LinearLayout) v.findViewById(R.id.lnrComplex);
				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				if (row.size() == 1) {
					holder.lnrGgroup.setVisibility(View.VISIBLE);

					holder.lnrLabel.setVisibility(View.GONE);
					holder.lnrEdit.setVisibility(View.GONE);
					holder.lnrEditArea.setVisibility(View.GONE);
					holder.lnrEditClickable.setVisibility(View.GONE);
					holder.lnrSpin.setVisibility(View.GONE);
					holder.lnrIjoomerCheckBox.setVisibility(View.GONE);
					holder.lnrComplex.setVisibility(View.GONE);

					((IjoomerTextView) holder.lnrGgroup.findViewById(R.id.txtLable)).setText(row.get(GROUP_NAME));
				} else if ((getStringArray(row.get("type")) != null)) {
					holder.lnrComplex.setVisibility(View.VISIBLE);

					holder.lnrLabel.setVisibility(View.GONE);
					holder.lnrEdit.setVisibility(View.GONE);
					holder.lnrEditArea.setVisibility(View.GONE);
					holder.lnrEditClickable.setVisibility(View.GONE);
					holder.lnrGgroup.setVisibility(View.GONE);
					holder.lnrSpin.setVisibility(View.GONE);
					holder.lnrIjoomerCheckBox.setVisibility(View.GONE);

					((IjoomerTextView) holder.lnrComplex.findViewById(R.id.txtLable)).setText(row.get(TITLE));

					final IjoomerCheckBox c1 = (IjoomerCheckBox) holder.lnrComplex.findViewById(R.id.chkValue1);
					final IjoomerCheckBox c2 = (IjoomerCheckBox) holder.lnrComplex.findViewById(R.id.chkValue2);
					final IjoomerCheckBox c3 = (IjoomerCheckBox) holder.lnrComplex.findViewById(R.id.chkValue3);

					if (getStringArray(row.get(VALUE))[0].equals("1")) {
						c1.setChecked(true);
					} else {
						c1.setChecked(false);
					}

					if (getStringArray(row.get(VALUE))[1].equals("1")) {
						c2.setChecked(true);
					} else {
						c2.setChecked(false);
					}

					if (getStringArray(row.get(VALUE))[2].equals("1")) {
						c3.setChecked(true);
					} else {
						c3.setChecked(false);
					}

					c1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								JSONArray arr = new JSONArray();
								String[] strVal = getStringArray(row.get(VALUE));
								arr.put(1, strVal[1]);
								arr.put(2, strVal[2]);
								arr.put(0, c1.isChecked() ? "1" : "0");
								row.put(VALUE, arr.toString());
							} catch (Exception e) {
							}
						}
					});
					c2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								JSONArray arr = new JSONArray();
								String[] strVal = getStringArray(row.get(VALUE));
								arr.put(0, strVal[0]);
								arr.put(2, strVal[2]);
								arr.put(1, c2.isChecked() ? "1" : "0");
								row.put(VALUE, arr.toString());
							} catch (Exception e) {
							}
						}
					});
					c3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								JSONArray arr = new JSONArray();
								String[] strVal = getStringArray(row.get(VALUE));
								arr.put(0, strVal[0]);
								arr.put(1, strVal[1]);
								arr.put(2, c3.isChecked() ? "1" : "0");
								row.put(VALUE, arr.toString());
							} catch (Exception e) {
							}
						}
					});

				} else if (row.get("type").equals("label")) {
					holder.lnrLabel.setVisibility(View.VISIBLE);

					holder.lnrComplex.setVisibility(View.GONE);
					holder.lnrEdit.setVisibility(View.GONE);
					holder.lnrEditArea.setVisibility(View.GONE);
					holder.lnrEditClickable.setVisibility(View.GONE);
					holder.lnrGgroup.setVisibility(View.GONE);
					holder.lnrSpin.setVisibility(View.GONE);
					holder.lnrIjoomerCheckBox.setVisibility(View.GONE);

					((IjoomerTextView) holder.lnrLabel.findViewById(R.id.txtLable)).setText(row.get(TITLE));
				} else if (row.get(TYPE).equals(CHECKBOX)) {
					final IjoomerCheckBox chb;

					holder.lnrIjoomerCheckBox.setVisibility(View.VISIBLE);

					holder.lnrComplex.setVisibility(View.GONE);
					holder.lnrLabel.setVisibility(View.GONE);
					holder.lnrEdit.setVisibility(View.GONE);
					holder.lnrEditArea.setVisibility(View.GONE);
					holder.lnrEditClickable.setVisibility(View.GONE);
					holder.lnrGgroup.setVisibility(View.GONE);
					holder.lnrSpin.setVisibility(View.GONE);

					chb = ((IjoomerCheckBox) holder.lnrIjoomerCheckBox.findViewById(R.id.txtValue));
					chb.setTextAppearance(JomPrivacySettingActivity.this, R.style.ijoomer_textview_h2);

					if (row.get(VALUE).toString().trim().length() > 0) {
						chb.setChecked(row.get(VALUE).toString().equals("1") ? true : false);
					}

					chb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton ButtonView, boolean isChecked) {
							try {
								row.put(VALUE, isChecked ? "1" : "0");
							} catch (Exception e) {
							}
						}
					});

					((IjoomerTextView) holder.lnrIjoomerCheckBox.findViewById(R.id.txtLable)).setText(row.get(TITLE));

				} else if (row.get(TYPE).equals(TEXT)) {
					final IjoomerEditText edit;
					holder.lnrEdit.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) holder.lnrEdit.findViewById(R.id.txtValue));
					edit.setText(row.get(VALUE));

				} else if (row.get(TYPE).equals(TEXTAREA)) {
					holder.lnrEditArea.setVisibility(View.VISIBLE);
					((IjoomerEditText) holder.lnrEditArea.findViewById(R.id.txtValue)).setText(row.get(VALUE));

				} else if (row.get(TYPE).equals(SELECT)) {

					holder.lnrSpin.setVisibility(View.VISIBLE);

					holder.lnrComplex.setVisibility(View.GONE);
					holder.lnrLabel.setVisibility(View.GONE);
					holder.lnrEdit.setVisibility(View.GONE);
					holder.lnrEditArea.setVisibility(View.GONE);
					holder.lnrEditClickable.setVisibility(View.GONE);
					holder.lnrGgroup.setVisibility(View.GONE);
					holder.lnrIjoomerCheckBox.setVisibility(View.GONE);

					((IjoomerTextView) holder.lnrSpin.findViewById(R.id.txtLable)).setText(row.get(TITLE));

					final MyCustomAdapter adapter = IjoomerUtilities.getSpinnerAdapter(row);
					final Spinner spnr = ((Spinner) holder.lnrSpin.findViewById(R.id.txtValue));

					spnr.setAdapter(adapter);
					spnr.setSelection(adapter.getDefaultPosition());
					adapter.notifyDataSetChanged();

					spnr.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							final String code = getPrivacyCode(spnr.getItemAtPosition(arg2).toString());
							row.put(VALUE, code);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});

				} else if (row.get(TYPE).equals(DATE)) {
					final IjoomerEditText edit;
					holder.lnrEditClickable.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) holder.lnrEditClickable.findViewById(R.id.txtValue));
					edit.setText(row.get(VALUE));
					edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), false, new CustomClickListner() {

								@Override
								public void onClick(String value) {
									((IjoomerEditText) v).setText(value);
								}
							});
						}
					});

				} else if (row.get(TYPE).equals(MULTIPLESELECT)) {
					final IjoomerEditText edit;
					holder.lnrEditClickable.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) holder.lnrEditClickable.findViewById(R.id.txtValue));
					edit.setText(row.get(VALUE));
					edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							IjoomerUtilities.getMultiSelectionDialog(row.get(TITLE), row.get(OPTIONS), "", new CustomClickListner() {

								@Override
								public void onClick(String value) {
									((IjoomerEditText) v).setText(value);
								}
							});

						}
					});
				}

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapter;
	}

	
	/**
	 * This method used to prepare list user preferences.
	 */
	public void prepareList() {

		listData.clear();
		groups = provider.getFieldGroups();
		int len = groups.size() - 1;
		for (int i = len; i >= 0; i--) {
			SmartListItem item = new SmartListItem();
			item.setItemLayout(R.layout.jom_privacy_dynamic_view_item);
			ArrayList<Object> obj = new ArrayList<Object>();
			obj.add(groups.get(i));
			item.setValues(obj);
			listData.add(item);

			fields = provider.getFields(groups.get(i).get(GROUP_NAME));
			for (HashMap<String, String> hashMap : fields) {
				SmartListItem item2 = new SmartListItem();
				item2.setItemLayout(R.layout.jom_privacy_dynamic_view_item);
				ArrayList<Object> obj2 = new ArrayList<Object>();
				obj2.add(hashMap);
				item2.setValues(obj2);
				listData.add(item2);
			}
		}

	}

}