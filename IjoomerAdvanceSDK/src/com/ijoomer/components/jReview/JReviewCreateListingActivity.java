package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public class JReviewCreateListingActivity extends JReviewMasterActivity {

	private String IN_CATEGORYID;
	private String IN_ARTICLEID;
	private boolean IS_EDIT;

	private TextView headerTitle;
	private LinearLayout lnr_form;
	private IjoomerEditText edtTitle;
	private IjoomerEditText edtTitleAlias;
	private IjoomerEditText edtSummary;
	private IjoomerEditText edtDiscription;
	private IjoomerEditText edtMetaDiscription;
	private IjoomerEditText edtMetaKeywords;
	private IjoomerEditText editMap;
	private IjoomerButton btnBack;
	private IjoomerButton btnSubmit;

	private ArrayList<HashMap<String, String>> artcledetails;
	private ArrayList<HashMap<String, String>> fields;
	private ArrayList<HashMap<String, String>> groups;
	private HashMap<String, String> selectedPlan = new HashMap<String, String>();

	private JReviewDataProvider dataProvider;

	final private int GET_ADDRESS_FROM_MAP = 1;

	private IjoomerCaching iCaching;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jreview_create_article_form;
	}

	@Override
	public void initComponents() {
		getIntentData();

		iCaching = new IjoomerCaching(this);
		dataProvider = new JReviewDataProvider(this);
		headerTitle = ((IjoomerTextView) getHeaderView().findViewById(R.id.txtHeader));
		edtTitle = (IjoomerEditText) findViewById(R.id.jreviewlistingtitle);
		edtTitleAlias = (IjoomerEditText) findViewById(R.id.jreviewlistingtitlealias);
		edtSummary = (IjoomerEditText) findViewById(R.id.jreviewlistingsummary);
		edtDiscription = (IjoomerEditText) findViewById(R.id.jreviewlistingdiscription);
		edtMetaDiscription = (IjoomerEditText) findViewById(R.id.jreviewlistingmetadiscription);
		edtMetaKeywords = (IjoomerEditText) findViewById(R.id.jreviewlistingmetakeywords);
		lnr_form = (LinearLayout) findViewById(R.id.lnr_form);
		btnBack = (IjoomerButton) findViewById(R.id.btnBack);
		btnSubmit = (IjoomerButton) findViewById(R.id.btnSubmit);
	}

	@Override
	public void prepareViews() {
		if(IS_EDIT){
			headerTitle.setText(getString(R.string.jreview_edit_article));
			edtTitle.setText(artcledetails.get(0).get(ARTICLENAME));
			edtSummary.setText(artcledetails.get(0).get(INTROTEXT));
			edtDiscription.setText(artcledetails.get(0).get(FULLTEXT));
			btnSubmit.setText(getString(R.string.jreview_save));
		}else{
			headerTitle.setText(getString(R.string.jreview_new_article));
			btnSubmit.setText(getString(R.string.jreview_create));
		}

		createForm();

		setEditable(true);
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
	public void setActionListeners() {
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hideSoftKeyboard();
				submitNewListing();
			}
		});
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to set editable view registration form.
	 * @param isEditable
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
					}else if (row.get(TYPE).equals(SELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrSpin)).setVisibility(View.VISIBLE);
					} else if (row.get(TYPE).equals(MULTIPLESELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.VISIBLE);
					}else if (row.get(TYPE).equals(LABEL)) {
						((LinearLayout) v.findViewById(R.id.lnrLabel)).setVisibility(View.VISIBLE);
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
					}else if (row.get(TYPE).equals(TIME)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.GONE);
					} else if (row.get(TYPE).equals(SELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrSpin)).setVisibility(View.GONE);
					} else if (row.get(TYPE).equals(MULTIPLESELECT)) {
						((LinearLayout) v.findViewById(R.id.lnrEditClickable)).setVisibility(View.GONE);
					}else if (row.get(TYPE).equals(LABEL)) {
						((LinearLayout) v.findViewById(R.id.lnrLabel)).setVisibility(View.GONE);
					}

					((LinearLayout) v.findViewById(R.id.lnrReadOnly)).setVisibility(View.VISIBLE);
				}
			}
		}
	}


	/**
	 * This method used to sumit registration form data.
	 */
	private void submitNewListing() {
		try{
			boolean validationFlag = true;

			if(edtTitle.getText().toString().length() <= 0){
				validationFlag = false;
			}

			JSONArray listingFields = new JSONArray();
			JSONObject fields = new JSONObject();
			int size = lnr_form.getChildCount();
			for (int i = 0; i < size; i++) {
				LinearLayout v = (LinearLayout) lnr_form.getChildAt(i);
				@SuppressWarnings("unchecked")
				HashMap<String, String> field = (HashMap<String, String>) v.getTag();
				IjoomerEditText edtValue = null;
				Spinner spnrValue = null;
				if (field != null) {
					if (field.get(TYPE).equals(TEXT)) {
						edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEdit)).findViewById(R.id.txtValue);
					} else if (field.get(TYPE).equals(TEXTAREA)) {
						edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditArea)).findViewById(R.id.txtValue);
					} else if (field.get(TYPE).equals(MAP)) {
						edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditMap)).findViewById(R.id.txtValue);
					} else if (field.get(TYPE).equals(LABEL)) {
						edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrLabel)).findViewById(R.id.txtValue);
					}else if (field.get(TYPE).equals(DATE)) {
						edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
						if (edtValue.getText().toString().trim().length() > 0) {
							if (!IjoomerUtilities.birthdateValidator(edtValue.getText().toString().trim())) {
								edtValue.setFocusable(true);
								edtValue.setError(getString(R.string.validation_invalid_birth_date));
								validationFlag = false;
							}
						}
					} else if (field.get(TYPE).equals(MULTIPLESELECT)) {
						edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
					}

					if (field.get(TYPE).equals(TIME)) {
						edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
					}

					if (field.get(TYPE).equals(SELECT)) {
						spnrValue = (Spinner) ((LinearLayout) v.findViewById(R.id.lnrSpin)).findViewById(R.id.txtValue);
						field.put(VALUE, spnrValue.getSelectedItem().toString());
						fields.put(field.get(FIELDNAME),field.get(VALUE));
					} else if (edtValue != null && edtValue.getText().toString().trim().length() <= 0 && (field.get(REQUIRED).equals("1"))) {
						edtValue.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					} else {
						field.put(VALUE, edtValue.getText().toString().trim());
						fields.put(field.get(FIELDNAME),field.get(VALUE));
					}
				}
			}
			listingFields.put(fields);

			if (validationFlag) {
				final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_create_article));
				dataProvider.submitNewListing(IN_CATEGORYID, IN_ARTICLEID, selectedPlan.get(PLANID), selectedPlan.get(ISSUBSCRIPTION), edtTitle.getText().toString(),
						edtTitleAlias.getText().toString() , edtSummary.getText().toString(), edtDiscription.getText().toString(), edtMetaKeywords.getText().toString()
						, edtMetaDiscription.getText().toString(), listingFields, new WebCallListener() {

					@Override
					public void onProgressUpdate(int progressCount) {
						proSeekBar.setProgress(progressCount);
					}

					@Override
					public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						if (responseCode == 200) {
							if(IS_EDIT){
								IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_edit_article), getString(R.string.jreview_listing_updated_successfully), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {
										getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
										getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, true);
										
										finish();
									}
								});
							}else{
								IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_new_article), getString(R.string.jreview_listing_created_successfully), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {
										getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
										finish();
									}
								});
							}
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.jreview_new_article), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
									getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
						}
					}
				});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * This method used to create dynamic registration form.
	 */
	private void createForm() {
		try{
			LayoutInflater inflater = LayoutInflater.from(JReviewCreateListingActivity.this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;

			int size = groups.size();
			for (int i = 0; i < size; i++) {
				View groupView = inflater.inflate(R.layout.jreview_dynamic_view_item, null);
				((LinearLayout) groupView.findViewById(R.id.lnrGgroup)).setVisibility(View.VISIBLE);
				((IjoomerTextView) groupView.findViewById(R.id.txtLable)).setText(groups.get(i).get(GROUPNAME));
				lnr_form.addView(groupView, params);

				fields = iCaching.parseData(new JSONArray(groups.get(i).get(FIELDS)));
				LinearLayout layout = null;
				int len = fields.size();
				for (int j = 0; j < len; j++) {
					final HashMap<String, String> field = fields.get(j);
					View fieldView = inflater.inflate(R.layout.jreview_dynamic_view_item, null);

					if (field.get(TYPE).equals(LABEL)) {
						final IjoomerEditText edit;
						layout = ((LinearLayout) fieldView.findViewById(R.id.lnrLabel));
						layout.setVisibility(View.VISIBLE);
						edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
						if(IS_EDIT){
							if (field.get("value").toString().trim().length() > 0) {
								edit.setText(field.get("value"));
							}
						}
					}else if (field.get(TYPE).equals(TEXT)) {
						final IjoomerEditText edit;
						layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
						layout.setVisibility(View.VISIBLE);
						edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
						if(IS_EDIT){
							if (field.get(VALUE).toString().trim().length() > 0) {
								edit.setText(field.get(VALUE));
							} else {
								edit.setText(field.get(VALUE));
							}
						}
						if (field.get(CAPTION).contains(getString(R.string.jreview_phone)) || field.get(CAPTION).contains(getString(R.string.jreview_year)) 
								|| field.get(CAPTION).contains(getString(R.string.jreview_fax)) || field.get(CAPTION).contains(getString(R.string.jreview_price))) {
							edit.setInputType(InputType.TYPE_CLASS_NUMBER);
						} else if (field.get(CAPTION).contains(getString(R.string.jreview_website)) || field.get(CAPTION).contains(getString(R.string.jreview_email))) {
							edit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
						}
					} else if (field.get(TYPE).equals(TEXTAREA)) {
						final IjoomerEditText edit;
						layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
						layout.setVisibility(View.VISIBLE);
						edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
						if(IS_EDIT){
							if (field.get(VALUE).toString().trim().length() > 0) {
								edit.setText(field.get(VALUE));
							}
						}
					} else if (field.get(TYPE).equals(MAP)) {
						final IjoomerEditText edit;
						final ImageView imgMap;
						layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditMap));
						layout.setVisibility(View.VISIBLE);
						edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
						imgMap = ((ImageView) layout.findViewById(R.id.imgMap));
						if(IS_EDIT){
							if (field.get(VALUE).toString().trim().length() > 0) {
								edit.setText(field.get(VALUE));
							} else {
								if (field.get(CAPTION).equalsIgnoreCase(getString(R.string.jreview_state))) {
									try {
										Address address = IjoomerUtilities.getAddressFromLatLong(0, 0);
										edit.setText(address.getAdminArea().replace(address.getCountryName() == null ? "" : address.getCountryName(), "")
												.replace(address.getPostalCode() == null ? "" : address.getPostalCode(), ""));
									} catch (Throwable e) {
										edit.setText("");
									}
								} else if (field.get(CAPTION).equalsIgnoreCase(getString(R.string.jreview_city_town))) {
									try {
										Address address = IjoomerUtilities.getAddressFromLatLong(0, 0);
										edit.setText(address.getSubAdminArea());
									} catch (Throwable e) {
										edit.setText("");
									}
								} else {
									edit.setText(field.get(VALUE));
								}
							}
						}
						imgMap.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								editMap = edit;
								Intent intent = new Intent(JReviewCreateListingActivity.this, IjoomerMapAddress.class);
								startActivityForResult(intent, GET_ADDRESS_FROM_MAP);
							}
						});

					} else if (field.get(TYPE).equals(SELECT)) {
						layout = ((LinearLayout) fieldView.findViewById(R.id.lnrSpin));
						layout.setVisibility(View.VISIBLE);
						final Spinner spn;
						spn = ((Spinner) layout.findViewById(R.id.txtValue));
						spn.setAdapter(IjoomerUtilities.getSpinnerAdapter(field));
						if (field.get(CAPTION).equalsIgnoreCase(getString(R.string.jreview_country))) {
							try {
								Address address = IjoomerUtilities.getAddressFromLatLong(0, 0);
								String country = address.getCountryName();
								int selectedIndex = 0;
								JSONArray jsonArray = null;

								jsonArray = new JSONArray(field.get(OPTIONS));
								int optionSize = jsonArray.length();
								for (int k = 0; k < optionSize; k++) {
									JSONObject options = (JSONObject) jsonArray.get(k);

									if (options.getString(VALUE).equalsIgnoreCase(country)) {
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

					} else if (field.get(TYPE).equals(DATE)) {
						final IjoomerEditText edit;
						layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
						layout.setVisibility(View.VISIBLE);
						edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
						if(IS_EDIT){
							edit.setText(field.get(VALUE));
						}
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

					} else if (field.get(TYPE).equals(TIME)) {
						final IjoomerEditText edit;
						layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
						layout.setVisibility(View.VISIBLE);
						edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
						if(IS_EDIT){
							edit.setText(field.get(VALUE));
						}
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
						if(IS_EDIT){
							edit.setText(field.get(VALUE));
						}
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

					if (field.get(REQUIRED).equalsIgnoreCase("1")) {
						((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION) + " *");
					} else {
						((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION));
					}

					((LinearLayout) fieldView.findViewById(R.id.lnrEdit)).setVisibility(View.GONE);
					((LinearLayout) fieldView.findViewById(R.id.lnrEditArea)).setVisibility(View.GONE);
					((LinearLayout) fieldView.findViewById(R.id.lnrSpin)).setVisibility(View.GONE);
					((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable)).setVisibility(View.GONE);
					((LinearLayout) fieldView.findViewById(R.id.lnrLabel)).setVisibility(View.GONE);

					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrReadOnly));
					layout.setVisibility(View.VISIBLE);

					((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION));
					((IjoomerEditText) layout.findViewById(R.id.txtValue)).setText(field.get(VALUE));
					fieldView.setTag(field);
					lnr_form.addView(fieldView, params);
				}

			}
		}catch(Exception e){

		}
	}

	/**
	 * This method is used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		try {
			IN_CATEGORYID = getIntent().getStringExtra(CATEGORY_ID);
			IN_ARTICLEID = getIntent().getStringExtra(ARTICLEID);
			IS_EDIT = getIntent().getBooleanExtra("IS_EDIT", false);
			groups = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra(GROUPS);
			if(IS_EDIT){
				artcledetails = (ArrayList<HashMap<String,String>>)getIntent().getSerializableExtra("IN_ARTICLE_DETAILS");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
