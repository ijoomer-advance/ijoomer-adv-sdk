package com.ijoomer.components.sobipro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

/**
 * This Fragment Contains All Method Related To SobiproEntryDetailFragment.
 * 
 * @author tasol
 * 
 */

public class SobiproCarEntryDetailFragment extends SmartFragment implements SobiproTagHolder {
	private ArrayList<HashMap<String, String>> entryArrayList;
	private String entryID;
	private String IN_TABLE;
	private int IN_POS;
	private ListView lstEntries;
	private LinearLayout lnrAbout;
	private AQuery androidAQuery;
	private View headerView;
	private ImageView imgEntry, imgShare, imgEmail, imgPhone;
	private IjoomerTextView txtTitle;
	private String phoneContact, emailContact;
	private String shareThumb;
	private SobiproCategoriesDataProvider dataProvider;
	private String descriptionShare = "";
	private String image[];
	private int imagePostion;
	private Timer myTimer;

	private IjoomerTextView lblMake, txtMake, lblYear, txtYear, txtMileage, lblMileage, lblPower, txtPower, lblDoors, txtDoors, txtPrice, lblGearBox, txtGearBox, lblAirBages,
			txtAirBages, lblSeats, txtSeats, txtDescription;

	/**
	 * Constructor.
	 * 
	 * @param entryID
	 *            represented selected entry id.
	 * @param IN_TABLE
	 *            represented table name.
	 */

	public SobiproCarEntryDetailFragment(String entryID, String IN_TABLE, int IN_POS, String IN_PAGELAYOUT) {
		this.entryID = entryID;
		this.IN_TABLE = IN_TABLE;
		this.IN_POS = IN_POS;
	}

	/**
	 * Overrides methods.
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_entry_detail_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			if (image != null && image.length > 0)
				startIconPreloader(image, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void initComponents(View currentView) {
		dataProvider = new SobiproCategoriesDataProvider(getActivity());
		lstEntries = (ListView) currentView.findViewById(R.id.lstEntries);
		androidAQuery = new AQuery(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		headerView = inflater.inflate(R.layout.sobipro_car_entry_detail_header, null, false);
		lnrAbout = (LinearLayout) headerView.findViewById(R.id.lnrAbout);
		imgEntry = (ImageView) headerView.findViewById(R.id.imgEntry);
		imgShare = (ImageView) headerView.findViewById(R.id.imgShare);
		imgEmail = (ImageView) headerView.findViewById(R.id.imgEmail);
		imgPhone = (ImageView) headerView.findViewById(R.id.imgPhone);
		txtTitle = (IjoomerTextView) headerView.findViewById(R.id.txtCarTitle);

		lblMake = (IjoomerTextView) headerView.findViewById(R.id.lblMake);
		txtMake = (IjoomerTextView) headerView.findViewById(R.id.txtMake);
		lblYear = (IjoomerTextView) headerView.findViewById(R.id.lblYear);
		txtYear = (IjoomerTextView) headerView.findViewById(R.id.txtYear);
		lblMileage = (IjoomerTextView) headerView.findViewById(R.id.lblMileage);
		txtMileage = (IjoomerTextView) headerView.findViewById(R.id.txtMileage);
		lblPower = (IjoomerTextView) headerView.findViewById(R.id.lblPower);
		txtPower = (IjoomerTextView) headerView.findViewById(R.id.txtPower);
		lblDoors = (IjoomerTextView) headerView.findViewById(R.id.lblDoors);
		txtDoors = (IjoomerTextView) headerView.findViewById(R.id.txtDoors);
		txtPrice = (IjoomerTextView) headerView.findViewById(R.id.txtPrice);

		lblGearBox = (IjoomerTextView) headerView.findViewById(R.id.lblGearBox);
		txtGearBox = (IjoomerTextView) headerView.findViewById(R.id.txtGearBox);

		lblAirBages = (IjoomerTextView) headerView.findViewById(R.id.lblAirBages);
		txtAirBages = (IjoomerTextView) headerView.findViewById(R.id.txtAirBages);

		lblSeats = (IjoomerTextView) headerView.findViewById(R.id.lblSeats);
		txtSeats = (IjoomerTextView) headerView.findViewById(R.id.txtSeats);

		txtDescription = (IjoomerTextView) headerView.findViewById(R.id.txtDescription);
		imagePostion = -1;

	}

	@Override
	public void prepareViews(View currentView) {

		txtTitle.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lstEntries.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgLightColor());
		lnrAbout.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
		lblMake.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lblYear.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lblMileage.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lblPower.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lblDoors.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lblGearBox.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lblAirBages.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		lblSeats.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		txtPrice.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		txtDescription.setTextColor(SobiproMasterActivity.themes[IN_POS].getTextColor());
		entryArrayList = dataProvider.getEntriesFromCache(IN_TABLE, entryID);

		prepareHeader();
		lstEntries.addHeaderView(headerView);
		lstEntries.setAdapter(null);

	}

	@Override
	public void setActionListeners(View currentView) {

		imgPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (phoneContact != null && phoneContact.length() > 0) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneContact));
					startActivity(intent);
				} else {
					((SmartActivity) getActivity()).ting(getString(R.string.sobipro_not_available_phone));
				}
			}
		});

		imgEntry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (entryArrayList.get(0).get(IMG_GALLERIES).length() > 0) {
						((SmartActivity) getActivity()).loadNew(SobiproGalleryActivity.class, getActivity(), false, "IN_IMAGES", entryArrayList.get(0).get(IMG_GALLERIES),
								"IN_INDEX", imagePostion);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		imgEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (emailContact != null && emailContact.length() > 0) {
					Intent email = new Intent(Intent.ACTION_SEND);
					email.putExtra(Intent.EXTRA_EMAIL, new String[] { emailContact });
					email.setType("message/rfc822");
					try {
						startActivity(Intent.createChooser(email, getString(R.string.sobipro_choose_email_client)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					((SmartActivity) getActivity()).ting(getString(R.string.sobipro_not_available_email));
				}

			}
		});

		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					((SmartActivity) getActivity())
							.loadNew(IjoomerShareActivity.class, getActivity(), false, "IN_SHARE_CAPTION", txtTitle.getText().toString(), "IN_SHARE_DESCRIPTION", descriptionShare,
									"IN_SHARE_THUMB", "" + shareThumb, "IN_SHARE_SHARELINK", "" + entryArrayList.get(0).get(SHARELINK).toString());
				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			myTimer.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			myTimer.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Class methods
	 */

