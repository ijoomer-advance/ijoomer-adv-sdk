package com.ijoomer.common.classes;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ijoomer.custom.interfaces.ShareListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.src.R;
import com.ijoomer.theme.ThemeManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To IjoomerShareActivity.
 *
 * @author tasol
 *
 */
public class IjoomerShareActivity extends IjoomerSuperMaster {

	private LinearLayout lnrSayAboutStory;
	private LinearLayout lnrEmailShare;
	private IjoomerEditText edtShareEmail;
	private IjoomerEditText edtStory;
	private IjoomerEditText edtShareEmailMessage;
	private IjoomerButton btnSend;
	private IjoomerButton btnCancel;
	private IjoomerButton btnShareStory;
	private RadioGroup rdgShare;
	private IjoomerRadioButton rdbFacebookShare;
	private ImageView imgShareAddEmail;
	private ImageView imgShareClose;

	private ArrayList<HashMap<String, Object>> selectedData;

	private String IN_SHARE_CAPTION;
	private String IN_SHARE_DESCRIPTION;
	private String IN_SHARE_SHARELINK;
	private String IN_SHARE_THUMB;

	private String currentSharing="facebook";

	/**
	 * Overrides methods
	 */
	 @Override
	 public int setLayoutId() {
		 return ThemeManager.getInstance().getShare();
	 }

	 @Override
	 public void initComponents() {

		 lnrSayAboutStory = (LinearLayout) findViewById(R.id.lnrSayAboutStory);
		 lnrEmailShare = (LinearLayout) findViewById(R.id.lnrEmailShare);
		 rdgShare = (RadioGroup) findViewById(R.id.rdgShare);
		 rdbFacebookShare = (IjoomerRadioButton) findViewById(R.id.rdbFacebookShare);
		 imgShareAddEmail = (ImageView) findViewById(R.id.imgShareAddEmail);
		 imgShareClose = (ImageView) findViewById(R.id.imgShareClose);
		 edtShareEmail = (IjoomerEditText) findViewById(R.id.edtShareEmail);
		 edtStory = (IjoomerEditText) findViewById(R.id.edtStory);
		 edtShareEmailMessage = (IjoomerEditText) findViewById(R.id.edtShareEmailMessage);
		 btnSend = (IjoomerButton) findViewById(R.id.btnSend);
		 btnCancel = (IjoomerButton) findViewById(R.id.btnCancel);
		 btnShareStory = (IjoomerButton) findViewById(R.id.btnShareStory);

		 selectedData = new ArrayList<HashMap<String, Object>>();
		 getIntentData();
	 }

