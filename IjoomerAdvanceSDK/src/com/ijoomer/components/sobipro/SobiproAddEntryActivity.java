package com.ijoomer.components.sobipro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.MyCustomAdapter;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.custom.interfaces.IjoomerClickListner;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproAddEntryDataProvider;
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
 * This Class Contains All Method Related To SobiproAddEntryActivity.
 * 
 * @author tasol
 * 
 */
public class SobiproAddEntryActivity extends SobiproMasterActivity {

	private SobiproAddEntryDataProvider dataProvider;
	private LinearLayout lnr_form;
	final private int PICK_IMAGE_USER_AVATAR = 1;
	final private int CAPTURE_IMAGE_USER_AVATAR = 2;
	private String selectedImagePathUserAvatar;
	private Bitmap selectedImage;
	private ImageView image;
	private TextView txtImageCaption;
	private IjoomerEditText editMap;
	final private int GET_ADDRESS_FROM_MAP = 3;
	private Button btnApply;
	private Button btnCancel;
	private String value, id;
	private String IN_SECTION_ID;
	private LinearLayout lnrApply;
	private String latitude, longitude;
	private ArrayList<String> pageLayouts;
	private String IN_PAGELAYOUT;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_add_entry;
	}

	@Override
	public void initComponents() {
		value = "";
		id = "";
		dataProvider = new SobiproAddEntryDataProvider(this);
		lnr_form = (LinearLayout) findViewById(R.id.add_entry_lnr_form);
		lnrApply = (LinearLayout) findViewById(R.id.lnrApply);
		btnApply = (Button) findViewById(R.id.btnApply);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		pageLayouts = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sobipro_pageLayout)));
	}

	@Override
	public void prepareViews() {
		latitude = getLatitude();
		longitude = getLongitude();

		getIntentData();
		getEntryFields();
	}

	@Override
	public void setActionListeners() {
		btnApply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSelectedData();
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PICK_IMAGE_USER_AVATAR) {
				selectedImagePathUserAvatar = getAbsolutePath(data.getData());
				selectedImage = decodeFile(selectedImagePathUserAvatar);
				image.setImageBitmap(selectedImage);
				image.setTag(selectedImagePathUserAvatar);
				txtImageCaption.setVisibility(View.INVISIBLE);
			} else if (requestCode == CAPTURE_IMAGE_USER_AVATAR) {
				selectedImagePathUserAvatar = getImagePath();
				selectedImage = decodeFile(selectedImagePathUserAvatar);
				image.setImageBitmap(selectedImage);
				image.setTag(selectedImagePathUserAvatar);
				txtImageCaption.setVisibility(View.INVISIBLE);
			} else if (requestCode == GET_ADDRESS_FROM_MAP) {
				editMap.setText(((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("address"));
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
			IN_SECTION_ID = this.getIntent().getStringExtra("IN_SECTION_ID");
			IN_PAGELAYOUT = getIntent().getStringExtra("IN_PAGELAYOUT");
			if (IN_SECTION_ID == null && IN_PAGELAYOUT == null) {

				JSONObject IN_OBJ = new JSONObject(new JSONObject(getIntent().getStringExtra("IN_OBJ")).getString(ITEMDATA));
				IN_SECTION_ID = IN_OBJ.getString(SECTION_ID);
				IN_PAGELAYOUT = IN_OBJ.getString(PAGELAYOUT);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * This method is used to get selected data from dynamic created form.
	 */

	@SuppressWarnings("unchecked")
	private void getSelectedData() {
		boolean validationFlag = true;
		ArrayList<HashMap<String, String>> searchField = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> field;
		int size = lnr_form.getChildCount();

		for (int i = 0; i < size; i++) {
			View v = (LinearLayout) lnr_form.getChildAt(i);
			field = new HashMap<String, String>();
			field.putAll((HashMap<String, String>) v.getTag());
			IjoomerEditText edtValue = null;
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

				} else if (field.get(TYPE).equals(IMAGE)) {
					ImageView txtValue;
					txtValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrImageText)).findViewById(R.id.txtValue);
					if (txtValue.getTag() != null) {
						field.put("image", txtValue.getTag().toString());
						searchField.add(field);
					}
				}
				if (edtValue != null) {
					if (field.get(REQUIRED).equals("1") && edtValue.getText().toString().length() <= 0) {
						edtValue.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					} else if (field.get(NAME).equalsIgnoreCase("field_email") && edtValue.getText().toString().length() > 0
							&& !IjoomerUtilities.emailValidator(edtValue.getText().toString())) {
						edtValue.setError(getString(R.string.validation_invalid_email));
						validationFlag = false;
					} else {
						if (field.get(NAME).equalsIgnoreCase("entry_parent")) {
							field.put(VALUE, id);
						} else {
							field.put(VALUE, edtValue.getText().toString().trim());
						}
						searchField.add(field);
					}

				}
			}
		}

		switch (pageLayouts.indexOf(IN_PAGELAYOUT)) {
		case 1:
			field = new HashMap<String, String>();
			field.put(NAME, "pid");
			field.put(VALUE, IN_SECTION_ID);
			searchField.add(field);
			break;
		case 2:
			field = new HashMap<String, String>();
			field.put(NAME, "field_location_latitude");
			field.put(VALUE, latitude);
			searchField.add(field);
			field = new HashMap<String, String>();
			field.put(NAME, "field_location_longitude");
			field.put(VALUE, longitude);
			searchField.add(field);
			field = new HashMap<String, String>();
			field.put(NAME, "pid");
			field.put(VALUE, IN_SECTION_ID);
			searchField.add(field);
			break;
		default:
			field = new HashMap<String, String>();
			field.put(NAME, "field_location_latitude");
			field.put(VALUE, latitude);
			searchField.add(field);
			field = new HashMap<String, String>();
			field.put(NAME, "field_location_longitude");
			field.put(VALUE, longitude);
			searchField.add(field);
			field = new HashMap<String, String>();
			field.put(NAME, "pid");
			field.put(VALUE, IN_SECTION_ID);
			searchField.add(field);
			break;
		}

		if (validationFlag) {
			dataProvider.addEntry(IN_SECTION_ID, searchField, new WebCallListener() {
				final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

				@Override
				public void onProgressUpdate(int progressCount) {
					proSeekBar.setProgress(progressCount);
				}

				@Override
				public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					try {
						if (responseCode == 200) {
							ting(getString(R.string.sobipro_entry_added));
							if (pageLayouts.indexOf(IN_PAGELAYOUT) == 2)
								IjoomerApplicationConfiguration.setReloadRequired(true);
							finish();
						} else {
							IjoomerUtilities.getCustomOkDialog(SobiproAddEntryActivity.this.getScreenCaption(),
									getString(getResources().getIdentifier("code" + responseCode, "string", SobiproAddEntryActivity.this.getPackageName())),
									getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
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

	}


	/**
	 * This method is used to get dynamic fields for Entry.
	 */

	private void getEntryFields() {
		dataProvider.getEntryFields(IN_SECTION_ID, new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (data1 != null && data1.size() > 0) {
						createForm(data1);
					} else {
						IjoomerUtilities.getCustomOkDialog(((IjoomerSuperMaster) SobiproAddEntryActivity.this).getScreenCaption(),
								getString(getResources().getIdentifier("code204", "string", SobiproAddEntryActivity.this.getPackageName())), getString(R.string.ok),
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
	 * This method is used to create form using dynamic fields.
	 * 
	 * @param FIELD_LIST
	 *            represented dynamic field list to create form for add entry.
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

			if (field.get(TYPE).equals(TEXT)) {
				final IjoomerEditText edit;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				edit.setText(Html.fromHtml(field.get(VALUE)));
				edit.setHint(field.get(CAPTION));
				if (field.get(NAME).equalsIgnoreCase("field_phone") || field.get(NAME).equalsIgnoreCase("field_zip") || field.get(NAME).equalsIgnoreCase("field_distance")
						|| field.get(NAME).equalsIgnoreCase("field_fax") || field.get(NAME).equalsIgnoreCase("field_working_hours")) {
					edit.setInputType(InputType.TYPE_CLASS_PHONE);
				}
			} else if (field.get(TYPE).equals(TEXTAREA)) {
				final IjoomerEditText edit;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				edit.setText(Html.fromHtml(field.get(VALUE)));
				edit.setHint(field.get(CAPTION));

			} else if (field.get(TYPE).equals(SELECT)) {

				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrSpin));
				layout.setVisibility(View.VISIBLE);
				MyCustomAdapter adapter = IjoomerUtilities.getSpinnerAdapter(field);
				((Spinner) layout.findViewById(R.id.txtValue)).setAdapter(adapter);
				((Spinner) layout.findViewById(R.id.txtValue)).setSelection(adapter.getDefaultPosition());

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
						IjoomerUtilities.getMultiSelectionDialogSobipro(field.get(CAPTION), field.get(OPTIONS), value, id, new IjoomerClickListner() {

							@Override
							public void onClick(String value, String id) {

								SobiproAddEntryActivity.this.value = value;
								if (field.get(NAME).equalsIgnoreCase("entry_parent"))
									SobiproAddEntryActivity.this.id = id;
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

				if (field.get(VALUE).toString().trim().length() <= 0) {
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
						editMap = edit;
						Intent intent = new Intent(SobiproAddEntryActivity.this, IjoomerMapAddress.class);
						startActivityForResult(intent, GET_ADDRESS_FROM_MAP);
					}
				});

			} else if (field.get(TYPE).equals(IMAGE)) {
				TextView txtCaption;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrImageText));
				layout.setVisibility(View.VISIBLE);
				txtCaption = ((TextView) layout.findViewById(R.id.txtCaption));
				txtCaption.setText("Add" + " " + field.get(CAPTION));
				layout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						image = (ImageView) v.findViewById(R.id.txtValue);
						txtImageCaption = (TextView) v.findViewById(R.id.txtCaption);
						showSelectImageDialog();
					}
				});
			} else if (field.get(TYPE).equals(CONTAINER)) {
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrWebsite));
				layout.setVisibility(View.VISIBLE);
				IjoomerEditText txtWebTitle = (IjoomerEditText) layout.findViewById(R.id.txtWebTitle);
				IjoomerEditText txtWebValue = (IjoomerEditText) layout.findViewById(R.id.txtWebValue);
				Spinner spnValue = (Spinner) layout.findViewById(R.id.spnValue);

				JSONArray jsonArray = null;
				int length = 0;
				try {
					jsonArray = new JSONArray(field.get(VALUE));
					length = jsonArray.length();

					for (int i = 0; i < length; i++) {
						JSONObject object = (JSONObject) jsonArray.getJSONObject(i);

						if (object.get(NAME).toString().equalsIgnoreCase("field_website")) {
							txtWebTitle.setHint(object.get(CAPTION).toString());
						} else if (object.get(NAME).toString().equalsIgnoreCase("field_website_url")) {
							txtWebValue.setHint(object.get(CAPTION).toString());

						} else if (object.get(NAME).toString().equalsIgnoreCase("field_website_protocol")) {

							MyCustomAdapter adapter = IjoomerUtilities.getSpinnerAdapter((new IjoomerCaching(SobiproAddEntryActivity.this).parseData(object).get(0)));
							spnValue.setAdapter(adapter);

						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			try {
				if (field.get(REQUIRED).equalsIgnoreCase("1")) {
					((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("*  ");
				} else {
					((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("   ");
				}
			} catch (Exception e) {
				((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("   ");
			}

			fieldView.setTag(field);
			lnr_form.addView(fieldView, params);
		}

		lnrApply.setVisibility(View.VISIBLE);

	}

	/**
	 * This method is used to show Image Selector dialog to select the image and
	 * upload for add new entry.
	 */

	private void showSelectImageDialog() {
		IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

			@Override
			public void onPhoneGallery() {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_USER_AVATAR);
			}

			@Override
			public void onCapture() {
				final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
				startActivityForResult(intent, CAPTURE_IMAGE_USER_AVATAR);
			}
		});
	}

}