	/**
	 * This is method is used to start Time task for to show images
	 * periodically.
	 */

	public void startTimerTask() {
		MyTimerTask myTask = new MyTimerTask();
		myTimer = new Timer();
		myTimer.schedule(myTask, 0, 3000);

	}

	/**
	 * Inner Class This class is used to load images which is periodically
	 * changed.
	 * 
	 * @author tasol
	 * 
	 */

	class MyTimerTask extends TimerTask {

		public void run() {
			try {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							if (++imagePostion == image.length) {
								imagePostion = 0;
							}

							androidAQuery.id(imgEntry).image(image[imagePostion], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default,
									null, AQuery.FADE_IN);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method is used to handled and displayed some details of entry which
	 * is displayed and managed in listview header.
	 */

	public void prepareHeader() {
		try {

			for (HashMap<String, String> row : entryArrayList) {

				if (row.get(LABELID).equalsIgnoreCase("field_name"))
					txtTitle.setText(row.get(VALUE));

				if (row.get(LABELID).equalsIgnoreCase("field_description"))
					descriptionShare = row.get(VALUE).toString();

				if (row.get(LABELID).equalsIgnoreCase("field_make")) {
					lblMake.setText(row.get(CAPTION));
					txtMake.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_first_registration")) {
					lblYear.setText(row.get(CAPTION));
					txtYear.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_mileage")) {
					lblMileage.setText(row.get(CAPTION));
					txtMileage.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_power")) {
					lblPower.setText(row.get(CAPTION));
					txtPower.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_door_count")) {
					lblDoors.setText(row.get(CAPTION));
					txtDoors.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_price")) {
					txtPrice.setText(row.get(UNIT) + " " + row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_gearbox")) {
					lblGearBox.setText(row.get(CAPTION));
					txtGearBox.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_airbags")) {
					lblAirBages.setText(row.get(CAPTION));
					txtAirBages.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_number_of_seats")) {
					lblSeats.setText(row.get(CAPTION));
					txtSeats.setText(row.get(VALUE));
				}
				if (row.get(LABELID).equalsIgnoreCase("field_description")) {
					txtDescription.setText(Html.fromHtml(row.get(VALUE)));
				}
				if (row.get(TYPE).equalsIgnoreCase("phone") && row.get(VALUE).length() > 0) {
					phoneContact = row.get(VALUE);
					imgPhone.setVisibility(View.VISIBLE);
				}
				if (row.get(TYPE).equalsIgnoreCase("email") && row.get(VALUE).length() > 0) {
					emailContact = row.get(VALUE);
					imgEmail.setVisibility(View.VISIBLE);
				}

			}

			try {
				image = ((IjoomerSuperMaster) getActivity()).getStringArray(entryArrayList.get(0).get(IMG_GALLERIES));

				androidAQuery.id(imgEntry).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used preload the images before activity called.
	 * 
	 * @param icons
	 *            represents the images which are going to display.
	 * @param index
	 *            represents the current index of the singe image from an Array.
	 */

	private void startIconPreloader(final String[] icons, final int index) {

		androidAQuery.ajax(icons[index], Bitmap.class, 0, new AjaxCallback<Bitmap>() {
			@Override
			public void callback(String url, Bitmap object, AjaxStatus status) {
				super.callback(url, object, status);
				if ((icons.length - 1) == index) {
					startTimerTask();
				} else {
					startIconPreloader(icons, index + 1);
				}
			}
		});
	}

}
