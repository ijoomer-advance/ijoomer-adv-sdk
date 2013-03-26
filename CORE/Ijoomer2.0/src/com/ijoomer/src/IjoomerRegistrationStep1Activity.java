package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ijoomer.common.classes.IjoomerRegistrationMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.oauth.IjoomerRegistration;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public class IjoomerRegistrationStep1Activity extends IjoomerRegistrationMaster {

	private IjoomerTextView txtGallery;
	private IjoomerTextView txtCapture;
	private IjoomerEditText edtName;
	private IjoomerEditText edtUserName;
	private IjoomerEditText edtPassword;
	private IjoomerEditText edtEmail;
	private IjoomerButton btnContinue;
	private IjoomerButton btnCancle;
	private ImageView imgUser;
	private Spinner spnRegistrationType;

	final private int PICK_IMAGE = 1;
	final private int CAPTURE_IMAGE = 2;
	private String selectedImagePath = "";

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_registration_step1;
	}

	@Override
	public void initComponents() {

		txtCapture = (IjoomerTextView) findViewById(R.id.txtCapture);
		txtGallery = (IjoomerTextView) findViewById(R.id.txtGallery);
		edtName = (IjoomerEditText) findViewById(R.id.edtName);
		edtUserName = (IjoomerEditText) findViewById(R.id.edtUserName);
		edtPassword = (IjoomerEditText) findViewById(R.id.edtPassword);
		edtEmail = (IjoomerEditText) findViewById(R.id.edtEmail);
		btnContinue = (IjoomerButton) findViewById(R.id.btnContinue);
		btnCancle = (IjoomerButton) findViewById(R.id.btnCancle);
		imgUser = (ImageView) findViewById(R.id.imgUser);
		spnRegistrationType = (Spinner) findViewById(R.id.spnRegistrationType);

		selectedImagePath = IjoomerGlobalConfiguration.defaultUserImage;
		imgUser.setImageBitmap(decodeFile(selectedImagePath));

		getProfileType();
	}

	@Override
	public void prepareViews() {
		((IjoomerTextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getString(R.string.header_registration));
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
	 * Class method
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
						if (profileTypes.size() <= 1) {
							spnRegistrationType.setVisibility(View.GONE);
						}
					}
				} else {
					responseMessageHandler(responseCode, true);
				}
			}
		});
	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getCustomOkDialog(getString(R.string.registration), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMathod() {
						if (responseCode == 599 && finishActivityOnConnectionProblem) {
							finish();
						}
					}
				});

	}
}