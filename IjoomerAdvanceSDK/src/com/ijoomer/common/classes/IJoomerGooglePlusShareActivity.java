package com.ijoomer.common.classes;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.Builder;
import com.google.android.gms.plus.PlusShare;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.ijoomer.theme.ThemeManager;

public class IJoomerGooglePlusShareActivity extends IjoomerSuperMaster implements ConnectionCallbacks, OnConnectionFailedListener {
	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	private static final int REQUEST_CODE_SHARE = 1000;
	private PlusClient mPlusClient;
	private PlusClient.Builder mPlusClientBuilder;
	private PlusShare.Builder mPlusShareBuilder;
	private IjoomerButton btnSignIn;
	private IjoomerButton btnShare;
	private IjoomerTextView textUserName;
	private IjoomerTextView txtLoginAs;
	private String IN_SHARE_LINK;
	private String IN_SHARE_MESSAGE;

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent data) {
		super.onActivityResult(requestCode, responseCode, data);
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
			mPlusClient.disconnect();
			mPlusClient.connect();
		} else if (requestCode == REQUEST_CODE_SHARE && responseCode == RESULT_OK) {
			finish();
		}

	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
	}

	@Override
	public int setLayoutId() {
		return ThemeManager.getInstance().getGoogleplus();
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public int setHeaderLayoutId() {
		return 0;
	}

	@Override
	public int setFooterLayoutId() {
		return 0;
	}

	@Override
	public void initComponents() {

		mPlusClientBuilder = new Builder(this, this, this);
		mPlusClientBuilder.setScopes(Scopes.PLUS_LOGIN, Scopes.PROFILE);
		mPlusClient = mPlusClientBuilder.build();

		// mPlusClient = new PlusClient(this, this, this, Scopes.PLUS_PROFILE);

		btnSignIn = (IjoomerButton) findViewById(R.id.btnSignIn);
		btnShare = (IjoomerButton) findViewById(R.id.btnShare);
		textUserName = (IjoomerTextView) findViewById(R.id.txtUserName);
		txtLoginAs = (IjoomerTextView) findViewById(R.id.txtLoginAs);
		txtLoginAs.setVisibility(View.GONE);
		getIntentData();
	}

	@Override
	public void prepareViews() {
		/*
		 * check google plus application available or not in device
		 */
		mPlusClient.connect();
	}

	@Override
	public void setActionListeners() {

		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!mPlusClient.isConnected() && btnSignIn.getText().equals(getString(R.string.signin))) {
					mPlusClient.connect();
				} else if (mPlusClient.isConnected() && btnSignIn.getText().equals(getString(R.string.signout))) {
					mPlusClient.clearDefaultAccount();
					mPlusClient.disconnect();
					btnSignIn.setText(getString(R.string.signin));
					textUserName.setText("");
					txtLoginAs.setVisibility(View.GONE);
					
					

				}
			}
		});

		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPlusClient.isConnected()) {

					mPlusShareBuilder = new PlusShare.Builder(getApplicationContext());
					mPlusShareBuilder.setType("text/plain");
					mPlusShareBuilder.setText(IN_SHARE_MESSAGE + IN_SHARE_LINK);
					mPlusShareBuilder.setContentUrl(Uri.parse(IN_SHARE_LINK));

					// Intent shareIntent =
					// PlusShare.Builder.from(IJoomerGooglePlusShareActivity.this).setText().setType("text/plain").setContent(Uri.parse("http://example.com/cheesecake/lemon"))
					// .getIntent();
					Intent shareIntent = mPlusShareBuilder.getIntent();
					startActivityForResult(shareIntent, REQUEST_CODE_SHARE);
				} else {
					ting(getString(R.string.signin_with_google_account));
				}
			}
		});
	}

	@Override
	public String[] setTabItemNames() {
		return null;
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
	public int[] setTabItemOnDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return null;
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection dialog.
			try {
				result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
			} catch (SendIntentException e) {
				mPlusClient.disconnect();
				mPlusClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		String accountName = mPlusClient.getAccountName();
		btnSignIn.setText(getString(R.string.signout));
		textUserName.setText(accountName);
		txtLoginAs.setVisibility(View.VISIBLE);
	}

	private void getIntentData() {
		IN_SHARE_LINK = getIntent().getStringExtra("IN_SHARE_LINK") != null ? getIntent().getStringExtra("IN_SHARE_LINK") : getString(R.string.site_url);
		IN_SHARE_MESSAGE = getIntent().getStringExtra("IN_SHARE_MESSAGE") != null ? getIntent().getStringExtra("IN_SHARE_MESSAGE") : "";
	}

}