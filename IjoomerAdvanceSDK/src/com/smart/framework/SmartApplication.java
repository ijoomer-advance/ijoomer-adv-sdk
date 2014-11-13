package com.smart.framework;

/*****
 * @author anjum.shrimali
 */

import java.io.File;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.application.configuration.ApplicationConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;

public class SmartApplication extends Application implements IjoomerSharedPreferences {

    public String APP_NAME;

    private SmartDataHelper dataHelper;

    public static SmartApplication REF_SMART_APPLICATION;

    public String SHARED_PREFERENCE;
    public String LOGFILENAME;
    private boolean isDBEnabled = false;
    private boolean isSharedPreferenceEnabled = false;
    private SharedPreferences sharedPreferences;
    public boolean attachedCrashHandler = false;
    public String securityKey = "";
    private String dbName, dbSql;
    private int dbVersion;
    private SmartVersionHandler smartVersionHandler;
    static Class<?> a = Activity.class;
    private UncaughtExceptionHandler defaultUEH;
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            // // here I do logging of exception to a db
            //
            // PendingIntent myActivity =
            // PendingIntent.getActivity(getContext(), 192837,
            // new Intent(getContext(), MyActivity.class),
            // PendingIntent.FLAG_ONE_SHOT);
            //
            // AlarmManager alarmManager =
            // (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            // alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
            // 15000, myActivity );
            // System.exit(2);

            // re-throw critical exception further to the os (important)
            defaultUEH.uncaughtException(thread, ex);
        }
    };

    public SmartApplication() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        REF_SMART_APPLICATION = this;

        loadConfiguration();

        @SuppressWarnings("unused")
        SmartFrameworkSecurity smartFrameworkSecurity = new SmartFrameworkSecurity(this);

        // if (smartFrameworkSecurity.matchKey(securityKey)) {

        if (isSharedPreferenceEnabled) {
            sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        }

        if (isDBEnabled) {
            try {
                dataHelper = new SmartDataHelper(getApplicationContext(), dbName, dbVersion, dbSql, getSmartVersionHandler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // } else {
        // Log.e("SmartFrameworkError",
        // "Sorry, the security key did not match!! Please try with appropriate security key.");
        // REF_SMART_APPLICATION = null;
        // this.onTerminate();
        // }

        // ACRA.init(this);

    }

    /**
     * Version Handler setter for handling application database versions and
     * providing scope for executing statements whenever the application is
     * installed or updated.
     *
     * @param smartVersionHandler
     *            = Reference of current Version Handler interface instance.
     */

    public void setSmartVersionHandler(SmartVersionHandler smartVersionHandler) {
        this.smartVersionHandler = smartVersionHandler;
    }

    /**
     * Getter for SmartVersionHandler
     *
     * @return = Instance of SmartVersionHandler
     */
    public SmartVersionHandler getSmartVersionHandler() {
        return smartVersionHandler;
    }

    /**
     * Loads configurations from ApplicationConfiguration Class.
     */
    private void loadConfiguration() {
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        APP_NAME = applicationConfiguration.AppName(this);
        SHARED_PREFERENCE = APP_NAME;
        isDBEnabled = applicationConfiguration.IsDBEnabled();
        isSharedPreferenceEnabled = applicationConfiguration.IsSharedPreferenceEnabled();
        attachedCrashHandler = applicationConfiguration.IsCrashHandlerEnabled();
        LOGFILENAME = applicationConfiguration.CrashHandlerFileName();
        securityKey = applicationConfiguration.SecurityKey();
        dbName = applicationConfiguration.DatabaseName();
        dbSql = applicationConfiguration.DatabaseSQL();
        dbVersion = applicationConfiguration.DatabaseVersion();
        if (applicationConfiguration instanceof SmartVersionHandler) {
            smartVersionHandler = (SmartVersionHandler) applicationConfiguration;
        }
    }

    /**
     * This method will set object of <b>SmartDataHelper</b> to framework. If
     * not set, it will use the default created by framework itself.
     *
     * @param dataHelper
     *            = Object of SmartDataHelper class
     */
    public void setDataHelper(SmartDataHelper dataHelper) {
        this.dataHelper = dataHelper;
    }

    /**
     * This method will return instance of <b>SharedPreferences</b> generated by
     * SmartFramework. Framework will use SharedPreference name as given in
     * <b>ApplicationConfiguration</b> for generation of SharedPreference.
     * <b>Note</b> : SharedPreference Mode will be private whenever generated by
     * SmartFramework.
     *
     * @return sharedPreferences = Instance of SharedPreferences created by
     *         SmartFramework.
     */
    public SharedPreferences readSharedPreferences() {
        return sharedPreferences;
    }

    /**
     * This method will write to <b>SharedPreferences</b>.
     *
     * @param key
     *            = String <b>key</b> to store in <b>SharedPreferences</b>.
     * @param value
     *            = String <b>value</b> to store in <b>SharedPreferences</b>.
     */
    public void writeSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = readSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * This method will write to <b>SharedPreferences</b>.
     *
     * @param key
     *            = String <b>key</b> to store in <b>SharedPreferences</b>.
     * @param value
     *            = boolean <b>value</b> to store in <b>SharedPreferences</b>.
     */
    public void writeSharedPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = readSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * This method will write to <b>SharedPreferences</b>.
     *
     * @param key
     *            = String <b>key</b> to store in <b>SharedPreferences</b>.
     * @param value
     *            = float <b>value</b> to store in <b>SharedPreferences</b>.
     */
    public void writeSharedPreferences(String key, float value) {
        SharedPreferences.Editor editor = readSharedPreferences().edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * This method will write to <b>SharedPreferences</b>.
     *
     * @param key
     *            = String <b>key</b> to store in <b>SharedPreferences</b>.
     * @param value
     *            = int <b>value</b> to store in <b>SharedPreferences</b>.
     */
    public void writeSharedPreferences(String key, int value) {
        SharedPreferences.Editor editor = readSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * This method will write to <b>SharedPreferences</b>.
     *
     * @param key
     *            = String <b>key</b> to store in <b>SharedPreferences</b>.
     * @param value
     *            = long <b>value</b> to store in <b>SharedPreferences</b>.
     */
    public void writeSharedPreferences(String key, long value) {
        SharedPreferences.Editor editor = readSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * This method will return instance of <b>SmartDataHelper</b> which is
     * currently being used by the SmartFramework.<br>
     * This method will return <b>null</b>, if <b>isDBEnabled</b> flag is false
     * in <b>ApplicationConfiguration</b>.
     *
     * @return dataHelper = Instance of <b>SmartDataHelper</b>.
     */
    public SmartDataHelper getDataHelper() {
        if (isDBEnabled)
            return dataHelper;
        else
            return null;
    }

    @Override
    public void onTerminate() {
        System.out.println("appdestroyed");
        if (isDBEnabled)
            dataHelper.getDB().close();
        super.onTerminate();
    }

    public boolean isAppForground() {

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            RunningAppProcessInfo info = i.next();

            if (info.uid == getApplicationInfo().uid && info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public void clearApplicationData() {

        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

}
