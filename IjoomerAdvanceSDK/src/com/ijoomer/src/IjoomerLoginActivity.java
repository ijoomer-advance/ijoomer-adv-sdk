package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerLoginMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.FacebookLoginHandller;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public class IjoomerLoginActivity extends IjoomerLoginMaster {

	private IjoomerTextView btnSignUp;
	private IjoomerTextView btnForgetPassword;
	private IjoomerTextView btnForgetUserName;
	private IjoomerEditText edtUserName;
	private IjoomerEditText edtPassword;
	private IjoomerButton btnLogin;
	private IjoomerButton btnFbLogin;

	private IjoomerGlobalConfiguration globalConfiguration;

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_login;
	}

	@Override
	public void initComponents() {

		edtUserName = (IjoomerEditText) findViewById(R.id.edtUserName);
		edtPassword = (IjoomerEditText) findViewById(R.id.edtPassword);
		btnLogin = (IjoomerButton) findViewById(R.id.btnLogin);
		btnFbLogin = (IjoomerButton) findViewById(R.id.btnFbLogin);
		btnSignUp = (IjoomerTextView) findViewById(R.id.btnSignUp);
		btnForgetPassword = (IjoomerTextView) findViewById(R.id.btnForgetPassword);
		btnForgetUserName = (IjoomerTextView) findViewById(R.id.btnForgetUserName);

		globalConfiguration = new IjoomerGlobalConfiguration(IjoomerLoginActivity.this);

	}

	@Override
	public void prepareViews() {

		btnSignUp.setText(Html.fromHtml(getString(R.string.create_account)));
		btnForgetPassword.setText(Html.fromHtml(getString(R.string.forget_password)));
		btnForgetUserName.setText(Html.fromHtml(getString(R.string.forget_username)));
		btnSignUp.setMovementMethod(new LinkMovementMethod());
		edtUserName.setText(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""));
		edtPassword.setText(getSmartApplication().readSharedPreferences().getString(SP_PASSWORD, ""));
		if (!IjoomerGlobalConfiguration.isAllowRegistration()) {
			// btnSignUp.setVisibility(View.GONE);
		}

	}

	@Override
	public void setActionListeners() {
		btnForgetUserName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showForgetUserNameDialog();
			}
		});

		btnForgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showForgetPasswordDialog();
			}
		});
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				loadNew(IjoomerRegistrationStep1Activity.class, IjoomerLoginActivity.this, false);

			}
		});

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				getSmartApplication().writeSharedPreferences(SP_USERNAME, edtUserName.getText().toString().trim());
				getSmartApplication().writeSharedPreferences(SP_PASSWORD, edtPassword.getText().toString().trim());

				boolean validationFlag = true;
				if (edtPassword.getText().toString().trim().length() <= 0) {
					edtPassword.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtUserName.getText().toString().trim().length() <= 0) {
					edtUserName.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (validationFlag) {
					IjoomerOauth.getInstance(IjoomerLoginActivity.this).authenticateUser(edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim(),
							new WebCallListener() {
								final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.authenticating_user));

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										globalConfiguration.loadGlobalConfiguration(new WebCallListener() {

											@Override
											public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
												proSeekBar.setProgress(100);
												if (responseCode == 200) {
													try {

														if (getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, "").length() > 0) {
															Intent intent = new Intent();
															intent.setClassName(IjoomerLoginActivity.this,
																	getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, ""));
															intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
															getSmartApplication().writeSharedPreferences(SP_LAST_ACTIVITY, "");
															startActivity(intent);
															getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
															finish();
														} else {
															if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
																Intent intent = new Intent();
																intent.setClassName(IjoomerLoginActivity.this,
																		getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
																intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
																startActivity(intent);
																finish();
															} else {

															}
															// loadNew(JomActivitiesActivity.class,
															// IjoomerLoginActivity.this,
															// true,
															// "IN_USERID",
															// "0");
														}
													} catch (Throwable e) {
														e.printStackTrace();
													}
													getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
												} else {
													responseMessageHandler(responseCode, true);
												}
											}

											@Override
											public void onProgressUpdate(int progressCount) {
												if (progressCount > 90) {
													try {
														proSeekBar.setProgress(progressCount);
													} catch (Throwable e) {
													}
												}
											}
										});

									} else {
										proSeekBar.setProgress(100);
										responseMessageHandler(responseCode, true);
									}
								}

								@Override
								public void onProgressUpdate(int progressCount) {
									if (progressCount < 91) {
										proSeekBar.setProgress(progressCount);
									}
								}
							});
				}

			}
		});

		btnFbLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginWithFacebook(new FacebookLoginHandller() {

					@Override
					public void loginStatus(int status, final JSONObject data) {

						final SeekBar progressBar = IjoomerUtilities.getLoadingDialog("Connecting through facebook...");
						IjoomerOauth.getInstance(IjoomerLoginActivity.this).authenticateUserWithFacebook(data, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								progressBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 703) {
									showUserSelectionDialog(data);
								} else if (responseCode == 200) {
									try {
										if (getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, "").length() > 0) {
											Intent intent = new Intent();
											intent.setClassName(IjoomerLoginActivity.this, getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, ""));
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											getSmartApplication().writeSharedPreferences(SP_LAST_ACTIVITY, "");
											startActivity(intent);
											getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
											getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);
											finish();
										} else {
											if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
												Intent intent = new Intent();
												intent.setClassName(IjoomerLoginActivity.this,
														getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
												intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(intent);
												finish();
											} else {

											}
											// loadNew(JomActivitiesActivity.class,
											// IjoomerLoginActivity.this, true,
											// "IN_USERID", "0");
										}

									} catch (Throwable e) {
										e.printStackTrace();
									}
									getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
									getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);
								} else {
									if (responseCode != 200) {
										responseMessageHandler(responseCode, true);
									}
								}
							}
						});

					}
				});
			}
		});

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent("clearStackActivity");
		intent.setType("text/plain");
		sendBroadcast(intent);
		moveTaskToBack(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		facebookLogout();
	}

	/**
	 * Class method
	 */

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getCustomOkDialog(getString(R.string.login), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
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