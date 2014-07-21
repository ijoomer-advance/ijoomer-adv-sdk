package com.ijoomer.src;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.IjoomerKeys;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * Created by tasol on 22/7/13.
 */
public class IjoomerPushNotificationLuncherActivity extends IjoomerSuperMaster implements IjoomerKeys {

	private LinearLayout lnrPbr;
	IjoomerGlobalConfiguration globalConfiguration;
	private String IN_PUSH_TYPE;
	private String IN_PUSH_ID;

	@Override
	public String[] setTabItemNames() {
		return new String[0];
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
		return new int[0];
	}

	@Override
	public int[] setTabItemOffDrawables() {
		return new int[0];
	}

	@Override
	public int[] setTabItemPressDrawables() {
		return new int[0];
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {

	}

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_push_luncher;
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
		lnrPbr = (LinearLayout) findViewById(R.id.lnrPbr);
		globalConfiguration = new IjoomerGlobalConfiguration(this);
		getIntentData();
	}

	@Override
	public void prepareViews() {

	}

	@Override
	public void setActionListeners() {

	}

	private void getIntentData() {
		IN_PUSH_TYPE = getIntent().getStringExtra("IN_PUSH_TYPE") != null ? getIntent().getStringExtra("IN_PUSH_TYPE") : "";
		IN_PUSH_ID = getIntent().getStringExtra("IN_PUSH_ID") != null ? getIntent().getStringExtra("IN_PUSH_ID") : "";

		getPushNotifiactionData();
	}

