package com.ijoomer.theme;

import android.app.Activity;

/**
 * Created by tasol on 9/7/13.
 */
public class ThemeManager {

    private static ThemeManager instanceOfThemeManager=null;

    private static final String IJOOMER_ACTIVITY_IDENTIFIER="ijoomer";
    private static final String JOM_ACTIVITY_IDENTIFIER="jom";
    private static final String ICMS_ACTIVITY_IDENTIFIER="icms";
    private static final String K2_ACTIVITY_IDENTIFIER="k2";
    private static final String JREVIEW_ACTIVITY_IDENTIFIER="jreview";
    private static final String SOBIPRO_ACTIVITY_IDENTIFIER="sobipro";
    private static final String EASYBLOG_ACTIVITY_IDENTIFIER="easyblog";

    private static Activity lastActivity;

    private int loadingDialog;
    private int okDialog;
    private int confirmDialog;
    private int contactDilaog;
    private int contactItemDilaog;
    private int share;
    private int facebook;
    private int twitter;
    private int googleplus;
    private int mapAddress;

    public int getWebview() {
        return webview;
    }

    public void setWebview(int webview) {
        this.webview = webview;
    }

    private int webview;


    public int getLoadingDialog() {
        return loadingDialog;
    }

    public void setLoadingDialog(int loadingDialog) {
        this.loadingDialog = loadingDialog;
    }

    public int getOkDialog() {
        return okDialog;
    }

    public void setOkDialog(int okDialog) {
        this.okDialog = okDialog;
    }

    public int getConfirmDialog() {
        return confirmDialog;
    }

    public void setConfirmDialog(int confirmDialog) {
        this.confirmDialog = confirmDialog;
    }

    public int getContactDilaog() {
        return contactDilaog;
    }

    public void setContactDilaog(int contactDilaog) {
        this.contactDilaog = contactDilaog;
    }

    public int getContactItemDilaog() {
        return contactItemDilaog;
    }

