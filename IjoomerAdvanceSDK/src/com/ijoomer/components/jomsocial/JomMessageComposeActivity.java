package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.library.jomsocial.JomMessageDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To JomMessageComposeActivity.
 * 
 * @author tasol
 * 
 */
public class JomMessageComposeActivity extends JomMasterActivity {

	private IjoomerEditText editAddFriend;
	private IjoomerEditText editSubject;

	private JomMessageDataProvider provider;

	private String IN_USERID;
	private String IN_USERNAME;
	private final int GET_FRIEND=1;
	private final String SELECTEDFRIEND="selectedFriend";
	private final String SELECTEDFRIENDIDS="selectedFriendIds";

	private IjoomerVoiceAndTextMessager voiceMessager;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_message_compose;
	}

	@Override
	public void initComponents() {
		getIntentData();

		provider = new JomMessageDataProvider(this);

		voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
		editAddFriend = (IjoomerEditText) findViewById(R.id.editAddFriend);
		editSubject = (IjoomerEditText) findViewById(R.id.editSubject);
	}

	@Override
	public void prepareViews() {

		if (!IN_USERID.equals("0")) {
			editAddFriend.setClickable(false);
			editAddFriend.setTag(IN_USERID);
			editAddFriend.setEnabled(false);
			editAddFriend.setText(IN_USERNAME);
		}

	}

	@Override
	public void setActionListeners() {

		voiceMessager.setMessageHandler(new MessageHandler() {

			@Override
			public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
				hideSoftKeyboard();
				boolean validationFlag = true;

				if (editAddFriend.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					editAddFriend.setError(getString(R.string.validation_value_required));
				}
				if (editSubject.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					editSubject.setError(getString(R.string.validation_value_required));
				}

				if (validationFlag) {

					provider.sendMessage(editAddFriend.getTag().toString().trim(), message, editSubject.getText().toString().trim(), voiceMessagePath, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IjoomerUtilities.getCustomOkDialog(getString(R.string.message), getString(R.string.message_send_successfully), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {
										finish();
									}
								});
							} else {
								responseErrorMessageHandler(responseCode, true);
							}
						}
					});
				}
			}

			@Override
			public void onButtonSend(String message) {
				hideSoftKeyboard();
				boolean validationFlag = true;

				if (editAddFriend.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					editAddFriend.setError(getString(R.string.validation_value_required));
				}
				if (editSubject.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					editSubject.setError(getString(R.string.validation_value_required));
				}

				if (validationFlag) {

					provider.sendMessage(editAddFriend.getTag().toString().trim(), message, editSubject.getText().toString().trim(), null, new WebCallListener() {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							if (responseCode == 200) {
								updateHeader(provider.getNotificationData());
								IjoomerApplicationConfiguration.setReloadRequired(true);
								IjoomerUtilities.getCustomOkDialog(getString(R.string.message), getString(R.string.message_send_successfully), getString(R.string.ok),
										R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

									@Override
									public void NeutralMethod() {
										finish();
									}
								});
							} else {
								responseErrorMessageHandler(responseCode, true);
							}
						}
					});
				}
			}

			@Override
			public void onToggle(int messager) {

			}
		});

		editAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String selectedFriend = editAddFriend.getTag() != null ? editAddFriend.getTag().toString():"";
				try {
					loadNewResult(JomFriendSearchActivity.class,JomMessageComposeActivity.this,GET_FRIEND,"IN_TYPE",FRIENDS,"IN_SELECTEDFRIENDIDS",selectedFriend,"IN_ISMULTIPLEALLOW",true);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(requestCode== GET_FRIEND){
				editAddFriend.setText(data.getStringExtra(SELECTEDFRIEND));
				editAddFriend.setTag(data.getStringExtra(SELECTEDFRIENDIDS));
			}
		}
	}

	/**
	 * Class methods
	 */


	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_USERID = getIntent().getStringExtra("IN_USERID") == null ? "0" : getIntent().getStringExtra("IN_USERID");
		IN_USERNAME = getIntent().getStringExtra("IN_USERNAME") == null ? "0" : getIntent().getStringExtra("IN_USERNAME");
	}

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.message), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
	}

}
