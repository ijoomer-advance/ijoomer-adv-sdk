package com.ijoomer.src;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.ijoomer.common.classes.IjoomerRegistrationMaster;
import com.ijoomer.common.classes.IjoomerSpannable;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.oauth.IjoomerRegistration;
import com.ijoomer.oauth.IjoomerUsersDataProvider;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerRegistrationStep1Activity.
 * 
 * @author tasol
 * 
 */
public class IjoomerRegistrationStep1Activity extends IjoomerRegistrationMaster {

	private LinearLayout lnrTermsNCondition;
	private IjoomerTextView txtGallery;
	private IjoomerTextView txtCapture;
	private IjoomerTextView txtTermsNCondition;
	private IjoomerEditText edtName;
	private IjoomerEditText edtUserName;
	private IjoomerEditText edtPassword;
	private IjoomerEditText edtEmail;
	private IjoomerButton btnContinue;
	private IjoomerButton btnCancle;
	private IjoomerCheckBox chkTermsNCondition;
	private ImageView imgUser;
	private Spinner spnRegistrationType;

	private String selectedImagePath = "";
	final private int PICK_IMAGE = 1;
	final private int CAPTURE_IMAGE = 2;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_registration_step1;
	}

	@Override
	public void initComponents() {

		lnrTermsNCondition = (LinearLayout) findViewById(R.id.lnrTermsNCondition);
		txtCapture = (IjoomerTextView) findViewById(R.id.txtCapture);
		txtGallery = (IjoomerTextView) findViewById(R.id.txtGallery);
		txtTermsNCondition = (IjoomerTextView) findViewById(R.id.txtTermsNCondition);
		txtTermsNCondition.setMovementMethod(new LinkMovementMethod());
		txtTermsNCondition.setText(addClickablePart(Html.fromHtml(getString(R.string.terms_n_condition_first) + "  " + getString(R.string.terms_n_condition_second))),
				BufferType.SPANNABLE);
		edtName = (IjoomerEditText) findViewById(R.id.edtName);
		edtUserName = (IjoomerEditText) findViewById(R.id.edtUserName);
		edtPassword = (IjoomerEditText) findViewById(R.id.edtPassword);
		edtEmail = (IjoomerEditText) findViewById(R.id.edtEmail);
		btnContinue = (IjoomerButton) findViewById(R.id.btnContinue);
		btnCancle = (IjoomerButton) findViewById(R.id.btnCancle);
		imgUser = (ImageView) findViewById(R.id.imgUser);
		chkTermsNCondition = (IjoomerCheckBox) findViewById(R.id.chkTermsNCondition);
		spnRegistrationType = (Spinner) findViewById(R.id.spnRegistrationType);

		selectedImagePath = IjoomerGlobalConfiguration.getDefaultAvatar();
		imgUser.setImageBitmap(decodeFile(selectedImagePath));

		getProfileType();
	}

	@Override
	public void prepareViews() {
		((IjoomerTextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getString(R.string.header_registration));
		if (IjoomerGlobalConfiguration.isEnableTerms()) {
			lnrTermsNCondition.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_CANCELED) {
			if (requestCode == PICK_IMAGE) {
				selectedImagePath = getAbsolutePath(data.getData());
				imgUser.setImageBitmap(decodeFile(selectedImagePath));
			} else if (requestCode == CAPTURE_IMAGE) {
				selectedImagePath = getImagePath();
				imgUser.setImageBitmap(decodeFile(selectedImagePath));
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}

	}

	@Override
	public void setActionListeners() {

		txtGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);

			}
		});

		txtCapture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
				startActivityForResult(intent, CAPTURE_IMAGE);
			}
		});

		btnContinue.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View paramView) {
				hideSoftKeyboard();
				boolean validationFlag = true;
				if (edtName.getText().toString().trim().length() <= 0) {
					edtName.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtUserName.getText().toString().trim().length() <= 0) {
					edtUserName.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtPassword.getText().toString().trim().length() <= 0) {
					edtPassword.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtEmail.getText().toString().trim().length() <= 0) {
					edtEmail.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				} else {
					if (!IjoomerUtilities.emailValidator(edtEmail.getText().toString().trim())) {
						validationFlag = false;
						edtEmail.setError(getString(R.string.validation_invalid_email));
					}
				}

				if (IjoomerGlobalConfiguration.isEnableTerms() && validationFlag && !chkTermsNCondition.isChecked()) {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.registration), getString(R.string.accept_terms_and_condition), getString(R.string.ok),
							R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMethod() {

								}
							});
					validationFlag = false;
				}

				if (validationFlag) {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_register_newuser));
					final String profileType = ((ArrayList<HashMap<String, String>>) spnRegistrationType.getTag()).get(spnRegistrationType.getSelectedItemPosition()).get("id");
					new IjoomerRegistration(IjoomerRegistrationStep1Activity.this).signUpNewUser(selectedImagePath, edtName.getText().toString().trim(), edtUserName.getText()
							.toString().trim(), edtPassword.getText().toString().trim(), edtEmail.getText().toString(), profileType, new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								loadNew(IjoomerRegistrationStep2Activity.class, IjoomerRegistrationStep1Activity.this, false);
							} else {
								responseMessageHandler(responseCode, true);
							}
						}
					});
				}
			}
		});

		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				finish();
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
	 * This method used to get registration profile type. 
	 */
	private void getProfileType() {
		new IjoomerRegistration(this).getProfileTypes(new WebCallListener() {
			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_register_user_profile_type));

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					if (data1 != null) {
						spnRegistrationType.setTag(data1);

						ArrayList<String> profileTypes = new ArrayList<String>();

						for (HashMap<String, String> hashMap : data1) {
							profileTypes.add(hashMap.get("name"));
						}
						spnRegistrationType.setAdapter(new IjoomerUtilities.MyCustomAdapter(IjoomerRegistrationStep1Activity.this, profileTypes));
						if (profileTypes.size() > 1) {
							spnRegistrationType.setVisibility(View.VISIBLE);
						}
					}
				} else {
					responseMessageHandler(responseCode, true);
				}
			}
		});
	}

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getCustomOkDialog(getString(R.string.registration), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});

	}

	/**
	 * This method used to add clicable part on spannable string.
	 * @param strSpanned represented spannable string
	 * @return represented {@link SpannableStringBuilder}
	 */
	public SpannableStringBuilder addClickablePart(Spanned strSpanned) {
		String str = strSpanned.toString();
		SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

		if (str.contains(getString(R.string.terms_n_condition_second))) {
			ssb.setSpan(new IjoomerSpannable(Color.parseColor(getString(R.color.blue)), true) {

				@Override
				public void onClick(View widget) {
					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_register_user_profile_type));
					new IjoomerUsersDataProvider(IjoomerRegistrationStep1Activity.this).getTermsNCondition(IjoomerGlobalConfiguration.getTermsObject(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								IjoomerUtilities.getTermsNConditionDialog(getString(R.string.terms_n_condition_second), data1.get(0).get("termsNcondition"));
							} else {
								responseMessageHandler(responseCode, false);
							}
						}
					});
				}
			}, str.indexOf(getString(R.string.terms_n_condition_second)), str.indexOf(getString(R.string.terms_n_condition_second))
					+ getString(R.string.terms_n_condition_second).length(), 0);

		}
		return ssb;

	}
}