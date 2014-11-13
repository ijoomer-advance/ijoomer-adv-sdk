package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.ijoomer.library.jomsocial.JomEventDataProvider;
import com.ijoomer.library.jomsocial.JomFriendsDataProvider;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This Fragment Contains All Method Related To JomFriendMemberSearchFragment.
 *
 * @author tasol
 *
 */
@SuppressLint("ValidFragment")
public class JomSearchEventInviteFragment extends SmartFragment implements JomTagHolder {

    private ListView lstFriend;
    private LinearLayout listFooter;
    private IjoomerEditText editSearch;
    private ImageView imgSearch;
    private IjoomerRadioButton rdbSelectAll;
    private IjoomerRadioButton rdbSelectNone;
    private IjoomerButton btnCancel;
    private IjoomerButton btnDone;
    private SeekBar proSeekBar;

    private ArrayList<SmartListItem> listData = new ArrayList<SmartListItem>();
    private HashMap<String, String> selectedFriends = new HashMap<String, String>();
    private ArrayList<SmartListItem> friendDara= new ArrayList<SmartListItem>();
    private SmartListAdapterWithHolder adapterFriend;
    private JomFriendsDataProvider providerSearchFriend;
    private JomEventDataProvider providerFriend;
    private AQuery androidQuery;

    private final String SELECTEDFRIEND="selectedFriend";
    private final String SELECTEDFRIENDIDS="selectedFriendIds";
    private boolean IN_ISMULTIPLEALLOW;
    private String IN_EVENT_ID;
    private boolean isSearchTextChanged=false;
    private boolean isSearchStart;

    public JomSearchEventInviteFragment() {
    }

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_search_friend_event_invite_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {
        listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
        lstFriend = (ListView) currentView.findViewById(R.id.lstFriend);
        editSearch = (IjoomerEditText) currentView.findViewById(R.id.editSearch);
        imgSearch = (ImageView) currentView.findViewById(R.id.imgSearch);
        rdbSelectAll = (IjoomerRadioButton) currentView.findViewById(R.id.rdbSelectAll);
        rdbSelectNone = (IjoomerRadioButton) currentView.findViewById(R.id.rdbSelectNone);
        btnCancel = (IjoomerButton) currentView.findViewById(R.id.btnCancel);
        btnDone = (IjoomerButton) currentView.findViewById(R.id.btnDone);

        lstFriend.addFooterView(listFooter, null, false);
        lstFriend.setAdapter(null);

        androidQuery = new AQuery(getActivity());
        providerSearchFriend = new JomFriendsDataProvider(getActivity());
        providerFriend = new JomEventDataProvider(getActivity());

    }

    @Override
    public void prepareViews(View currentView) {
        getIntentData();

    }

