package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomFriendsDataProvider;
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
 * This Fragment Contains All Method Related To JomFriendMemberSearchFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomFriendSearchFragment extends SmartFragment implements JomTagHolder {

	private ListView lstFriend;
	private LinearLayout listFooter;
	private SeekBar proSeekBar;

	private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder adapterFriend;
	private JomFriendsDataProvider providerSearchFriend;
	private AQuery androidQuery;

	private String serachKeyword;

	public void setSerachKeyword(String serachKeyword) {
		this.serachKeyword = serachKeyword;
	}

	public JomFriendSearchFragment() {
	}

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_friend_search_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
		lstFriend = (ListView) currentView.findViewById(R.id.lstFriend);
		lstFriend.addFooterView(listFooter, null, false);
		lstFriend.setAdapter(null);

		androidQuery = new AQuery(getActivity());
		providerSearchFriend = new JomFriendsDataProvider(getActivity());

	}

	@Override
	public void prepareViews(View currentView) {
		getSearchMember(true);
	}

	@Override
	public void setActionListeners(View currentView) {
		lstFriend.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!providerSearchFriend.isCalling() && providerSearchFriend.hasNextPage()) {
						listFooterVisible();
						providerSearchFriend.getSearchFriendList(serachKeyword, new WebCallListener() {

							@Override
							public void onProgressUpdate(int progressCount) {

							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
								try {
									listFooterInvisible();
									if (responseCode == 200) {
										prepareList(data1, true);
									} else {
										responseErrorMessageHandler(responseCode, false);
									}
								} catch (Throwable e) {
								}
							}
						});
					}
				}
			}
		});
        lstFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                if (((HashMap<String, String>) ((SmartListItem) lstFriend.getAdapter().getItem(pos)).getValues().get(0)).get(USER_PROFILE).equalsIgnoreCase("1")) {
                    ((JomMasterActivity) getActivity()).gotoProfile(((HashMap<String, String>) ((SmartListItem) lstFriend.getAdapter().getItem(pos)).getValues().get(0)).get(USER_ID));
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
		getSearchMember(false);
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
	 * This method used to get search member.
	 * @param isProgressShow represented progress shown
	 */
	private void getSearchMember(final boolean isProgressShow) {
		providerSearchFriend.restorePagingSettings();
		if (isProgressShow) {
			proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
		}
		providerSearchFriend.getSearchFriendList(serachKeyword, new WebCallListener() {

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
						prepareList(data1, false);
						adapterFriend = getListAdapter();
						lstFriend.setAdapter(adapterFriend);
					} else {
						responseErrorMessageHandler(responseCode, false);
					}
				} catch (Throwable e) {
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.member), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {
				
			}
		});
	}

	/**
	 * This method used to prepare list search member.
	 * @param data represented search member data
	 * @param append represented data append
	 */
	public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listData.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_friend_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					adapterFriend.add(item);
				} else {
					listData.add(item);
				}
			}

		}
	}

	/**
	 * List adapter for search member.
	 */
	private SmartListAdapterWithHolder getListAdapter() {
		SmartListAdapterWithHolder adapter = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_friend_list_item, listData, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.friendmembertxtName = (IjoomerTextView) v.findViewById(R.id.txtName);
				holder.friendmemberImage = (ImageView) v.findViewById(R.id.imgFriend);
				holder.friendmemberimgOnlineStatus = (ImageView) v.findViewById(R.id.imgOnlineStatus);
				holder.friendmemberimgOnlineStatus.setImageDrawable(getResources().getDrawable(R.drawable.jom_friend_member_offline));

				@SuppressWarnings("unchecked")
				final HashMap<String, String> friend = (HashMap<String, String>) item.getValues().get(0);
				holder.friendmembertxtName.setText(friend.get(USER_NAME));
				androidQuery.id(holder.friendmemberImage).image(friend.get(USER_AVATAR), true, true, ((SmartActivity) getActivity()).getDeviceWidth(), 0);
				if (friend.get(USER_ONLINE).equalsIgnoreCase("1")) {
					holder.friendmemberimgOnlineStatus.setImageDrawable(getResources().getDrawable(R.drawable.jom_friend_member_online));
				}

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
