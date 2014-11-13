package com.ijoomer.components.jomsocial;


import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerSpinnerAdapter;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomAdvancedSearchDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.exception.InvalidKeyFormatException;
import com.smart.exception.NullDataException;
import com.smart.exception.WronNumberOfArgumentsException;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * This Class Contains All Method Related To JomAdvanceSearchActivity.
 *
 * @author tasol
 *
 */
public class JomAdvanceSearchActivity extends JomMasterActivity{

	private ListView lstAdvanceSearch;
	private LinearLayout containsSingleValue,containsToValue;
	private IjoomerEditText txtValue;
	private IjoomerEditText txtValueMultiSelect;
	private IjoomerEditText txtValueTo;
	private IjoomerEditText txtValueFrom;
	private IjoomerButton btnSearch;
	private IjoomerCheckBox chbMemberWithAvatar;
	private IjoomerRadioButton rbMatchAllCriteria,rbMatchAnyCriteria;
	private Spinner spCriteria,spCondition,spOptions;
	private ImageView btnAddCriteria;
	private RadioGroup rgCriteria;
	private View headerView;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private ArrayList<HashMap<String, String>> formData;
	private ArrayList<String> advanceSearchCondition ;
	private ArrayList<String> advanceSearchData ;
	private ArrayList<String> advanceSearchGroup ;
	private ArrayList<String> advanceSearchId ;
	private ArrayList<String> advanceSearchConditionOptions;
	private ArrayList<String> advanceSearchOptionValue ;
	private ArrayList<String> advanceSearchConditionOptionName;
	private ArrayList<String> advanceSearchConditionOptionRange;
	private ArrayList<String> advanceSearchConditionValue;
	private ArrayList<String> advanceSearchConditionType;
	private ArrayList<HashMap<String, String>> selectedArray;
	private HashMap<String, String> selectedHash;

	private SmartListAdapterWithHolder listAdapterWithHolderSearch;
	private JomAdvancedSearchDataProvider jomAdvancedSearch;
	private JSONObject jObject;
	private JSONObject joOptions;

