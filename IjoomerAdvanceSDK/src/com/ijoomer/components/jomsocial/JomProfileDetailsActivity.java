package com.ijoomer.components.jomsocial;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.MyCustomAdapter;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomProfileDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomProfileDetailsActivity.
 * 
 * @author tasol
 * 
 */
public class JomProfileDetailsActivity extends JomMasterActivity {

	private LinearLayout lnr_form;
	private IjoomerEditText editMap;
	private IjoomerButton btnBack;
	private IjoomerButton btnEditSave;

	private ArrayList<HashMap<String, String>> oldFieldData = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> fields;
	ArrayList<HashMap<String, String>> groups;
	private JomProfileDataProvider dataProvider;

	final private int GET_ADDRESS_FROM_MAP = 1;
	private String IN_USERID;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_dynamic_view;
	}

	@Override
	public void initComponents() {
		lnr_form = (LinearLayout) findViewById(R.id.lnr_form);
		btnBack = (IjoomerButton) findViewById(R.id.btnBack);
		btnEditSave = (IjoomerButton) findViewById(R.id.btnEditSave);

		dataProvider = new JomProfileDataProvider(this);

		getIntentData();
	}

	@Override
	public void prepareViews() {
		createForm();
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
				if (((IjoomerButton) v).getText().toString().equalsIgnoreCase(getString(R.string.save))) {
					updateUserDetails();
				} else {
					((IjoomerButton) v).setText(getString(R.string.save));
					setEditable(true);
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GET_ADDRESS_FROM_MAP) {
				editMap.setText(((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("address"));
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");

		if (!IN_USERID.equals("0")) {
			btnEditSave.setVisibility(View.GONE);
		}
	}

	
	/**
	 * This method used to set editable view for user details.
	 * @param isEditable represented is editable
	 */
	@SuppressWarnings("unchecked")
	private void setEditable(boolean isEditable) {

		int size = lnr_form.getChildCount();
		if (isEditable) {
			for (int i = 0; i < size; i++) {

				View v = lnr_form.getChildAt(i);
				HashMap<String, String> row = (HashMap<String, String>) v.getTag();
				if (((LinearLayout) v.findViewById(R.id.lnrGgroup)).getVisibility() == View.VISIBLE) {

				} else {
					if (row.get(TYPE).equals(TEXT)) {
						((LinearLayout) v.findViewById(R.id.lnrEdit)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(TEXTAREA)) {
						((LinearLayout) v.findViewById(R.id.lnrEditArea)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(DATE)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(TIME)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(SELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrSpin)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(MAP)) {
						((LinearLayout) v.findViewById(R.id.lnrEditMap)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(LABEL)) {
						((LinearLayout) v.findViewById(R.id.lnrLabel)).setVisibility(View.VISIBLE);
					}else if (row.get(TYPE).equals(MULTIPLESELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.VISIBLE);
					}

					((LinearLayout) v.findViewById(R.id.lnrReadOnly)).setVisibility(View.GONE);
				}

			}
		} else {
			for (int i = 0; i < size; i++) {

				View v = lnr_form.getChildAt(i);
				HashMap<String, String> row = (HashMap<String, String>) v.getTag();
				if (((LinearLayout) v.findViewById(R.id.lnrGgroup)).getVisibility() == View.VISIBLE) {

				} else {
					if (row.get(TYPE).equals(TEXT)) {
						((LinearLayout) v.findViewById(R.id.lnrEdit)).setVisibility(View.GONE);
					} else if (row.get(TYPE).equals(TEXTAREA)) {
						((LinearLayout) v.findViewById(R.id.lnrEditArea)).setVisibility(View.GONE);
					} else if (row.get(TYPE).equals(DATE)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.GONE);
					} else if (row.get(TYPE).equals(TIME)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(SELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrSpin)).setVisibility(View.GONE);
					} else if (row.get(TYPE).equals(MAP)) {
						((LinearLayout) v.findViewById(R.id.lnrEditMap)).setVisibility(View.GONE);
					}else if (row.get(TYPE).equals(LABEL)) {
						((LinearLayout) v.findViewById(R.id.lnrLabel)).setVisibility(View.GONE);
					}else if (row.get(TYPE).equals(MULTIPLESELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.GONE);
					}

					((LinearLayout) v.findViewById(R.id.lnrReadOnly)).setVisibility(View.VISIBLE);

				}

			}
		}

	}

	
	/**
	 * This method used to update user details.
	 */
	private void updateUserDetails() {

		boolean validationFlag = true;
		final ArrayList<HashMap<String, String>> userDetailsField = new ArrayList<HashMap<String, String>>();
		int size = lnr_form.getChildCount();
		for (int i = 0; i < size; i++) {
			LinearLayout v = (LinearLayout) lnr_form.getChildAt(i);
			@SuppressWarnings("unchecked")
			HashMap<String, String> field = (HashMap<String, String>) v.getTag();
			IjoomerEditText edtValue = null;
			Spinner spnrValue = null;
			ImageView imgPrivacyValue = null;
			LinearLayout readOnly = ((LinearLayout) v.findViewById(R.id.lnrReadOnly));
			IjoomerEditText edtReadOnly = (IjoomerEditText) readOnly.findViewById(R.id.txtValue);
			if (field != null) {
				if (field.get(TYPE).equals(TEXT)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEdit)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrEdit)).findViewById(R.id.imgPrivacyValue);
				} else if (field.get(TYPE).equals(TEXTAREA)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditArea)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrEditArea)).findViewById(R.id.imgPrivacyValue);
				} else if (field.get("type").equals(MAP)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditMap)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrEditMap)).findViewById(R.id.imgPrivacyValue);
				}else if (field.get("type").equals(LABEL)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrLabel)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrLabel)).findViewById(R.id.imgPrivacyValue);
				}else if (field.get(TYPE).equals(DATE)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.imgPrivacyValue);

					if (edtValue.getText().toString().trim().length() > 0) {
						if (!IjoomerUtilities.birthdateValidator(edtValue.getText().toString().trim())) {
							edtValue.setFocusable(true);
							edtValue.setError(getString(R.string.validation_invalid_birth_date));
							validationFlag = false;
						}
					}
				}else if (field.get(TYPE).equals(MULTIPLESELECT)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.imgPrivacyValue);
				}else if (field.get(TYPE).equals(TIME)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.imgPrivacyValue);
				}

				if (field.get(TYPE).equals(SELECT)) {
					spnrValue = (Spinner) ((LinearLayout) v.findViewById(R.id.lnrSpin)).findViewById(R.id.txtValue);
					imgPrivacyValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrSpin)).findViewById(R.id.imgPrivacyValue);
					try {
						JSONArray options = new JSONArray(field.get(OPTIONS));
						field.put(VALUE, ((JSONObject) options.get(spnrValue.getSelectedItemPosition())).getString(VALUE));
						if (((JSONObject) options.get(spnrValue.getSelectedItemPosition())).has("title")) {
							edtReadOnly.setText(((JSONObject) options.get(spnrValue.getSelectedItemPosition())).getString("title"));
						} else {
							edtReadOnly.setText(((JSONObject) options.get(spnrValue.getSelectedItemPosition())).getString(VALUE));
						}
						JSONObject privacy = new JSONObject(field.get(PRIVACY));
						privacy.put(VALUE, imgPrivacyValue.getTag().toString());
						field.put(PRIVACY, privacy.toString());
					} catch (Throwable e) {
						field.put(VALUE, "");
						try {
							JSONObject privacy = new JSONObject(field.get(PRIVACY));
							privacy.put(VALUE, "");
							field.put(PRIVACY, privacy.toString());
						} catch (Throwable e1) {
						}
					}
					userDetailsField.add(field);
				} else if (edtValue != null && edtValue.getText().toString().trim().length() <= 0 && (field.get(REQUIRED).equals("1"))) {
					edtValue.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				} else {
					field.put(VALUE, edtValue.getText().toString().trim());
					try {
						JSONObject privacy = new JSONObject(field.get(PRIVACY));
						privacy.put(VALUE, imgPrivacyValue.getTag().toString());
						field.put(PRIVACY, privacy.toString());
					} catch (Throwable e) {
						e.printStackTrace();
					}
					userDetailsField.add(field);
				}
				if (spnrValue == null) {
					edtReadOnly.setText(edtValue.getText());
				}

			}
		}

		if (validationFlag) {
			if (isDetailsChange(userDetailsField)) {

				final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
				dataProvider.updateUserDetails(userDetailsField, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							updateHeader(dataProvider.getNotificationData());
							IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_loading_profile), getString(R.string.profile_updated_successfully), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@SuppressWarnings("unchecked")
								@Override
								public void NeutralMethod() {
									setEditable(false);
									btnBack.setText(getString(R.string.back));
									btnEditSave.setText(getString(R.string.edit));
									oldFieldData.clear();
									for (HashMap<String, String> hashMap : userDetailsField) {
										oldFieldData.add((HashMap<String, String>) hashMap.clone());
									}
								}
							});

						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_loading_profile), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				});
			} else {
				setEditable(false);
				btnBack.setText(getString(R.string.back));
				btnEditSave.setText(getString(R.string.edit));
			}
		}
	}

	
	/**
	 * This method used to create dynamic form user details.
	 */
	private void createForm() {
		groups = dataProvider.getFieldGroups();
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;

		int size = groups.size();
		for (int i = 0; i < size; i++) {
			// create group header

			View groupView = inflater.inflate(R.layout.ijoomer_dynamic_view_item, null);
			((LinearLayout) groupView.findViewById(R.id.lnrGgroup)).setVisibility(View.VISIBLE);
			((IjoomerTextView) groupView.findViewById(R.id.txtLable)).setText(groups.get(i).get(GROUP_NAME));

			lnr_form.addView(groupView, params);

			fields = dataProvider.getFields(groups.get(i).get(GROUP_NAME));
			LinearLayout layout = null;

			int len = fields.size();
			for (int j = 0; j < len; j++) {
				final HashMap<String, String> field = fields.get(j);
				final View fieldView = inflater.inflate(R.layout.ijoomer_dynamic_view_item, null);

				final ImageView imgPrivacyValue;
				final Spinner spnWhoCanSee;
				final ImageView imgPrivacyValueReadOnly;
				if (field.get(TYPE).equals(LABEL)) {
					final IjoomerEditText edit;
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrLabel));
					layout.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
					if (field.get("value").toString().trim().length() > 0) {
						edit.setText(field.get("value"));
					} else {
					}
				} else if (field.get(TYPE).equals(TEXT)) {
					final IjoomerEditText edit;
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
					layout.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
					if (field.get("value").toString().trim().length() > 0) {
						edit.setText(field.get("value"));
					} else {
					}
					if (field.get("caption").contains("phone") || field.get("caption").contains(getString(R.string.year))) {
						edit.setInputType(InputType.TYPE_CLASS_NUMBER);
					} else if (field.get("caption").contains(getString(R.string.website)) || field.get("caption").contains(getString(R.string.email))) {
						edit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
					}
				} else if (field.get(TYPE).equals(TEXTAREA)) {
					final IjoomerEditText edit;
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
					layout.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));

					if (field.get("value").toString().trim().length() > 0) {
						edit.setText(field.get("value"));
					} else {
					}
				} else if (field.get(TYPE).equals(MAP)) {
					final IjoomerEditText edit;
					final ImageView imgMap;
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditMap));
					layout.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
					imgMap = ((ImageView) layout.findViewById(R.id.imgMap));
					if (field.get("value").toString().trim().length() > 0) {
						edit.setText(field.get("value"));
					} else {
						if (field.get("caption").equalsIgnoreCase(getString(R.string.state))) {
							try {
								Address address = IjoomerUtilities.getAddressFromLatLong(0, 0);
								edit.setText(address.getAdminArea().replace(address.getCountryName() == null ? "" : address.getCountryName(), "").replace(address.getPostalCode() == null ? "" : address.getPostalCode(), ""));
							} catch (Throwable e) {
								edit.setText("");
							}
						} else if (field.get("caption").equalsIgnoreCase(getString(R.string.city_town))) {
							try {
								Address address = IjoomerUtilities.getAddressFromLatLong(0, 0);
								edit.setText(address.getSubAdminArea());
							} catch (Throwable e) {
								edit.setText("");
							}
						} else {
							edit.setText(field.get("value"));
						}
					}

					imgMap.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							editMap = edit;
							Intent intent = new Intent(JomProfileDetailsActivity.this, IjoomerMapAddress.class);
							startActivityForResult(intent, GET_ADDRESS_FROM_MAP);
						}
					});

				} else if (field.get(TYPE).equals(SELECT)) {
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrSpin));
					layout.setVisibility(View.VISIBLE);
					final Spinner spn;

					spn = ((Spinner) layout.findViewById(R.id.txtValue));
					MyCustomAdapter adapter = IjoomerUtilities.getSpinnerAdapter(field);
					spn.setAdapter(adapter);
					spn.setSelection(adapter.getDefaultPosition());

					if (field.get("value").toString().trim().length() <= 0) {
						if (field.get("caption").equalsIgnoreCase(getString(R.string.country))) {

							try {
								Address address = IjoomerUtilities.getAddressFromLatLong(0, 0);
								String country = address.getCountryName();
								int selectedIndex = 0;
								JSONArray jsonArray = null;

								jsonArray = new JSONArray(field.get("options"));
								int optionSize = jsonArray.length();
								for (int k = 0; k < optionSize; k++) {
									JSONObject options = (JSONObject) jsonArray.get(k);

									if (options.getString("value").equalsIgnoreCase(country)) {
										selectedIndex = k;
										break;
									}
								}
								spn.setSelection(selectedIndex);
							} catch (Throwable e) {
								e.printStackTrace();
								spn.setSelection(0);
							}
						}

					}
				} else if (field.get(TYPE).equals(DATE)) {
					final IjoomerEditText edit;
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
					layout.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
					edit.setText(field.get(VALUE));
					edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), true, new CustomClickListner() {

								@Override
								public void onClick(String value) {
									((IjoomerEditText) v).setText(value);
									((IjoomerEditText) v).setError(null);
								}
							});

						}
					});
				}else if (field.get(TYPE).equals(TIME)) {
					final IjoomerEditText edit;
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
					layout.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
					edit.setText(field.get(VALUE));
					edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							IjoomerUtilities.getTimeDialog(((IjoomerEditText) v).getText().toString(), new CustomClickListner() {

								@Override
								public void onClick(String value) {
									((IjoomerEditText) v).setText(value);
									((IjoomerEditText) v).setError(null);
								}
							});

						}
					});

				} else if (field.get(TYPE).equals(MULTIPLESELECT)) {
					final IjoomerEditText edit;
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
					layout.setVisibility(View.VISIBLE);
					edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
					edit.setText(field.get(VALUE));
					edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(final View v) {
							IjoomerUtilities.getMultiSelectionDialog(field.get(CAPTION), field.get(OPTIONS), ((IjoomerEditText) v).getText().toString(), new CustomClickListner() {

								@Override
								public void onClick(String value) {
									((IjoomerEditText) v).setText(value);
								}
							});

						}
					});
				}

				try {
					if (field.get(REQUIRED).equalsIgnoreCase("1")) {
						((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION) + " *");
					} else {
						((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION));
					}
				} catch (Exception e) {
					((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION));
				}

				imgPrivacyValue = ((ImageView) layout.findViewById(R.id.imgPrivacyValue));
				spnWhoCanSee = ((Spinner) layout.findViewById(R.id.spnWhoCanSee));
				spnWhoCanSee.setAdapter(IjoomerUtilities.getPrivacySpinnerAdapter(field));
				spnWhoCanSee.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
						if (getPrivacyCodeAtIndex(pos).equals("0")) {
							imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_public);
							((ImageView) ((LinearLayout) fieldView.findViewById(R.id.lnrReadOnly)).findViewById(R.id.imgPrivacyValue)).setImageResource(R.drawable.ijoomer_privacy_public);
						} else if (getPrivacyCodeAtIndex(pos).equals("20")) {
							imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_sitemember);
							((ImageView) ((LinearLayout) fieldView.findViewById(R.id.lnrReadOnly)).findViewById(R.id.imgPrivacyValue)).setImageResource(R.drawable.ijoomer_privacy_sitemember);
						} else if (getPrivacyCodeAtIndex(pos).equals("30")) {
							imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_friend);
							((ImageView) ((LinearLayout) fieldView.findViewById(R.id.lnrReadOnly)).findViewById(R.id.imgPrivacyValue)).setImageResource(R.drawable.ijoomer_privacy_friend);
						} else if (getPrivacyCodeAtIndex(pos).equals("40")) {
							imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_onlyme);
							((ImageView) ((LinearLayout) fieldView.findViewById(R.id.lnrReadOnly)).findViewById(R.id.imgPrivacyValue)).setImageResource(R.drawable.ijoomer_privacy_onlyme);
						}
						try {
							imgPrivacyValue.setTag(((JSONObject) new JSONObject(field.get("privacy")).getJSONArray("options").get(pos)).getString("value"));
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

				try {
					if (new JSONObject(field.get("privacy")).getString("value").equals("0")) {
						imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_public);
					} else if (new JSONObject(field.get("privacy")).getString("value").equals("20")) {
						imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_sitemember);
					} else if (new JSONObject(field.get("privacy")).getString("value").equals("30")) {
						imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_friend);
					} else if (new JSONObject(field.get("privacy")).getString("value").equals("40")) {
						imgPrivacyValue.setImageResource(R.drawable.ijoomer_privacy_onlyme);
					}
					imgPrivacyValue.setTag(new JSONObject(field.get("privacy")).getString("value"));
					int privacySize = new JSONObject(field.get("privacy")).getJSONArray("options").length();
					for (int k = 0; k < privacySize; k++) {
						if (((JSONObject) new JSONObject(field.get("privacy")).getJSONArray("options").get(k)).getString("value").equals(new JSONObject(field.get("privacy")).getString("value"))) {
							spnWhoCanSee.setSelection(k);
							break;
						}
					}
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
				imgPrivacyValue.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						spnWhoCanSee.performClick();
					}
				});

				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrReadOnly));
				layout.setVisibility(View.VISIBLE);

				((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION));
				imgPrivacyValueReadOnly = (ImageView) layout.findViewById(R.id.imgPrivacyValue);
				try {
					if (new JSONObject(field.get("privacy")).getString("value").equals("0")) {
						imgPrivacyValueReadOnly.setImageResource(R.drawable.ijoomer_privacy_public);
					} else if (new JSONObject(field.get("privacy")).getString("value").equals("20")) {
						imgPrivacyValueReadOnly.setImageResource(R.drawable.ijoomer_privacy_sitemember);
					} else if (new JSONObject(field.get("privacy")).getString("value").equals("30")) {
						imgPrivacyValueReadOnly.setImageResource(R.drawable.ijoomer_privacy_friend);
					} else if (new JSONObject(field.get("privacy")).getString("value").equals("40")) {
						imgPrivacyValueReadOnly.setImageResource(R.drawable.ijoomer_privacy_onlyme);
					}
					imgPrivacyValueReadOnly.setTag(new JSONObject(field.get("privacy")).getString("value"));
				} catch (Throwable e) {
				}
				if (field.get(TYPE).equals(SELECT)) {
					try {
						((IjoomerEditText) layout.findViewById(R.id.txtValue)).setText(((Spinner) ((LinearLayout) fieldView.findViewById(R.id.lnrSpin)).findViewById(R.id.txtValue)).getSelectedItem().toString());
					} catch (Throwable e) {
					}
				} else {
					final IjoomerEditText edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
					if (field.get(CAPTION).contains(getString(R.string.phone)) && field.get(VALUE).toString().trim().length() > 0) {
						if (field.get(VALUE).toString().trim().length() > 0) {
							edit.setTextColor(getResources().getColor(R.color.jom_blue));
							edit.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									String number = "tel:" + field.get(VALUE).toString().trim();
									Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
									startActivity(callIntent);
								}
							});
						}
					} else if (field.get(CAPTION).contains(getString(R.string.website)) && field.get(VALUE).toString().trim().length() > 0) {
						if (field.get(VALUE).toString().trim().length() > 0) {
							edit.setTextColor(getResources().getColor(R.color.jom_blue));
							edit.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									try {
										loadNew(IjoomerWebviewClient.class, JomProfileDetailsActivity.this, false, "url", field.get(VALUE).toString().trim());
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}
							});
						}
					} else if (field.get(CAPTION).equalsIgnoreCase(getString(R.string.email)) && field.get(VALUE).toString().trim().length() > 0) {
						if (field.get(VALUE).toString().trim().length() > 0) {
							edit.setTextColor(getResources().getColor(R.color.jom_blue));
							edit.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Intent intent = new Intent(Intent.ACTION_SEND);
									intent.setType("text/plain");
									intent.putExtra(Intent.EXTRA_EMAIL, field.get(VALUE).toString().trim());
									intent.putExtra(Intent.EXTRA_SUBJECT, "");
									intent.putExtra(Intent.EXTRA_TEXT, "");

									startActivity(Intent.createChooser(intent, "Send Email"));
								}
							});
						}
					} else if (field.get(CAPTION).equalsIgnoreCase(getString(R.string.state)) || field.get(CAPTION).equalsIgnoreCase(getString(R.string.country)) || field.get(CAPTION).equalsIgnoreCase(getString(R.string.city_town)) && field.get(VALUE).toString().trim().length() > 0) {
						if (field.get(VALUE).toString().trim().length() > 0) {
							try {
								Address address = IjoomerUtilities.getLatLongFromAddress(field.get(VALUE).toString());

								if (String.valueOf(address.getLatitude()).trim().length() > 0 && String.valueOf(address.getLongitude()).trim().length() > 0) {
									edit.setTextColor(getResources().getColor(R.color.jom_blue));
									edit.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											try {
												Address address = IjoomerUtilities.getLatLongFromAddress(field.get(VALUE).toString());

												if (String.valueOf(address.getLatitude()).trim().length() > 0 && String.valueOf(address.getLongitude()).trim().length() > 0) {
													ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
													HashMap<String, String> map = new HashMap<String, String>();
													map.put("lat", String.valueOf(address.getLatitude()));
													map.put("long", String.valueOf(address.getLongitude()));
													list.add(map);
													loadNew(JomMapActivity.class, JomProfileDetailsActivity.this, false, "IN_MAPLIST", list, "IS_SHOW_BUBBLE", false);
												} else {
													edit.setTextColor(getResources().getColor(R.color.jom_txt_color));
													ting(getString(R.string.lat_long_not_found));
												}
											} catch (Throwable e) {
												e.printStackTrace();
												edit.setTextColor(getResources().getColor(R.color.jom_txt_color));
												ting(getString(R.string.lat_long_not_found));
											}
										}
									});
								}
							} catch (Exception e) {
							}
						}

					}
					edit.setText(field.get(VALUE));
				}
				((LinearLayout) fieldView.findViewById(R.id.lnrEdit)).setVisibility(View.GONE);
				((LinearLayout) fieldView.findViewById(R.id.lnrEditArea)).setVisibility(View.GONE);
				((LinearLayout) fieldView.findViewById(R.id.lnrSpin)).setVisibility(View.GONE);
				((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable)).setVisibility(View.GONE);
				((LinearLayout) fieldView.findViewById(R.id.lnrEditMap)).setVisibility(View.GONE);
				((LinearLayout) fieldView.findViewById(R.id.lnrLabel)).setVisibility(View.GONE);

				fieldView.setTag(field);
				lnr_form.addView(fieldView, params);
			}

		}
	}

	
	/**
	 * This method used to check is user details edit.
	 * @param userDetailFields represented user detail data
	 * @return represented {@link Boolean}
	 */
	private boolean isDetailsChange(ArrayList<HashMap<String, String>> userDetailFields) {
		ArrayList<HashMap<String, String>> oldFields;
		if (oldFieldData.size() > 0) {
			oldFields = oldFieldData;
		} else {
			oldFields = new JomProfileDataProvider(this).getFields();
		}
		int i;
		int size = oldFields.size();
		for (i = 0; i < size; i++) {

			try {
				if (!oldFields.get(i).get(VALUE).equals(userDetailFields.get(i).get(VALUE)) || !new JSONObject(oldFields.get(i).get(PRIVACY)).getString(VALUE).equals(new JSONObject(userDetailFields.get(i).get(PRIVACY)).getString(VALUE))) {
					break;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (i == size) {
			return false;
		} else {
			return true;
		}
	}

}