	 @Override
	 public void setActionListeners() {
		 btnCancel.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View arg0) {
				 finish();
			 }
		 });

		 btnSend.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 if (edtShareEmail.getText().toString().trim().length() > 0) {
					 onEmail(edtShareEmail.getText().toString(), edtShareEmailMessage.getText().toString().trim());
				 } else {
					 ting(getString(R.string.email) + " " + getString(R.string.validation_value_required));
				 }
			 }
		 });

		 rdgShare.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			 @Override
			 public void onCheckedChanged(RadioGroup arg0, int id) {
				 switch (id) {
				 case R.id.rdbFacebookShare:
					 edtStory.requestFocus();
					 currentSharing = "facebook";
					 lnrSayAboutStory.setVisibility(View.VISIBLE);
					 lnrEmailShare.setVisibility(View.GONE);
					 break;
				 case R.id.rdbTwitterShare:
					 edtStory.requestFocus();
					 currentSharing = "twitter";
					 lnrSayAboutStory.setVisibility(View.VISIBLE);
					 lnrEmailShare.setVisibility(View.GONE);
					 break;
				 case R.id.rdbGooglePlusShare:
					 edtStory.requestFocus();
					 currentSharing = "googleplus";
					 lnrSayAboutStory.setVisibility(View.VISIBLE);
					 lnrEmailShare.setVisibility(View.GONE);
					 break;
				 case R.id.rdbEmailShare:
					 currentSharing = "facebook";
					 lnrSayAboutStory.setVisibility(View.GONE);
					 lnrEmailShare.setVisibility(View.VISIBLE);
					 break;

				 default:
					 break;
				 }
			 }
		 });

		 btnShareStory.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View arg0) {
				 if (currentSharing.equals("facebook")) {
					 onFacebook(edtStory.getText().toString().trim());
				 } else if (currentSharing.equals("twitter")) {
					 onTwitter(edtStory.getText().toString().trim());
				 } else {
					 onGoolePlus(edtStory.getText().toString().trim());
				 }
			 }
		 });

		 imgShareAddEmail.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {

				 IjoomerUtilities.getContactDialog(selectedData, new ShareListner() {

					 @SuppressWarnings("unchecked")
					 @Override
					 public void onClick(String shareOn, Object value, String message) {
						 selectedData.clear();
						 selectedData.addAll((ArrayList<HashMap<String, Object>>) value);
						 edtShareEmail.setText(message);
					 }
				 });
			 }
		 });
		 imgShareClose.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View arg0) {
				 finish();
			 }
		 });

	 }

	 @Override
	 public void onCheckedChanged(RadioGroup arg0, int arg1) {

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
	 public void prepareViews() {
		 rdbFacebookShare.setChecked(true);
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

	 /**
	  * Class methods
	  */

	  /**
	   * This method used to get Intent data.
	   */
	   private void getIntentData() {
		   IN_SHARE_CAPTION = getIntent().getStringExtra("IN_SHARE_CAPTION") != null ? getIntent().getStringExtra("IN_SHARE_CAPTION") : "";
		   IN_SHARE_DESCRIPTION = getIntent().getStringExtra("IN_SHARE_DESCRIPTION") != null ? getIntent().getStringExtra("IN_SHARE_DESCRIPTION") : "";
		   IN_SHARE_THUMB = getIntent().getStringExtra("IN_SHARE_THUMB") != null ? getIntent().getStringExtra("IN_SHARE_THUMB") : "";
		   IN_SHARE_SHARELINK = getIntent().getStringExtra("IN_SHARE_SHARELINK") != null ? getIntent().getStringExtra("IN_SHARE_SHARELINK") : "";
		   System.out.println("IN_SHARE_CAPTION"+IN_SHARE_CAPTION);
		   System.out.println("IN_SHARE_DESCRIPTION"+IN_SHARE_DESCRIPTION);
		   System.out.println("IN_SHARE_THUMB"+IN_SHARE_THUMB);
		   System.out.println("IN_SHARE_SHARELINK"+IN_SHARE_SHARELINK);
	   }

	   /**
	    * This method used to share data on mail.
	    *
	    * @param value
	    *            represented sender id with (,) separated
	    * @param message
	    *            represented message share
	    */
	   private void onEmail(String value, String message) {
		   String[] to = value.toString().split(",");
		   Intent i = new Intent(Intent.ACTION_SEND);
		   i.setType("text/html");
		   i.putExtra(Intent.EXTRA_EMAIL, to);
		   i.putExtra(Intent.EXTRA_SUBJECT, String.format(getString(R.string.share_email_subject), IN_SHARE_CAPTION));
		   i.putExtra(
				   Intent.EXTRA_TEXT,
				   Html.fromHtml(IjoomerUtilities.prepareEmailBody(message == null ? "" : message, getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "") + " " + getString(R.string.saw_this_story_on_the) + " "
						   + getString(R.string.app_name) + " " + getString(R.string.thought_you_should_see_it), IN_SHARE_CAPTION, IN_SHARE_DESCRIPTION, IN_SHARE_SHARELINK, getString(R.string.try_ijoomeradvance), getString(R.string.site_url))));
		   try {
			   startActivity(Intent.createChooser(i, "Send mail..."));
		   } catch (android.content.ActivityNotFoundException ex) {
			   ting(getString(R.string.share_email_no_client));
		   }
	   }

	   /**
	    * This method used to share data on facebook.
	    *
	    * @param message
	    *            represented message share
	    */
	   private void onFacebook(String message) {
		   try {
			   loadNew(IjoomerFacebookSharingActivity.class, IjoomerShareActivity.this, false, "IN_CAPTION", IN_SHARE_CAPTION, "IN_NAME", IN_SHARE_CAPTION, "IN_DESCRIPTION", IN_SHARE_DESCRIPTION, "IN_LINK", IN_SHARE_SHARELINK,
					   "IN_PICTURE", IN_SHARE_THUMB, "IN_MESSAGE", message == null ? "" : message);
		   } catch (Throwable e) {
			   e.printStackTrace();
		   }
	   }

	   /**
	    * This method used to share data on twitter.
	    *
	    * @param message
	    *            represented message share
	    */
	   private void onTwitter(String message) {
		   try {
			   loadNew(IJoomerTwitterShareActivity.class, IjoomerShareActivity.this, false, "IN_TWIT_MESSAGE", message == null ? "" : message + " \n " + IN_SHARE_SHARELINK, "IN_TWIT_IMAGE", IN_SHARE_THUMB);
		   } catch (Throwable e) {
			   e.printStackTrace();
		   }
	   }

	   /**
	    * This method used to share data on Google Plus.
	    *
	    * @param message
	    *            represented message share
	    */
	   private void onGoolePlus(String message) {
		   try {
			   loadNew(IJoomerGooglePlusShareActivity.class, IjoomerShareActivity.this, false, "IN_SHARE_LINK", IN_SHARE_SHARELINK, "IN_SHARE_MESSAGE", message);
		   } catch (Throwable e) {
			   e.printStackTrace();
		   }
	   }

}
