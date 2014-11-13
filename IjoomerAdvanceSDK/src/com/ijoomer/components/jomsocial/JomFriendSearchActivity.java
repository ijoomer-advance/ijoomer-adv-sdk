package com.ijoomer.components.jomsocial;

import android.widget.RadioGroup;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To JomFriendListActivity.
 * 
 * @author tasol
 * 
 */
public class JomFriendSearchActivity extends JomMasterActivity {

	private JomSearchFriendFragment friendFragment;
	private JomSearchEventInviteFragment eventFragment;
	private JomSearchGroupInviteFragment groupFragment;

    private String IN_TYPE;

	/**
	 * Overrides methods
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_search;
	}

	@Override
	public void initComponents() {
	}

	@Override
	public void prepareViews() {
        getIntentData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void setActionListeners() {

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {

	}

    private void getIntentData(){
        IN_TYPE = getIntent().getStringExtra("IN_TYPE") != null ? getIntent().getStringExtra("IN_TYPE") :"";
        if(IN_TYPE.equals(FRIENDS)){
            if (friendFragment == null) {
                friendFragment = new JomSearchFriendFragment();
            }
            addFragment(R.id.lnrFragment, friendFragment);
        }else if(IN_TYPE.equals(EVENT)){
            if (eventFragment == null) {
                eventFragment = new JomSearchEventInviteFragment();
            }
            addFragment(R.id.lnrFragment, eventFragment);
        }else if(IN_TYPE.equals(GROUP)){
            if (groupFragment == null) {
                groupFragment = new JomSearchGroupInviteFragment();
            }
            addFragment(R.id.lnrFragment, groupFragment);
        }
    }

}
