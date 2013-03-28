package com.smart.framework;

/**
 * @author anjum.shrimali
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.util.AQUtility;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.maps.MapView;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerHorizontalScroll;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.map.IjoomerActivityHostFragment;
import com.ijoomer.src.IjoomerLoginActivity;
import com.ijoomer.src.R;
import com.ijoomer.weservice.IjoomerWebService;
import com.smart.exception.InvalidKeyFormatException;
import com.smart.exception.NullDataException;
import com.smart.exception.WronNumberOfArgumentsException;

public abstract class SmartActivity extends FragmentActivity implements SmartActivityHandler, AdvertisementHandller, IjoomerSharedPreferences {

	public static final int SCREEN_ORIENTATION_UNSPECIFIED = -1;
	public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
	public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
	public static final int SCREEN_ORIENTATION_USER = 2;
	public static final int SCREEN_ORIENTATION_BEHIND = 3;
	public static final int SCREEN_ORIENTATION_SENSOR = 4;
	public static final int SCREEN_ORIENTATION_NOSENSOR = 5;
	public static int CURRENTORIENTATION = SCREEN_ORIENTATION_UNSPECIFIED;

	private Tracker mTracker;
	private SmartApplication application = null;

	private ProgressDialog progress = null;
	private String progressMsg = "";

	private WakeLock wakelock;

	private int optionMenu = 0;

	public int width;

	public int height;

	public int density;

	public int orientation;

	public View screenRootView;

	private LinearLayout headerView;

	private LinearLayout footerView;

	private LinearLayout contentView;

	private int headerResId = 0;

	private int footerResId = 0;

	private int screenRootViewResId = 0;

	private LinearLayout grandParent;

	private LinearLayout sideMenu;

	private LinearLayout topView;
	private IjoomerHorizontalScroll hScrollView;

	private LinearLayout topAdvertiseView;
	private LinearLayout bottomAdvertiseView;

	public static LocationManager mlocManager;
	public static LocationListner mListner;
	private static String latitude;
	public static String longitude;

	public int getDeviceWidth() {
		return getWindowManager().getDefaultDisplay().getWidth();
	}

	public int getDeviceHeight() {
		return getWindowManager().getDefaultDisplay().getHeight();
	}

	public LinearLayout getTopAdvertiseView() {
		return topAdvertiseView;
	}

	public LinearLayout getBottomAdvertiseView() {
		return bottomAdvertiseView;
	}

	/** Called when the activity is first created. */

	@Override
	protected void onStart() {
		super.onStart();
		System.gc();
		hScrollView.post(new Runnable() {

			@Override
			public void run() {
				hScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.gc();
		this.application = SmartApplication.REF_SMART_APPLICATION;

		if (getClass().getName().toLowerCase().contains("jom") && getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, "").length() <= 0) {

			getSmartApplication().writeSharedPreferences(SP_LAST_ACTIVITY, getClass().getName());
			Intent intent = new Intent("clearStackActivity");
			intent.setType("text/plain");
			sendBroadcast(intent);
			IjoomerWebService.cookies = null;
			loadNew(IjoomerLoginActivity.class, this, true);

		} else {
			EasyTracker.getInstance().setContext(this);
			mTracker = EasyTracker.getInstance().getTracker();
			mTracker.sendView(getClass().getSimpleName());

			printDeviceConfig(this);
			// if (!isMemorySufficiant()) {
			// getOKDialog("Low Memory", "Please close other application.",
			// "ok",
			// true, new AlertNeutral() {
			//
			// @Override
			// public void NeutralMathod(DialogInterface dialog, int id) {
			// finish();
			// }
			// });
			// } else

			if (mlocManager == null) {
				try {
					mlocManager = (LocationManager) getSystemService(Activity.LOCATION_SERVICE);
					mListner = new LocationListner();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try {

								mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListner);
							} catch (Throwable e) {
								e.printStackTrace();
							}
							try {
								mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mListner);
							} catch (Throwable e) {
								e.printStackTrace();
							}

						}
					});
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			{
				setRequestedOrientation(CURRENTORIENTATION);
				/** Initialize Components */
				initiateActivity();

				postOnCreate();

				loadHeaderComponents();

				initComponents();

				prepareViews();

				setActionListeners();

			}
		}

	}

	protected void postOnCreate() {

	}

	/**
	 * This method will initiate activity in following three steps.<br>
	 * <li>SmartApplication Object's instance will be set to local application
	 * field.</li> <li>Crash handler flag will be checked and attach the crash
	 * handler if flag is set to true.</li> <li>Get all the layouts and set it
	 * on the screen.</li>
	 */
	private void initiateActivity() {
		if (application == null) {
			this.application = SmartApplication.REF_SMART_APPLICATION;
		}

		try {
			if (application.attachedCrashHandler)
				CrashReportHandler.attach(this);
		} catch (Throwable e) {
			e.printStackTrace();
			finish();
		}

		if (setLayoutView() != null) {
			setContentView(setLayoutView());
		} else {
			setContentView(setLayoutId());
		}

	}

	public void setContentView(View contentView) {

		((LinearLayout) getScreenRootView()).addView(getHeaderView());
		((LinearLayout) getScreenRootView()).addView(getTopAdd());

		((LinearLayout) getScreenRootView()).addView(contentView);

		((LinearLayout) getScreenRootView()).addView(getBottomAdd());
		((LinearLayout) getScreenRootView()).addView(getFooterView());

		FrameLayout frm = new FrameLayout(this);
		frm.addView(screenRootView);
		frm.setLayoutParams(new FrameLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));
		FrameLayout frm1 = new FrameLayout(this);
		frm1.setLayoutParams(new FrameLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));
		frm1.setBackgroundColor(Color.TRANSPARENT);
		frm.addView(frm1, new FrameLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight()));
		frm1.setId(123);
		frm1.setVisibility(View.GONE);

		topView = new LinearLayout(this);
		topView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

		grandParent = new LinearLayout(this);
		grandParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

		hScrollView = new IjoomerHorizontalScroll(this);
		hScrollView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
		hScrollView.setId(100);

		sideMenu = new LinearLayout(this);
		sideMenu.setId(108);
		// sideMenu.setBackgroundColor(Color.parseColor("#80ff23ff"));

		grandParent.addView(sideMenu, new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth() - convertSizeToDeviceDependent(60),
				LinearLayout.LayoutParams.FILL_PARENT));
		grandParent.addView(frm, new FrameLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), FrameLayout.LayoutParams.FILL_PARENT));

		hScrollView.addView(grandParent, new LinearLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), LinearLayout.LayoutParams.FILL_PARENT));

		topView.addView(hScrollView, new FrameLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(), FrameLayout.LayoutParams.FILL_PARENT));

		super.setContentView(topView);
		hScrollView.post(new Runnable() {

			@Override
			public void run() {
				hScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
			}
		});

	}

	@Override
	public void setContentView(int resId) {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(resId, getContentView());
		this.setContentView(getContentView());
	}

	public View getScreenRootView() {

		if (screenRootView == null) {
			if (screenRootViewResId == 0) {
				screenRootView = new LinearLayout(this);
				screenRootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
				((LinearLayout) screenRootView).setOrientation(LinearLayout.VERTICAL);
			} else {
				LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				screenRootView = inflater.inflate(screenRootViewResId, null);
			}
		}

		return screenRootView;
	}

	public LinearLayout getHeaderView() {
		if (headerView == null) {
			headerResId = setHeaderLayoutId();
			if (headerResId != 0) {
				headerView = new LinearLayout(this);
				headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				inflater.inflate(headerResId, headerView);
			} else {
				headerView = new LinearLayout(this);
				headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			}

		}
		return headerView;
	}

	public LinearLayout getTopAdd() {
		if (topAdvertiseView == null) {
			View v = setTopAdvertisement();
			if (v != null) {
				topAdvertiseView = new LinearLayout(this);
				topAdvertiseView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				topAdvertiseView.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			} else {
				topAdvertiseView = new LinearLayout(this);
				topAdvertiseView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			}

		}
		return topAdvertiseView;
	}

	public LinearLayout getBottomAdd() {
		if (bottomAdvertiseView == null) {
			View v = setBottomAdvertisement();
			if (v != null) {
				bottomAdvertiseView = new LinearLayout(this);
				bottomAdvertiseView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				bottomAdvertiseView.addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			} else {
				bottomAdvertiseView = new LinearLayout(this);
				bottomAdvertiseView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			}

		}
		return bottomAdvertiseView;
	}

	public LinearLayout getFooterView() {
		if (footerView == null) {
			footerResId = setFooterLayoutId();
			if (footerResId != 0) {
				footerView = new LinearLayout(this);
				footerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
				LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				inflater.inflate(footerResId, footerView);
			} else {
				footerView = new LinearLayout(this);
				footerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			}
		}
		return footerView;
	}

	private LinearLayout getContentView() {
		if (contentView == null) {
			contentView = new LinearLayout(this);
			contentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
			contentView.setOrientation(LinearLayout.VERTICAL);
		}
		return contentView;
	}

	/**
	 * This method will set header layout from R class.
	 * 
	 * @param headerResId
	 *            = Int from R.layout.layout_name
	 */
	public void setHeaderView(int headerResId) {
		this.headerResId = headerResId;
	}

	/**
	 * This method will set header custom header layout view.
	 * 
	 * @param headerResId
	 *            = Int from R.layout.layout_name
	 */
	public void setHeaderView(LinearLayout headerView) {
		this.headerView = headerView;
	}

	/**
	 * This method will set footer layout from R class.
	 * 
	 * @param footerResId
	 *            = Int footer layout (R.layout.footer_layout)
	 */
	public void setFooterView(int footerResId) {
		this.footerResId = footerResId;
	}

	/**
	 * This method will set custom footer view.
	 * 
	 * @param footerView
	 *            = View footerView.
	 */
	public void setFooterView(LinearLayout footerView) {
		this.footerView = footerView;
	}

	/**
	 * This method will set screen root view layout from R class.<br>
	 * This is the main layout which will hold all header, footer and the main
	 * content area.
	 * 
	 * @param screenRootResId
	 *            = Int screen root view layout (R.layout.screenrootview)
	 */
	public void setScreenRootView(int screenRootResId) {
		screenRootViewResId = screenRootResId;
	}

	/**
	 * This method will return the instance of SmartApplication object.
	 * <b>Note</b> : Only one instance of this class SmartApplication will exist
	 * in whole project.
	 * 
	 * @return application = SmartApplication object which is being used by the
	 *         framework.
	 */
	public SmartApplication getSmartApplication() {
		return SmartApplication.REF_SMART_APPLICATION;
	}

	/**
	 * This method will write any text string to the log file generated by the
	 * SmartFramework.
	 * 
	 * @param text
	 *            = String text is the text which is to be written to the log
	 *            file.
	 */
	public void appendLog(String text) {
		File logFile = new File("sdcard/" + application.LOGFILENAME);
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
			Calendar calendar = Calendar.getInstance();
			try {
				System.err.println("Logged Date-Time : " + calendar.getTime().toLocaleString());
			} catch (Throwable e) {
			}
			buf.append("Logged Date-Time : " + calendar.getTime().toLocaleString());
			buf.append("\n\n");
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method will show the progress dialog with given message in the given
	 * activity's context.<br>
	 * The progress dialog will be non cancellable by default. User can not
	 * dismiss it by pressing back IjoomerButton.
	 * 
	 * @param msg
	 *            = String msg to be displayed in progress dialog.
	 * @param context
	 *            = Context context will be current activity's context.
	 *            <b>Note</b> : A new progress dialog will be generated on
	 *            screen each time this method is called.
	 */
	public void showProgressDialog(String msg, final Context context) {
		progressMsg = msg;

		runOnUiThread(new Runnable() {
			public void run() {
				progress = ProgressDialog.show(context, "", progressMsg);
			}
		});
	}

	/**
	 * This method will show the progress dialog with given message in the given
	 * activity's context.<br>
	 * The progress dialog can be set cancellable by passing appropriate flag in
	 * parameter. User can dismiss the current progress dialog by pressing back
	 * IjoomerButton if the flag is set to <b>true</b>; This method can also be
	 * called from non UI threads.
	 * 
	 * @param msg
	 *            = String msg to be displayed in progress dialog.
	 * @param context
	 *            = Context context will be current activity's context.
	 *            <b>Note</b> : A new progress dialog will be generated on
	 *            screen each time this method is called.
	 */
	public void showProgressDialog(String msg, final Context context, final boolean isCancellable) {
		progressMsg = msg;

		runOnUiThread(new Runnable() {
			public void run() {
				progress = ProgressDialog.show(context, "", progressMsg);
				progress.setCancelable(isCancellable);
			}
		});
	}

	/**
	 * This method will hide existing progress dialog.<br>
	 * It will not throw any Exception if there is no progress dialog on the
	 * screen and can also be called from non UI threads.
	 */
	public void hideProgressDialog() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					if (progress.isShowing())
						progress.dismiss();
				} catch (Throwable e) {

				}
			}
		});
	}

	/**
	 * This method will generate and show the Ok dialog with given message and
	 * single message IjoomerButton.<br>
	 * 
	 * @param title
	 *            = String title will be the title of OK dialog.
	 * @param msg
	 *            = String msg will be the message in OK dialog.
	 * @param IjoomerButtonCaption
	 *            = String IjoomerButtonCaption will be the name of OK
	 *            IjoomerButton.
	 * @param target
	 *            = String target is AlertNewtral callback for OK IjoomerButton
	 *            click action.
	 */
	public void getOKDialog(String title, String msg, String IjoomerButtonCaption, boolean isCancelable, final AlertNeutral target) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		int imageResource = android.R.drawable.ic_dialog_alert;
		Drawable image = getResources().getDrawable(imageResource);

		builder.setTitle(title).setMessage(msg).setIcon(image).setCancelable(false).setNeutralButton(IjoomerButtonCaption, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				target.NeutralMathod(dialog, id);
			}
		});

		AlertDialog alert = builder.create();
		alert.setCancelable(isCancelable);
		alert.show();
	}

	public void getCustomOkDialog(String title, String msg, int layoutID, int IjoomerTextViewID, int IjoomerButtonID, final CustomAlertNeutral target) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(layoutID);

		if (title.length() > 0)
			dialog.setTitle(title);

		IjoomerTextView tv = (IjoomerTextView) dialog.findViewById(IjoomerTextViewID);
		tv.setText(msg);
		IjoomerButton ok = (IjoomerButton) dialog.findViewById(IjoomerButtonID);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				target.NeutralMathod();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void getCustomConfirmDialog(String title, String msg, int layoutID, int IjoomerTextViewID, int okIjoomerButtonID, int cancelIjoomerButtonId,
			final CustomAlertMagnatic target) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(layoutID);
		if (title.length() > 0)
			dialog.setTitle(title);

		IjoomerTextView tv = (IjoomerTextView) dialog.findViewById(IjoomerTextViewID);
		tv.setText(msg);
		IjoomerButton ok = (IjoomerButton) dialog.findViewById(okIjoomerButtonID);
		IjoomerButton cancel = (IjoomerButton) dialog.findViewById(cancelIjoomerButtonId);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				target.PositiveMathod();
				dialog.dismiss();
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				target.NegativeMathod();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	/**
	 * This method will generate and show the OK/Cancel dialog with given
	 * message and single message IjoomerButton.<br>
	 * 
	 * @param title
	 *            = String title will be the title of OK dialog.
	 * @param msg
	 *            = String msg will be the message in OK dialog.
	 * @param positiveBtnCaption
	 *            = String positiveBtnCaption will be the name of OK
	 *            IjoomerButton.
	 * @param negativeBtnCaption
	 *            = String negativeBtnCaption will be the name of OK
	 *            IjoomerButton.
	 * @param target
	 *            = String target is AlertNewtral callback for OK IjoomerButton
	 *            click action.
	 */
	public void getConfirmDialog(String title, String msg, String positiveBtnCaption, String negativeBtnCaption, boolean isCancelable, final AlertMagnatic target) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		int imageResource = android.R.drawable.ic_dialog_alert;
		Drawable image = getResources().getDrawable(imageResource);

		builder.setTitle(title).setMessage(msg).setIcon(image).setCancelable(false).setPositiveButton(positiveBtnCaption, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				target.PositiveMathod(dialog, id);
			}
		}).setNegativeButton(negativeBtnCaption, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				target.NegativeMathod(dialog, id);
			}
		});

		AlertDialog alert = builder.create();
		alert.setCancelable(isCancelable);
		alert.show();
	}

	/**
	 * This method will show short length Toast message with given string.
	 * 
	 * @param msg
	 *            = String msg to be shown in Toast message.
	 */
	public void ting(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * This method will show short length Toast message with given string.<br>
	 * This method can also be called from non UI thread.
	 * 
	 * @param msg
	 *            = String msg to be shown in Toast message.
	 */
	public void tingOnUI(final String msg) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(SmartActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});

	}

	/**
	 * This method will show long length Toast message with given string.
	 * 
	 * @param msg
	 *            = String msg to be shown in Toast message.
	 */
	public void tong(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * This method will show long length Toast message with given string.<br>
	 * This method can also be called from non UI thread.
	 * 
	 * @param msg
	 *            = String msg to be shown in Toast message.
	 */
	public void tongOnUI(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(SmartActivity.this, msg, Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * This method will load new activity. If the <b>forgetMe</b> flag is passed
	 * <b>false</b> then the current activity will remain in activity stack.
	 * Otherwise it will be finished first and then new activity will be loaded.
	 * 
	 * @param clazz
	 *            = Class clazz Activity will be loaded.
	 * @param current
	 *            = Activity current will be the same activity from which this
	 *            function is being called.
	 * @param forgetMe
	 *            = boolean forgetMe will decide whether the current activity
	 *            should remain in activity stack or not.
	 */
	public void loadNew(Class<?> clazz, Activity current, boolean forgetMe) {
		Intent intent = new Intent(current, clazz);
		startActivity(intent);
		if (forgetMe) {
			finish();
		}
	}

	public void loadNew(Class<?> clazz, Activity current, int flag) {
		Intent intent = new Intent(current, clazz);
		intent.setFlags(flag);
		startActivity(intent);

	}

	/**
	 * This method will load new activity. If the <b>forgetMe</b> flag is passed
	 * <b>false</b> then the current activity will remain in activity stack.
	 * Otherwise it will be finished first and then new activity will be loaded.
	 * This method will also pass String value with a key to next activity.
	 * 
	 * @param clazz
	 *            = Class clazz Activity will be loaded.
	 * @param current
	 *            = Activity current will be the same activity from which this
	 *            function is being called.
	 * @param forgetMe
	 *            = boolean forgetMe will decide whether the current activity
	 *            should remain in activity stack or not.
	 * @param key
	 *            = String key will be the key to pass value to next activity.
	 * @param dataTopass
	 *            = Any number of key-value pair of any type passed to next
	 *            activity with specified String key.<br>
	 *            Note: Key Must Be String ,Non-Empty and NotNull.<br>
	 *            Value can be any type.<br>
	 *            e.g<br>
	 *            <b> {@code}loadNew(NextActivity.Class, CurrentActivity.this
	 *            ,true,"first","hi","second",10,"third",false);
	 * 
	 */
	public void loadNew(Class<?> clazz, Activity current, boolean forgetMe, Object... dataTopass) throws WronNumberOfArgumentsException, InvalidKeyFormatException,
			NullDataException {

		if (dataTopass.length % 2 != 0) {
			throw new WronNumberOfArgumentsException();
		}
		Intent intent = new Intent(current, clazz);

		for (int i = 1; i < dataTopass.length; i += 2) {

			if ((!(dataTopass[i - 1] instanceof String)) || (String.valueOf(dataTopass[i - 1]).length() <= 0) || (dataTopass[i - 1] == null)) {
				throw new InvalidKeyFormatException();
			}

			if (dataTopass[i] == null)
				throw new NullDataException();

			try {
				if (dataTopass[i] instanceof Boolean) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), Boolean.parseBoolean(String.valueOf(dataTopass[i])));
				} else if (dataTopass[i] instanceof boolean[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (boolean[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Byte) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), Byte.parseByte((String.valueOf(dataTopass[i]))));
				} else if (dataTopass[i] instanceof byte[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (byte[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Character) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (char) (Character) dataTopass[i]);
				} else if (dataTopass[i] instanceof char[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (char[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Double) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), Double.parseDouble((String.valueOf(dataTopass[i]))));
				} else if (dataTopass[i] instanceof double[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (double[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Float) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), Float.parseFloat(String.valueOf(dataTopass[i])));
				} else if (dataTopass[i] instanceof float[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (float[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Integer) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), Integer.parseInt(String.valueOf(dataTopass[i])));
				} else if (dataTopass[i] instanceof int[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (int[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Long) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), Long.parseLong(String.valueOf(dataTopass[i])));
				} else if (dataTopass[i] instanceof long[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (long[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Short) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), Short.parseShort(String.valueOf(dataTopass[i])));
				} else if (dataTopass[i] instanceof short[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (short[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof String) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (String.valueOf(dataTopass[i])));
				} else if (dataTopass[i] instanceof String[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (String[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Parcelable) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (Parcelable) dataTopass[i]);
				} else if (dataTopass[i] instanceof Parcelable[]) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (Parcelable[]) dataTopass[i]);
				} else if (dataTopass[i] instanceof Serializable) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (Serializable) dataTopass[i]);
				} else if (dataTopass[i] instanceof Bundle) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (Bundle) dataTopass[i]);
				} else if (dataTopass[i] instanceof CharSequence) {
					intent.putExtra(String.valueOf(dataTopass[i - 1]), (CharSequence) dataTopass[i]);
				}
			} catch (Throwable e) {
			}
		}

		startActivity(intent);

		if (forgetMe) {
			finish();
		}
	}

	/*
	 * public String getmasterPDFURL() { return masterPDFURL; }
	 */

	public String fireSOAP(String url, String[] nodes) {
		String result = "";
		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			URL sourceURL = new URL(url);
			URLConnection conn_sourceURL = sourceURL.openConnection();
			conn_sourceURL.setConnectTimeout(7000);
			conn_sourceURL.setReadTimeout(7000);

			InputStream is_sourceURL = conn_sourceURL.getInputStream();

			XMLHandler xmlHandler = new XMLHandler();
			for (String node : nodes) {
				xmlHandler.nodes.add(node);
			}

			xr.setContentHandler(xmlHandler);
			xr.parse(new InputSource(is_sourceURL));

			if (xmlHandler.result != null)
				result = xmlHandler.result;
			else
				result = "";
			xmlHandler.result = "";

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * This method will set layout of option menu from R class. Developer just
	 * needs to set the option menu here and the frame will load it
	 * automatically.
	 * 
	 * @param optionMenu
	 *            = int Layout of option menu from R class (R.menu.test_menu)
	 */
	public void setOptionMenu(int optionMenu) {
		this.optionMenu = optionMenu;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (optionMenu != 0) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(optionMenu, menu);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method will return android device UDID.
	 * 
	 * @return DeviceID = String DeviceId will be the Unique Id of android
	 *         device.
	 */
	public String getDeviceUDID() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * IjoomerEditText input mask for Postal codes.
	 * 
	 * @return
	 */
	public InputFilter getPostalMask() {
		return new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

				if (source.length() > 0) {

					if (!Character.isLetterOrDigit(source.charAt(0)))
						return "";
					else {
						switch (dstart) {
						case 0:
							if (!Character.isLetter(source.charAt(0)))
								return "";
							else {
								source = source.toString().toUpperCase();
								break;
							}

						case 1:
							if (!Character.isDigit(source.charAt(0)))
								return "";
							else
								break;

						case 2:
							if (!Character.isLetter(source.charAt(0)))
								return "";
							else {
								source = source.toString().toUpperCase();
								break;
							}

						case 3:
							if (!Character.isDigit(source.charAt(0)))
								return "";
							else
								return "-" + source;

						case 5:
							if (!Character.isLetter(source.charAt(0)))
								return "";
							else {
								source = source.toString().toUpperCase();
								break;
							}

						case 6:
							if (!Character.isDigit(source.charAt(0)))
								return "";
							else
								break;

						default:
							return "";
						}
					}
				} else {

				}

				return null;
			}
		};
	}

	/**
	 * IjoomerEditText input mask for telephone numbers.
	 * 
	 * @return
	 */

	public InputFilter getPhoneMask() {
		return new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				System.out.println("source = " + source + ", start = " + start + ", end = " + end + ", dest = " + dest + ", dstart = " + dstart + ", dend = " + dend);

				if (source.length() > 0) {

					if (!Character.isDigit(source.charAt(0)))
						return "";
					else {
						if ((dstart == 3) || (dstart == 7))
							return "-" + source;
						else if (dstart >= 12)
							return "";
					}

				} else {

				}

				return null;
			}
		};
	}

	/**
	 * This method will set the wake lock. Once this method is called device
	 * will not sleep until either application is finished or removeWakeLock()
	 * method is called.
	 */
	public void setWakeLock() {

		if (wakelock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "");
		}

		wakelock.acquire();
	}

	/**
	 * This method will remove wake lock.
	 */
	public void removeWakeLock() {
		wakelock.release();
	}

	public boolean isMemorySufficiant() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		if (memoryInfo.availMem <= memoryInfo.threshold + (memoryInfo.threshold / 2)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method will print full device configuration to log file and on log
	 * cat.
	 * 
	 * @param context
	 */
	public void printDeviceConfig(Context context) {

		StringBuilder stringBuilder = new StringBuilder();

		try {
			stringBuilder.append("=============== Current Version ===============================\n");
			stringBuilder.append("1.1 Build (Released on 28-09-2011 12:23 PM)");
			stringBuilder.append("\n");
			System.err.println("=============== HEAP INFO ===============================");
			stringBuilder.append("=============== HEAP INFO(S) ===============================");
			stringBuilder.append("\n");

			ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
			MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
			activityManager.getMemoryInfo(memoryInfo);
			System.err.println("Over All Memory: " + (memoryInfo.availMem / 1024) + " KB");
			stringBuilder.append("Over All Memory: " + (memoryInfo.availMem / 1024) + " KB");
			stringBuilder.append("\n");
			System.err.println("low Memory: " + memoryInfo.lowMemory);
			stringBuilder.append("low Memory: " + memoryInfo.lowMemory);
			stringBuilder.append("\n");
			System.err.println("Threshold Memory: " + (memoryInfo.threshold / 1024) + " KB");
			stringBuilder.append("Threshold Memory: " + (memoryInfo.threshold / 1024) + " KB");
			stringBuilder.append("\n");

			System.err.println("=============== OS INFO ===============================");
			stringBuilder.append("=============== OS INFO ===============================");
			stringBuilder.append("\n");
			System.err.println("Device MODEL: " + android.os.Build.MODEL);
			stringBuilder.append("Device MODEL: " + android.os.Build.MODEL);
			stringBuilder.append("\n");
			System.err.println("VERSION RELEASE: " + android.os.Build.VERSION.RELEASE);
			stringBuilder.append("VERSION RELEASE: " + android.os.Build.VERSION.RELEASE);
			stringBuilder.append("\n");
			System.err.println("VERSION SDK: " + android.os.Build.VERSION.SDK);
			stringBuilder.append("VERSION SDK: " + android.os.Build.VERSION.SDK);
			stringBuilder.append("\n");

			System.err.println("=============== Device Information ===============================");
			stringBuilder.append("=============== Device Information ===============================");
			stringBuilder.append("\n");
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			System.err.println("Device Resolution (WxH)= " + dm.widthPixels + " x " + dm.heightPixels);
			width = dm.widthPixels;
			height = dm.heightPixels;
			density = dm.densityDpi;
			orientation = getResources().getConfiguration().orientation;

			stringBuilder.append("Device Resolution (WxH)= " + dm.widthPixels + " x " + dm.heightPixels);
			stringBuilder.append("\n");
			System.err.println("Density DPI= " + dm.densityDpi);
			stringBuilder.append("Density DPI= " + dm.densityDpi);
			stringBuilder.append("\n");

		} catch (Throwable e) {
			e.printStackTrace();
			StringWriter stackTrace = new StringWriter();
			e.printStackTrace(new PrintWriter(stackTrace));
			stringBuilder.append("\n");
			stringBuilder.append("=============== Exception while Fetching Information ===============================");
			stringBuilder.append("\n");
			stringBuilder.append(stackTrace);
			stringBuilder.append("\n");
		}

		appendLog(stringBuilder.toString());

	}

	public void setApplicationOrientation(int orientation) {
		CURRENTORIENTATION = orientation;
	}

	@Override
	public void onLowMemory() {

		AQUtility.cleanCacheAsync(this);
		getOKDialog(getString(R.string.alert_title_low_memory), getString(R.string.alert_message_low_memory), getString(R.string.ok), true, new AlertNeutral() {

			@Override
			public void NeutralMathod(DialogInterface dialog, int id) {
				finish();
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		System.gc();
	}

	public MapView getMapView() {
		return IjoomerActivityHostFragment.mapView;
	}

	public int convertSizeToDeviceDependent(int value) {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return ((dm.densityDpi * value) / 160);
	}

	public String getLatitude() {
		if (latitude != null) {
			return latitude;
		}
		Location loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null) {
			loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				return "" + loc.getLatitude();
			}
		} else {
			return "" + loc.getLatitude();
		}
		return "0.0";
	}

	public void setLatitude(String latitide) {
		latitude = latitide;
	}

	public String getLongitude() {
		if (longitude != null) {
			return longitude;
		}
		Location loc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null) {
			loc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				return "" + loc.getLongitude();
			}
		} else {
			return "" + loc.getLongitude();
		}
		return "0.0";
	}

	public void setLongitude(String longitude) {
		SmartActivity.longitude = longitude;
	}

	class LocationListner implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			setLatitude("" + location.getLatitude());
			setLongitude("" + location.getLongitude());
			mlocManager.removeUpdates(mListner);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}
}
