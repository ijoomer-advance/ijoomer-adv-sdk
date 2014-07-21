package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
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
public class JomTagPhotoFragment extends SmartFragment implements JomTagHolder {

    private ListView lstTag;
    private LinearLayout listFooter;
    private LinearLayout lnrFriendSearch;
    private IjoomerEditText editSearch;
    private ImageView imgSearch;
    private ProgressBar pbrTag;


    private ArrayList<SmartListItem> friendListData = new ArrayList<SmartListItem>();
    private ArrayList<SmartListItem> friendLastData= new ArrayList<SmartListItem>();
    private SmartListAdapterWithHolder adapterTag;
    private JomGalleryDataProvider providerSearchFriend;
    private JomGalleryDataProvider providerFriend;

    private String IN_PHOTO_ID;
    private String IN_TAG_TYPE;
    private ArrayList<HashMap<String,String>> IN_PHOTO_REMOVE_TAG_LIST;

    private boolean isSearchTextChanged=false;
    private boolean isSearchStart;
    private boolean isRemoveTag;

    public JomTagPhotoFragment() {
    }

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_tag_photo_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {
        listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
        lstTag = (ListView) currentView.findViewById(R.id.lstTag);
        lnrFriendSearch = (LinearLayout) currentView.findViewById(R.id.lnrFriendSearch);
        editSearch = (IjoomerEditText) currentView.findViewById(R.id.editSearch);
        imgSearch = (ImageView) currentView.findViewById(R.id.imgSearch);
        pbrTag = (ProgressBar) currentView.findViewById(R.id.pbrTag);
        lstTag.addFooterView(listFooter, null, false);
        lstTag.setAdapter(null);

        providerSearchFriend = new JomGalleryDataProvider(getActivity());
        providerFriend = new JomGalleryDataProvider(getActivity());
    }

    @Override
    public void prepareViews(View currentView) {
        getIntentData();
    }