	public static String operator = "and",avatarOnly = "0";
	private static String selectedValue;
	private static String selectedValueTo;
	private static String selectedValueFrom;
	private String NAME = "name";
	private String TMP_CRITERIA_ID = "tmpCriteriaID";
	private String TMP_CONDITION_ID = "tmpConditionID";
	private String FIELD_ID = "fieldid";
	private String FIELD = "field";
	private String CONDITION_NAME = "condition_name";
	private String CONDITION = "condition";
	private String FIELD_TYPE = "fieldType";
	private String VALUE_TYPE = "valuetype";
	private String VALUE = "value";
	private static int fieldPosition =1,conditionPosition=0,selectedListCount=0,selectedListPosition;
	private boolean isFromMultipleValue;
	private boolean isFromSelect;
	private boolean isListSelected;

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_advance_search;
	}

	@Override
	public void initComponents() {
		jomAdvancedSearch = new JomAdvancedSearchDataProvider(this);

		LayoutInflater inflater = (LayoutInflater) this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		lstAdvanceSearch = (ListView) findViewById(R.id.lstAdvanceSearch);
		headerView=inflater.inflate(R.layout.jom_advance_search_header,null);

		txtValue = (IjoomerEditText) headerView.findViewById(R.id.txtValue);
		txtValueFrom = (IjoomerEditText) headerView.findViewById(R.id.txtValueFrom);
		txtValueTo = (IjoomerEditText) headerView.findViewById(R.id.txtValueTo);
		txtValueMultiSelect = (IjoomerEditText) headerView.findViewById(R.id.txtValueMultiSelect);
		btnSearch = (IjoomerButton) findViewById(R.id.btnSearch);
		btnAddCriteria = (ImageView) headerView.findViewById(R.id.btnAddCriteria);
		rgCriteria = (RadioGroup) findViewById(R.id.rgCriteria);
		rbMatchAnyCriteria = (IjoomerRadioButton) findViewById(R.id.rbMatchAnyCriteria);
		rbMatchAllCriteria = (IjoomerRadioButton) findViewById(R.id.rbMatchAllCriteria);
		rbMatchAllCriteria.setButtonDrawable(this.getResources().getDrawable(R.drawable.radio_selector));
		rbMatchAnyCriteria.setButtonDrawable(this.getResources().getDrawable(R.drawable.radio_selector));
		rbMatchAnyCriteria.setPadding(30,3,10,1);
		rbMatchAllCriteria.setPadding(30,3,0,1);
		containsSingleValue = (LinearLayout)headerView.findViewById(R.id.containsSingleValue);
		containsToValue = (LinearLayout)headerView.findViewById(R.id.containsToValue);
		chbMemberWithAvatar = (IjoomerCheckBox) findViewById(R.id.chbMemberWithAvatar);
		chbMemberWithAvatar.setPadding(30,1,0,3);
		chbMemberWithAvatar.setButtonDrawable(this.getResources().getDrawable(R.drawable.checkbox_selector));
		spCriteria = (Spinner)headerView.findViewById(R.id.spCriteria);
		spCondition = (Spinner) headerView.findViewById(R.id.spCondition);
		spOptions = (Spinner) headerView.findViewById(R.id.spOptions);

		isFromMultipleValue = false;
		isFromSelect = false;
		advanceSearchId = new ArrayList<String>();
		advanceSearchGroup = new ArrayList<String>();
		advanceSearchData = new ArrayList<String>();
		advanceSearchCondition = new ArrayList<String>();
		advanceSearchConditionOptions = new ArrayList<String>();
		selectedArray = new ArrayList<HashMap<String,String>>();

		try{
			lstAdvanceSearch.addHeaderView(headerView);
			lstAdvanceSearch.setAdapter(null);
		}catch (Throwable e) {
		}
	}

	@Override
	public void prepareViews() {
		jomAdvancedSearch.getAdvanceSearchData(new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage,
					ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					formData = data1;
					for (HashMap<String, String> hashMap : data1) {
						try {
							advanceSearchData.add(hashMap.get(NAME));
							advanceSearchGroup.add(hashMap.get(TYPE));
							if(hashMap.containsKey(OPTIONS)){
								advanceSearchConditionOptions.add(hashMap.get(OPTIONS));
							}else{
								advanceSearchConditionOptions.add("");
							}
							if(!hashMap.get(TYPE).equals(GROUP)){
								advanceSearchCondition.add(hashMap.get(CONDITION));
							}else{
								advanceSearchCondition.add("");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}


					}
					spCriteria.setAdapter(new IjoomerSpinnerAdapter(JomAdvanceSearchActivity.this, advanceSearchData,advanceSearchGroup,advanceSearchId));
					spCriteria.setSelection(1);
				}else {
					responseErrorMessageHandler(responseCode, true);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(selectedArray!=null && selectedArray.size()>0){
			selectedArray.clear();
			if(listAdapterWithHolderSearch!=null){
				listAdapterWithHolderSearch.clear();
			}
		}
	}

	@Override
	public void setActionListeners() {
		try{
			txtValueTo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), false, new CustomClickListner() {

						@Override
						public void onClick(String value) {
							txtValueTo.setText(value);
							selectedValueTo = value;
						}
					});

				}
			});

			txtValueFrom.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), false, new CustomClickListner() {

						@Override
						public void onClick(String value) {
							txtValueFrom.setText(value);
							selectedValueFrom = value.trim();
						}
					});

				}
			});

			txtValue.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					selectedValue =s.toString();
				}
			});


			spCriteria.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0,
						View arg1, int arg2, long arg3) {
					fieldPosition = arg2;
					JSONArray ja;
					JSONArray jaOptions;
					advanceSearchConditionValue = new ArrayList<String>();
					advanceSearchConditionType = new ArrayList<String>();
					advanceSearchOptionValue = new ArrayList<String>();
					advanceSearchConditionOptionName = new ArrayList<String>();
					advanceSearchConditionOptionRange = new ArrayList<String>();
					try {
						ja = new JSONArray(advanceSearchCondition.get(arg2));
						for(int i = 0;i<ja.length();i++){
							jObject = ja.getJSONObject(i);
							advanceSearchConditionValue.add(jObject.getString(NAME));
							advanceSearchConditionType.add(jObject.getString(CONDITION_VALUE_TYPE));
							advanceSearchConditionOptionRange.add(jObject.getString("range").trim());

						}
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					if(formData.get(arg2).get(TYPE).equals("select") || formData.get(arg2).get(TYPE).equals("radio") 
							|| formData.get(arg2).get(TYPE).equals("checkbox") || formData.get(arg2).get(TYPE).equals("gender")){
						isFromSelect = true;
						txtValue.setVisibility(View.GONE);
						spOptions.setVisibility(View.VISIBLE);
						try {
							jaOptions = new JSONArray(advanceSearchConditionOptions.get(arg2));
							for(int i = 0;i<jaOptions.length();i++){
								joOptions = jaOptions.getJSONObject(i);
								advanceSearchOptionValue.add(joOptions.getString(VALUE));
								advanceSearchConditionOptionName.add(joOptions.getString(NAME));
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						if(advanceSearchOptionValue!=null && advanceSearchOptionValue.get(0)!=null){
							selectedValue = advanceSearchOptionValue.get(0);
						}
						spOptions.setAdapter(new IjoomerSpinnerAdapter(JomAdvanceSearchActivity.this, advanceSearchConditionOptionName,advanceSearchOptionValue,advanceSearchId));
					}else if(formData.get(arg2).get(TYPE).equals("birthdate") || formData.get(arg2).get(TYPE).equals("date")){
						isFromSelect = false;
						spOptions.setVisibility(View.GONE);
						txtValue.setVisibility(View.VISIBLE);
					}else if(formData.get(arg2).get(TYPE).equals("multiselect")){
						isFromSelect = false;
						spOptions.setVisibility(View.GONE);
						txtValue.setVisibility(View.GONE);
						try {
							jaOptions = new JSONArray(advanceSearchConditionOptions.get(arg2));
							for(int i = 0;i<jaOptions.length();i++){
								joOptions = jaOptions.getJSONObject(i);
								advanceSearchOptionValue.add(joOptions.getString(VALUE));
								advanceSearchConditionOptionName.add(joOptions.getString(NAME));
							}
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						containsToValue.setVisibility(View.GONE);
						containsSingleValue.setVisibility(View.VISIBLE);
						txtValueMultiSelect.setVisibility(View.VISIBLE);
					}else{
						isFromSelect = false;
						selectedValue = "";
						containsToValue.setVisibility(View.GONE);
						containsSingleValue.setVisibility(View.VISIBLE);
						txtValue.setVisibility(View.VISIBLE);
						txtValueMultiSelect.setVisibility(View.GONE);
						spOptions.setVisibility(View.GONE);
					}

					spCondition.setAdapter(new IjoomerSpinnerAdapter(JomAdvanceSearchActivity.this, advanceSearchConditionValue,advanceSearchConditionType,advanceSearchId));

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					selectedValue = advanceSearchOptionValue.get(0);

				}
			});
			
			txtValueMultiSelect.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					IjoomerUtilities.getMultiSelectionDialog("", advanceSearchConditionOptions.get(fieldPosition), "", new CustomClickListner() {

						@Override
						public void onClick(String value) {
							((IjoomerEditText) v).setText(value);
							txtValueMultiSelect.setText(value);
						}
					});
				}
			});

			spOptions.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					selectedValue = advanceSearchOptionValue.get(arg2);

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					selectedValue = advanceSearchOptionValue.get(0);
				}
			});

			spCondition.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					conditionPosition = arg2;
					try {

						if(advanceSearchConditionOptionRange.get(conditionPosition)!=null && advanceSearchConditionOptionRange.get(conditionPosition).equalsIgnoreCase("1")){
							containsSingleValue.setVisibility(View.GONE);
							containsToValue.setVisibility(View.VISIBLE);
							isFromMultipleValue = true;
						}else{
							isFromMultipleValue = false;
							containsSingleValue.setVisibility(View.VISIBLE);
							containsToValue.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});

			btnAddCriteria.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					try {
						boolean isError =false;
						if(!isFromSelect){
							if(isFromMultipleValue){
								if(txtValueFrom!=null && txtValueFrom.getText().toString().trim().length()>0){

								}else{
									txtValueFrom.setError(getString(R.string.validation_value_required));
									isError = true;
								}
								if(txtValueTo!=null && txtValueTo.getText().toString().trim().length()>0){

								}else{
									txtValueTo.setError(getString(R.string.validation_value_required));
									isError = true;
								}
							}else{
								if(txtValue!=null && txtValue.getText().toString().trim().length()>0){

								}else{
									txtValue.setError(getString(R.string.validation_value_required));
									isError = true;
								}
							}
						}
						if(!isError){
							selectedArray = makeHashMapFromSelected();
							prepareList(selectedArray);
							spCondition.setSelection(0);
							spCriteria.setSelection(1);
							txtValue.setText("");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}



				}
			});

			btnSearch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					selectedArray = makeHashMapFromSelected();
					try {
						loadNew(JomAdvanceSearchDetailActivity.class, JomAdvanceSearchActivity.this, false,
								"IN_OPERATOR", operator, "IN_AVATARONLY", avatarOnly, "IN_SELECTEDARRAY", selectedArray);
					} catch (WronNumberOfArgumentsException e) {
						e.printStackTrace();
					} catch (InvalidKeyFormatException e) {
						e.printStackTrace();
					} catch (NullDataException e) {
						e.printStackTrace();
					}
				}
			});

		}catch (Throwable e) {
		}


		rgCriteria.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int selectedId = rgCriteria.getCheckedRadioButtonId();
				RadioButton checkedRadioButton = (RadioButton)rgCriteria.findViewById(selectedId);
				if(checkedRadioButton.getText().equals(getString(R.string.matchanycriteria))){
					operator = "and";
				}else{
					operator = "or";
				}

			}
		});
		chbMemberWithAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (((CheckBox) v).isChecked()) {
					avatarOnly = "1";
				}else{
					avatarOnly = "0";
				}


			}
		});
	}


	/**
	 * Class methods
	 * @return
	 */

	/**
	 * This method used to hash map data from selected.
	 * @return represented {@link HashMap} list
	 */
	public ArrayList<HashMap<String, String>> makeHashMapFromSelected(){
		ArrayList<HashMap<String, String>> hashArray = new ArrayList<HashMap<String,String>>();


		try {
			selectedHash = new HashMap<String, String>();
			if(selectedArray!=null){
				selectedHash.put(NAME,formData.get(fieldPosition).get(NAME));
				selectedHash.put(TMP_CRITERIA_ID,String.valueOf(fieldPosition));
				selectedHash.put(TMP_CONDITION_ID,String.valueOf(conditionPosition));
				selectedHash.put(FIELD_ID,formData.get(fieldPosition).get(ID));
				selectedHash.put(FIELD,formData.get(fieldPosition).get(CONDITION_FIELD_CODE));
				JSONArray ja = new JSONArray(advanceSearchCondition.get(fieldPosition));
				JSONObject jo = null;
				jo = ja.getJSONObject(conditionPosition);
				selectedHash.put(CONDITION_NAME,jo.getString(NAME));
				selectedHash.put(CONDITION,jo.getString(VALUE));
				selectedHash.put(FIELD_TYPE,jo.getString(VALUE_TYPE));
				if(isFromMultipleValue){
					selectedValue  = selectedValueFrom +","+selectedValueTo;
					selectedHash.put(VALUE,selectedValue);
				}else{
					if(isFromSelect){
						if(selectedValue!=null && selectedValue.length()>0){
						}else{
							selectedValue = advanceSearchOptionValue.get(0);
						}
					}
					selectedHash.put(VALUE,selectedValue);
				}
				selectedArray.add(selectedHash);
				selectedArray.size();
			}
			try{
				if(isListSelected){
					selectedArray.remove(selectedListPosition);
				}
			}catch (Throwable e) {
			}
			if(selectedArray!=null)
				for(int i=selectedArray.size()-1;i>=0;i--){
					HashMap<String, String> hashMap = selectedArray.get(i);
					hashArray.add(hashMap);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}


		return hashArray;
	}

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.search), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
	}

	/**
	 * This method used to prepare advance search data.
	 * @param data represented data list
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data){


		if(listAdapterWithHolderSearch!=null){
			listAdapterWithHolderSearch.clear();
		}else{
			listAdapterWithHolderSearch = getSearchListAdapter();
			lstAdvanceSearch.setAdapter(listAdapterWithHolderSearch);
		}
		for (HashMap<String, String> hashMap : data) {

			SmartListItem item = new SmartListItem();
			item.setItemLayout(R.layout.jom_advance_search_list_adapter);
			ArrayList<Object> obj = new ArrayList<Object>();
			obj.add(hashMap);
			item.setValues(obj);
			listAdapterWithHolderSearch.add(item);
		}

		isListSelected = false;

	}


	/**
	 * List adapter for search.
	 * @return
	 */
	private SmartListAdapterWithHolder getSearchListAdapter() {
		SmartListAdapterWithHolder adapter = listAdapterWithHolderSearch = new SmartListAdapterWithHolder(JomAdvanceSearchActivity.this, R.layout.jom_advance_search_list_adapter,
				listData, new ItemView() {

			@SuppressWarnings({ "unchecked", "deprecation" })
			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
				holder.txtCriteriaName = (IjoomerTextView) v.findViewById(R.id.tvCriteriaName);
				holder.tvCondition = (IjoomerTextView) v.findViewById(R.id.tvCondition);
				holder.tvValue = (IjoomerTextView) v.findViewById(R.id.tvValue);
				holder.btnAddCriteria = (ImageView) v.findViewById(R.id.btnAddCriteria);
				holder.rvTitleRow = (RelativeLayout)v.findViewById(R.id.rvTitleRow);
				
				final HashMap<String, String> search = (HashMap<String, String>) item.getValues().get(0);

				holder.txtCriteriaName.setText(search.get(NAME));
				holder.tvCondition.setText(search.get(CONDITION_NAME));
				holder.tvValue.setText(search.get(VALUE));
				holder.rvTitleRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.jom_advance_search_list_bg));
			
				holder.btnAddCriteria.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						selectedArray.remove(position);
						prepareList(selectedArray);
					}
				});
			
				holder.rvTitleRow.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						selectedListPosition = position;
						isListSelected = true;
						selectedListCount++;
						if(selectedListCount>1){
							prepareList(selectedArray);

							spCriteria.setSelection(Integer.valueOf(search.get(TMP_CRITERIA_ID)));
							spCondition.setSelection(Integer.valueOf(search.get(TMP_CONDITION_ID)));
							txtValue.setText(search.get(VALUE));
							selectedListCount = 0;
							isListSelected = true;
						}else{
							holder.rvTitleRow.setBackgroundColor(Color.WHITE);
							spCriteria.setSelection(Integer.valueOf(search.get(TMP_CRITERIA_ID)));
							spCondition.setSelection(Integer.valueOf(search.get(TMP_CONDITION_ID)));
							txtValue.setText(search.get(VALUE));
						}
					}
				});
				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}

		});
		return adapter;
	}

}
