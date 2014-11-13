package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import com.ijoomer.common.classes.IjoomerSplashMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To IjoomerSplashActivity.
 * 
 * @author tasol
 * 
 */
public class IjoomerSplashActivity extends IjoomerSplashMaster {

	private IjoomerGlobalConfiguration globalConfiguration;
	private LinearLayout lnrSync;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		IjoomerApplicationConfiguration.setDefaultConfiguration(this);
		return R.layout.ijoomer_splash;
	}

	@Override
	public void initComponents() {
		globalConfiguration = new IjoomerGlobalConfiguration(IjoomerSplashActivity.this);

		lnrSync = (LinearLayout) findViewById(R.id.lnrSync);

		loadComponents();
	}

	@Override
	public void prepareViews() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			tong("External SD card not mounted");
		}
	}

	@Override
	public void setActionListeners() {
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	/**
	 * Class methods
	 */

	/**
	 * This method used to get global configuration data.
	 */

	private void loadComponents() {
		globalConfiguration.getComponents(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage,
					ArrayList<HashMap<String, String>> data1, Object data2) {
				if (Boolean.parseBoolean(getString(R.string.show_url_set_dialog))) {
					if (getSmartApplication().readSharedPreferences().getBoolean(SP_URL_SETTING, false)) {
						if (!getSmartApplication().readSharedPreferences().getBoolean(SP_ICON_PRELOADER, false)) {
							lnrSync.setVisibility(View.VISIBLE);
						}
						authentication();
					} else {
						showUrlSettingDialog();
					}
				} else {
					if (!getSmartApplication().readSharedPreferences().getBoolean(SP_ICON_PRELOADER, false)) {
						lnrSync.setVisibility(View.VISIBLE);
					}
					authentication();
				}
			}
		});
	}

	/**
	 * This method used to get global configuration data.
	 */
	private void authentication() {
		globalConfiguration.loadGlobalConfiguration(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					if (IjoomerGlobalConfiguration.isLoginRequired() || IjoomerUtilities.getLoginParams() != null
							&& IjoomerUtilities.getLoginParams().toString().length() > 0) {
						if (!getSmartApplication().readSharedPreferences().getBoolean(SP_ISLOGOUT, true)) {
							IjoomerOauth.getInstance(IjoomerSplashActivity.this).autoLogin(IjoomerUtilities.getLoginParams().toString(),
									new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage,
										ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										globalConfiguration.loadGlobalConfiguration(new WebCallListener() {

											@Override
											public void onProgressUpdate(int progressCount) {
											}

											@Override
											public void onCallComplete(int responseCode, String errorMessage,
													ArrayList<HashMap<String, String>> data1, Object data2) {
												if (responseCode == 200) {
													try {
														if (getSmartApplication().readSharedPreferences()
																.getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
															try {
																Intent intent = new Intent();
																intent.setClassName(
																		IjoomerSplashActivity.this,
																		getSmartApplication().readSharedPreferences().getString(
																				SP_DEFAULT_LANDING_SCREEN, ""));
																intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
																intent.putExtra(
																		"IN_OBJ",
																		getSmartApplication().readSharedPreferences().getString(
																				SP_LAST_ACTIVITY_INTENT, ""));
																startActivity(intent);
															} catch (Exception e) {
																try {
																	loadNew(IjoomerHomeActivity.class, IjoomerSplashActivity.this,
																			true, "IN_USERID", "0");
																} catch (Throwable e1) {
																	e1.printStackTrace();
																}
															} finally {
																finish();
															}
														} else {
															try {
																loadNew(IjoomerHomeActivity.class, IjoomerSplashActivity.this,
																		true, "IN_USERID", "0");
															} catch (Throwable e1) {
																e1.printStackTrace();
															}
														}
													} catch (Throwable e) {
														e.printStackTrace();
													}
												} else {
													responseMessageHandler(responseCode, true);
												}
											}
										});

									} else {
										IjoomerUtilities.getCustomOkDialog(
												getString(R.string.dialog_loading_profile),
												getString(getResources().getIdentifier("code" + responseCode, "string",
														getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
														new CustomAlertNeutral() {

													@Override
													public void NeutralMethod() {
														loadNew(IjoomerLoginActivity.class, IjoomerSplashActivity.this, true);
													}
												});
									}
								}
							});
						} else {
							loadNew(IjoomerLoginActivity.class, IjoomerSplashActivity.this, true);
						}
					} else {
						if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
							try {
								Intent intent = new Intent();
								intent.setClassName(IjoomerSplashActivity.this,
										getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("IN_OBJ",
										getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT, ""));
								startActivity(intent);
							} catch (Exception e) {
								try {
									loadNew(IjoomerHomeActivity.class, IjoomerSplashActivity.this, true, "IN_USERID", "0");
								} catch (Throwable e1) {
									e1.printStackTrace();
								}
							} finally {
								finish();
							}
							finish();
						} else {
							try {
								loadNew(IjoomerHomeActivity.class, IjoomerSplashActivity.this, true, "IN_USERID", "0");
							} catch (Throwable e1) {
								e1.printStackTrace();
							}
						}
					}

				} else if (responseCode == 599
						&& getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
					try {
						Intent intent = new Intent();
						intent.setClassName(IjoomerSplashActivity.this,
								getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("IN_OBJ", getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT, ""));
						startActivity(intent);
					} catch (Exception e) {
						try {
							loadNew(IjoomerHomeActivity.class, IjoomerSplashActivity.this, true, "IN_USERID", "0");
						} catch (Throwable e1) {
							e1.printStackTrace();
						}
					} finally {
						finish();
					}

					finish();

				} else {
					responseMessageHandler(responseCode, true);
				}
			}
		});
	}

	/**
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.splash),
				getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {
				if (responseCode == 599 && finishActivityOnConnectionProblem) {
					finish();
				}
			}
		});
	}

}