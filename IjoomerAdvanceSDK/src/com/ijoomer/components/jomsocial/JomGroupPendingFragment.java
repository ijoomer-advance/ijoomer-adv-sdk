package com.ijoomer.components.jomsocial;

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
import com.ijoomer.library.jomsocial.JomGroupDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To JomGroupPendingFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomGroupPendingFragment extends SmartFragment implements JomTagHolder {

	private ListView lstGroup;
	private LinearLayout listFooter;
	private SeekBar proSeekBar;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterGroup;
	private JomGroupDataProvider providerPendingGroup;
	private JomGroupDataProvider provider;
	private AQuery androidQuery;
	
	private String IN_USERID;

	public JomGroupPendingFragment() {
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_group_my_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstGroup = (ListView) currentView.findViewById(R.id.lstGroup);
		lstGroup.addFooterView(listFooter, null, false);
		lstGroup.setAdapter(null);

		androidQuery = new AQuery(getActivity());
		providerPendingGroup = new JomGroupDataProvider(getActivity());
		provider = new JomGroupDataProvider(getActivity());

		getIntentData();
	}

	@Override
	public void prepareViews(View currentView) {

		if (adapterGroup == null) {
			getPendingGroup(true);
		} else {
			lstGroup.setAdapter(adapterGroup);
			getPendingGroup(false);
		}
	}

	@Override
	public void setActionListeners(View currentView) {
		lstGroup.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!providerPendingGroup.isCalling() && providerPendingGroup.hasNextPage()) {
						listFooterVisible();
						providerPendingGroup.getPendingGroupList(new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									listFooterInvisible();
									if (responseCode == 200) {
										((JomMasterActivity) getActivity()).updateHeader(providerPendingGroup.getNotificationData());
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
	 * This method used to update fragment.
	 */
	public void update() {
		getPendingGroup(false);
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
	 * This method used to get pending group.
	 * @param isProgressShow represented progress shown
	 */
	private void getPendingGroup(final boolean isProgressShow) {
		providerPendingGroup.restorePagingSettings();
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		providerPendingGroup.getPendingGroupList(new WebCallListener() {

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
						((JomMasterActivity) getActivity()).updateHeader(providerPendingGroup.getNotificationData());
						prepareList(data1, false);
						adapterGroup = getListAdapter(listData);
						lstGroup.setAdapter(adapterGroup);
					} else {
						lstGroup.setAdapter(null);
						responseErrorMessageHandler(responseCode, isProgressShow);
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.group), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {
						
					}
				});
	}

	/**
	 * This method used to prepare list pending group.
	 * @param data represented group data
	 * @param append represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {

		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_group_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					adapterGroup.add(item);
				} else {
					listData.add(item);
				}
			}
		}

	}

	/**
	 * List adapter for pending group
	 */
	private SmartListAdapterWithHolder getListAdapter(ArrayList<SmartListItem> data) {

		SmartListAdapterWithHolder adapter = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_group_list_item, data, new ItemView() {

			@SuppressWarnings("unchecked")
			@Override
			public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {

				holder.lnrGroupItem = (LinearLayout) v.findViewById(R.id.lnrGroupItem);
				holder.imgGroupAvatar = (ImageView) v.findViewById(R.id.imgGroupAvatar);
				holder.txtGroupTitle = (IjoomerTextView) v.findViewById(R.id.txtGroupTitle);
				holder.txtGroupDescription = (IjoomerTextView) v.findViewById(R.id.txtGroupDescription);
				holder.txtGroupMember = (IjoomerTextView) v.findViewById(R.id.txtGroupMember);
				holder.txtGroupDiscussion = (IjoomerTextView) v.findViewById(R.id.txtGroupDiscussion);
				holder.txtGroupWallPost = (IjoomerTextView) v.findViewById(R.id.txtGroupWallPost);
				holder.lnrGroupPending = (LinearLayout) v.findViewById(R.id.lnrGroupPending);
				holder.txtGroupPendingAccept = (IjoomerTextView) v.findViewById(R.id.txtGroupPendingAccept);
				holder.txtGroupPendingReject = (IjoomerTextView) v.findViewById(R.id.txtGroupPendingReject);
				holder.lnrGroupPending.setVisibility(View.GONE);

				final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

				holder.lnrGroupPending.setVisibility(View.VISIBLE);
				androidQuery.id(holder.imgGroupAvatar).image(row.get(AVATAR), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0);

				holder.txtGroupTitle.setText(row.get(TITLE));
				holder.txtGroupDescription.setText(row.get(DESCRIPTION));
				holder.txtGroupMember.setText(row.get(MEMBERS));
				holder.txtGroupDiscussion.setText(row.get(DISCUSSIONS));
				holder.txtGroupWallPost.setText(row.get(WALLS));

				holder.txtGroupPendingAccept.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.groupInvitation(row.get(ID), "1", new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									((JomMasterActivity) getActivity()).updateHeader(provider.getNotificationData());
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
								adapterGroup.remove(adapterGroup.getItem(position));

							}
						});
					}
				});
				holder.txtGroupPendingReject.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
						provider.groupInvitation(row.get(ID), "0", new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {
								proSeekBar.setProgress(progressCount);
							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									((JomMasterActivity) getActivity()).updateHeader(provider.getNotificationData());
								} else {
									responseErrorMessageHandler(responseCode, false);
								}
								adapterGroup.remove(adapterGroup.getItem(position));
							}
						});
					}
				});
				holder.lnrGroupItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (IjoomerGlobalConfiguration.getJomsocialVersion().equals(IjoomerGlobalConfiguration.JOMVERSION_V30)) {
							try {
								((SmartActivity) getActivity()).loadNew(JomGroupDetailsActivity_v30.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", row);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						} else {
							try {
								((SmartActivity) getActivity()).loadNew(JomGroupDetailsActivity.class, getActivity(), false, "IN_USERID", IN_USERID, "IN_GROUP_DETAILS", row);
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
		return adapter;
	}

}
