package com.ijoomer.components.jReview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.MyCustomAdapter;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.custom.interfaces.IjoomerClickListner;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jReview.JReviewDataProvider;
import com.ijoomer.src.R;

public class JReviewSearchActivity extends JReviewMasterActivity{

	private ArrayList<String> search_by;
	private HashMap<String, String> searchfields;
	private ArrayList<HashMap<String, String>> search_criterias;
	private ArrayList<HashMap<String, String>> search_categories;
	private String optionPhrase;
	private String ALL = "all";
	private String ANY = "any";
	private String EXACT = "exact";
	private IjoomerEditText edtKeyword, edtSearchby, edtAuthor, edtCategory;
	private IjoomerRadioButton btnAllword, btnAnyword, btnExactword;
	private Spinner spnCriteria;
	private Button btnApply, btnCancel;
	private LinearLayout author_Layout,lnr_form,serachCategoryLayout;

	final private int GET_ADDRESS_FROM_MAP = 1;

	private ArrayList<HashMap<String, String>> fields;
	private ArrayList<HashMap<String, String>> groups;

	private IjoomerCaching iCaching;
	private JReviewDataProvider dataProvider;

	@Override
	public int setLayoutId() {
		return R.layout.jreview_search;
	}

	@Override
	public void initComponents() {
		iCaching = new IjoomerCaching(this);
		dataProvider = new JReviewDataProvider(this);

		getSearchCriteria();

		edtSearchby = (IjoomerEditText) findViewById(R.id.edtsearchby);
		edtKeyword = (IjoomerEditText) findViewById(R.id.edtkeyword);
		edtAuthor = (IjoomerEditText) findViewById(R.id.edtauthor);

		btnAllword = (IjoomerRadioButton) findViewById(R.id.btnAllword);
		btnAnyword = (IjoomerRadioButton) findViewById(R.id.btnAnyword);
		btnExactword = (IjoomerRadioButton) findViewById(R.id.btnExactword);
		btnApply = (Button) findViewById(R.id.btnApply);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		spnCriteria = (Spinner) findViewById(R.id.spncriteria);
		edtCategory = (IjoomerEditText) findViewById(R.id.edtCategory);
		edtCategory.setTag("");
		optionPhrase = ALL;
		lnr_form = (LinearLayout) findViewById(R.id.lnr_form);
		author_Layout = (LinearLayout) findViewById(R.id.authorview);
		serachCategoryLayout = (LinearLayout) findViewById(R.id.searchcategoryView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		applySideMenu("JReviewAllDirectoriesActivity");
	}

	@Override
	public void prepareViews() {
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
		if(IjoomerGlobalConfiguration.isAllowSearchByAuthor()){
			author_Layout.setVisibility(View.VISIBLE);
		}		
		spnCriteria.setAdapter(getSpinnerAdapter(search_criterias));
	}

	@Override
	public void setActionListeners() {
		edtSearchby.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				getSearchByDialog(getString(R.string.jreview_search_by), search_by, ((IjoomerEditText) v).getText().toString(), new CustomClickListner() {

					@Override
					public void onClick(String value) {
						((IjoomerEditText) v).setText(value);
					}
				});

			}
		});

		edtCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				getCategoriesDialog(getString(R.string.jreview_search_category), search_categories, ((IjoomerEditText) v).getText().toString(), ((IjoomerEditText) v).getTag().toString(), new IjoomerClickListner() {

					@Override
					public void onClick(String value, String id) {
						((IjoomerEditText) v).setText(value);
						((IjoomerEditText) v).setTag(id);
					}
				});

			}
		});

		spnCriteria.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if(position > 0){
					search_categories = dataProvider.getCategoriesbyCriteria(search_criterias.get(position).get("id"));
					if(search_categories != null && search_categories.size() > 0){
						edtCategory.setText("");
						edtCategory.setTag("");
						serachCategoryLayout.setVisibility(View.VISIBLE);
						searchfields = dataProvider.getArticlesbyCriteria(search_criterias.get(position).get("id"));
						lnr_form.removeAllViews();
						if(searchfields!=null && searchfields.size()>0){
							lnr_form.setVisibility(View.VISIBLE);
							try{
								groups = iCaching.parseData(new JSONArray(searchfields.get("jreview_group")));
								createForm();
								setEditable(true);
							}catch(Exception e){
								e.printStackTrace();
							}
						}else{
							lnr_form.setVisibility(View.GONE);
						}
					}else{
						serachCategoryLayout.setVisibility(View.GONE);
						lnr_form.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		btnAllword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionPhrase = ALL;
			}
		});
		btnAnyword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionPhrase = ANY;
			}
		});
		btnExactword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionPhrase = EXACT;
			}
		});


		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				applySearch();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void getSearchCriteria(){
		HashMap<String, String> iCriteria = new HashMap<String, String>();
		iCriteria.put("name", "-Select-");
		iCriteria.put("value", "-1");
		search_criterias = dataProvider.getSearchFields();
		search_criterias.add(0, iCriteria);

		search_by = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.jreview_searchby)));
	}

	/**
	 * This method used to create dynamic registration form.
	 */
	private void createForm() {
		try{
			LayoutInflater inflater = LayoutInflater.from(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			params.topMargin = 10;

			int size = groups.size();
			for (int i = 0; i < size; i++) {
				View groupView = inflater.inflate(R.layout.jreview_dynamic_view_item, null);
				((LinearLayout) groupView.findViewById(R.id.lnrGgroup)).setVisibility(View.VISIBLE);
				((IjoomerTextView) groupView.findViewById(R.id.txtLable)).setText(groups.get(i).get("group_name"));
				lnr_form.addView(groupView, params);

				fields = iCaching.parseData(new JSONArray(groups.get(i).get("fields")));
				LinearLayout layout = null;
				int len = fields.size();
				for (int j = 0; j < len; j++) {
					final HashMap<String, String> field = fields.get(j);
					if(field.get(SEARCHABLE).equalsIgnoreCase("1")){
						View fieldView = inflater.inflate(R.layout.jreview_dynamic_view_item, null);

						if (field.get(TYPE).equals(LABEL)) {
							layout = ((LinearLayout) fieldView.findViewById(R.id.lnrLabel));
							layout.setVisibility(View.VISIBLE);
						}else if (field.get(TYPE).equals(TEXT)) {
							final IjoomerEditText edit;
							layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
							layout.setVisibility(View.VISIBLE);
							edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
							if (field.get(CAPTION).contains(getString(R.string.jreview_phone)) || field.get(CAPTION).contains(getString(R.string.jreview_year)) || field.get(CAPTION).contains(getString(R.string.jreview_fax))) {
								edit.setInputType(InputType.TYPE_CLASS_NUMBER);
							} else if (field.get(CAPTION).contains(getString(R.string.jreview_website)) || field.get(CAPTION).contains(getString(R.string.jreview_email))) {
								edit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
							}
						} else if (field.get(TYPE).equals(TEXTAREA)) {
							layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
							layout.setVisibility(View.VISIBLE);
						} else if (field.get(TYPE).equals(MAP)) {
							final IjoomerEditText edit;
							final ImageView imgMap;
							layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditMap));
							layout.setVisibility(View.VISIBLE);
							edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
							imgMap = ((ImageView) layout.findViewById(R.id.imgMap));
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
							} 
							imgMap.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(JReviewSearchActivity.this, IjoomerMapAddress.class);
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
			}
		}catch(Exception e){
		}
	}

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

	private void applySearch(){
		try{
			JSONArray searchFields = new JSONArray();
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
					} else if(edtValue.getText().toString().trim().length() > 0 ) {
						field.put(VALUE, edtValue.getText().toString().trim());
						fields.put(field.get(FIELDNAME),field.get(VALUE));
					}
				}
			}
			searchFields.put(fields);

			try {
				loadNew(JReviewSearchResultActivity.class, JReviewSearchActivity.this, false, "IN_CAPTION", getString(R.string.jreview_search_results), "IN_OPTIONCRITERIA", edtSearchby.getText().toString(), "IN_SEARCH_KEY", edtKeyword
						.getText().toString().trim(), "IN_SEARCH_PHRASE", optionPhrase, "IN_CATEGORY_IDS", edtCategory.getTag().toString(), "IN_AUTHOR", edtAuthor.getText().toString(), "IN_SEARCHFIELD", searchFields.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e){

		}
	}


	private MyCustomAdapter getSpinnerAdapter(ArrayList<HashMap<String, String>> fields) {
		final ArrayList<String> values = new ArrayList<String>();

		int size = fields.size();
		for (int i = 0; i < size; i++) {
			HashMap<String, String> field = fields.get(i);
			if(field.containsKey(NAME)){
				values.add(field.get(NAME));
			}else if(field.containsKey(CATEGORY_NAME)){
				values.add(field.get(CATEGORY_NAME));
			}
		}

		final MyCustomAdapter adpater = new MyCustomAdapter(this, values);
		return adpater;
	}

	private void getSearchByDialog(final String name, ArrayList<String> searchby, final String value, final CustomClickListner target) {
		final ArrayList<String> values = new ArrayList<String>();

		for (int i = 0; i < searchby.size(); i++) {
			values.add(searchby.get(i));
		}

		final boolean[] selections = new boolean[values.size()];
		final StringBuilder newValue = new StringBuilder();

		AlertDialog alert = null;

		if (value.length() > 0) {
			String[] oldValue = value.split(",");
			int size = values.size();
			for (int i = 0; i < size; i++) {
				int len = oldValue.length;
				for (int j = 0; j < len; j++) {
					if (values.get(i).toString().trim().equalsIgnoreCase(oldValue[j].trim())) {
						selections[i] = true;
						break;
					}
				}
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(name);
		builder.setMultiChoiceItems(values.toArray(new CharSequence[values.size()]), selections,
				new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				selections[which] = isChecked;
			}
		});

		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int size = selections.length;
				for (int i = 0; i < size; i++) {
					if (selections[i]) {
						newValue.append(newValue.length() > 0 ? "," + values.get(i) : values.get(i));
					}
				}
				target.onClick(newValue.toString());

			}
		});
		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				target.onClick(value);
			}
		});
		alert = builder.create();
		alert.show();
	}

	private void getCategoriesDialog(final String name, ArrayList<HashMap<String, String>> categories, final String value, final String id, final IjoomerClickListner target) {
		final ArrayList<String> values = new ArrayList<String>();
		final ArrayList<String> ids = new ArrayList<String>();

		for (int i = 0; i < categories.size(); i++) {
			values.add(categories.get(i).get(CATEGORY_NAME));
			ids.add(categories.get(i).get(CATEGORY_ID));
		}

		final boolean[] selections = new boolean[values.size()];
		final StringBuilder newValue = new StringBuilder();
		final StringBuilder newIds = new StringBuilder();

		AlertDialog alert = null;

		if (value.length() > 0) {
			String[] oldValue = value.split(",");
			int size = values.size();
			for (int i = 0; i < size; i++) {
				int len = oldValue.length;
				for (int j = 0; j < len; j++) {
					if (values.get(i).toString().trim().equalsIgnoreCase(oldValue[j].trim())) {
						selections[i] = true;
						break;
					}
				}
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(name);
		builder.setMultiChoiceItems(values.toArray(new CharSequence[values.size()]), selections,
				new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				selections[which] = isChecked;
			}
		});

		builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int size = selections.length;
				for (int i = 0; i < size; i++) {
					if (selections[i]) {
						newValue.append(newValue.length() > 0 ? "," + values.get(i) : values.get(i));
						newIds.append(newIds.length() > 0 ? "," + ids.get(i) : ids.get(i));
					}
				}
				target.onClick(newValue.toString(),newIds.toString());
			}
		});

		builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				target.onClick(value,id);
			}
		});
		alert = builder.create();
		alert.show();
	}
}
