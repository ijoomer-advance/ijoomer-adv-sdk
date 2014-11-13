package com.ijoomer.components.easyblog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.MyCustomAdapter;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.custom.interfaces.IjoomerClickListner;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.easyblog.EasyBlogAddBlogDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To EasyBlogAddBlogActivity.
 *
 * @author tasol
 *
 */
public class EasyBlogAddBlogActivity extends EasyBlogMasterActivity {

	private LinearLayout lnr_form;
	private LinearLayout lnrApply;
	private TextView txtImageCaption;
	private Bitmap selectedImage;
	private Button btnApply;
	private Button btnCancel;

	private ImageView image;

	private AQuery andAQuery;
	private EasyBlogAddBlogDataProvider dataProvider;
	final private int CAPTURE_IMAGE_USER_AVATAR = 2;
	final private int PICK_IMAGE_USER_AVATAR = 1;
	private String selectedImagePathUserAvatar;

	private String IN_BLOGID;

	/**
	 * Overrides methods
	 */

	 @Override
	 public int setLayoutId() {
		 return R.layout.easyblog_add_blog;
	 }

	 @Override
	 public void initComponents() {
		 dataProvider = new EasyBlogAddBlogDataProvider(this);
		 lnr_form = (LinearLayout) findViewById(R.id.add_entry_lnr_form);
		 lnrApply = (LinearLayout) findViewById(R.id.lnrApply);
		 btnApply = (Button) findViewById(R.id.btnApply);
		 btnCancel = (Button) findViewById(R.id.btnCancel);

		 andAQuery = new AQuery(this);
	 }

	 @Override
	 public void prepareViews() {
		 IN_BLOGID = getIntent().getStringExtra("IN_BLOGID")!=null?getIntent().getStringExtra("IN_BLOGID"):"0";
		 getBlodField();
	 }

	 @Override
	 public void setActionListeners() {
		 btnApply.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 getSelectedData();
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
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 super.onActivityResult(requestCode, resultCode, data);

		 if (resultCode == Activity.RESULT_OK) {
			 if (requestCode == PICK_IMAGE_USER_AVATAR) {
				 selectedImagePathUserAvatar = getAbsolutePath(data.getData());
				 selectedImage = decodeFile(selectedImagePathUserAvatar);
				 image.setImageBitmap(selectedImage);
				 image.setTag(selectedImagePathUserAvatar);
				 txtImageCaption.setVisibility(View.INVISIBLE);
			 } else if (requestCode == CAPTURE_IMAGE_USER_AVATAR) {
				 selectedImagePathUserAvatar = getImagePath();
				 selectedImage = decodeFile(selectedImagePathUserAvatar);
				 image.setImageBitmap(selectedImage);
				 image.setTag(selectedImagePathUserAvatar);
				 txtImageCaption.setVisibility(View.INVISIBLE);
			 } else {
				 super.onActivityResult(requestCode, resultCode, data);
			 }
		 }
	 }

	 /**
	  * Class methods.
	  */

	  /**
	   * This method is used to get selected data from dynamic created form.
	   */

