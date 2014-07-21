package com.ijoomer.plugins;

import android.content.ContentProviderOperation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.plugins.PluginsContactUsDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This Class Contains UnNormalizeFields PluginsContactUsActivity.
 * 
 * @author tasol
 * 
 */
public class PluginsContactUsActivity extends PluginsMasterActivity {

	private LinearLayout lnrContactPostcode;
	private LinearLayout lnrContactTelephone;
	private LinearLayout lnrContactFax;
	private LinearLayout lnrContactMobile;
	private LinearLayout lnrContactInfo;
	private LinearLayout lnrContactForm;
	private LinearLayout lnrLinks;
	private LinearLayout lnrTab;
	private IjoomerEditText edtName;
	private IjoomerEditText edtEmail;
	private IjoomerEditText edtMessage;
	private IjoomerTextView txtValueName;
	private IjoomerTextView txtValuePosition;
	private IjoomerTextView txtValueAddress;
	private IjoomerTextView txtValueState;
	private IjoomerTextView txtValueCountry;
	private IjoomerTextView txtValuePostcode;
	private IjoomerTextView txtValueCity;
	private IjoomerTextView txtValueTelephone;
	private IjoomerTextView txtValueFax;
	private IjoomerTextView txtValueMobile;
	private IjoomerTextView txtValueWebpage;
	private IjoomerTextView txtValueMisc;
	private IjoomerTextView txtValueEmailTo;
	private IjoomerButton btnSendEmail;
	private IjoomerButton btnAddToContact;
	private IjoomerRadioButton rdbContactInfo;
	private IjoomerRadioButton rdbContactForm;
	private Spinner spnSubject;
	private ImageView image;
	private SeekBar proSeekBar;

	private ArrayList<String> subjects;
	private HashMap<String, String> contactInfo;
	private PluginsContactUsDataProvider contactUsDataProvider = new PluginsContactUsDataProvider(this);
	private AQuery androidAQuery;

	private String IN_ID = "";
	private String IN_SUBJECT = "";
	private String IN_ITEM_ID = "";
	private String IN_SHOW_CONTACT_FORM = "0";
	private String form = "1";
	private String imagePath;

	private JSONObject IN_OBJ;

	/**
	 * Overrides methods
	 */
	@Override
	public int setLayoutId() {
		return R.layout.plugins_contact_us;
	}

	@Override
	public void initComponents() {

		lnrContactPostcode = (LinearLayout) findViewById(R.id.lnrContactPostcode);
		lnrContactTelephone = (LinearLayout) findViewById(R.id.lnrContactTelephone);
		lnrContactFax = (LinearLayout) findViewById(R.id.lnrContactFax);
		lnrContactMobile = (LinearLayout) findViewById(R.id.lnrContactMobile);
		lnrLinks = (LinearLayout) findViewById(R.id.lnrLinks);
		lnrContactInfo = (LinearLayout) findViewById(R.id.lnrContactInfo);
		lnrContactForm = (LinearLayout) findViewById(R.id.lnrContactForm);
		lnrTab = (LinearLayout) findViewById(R.id.lnrTab);
		txtValueName = (IjoomerTextView) findViewById(R.id.txtValueName);
		txtValuePosition = (IjoomerTextView) findViewById(R.id.txtValuePosition);
		txtValueAddress = (IjoomerTextView) findViewById(R.id.txtValueAddress);
		txtValueState = (IjoomerTextView) findViewById(R.id.txtValueState);
		txtValueCountry = (IjoomerTextView) findViewById(R.id.txtValueCountry);
		txtValuePostcode = (IjoomerTextView) findViewById(R.id.txtValuePostcode);
		txtValueCity = (IjoomerTextView) findViewById(R.id.txtValueCity);
		txtValueTelephone = (IjoomerTextView) findViewById(R.id.txtValueTelephone);
		txtValueFax = (IjoomerTextView) findViewById(R.id.txtValueFax);
		txtValueMobile = (IjoomerTextView) findViewById(R.id.txtValueMobile);
		txtValueWebpage = (IjoomerTextView) findViewById(R.id.txtValueWebpage);
		txtValueMisc = (IjoomerTextView) findViewById(R.id.txtValueMisc);
		txtValueEmailTo = (IjoomerTextView) findViewById(R.id.txtValueEmailTo);
		rdbContactInfo = (IjoomerRadioButton) findViewById(R.id.rdbContactInfo);
		rdbContactForm = (IjoomerRadioButton) findViewById(R.id.rdbContactForm);
		image = (ImageView) findViewById(R.id.image);
		edtName = (IjoomerEditText) findViewById(R.id.edtContactName);
		edtEmail = (IjoomerEditText) findViewById(R.id.edtContactEmail);
		edtMessage = (IjoomerEditText) findViewById(R.id.edtContactMessage);
		btnSendEmail = (IjoomerButton) findViewById(R.id.btnSendEmail);
		btnAddToContact = (IjoomerButton) findViewById(R.id.btnAddToContact);
		spnSubject = (Spinner) findViewById(R.id.spnSubject);

		subjects = new ArrayList<String>();
		androidAQuery = new AQuery(this);

	}

