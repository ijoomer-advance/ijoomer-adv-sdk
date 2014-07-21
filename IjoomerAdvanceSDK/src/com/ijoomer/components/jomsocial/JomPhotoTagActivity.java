package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.custom.interfaces.PhotoTagListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.PhotoTagView;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To JomPhotoTagActivity.
 *
 * @author tasol
 *
 */
public class JomPhotoTagActivity extends JomMasterActivity {

	private LinearLayout lnrTagOptions;
	private IjoomerButton btnAddTag;
	private IjoomerButton btnRemoveTag;
	private PopupWindow dialog;
	private ProgressBar pbrImage;

	private AQuery androidQuery;
	private PhotoTagView imgPhotoDetail;
	private ArrayList<HashMap<String, String>> tagList;

	private HashMap<String, String> IN_PHOTO_DATA;

	private JomGalleryDataProvider provider;

	private String tagPosition;

	private final int ADD_TAG =1;
	private final int REMOVE_TAG =2;


	/**
	 * Overrides methods
	 */

	 @Override
	 public int setLayoutId() {
		 return R.layout.jom_photo_tag;
	 }

	 @Override
	 public void initComponents() {
		 getIntentData();

		 androidQuery = new AQuery(this);
		 provider = new JomGalleryDataProvider(this);

		 imgPhotoDetail = (PhotoTagView) findViewById(R.id.imgPhotoDetail);
		 pbrImage = (ProgressBar) findViewById(R.id.pbrImage);
		 btnAddTag = (IjoomerButton) findViewById(R.id.btnAddTag);
		 btnRemoveTag = (IjoomerButton) findViewById(R.id.btnRemoveTag);
		 lnrTagOptions = (LinearLayout) findViewById(R.id.lnrTagOptions);
	 }

	 @Override
	 public void prepareViews() {
		 pbrImage.setVisibility(View.VISIBLE);
		 imgPhotoDetail.setTagLabelResource(R.drawable.tag_label);

		 androidQuery.ajax(IN_PHOTO_DATA.get("url"), Bitmap.class, 0, new AjaxCallback<Bitmap>() {
			 @Override
			 public void callback(String url, Bitmap object, AjaxStatus status) {
				 super.callback(url, object, status);
				 pbrImage.setVisibility(View.GONE);
				 imgPhotoDetail.setImageBitmap(object);

			 }
		 });
		 loadPhotoTags();
	 }

	 @Override
	 public void setActionListeners() {

		 imgPhotoDetail.setPhotoTagListener(new PhotoTagListener() {

			 @Override
			 public void onTagedItemClicked(int position, Object data) {
				 gotoProfile(tagList.get(position).get(USER_ID));
			 }

			 @Override
			 public void onAddNewTag(String rectPosition) {
				 tagPosition = rectPosition;
				 try{
					 loadNewResult(JomTagPhotoVideoAddRemoveActivity.class, JomPhotoTagActivity.this, ADD_TAG, "IN_TYPE", PHOTOS, "IN_PHOTO_ID", IN_PHOTO_DATA.get(ID).toString(), "IN_TAG_TYPE", "add");
				 }catch (Exception e){

				 }
				 //showPhotoTagOrRemoveDialog(false);
			 }

			 @Override
			 public void showTagOptions(boolean isTagCanceld) {
				 lnrTagOptions.setVisibility(View.VISIBLE);
				 if (imgPhotoDetail.getTagedUserList() != null && imgPhotoDetail.getTagedUserList().size() > 0) {
					 btnRemoveTag.setVisibility(View.VISIBLE);
				 } else {
					 btnRemoveTag.setVisibility(View.GONE);
				 }
				 if (!isTagCanceld) {
					 btnAddTag.setText(getString(R.string.select_tag_user));
				 } else {
					 btnAddTag.setText(getString(R.string.add_tag_manual));
				 }
			 }

			 @Override
			 public void onTagAreaConflict() {
				 IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user), getString(R.string.dialog_photo_tag_error), getString(R.string.ok),
						 R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					 @Override
					 public void NeutralMethod() {

					 }
				 });
			 }

