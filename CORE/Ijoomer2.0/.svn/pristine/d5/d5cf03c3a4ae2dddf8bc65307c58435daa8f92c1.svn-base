package com.ijoomer.common.classes;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.TwitterOAuthView;
import com.ijoomer.customviews.TwitterOAuthView.Listener;
import com.ijoomer.customviews.TwitterOAuthView.Result;
import com.ijoomer.src.R;
import com.smart.framework.CustomAlertNeutral;

public class IJoomerTwitterShareActivity extends IjoomerSuperMaster {

	private LinearLayout lnrTwit;
	private ProgressBar pbrTwitterShare;
	private IjoomerButton btnClose;

	private AQuery androidQuery;
	private TwitterOAuthView webTwitter;
	private File statusImage;

	private static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	private static final String OAUTH_CALLBACK_HOST = "callback";
	private static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
	private String IN_TWIT_MESSAGE = "";
	private String IN_TWIT_IMAGE = "";

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_twitter_share;
	}

	@Override
	public void initComponents() {

		lnrTwit = (LinearLayout) findViewById(R.id.lnrTwit);
		pbrTwitterShare = (ProgressBar) findViewById(R.id.pbrTwitterShare);
		webTwitter = (TwitterOAuthView) findViewById(R.id.webTwitter);
		btnClose = (IjoomerButton) findViewById(R.id.btnClose);

		androidQuery = new AQuery(this);

		getIntentData();
	}

	@Override
	public void prepareViews() {
		if (IN_TWIT_MESSAGE.length() > 0) {
			if (isAuthenticated()) {
				webTwitter.setVisibility(View.GONE);
				lnrTwit.setVisibility(View.VISIBLE);
				if (IN_TWIT_IMAGE.length() > 0) {
					androidQuery.download(IN_TWIT_IMAGE, new File("/sdcard/img.png"), new AjaxCallback<File>() {
						@Override
						public void callback(String url, File file, AjaxStatus status) {
							super.callback(url, file, status);
							if (status.getCode() == 200) {
								statusImage = file;
							}
							sendTweet(IN_TWIT_MESSAGE);
						}
					});
				} else {
					sendTweet(IN_TWIT_MESSAGE);
				}

			} else {
				webTwitter.setVisibility(View.VISIBLE);
				lnrTwit.setVisibility(View.GONE);

				webTwitter.setWebChromeClient(new WebChromeClient() {
					SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.twitter_authorisation));

					@Override
					public void onProgressChanged(WebView view, int newProgress) {
						if (proSeekBar == null) {
							proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.twitter_authorisation));
						}
						if (newProgress >= 100) {
							proSeekBar.setProgress(newProgress);
							proSeekBar = null;
						} else {
							proSeekBar.setProgress(newProgress);
						}
					}
				});
				webTwitter.start(IjoomerApplicationConfiguration.twitterConsumerKey, IjoomerApplicationConfiguration.twitterSecretKey, OAUTH_CALLBACK_URL, true, new Listener() {

					@Override
					public void onSuccess(TwitterOAuthView view, AccessToken accessToken) {
						getSmartApplication().writeSharedPreferences(SP_TWITTER_TOKEN, accessToken.getToken());
						getSmartApplication().writeSharedPreferences(SP_TWITTER_SECRET_TOKEN, accessToken.getTokenSecret());
						webTwitter.setVisibility(View.GONE);
						lnrTwit.setVisibility(View.VISIBLE);
						if (IN_TWIT_IMAGE.length() > 0) {
							androidQuery.download(IN_TWIT_IMAGE, new File("/sdcard/img.png"), new AjaxCallback<File>() {
								@Override
								public void callback(String url, File file, AjaxStatus status) {
									super.callback(url, file, status);
									if (status.getCode() == 200) {
										statusImage = file;
									}
									sendTweet(IN_TWIT_MESSAGE);
								}
							});
						} else {
							sendTweet(IN_TWIT_MESSAGE);
						}
					}

					@Override
					public void onFailure(TwitterOAuthView view, Result result) {
						System.out.println("Result : " + result);
					}
				});
			}
		} else {
			IjoomerUtilities.getCustomOkDialog(getString(R.string.alert_title_twitter), getString(R.string.validation_value_required), getString(R.string.ok),
					R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

						@Override
						public void NeutralMathod() {
							finish();
						}
					});
		}
	}

	@Override
	public void setActionListeners() {
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				finish();
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
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

	/**
	 * Class method
	 */
	private void getIntentData() {
		IN_TWIT_MESSAGE = getIntent().getStringExtra("IN_TWIT_MESSAGE") == null ? "" : getIntent().getStringExtra("IN_TWIT_MESSAGE");
		IN_TWIT_IMAGE = getIntent().getStringExtra("IN_TWIT_IMAGE") == null ? "" : getIntent().getStringExtra("IN_TWIT_IMAGE");
		if (IN_TWIT_MESSAGE.length() > 140) {
			IN_TWIT_MESSAGE = IN_TWIT_MESSAGE.substring(0, 139);
		}
	}

	private boolean isAuthenticated() {

		if (getSmartApplication().readSharedPreferences().getString(SP_TWITTER_TOKEN, "").length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void sendTweet(final String twitMessage) {

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pbrTwitterShare.setVisibility(View.VISIBLE);
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {

					AccessToken a = new AccessToken(getSmartApplication().readSharedPreferences().getString(SP_TWITTER_TOKEN, ""), getSmartApplication().readSharedPreferences()
							.getString(SP_TWITTER_SECRET_TOKEN, ""));
					final Twitter twitter = new TwitterFactory().getInstance();
					twitter.setOAuthConsumer(IjoomerApplicationConfiguration.getTwitterConsumerKey(), IjoomerApplicationConfiguration.getTwitterSecretKey());
					twitter.setOAuthAccessToken(a);
					
					StatusUpdate statusUpdate = new StatusUpdate(twitMessage);
					if (statusImage != null) {
//						statusUpdate.setMedia(statusImage);
					}
					twitter.updateStatus(statusUpdate);
					return true;
				} catch (TwitterException e) {
					e.printStackTrace();
					IjoomerUtilities.getCustomOkDialog(getString(R.string.alert_title_twitter), e.getErrorMessage(), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
							new CustomAlertNeutral() {

								@Override
								public void NeutralMathod() {
									finish();
								}
							});
					return false;
				}

			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				pbrTwitterShare.setVisibility(View.GONE);
				if (result) {
					IjoomerUtilities.getCustomOkDialog(getString(R.string.alert_title_twitter), getString(R.string.alert_message_twitter_success), getString(R.string.ok),
							R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								@Override
								public void NeutralMathod() {
									finish();
								}
							});
				}
			}
		}.execute();
	}
}
