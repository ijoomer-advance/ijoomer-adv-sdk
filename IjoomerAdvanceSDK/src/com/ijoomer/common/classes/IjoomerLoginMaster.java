package com.ijoomer.common.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.oauth.IjoomerOauth;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

public abstract class IjoomerLoginMaster extends IjoomerSuperMaster {

	private Dialog dialog;

	public IjoomerLoginMaster() {
		super();
		setOptionMenu(R.menu.ijoomer_setting);
	}

	@Override
	public int setTabBarDividerResId() {
		return 0;
	}

	@Override
	public int setTabItemLayoutId() {
		return 0;
	}

	@Override
	public String[] setTabItemNames() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	public void showUserSelectionDialog(final JSONObject object) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.ijoomer_facebook_user);

		IjoomerTextView txtWelcome = (IjoomerTextView) dialog.findViewById(R.id.txtWelcome);
		IjoomerButton btnNewUser = (IjoomerButton) dialog.findViewById(R.id.btnNewUser);
		IjoomerButton btnMemberOfSite = (IjoomerButton) dialog.findViewById(R.id.btnMemberOfSite);
		try {
			txtWelcome.setText(Html.fromHtml(String.format(getString(R.string.facebook_welcome), object.getString("name"))));
		} catch (Throwable e) {
		}
		btnNewUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNewUserDialog(object);
			}
		});
		btnMemberOfSite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showExistingUserDialog(object);
			}
		});

		dialog.show();
	}

	public void showNewUserDialog(final JSONObject object) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.ijoomer_facebook_new_user);

		final IjoomerEditText edtUserName = (IjoomerEditText) dialog.findViewById(R.id.edtUserName);
		final IjoomerTextView txtName = (IjoomerTextView) dialog.findViewById(R.id.txtName);
		final IjoomerTextView txtEmail = (IjoomerTextView) dialog.findViewById(R.id.txtEmail);

		txtName.setMovementMethod(new ScrollingMovementMethod());
		txtEmail.setMovementMethod(new ScrollingMovementMethod());

		try {
			txtName.setText(object.getString("name"));
			edtUserName.setText(object.getString("username"));
			Drawable errorIcon = getResources().getDrawable(R.drawable.com_facebook_close);
			errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
			edtUserName.setError(getString(R.string.username), errorIcon);

			edtUserName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					edtUserName.setError(null, null);
				}
			});
			// edtUserName.setError(getString(R.string.username));
			txtEmail.setText(object.getString("email"));
		} catch (Throwable e) {
			e.printStackTrace();
		}

		IjoomerButton btnBack = (IjoomerButton) dialog.findViewById(R.id.btnBack);
		IjoomerButton btnCreate = (IjoomerButton) dialog.findViewById(R.id.btnCreate);
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edtUserName.getText().toString().trim().length() <= 0) {
					edtUserName.setError(getString(R.string.validation_value_required));
				} else {
					final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_connect_facebook));
					IjoomerOauth.getInstance(IjoomerLoginMaster.this).authenticateUserWithFacebook(object, edtUserName.getText().toString().trim(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							progressBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								try {
									if (getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, "").length() > 0) {
										Intent intent = new Intent();
										intent.setClassName(IjoomerLoginMaster.this, getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, ""));
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										getSmartApplication().writeSharedPreferences(SP_LAST_ACTIVITY, "");
										startActivity(intent);
										getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
										getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);
										finish();
									} else {
										if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
											Intent intent = new Intent();
											intent.setClassName(IjoomerLoginMaster.this, getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
											intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);
											finish();
										} else {

										}
										// loadNew(JomActivitiesActivity.class,
										// IjoomerLoginMaster.this, true,
										// "IN_USERID", "0");
									}
								} catch (Throwable e) {
									e.printStackTrace();
								}
								getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
								getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);
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
				}

			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	public void showExistingUserDialog(final JSONObject object) {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.ijoomer_facebook_existing_site_user);

		final IjoomerEditText edtUserName = (IjoomerEditText) dialog.findViewById(R.id.edtUserName);
		final IjoomerEditText edtPassword = (IjoomerEditText) dialog.findViewById(R.id.edtPassword);

		IjoomerButton btnBack = (IjoomerButton) dialog.findViewById(R.id.btnBack);
		IjoomerButton btnLogin = (IjoomerButton) dialog.findViewById(R.id.btnLogin);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean validationFlag = true;

				if (edtUserName.getText().toString().trim().length() <= 0) {
					edtUserName.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (edtPassword.getText().toString().trim().length() <= 0) {
					edtPassword.setError(getString(R.string.validation_value_required));
					validationFlag = false;
				}
				if (validationFlag) {

					final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_connect_facebook));
					IjoomerOauth.getInstance(IjoomerLoginMaster.this).authenticateUserWithFacebook(object, edtUserName.getText().toString().trim(),
							edtPassword.getText().toString().trim(), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
									progressBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										try {
											if (getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, "").length() > 0) {
												Intent intent = new Intent();
												intent.setClassName(IjoomerLoginMaster.this, getSmartApplication().readSharedPreferences().getString(SP_LAST_ACTIVITY, ""));
												intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												getSmartApplication().writeSharedPreferences(SP_LAST_ACTIVITY, "");
												startActivity(intent);
												getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
												getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);
												finish();
											} else {
												if (getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, "").length() > 0) {
													Intent intent = new Intent();
													intent.setClassName(IjoomerLoginMaster.this,
															getSmartApplication().readSharedPreferences().getString(SP_DEFAULT_LANDING_SCREEN, ""));
													intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
													startActivity(intent);
													finish();
												} else {

												}
												// loadNew(JomActivitiesActivity.class,
												// IjoomerLoginMaster.this,
												// true, "IN_USERID", "0");
											}
										} catch (Throwable e) {
											e.printStackTrace();
										}
										getSmartApplication().writeSharedPreferences(SP_ISLOGOUT, false);
										getSmartApplication().writeSharedPreferences(SP_ISFACEBOOKLOGIN, true);
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

				}

			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.ic_menu_setting) {
			showUrlSettingDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	public void showForgetPasswordDialog() {

		dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.ijoomer_forget_reset_password);
		final IjoomerTextView txtTitle = (IjoomerTextView) dialog.findViewById(R.id.txtTitle);
		final LinearLayout lntStep1 = (LinearLayout) dialog.findViewById(R.id.lntStep1);
		final IjoomerEditText edtEmail = (IjoomerEditText) dialog.findViewById(R.id.edtEmail);
		final LinearLayout lntStep2 = (LinearLayout) dialog.findViewById(R.id.lntStep2);
		final IjoomerEditText edtUserName = (IjoomerEditText) dialog.findViewById(R.id.edtUserName);
		final IjoomerEditText edtToken = (IjoomerEditText) dialog.findViewById(R.id.edtToken);
		final LinearLayout lntStep3 = (LinearLayout) dialog.findViewById(R.id.lntStep3);
		final IjoomerEditText edtPassword = (IjoomerEditText) dialog.findViewById(R.id.edtPassword);
		final IjoomerEditText edtRetypePassword = (IjoomerEditText) dialog.findViewById(R.id.edtRetypePassword);

		final IjoomerButton btnNext = (IjoomerButton) dialog.findViewById(R.id.btnNext);
		final IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);

		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				hideSoftKeyboard();
				boolean validationFlag = true;
				if (lntStep1.getVisibility() == View.VISIBLE) {
					if (edtEmail.getText().toString().trim().length() <= 0) {
						edtEmail.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					} else {
						if (IjoomerUtilities.emailValidator(edtEmail.getText().toString().trim())) {
							final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_verifying_emailid));
							IjoomerOauth.getInstance(IjoomerLoginMaster.this).forgetPasswordStep1(edtEmail.getText().toString().trim(), new WebCallListener() {

								@Override
								public void onProgressUpdate(int progressCount) {
									progressBar.setProgress(progressCount);
								}

								@Override
								public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									if (responseCode == 200) {
										lntStep1.setVisibility(View.GONE);
										lntStep2.setVisibility(View.VISIBLE);
										lntStep3.setVisibility(View.GONE);
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
							IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_loading_profile), getString(R.string.validation_invalid_email), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMathod() {

										}
									});
						}

					}
				} else if (lntStep2.getVisibility() == View.VISIBLE) {
					if (edtUserName.getText().toString().trim().length() <= 0) {
						edtUserName.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					}
					if (edtToken.getText().toString().trim().length() <= 0) {
						edtToken.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					}
					if (validationFlag) {
						final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_verifying_token));
						IjoomerOauth.getInstance(IjoomerLoginMaster.this).forgetPasswordStep2(edtUserName.getText().toString().trim(), edtToken.getText().toString().trim(),
								new WebCallListener() {

									@Override
									public void onProgressUpdate(int progressCount) {
										progressBar.setProgress(progressCount);
									}

									@Override
									public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
										if (responseCode == 200) {
											lntStep1.setVisibility(View.GONE);
											lntStep2.setVisibility(View.GONE);
											lntStep3.setVisibility(View.VISIBLE);
											btnNext.setText(getString(R.string.resetpassword_finish));
											txtTitle.setText(getString(R.string.resetpassword_step3));
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

					}

				} else {
					if (edtPassword.getText().toString().trim().length() <= 0) {
						edtPassword.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					}
					if (edtRetypePassword.getText().toString().trim().length() <= 0) {
						edtRetypePassword.setError(getString(R.string.validation_value_required));
						validationFlag = false;
					}
					if (!edtRetypePassword.getText().toString().equals(edtPassword.getText().toString())) {
						edtRetypePassword.setError(getString(R.string.validation_passwod_not_match));
						validationFlag = false;
					}
					if (validationFlag) {

						final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_resetiing_password));
						IjoomerOauth.getInstance(IjoomerLoginMaster.this).forgetPasswordStep3(edtPassword.getText().toString().trim(), new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								progressBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.alert_title_forget_password), getString(R.string.alert_message_forget_password),
											getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

												@Override
												public void NeutralMathod() {
													dialog.dismiss();
												}
											});
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

					}
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				dialog.dismiss();
			}
		});
		dialog.show();

	}

	public void showForgetUserNameDialog() {
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.ijoomer_forget_username_dialog);
		final IjoomerEditText edtEmail = (IjoomerEditText) dialog.findViewById(R.id.edtEmail);
		IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);
		IjoomerButton btnSend = (IjoomerButton) dialog.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				hideSoftKeyboard();
				if (edtEmail.getText().toString().trim().length() <= 0) {
					edtEmail.setError(getString(R.string.validation_value_required));
				} else {
					final SeekBar progressBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_verifying_emailid));
					IjoomerOauth.getInstance(IjoomerLoginMaster.this).forgetUserName(edtEmail.getText().toString().trim(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							progressBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_forget_username), getString(R.string.alert_check_email), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

											@Override
											public void NeutralMathod() {
												dialog.dismiss();
											}
										});
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
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

}
