package com.ijoomer.components.sobipro;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.MyCustomAdapter;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.RangeSeekBar;
import com.ijoomer.customviews.RangeSeekBar.RangeSeekBarListener;
import com.ijoomer.library.sobipro.SobiproAdvanceSearchFieldsDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Activity class for SobiproSearchActivity view
 * 
 * @author tasol
 * 
 */

public class SobiproSearchActivity extends SobiproMasterActivity implements com.ijoomer.customviews.SeekBarWithTwoThumb.SeekBarChangeListener {

	private IjoomerEditText edtKeyword;
	private IjoomerRadioButton btnAllword, btnAnyword, btnExactword;
	private IjoomerEditText edtDistance;
	private IjoomerEditText edtCurrentLocation;
	private IjoomerRadioButton price1, price2, price3, price4;
	private SobiproAdvanceSearchFieldsDataProvider searchAdvanceFieldsDataProvider;
	private LinearLayout lnr_form;
	private Button btnApply, btnCancel;
	final private int GET_ADDRESS_FROM_MAP = 1;
	private String IN_FEATUREDFIRST = "No";
	private String section_id;
	private String optionCriteria;
	private String optionPrice = "";
	private SeekBar seekRadiusSearch;
	private String seekProgress;
	private int IN_POS;
	private String ALL = "all";
	private String ANY = "any";
	private String EXACT = "exact";
	private ImageView imgPlace;
	private String latitude, longitude;
	private String IN_PAGELAYOUT;
	private ArrayList<String> pageLayouts;
	private LinearLayout lnrSearchWithin, lnrWithinText, lnrLocation, lnrLocationLable, lnrSep1, lnrSep2;

	/**
	 * Overrides methods.
	 */
	@Override
	public int setLayoutId() {
		return R.layout.sobipro_search;
	}

