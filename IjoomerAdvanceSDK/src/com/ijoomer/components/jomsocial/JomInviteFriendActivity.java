package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.library.jomsocial.JomEventDataProvider;
import com.ijoomer.library.jomsocial.JomGroupDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To JomInviteFriendActivity.
 * 
 * @author tasol
 * 
 */
public class JomInviteFriendActivity extends JomMasterActivity {

	private IjoomerEditText edtAddFriend;
	private IjoomerEditText edtMessage;
	private IjoomerButton btnCancel;
	private IjoomerButton btnInvite;

	private JomGroupDataProvider groupProvider;
	private JomEventDataProvider eventProvider;

	private final int GET_FRIEND=1;
	private final String SELECTEDFRIEND="selectedFriend";
	private final String SELECTEDFRIENDIDS="selectedFriendIds";
	private String IN_GROUP_ID;
	private String IN_EVENT_ID;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_invite_friend;
	}

	@Override
	public void initComponents() {
		getIntentData();

		groupProvider = new JomGroupDataProvider(this);
		eventProvider = new JomEventDataProvider(this);

		edtAddFriend = (IjoomerEditText) findViewById(R.id.edtAddFriend);
		edtMessage = (IjoomerEditText) findViewById(R.id.edtMessage);
		btnInvite = (IjoomerButton) findViewById(R.id.btnInvite);
		btnCancel = (IjoomerButton) findViewById(R.id.btnCancel);
	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {

		btnInvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				hideSoftKeyboard();
				boolean validationFlag = true;

				if (edtAddFriend.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtAddFriend.setError(getString(R.string.validation_value_required));
				}

				if (validationFlag) {
					if (!IN_GROUP_ID.equals("0")) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						groupProvider.inviteFriend(IN_GROUP_ID, edtAddFriend.getTag().toString().trim(), edtMessage.getText().toString().trim().length() > 0 ? edtMessage.getText().toString().trim()
								: null, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

								if (responseCode == 200) {
									updateHeader(groupProvider.getNotificationData());
									finish();
								} else {
									responseErrorMessageHandler(responseCode, true);
								}

							}
						});
					} else if (!IN_EVENT_ID.equals("0")) {
						final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						eventProvider.inviteFriend(IN_EVENT_ID, edtAddFriend.getTag().toString().trim(), edtMessage.getText().toString().trim().length() > 0 ? edtMessage.getText().toString().trim()
								: null, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

								if (responseCode == 200) {
									updateHeader(eventProvider.getNotificationData());
									finish();
								} else {
									responseErrorMessageHandler(responseCode, true);
								}

							}
						});

					}
				} else {
					edtAddFriend.setError(getString(R.string.validation_value_required));
				}
			}
		});
		edtAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String selectedFriend = edtAddFriend.getTag() != null ? edtAddFriend.getTag().toString():"";
				if(!IN_EVENT_ID.equals("0")){
					try {
						loadNewResult(JomFriendSearchActivity.class,JomInviteFriendActivity.this,GET_FRIEND,"IN_TYPE",EVENT,"IN_SELECTEDFRIENDIDS",selectedFriend,"IN_ISMULTIPLEALLOW",true,"IN_EVENT_ID",IN_EVENT_ID);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}else{
					try {
						loadNewResult(JomFriendSearchActivity.class,JomInviteFriendActivity.this,GET_FRIEND,"IN_TYPE",GROUP,"IN_SELECTEDFRIENDIDS",selectedFriend,"IN_ISMULTIPLEALLOW",true,"IN_GROUP_ID",IN_GROUP_ID);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(requestCode== GET_FRIEND){
				edtAddFriend.setText(data.getStringExtra(SELECTEDFRIEND));
				edtAddFriend.setTag(data.getStringExtra(SELECTEDFRIENDIDS));
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
		IN_GROUP_ID = getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ID");
		IN_EVENT_ID = getIntent().getStringExtra("IN_EVENT_ID") == null ? "0" : getIntent().getStringExtra("IN_EVENT_ID");
	}

	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.invite_friends), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {

			}
		});
	}
}