	   @SuppressWarnings("unchecked")
	 private void getSelectedData() {
		   boolean validationFlag = true;
		   ArrayList<HashMap<String, String>> searchField = new ArrayList<HashMap<String, String>>();
		   HashMap<String, String> field;
		   int size = lnr_form.getChildCount();

		   for (int i = 0; i < size; i++) {
			   View v = (LinearLayout) lnr_form.getChildAt(i);
			   field = new HashMap<String, String>();
			   field.putAll((HashMap<String, String>) v.getTag());
			   IjoomerEditText edtValue = null;
			   Spinner spnrValue = null;
			   IjoomerCheckBox chbValue = null;

			   if (field != null) {
				   if (field.get(TYPE).equals(TEXT)) {
					   edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEdit)).findViewById(R.id.txtValue);

				   } else if (field.get(TYPE).equals(TEXTAREA)) {
					   edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditArea)).findViewById(R.id.txtValue);
				   } else if (field.get(TYPE).equals(DATETIME)) {
					   edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
				   }
				   if (field.get(TYPE).equals(CHECKBOX)) {
					   chbValue = (IjoomerCheckBox) ((LinearLayout) v.findViewById(R.id.lnrCheckbox)).findViewById(R.id.txtValue);
					   field.put(VALUE, chbValue.isChecked() ? "1" : "0");
					   searchField.add(field);
				   } else if (field.get(TYPE).equals(SELECT)) {

					   spnrValue = (Spinner) ((LinearLayout) v.findViewById(R.id.lnrSpin)).findViewById(R.id.txtValue);
					   try {
						   JSONArray options = new JSONArray(field.get(OPTIONS));
						   field.put(VALUE, ((JSONObject) options.get(spnrValue.getSelectedItemPosition())).getString(VALUE));
					   } catch (Throwable e) {
						   e.printStackTrace();
					   }

					   searchField.add(field);
				   } else if (field.get(TYPE).equals(MULTISELECT)) {
					   edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);

				   } else if (field.get(TYPE).equals(IMAGE)) {
					   ImageView txtValue;
					   txtValue = (ImageView) ((LinearLayout) v.findViewById(R.id.lnrImageText)).findViewById(R.id.txtValue);
					   if (txtValue.getTag() != null) {
						   field.put("image", txtValue.getTag().toString());
						   searchField.add(field);
					   }
				   }
				   if (edtValue != null) {
					   if (field.get(REQUIRED).equals("1") && edtValue.getText().toString().length() <= 0) {
						   edtValue.setError(getString(R.string.validation_value_required));
						   validationFlag = false;
					   } else if (field.get(NAME).equalsIgnoreCase("field_email") && edtValue.getText().toString().length() > 0
							   && !IjoomerUtilities.emailValidator(edtValue.getText().toString())) {
						   edtValue.setError(getString(R.string.validation_invalid_email));
						   validationFlag = false;
					   } else {
						   if(field.get(TYPE).equals(MULTISELECT)){
							   field.put(VALUE, edtValue.getTag().toString());
						   }else{
							   field.put(VALUE, edtValue.getText().toString().trim());
						   }
						   searchField.add(field);
					   }

				   }
			   }
		   }

		   if (validationFlag) {
			   dataProvider.addBlog(IN_BLOGID,searchField, new WebCallListener() {
				   final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

				   @Override
				   public void onProgressUpdate(int progressCount) {
					   proSeekBar.setProgress(progressCount);
				   }

				   @Override
				   public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					   try {
						   if (responseCode == 200) {
							   IjoomerApplicationConfiguration.setReloadRequired(true);
							   finish();
						   } else {
							   IjoomerUtilities.getCustomOkDialog(EasyBlogAddBlogActivity.this.getScreenCaption(),
									   getString(getResources().getIdentifier("code" + responseCode, "string", EasyBlogAddBlogActivity.this.getPackageName())),
									   getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
								   @Override
								   public void NeutralMethod() {

								   }
							   });
						   }
					   } catch (Exception e) {
						   e.printStackTrace();
					   }

				   }
			   });
		   }

	   }

	   /**
	    * This method is used to get dynamic fields for blog.
	    */

	   private void getBlodField() {
		   dataProvider.getBlogField(IN_BLOGID,new WebCallListener() {
			   final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			   @Override
			   public void onProgressUpdate(int progressCount) {
				   proSeekBar.setProgress(progressCount);
			   }

			   @Override
			   public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				   try {
					   if (data1 != null && data1.size() > 0) {
						   createForm(data1);
					   } else {
						   IjoomerUtilities.getCustomOkDialog((EasyBlogAddBlogActivity.this).getScreenCaption(),
								   getString(getResources().getIdentifier("code204", "string", EasyBlogAddBlogActivity.this.getPackageName())), getString(R.string.ok),
								   R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
							   @Override
							   public void NeutralMethod() {

							   }
						   });
					   }

				   } catch (Exception e) {
					   e.printStackTrace();
				   }
			   }
		   });
	   }

	   /**
	    * This method is used to create form using dynamic fields.
	    *
	    * @param FIELD_LIST
	    *            represented dynamic field list to create form for add entry.
	    */

	   private void createForm(ArrayList<HashMap<String, String>> FIELD_LIST) {

		   LayoutInflater inflater = LayoutInflater.from(this);
		   LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				   LinearLayout.LayoutParams.WRAP_CONTENT);
		   params.topMargin = 7;
		   LinearLayout layout = null;

		   int size = FIELD_LIST.size();
		   for (int j = 0; j < size; j++) {
			   final HashMap<String, String> field = FIELD_LIST.get(j);
			   View fieldView = inflater.inflate(R.layout.easyblog_dynamic_view_item, null);

			   if (field.get(TYPE).equals(TEXT)) {
				   final IjoomerEditText edit;
				   layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
				   layout.setVisibility(View.VISIBLE);
				   edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				   edit.setText(Html.fromHtml(field.get(VALUE)));
				   edit.setHint(field.get(CAPTION));
				   if (field.get(NAME).equalsIgnoreCase("field_phone") || field.get(NAME).equalsIgnoreCase("field_zip") || field.get(NAME).equalsIgnoreCase("field_distance")
						   || field.get(NAME).equalsIgnoreCase("field_fax") || field.get(NAME).equalsIgnoreCase("field_working_hours")) {
					   edit.setInputType(InputType.TYPE_CLASS_PHONE);
				   }
			   } else if (field.get(TYPE).equals(TEXTAREA)) {
				   final IjoomerEditText edit;
				   layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
				   layout.setVisibility(View.VISIBLE);
				   edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				   edit.setText(Html.fromHtml(field.get(VALUE)));
				   edit.setHint(field.get(CAPTION));

			   } else if (field.get(TYPE).equals(SELECT)) {
				   final Spinner spn;
				   layout = ((LinearLayout) fieldView.findViewById(R.id.lnrSpin));
				   layout.setVisibility(View.VISIBLE);
				   spn = (Spinner) layout.findViewById(R.id.txtValue);
				   MyCustomAdapter adapter = IjoomerUtilities.getSpinnerAdapter(field);
				   spn.setAdapter(adapter);
				   spn.setSelection(adapter.getDefaultPosition());

			   } else if (field.get(TYPE).equals(DATETIME)) {
				   final IjoomerEditText edit;
				   layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
				   layout.setVisibility(View.VISIBLE);
				   edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				   edit.setText(field.get(VALUE));
				   edit.setHint(field.get(CAPTION));
				   edit.setOnClickListener(new OnClickListener() {

					   @Override
					   public void onClick(final View v) {
						   IjoomerUtilities.getDateTimeDialog(((IjoomerEditText) v).getText().toString(), new CustomClickListner() {

							   @Override
							   public void onClick(String value) {
								   ((IjoomerEditText) v).setText(value);
								   ((IjoomerEditText) v).setError(null);
							   }
						   });
					   }
				   });
			   } else if (field.get(TYPE).equals(MULTISELECT)) {
				   final IjoomerEditText edit;
				   layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
				   layout.setVisibility(View.VISIBLE);
				   edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				   edit.setText(field.get(VALUE));
				   edit.setHint(field.get(CAPTION));
				   edit.setOnClickListener(new OnClickListener() {
					   @Override
					   public void onClick(final View v) {
						   String values = "";
						   String ids = "";
						   try {
							   if (((IjoomerEditText) v).getTag() != null)
								   ids = ((IjoomerEditText) v).getTag().toString();
							   values = ((IjoomerEditText) v).getText().toString();
						   } catch (Exception e) {
							   e.printStackTrace();
						   }
						   IjoomerUtilities.getMultiSelectionDialogSobipro(field.get(CAPTION), field.get(OPTIONS), values, ids, new IjoomerClickListner() {

							   @Override
							   public void onClick(String value, String id) {


								   ((IjoomerEditText) v).setText(value.trim());
								   ((IjoomerEditText) v).setTag(id.trim());

							   }
						   });

					   }
				   });
			   } else if (field.get(TYPE).equals(IMAGE)) {
				   TextView txtCaption;
				   layout = ((LinearLayout) fieldView.findViewById(R.id.lnrImageText));
				   layout.setVisibility(View.VISIBLE);
				   txtCaption = ((TextView) layout.findViewById(R.id.txtCaption));
				   if(field.get(VALUE).trim().length()>0 && field.get(VALUE).contains("http://")){
					   andAQuery.id((ImageView)layout.findViewById(R.id.txtValue)).image(field.get(VALUE).trim(),true,true);
				   }
				   txtCaption.setText(field.get(CAPTION));
				   layout.setOnClickListener(new OnClickListener() {

					   @Override
					   public void onClick(View v) {

						   image = (ImageView) v.findViewById(R.id.txtValue);
						   txtImageCaption = (TextView) v.findViewById(R.id.txtCaption);
						   showSelectImageDialog();
					   }
				   });
			   }

			   try {
				   if (field.get(REQUIRED).equalsIgnoreCase("1")) {
					   if(field.get(TYPE).equals(SELECT)){
						   ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION)+" *");
					   }else{
						   ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("*  ");
					   }
				   } else {
					   if(field.get(TYPE).equals(SELECT)){
						   ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText(field.get(CAPTION));
					   }else{
						   ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setVisibility(View.GONE);
					   }
				   }
			   } catch (Exception e) {
				   ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("   ");
			   }

			   fieldView.setTag(field);
			   lnr_form.addView(fieldView, params);
		   }

		   lnrApply.setVisibility(View.VISIBLE);

	   }

	   /**
	    * This method is used to show Image Selector dialog to select the image and
	    * upload for add new entry.
	    */

	   private void showSelectImageDialog() {
		   IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

			   @Override
			   public void onPhoneGallery() {
				   Intent intent = new Intent();
				   intent.setType("image/*");
				   intent.setAction(Intent.ACTION_GET_CONTENT);
				   startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE_USER_AVATAR);
			   }

			   @Override
			   public void onCapture() {
				   final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				   intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
				   startActivityForResult(intent, CAPTURE_IMAGE_USER_AVATAR);
			   }
		   });
	   }

}
