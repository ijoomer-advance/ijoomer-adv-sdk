package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.view.View;

import com.ijoomer.common.classes.IjoomerSplashMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public class IjoomerSplashActivity extends IjoomerSplashMaster {

	IjoomerGlobalConfiguration globalConfiguration;

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_splash;
	}

	@Override
	public void initComponents() {

		IjoomerApplicationConfiguration.setDefaultConfiguration();
		globalConfiguration = new IjoomerGlobalConfiguration(IjoomerSplashActivity.this);

		if (getSmartApplication().readSharedPreferences().getBoolean(SP_URL_SETTING, false)) {
			authentication();
		} else {
			showUrlSettingDialog();
		}
	}

	@Override
	public void prepareViews() {
	}

	@Override
	public void setActionListeners() {
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	/**
	 * Class method
	 */

	private void authentication() {
		globalConfiguration.loadGlobalConfiguration(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					if (IjoomerGlobalConfiguration.isLoginRequired() || getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, "").length() > 0) {
						if (!getSmartApplication().readSharedPreferences().getBoolean(SP_ISLOGOUT, true)) {
							IjoomerOauth.getInstance(IjoomerSplashActivity.this).autoLogin(getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, ""),
									new WebCallListener() {

										@Override
										public void onProgressUpdate(int progressCount) {
										}

										@Override
										public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
											if (responseCode == 200) {
												try {
													if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
														Intent intent = new Intent();
														intent.setClassName(IjoomerSplashActivity.this,
																getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
														intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
														startActivity(intent);
														finish();
													} else {

													}
													// loadNew(JomActivitiesActivity.class,
													// IjoomerSplashActivity.this,
													// true, "IN_USERID", "0");
												} catch (Throwable e) {
													e.printStackTrace();
												}
											} else {
												IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_loading_profile),
														getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
														R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

															@Override
															public void NeutralMathod() {

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
							Intent intent = new Intent();
							intent.setClassName(IjoomerSplashActivity.this, getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
						} else {

						}
						// loadNew(IjoomerHomeActivity.class,
						// IjoomerSplashActivity.this, true);
					}

				} else {
					responseMessageHandler(responseCode, true);
				}
			}
		});
	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.splash), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
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