			 @Override
			 public void onCancel() {
				 btnAddTag.setText(getString(R.string.add_tag_manual));
			 }
		 });

		 btnAddTag.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {

				 if (btnAddTag.getText().toString().equals(getString(R.string.add_tag_manual))) {
					 imgPhotoDetail.setAddTag();
					 btnAddTag.setText(getString(R.string.select_tag_user));
				 } else {
					 imgPhotoDetail.addNewTag();
				 }

			 }
		 });
		 btnRemoveTag.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 try{
					 loadNewResult(JomTagPhotoVideoAddRemoveActivity.class,JomPhotoTagActivity.this,REMOVE_TAG,"IN_TYPE",PHOTOS,"IN_PHOTO_ID",IN_PHOTO_DATA.get(ID).toString(),"IN_TAG_TYPE","remove","IN_PHOTO_REMOVE_TAG_LIST",imgPhotoDetail.getTagedUserList());
				 }catch (Exception e){

				 }
			 }
		 });

	 }

	 @Override
	 public void onCheckedChanged(RadioGroup arg0, int arg1) {
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
	 protected void onActivityResult(int requestCode, int resultCode,final  Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);
		 if(resultCode==RESULT_OK){
			 onResume();
			 if(requestCode==ADD_TAG){
				 provider.addPhotoTag(IN_PHOTO_DATA.get(ID), data.getStringExtra("IN_USER_ID"), tagPosition, new WebCallListener() {
					 final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					 @Override
					 public void onProgressUpdate(int progressCount) {

					 }

					 @Override
					 public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						 btnAddTag.setText(getString(R.string.add_tag_manual));
						 if (responseCode == 200) {

							 provider.getPhotoTages(IN_PHOTO_DATA.get(ID), new WebCallListener() {

								 @Override
								 public void onProgressUpdate(int progressCount) {
									 proSeekBar.setProgress(progressCount);
								 }

								 @Override
								 public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
									 if (responseCode == 200) {
										 tagList = data1;
									 }
									 imgPhotoDetail.setTagedUserList(tagList);

								 }
							 });
						 } else {
							 proSeekBar.setProgress(100);
							 IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user),
									 getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
									 R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

								 @Override
								 public void NeutralMethod() {
									 if (dialog != null && dialog.isShowing()) {
										 dialog.dismiss();
									 }

								 }
							 });
						 }
					 }
				 });
			 }else if(requestCode==REMOVE_TAG){
				 provider.removePhotoTag(data.getStringExtra("IN_TAG_ID"), new WebCallListener() {
					 final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

					 @Override
					 public void onProgressUpdate(int progressCount) {
						 proSeekBar.setProgress(progressCount);
					 }

					 @Override
					 public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
						 if (responseCode == 200) {
							 tagList.remove(data.getIntExtra("IN_TAG_POSITION",0));
							 imgPhotoDetail.setTagedUserList(tagList);
						 } else {
							 responseErrorMessageHandler(responseCode);
						 }
					 }
				 });
			 }
		 }
	 }

	 /**
	  * Class methods
	  */


	 /**
	  * This method used to get intent data.
	  */
	 @SuppressWarnings("unchecked")
	 private void getIntentData() {
		 IN_PHOTO_DATA = (HashMap<String, String>) getIntent().getSerializableExtra("IN_PHOTO_DATA");
	 }

	 /**
	  * This method used to shown response message.
	  *
	  * @param responseCode
	  *            represented response code
	  */
	 private void responseErrorMessageHandler(int responseCode) {
		 IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				 getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			 @Override
			 public void NeutralMethod() {

			 }
		 });
	 }

	 /**
	  * This method used to load photo tag.
	  */
	 private void loadPhotoTags() {
		 provider.getPhotoTages(IN_PHOTO_DATA.get("id"), new WebCallListener() {

			 @Override
			 public void onProgressUpdate(int progressCount) {
			 }

			 @Override
			 public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				 tagList = data1;
				 imgPhotoDetail.setTagedUserList(data1);
			 }
		 });
	 }

}
