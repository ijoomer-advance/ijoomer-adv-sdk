package com.ijoomer.components.sobipro;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This Fragment Contains All Method Related To SobiproImageFragment.
 * 
 * @author tasol
 * 
 */

public class SobiproRestaurantDealFragment extends SmartFragment implements SobiproTagHolder {
	private ImageView img;
	private AQuery aQuery;
	private IjoomerTextView txtTitle, txtDeal;
	HashMap<String, String> dealData;
	Timer myTimer;
	String image[];
	private int imagePostion;

	/**
	 * Constructor
	 * 
	 * @param imgUrl
	 *            represented image url which will need to display.
	 */

	public SobiproRestaurantDealFragment(HashMap<String, String> dealData) {
		this.dealData = dealData;

	}

	/**
	 * Overrides methods.
	 */

	@Override
	public int setLayoutId() {
		return R.layout.sobipro_restaurant_entry_header_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		aQuery = new AQuery(getActivity());
		imagePostion = -1;
		img = (ImageView) currentView.findViewById(R.id.img);
		txtTitle = (IjoomerTextView) currentView.findViewById(R.id.txtTitle);
		txtDeal = (IjoomerTextView) currentView.findViewById(R.id.txtDeal);

	}

	@Override
	public void onResume() {
		super.onResume();
		try {

			image = ((IjoomerSuperMaster) getActivity()).getStringArray(dealData.get(IMG_GALLERIES));
			if (image.length > 1)
				startIconPreloader(image, 0);
			else
				aQuery.id(img).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default, null, AQuery.FADE_IN_NETWORK);

		} catch (Exception e) {
			e.printStackTrace();
		}

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

	@Override
	public void prepareViews(View currentView) {

		if (dealData.get(NAME).trim().length() > 0) {
			txtTitle.setText(dealData.get(NAME));
		} else {
			txtTitle.setVisibility(View.GONE);
		}
		if (dealData.get(DEALTEXT).trim().length() > 0) {
			txtDeal.setText(dealData.get(DEALTEXT));
		} else {
			txtDeal.setVisibility(View.GONE);
		}
	}

	@Override
	public void setActionListeners(View currentView) {

		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					((SmartActivity) getActivity()).loadNew(SobiproGalleryActivity.class, getActivity(), false, "IN_IMAGES", dealData.get(IMG_GALLERIES), "IN_INDEX", imagePostion);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * This is method is used to start Time task for to show images
	 * periodically.
	 */

	public void startTimerTask() {
		MyTimerTask myTask = new MyTimerTask();
		myTimer = new Timer();
		myTimer.schedule(myTask, 0, 6000);

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
							aQuery.id(img).image(image[imagePostion], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default, null,
									AQuery.FADE_IN_NETWORK);

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
	 * This method is used preload the images before activity called.
	 * 
	 * @param icons
	 *            represents the images which are going to display.
	 * @param index
	 *            represents the current index of the singe image from an Array.
	 */

	private void startIconPreloader(final String[] icons, final int index) {

		aQuery.ajax(icons[index], Bitmap.class, 0, new AjaxCallback<Bitmap>() {
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
