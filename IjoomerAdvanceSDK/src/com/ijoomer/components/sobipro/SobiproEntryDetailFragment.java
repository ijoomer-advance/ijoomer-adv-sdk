package com.ijoomer.components.sobipro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerMapPloyline;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerMultiPurposeSelector;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.sobipro.SobiproCategoriesDataProvider;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To SobiproEntryDetailFragment.
 * 
 * @author tasol
 * 
 */
public class SobiproEntryDetailFragment extends SmartFragment implements SobiproTagHolder {
	private ArrayList<HashMap<String, String>> entryArrayList;
	private String entryID;
	private String IN_TABLE;
	private int IN_POS;
	private IjoomerMultiPurposeSelector selector;
	private ArrayList<SmartListItem> listReviewData;
	private ArrayList<SmartListItem> listAboutData;
	private ListView lstEntries;
	private AQuery androidAQuery;
	private boolean showExpandedReview = false;
	private SmartListAdapterWithHolder reviewListAdapterWithHolder;
	private SmartListAdapterWithHolder aboutListAdapterWithHolder;
	private View headerView;
	private ImageView imgEntry, imgLogo, imgMap, imgShare, imgEmail, imgPhone;
	private IjoomerTextView txtAddress, txtDistance, txtTitle, txtAddReview;
	private IjoomerButton btnFavourite;
	private IjoomerRadioButton btnAbout, btnReviews;
	private IjoomerRatingBar rtbRating;
	private String phoneContact, emailContact;
	private String shareThumb;
	private SobiproCategoriesDataProvider dataProvider;
	private String descriptionShare = "";
	private String image[];
	private int imagePostion;
	private Timer myTimer;
	private String IN_PAGELAYOUT;

	/**
	 * Constructor.
	 * 
	 * @param entryID
	 *            represented selected entry id.
	 * @param IN_TABLE
	 *            represented table name.
	 */

	public SobiproEntryDetailFragment(String entryID, String IN_TABLE, int IN_POS, String IN_PAGELAYOUT) {
		this.entryID = entryID;
		this.IN_TABLE = IN_TABLE;
		this.IN_POS = IN_POS;
		this.IN_PAGELAYOUT = IN_PAGELAYOUT;
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
		headerView = inflater.inflate(R.layout.sobipro_entry_detail_header, null, false);
		listReviewData = new ArrayList<SmartListItem>();
		listAboutData = new ArrayList<SmartListItem>();
		imgEntry = (ImageView) headerView.findViewById(R.id.imgEntry);
		imgLogo = (ImageView) headerView.findViewById(R.id.imgLogo);
		imgMap = (ImageView) headerView.findViewById(R.id.imgMap);
		imgShare = (ImageView) headerView.findViewById(R.id.imgShare);
		imgEmail = (ImageView) headerView.findViewById(R.id.imgEmail);
		imgPhone = (ImageView) headerView.findViewById(R.id.imgPhone);
		txtAddress = (IjoomerTextView) headerView.findViewById(R.id.txtAddress);
		txtDistance = (IjoomerTextView) headerView.findViewById(R.id.txtDistance);
		txtTitle = (IjoomerTextView) headerView.findViewById(R.id.txtTitle);
		btnFavourite = (IjoomerButton) headerView.findViewById(R.id.btnFavourite);
		btnAbout = (IjoomerRadioButton) headerView.findViewById(R.id.btnAbout);
		selector = new IjoomerMultiPurposeSelector(getActivity());

		btnReviews = (IjoomerRadioButton) headerView.findViewById(R.id.btnReviews);
		txtAddReview = (IjoomerTextView) headerView.findViewById(R.id.txtAddReview);
		rtbRating = (IjoomerRatingBar) headerView.findViewById(R.id.rtbRating);
		imagePostion = -1;

	}