    @Override
    public void setActionListeners(View currentView) {
        imgSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((IjoomerSuperMaster)getActivity()).hideSoftKeyboard();
                if (editSearch.getText().toString().trim().length() > 0) {
                    if(friendLastData.size()==0){
                        friendLastData.addAll(friendListData);
                    }
                    isSearchStart=true;
                    imgSearch.setClickable(false);
                    pbrTag.setVisibility(View.VISIBLE);
                    providerSearchFriend.restorePagingSettings();
                    providerSearchFriend.getFriendsForTagPhoto(IN_PHOTO_ID, editSearch.getText().toString(), new WebCallListener() {

                        @Override
                        public void onProgressUpdate(int progressCount) {

                        }

                        @Override
                        public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                            try{
                                imgSearch.setClickable(true);
                                pbrTag.setVisibility(View.GONE);
                                if (responseCode == 200) {
                                    IjoomerApplicationConfiguration.setReloadRequired(true);
                                    if(IN_TAG_TYPE.equals("add")){
                                        prepareTagList(data1, isRemoveTag, false);
                                    }
                                    adapterTag = getTagListAdapter();
                                    lstTag.setAdapter(adapterTag);
                                } else {
                                    IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_title_tag_user),
                                            getActivity().getString(getActivity().getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getActivity().getString(R.string.ok),
                                            R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {
                                        }
                                    });
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    editSearch.setError(getString(R.string.validation_value_required));
                }
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence c, int arg1, int arg2, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                isSearchTextChanged=true;
            }

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length() == 0) {
                    ((IjoomerSuperMaster)getActivity()).hideSoftKeyboard();
                    if(friendLastData.size()>0){
                        friendListData.clear();
                        friendListData.addAll(friendLastData);
                    }
                    friendLastData.clear();
                    adapterTag = getTagListAdapter();
                    lstTag.setAdapter(adapterTag);
                    isSearchStart=false;
                }
            }
        });

        lstTag.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(isSearchTextChanged){
                    isSearchTextChanged=false;
                }else{
                    if ((!isRemoveTag) && (firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
                        if (!isSearchStart) {
                            if(!providerFriend.isCalling() && providerFriend.hasNextPage()){
                                listFooter.setVisibility(View.VISIBLE);
                                providerFriend.getFriendsForTagPhoto(IN_PHOTO_ID,null, new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        try{
                                            listFooter.setVisibility(View.GONE);
                                            if (responseCode == 200) {
                                                prepareTagList(data1, isRemoveTag, true);
                                            } else {
                                                responseErrorMessageHandler(responseCode,false);
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        } else {
                            if(!providerSearchFriend.isCalling() && providerSearchFriend.hasNextPage()){
                                providerSearchFriend.getFriendsForTagPhoto(IN_PHOTO_ID, editSearch.getText().toString(), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        try{
                                            listFooter.setVisibility(View.GONE);
                                            if (responseCode == 200) {
                                                prepareTagList(data1, isRemoveTag, true);
                                            } else {
                                                responseErrorMessageHandler(responseCode,false);
                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

    }

    /**
     * Class methods
     */

    @SuppressWarnings("unchecked")
	private void getIntentData(){
        IN_PHOTO_ID = getActivity().getIntent().getStringExtra("IN_PHOTO_ID") !=null ? getActivity().getIntent().getStringExtra("IN_PHOTO_ID") :"";
        IN_TAG_TYPE = getActivity().getIntent().getStringExtra("IN_TAG_TYPE") !=null ? getActivity().getIntent().getStringExtra("IN_TAG_TYPE") :"";
        IN_PHOTO_REMOVE_TAG_LIST = ((ArrayList<HashMap<String,String>>) getActivity().getIntent().getSerializableExtra("IN_PHOTO_REMOVE_TAG_LIST")) !=null ? (ArrayList<HashMap<String,String>>) getActivity().getIntent().getSerializableExtra("IN_PHOTO_REMOVE_TAG_LIST") : new ArrayList<HashMap<String, String>>();

        if(IN_TAG_TYPE.equals("add")){
            lnrFriendSearch.setVisibility(View.VISIBLE);
            isRemoveTag=false;
            loadPhotoTagsFriends(true);
        }else{
            lnrFriendSearch.setVisibility(View.GONE);
            isRemoveTag=true;
            loadPhotoTagsRemove(IN_PHOTO_REMOVE_TAG_LIST);
        }
    }
    /**
     * This method used to update fragment.
     */
    public void update() {
        if(isRemoveTag){
            loadPhotoTagsRemove(IN_PHOTO_REMOVE_TAG_LIST);
        }else{
            loadPhotoTagsFriends(false);
        }
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
     * This method used to load photo tag.
     */
    private void loadPhotoTagsFriends(boolean isProgressShow) {
        if(isProgressShow){
            pbrTag.setVisibility(View.VISIBLE);
        }
        providerFriend.restorePagingSettings();
        providerFriend.getFriendsForTagPhoto(IN_PHOTO_ID, null, new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {

            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                try{
                    pbrTag.setVisibility(View.GONE);
                    if (responseCode == 200) {
                        prepareTagList(data1, isRemoveTag, false);
                        adapterTag = getTagListAdapter();
                        lstTag.setAdapter(adapterTag);
                    } else {
                        responseErrorMessageHandler(responseCode, false);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This method used to load photo tag.
     */
    private void loadPhotoTagsRemove(ArrayList<HashMap<String,String>> data) {
        prepareTagList(data, true, false);
        adapterTag = getTagListAdapter();
        lstTag.setAdapter(adapterTag);
        pbrTag.setVisibility(View.GONE);
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
     * This method used to prepare list photo tag.
     *
     * @param data
     *            represented photo tag data
     * @param isRemoveTag
     *            represented tag remove
     * @param append
     *            represented tag data append
     */
    private void prepareTagList(ArrayList<HashMap<String, String>> data, boolean isRemoveTag, boolean append) {

        if (data != null) {
            if (isRemoveTag) {
                friendListData.clear();
                for (HashMap<String, String> hashMap : data) {
                    SmartListItem item = new SmartListItem();
                    item.setItemLayout(R.layout.jom_photo_video_tag_item);
                    ArrayList<Object> obj = new ArrayList<Object>();
                    hashMap.put("taged", "true");
                    obj.add(hashMap);
                    item.setValues(obj);
                    friendListData.add(item);
                }
            } else {
                if (!append) {
                    friendListData.clear();
                }
                for (HashMap<String, String> hashMap : data) {
                    SmartListItem item = new SmartListItem();
                    item.setItemLayout(R.layout.jom_photo_video_tag_item);
                    ArrayList<Object> obj = new ArrayList<Object>();
                    obj.add(hashMap);
                    item.setValues(obj);
                    if (append) {
                        adapterTag.add(item);
                    } else {
                        friendListData.add(item);
                    }
                }

            }
        }

    }

    /**
     * List adapter for photo tag.
     *
     * @return
     */

    private SmartListAdapterWithHolder getTagListAdapter() {

        SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_photo_video_tag_item, friendListData, new ItemView() {
            @Override
            public View setItemView(final int position, View v, SmartListItem item, ViewHolder holder) {
                holder.txtPhotoTagUser = (IjoomerTextView) v.findViewById(R.id.txtPhotoTagUser);
                holder.btnPhotoTag = (IjoomerButton) v.findViewById(R.id.btnPhotoTag);
                holder.btnRemovePhotoTag = (IjoomerButton) v.findViewById(R.id.btnRemovePhotoTag);

                @SuppressWarnings("unchecked")
                final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

                if (row.containsKey(TAGED) && row.get(DELETEALLOWED).equals("1")) {
                    holder.btnPhotoTag.setVisibility(View.GONE);
                    holder.btnRemovePhotoTag.setVisibility(View.VISIBLE);
                } else if (!row.containsKey(TAGED)) {
                    holder.btnPhotoTag.setVisibility(View.VISIBLE);
                    holder.btnRemovePhotoTag.setVisibility(View.GONE);
                }

                holder.txtPhotoTagUser.setText(row.get(USER_NAME));

                holder.txtPhotoTagUser.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (row.get(USER_PROFILE).equals("1")) {
                            ((JomMasterActivity)getActivity()).gotoProfile(row.get(USER_ID));
                        }
                    }
                });

                holder.btnPhotoTag.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("IN_USER_ID", row.get(USER_ID));
                        ((JomTagPhotoVideoAddRemoveActivity)getActivity()).setResult(intent);
                    }
                });
                holder.btnRemovePhotoTag.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("IN_TAG_ID",row.get(ID));
                        intent.putExtra("IN_TAG_POSITION",position);
                        ((JomTagPhotoVideoAddRemoveActivity)getActivity()).setResult(intent);
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
