package com.ijoomer.components.easyblog;

import android.view.View;

import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.src.R;

/**
 * This Class Contains All Method Related To EasyBlogEntriesActivity.
 * 
 * @author tasol
 * 
 */
public class EasyBlogEntriesActivity extends EasyBlogMasterActivity {

	private IjoomerButton btnAddBlog;
    private EasyBlogEntriesFragment easyBlogEntriesFragment;
    /**
	 * Overrides method
	 */
	@Override
	public int setLayoutId() {
		return R.layout.easyblog_entries;
	}

	@Override
	public void initComponents() {
        btnAddBlog = (IjoomerButton) findViewById(R.id.btnAddBlog);
        if(getSmartApplication().readSharedPreferences().getString(SP_LOGIN_REQ_OBJECT, "").length() > 0){
            btnAddBlog.setVisibility(View.VISIBLE);
        }
	}

	@Override
	public void prepareViews() {
        if(easyBlogEntriesFragment==null){
            easyBlogEntriesFragment = new EasyBlogEntriesFragment();
        }
		addFragment(R.id.lnrFragment, easyBlogEntriesFragment);

	}

	@Override
	public void setActionListeners() {
        btnAddBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNew(EasyBlogAddBlogActivity.class,EasyBlogEntriesActivity.this,false);
            }
        });
	}

    @Override
    protected void onResume() {
        if(IjoomerApplicationConfiguration.isReloadRequired()){
            IjoomerApplicationConfiguration.setReloadRequired(false);
            if(easyBlogEntriesFragment==null){
                easyBlogEntriesFragment = new EasyBlogEntriesFragment();
            }
            easyBlogEntriesFragment.update();
        }
        super.onResume();
    }
}