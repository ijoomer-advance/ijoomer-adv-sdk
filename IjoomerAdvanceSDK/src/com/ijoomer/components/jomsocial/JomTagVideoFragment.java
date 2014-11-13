package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
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

/**
 * This Fragment Contains All Method Related To JomFriendMemberSearchFragment.
 *
 * @author tasol
 *
 */
@SuppressLint("ValidFragment")
public class JomTagVideoFragment extends SmartFragment implements JomTagHolder {

    private ListView lstTag;
    private LinearLayout listFooter;
    private IjoomerEditText editSearch;
    private ImageView imgSearch;

    private ProgressBar pbrTag;


    private ArrayList<SmartListItem> tagListData = new ArrayList<SmartListItem>();
    private ArrayList<SmartListItem> friendLastData= new ArrayList<SmartListItem>();
    private SmartListAdapterWithHolder adapterTag;
    private JomGalleryDataProvider providerSearchFriend;
    private JomGalleryDataProvider providerFriend;
    private JomGalleryDataProvider providerTag;
    private JomGalleryDataProvider provider;

    private String IN_VIDEO_ID;

    private boolean isSearchTextChanged=false;
    private boolean isSearchStart;

    public JomTagVideoFragment() {
    }

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_tag_video_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {
        listFooter = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.ijoomer_list_footer, null);
        lstTag = (ListView) currentView.findViewById(R.id.lstTag);
        pbrTag = (ProgressBar) currentView.findViewById(R.id.pbrTag);
        editSearch = (IjoomerEditText) currentView.findViewById(R.id.editSearch);
        imgSearch = (ImageView) currentView.findViewById(R.id.imgSearch);
        lstTag.addFooterView(listFooter, null, false);
        lstTag.setAdapter(null);

