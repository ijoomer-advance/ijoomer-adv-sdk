package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomEventDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Fragment Contains All Method Related To JomEventAllFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomEventAllFragment extends SmartFragment implements JomTagHolder {

	private ListView lstEvent;
	private LinearLayout listFooter;
	private SeekBar proSeekBar;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterEvent;
	private JomEventDataProvider providerAllEvent;
	private AQuery androidQuery;
	
	private String IN_USERID;

	public JomEventAllFragment() {
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_event_all_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstEvent = (ListView) currentView.findViewById(R.id.lstEvent);
		lstEvent.addFooterView(listFooter, null, false);
		lstEvent.setAdapter(null);

		androidQuery = new AQuery(getActivity());
		providerAllEvent = new JomEventDataProvider(getActivity());

		getIntentData();
	}

	@Override
	public void prepareViews(View currentView) {
		if (adapterEvent == null) {
			getAllEvent(true);
		} else {
			lstEvent.setAdapter(adapterEvent);
			getAllEvent(false);
		}

	}

	@Override
	public void setActionListeners(View currentView) {
		lstEvent.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!providerAllEvent.isCalling() && providerAllEvent.hasNextPage()) {
						listFooterVisible();
						providerAllEvent.getAllEventList(new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									listFooterInvisible();
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(providerAllEvent.getNotificationData());
										prepareList(data1, true);
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
								} catch (Exception e) {
								}

							}
						});
					}
				}
			}
		});
	}

	/**
	 * Class methods
	 */
	
	/**
	 * This method used to update all event fragment.
	 */
	public void update() {
		getAllEvent(false);
	}

	/**
	 * This method used to visible list footer
	 */
	public void listFooterVisible() {
		listFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * This method used to gone list footer
	 */
	public void listFooterInvisible() {
		listFooter.setVisibility(View.GONE);
	}

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_USERID = getActivity().getIntent().getStringExtra("IN_USERID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_USERID");
	}

	/**
	 * This method used to get all event.
	 * @param isProgressShow represented progress shown
	 */
	private void getAllEvent(final boolean isProgressShow) {
		providerAllEvent.restorePagingSettings();
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		providerAllEvent.getAllEventList(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {
				if (isProgressShow) {
					proSeekBar.setProgress(progressCount);
				}
			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				try {
					if (responseCode == 200) {
						((JomMasterActivity) getActivity()).updateHeader(providerAllEvent.getNotificationData());
						prepareList(data1, false);
						adapterEvent = getListAdapter(listData);
						lstEvent.setAdapter(adapterEvent);
					} else {
						lstEvent.setAdapter(null);
						responseErrorMessageHandler(responseCode, false);
					}
				} catch (Exception e) {
				}
			}
		});
	}
	
	/**
	 * This method used to shown response message.
	 * @param responseCode represented response code
	 * @param finishActivityOnConnectionProblem represented finish activity on connection problem 
	 */
	private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
		IjoomerUtilities.getCustomOkDialog(getString(R.string.event), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to prepare list for all event data.
	 * @param data represented all event data list
	 * @param append represented append data
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_event_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					adapterEvent.add(item);
				} else {
					listData.add(item);
				}
			}

		}

	}

	/**
	 * List adapter fro all event.
	 */
	private SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> data) {

		SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_event_list_item, data, new ItemView() {

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

				holder.lnrEventList = (LinearLayout) v.findViewById(R.id.lnrEventList);
				holder.imgEventAvatar = (ImageView) v.findViewById(R.id.imgEventAvatar);
				holder.txtEventDate = (IjoomerTextView) v.findViewById(R.id.txtEventDate);
				holder.txtEventTitle = (IjoomerTextView) v.findViewById(R.id.txtEventTitle);
				holder.txtEventStatus = (IjoomerTextView) v.findViewById(R.id.txtEventStatus);
				holder.txEventLocation = (IjoomerTextView) v.findViewById(R.id.txEventLocation);
				holder.txtEventStartEndDate = (IjoomerTextView) v.findViewById(R.id.txtEventStartEndDate);
				holder.txtEventGuestAttendingCount = (IjoomerTextView) v.findViewById(R.id.txtEventGuestAttendingCount);
				holder.lnrEventPending = (LinearLayout) v.findViewById(R.id.lnrEventPending);
				holder.txtEventPendingAccept = (IjoomerTextView) v.findViewById(R.id.txtEventPendingAccept);
				holder.txtEventPendingReject = (IjoomerTextView) v.findViewById(R.id.txtEventPendingReject);
				holder.lnrEventPending.setVisibility(View.GONE);
				holder.txtEventStatus.setVisibility(View.GONE);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);
				androidQuery.id(holder.imgEventAvatar).image(row.get(AVATAR), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0);
				holder.txtEventTitle.setText(row.get(TITLE));
				String[] dateFormate = row.get(DATE).toString().split(" ");
				holder.txtEventDate.setText(dateFormate[1] + " " + dateFormate[0]);
				holder.txEventLocation.setText(row.get(LOCATION));
				holder.txtEventStartEndDate.setText(row.get(STARTDATE) + " - " + row.get(ENDDATE));
				holder.txtEventGuestAttendingCount.setText(String.format(getString(R.string.event_guest_attending_count), row.get(CONFIRMED)));
				if (row.get(ONGOING).equals("1")) {
					holder.txtEventStatus.setVisibility(View.VISIBLE);
				}

				holder.lnrEventList.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
							try {
								((SmartActivity) getActivity()).loadNew(JomEventDetailsActivity_v30.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", row);
							} catch (Throwable e) {
								e.printStackTrace();
							}

						} else {

							try {
								((SmartActivity) getActivity()).loadNew(JomEventDetailsActivity.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_EVENT_DETAILS", row);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});

				return v;
			}

			@Override
			public View setItemView(int position, View v, SmartListItem item) {
				return null;
			}
		});
		return adapterWithHolder;
	}

}
