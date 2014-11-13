package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomAdvancedSearchDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains All Method Related To JomAdvanceSearchDetailActivity.
 * 
 * @author tasol
 * 
 */
public class JomAdvanceSearchDetailActivity extends JomMasterActivity {

	private LinearLayout listFooter;
	private ListView lstFriend;

	private String IN_OPERATOR;
	private String IN_AVATARONLY;
	private ArrayList<HashMap<String, String>> IN_SELECTEDARRAY;

	private AQuery androidQuery;
	private ArrayList<SmartListItem> listDataMember = new ArrayList<SmartListItem>();
	private SmartListAdapterWithHolder listAdapterWithHolderMember;

	private JomAdvancedSearchDataProvider jomAdvancedSearch;



	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_advance_search_list;
	}

	@Override
	public void initComponents() {
		getIntentData();

		androidQuery = new AQuery(this);
		jomAdvancedSearch = new JomAdvancedSearchDataProvider(this);

		listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
		lstFriend = (ListView) findViewById(R.id.lstMember);

		lstFriend.addFooterView(listFooter);
	}

	@Override
	public void prepareViews() {
		getSearchResults();
	}


	@Override
	public void setActionListeners() {

		lstFriend.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
					if (!jomAdvancedSearch.isCalling() && jomAdvancedSearch.hasNextPage()) {
						listFooterVisible();
						jomAdvancedSearch.advanceSearchPostData(IN_OPERATOR, IN_AVATARONLY, IN_SELECTEDARRAY ,new WebCallListener() {


							@Override
							public void onProgressUpdate(int progressCount) {


							}

							@Override
							public void onCallComplete(int responseCode, String errorMessage,
									ArrayList<HashMap<String, String>> data1, Object data2) {
								if (responseCode == 200) {
									try {
										updateHeader(jomAdvancedSearch.getNotificationData());
										prepareListMember(data1, true);
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}else{
									responseErrorMessageHandler(responseCode, true);
								}
							}
						});
					}else{
                        listFooterInvisible();
                    }
				}
			}
		});

		lstFriend.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int pos, long arg3) {
				if (((HashMap<String, String>) ((SmartListItem) lstFriend.getAdapter().getItem(pos)).getValues().get(0)).get(USER_PROFILE).equalsIgnoreCase("1")) {
					gotoProfile(((HashMap<String, String>) ((SmartListItem) lstFriend.getAdapter().getItem(pos)).getValues().get(0)).get(USER_ID));
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

	/**
	 * Class method
	 */
	/**
	 * This method used to get intent data.
	 */
	@SuppressWarnings("unchecked")
	private void getIntentData() {
		IN_OPERATOR = getIntent().getStringExtra("IN_OPERATOR");
		IN_AVATARONLY = getIntent().getStringExtra("IN_AVATARONLY");
		IN_SELECTEDARRAY = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_SELECTEDARRAY");
	}


	private void getSearchResults(){
		jomAdvancedSearch.advanceSearchPostData(IN_OPERATOR, IN_AVATARONLY, IN_SELECTEDARRAY ,new WebCallListener() {

			final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
			@Override
			public void onProgressUpdate(int progressCount) {
				proSeekBar.setProgress(progressCount);

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage,
					ArrayList<HashMap<String, String>> data1, Object data2) {
				if (responseCode == 200) {
					try {
						updateHeader(jomAdvancedSearch.getNotificationData());
						prepareListMember(data1, false);
						listAdapterWithHolderMember = getMemberListAdapter();
						lstFriend.setAdapter(listAdapterWithHolderMember);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}else{
					responseErrorMessageHandler(responseCode, true);
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
		IjoomerUtilities.getCustomOkDialog(getString(R.string.search), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
				getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

			@Override
			public void NeutralMethod() {
				finish();
			}
		});
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
	 * This method used to prepare list for member data.
	 * @param data represented member data list
	 * @param append represented data append
	 */
	public void prepareListMember(ArrayList<HashMap<String, String>> data, boolean append) {
		if (data != null) {
			if (!append) {
				listDataMember.clear();
			}
			for (HashMap<String, String> hashMap : data) {
				SmartListItem item = new SmartListItem();
				item.setItemLayout(R.layout.jom_friend_list_item);
				ArrayList<Object> obj = new ArrayList<Object>();
				obj.add(hashMap);
				item.setValues(obj);
				if (append) {
					listAdapterWithHolderMember.add(item);
				} else {
					listDataMember.add(item);
				}
			}
		}
	}



	/**
	 * List adapter for member.
	 */
	private SmartListAdapterWithHolder getMemberListAdapter() {

		SmartListAdapterWithHolder adapter = new SmartListAdapterWithHolder(JomAdvanceSearchDetailActivity.this, R.layout.jom_friend_list_item, listDataMember, new ItemView() {

			@Override
			public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {
				holder.friendmembertxtName = (IjoomerTextView) v.findViewById(R.id.txtName);
				holder.friendmemberImage = (ImageView) v.findViewById(R.id.imgFriend);
				holder.friendmemberimgOnlineStatus = (ImageView) v.findViewById(R.id.imgOnlineStatus);
				holder.friendmemberimgOnlineStatus.setImageDrawable(getResources().getDrawable(R.drawable.jom_friend_member_offline));

				@SuppressWarnings("unchecked")
				final HashMap<String, String> friend = (HashMap<String, String>) item.getValues().get(0);
				holder.friendmembertxtName.setText(friend.get(USER_NAME));
				androidQuery.id(holder.friendmemberImage).image(friend.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
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
