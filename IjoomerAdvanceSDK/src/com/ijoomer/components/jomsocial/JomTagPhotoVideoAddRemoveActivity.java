package com.ijoomer.components.jomsocial;

import android.app.Activity;
import android.content.Intent;
import android.widget.RadioGroup;

import com.ijoomer.src.R;


/**
 * This Class Contains All Method Related To JomFriendListActivity.
 * 
 * @author tasol
 * 
 */
public class JomTagPhotoVideoAddRemoveActivity extends JomMasterActivity {

	private JomTagVideoFragment videoFragment;
	private JomTagPhotoFragment photoFragment;

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
        if(IN_TYPE.equals(VIDEO)){
            if (videoFragment == null) {
                videoFragment = new JomTagVideoFragment();
            }
            addFragment(R.id.lnrFragment, videoFragment);
        }else if(IN_TYPE.equals(PHOTOS)){
            if (photoFragment == null) {
                photoFragment = new JomTagPhotoFragment();
            }
            addFragment(R.id.lnrFragment, photoFragment);
        }
    }

    public void setResult(Intent intent){
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
