package com.ijoomer.components.sobipro;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproReviewsDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Activity class for SobiproReviewAddActivity view
 * 
 * @author tasol
 * 
 */

public class SobiproReviewAddActivity extends SobiproMasterActivity {

	private LinearLayout lnr_form;
	private IjoomerButton btnCancel;
	private IjoomerButton btnCreate;
	private SeekBar proSeekBar;
	private String IN_ENTRY_ID;
	private ArrayList<HashMap<String, String>> fieldList;
	private SobiproReviewsDataProvider dataProvider;
	private String IN_SECTION_ID = "1";

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_add_review;
	}

	@Override
	public void initComponents() {
		lnr_form = (LinearLayout) findViewById(R.id.lnr_form);
		btnCancel = (IjoomerButton) findViewById(R.id.btnCancel);
		btnCreate = (IjoomerButton) findViewById(R.id.btnCreate);
		dataProvider = new SobiproReviewsDataProvider(this);

	}

	@Override
	public void prepareViews() {
		getIntentData();
		getReviewFields();

	}

	@Override
	public void setActionListeners() {
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftKeyboard();
				saveReview();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Class method
	 */

	/**
	 * This method used to get intent data.
	 */

	private void getIntentData() {
		try {
			IN_SECTION_ID = getIntent().getStringExtra("IN_SECTION_ID");
			IN_ENTRY_ID = getIntent().getStringExtra("IN_ENTRY_ID");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to create a dynamic form for add review.
	 */

	private void createForm() {
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
		params.topMargin = 10;

		LinearLayout layout = null;
		int size = fieldList.size();
		for (int j = 0; j < size; j++) {
			final HashMap<String, String> field = fieldList.get(j);
			View fieldView = inflater.inflate(R.layout.sobipro_dynamic_view_item, null);

			if (field.get(TYPE).equals(TEXT)) {
				final IjoomerEditText edit;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				edit.setText(Html.fromHtml(field.get(VALUE)));
				edit.setHint(field.get(CAPTION));

			} else if (field.get(TYPE).equals(TEXTAREA)) {
				final IjoomerEditText edit;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
				layout.setVisibility(View.VISIBLE);
				edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
				edit.setText(Html.fromHtml(field.get(VALUE)));
				edit.setHint(field.get(CAPTION));

			} else if (field.get(TYPE).equals(SELECT)) {
				final IjoomerRatingBar rtb;
				final IjoomerTextView txtCaption;
				layout = ((LinearLayout) fieldView.findViewById(R.id.lnrRatingBar));
				layout.setVisibility(View.VISIBLE);
				rtb = (IjoomerRatingBar) layout.findViewById(R.id.ratingBar);
				rtb.setStarSize(20);
				rtb.setEditable(true);

				txtCaption = (IjoomerTextView) layout.findViewById(R.id.txtCaption);
				txtCaption.setText(field.get(CAPTION));

			}

			try {
				if (field.get(REQUIRED).equalsIgnoreCase("1")) {
					((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("*  ");
				} else {
					((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("   ");
				}
			} catch (Exception e) {
				((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("   ");
			}

			fieldView.setTag(field);
			lnr_form.addView(fieldView, params);
		}

	}

	/**
	 * This method is used to get dynamic fields for add review.
	 */

	private void getReviewFields() {

		proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		dataProvider.getReviewFields(IN_SECTION_ID, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);
			}

			@Override
			public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						if (data1 != null && data1.size() > 0) {
							fieldList = data1;
							createForm();
						} else {
							IjoomerUtilities.getCustomOkDialog(SobiproReviewAddActivity.this.getScreenCaption(),
									getString(getResources().getIdentifier("code204", "string", SobiproReviewAddActivity.this.getPackageName())), getString(R.string.ok),
									R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {
								@Override
								public void NeutralMethod() {

								}
							});
						}
					} else {
						IjoomerUtilities.getCustomOkDialog(SobiproReviewAddActivity.this.getScreenCaption(),
								getString(getResources().getIdentifier("code" + responseCode, "string", SobiproReviewAddActivity.this.getPackageName())), getString(R.string.ok),
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
	 * This method is used to save newely created review.
	 */

	@SuppressWarnings("unchecked")
	private void saveReview() {

		boolean validationFlag = true;
		ArrayList<HashMap<String, String>> reviewField = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> ratingField = new ArrayList<HashMap<String, String>>();
		int size = lnr_form.getChildCount();
		for (int i = 0; i < size; i++) {
			View v = (LinearLayout) lnr_form.getChildAt(i);
			HashMap<String, String> field = new HashMap<String, String>();
			field.putAll((HashMap<String, String>) v.getTag());
			IjoomerEditText edtValue = null;
			IjoomerRatingBar rtb = null;

			if (field != null) {
				if (field.get(TYPE).equals(TEXT)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEdit)).findViewById(R.id.txtValue);
				} else if (field.get(TYPE).equals(TEXTAREA)) {
					edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditArea)).findViewById(R.id.txtValue);
				}

				else if (field.get(TYPE).equals(SELECT)) {
					rtb = (IjoomerRatingBar) ((LinearLayout) v.findViewById(R.id.lnrRatingBar).findViewById(R.id.ratingBar));

					try {
						if ((rtb.getStarRating() * 2) > 0)
							field.put(VALUE, (rtb.getStarRating() * 2) + "");
						ratingField.add(field);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (edtValue != null) {
					if (field.get(REQUIRED).equals("1") && edtValue.getText().toString().length() <= 0) {

						edtValue.setError(getString(R.string.validation_value_required));

						validationFlag = false;
					} else if (field.get("title").equals("vmail") && !IjoomerUtilities.emailValidator(edtValue.getText().toString())) {
						edtValue.setError(getString(R.string.validation_invalid_email));
						validationFlag = false;

					} else {
						field.put(VALUE, edtValue.getText().toString().trim());
						reviewField.add(field);
					}
				}

			}
		}

		if (validationFlag) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

			dataProvider.addReview(IN_ENTRY_ID, IN_SECTION_ID, reviewField, ratingField, new WebCallListener() {

				@Override
				public void onProgressUpdate(int progressCount) {
					proSeekBar.setProgress(progressCount);
				}

				@Override
				public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
					try {
						if (responseCode == 200) {
							IjoomerApplicationConfiguration.setReloadRequired(true);
							ting(errorMessage);
							finish();
						} else {
							IjoomerUtilities.getCustomOkDialog(getString(R.string.review), getString(getResources()
									.getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
									new CustomAlertNeutral() {

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
}
