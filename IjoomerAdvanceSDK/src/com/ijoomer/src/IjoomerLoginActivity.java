package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;

import com.Facebook.Permissions;
import com.Facebook.Properties;
import com.Facebook.SimpleFacebook;
import com.Facebook.SimpleFacebookConfiguration;
import com.Facebook.entities.Profile;
import com.Facebook.utils.Attributes;
import com.Facebook.utils.PictureAttributes;
import com.ijoomer.common.classes.IjoomerLoginMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;


/**
 * This Class Contains All Method Related To IjoomerLoginActivity.
 *
 * @author tasol
 *
 */
public class IjoomerLoginActivity extends IjoomerLoginMaster{

	private IjoomerTextView btnSignUp;
	private IjoomerTextView btnForgetPassword;
	private IjoomerTextView btnForgetUserName;
	private IjoomerEditText edtUserName;
	private IjoomerEditText edtPassword;
	private IjoomerButton btnLogin;
	private IjoomerButton btnFbLogin;
	private IjoomerGlobalConfiguration globalConfiguration;

	private SimpleFacebook simpleFacebook;
	private SimpleFacebookConfiguration.Builder simpleFacebookConfigurationBuilder;
	private SimpleFacebookConfiguration simpleFacebookConfiguration;

	/**
	 * Overrides methods
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

		simpleFacebook = SimpleFacebook.getInstance(this);
		simpleFacebookConfigurationBuilder = new SimpleFacebookConfiguration.Builder();
		simpleFacebookConfigurationBuilder.setAppId(getString(R.string.facebook_app_id));
		simpleFacebookConfigurationBuilder.setPermissions(new Permissions[]{Permissions.EMAIL,Permissions.BASIC_INFO,Permissions.USER_PHOTOS});
		simpleFacebookConfiguration = simpleFacebookConfigurationBuilder.build();
		SimpleFacebook.setConfiguration(simpleFacebookConfiguration);
	}

	@Override
	public void prepareViews() {

		btnSignUp.setText(Html.fromHtml(getString(R.string.create_account)));
		btnForgetPassword.setText(Html.fromHtml(getString(R.string.forget_password)));
		btnForgetUserName.setText(Html.fromHtml(getString(R.string.forget_username)));
		btnSignUp.setMovementMethod(new LinkMovementMethod());
		edtUserName.setText(getSmartApplication().readSharedPreferences().getString(SP_USERNAME, ""));
		edtPassword.setText(getSmartApplication().readSharedPreferences().getString(SP_PASSWORD, ""));
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
				if(IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)){
					loadNew(IjoomerRegistrationStep1Activity_v30.class, IjoomerLoginActivity.this, false);
				}else{
					loadNew(IjoomerRegistrationStep1Activity.class, IjoomerLoginActivity.this, false);
				}
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
					IjoomerOauth.getInstance(IjoomerLoginActivity.this).authenticateUser(edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim(), new WebCallListener() {
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
													try {
														Intent intent = new Intent();
														intent.setClassName(IjoomerLoginActivity.this, getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, ""));
														intent.putExtra("IN_OBJ",getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT,""));
														intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														getSmartApplication().writeSharedPreferences(SP_LAST_ACTIVITY, "");
														startActivity(intent);
														getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
													} catch (Exception e) {
														try {
															loadNew(IjoomerHomeActivity.class, IjoomerLoginActivity.this, true, "IN_USERID", "0");
														} catch (Throwable e1) {
															e1.printStackTrace();
														}
													} finally {
														finish();
													}
												} else if(getSmartApplication().readSharedPreferences().getBoolean(SP_DOLOGIN, false)){
													getSmartApplication().writeSharedPreferences(SP_DOLOGIN, false);
													getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, true);
													getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
													finish();
												} else {
													if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
														try {
															Intent intent = new Intent();
															intent.setClassName(IjoomerLoginActivity.this, getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
															intent.putExtra("IN_OBJ",getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT,""));
															intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
															startActivity(intent);
														} catch (Exception e) {
															try {
																loadNew(IjoomerHomeActivity.class, IjoomerLoginActivity.this, true, "IN_USERID", "0");
															} catch (Throwable e1) {
																e1.printStackTrace();
															}
														} finally {
															finish();
														}

													}else{
														try {
															loadNew(IjoomerHomeActivity.class, IjoomerLoginActivity.this, true, "IN_USERID", "0");
														} catch (Throwable e1) {
															e1.printStackTrace();
														}
													}
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
				if(simpleFacebook.isLogin()){
					getProfile();
				}else{
					simpleFacebook.login(new OnLoginListener());
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		if(getSmartApplication().readSharedPreferences().getBoolean(SP_DOLOGIN, false)){
			finish();
		}else{
			Intent intent = new Intent("clearStackActivity");
			intent.setType("text/plain");
			sendBroadcast(intent);
			moveTaskToBack(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		simpleFacebook.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to login using facebook user.
	 * @param profile facebook user profile object
	 */
	private void doFacebookLogin(final Profile profile) {

		final SeekBar progressBar = IjoomerUtilities.getLoadingDialog("Connecting through facebook...");
		IjoomerOauth.getInstance(IjoomerLoginActivity.this).authenticateUserWithFacebook(profile, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				progressBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 703) {
					showUserSelectionDialog(profile);
				} else if (responseCode == 200) {
					try {
						if (getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, "").length() > 0) {

							try {
								Intent intent = new Intent();
								intent.setClassName(IjoomerLoginActivity.this, getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, ""));
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("IN_OBJ",getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT,""));
								getSmartApplication().writeSharedPreferences(SP_LAST_ACTIVITY, "");
								startActivity(intent);
								getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
								getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);
							} catch (Exception e) {
								try {
									loadNew(IjoomerHomeActivity.class, IjoomerLoginActivity.this, true, "IN_USERID", "0");
								} catch (Throwable e1) {
									e1.printStackTrace();
								}
							} finally {
								finish();
							}

						} else if(getSmartApplication().readSharedPreferences().getBoolean(SP_DOLOGIN, false)){
							getSmartApplication().writeSharedPreferences(SP_DOLOGIN, false);
							getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, true);
							getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, true);
							finish();
						} else {
							if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
								try {
									Intent intent = new Intent();
									intent.setClassName(IjoomerLoginActivity.this, getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
									intent.putExtra("IN_OBJ",getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT,""));
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
								} catch (Exception e) {
									try {
										loadNew(IjoomerHomeActivity.class, IjoomerLoginActivity.this, true, "IN_USERID", "0");
									} catch (Throwable e1) {
										e1.printStackTrace();
									}
								} finally {
									finish();
								}
							}else{
								try {
									loadNew(IjoomerHomeActivity.class, IjoomerLoginActivity.this, true, "IN_USERID", "0");
								} catch (Throwable e1) {
									e1.printStackTrace();
								}
							}
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

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem
	 */
	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getCustomOkDialog(getString(R.string.login), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {
			}
		});

	}

	private void getProfile(){
		PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
		pictureAttributes.setHeight(100);
		pictureAttributes.setWidth(100);
		pictureAttributes.setType(PictureAttributes.PictureType.SQUARE);

		Properties properties = new Properties.Builder()
		.add(Properties.ID)
		.add(Properties.NAME)
		.add(Properties.USER_NAME)
		.add(Properties.EMAIL)
		.add(Properties.PICTURE, pictureAttributes)
		.build();
		simpleFacebook.getProfile(properties,new OnProfileRequestListener());
	}

	class OnLoginListener implements SimpleFacebook.OnLoginListener{


		@Override
		public void onLogin() {
			getProfile();
		}

		@Override
		public void onNotAcceptingPermissions() {
		}

		@Override
		public void onThinking() {
		}

		@Override
		public void onException(Throwable throwable) {
		}

		@Override
		public void onFail(String reason) {
			IjoomerUtilities.getCustomOkDialog(getString(R.string.friend),reason, getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

				@Override
				public void NeutralMethod() {
				}
			});
		}
	}

	class OnProfileRequestListener implements SimpleFacebook.OnProfileRequestListener{


		@Override
		public void onThinking() {
			showProgressDialog("Getting Facebook Profile...",IjoomerLoginActivity.this,true);
		}

		@Override
		public void onException(Throwable throwable) {
			hideProgressDialog();
		}

		@Override
		public void onFail(String reason) {
			hideProgressDialog();
			IjoomerUtilities.getCustomOkDialog(getString(R.string.friend),reason, getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

				@Override
				public void NeutralMethod() {
				}
			});
		}

		@Override
		public void onComplete(Profile profile) {
			hideProgressDialog();
			doFacebookLogin(profile);
		}
	}

}