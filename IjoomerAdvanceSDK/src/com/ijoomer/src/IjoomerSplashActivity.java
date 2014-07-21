package com.ijoomer.src;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerSplashMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartApplication;

/**
 * This Class Contains All Method Related To IjoomerSplashActivity.
 * 
 * @author tasol
 * 
 */
public class IjoomerSplashActivity extends IjoomerSplashMaster {

	private IjoomerGlobalConfiguration globalConfiguration;
	private LinearLayout lnrSync;

	private SeekBar skProgress;
	private IjoomerTextView txtPercent;
	private LinearLayout lnrProgress;

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

		txtPercent = (IjoomerTextView) findViewById(R.id.txtPercent);
		skProgress = (SeekBar) findViewById(R.id.skProgress);
		lnrProgress = (LinearLayout) findViewById(R.id.lnrProgress);

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
						if(IjoomerGlobalConfiguration.isEnableJReview()){
							if (getLastRequestTime().length() <= 0) {
								lnrProgress.setVisibility(View.VISIBLE);
								skProgress.setMax(100);
								skProgress.setProgress(2);
								txtPercent.setText("2 %");
							}
							authentication();
						}else{
							System.out.println("DATA"+!getSmartApplication().readSharedPreferences().getBoolean(SP_ICON_PRELOADER, false));
							if (!getSmartApplication().readSharedPreferences().getBoolean(SP_ICON_PRELOADER, false)) {
								System.out.println("LOADING......");
								lnrSync.setVisibility(View.VISIBLE);
							}
							authentication();
						}
					} else {
						showUrlSettingDialog();
					}
				} else {
					if(IjoomerGlobalConfiguration.isEnableJReview()){
						if (getLastRequestTime().length() <= 0) {
							lnrProgress.setVisibility(View.VISIBLE);
							skProgress.setMax(100);
							skProgress.setProgress(2);
							txtPercent.setText("2 %");
						}
						authentication();
					}else{
						System.out.println("DATA"+getSmartApplication().readSharedPreferences().getBoolean(SP_ICON_PRELOADER, false));
						if (!getSmartApplication().readSharedPreferences().getBoolean(SP_ICON_PRELOADER, false)) {
							System.out.println("LOADING......");
							lnrSync.setVisibility(View.VISIBLE);
						}
						authentication();
					}
				}
			}
		});
	}

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
													InitializeApplication();
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
							if(IjoomerGlobalConfiguration.isEnableJReview()){
								getJreviewContent(true);
							}
						}
					} else {
						InitializeApplication();
					}
				} else if (responseCode == 599
						&& getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
					InitializeApplication();
				} else {
					responseMessageHandler(responseCode, true);
				}
			}
		});
	}

	private void InitializeApplication(){
		if(IjoomerGlobalConfiguration.isEnableJReview()){
			getJreviewContent(false);
		} else {
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
		}
	}

	private void getJreviewContent(final boolean isloginRequired) {
		if (getLastRequestTime().length() <= 0) {
			globalConfiguration.async_post("1", getSmartApplication().readSharedPreferences().getString(SP_LAST_REQUEST_DATE, ""), new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {
					skProgress.setProgress(progressCount);
					txtPercent.setText(progressCount + " %");
				}

				@Override
				public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					if (responseCode == 599) {
						finish();
					} else {
						setLastRequestTime();
						if(isloginRequired){
							loadNew(IjoomerLoginActivity.class, IjoomerSplashActivity.this,true);
						} else {
							try {
								Intent intent = new Intent();
								intent.setClassName(IjoomerSplashActivity.this, getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("IN_OBJ", getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT, ""));
								startActivity(intent);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								finish();
							}
						}
					}
				}
			});
		} else {
			// used to get updated data
			getUpdatedData();

			try {
				Intent intent = new Intent();
				intent.setClassName(IjoomerSplashActivity.this, getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("IN_OBJ", getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY_INTENT, ""));
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				finish();
			}
		}

		// set reload pages flag
		getSmartApplication().writeSharedPreferences(SP_RELOADARTICLES, false);
		getSmartApplication().writeSharedPreferences(SP_RELOADFAVUORITEARTICLES, false);
		getSmartApplication().writeSharedPreferences(SP_RELOADARTICLEDETAILS, false);
		getSmartApplication().writeSharedPreferences(SP_RELOADREVIEWS, false);
	}

	private void getUpdatedData() {
		globalConfiguration.getData("0", getSmartApplication().readSharedPreferences().getString(SP_LAST_REQUEST_DATE, ""), new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					try {
						if (!globalConfiguration.isCalling() && globalConfiguration.hasNextPage()) {
							getUpdatedData();
						} else {
							setLastRequestTime();
						}
					} catch (Exception e) {
						setLastRequestTime();
					}
				}
			}
		});
	}

	private void setLastRequestTime() {
		SmartApplication.REF_SMART_APPLICATION.writeSharedPreferences(IjoomerSharedPreferences.SP_LAST_REQUEST_DATE, String.valueOf((Calendar.getInstance().getTimeInMillis() / 1000)));
	}

	private String getLastRequestTime() {
		return getSmartApplication().readSharedPreferences().getString(SP_LAST_REQUEST_DATE, "");
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