	@SuppressWarnings("deprecation")
	@Override
	public void prepareViews(View currentView) {
		selector.setPressedDrawableResource(R.drawable.sobipro_detailpage_selector_selected_btn);
		selector.setDefaultDrawableResource(SobiproMasterActivity.themes[IN_POS].getSelectorBgDrawable());

		selector.setDefaultTextColor(getResources().getColor(R.color.sobipro_white));
		selector.setPressedTextColor(getResources().getColor(R.color.sobipro_black));

		btnAbout.setTextColor(selector.getTextSelector());
		btnAbout.setBackgroundDrawable(selector.getSelector());

		btnReviews.setTextColor(selector.getTextSelector());
		btnReviews.setBackgroundDrawable(selector.getSelector());

		btnFavourite.setBackgroundResource(SobiproMasterActivity.themes[IN_POS].getFavouriteBtnDrawable());

		imgMap.setBackgroundResource(SobiproMasterActivity.themes[IN_POS].getMapBtnDrawable());
		txtTitle.setTextColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
		txtAddReview.setTextColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
		entryArrayList = dataProvider.getEntriesFromCache(IN_TABLE, entryID);
		if (dataProvider.isFavourite(entryID, IN_PAGELAYOUT)) {
			btnFavourite.setEnabled(false);
			btnFavourite.setBackgroundResource(R.drawable.sobipro_favourite_desabled);
			dataProvider.addToFavourite(entryArrayList, IN_PAGELAYOUT);
		}
		prepareHeader();
		lstEntries.addHeaderView(headerView);
		try {
			prepareReviewList(new IjoomerCaching(getActivity()).parseData(new JSONArray(entryArrayList.get(0).get(REVIEWRATING))), false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		prepareAboutList(entryArrayList, false);
		reviewListAdapterWithHolder = getReviewListAdapter(listReviewData);
		aboutListAdapterWithHolder = getAboutListAdapter(listAboutData);
		lstEntries.setAdapter(aboutListAdapterWithHolder);

	}

	@Override
	public void setActionListeners(View currentView) {
		imgMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map1 = new HashMap<String, String>();
				try {
					if (((SmartActivity) getActivity()).getLatitude() != null && ((SmartActivity) getActivity()).getLatitude().length() > 0) {
						map1.put("latitude", ((SmartActivity) getActivity()).getLatitude());
						map1.put("longitude", ((SmartActivity) getActivity()).getLongitude());
						list.add(map1);
						HashMap<String, String> map2 = new HashMap<String, String>();
						map2.put("latitude", entryArrayList.get(0).get(LATITUDE));
						map2.put("longitude", entryArrayList.get(0).get(LONGITUDE));
						list.add(map2);
						((SmartActivity) getActivity()).loadNew(IjoomerMapPloyline.class, getActivity(), false, "IN_ADDRESS_LIST", list, "IN_DESTINATION_ROUND_SHOW", true);
					} else {
						((SmartActivity) getActivity()).ting(getString(R.string.sobipro_error_location));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

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
					if (entryArrayList.get(0).get(IMG_GALLERIES) != null && entryArrayList.get(0).get(IMG_GALLERIES).length() > 0) {
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
		btnFavourite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dataProvider.addToFavourite(entryArrayList, IN_PAGELAYOUT);
				btnFavourite.setEnabled(false);
				btnFavourite.setBackgroundResource(R.drawable.sobipro_favourite_desabled);
				Toast.makeText(getActivity(), getString(R.string.sobipro_addtofavorite), Toast.LENGTH_SHORT).show();
			}
		});
		btnAbout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lstEntries.setAdapter(aboutListAdapterWithHolder);
				txtAddReview.setVisibility(View.GONE);
			}
		});

		btnReviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lstEntries.setAdapter(reviewListAdapterWithHolder);
				txtAddReview.setVisibility(View.VISIBLE);
			}
		});

		txtAddReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					((SmartActivity) getActivity()).loadNew(SobiproReviewAddActivity.class, getActivity(), false, "IN_SECTION_ID", entryArrayList.get(0).get(SECTIONID),
							"IN_ENTRY_ID", entryArrayList.get(0).get(ID), "IN_CAT_ID", entryArrayList.get(0).get(CATID));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		imgShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					((SmartActivity) getActivity()).loadNew(IjoomerShareActivity.class, getActivity(), false, "IN_SHARE_CAPTION", txtTitle.getText().toString(),
							"IN_SHARE_DESCRIPTION", descriptionShare, "IN_SHARE_THUMB", shareThumb, "IN_SHARE_SHARELINK", entryArrayList.get(0).get(SHARELINK).toString());
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
	 * Class methods.
	 */

	/**
	 * This method is used to handled and displayed some details of entry which
	 * is displayed and managed in listview header.
	 */

	public void prepareHeader() {
		try {
			txtTitle.setText(entryArrayList.get(0).get(TITLE));
			for (HashMap<String, String> row : entryArrayList) {

				if (row.get(LABELID).equalsIgnoreCase("field_address"))
					txtAddress.setText(row.get(VALUE));
				if (row.get(LABELID).equalsIgnoreCase("field_description"))
					descriptionShare = row.get(VALUE).toString();
				if (row.get(LABELID).equalsIgnoreCase("field_company_logo") && row.get("value").length() > 0) {
					shareThumb = row.get("value");
					androidAQuery.id(imgLogo).image(row.get("value"), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default);
					imgLogo.setVisibility(View.VISIBLE);
				}

			}
			try {
				image = ((IjoomerSuperMaster) getActivity()).getStringArray(entryArrayList.get(0).get(IMG_GALLERIES));
				androidAQuery.id(imgEntry).image(image[0], true, true, ((SmartActivity) getActivity()).getDeviceWidth(), R.drawable.sobipro_entry_default);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				double dist = ((SobiproMasterActivity) getActivity()).distanceFrom(Float.parseFloat(((SmartActivity) getActivity()).getLatitude()),
						Float.parseFloat(((SmartActivity) getActivity()).getLongitude()), Float.parseFloat(entryArrayList.get(0).get(LATITUDE)),
						Float.parseFloat(entryArrayList.get(0).get(LONGITUDE)));
				txtDistance.setText(dist + " " + getActivity().getString(R.string.sobipro_restaurant_miles_text));
				txtDistance.setBackgroundColor(getResources().getColor(R.color.sobipro_transparent_black));
			} catch (Exception e) {
			}
			try {
				if (entryArrayList.get(0).get("distance").length() > 0) {
					txtDistance.setText(entryArrayList.get(0).get("distance") + " " + getActivity().getString(R.string.sobipro_miles_text));
					txtDistance.setBackgroundColor(getResources().getColor(R.color.sobipro_transparent_black));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (entryArrayList.get(0).get(TOTALREVIEWCOUNT).length() > 0)
				btnReviews.setText((getActivity().getString(R.string.sobipro_detail_review_text)) + "(" + entryArrayList.get(0).get(TOTALREVIEWCOUNT) + ")");
			if (entryArrayList.get(0).get(AVERAGERATING).length() > 0) {
				rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent);
				rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half);
				rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
				rtbRating.setStarBgColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
				rtbRating.setStarSize(14);
				rtbRating.setStarRating(Float.parseFloat(entryArrayList.get(0).get(AVERAGERATING)) / 2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * List adapter for review list.
	 * 
	 * @param listData
	 *            represented review data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getReviewListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_review_list_item, listData, new ItemView() {

			@Override
			public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

				holder.txtTitle = (IjoomerTextView) v.findViewById(R.id.txtTitle);
				holder.rtbRating = (IjoomerRatingBar) v.findViewById(R.id.rtbRating);
				holder.txtReview = (IjoomerTextView) v.findViewById(R.id.txtReview);
				holder.lnrPros = (LinearLayout) v.findViewById(R.id.lnrPros);
				holder.lnrCons = (LinearLayout) v.findViewById(R.id.lnrCons);
				holder.txtPositiveReview = (IjoomerTextView) v.findViewById(R.id.txtPositiveReview);
				holder.txtNegativeReview = (IjoomerTextView) v.findViewById(R.id.txtNegativeReview);
				holder.lnrRatingCriteria = (LinearLayout) v.findViewById(R.id.lnrRatingCriteria);
				holder.txtProsTitle = (IjoomerTextView) v.findViewById(R.id.txtProsTitle);
				holder.txtConsTitle = (IjoomerTextView) v.findViewById(R.id.txtConsTitle);
				holder.lnrRatingLeft = (LinearLayout) v.findViewById(R.id.lnrRatingLeft);
				holder.lnrRatingRight = (LinearLayout) v.findViewById(R.id.lnrRatingRight);
				holder.txtPlus = (IjoomerTextView) v.findViewById(R.id.txtPlus);
				holder.lnrExpandedView = (LinearLayout) v.findViewById(R.id.lnrExpandedView);
				holder.imgSeparator = (ImageView) v.findViewById(R.id.imgSeparator);

				holder.txtTitle.setTextColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
				holder.txtPlus.setTextColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
				holder.imgSeparator.setBackgroundColor(SobiproMasterActivity.themes[IN_POS].getBgLightColor());
				holder.txtProsTitle.setTextColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
				holder.txtProsTitle.setCompoundDrawablesWithIntrinsicBounds(SobiproMasterActivity.themes[IN_POS].getProsDrawable(), 0, 0, 0);
				holder.txtProsTitle.setCompoundDrawablePadding(5);

				holder.txtConsTitle.setTextColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
				holder.txtConsTitle.setCompoundDrawablesWithIntrinsicBounds(SobiproMasterActivity.themes[IN_POS].getConsDrawable(), 0, 0, 0);
				holder.txtConsTitle.setCompoundDrawablePadding(5);

				try {

					@SuppressWarnings("unchecked")
					HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);

					holder.txtTitle.setText(value.get(REVIEWTITLE));

					holder.rtbRating.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
					holder.rtbRating.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
					holder.rtbRating.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
					holder.rtbRating.setStarBgColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
					holder.rtbRating.setStarSize(14);
					if (value.get(AVERAGERATING).length() > 0)
						holder.rtbRating.setStarRating(Float.parseFloat(value.get(AVERAGERATING)) / 2);

					holder.txtPlus.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (showExpandedReview) {
								showExpandedReview = false;
							} else {
								showExpandedReview = true;
							}
							reviewListAdapterWithHolder.notifyDataSetChanged();
							lstEntries.setSelection(position);

						}
					});
					if (showExpandedReview) {
						holder.txtPlus.setText("- ");
						holder.lnrExpandedView.setVisibility(View.VISIBLE);
						holder.txtReview.setText(value.get(REVIEW));
						if (value.get(REVIEWPOSITIVES) != null && value.get(REVIEWPOSITIVES).length() > 0) {
							holder.lnrPros.setVisibility(View.VISIBLE);
							holder.txtPositiveReview.setText(value.get(REVIEWPOSITIVES));

						}
						if (value.get(REVIEWNEGATIVES) != null && value.get(REVIEWNEGATIVES).length() > 0) {
							holder.lnrCons.setVisibility(View.VISIBLE);
							holder.txtNegativeReview.setText(value.get(REVIEWNEGATIVES));
						}

						ArrayList<HashMap<String, String>> ratingCriterias = null;
						try {
							ratingCriterias = new IjoomerCaching(getActivity()).parseData(new JSONArray(value.get(RATINGS)));

							holder.lnrRatingLeft.removeAllViews();
							holder.lnrRatingRight.removeAllViews();

							for (int i = 0; i < ratingCriterias.size(); i++) {

								LinearLayout layout = new LinearLayout(getActivity());
								LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
								LinearLayout.LayoutParams captionParam = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
								LinearLayout.LayoutParams ratingParam = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
								ratingParam.gravity = Gravity.CENTER_VERTICAL;
								captionParam.gravity = Gravity.CENTER_VERTICAL;
								IjoomerRatingBar rtb = new IjoomerRatingBar(getActivity());

								rtb.setStarSize(11);
								rtb.setFilledStarResourceId(R.drawable.sobipro_rating_transparent_entry_detail);
								rtb.setHalfFilledStarResourceId(R.drawable.sobipro_rating_transparent_half_entry_detail);
								rtb.setEmptyStarResourceId(R.drawable.sobipro_rating_empty_star);
								rtb.setStarBgColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
								if (ratingCriterias.get(i).get(RATINGVOTE).length() > 0) {
									rtb.setStarRating(Float.parseFloat(ratingCriterias.get(i).get(RATINGVOTE)) / 2);
								}

								ratingParam.setMargins(10, 0, 0, 0);
								IjoomerTextView txtCaption = new IjoomerTextView(getActivity());

								txtCaption.setText(ratingCriterias.get(i).get(CRITERIONNAME));
								txtCaption.setSingleLine(true);
								txtCaption.setTextAppearance(getActivity(), R.style.ijoomer_textview_h2);
								txtCaption.setGravity(Gravity.RIGHT);
								rtb.setGravity(Gravity.LEFT);
								layout.addView(txtCaption, captionParam);
								layout.addView(rtb, ratingParam);
								if (i % 2 == 0)
									holder.lnrRatingLeft.addView(layout, param);
								else
									holder.lnrRatingRight.addView(layout, param);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						holder.txtPlus.setText("+");
						holder.lnrExpandedView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return v;

			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;

	}
	
	/**
	 * This is method is used to start Time task for to show images periodically.
	 */

	public void startTimerTask() {
		MyTimerTask myTask = new MyTimerTask();
		myTimer = new Timer();
		myTimer.schedule(myTask, 0, 3000);

	}

	/**
	 * Inner Class
	 * This class is used to load images which is periodically changed.
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
							if (image != null) {
								if (++imagePostion == image.length) {
									imagePostion = 0;
								}
								androidAQuery.id(imgEntry).image(image[imagePostion], true, true, ((SmartActivity) getActivity()).getDeviceWidth(),
										R.drawable.sobipro_entry_default, null, AQuery.FADE_IN_NETWORK);
							}

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
	 * List adapter for about list.
	 * 
	 * @param listData
	 *            represented about data
	 * @return represented {@link SmartListAdapterWithHolder}
	 */

	public SmartListAdapterWithHolder getAboutListAdapter(ArrayList<SmartListItem> listData) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.sobipro_about_list_item, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

				holder.txtCaption = (IjoomerTextView) v.findViewById(R.id.txtCaption);
				holder.txtValue = (IjoomerTextView) v.findViewById(R.id.txtValue);

				try {
					holder.txtCaption.setTextColor(SobiproMasterActivity.themes[IN_POS].getBgColor());
					@SuppressWarnings("unchecked")
					HashMap<String, String> value = (HashMap<String, String>) item.getValues().get(0);
					if (value.get(VALUE) != null && value.get(VALUE).length() > 0) {
						holder.txtCaption.setText(value.get(CAPTION));
						holder.txtValue.setText(value.get(VALUE));
					}
				} catch (Exception e) {
					e.printStackTrace();

				}
				return v;

			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;

	}

	/**
	 * This method is used to prepare initial list from response data for review
	 * list.
	 * 
	 * @param data
	 *            : data from response.
	 * @param append
	 *            represented for paging to show new data in same page.
	 * 
	 * 
	 */
	public void prepareReviewList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {

			listReviewData.clear();
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.sobipro_review_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);

				listReviewData.add(item);
			}
		}

	}
	
	/**
	 * This method is used preload the images before activity called.
	 * @param icons represents the images which are going to display.
	 * @param index represents the current index of the singe image from an Array.
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

	/**
	 * This method is used to prepare initial list from response data for about
	 * list.
	 * 
	 * @param data
	 *            data from response.
	 * @param append
	 *            represented for paging to show new data in same page.
	 * 
	 * 
	 */

	public void prepareAboutList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			listAboutData.clear();
			for (HashMap<String, String> hashMap : data) {
				try {
					if (!hashMap.get(LABELID).equalsIgnoreCase("field_name") && !hashMap.get(LABELID).equalsIgnoreCase("field_address")
							&& !hashMap.get(LABELID).equalsIgnoreCase("field_distance") && !hashMap.get(LABELID).equalsIgnoreCase("field_company_logo")
							&& hashMap.get(VALUE) != null && hashMap.get(VALUE).trim().length() > 0) {

						if (hashMap.get(TYPE).equalsIgnoreCase("phone") && hashMap.get(VALUE).length() > 0) {
							phoneContact = hashMap.get(VALUE);
							imgPhone.setVisibility(View.VISIBLE);
						} else if (hashMap.get(TYPE).equalsIgnoreCase("email") && hashMap.get(VALUE).length() > 0) {
							emailContact = hashMap.get(VALUE);
							imgEmail.setVisibility(View.VISIBLE);
						}

						SmartListItem item = new SmartListItem();
						item.setItemLayout(R.layout.sobipro_about_list_item);
						ArrayList<Object> obj = new ArrayList<Object>();
						obj.add(hashMap);
						item.setValues(obj);

						listAboutData.add(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}


}