    @Override
    public void setActionListeners(View currentView) {
        lstFriend.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(isSearchTextChanged){
                    isSearchTextChanged=false;
                }else{
                    if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
                        if(isSearchStart){
                            if (!providerSearchFriend.isCalling() && providerSearchFriend.hasNextPage()) {
                                listFooterVisible();
                               /* providerSearchFriend.getSearchFriendList(editSearch.getText().toString(), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        try {
                                            listFooterInvisible();
                                            ((JomMasterActivity)getActivity()).updateHeader(providerSearchFriend.getNotificationData());
                                            for (HashMap<String, String> hashMap : data1) {
                                                hashMap.put("isChecked", "false");
                                            }
                                            prepareList(data1, true);
                                        } catch (Throwable e) {
                                        }
                                    }
                                });*/
                            }
                        }else{
                            if (!providerFriend.isCalling() && providerFriend.hasNextPage()) {
                                listFooterVisible();
                                providerFriend.getInviteFriendList(IN_EVENT_ID,new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        try {
                                            listFooterInvisible();
                                            ((JomMasterActivity)getActivity()).updateHeader(providerSearchFriend.getNotificationData());
                                            for (HashMap<String, String> hashMap : data1) {
                                                hashMap.put("isChecked", "false");
                                            }
                                            prepareList(data1, true);
                                        } catch (Throwable e) {
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedFriends = "";
                String selectedFriendIDS = "";

                for (Iterator<String> iterator = JomSearchEventInviteFragment.this.selectedFriends.keySet().iterator(); iterator.hasNext();) {
                    String type = iterator.next();

                    if (iterator.hasNext()) {
                        selectedFriends += JomSearchEventInviteFragment.this.selectedFriends.get(type) + ",";
                        selectedFriendIDS += type + ",";
                    } else {
                        selectedFriends += JomSearchEventInviteFragment.this.selectedFriends.get(type);
                        selectedFriendIDS += type;
                    }

                }
                Intent intent =  new Intent();
                intent.putExtra(SELECTEDFRIEND,selectedFriends);
                intent.putExtra(SELECTEDFRIENDIDS,selectedFriendIDS);
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
			@Override
            public void onClick(View view) {
                if(editSearch.getText().toString().trim().length()>0){
                    isSearchStart=true;
                    friendDara.clear();
                    for (SmartListItem item : listData){
                        ((HashMap<String,String>)item.getValues().get(0)).put("isChecked","false");
                    }
                    friendDara.addAll(listData);
                    getSearchFriend(true);

                }else{
                    editSearch.setError(getActivity().getString(R.string.validation_value_required));
                }
            }
        });


        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                isSearchTextChanged =true;
                isSearchStart=false;
            }

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length() == 0) {
                    isSearchStart=false;
                    ((IjoomerSuperMaster)getActivity()).hideSoftKeyboard();
                    listData.clear();
                    listData.addAll(friendDara);
                    adapterFriend = getFriendListAdapter();
                    lstFriend.setAdapter(adapterFriend);
                    rdbSelectAll.setChecked(false);
                    rdbSelectAll.setChecked(false);
                }
            }
        });

        rdbSelectAll.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View arg0) {
                if (listData != null) {
                    rdbSelectNone.setChecked(false);
                    int size = listData.size();
                    for (int i = 0; i < size; i++) {
                        ((HashMap<String, String>) ((SmartListItem) listData.get(i)).getValues().get(0)).put("isChecked", "true");
                    }
                    adapterFriend.notifyDataSetChanged();
                }
            }
        });
        rdbSelectNone.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                if (adapterFriend != null) {
                    rdbSelectAll.setChecked(false);
                    int size = listData.size();
                    for (int i = 0; i < size; i++) {
                        ((HashMap<String, String>) ((SmartListItem) listData.get(i)).getValues().get(0)).put("isChecked", "false");
                    }
                    adapterFriend.notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * Class methods
     */

    private void getIntentData(){
        IN_ISMULTIPLEALLOW = getActivity().getIntent().getBooleanExtra("IN_ISMULTIPLEALLOW",true);
        IN_EVENT_ID = getActivity().getIntent().getStringExtra("IN_EVENT_ID") != null ? getActivity().getIntent().getStringExtra("IN_EVENT_ID") :"0" ;
        if(!IN_ISMULTIPLEALLOW){
            rdbSelectNone.setVisibility(View.GONE);
            rdbSelectAll.setVisibility(View.GONE);
        }
        getFriendList(true);
    }


    /**
     * This method used to update fragment.
     */
    public void update() {
        getFriendList(false);
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
    private void getSearchFriend(final boolean isProgressShow) {
        providerSearchFriend.restorePagingSettings();
        if (isProgressShow) {
            proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
        }
      /*  providerSearchFriend.getSearchFriendList(editSearch.getText().toString(), new WebCallListener() {

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
                        rdbSelectAll.setChecked(false);
                        rdbSelectAll.setChecked(false);
                        ((JomMasterActivity)getActivity()).updateHeader(providerSearchFriend.getNotificationData());
                        for (HashMap<String, String> hashMap : data1) {
                            hashMap.put("isChecked", "false");
                        }
                        prepareList(data1, false);
                        adapterFriend = getFriendListAdapter();
                        lstFriend.setAdapter(adapterFriend);
                    } else {
                        responseErrorMessageHandler(responseCode, false);
                    }
                } catch (Throwable e) {
                }
            }
        });*/
    }

    /**
     * This method used to get search member.
     * @param isProgressShow represented progress shown
     */
    private void getFriendList(final boolean isProgressShow) {
        providerFriend.restorePagingSettings();
        if (isProgressShow) {
            proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
        }
        providerFriend.getInviteFriendList(IN_EVENT_ID,new WebCallListener() {

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
                        ((JomMasterActivity)getActivity()).updateHeader(providerSearchFriend.getNotificationData());
                        String IN_SELECTEDFRIENDIDS = getActivity().getIntent().getStringExtra("IN_SELECTEDFRIENDIDS") != null ? getActivity().getIntent().getStringExtra("IN_SELECTEDFRIENDIDS") : "";
                        String[] friendIdArray =null;
                        if(isProgressShow && IN_SELECTEDFRIENDIDS.trim().length() > 0){
                            friendIdArray = IN_SELECTEDFRIENDIDS.split(",");
                        }
                        for (HashMap<String, String> hashMap : data1) {
                            if(friendIdArray!=null && friendIdArray.length > 0){
                                for (String friendId : friendIdArray){
                                    if(hashMap.get(USER_ID).equals(friendId)){
                                        hashMap.put("isChecked", "true");
                                        break;
                                    }else{
                                        hashMap.put("isChecked", "false");
                                    }
                                }
                            }else{
                                hashMap.put("isChecked", "false");
                            }
                            if(friendIdArray!=null && friendIdArray.length == data1.size()){
                                rdbSelectAll.setChecked(true);
                            }
                        }

                        prepareList(data1, false);
                        adapterFriend = getFriendListAdapter();
                        lstFriend.setAdapter(adapterFriend);
                    } else {
                        if(isProgressShow){
                            rdbSelectAll.setVisibility(View.GONE);
                            rdbSelectNone.setVisibility(View.GONE);
                            btnDone.setVisibility(View.GONE);
                        }
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
     * This method used to prepare list friend.
     * @param data represented friend data
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
     * List adapter for friend.
     */

    private SmartListAdapterWithHolder getFriendListAdapter() {
        SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_friend_list_item, listData, new ItemView() {

            @Override
            public View setItemView(int position, View v, SmartListItem item, ViewHolder holder) {

                holder.friendmembertxtName = (IjoomerTextView) v.findViewById(R.id.txtName);
                holder.friendmemberImage = (ImageView) v.findViewById(R.id.imgFriend);
                holder.friendmemberimgOnlineStatus = (ImageView) v.findViewById(R.id.imgOnlineStatus);
                holder.chkSelectFriend = (IjoomerCheckBox) v.findViewById(R.id.chkSelectFriend);
                holder.chkSelectFriend.setVisibility(View.VISIBLE);
                holder.friendmemberimgOnlineStatus.setVisibility(View.GONE);
                @SuppressWarnings("unchecked")
                final HashMap<String, String> friend = (HashMap<String, String>) item.getValues().get(0);

                holder.chkSelectFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @SuppressWarnings("unchecked")
					@Override
                    public void onCheckedChanged(CompoundButton ButtonView, boolean isChecked) {
                        if (isChecked) {
                            if(!IN_ISMULTIPLEALLOW){
                                selectedFriends.clear();
                                for (SmartListItem item : listData ){
                                    ((HashMap<String, String>)item.getValues().get(0)).put("isChecked", "false");
                                }
                            }
                            selectedFriends.put(friend.get(USER_ID), friend.get(USER_NAME));
                        } else {
                            selectedFriends.remove(friend.get(USER_ID));
                        }
                        if(selectedFriends.size()==0){
                            rdbSelectNone.setChecked(true);
                        }else if(selectedFriends.size() == listData.size()){
                            rdbSelectAll.setChecked(true);
                        }else{
                            rdbSelectAll.setChecked(false);
                            rdbSelectNone.setChecked(false);
                        }
                        friend.put("isChecked", "" + isChecked);
                        adapterFriend.notifyDataSetChanged();
                    }
                });

                holder.chkSelectFriend.setChecked(Boolean.parseBoolean(friend.get("isChecked").toString()));
                holder.friendmembertxtName.setText(friend.get(USER_NAME));
                androidQuery.id(holder.friendmemberImage).image(friend.get(USER_AVATAR), true, true, ((SmartActivity)getActivity()).getDeviceWidth(), 0);
                if (friend.containsKey(USER_ONLINE) && friend.get(USER_ONLINE).equalsIgnoreCase("1")) {
                    holder.friendmemberimgOnlineStatus.setImageResource(R.drawable.jom_friend_member_online);
                } else {
                    holder.friendmemberimgOnlineStatus.setImageResource(R.drawable.jom_friend_member_offline);
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

}
