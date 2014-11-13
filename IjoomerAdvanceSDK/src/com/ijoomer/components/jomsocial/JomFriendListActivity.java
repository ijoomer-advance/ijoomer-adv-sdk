package com.ijoomer.components.jomsocial;

import java.util.ArrayList;
import java.util.HashMap;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To JomFriendListActivity.
 *
 * @author tasol
 *
 */
public class JomFriendListActivity extends JomMasterActivity {

    private IjoomerTextView txtFriend;
    private IjoomerTextView txtMember;
    private IjoomerTextView txtMap;
    private IjoomerTextView txtSearch;
    private IjoomerEditText editSearch;
    private ImageView imgSearch;

    private JomFriendFragment friendFragment;
    private JomMemberFragment memberFragment;
    private JomFriendMemberSearchFragment searchFragment;

    final private String FRIEND = "friend";
    final private String MEMBER = "member";
    private String CURRENT_LIST = FRIEND;


    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_friend_member;
    }

    @Override
    public void initComponents() {
        txtFriend = (IjoomerTextView) findViewById(R.id.txtFriend);
        txtMember = (IjoomerTextView) findViewById(R.id.txtMember);
        txtMap = (IjoomerTextView) findViewById(R.id.txtMap);
        editSearch = (IjoomerEditText) findViewById(R.id.editSearch);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        txtSearch = (IjoomerTextView) findViewById(R.id.txtSearch);
    }

    @Override
    public void prepareViews() {
        txtFriend.setTextColor(getResources().getColor(R.color.jom_blue));
        if (friendFragment == null) {
            friendFragment = new JomFriendFragment();
        }
        addFragment(R.id.lnrFragment, friendFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (IjoomerApplicationConfiguration.isReloadRequired()) {
            IjoomerApplicationConfiguration.setReloadRequired(false);
            if (CURRENT_LIST.equals(FRIEND)) {
                friendFragment.update();
            } else {
                memberFragment.update();
            }
        }
    }

    @Override
    public void setActionListeners() {

        txtSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    loadNew(JomAdvanceSearchActivity.class, JomFriendListActivity.this, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        txtMember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                txtFriend.setTextColor(getResources().getColor(R.color.jom_txt_color));
                txtMap.setTextColor(getResources().getColor(R.color.jom_txt_color));
                txtMember.setTextColor(getResources().getColor(R.color.jom_blue));
                editSearch.setText("");
                CURRENT_LIST = MEMBER;
                if (memberFragment == null) {
                    memberFragment = new JomMemberFragment();
                }
                addFragment(R.id.lnrFragment, memberFragment);
            }
        });

        txtFriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                txtFriend.setTextColor(getResources().getColor(R.color.jom_blue));
                txtMap.setTextColor(getResources().getColor(R.color.jom_txt_color));
                txtMember.setTextColor(getResources().getColor(R.color.jom_txt_color));
                CURRENT_LIST = FRIEND;
                if (friendFragment == null) {
                    friendFragment = new JomFriendFragment();
                }
                addFragment(R.id.lnrFragment, friendFragment);
            }
        });
        txtMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<HashMap<String,String>> mapData=new ArrayList<HashMap<String, String>>();
                if(editSearch.getText().toString().trim().length() > 0){
                    mapData.addAll(searchFragment.mapData);
                }else if(CURRENT_LIST.equals(FRIEND)){
                    mapData.addAll(friendFragment.mapData);
                }else{
                    mapData.addAll(memberFragment.mapData);
                }
                try {
                    loadNew(JomMapActivity.class, JomFriendListActivity.this, false, "IN_MAPLIST",mapData, "IN_SHOW_BUBBLE", true);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

        imgSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                hideSoftKeyboard();
                if (editSearch.getText().toString().trim().length() > 0) {
                    if (searchFragment == null) {
                        searchFragment = new JomFriendMemberSearchFragment();
                    }
                    searchFragment.setSerachKeyword(editSearch.getText().toString().trim());
                    if(CURRENT_LIST.equals(MEMBER)){
                        searchFragment.setType(MEMBERS);
                    }else{
                        searchFragment.setType(FRIENDS);
                    }
                    addFragment(R.id.lnrFragment, searchFragment);

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
            }

            @Override
            public void afterTextChanged(Editable edit) {
                if (edit.length() == 0) {
                    hideSoftKeyboard();
                    if(CURRENT_LIST.equals(MEMBER)){
                    if (memberFragment == null) {
                        memberFragment = new JomMemberFragment();
                    }
                    addFragment(R.id.lnrFragment, memberFragment);
                    }else{
                        if (friendFragment == null) {
                            friendFragment = new JomFriendFragment();
                        }
                        addFragment(R.id.lnrFragment, friendFragment);
                    }
                }
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {

    }

}