        providerSearchFriend = new JomGalleryDataProvider(getActivity());
        providerFriend = new JomGalleryDataProvider(getActivity());
        providerTag = new JomGalleryDataProvider(getActivity());
        provider = new JomGalleryDataProvider(getActivity());
    }

    @Override
    public void prepareViews(View currentView) {
        getIntentData();
        getTagList(true);
    }

    @Override
    public void setActionListeners(View currentView) {
        imgSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((IjoomerSuperMaster)getActivity()).hideSoftKeyboard();
                if (editSearch.getText().toString().trim().length() > 0) {
                    if(friendLastData.size()==0){
                        friendLastData.addAll(tagListData);
                    }
                    isSearchStart=true;
                    imgSearch.setClickable(false);
                    pbrTag.setVisibility(View.VISIBLE);
                    providerSearchFriend.restorePagingSettings();
                    providerSearchFriend.getFriendsForTagVideo(IN_VIDEO_ID, editSearch.getText().toString(), new WebCallListener() {

                        @Override
                        public void onProgressUpdate(int progressCount) {

                        }

                        @Override
                        public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                            try{
                                pbrTag.setVisibility(View.GONE);
                                imgSearch.setClickable(true);
                                if (responseCode == 200) {
                                    IjoomerApplicationConfiguration.setReloadRequired(true);
                                    prepareTagList(null,data1,false);
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
                        tagListData.clear();
                        tagListData.addAll(friendLastData);
                    }
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
                    if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
                        if (!isSearchStart) {
                            if(!providerFriend.isCalling() && providerFriend.hasNextPage()){
                                listFooter.setVisibility(View.VISIBLE);
                                providerFriend.getFriendsForTagPhoto(IN_VIDEO_ID,null, new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        try{
                                            listFooter.setVisibility(View.GONE);
                                            if (responseCode == 200) {
                                                prepareTagList(null,data1,true);
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
                                listFooter.setVisibility(View.VISIBLE);
                                providerSearchFriend.getFriendsForTagPhoto(IN_VIDEO_ID, editSearch.getText().toString(), new WebCallListener() {

                                    @Override
                                    public void onProgressUpdate(int progressCount) {

                                    }

                                    @Override
                                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                        try{
                                            listFooter.setVisibility(View.GONE);
                                            if (responseCode == 200) {
                                                prepareTagList(null,data1,true);
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

    private void getIntentData(){
        IN_VIDEO_ID = getActivity().getIntent().getStringExtra("IN_VIDEO_ID") !=null ? getActivity().getIntent().getStringExtra("IN_VIDEO_ID") :"";
    }
    /**
     * This method used to update fragment.
     */
    public void update() {
        getTagList(false);
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
    private void getTagList(final boolean isProgressShow) {
        providerTag.restorePagingSettings();
        providerTag.getVideosTages(IN_VIDEO_ID, new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {

            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage,final ArrayList<HashMap<String, String>> tagList, Object data2) {
                providerFriend.restorePagingSettings();
                providerFriend.getFriendsForTagVideo(IN_VIDEO_ID,null, new WebCallListener() {

                    @Override
                    public void onProgressUpdate(int progressCount) {

                    }

                    @Override
                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                        try{
                            pbrTag.setVisibility(View.GONE);
                            if (responseCode == 200) {
                                ((JomMasterActivity)getActivity()).updateHeader(providerTag.getNotificationData());
                                IjoomerApplicationConfiguration.setReloadRequired(true);
                                prepareTagList(tagList,data1,false);
                                adapterTag = getTagListAdapter();
                                lstTag.setAdapter(adapterTag);
                            } else {
                                IjoomerUtilities.getCustomOkDialog(getString(R.string.dialog_title_tag_user),
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
     * This method used to prepare list video tag.
     */
    private void prepareTagList(final ArrayList<HashMap<String,String>> tagList,final ArrayList<HashMap<String,String>> friendList,final boolean isAppend) {
        if(!isAppend){
            tagListData.clear();
        }
        if (tagList != null) {
            for (HashMap<String, String> hashMap : tagList) {
                SmartListItem item = new SmartListItem();
                item.setItemLayout(R.layout.jom_photo_video_tag_item);
                ArrayList<Object> obj = new ArrayList<Object>();
                hashMap.put(TAGED, "true");
                obj.add(hashMap);
                item.setValues(obj);
                tagListData.add(item);
            }
        }
        if (friendList != null) {
            for (HashMap<String, String> hashMap : friendList) {
                SmartListItem item = new SmartListItem();
                item.setItemLayout(R.layout.jom_photo_video_tag_item);
                ArrayList<Object> obj = new ArrayList<Object>();
                obj.add(hashMap);
                item.setValues(obj);
                tagListData.add(item);
            }
        }

    }

    /**
     * List adapter for video tag.
     *
     * @return represented {@link com.smart.framework.SmartListAdapterWithHolder}
     */
    private SmartListAdapterWithHolder getTagListAdapter() {

        SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.jom_photo_video_tag_item, tagListData, new ItemView() {
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

                        pbrTag.setVisibility(View.VISIBLE);
                        provider.addVideoTag(IN_VIDEO_ID, row.get(USER_ID), new WebCallListener() {

                            @Override
                            public void onProgressUpdate(int progressCount) {

                            }

                            @Override
                            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                if (responseCode == 200) {
                                    ((JomMasterActivity)getActivity()).updateHeader(provider.getNotificationData());
                                    adapterTag.clear();
                                    getTagList(true);
                                    JomVideoDetailsActivity.videoTagCount+=1;
                                } else {
                                    IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_title_tag_user),
                                            getActivity().getString(getActivity().getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getActivity().getString(R.string.ok),
                                            R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {

                                        }
                                    });
                                }
                            }
                        });
                    }
                });
                holder.btnRemovePhotoTag.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        pbrTag.setVisibility(View.VISIBLE);
                        provider.removeVideoTag(row.get(ID), new WebCallListener() {

                            @Override
                            public void onProgressUpdate(int progressCount) {

                            }

                            @Override
                            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                if (responseCode == 200) {
                                    ((JomMasterActivity)getActivity()).updateHeader(provider.getNotificationData());
                                    adapterTag.clear();
                                    getTagList(true);
                                    JomVideoDetailsActivity.videoTagCount-=1;
                                } else {
                                    IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_title_tag_user),
                                            getActivity().getString(getActivity().getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getActivity().getString(R.string.ok),
                                            R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {

                                        }
                                    });
                                }
                            }
                        });
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