	@Override
	public void prepareViews() {
		getIntentData();
		if (IN_SHOW_CONTACT_FORM.equalsIgnoreCase("1")) {
			if (IN_SUBJECT.length() > 0) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(PluginsContactUsActivity.this, android.R.layout.simple_spinner_item, subjects);
				adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
				spnSubject.setAdapter(adapter);
			} else {
				spnSubject.setVisibility(View.GONE);
			}
		} else {
			lnrTab.setVisibility(View.GONE);
		}
		getContactInfo();
		((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());

	}

	@Override
	public void setActionListeners() {

		btnSendEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (contactValidation()) {
					String name = edtName.getText().toString();
					String subject = subjects.get(spnSubject.getSelectedItemPosition());
					String message = edtMessage.getText().toString();
					String email = edtEmail.getText().toString();
					form = "0";
					proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					contactUsDataProvider.sendContact(form, IN_ITEM_ID, IN_ID, name, email, subject, message, new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							ting(errorMessage);
							clearAll();

						}
					});

				}

			}
		});
		btnAddToContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

				ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
						.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
				if (txtValueName.getText().toString().length() > 0) {
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, txtValueName.getText().toString()).build());
				}
				if (txtValueMobile.getText().toString().length() > 0) {
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, txtValueMobile.getText().toString())
							.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
				}
				if (txtValueTelephone.getText().toString().length() > 0) {
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, txtValueTelephone.getText().toString())
							.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME).build());
				}
				if (txtValueEmailTo.getText().toString().length() > 0) {
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Email.DATA, txtValueEmailTo.getText().toString())
							.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());
				}

				if (txtValueAddress.getText().toString().length() > 0) {
					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA, txtValueAddress.getText().toString())
							.withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK).build());
				}
				try {
					Bitmap bitmap = getBitmapFromURL(imagePath);

					ByteArrayOutputStream image = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, image);

					ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
							.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
							.withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, image.toByteArray()).build());
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
					ting(getString(R.string.addcontactsucessfully));

				} catch (Exception e) {
					e.printStackTrace();
					ting(getString(R.string.addcontacterror));

				}
				btnAddToContact.setVisibility(View.GONE);

			}
		});

		rdbContactForm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				lnrContactInfo.setVisibility(View.GONE);
				lnrContactForm.setVisibility(View.VISIBLE);

			}
		});
		rdbContactInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				lnrContactForm.setVisibility(View.GONE);
				lnrContactInfo.setVisibility(View.VISIBLE);

			}
		});

	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get contact info.
	 */
	private void getContactInfo() {
		form = "1";
		proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		contactUsDataProvider.getContactInfo(form, IN_ID, IN_ITEM_ID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					if (data1 != null && data1.size() > 0)
						contactInfo = data1.get(0);
					createView();
				} else {
					IjoomerUtilities.getCustomOkDialog(getScreenCaption(), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
							getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {
								}
							});
				}

			}
		});
	}

	/**
	 * This method used to contact validation.
	 * 
	 * @return
	 */
	private boolean contactValidation() {
		boolean valid = true;
		if (edtName.getText().length() == 0) {
			valid = false;
			edtName.setError(getString(R.string.validation_value_required));
		}
		if (edtMessage.getText().length() == 0) {
			valid = false;
			edtMessage.setError(getString(R.string.validation_value_required));
		}
		if (edtEmail.getText().length() == 0) {
			valid = false;
			edtEmail.setError(getString(R.string.validation_value_required));
		} else if (!IjoomerUtilities.emailValidator(edtEmail.getText().toString().trim())) {
			valid = false;
			edtEmail.setError(getString(R.string.validation_invalid_email));
		}
		return valid;
	}

	/**
	 * This method used to get intent data.
	 */
	public void getIntentData() {
		try {
			IN_OBJ = new JSONObject(getIntent().getStringExtra("IN_OBJ"));
			IN_ITEM_ID = IN_OBJ.getString("itemid");

			JSONObject obj = new JSONObject(IN_OBJ.getString(ITEMDATA));

			IN_ID = obj.getString("id");
			IN_SHOW_CONTACT_FORM = obj.getString("showContactForm");
			IN_SUBJECT = obj.getString("subjectLine");

			subjects = new ArrayList<String>(Arrays.asList(IN_SUBJECT.split(",")));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method used to clear all data.
	 */
	public void clearAll() {
		edtEmail.setText("");
		edtName.setText("");
		edtMessage.setText("");
		spnSubject.setSelection(0);
	}

	/**
	 * This method used to get bitmap from url.
	 * 
	 * @param src
	 *            represented source url
	 * @return represented {@link Bitmap}
	 */
	public Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method used to create contact view.
	 */
	public void createView() {

		if (contactInfo.get(NAME).trim().toString().length() > 0) {
			txtValueName.setText(contactInfo.get(NAME).trim());
		}
		if (contactInfo.get(POSITON).trim().toString().length() > 0) {
			txtValuePosition.setText(contactInfo.get(POSITON).trim());
		}
		if (contactInfo.get(ADDRESS).trim().toString().length() > 0) {
			txtValueAddress.setText(contactInfo.get(ADDRESS).trim());
		}
		if (contactInfo.get(STATE).trim().toString().length() > 0) {
			txtValueState.setText(contactInfo.get(STATE).trim());
		}
		if (contactInfo.get(COUNTRY).trim().toString().length() > 0) {
			txtValueCountry.setText(contactInfo.get(COUNTRY).trim());
		}
		if (contactInfo.get(POSTCODE).trim().toString().length() > 0 && contactInfo.get(CITY).trim().toString().length() == 0) {
			txtValuePostcode.setText(contactInfo.get(POSTCODE).trim());
			lnrContactPostcode.setVisibility(View.VISIBLE);
		}
		if (contactInfo.get(CITY).trim().toString().length() > 0) {
			txtValueCity.setText(contactInfo.get(CITY).trim() + "-" + contactInfo.get(POSTCODE).trim());
		}
		if (contactInfo.get(TELEPHONE).trim().toString().length() > 0) {
			txtValueTelephone.setText(contactInfo.get(TELEPHONE).trim());
			lnrContactTelephone.setVisibility(View.VISIBLE);
		}
		if (contactInfo.get(FAX).trim().toString().length() > 0) {
			txtValueFax.setText(contactInfo.get(FAX).trim());
			lnrContactFax.setVisibility(View.VISIBLE);
		}
		if (contactInfo.get(MOBILE).trim().toString().length() > 0) {
			txtValueMobile.setText(contactInfo.get(MOBILE).trim());
			lnrContactMobile.setVisibility(View.VISIBLE);
		}
		if (contactInfo.get(WEBPAGE).trim().toString().length() > 0) {
			txtValueWebpage.setText(contactInfo.get(WEBPAGE).trim());
		}
		if (contactInfo.get(MISC).trim().toString().length() > 0) {
			txtValueMisc.setText(contactInfo.get(MISC).trim());
		}
		if (contactInfo.get(EMAILTO).trim().toString().length() > 0) {
			txtValueEmailTo.setText(contactInfo.get(EMAILTO).trim());
		}

		if (contactInfo.containsKey(IMAGE)) {
			androidAQuery.id(image).image(contactInfo.get(IMAGE));
			imagePath = contactInfo.get(IMAGE);
		} else {
			image.setVisibility(View.GONE);
		}
		btnAddToContact.setVisibility(View.VISIBLE);
		if (contactInfo.get(LINKS) != null && contactInfo.get(LINKS).length() > 0) {

			try {
				lnrLinks.removeAllViews();
				JSONArray jsonArrayUrls = new JSONArray(contactInfo.get(LINKS));
				for (int i = 0; i < jsonArrayUrls.length(); i++) {
					JSONObject jsonObject = jsonArrayUrls.getJSONObject(i);
					IjoomerTextView textUrl = new IjoomerTextView(PluginsContactUsActivity.this);

					textUrl.setTag(jsonObject.get("url").toString());
					textUrl.setTextColor(Color.parseColor(getString(R.color.blue)));

					SpannableString spanString = new SpannableString(jsonObject.get("caption").toString());
					spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
					spanString.setSpan(new StyleSpan(Typeface.NORMAL), 0, spanString.length(), 0);
					textUrl.setText(spanString);

					textUrl.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							String url = (String) v.getTag();
							Intent intent = new Intent(PluginsContactUsActivity.this, IjoomerWebviewClient.class);
							intent.putExtra("url", url);
							startActivity(intent);

						}
					});

					lnrLinks.addView(textUrl);

				}

			} catch (Exception e) {

			}
		}
	}
}