	@Override
	public void initComponents() {
		edtKeyword = (IjoomerEditText) findViewById(R.id.edtkeyword);
		edtDistance = (IjoomerEditText) findViewById(R.id.edtDistance);
		edtCurrentLocation = (IjoomerEditText) findViewById(R.id.edtCurrentLocation);
		btnAllword = (IjoomerRadioButton) findViewById(R.id.btnAllword);
		btnAnyword = (IjoomerRadioButton) findViewById(R.id.btnAnyword);
		btnExactword = (IjoomerRadioButton) findViewById(R.id.btnExactword);
		lnr_form = (LinearLayout) findViewById(R.id.lnr_form);
		btnApply = (Button) findViewById(R.id.btnApply);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		seekRadiusSearch = (SeekBar) findViewById(R.id.seekRadiusSearch);
		imgPlace = (ImageView) findViewById(R.id.imgPlace);
		lnrSearchWithin = (LinearLayout) findViewById(R.id.lnrSearchWithin);
		lnrWithinText = (LinearLayout) findViewById(R.id.lnrWithinTxt);
		lnrLocation = (LinearLayout) findViewById(R.id.lnrLocation);
		lnrLocationLable = (LinearLayout) findViewById(R.id.lnrLocationLable);
		lnrSep1 = (LinearLayout) findViewById(R.id.lnrSep1);
		lnrSep2 = (LinearLayout) findViewById(R.id.lnrSep2);
		optionCriteria = ALL;
		searchAdvanceFieldsDataProvider = new SobiproAdvanceSearchFieldsDataProvider(this);
		pageLayouts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sobipro_pageLayout)));
	}

	@Override
	public void prepareViews() {
		latitude = getLatitude();
		longitude = getLongitude();
		getIntentData();
		try {
			edtCurrentLocation.setText(IjoomerUtilities.getAddressFromLatLong(0, 0).getSubAdminArea().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
		case 1:

			seekRadiusSearch.setVisibility(View.GONE);
			lnrSearchWithin.setVisibility(View.GONE);
			lnrWithinText.setVisibility(View.GONE);
			lnrLocation.setVisibility(View.GONE);
			lnrLocationLable.setVisibility(View.GONE);
			lnrSep1.setVisibility(View.GONE);
			lnrSep2.setVisibility(View.GONE);
			break;

		case 2:
			seekRadiusSearch.setVisibility(View.VISIBLE);
			lnrSearchWithin.setVisibility(View.VISIBLE);
			lnrWithinText.setVisibility(View.VISIBLE);
			lnrLocation.setVisibility(View.VISIBLE);
			lnrLocationLable.setVisibility(View.VISIBLE);
			lnrSep1.setVisibility(View.VISIBLE);
			lnrSep2.setVisibility(View.VISIBLE);
			break;

		default:
			seekRadiusSearch.setVisibility(View.VISIBLE);
			lnrSearchWithin.setVisibility(View.VISIBLE);
			lnrWithinText.setVisibility(View.VISIBLE);
			lnrLocation.setVisibility(View.VISIBLE);
			lnrLocationLable.setVisibility(View.VISIBLE);
			lnrSep1.setVisibility(View.VISIBLE);
			lnrSep2.setVisibility(View.VISIBLE);
			break;
		}

		getAdvanceFields();
	}

	@Override
	public void setActionListeners() {
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelectedData();
			}
		});
		imgPlace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SobiproSearchActivity.this, IjoomerMapAddress.class);
				startActivityForResult(intent, GET_ADDRESS_FROM_MAP);

			}
		});
		btnAllword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionCriteria = ALL;
			}
		});
		btnAnyword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionCriteria = ANY;
			}
		});
		btnExactword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				optionCriteria = EXACT;
			}
		});
		seekRadiusSearch.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (seekProgress.length() > 0) {
					edtDistance.setText(seekProgress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekProgress = String.valueOf(progress);
				if (seekProgress.length() > 0) {
					edtDistance.setText(seekProgress);
				}

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void SeekBarValueChanged(int Thumb1Value, int Thumb2Value) {
		Toast.makeText(SobiproSearchActivity.this, Thumb1Value + "" + " " + Thumb2Value + "", Toast.LENGTH_LONG).show();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GET_ADDRESS_FROM_MAP) {

				edtCurrentLocation.setText(((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("address"));
				latitude = ((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("latitude");
				longitude = ((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("longitude");

			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	/**
	 * Class methods.
	 */

	/**
	 * This method used to get intent data.
	 */

	private void getIntentData() {
		try {
			section_id = this.getIntent().getStringExtra("IN_SECTION_ID");
			IN_POS = this.getIntent().getIntExtra("IN_POS", 0);
			IN_PAGELAYOUT = this.getIntent().getStringExtra("IN_PAGELAYOUT");
			IN_FEATUREDFIRST = getIntent().getStringExtra("IN_FEATUREDFIRST");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to get search dynamic fields.
	 */

	private void getAdvanceFields() {
		searchAdvanceFieldsDataProvider.getAdvanceSearchFields(section_id, new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						if (data1 != null && data1.size() > 0) {
							createForm(data1);
						} else {
							IjoomerUtilities.getCustomOkDialog(SobiproSearchActivity.this.getScreenCaption(),
									getString(getResources().getIdentifier("code204", "string", SobiproSearchActivity.this.getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
										@Override
										public void NeutralMethod() {

										}
									});
						}
					} else {
						IjoomerUtilities.getCustomOkDialog(SobiproSearchActivity.this.getScreenCaption(),
								getString(getResources().getIdentifier("code" + responseCode, "string", SobiproSearchActivity.this.getPackageName())), getString(R.string.ok),
								R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
									@Override
									public void NeutralMethod() {

									}
								});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * This method is used to create dynamic form for search.
	 * 
	 * @param FIELD_LIST
	 *            represented dynamic field list.
	 */

	private void createForm(ArrayList<HashMap<String, String>> FIELD_LIST) {
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = 7;
		LinearLayout layout = null;
		int size = FIELD_LIST.size();
		for (int j = 0; j < size; j++) {
			final HashMap<String, String> field = FIELD_LIST.get(j);
			View fieldView = inflater.inflate(R.layout.sobipro_dynamic_view_item, null);

			price1 = (IjoomerRadioButton) fieldView.findViewById(R.id.price1);
			price2 = (IjoomerRadioButton) fieldView.findViewById(R.id.price2);
			price3 = (IjoomerRadioButton) fieldView.findViewById(R.id.price3);
			price4 = (IjoomerRadioButton) fieldView.findViewById(R.id.price4);

			price1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					optionPrice = "1";
				}
			});
			price2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					optionPrice = "2";
				}
			});
			price3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					optionPrice = "3";
				}
			});
			price4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					optionPrice = "4";
				}
			});

			if (field.get(TYPE).equals(TEXT)) {
				final IjoomerEditText edit;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				edit.setText(Html.fromHtml(field.get(VALUE)));
				edit.setHint(field.get(CAPTION));
			} else if (field.get(TYPE).equals(TEXTAREA)) {
				final IjoomerEditText edit;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				edit.setText(Html.fromHtml(field.get(VALUE)));
				edit.setHint(field.get(CAPTION));

			} else if (field.get(TYPE).equals(SELECT)) {
				if (field.get(NAME).equalsIgnoreCase(FIELDPRICE)) {
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrPrice));
					layout.setVisibility(View.VISIBLE);

				} else {
					layout = ((LinearLayout) fieldView.findViewById(R.id.lnrSpin));
					layout.setVisibility(View.VISIBLE);
					MyCustomAdapter adapter = IjoomerUtilities.getSpinnerAdapter(field);
					((Spinner) layout.findViewById(R.id.txtValue)).setAdapter(adapter);
					((Spinner) layout.findViewById(R.id.txtValue)).setSelection(adapter.getDefaultPosition());
				}

			} else if (field.get(TYPE).equals(DATETIME)) {
				final IjoomerEditText edit;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				edit.setText(field.get(VALUE));
				edit.setHint(field.get(CAPTION));
				edit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						IjoomerUtilities.getDateTimeDialog(((IjoomerEditText) v).getText().toString(), new CustomClickListner() {

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
				edit.setHint(field.get(CAPTION));
				edit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						IjoomerUtilities.getMultiSelectionDialog(field.get(CAPTION), field.get(OPTIONS), "", new CustomClickListner() {

							@Override
							public void onClick(String value) {
								((IjoomerEditText) v).setText(value.trim());
							}
						});

					}
				});
			} else if (field.get(TYPE).equals(MAP)) {
				final IjoomerEditText edit;
				final ImageView imgMap;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditMap));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				imgMap = ((ImageView) layout.findViewById(R.id.imgMap));
				edit.setText(field.get(VALUE));
				edit.setHint(field.get(CAPTION));
				imgMap.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
					}
				});

			} else if (field.get(TYPE).equals(RANGE)) {
				final RangeSeekBar swtt;
				final IjoomerTextView txtLable, txtValue1, txtValue2;
				String from = "", to = "";
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrRangeSeekbar));
				txtLable = (IjoomerTextView) layout.findViewById(R.id.txtLable);
				txtValue1 = (IjoomerTextView) layout.findViewById(R.id.txtValue1);
				txtValue2 = (IjoomerTextView) layout.findViewById(R.id.txtValue2);

				layout.setVisibility(View.VISIBLE);
				swtt = (RangeSeekBar) layout.findViewById(R.id.rangeSeekBarView1);
				swtt.setVisibility(View.VISIBLE);
				try {
					JSONObject jsonObjectFrom = new JSONObject(field.get("from").toString());
					from = jsonObjectFrom.get(VALUE).toString();
					JSONObject jsonObjectTo = new JSONObject(field.get("to").toString());
					to = jsonObjectTo.get(VALUE).toString();

					swtt.setScaleRangeMin(Float.parseFloat(from));
					swtt.setScaleRangeMax(Float.parseFloat(to));

				} catch (JSONException e) {
					e.printStackTrace();
				}
				swtt.setListener(new RangeSeekBarListener() {

					@Override
					public void onCreate(int index, float value) {
					}

					@SuppressWarnings("unchecked")
					@Override
					public void onSeek(int index, float value) {
						float f = 0.5f;
						float rounded = f * Math.round(value / f);
						HashMap<String, String> hashMap;
						if (swtt.getTag() != null) {
							hashMap = (HashMap<String, String>) swtt.getTag();
						} else {
							hashMap = new HashMap<String, String>();
						}
						if (index == 0) {
							txtValue1.setText(getString(R.string.sobipro_range_from) + rounded + "");
							hashMap.put("FROM", rounded + "");
						} else if (index == 1) {
							txtValue2.setText(getString(R.string.sobipro_range_to) + rounded + "");
							hashMap.put("TO", rounded + "");
						}
						swtt.setTag(hashMap);
					}
				});
				txtLable.setText(field.get(CAPTION));
			} else if (field.get(TYPE).equals("date")) {
				final IjoomerEditText editFrom;
				final IjoomerEditText editTo;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditSearchClickable));
				layout.setVisibility(View.VISIBLE);
				editFrom = ((IjoomerEditText) layout.findViewById(R.id.txtValueFrom));
				editTo = ((IjoomerEditText) layout.findViewById(R.id.txtValueTo));

				JSONObject jsonObjectFrom;
				JSONObject jsonObjectTo;
				try {
					jsonObjectFrom = new JSONObject(field.get("from").toString());

					editFrom.setHint(field.get(CAPTION) + " " + jsonObjectFrom.get(NAME).toString());

					jsonObjectTo = new JSONObject(field.get("to").toString());

					editTo.setHint(field.get(CAPTION) + " " + jsonObjectTo.get(NAME).toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				editFrom.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), false, new CustomClickListner() {

							@Override
							public void onClick(String value) {
								((IjoomerEditText) v).setText(value);
								((IjoomerEditText) v).setError(null);
							}
						});

					}
				});

				editTo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {

						IjoomerUtilities.getDateDialog(((IjoomerEditText) v).getText().toString(), false, new CustomClickListner() {

							@Override
							public void onClick(String value) {
								((IjoomerEditText) v).setText(value);
								((IjoomerEditText) v).setError(null);
							}
						});

					}
				});

			}

			fieldView.setTag(field);
			lnr_form.addView(fieldView, params);
		}
	}

	/**
	 * This method is used to get selected data by user in search form.
	 */
	@SuppressWarnings("unchecked")
	private void getSelectedData() {
		ArrayList<HashMap<String, String>> searchField = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> field;
		int size = lnr_form.getChildCount();

		for (int i = 0; i < size; i++) {
			View v = (LinearLayout) lnr_form.getChildAt(i);
			field = new HashMap<String, String>();
			field.putAll((HashMap<String, String>) v.getTag());
			IjoomerEditText edtValue = null;
			IjoomerEditText edtToValue = null;
			IjoomerEditText edtFromValue = null;
			Spinner spnrValue = null;
			IjoomerCheckBox chbValue = null;

			if (field != null) {
				if (field.get(TYPE).equals(TEXT)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEdit)).findViewById(R.id.txtValue);
				} else if (field.get(TYPE).equals(TEXTAREA)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditArea)).findViewById(R.id.txtValue);
				} else if (field.get(TYPE).equals(DATETIME)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
				} else if (field.get(TYPE).equals(MAP)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditMap)).findViewById(R.id.txtValue);
				}
				if (field.get(TYPE).equals(CHECKBOX)) {
					chbValue = (IjoomerCheckBox) ((LinearLayout) v.findViewById(R.id.lnrCheckbox)).findViewById(R.id.txtValue);
					field.put(VALUE, chbValue.isChecked() ? "1" : "0");
					searchField.add(field);
				} else if (field.get(TYPE).equals(SELECT)) {

					spnrValue = (Spinner) ((LinearLayout) v.findViewById(R.id.lnrSpin)).findViewById(R.id.txtValue);
					try {
						JSONArray options = new JSONArray(field.get(OPTIONS));
						field.put(VALUE, ((JSONObject) options.get(spnrValue.getSelectedItemPosition())).getString(VALUE));
					} catch (Throwable e) {
						e.printStackTrace();
					}

					searchField.add(field);
				} else if (field.get(TYPE).equals(MULTIPLESELECT)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
				} else if (field.get(TYPE).equals(RANGE)) {
					try {

						RangeSeekBar rangeSeekBar = (RangeSeekBar) ((LinearLayout) v.findViewById(R.id.lnrRangeSeekbar)).findViewById(R.id.rangeSeekBarView1);
						JSONObject jsonObjectFrom = new JSONObject(field.get("from").toString());

						JSONObject jsonObjectTo = new JSONObject(field.get("to").toString());

						if ((HashMap<String, String>) rangeSeekBar.getTag() != null) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put(jsonObjectFrom.get(NAME).toString(), ((HashMap<String, String>) rangeSeekBar.getTag()).get("FROM"));
							jsonObject.put(jsonObjectTo.get(NAME).toString(), ((HashMap<String, String>) rangeSeekBar.getTag()).get("TO"));
							field.put(VALUE, jsonObject.toString());
						}
						searchField.add(field);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (field.get(TYPE).equalsIgnoreCase("date")) {
					try {

						edtToValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditSearchClickable)).findViewById(R.id.txtValueTo);

						edtFromValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditSearchClickable)).findViewById(R.id.txtValueFrom);

						JSONObject jsonObjectFrom = new JSONObject(field.get("from").toString());

						JSONObject jsonObjectTo = new JSONObject(field.get("to").toString());

						JSONObject jsonObject = new JSONObject();
						jsonObject.put(jsonObjectFrom.get(NAME).toString(), edtFromValue.getText().toString().trim());

						jsonObject.put(jsonObjectTo.get(NAME).toString(), edtToValue.getText().toString().trim());
						field.put(VALUE, jsonObject.toString());
						searchField.add(field);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				if (edtValue != null) {
					field.put(VALUE, edtValue.getText().toString().trim());
					searchField.add(field);
				}
			}
		}

		switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
		case 1:
			break;
		case 2:
			if (edtCurrentLocation.getText().toString().trim().length() > 0) {
				if (getLatitude() != null && getLatitude().length() > 0) {
					field = new HashMap<String, String>();
					field.put(NAME, "field_latitude");
					field.put(VALUE, latitude);
					searchField.add(field);
					field = new HashMap<String, String>();
					field.put(NAME, "field_longitude");
					field.put(VALUE, longitude);
					searchField.add(field);
					if (edtDistance.getText().toString().trim().length() > 0) {
						field = new HashMap<String, String>();
						field.put(NAME, "field_distance");
						field.put(VALUE, edtDistance.getText().toString());
						searchField.add(field);
					} else {
						edtDistance.setText("");
					}
				} else {
					edtCurrentLocation.setText("");
				}

			}

			field = new HashMap<String, String>();
			field.put(NAME, "field_price");
			field.put(VALUE, optionPrice);
			searchField.add(field);

			break;
		default:
			if (edtCurrentLocation.getText().toString().trim().length() > 0) {
				if (getLatitude() != null && getLatitude().length() > 0) {
					field = new HashMap<String, String>();
					field.put(NAME, "field_latitude");
					field.put(VALUE, latitude);
					searchField.add(field);
					field = new HashMap<String, String>();
					field.put(NAME, "field_longitude");
					field.put(VALUE, longitude);
					searchField.add(field);
					if (edtDistance.getText().toString().trim().length() > 0) {
						field = new HashMap<String, String>();
						field.put(NAME, "field_distance");
						field.put(VALUE, edtDistance.getText().toString());
						searchField.add(field);
					} else {
						edtDistance.setText("");
					}
				} else {
					edtCurrentLocation.setText("");
				}

			}

			field = new HashMap<String, String>();
			field.put(NAME, "field_price");
			field.put(VALUE, optionPrice);
			searchField.add(field);

			break;
		}

		try {
			loadNew(SobiproSearchResultActivity.class, SobiproSearchActivity.this, false, "IN_POS", IN_POS, "IN_OPTIONCRITERIA", optionCriteria, "IN_SEARCH_KEY", edtKeyword
					.getText().toString().trim(), "IN_SECTION_ID", section_id, "IN_SEARCHFIELD", searchField, "IN_PAGELAYOUT", IN_PAGELAYOUT, "IN_FEATUREDFIRST", IN_FEATUREDFIRST);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