	private void getPushNotifiactionData() {

		lnrPbr.setVisibility(View.VISIBLE);

		globalConfiguration.getPushData(IN_PUSH_ID, new WebCallListener() {
			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				lnrPbr.setVisibility(View.GONE);
				Intent gotoIntent = null;
				if (responseCode == 200) {
					try {
						if (IN_PUSH_TYPE.equals("replaycomment")) {
							JSONObject contentData = ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data");
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomWallOrActivityDetailActivity");
							ArrayList<HashMap<String, String>> IN_WALL_DETAILS = new ArrayList<HashMap<String, String>>();
							HashMap<String, String> row = new HashMap<String, String>();
							row.put("content", contentData.getString("content"));
							row.putAll(new HashMap<String, String>(jsonToMap(contentData.getJSONObject("user_detail"))));
							contentData.remove("user_detail");
							row.putAll(new HashMap<String, String>(jsonToMap(contentData)));
							IN_WALL_DETAILS.add(row);
							gotoIntent.putExtra("IN_WALL_DETAILS_LIST_TYPE", "comments");
							gotoIntent.putExtra("IN_WALL_DETAILS", IN_WALL_DETAILS);
						}
						if (IN_PUSH_TYPE.equals("walllike")) {
							JSONObject contentData = ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data");
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomWallOrActivityDetailActivity");
							ArrayList<HashMap<String, String>> IN_WALL_DETAILS = new ArrayList<HashMap<String, String>>();
							HashMap<String, String> row = new HashMap<String, String>();
							row.put("content", contentData.getString("content"));
							row.putAll(new HashMap<String, String>(jsonToMap(contentData.getJSONObject("user_detail"))));
							contentData.remove("user_detail");
							row.putAll(new HashMap<String, String>(jsonToMap(contentData)));
							IN_WALL_DETAILS.add(row);
							gotoIntent.putExtra("IN_WALL_DETAILS_LIST_TYPE", "likes");
							gotoIntent.putExtra("IN_WALL_DETAILS", IN_WALL_DETAILS);
						} else if (IN_PUSH_TYPE.equals("profile")) {
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomProfileActivity");
							try {
								gotoIntent.putExtra("IN_USERID", ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data").has("id") ? ((JSONObject) data2)
										.getJSONObject("data").getJSONObject("content_data").getString("id") : "0");
							} catch (Exception e) {
								gotoIntent.putExtra("IN_USERID", "0");
							}
						} else if (IN_PUSH_TYPE.equals("friend")) {
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomFriendListActivity");
						} else if (IN_PUSH_TYPE.equals("message")) {
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomMessageActivity");
						} else if (IN_PUSH_TYPE.equals("group")) {
							JSONObject groupData = ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data");
							if (groupData.getString("type").equals("group")) {
								gotoIntent = new Intent();
								if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
									gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomGroupDetailsActivity_v30");
								} else {
									gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomGroupDetailsActivity");
								}
								gotoIntent.putExtra("IN_USERID", "0");
								gotoIntent.putExtra("IN_GROUP_DETAILS", new HashMap<String, String>(jsonToMap(groupData)));
							} else if (groupData.getString("type").equals("discussion")) {
								JSONObject discussionDetails = new JSONObject(groupData.getString("discussiondetail"));
								JSONObject groupDetails = new JSONObject(groupData.getString("groupdetail"));
								gotoIntent = new Intent();
								gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomGroupDiscussionDetailsActivity");
								gotoIntent.putExtra("IN_DISCUSSION_DETAILS", new HashMap<String, String>(jsonToMap(discussionDetails)));
								gotoIntent.putExtra("IN_GROUP_DETAILS", new HashMap<String, String>(jsonToMap(groupDetails)));
							} else if (groupData.getString("type").equals("announcement")) {
								JSONObject announcementDetails = new JSONObject(groupData.getString("announcementdetail"));
								JSONObject groupDetails = new JSONObject(groupData.getString("groupdetail"));
								gotoIntent = new Intent();
								gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomGroupAnnouncementDetailsActivity");
								gotoIntent.putExtra("IN_ANNOUCEMENT_DETAILS", new HashMap<String, String>(jsonToMap(announcementDetails)));
								gotoIntent.putExtra("IN_GROUP_DETAILS", new HashMap<String, String>(jsonToMap(groupDetails)));
							} else if (groupData.getString("type").equals("album")) {
								gotoIntent = new Intent();
								gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomAlbumsDetailsActivity");
								gotoIntent.putExtra("IN_ALBUM", new HashMap<String, String>(jsonToMap(groupData)));
								gotoIntent.putExtra("IN_GROUP_ID", groupData.getString("groupid"));
								gotoIntent.putExtra("IN_GROUP_UPLOAD_PHOTO", groupData.getString("uploadPhoto"));
							} else if (groupData.getString("type").equals("videos")) {
								gotoIntent = new Intent();
								gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomVideoDetailsActivity");
								gotoIntent.putExtra("IN_VIDEO_DETAILS", new HashMap<String, String>(jsonToMap(groupData)));
								gotoIntent.putExtra("IN_GROUPID", groupData.getString("groupid"));
							} else if (groupData.getString("type").equals("event")) {
								gotoIntent = new Intent();
								if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
									gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomEventDetailsActivity_v30");
								} else {
									gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomEventDetailsActivity");
								}
								gotoIntent.putExtra("IN_EVENT_DETAILS", new HashMap<String, String>(jsonToMap(groupData)));
							} else if (groupData.getString("type").equals("profile")) {
								gotoIntent = new Intent();
								gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomProfileActivity");
								gotoIntent.putExtra("IN_USERID", groupData.has("id") ? groupData.getString("id") : "0");
							}
						} else if (IN_PUSH_TYPE.equals("event")) {
							JSONObject eventData = ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data");
							gotoIntent = new Intent();
							if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
								gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomEventDetailsActivity_30");
							}else{
								gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomEventDetailsActivity");
							}
							gotoIntent.putExtra("IN_EVENT_DETAILS", new HashMap<String, String>(jsonToMap(eventData)));
						} else if (IN_PUSH_TYPE.equals("album")) {
							JSONObject photoData = ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data");
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomAlbumsDetailsActivity");
							gotoIntent.putExtra("IN_ALBUM", new HashMap<String, String>(jsonToMap(photoData)));
						} else if (IN_PUSH_TYPE.equals("videos")) {
							JSONObject videoData = ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data");
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomVideoDetailsActivity");
							gotoIntent.putExtra("IN_VIDEO_DETAILS", new HashMap<String, String>(jsonToMap(videoData)));
						} else if (IN_PUSH_TYPE.equals("photos")) {
							JSONObject photoData = ((JSONObject) data2).getJSONObject("data").getJSONObject("content_data");
							JSONObject photoDetail = new JSONObject(photoData.getString("photodetail"));
							JSONObject albumDetail = new JSONObject(photoData.getString("albumdetail"));
							ArrayList<HashMap<String, String>> IN_PHOTO_LIST = new ArrayList<HashMap<String, String>>();
							IN_PHOTO_LIST.add(new HashMap<String, String>(jsonToMap(photoDetail)));
							gotoIntent = new Intent();
							gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomPhotoDetailsActivity");
							gotoIntent.putExtra("IN_PHOTO_LIST", IN_PHOTO_LIST);
							gotoIntent.putExtra("IN_ALBUM", new HashMap<String, String>(jsonToMap(albumDetail)));
						}
						startActivity(gotoIntent);
						finish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					if (IN_PUSH_TYPE.equals("profile")) {
						gotoIntent = new Intent();
						gotoIntent.setClassName(IjoomerPushNotificationLuncherActivity.this, "com.ijoomer.components.jomsocial.JomProfileActivity");
						gotoIntent.putExtra("IN_USERID", "0");
						startActivity(gotoIntent);
					} else {
						responseErrorMessageHandler(responseCode, true);
					}
				}
			}

			@Override
			public void onProgressUpdate(int progressCount) {
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
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_loading_profile), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						if (responseCode == 599 && finishActivityOnConnectionProblem) {
							finish();
						}
					}
				});
	}

}