    public void setContactItemDilaog(int contactItemDilaog) {
        this.contactItemDilaog = contactItemDilaog;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getFacebook() {
        return facebook;
    }

    public void setFacebook(int facebook) {
        this.facebook = facebook;
    }

    public int getTwitter() {
        return twitter;
    }

    public void setTwitter(int twitter) {
        this.twitter = twitter;
    }

    public int getGoogleplus() {
        return googleplus;
    }

    public void setGoogleplus(int googleplus) {
        this.googleplus = googleplus;
    }

    public int getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(int mapAddress) {
        this.mapAddress = mapAddress;
    }

    private ThemeManager(){
    }

    public synchronized static ThemeManager getInstance(){
        if(instanceOfThemeManager==null){
            instanceOfThemeManager = new ThemeManager();
        }
        return instanceOfThemeManager;
    }


    public void setTheme(Activity currentActivity){
        int themeId=0;
        if(currentActivity.getClass().getSimpleName().toLowerCase().contains(JOM_ACTIVITY_IDENTIFIER)){
            themeId = currentActivity.getResources().getIdentifier(JOM_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName());
            setLayout(currentActivity,JOM_ACTIVITY_IDENTIFIER);
        }else if(currentActivity.getClass().getSimpleName().toLowerCase().contains(ICMS_ACTIVITY_IDENTIFIER)){
            themeId = currentActivity.getResources().getIdentifier(ICMS_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName());
            setLayout(currentActivity,ICMS_ACTIVITY_IDENTIFIER);
        }else if(currentActivity.getClass().getSimpleName().toLowerCase().contains(SOBIPRO_ACTIVITY_IDENTIFIER)){
            themeId = currentActivity.getResources().getIdentifier(SOBIPRO_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName());
            setLayout(currentActivity,SOBIPRO_ACTIVITY_IDENTIFIER);
        }else if(currentActivity.getClass().getSimpleName().toLowerCase().contains(K2_ACTIVITY_IDENTIFIER)){
            themeId = currentActivity.getResources().getIdentifier(K2_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName());
            setLayout(currentActivity,K2_ACTIVITY_IDENTIFIER);
        }else if(currentActivity.getClass().getSimpleName().toLowerCase().contains(EASYBLOG_ACTIVITY_IDENTIFIER)){
            themeId = currentActivity.getResources().getIdentifier(EASYBLOG_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName());
            setLayout(currentActivity,EASYBLOG_ACTIVITY_IDENTIFIER);
        }else if(currentActivity.getClass().getSimpleName().toLowerCase().contains(JREVIEW_ACTIVITY_IDENTIFIER)){
            themeId = currentActivity.getResources().getIdentifier(JREVIEW_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName());
            setLayout(currentActivity,JREVIEW_ACTIVITY_IDENTIFIER);
        }

        if(themeId>0){
            lastActivity =currentActivity;
            currentActivity.setTheme(themeId);
        }else{
            if(lastActivity!=null){
                if(themeId==0){
                    if(lastActivity.getClass().getSimpleName().toLowerCase().contains(JOM_ACTIVITY_IDENTIFIER)){
                        themeId = lastActivity.getResources().getIdentifier(JOM_ACTIVITY_IDENTIFIER+"_theme","style",lastActivity.getPackageName());
                        setLayout(currentActivity,JOM_ACTIVITY_IDENTIFIER);
                    }else if(lastActivity.getClass().getSimpleName().toLowerCase().contains(ICMS_ACTIVITY_IDENTIFIER)){
                        themeId = lastActivity.getResources().getIdentifier(ICMS_ACTIVITY_IDENTIFIER+"_theme","style",lastActivity.getPackageName());
                        setLayout(currentActivity,ICMS_ACTIVITY_IDENTIFIER);
                    }else if(lastActivity.getClass().getSimpleName().toLowerCase().contains(SOBIPRO_ACTIVITY_IDENTIFIER)){
                        themeId = lastActivity.getResources().getIdentifier(SOBIPRO_ACTIVITY_IDENTIFIER+"_theme","style",lastActivity.getPackageName());
                        setLayout(currentActivity,SOBIPRO_ACTIVITY_IDENTIFIER);
                    }else if(lastActivity.getClass().getSimpleName().toLowerCase().contains(K2_ACTIVITY_IDENTIFIER)){
                        themeId = lastActivity.getResources().getIdentifier(K2_ACTIVITY_IDENTIFIER+"_theme","style",lastActivity.getPackageName());
                        setLayout(currentActivity,K2_ACTIVITY_IDENTIFIER);
                    }else if(lastActivity.getClass().getSimpleName().toLowerCase().contains(EASYBLOG_ACTIVITY_IDENTIFIER)){
                        themeId = lastActivity.getResources().getIdentifier(EASYBLOG_ACTIVITY_IDENTIFIER+"_theme","style",lastActivity.getPackageName());
                        setLayout(currentActivity,EASYBLOG_ACTIVITY_IDENTIFIER);
                    }else if(lastActivity.getClass().getSimpleName().toLowerCase().contains(JREVIEW_ACTIVITY_IDENTIFIER)){
                        themeId = lastActivity.getResources().getIdentifier(JREVIEW_ACTIVITY_IDENTIFIER+"_theme","style",lastActivity.getPackageName());
                        setLayout(currentActivity,JREVIEW_ACTIVITY_IDENTIFIER);
                    }else{
                        currentActivity.setTheme(currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName()));
                        setLayout(currentActivity,IJOOMER_ACTIVITY_IDENTIFIER);
                    }
                }
                currentActivity.setTheme(themeId);
            }else{
                currentActivity.setTheme(currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_theme","style",currentActivity.getPackageName()));
                setLayout(currentActivity,IJOOMER_ACTIVITY_IDENTIFIER);
            }
        }

    }

    private  void setLayout(Activity currentActivity,String identifier){
        setLoadingDialog(currentActivity.getResources().getIdentifier(identifier+"_loading_dialog","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_loading_dialog","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_loading_dialog","layout",currentActivity.getPackageName()));
        setOkDialog(currentActivity.getResources().getIdentifier(identifier+"_ok_dialog","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_ok_dialog","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_ok_dialog","layout",currentActivity.getPackageName()));
        setConfirmDialog(currentActivity.getResources().getIdentifier(identifier+"_confirm_dialog","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_confirm_dialog","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_confirm_dialog","layout",currentActivity.getPackageName()));
        setContactDilaog(currentActivity.getResources().getIdentifier(identifier+"_contact_mail_dialog","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_contact_mail_dialog","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_contact_mail_dialog","layout",currentActivity.getPackageName()));
        setContactItemDilaog(currentActivity.getResources().getIdentifier(identifier+"_contact_mail_dialog_item","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_contact_mail_dialog_item","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_contact_mail_dialog_item","layout",currentActivity.getPackageName()));
        setFacebook(currentActivity.getResources().getIdentifier(identifier+"_facebook_main","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_facebook_main","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier("facebook_main","layout",currentActivity.getPackageName()));
        setGoogleplus(currentActivity.getResources().getIdentifier(identifier+"_googleplus_share","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_googleplus_share","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_googleplus_share","layout",currentActivity.getPackageName()));
        setShare(currentActivity.getResources().getIdentifier(identifier+"_share","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_share","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_share","layout",currentActivity.getPackageName()));
        setTwitter(currentActivity.getResources().getIdentifier(identifier+"_twitter_share","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_twitter_share","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_twitter_share","layout",currentActivity.getPackageName()));
        setMapAddress(currentActivity.getResources().getIdentifier(identifier+"_map_address","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_map_address","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_map_address","layout",currentActivity.getPackageName()));
        setWebview(currentActivity.getResources().getIdentifier(identifier+"_custom_webview","layout",currentActivity.getPackageName()) > 0 ? currentActivity.getResources().getIdentifier(identifier+"_custom_webview","layout",currentActivity.getPackageName()) : currentActivity.getResources().getIdentifier(IJOOMER_ACTIVITY_IDENTIFIER+"_custom_webview","layout",currentActivity.getPackageName()));
    }


}
