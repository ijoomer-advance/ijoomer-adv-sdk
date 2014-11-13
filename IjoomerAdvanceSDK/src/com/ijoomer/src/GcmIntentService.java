package com.ijoomer.src;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;

/**
 * This Class Contains All Method Related To GCMIntentService.
 * 
 * @author tasol
 * 
 */
public class GcmIntentService extends IntentService {

	private static int count = 1;

	public GcmIntentService() {
		super(IjoomerApplicationConfiguration.getGCMProjectId());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		handleMessage(this, intent);
	}

	/**
	 * Class methods
	 */

	/**
	 * This methods used to handle push notification message.
	 * 
	 * @param mContext
	 *            represented {@link Context}
	 * @param intent
	 *            represented {@link Intent}
	 */
	@SuppressWarnings("deprecation")
	private void handleMessage(Context mContext, Intent intent) {
		long when = System.currentTimeMillis();
		int icon = R.drawable.ijoomer_push_notification_icon;

		try {

			Bundle gcmData = intent.getExtras();

			NotificationManager notificationManager = (NotificationManager) mContext
					.getSystemService(Context.NOTIFICATION_SERVICE);
			PendingIntent contentIntent = null;
			if (gcmData.getString("type").equals("backend")) {
				contentIntent = PendingIntent.getActivity(mContext,
						(int) (Math.random() * 100), new Intent(),
						PendingIntent.FLAG_UPDATE_CURRENT);
			} else {
				if (gcmData.getString("type").equals("message")) {
					Intent gotoIntent = new Intent();
					gotoIntent
							.setClassName(mContext,
									"com.ijoomer.components.jomsocial.JomMessageActivity");
					contentIntent = PendingIntent.getActivity(mContext,
							(int) (Math.random() * 100), gotoIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
				} else if (gcmData.getString("type").equals("friend")) {
					Intent gotoIntent = new Intent();
					gotoIntent
							.setClassName(mContext,
									"com.ijoomer.components.jomsocial.JomFriendListActivity");
					contentIntent = PendingIntent.getActivity(mContext,
							(int) (Math.random() * 100), gotoIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
				} else if (gcmData.getString("type").equals("eventmail")
						|| gcmData.getString("type").equals("groupmail")
						|| gcmData.getString("type").equals("online")) {
					contentIntent = PendingIntent.getActivity(mContext,
							(int) (Math.random() * 100), new Intent(),
							PendingIntent.FLAG_UPDATE_CURRENT);
				} else {
					Intent gotoIntent = new Intent();
					gotoIntent
							.setClassName(mContext,
									"com.ijoomer.src.IjoomerPushNotificationLuncherActivity");
					gotoIntent.putExtra("IN_PUSH_TYPE",
							gcmData.getString("type"));
					gotoIntent.putExtra("IN_PUSH_ID", gcmData.getString("id"));
					contentIntent = PendingIntent.getActivity(mContext,
							(int) (Math.random() * 100), gotoIntent,
							PendingIntent.FLAG_UPDATE_CURRENT);
				}

			}

			Notification notification = new Notification(icon,
					gcmData.getString("message"), when);
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.setLatestEventInfo(mContext,
					gcmData.getString("type"),
					intent.getExtras().getString("message"), contentIntent);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(count, notification);
			count = count + 1;

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
