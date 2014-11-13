package com.ijoomer.components.jomsocial;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.src.R;
import com.smart.framework.CustomAlertNeutral;

/**
 * This Class Contains All Method Related To JomAlbumsActivity.
 *
 * @author tasol
 *
 */
public class JomAlbumsActivity extends JomMasterActivity {

    private LinearLayout lnrPhotosHeader;
    private IjoomerTextView txtAllPhotos;
    private IjoomerTextView txtMyPhotos;
    private IjoomerTextView txtAddAlbum;
    private IjoomerButton btnAddAlbum;

    private JomAlbumAllFragment allAlbumFragment;
    private JomAlbumMyFragment myAlbumFragment;
    private JomAlbumAddFragment addAlbumFragment;

    final private String ALLPHOTOS = "allphotos";
    final private String MYPHOTOS = "myphotos";
    final private String ADDALBUM = "addalbum";
    private String IN_GROUP_ID;
    private String IN_PROFILE_COVER;
    private String IN_GROUP_ADD_ALBUM;
    private String currentList = MYPHOTOS;
    public boolean ADDALBUMFLAG;


    /**
     * Overrides method
     */
    @Override
    public int setLayoutId() {
        return R.layout.jom_album;
    }

    @Override
    public void initComponents() {
        lnrPhotosHeader = (LinearLayout) findViewById(R.id.lnrPhotosHeader);
        txtAllPhotos = (IjoomerTextView) findViewById(R.id.txtAllPhotos);
        txtMyPhotos = (IjoomerTextView) findViewById(R.id.txtMyPhotos);
        txtAddAlbum = (IjoomerTextView) findViewById(R.id.txtAddAlbum);
        btnAddAlbum = (IjoomerButton) findViewById(R.id.btnAddAlbum);
        getIntentData();
    }

    @Override
    public void prepareViews() {

        txtMyPhotos.setTextColor(getResources().getColor(R.color.jom_blue));

        if (!IN_GROUP_ID.equals("0")) {
            txtAllPhotos.setVisibility(View.GONE);
            txtMyPhotos.setVisibility(View.GONE);
            if (!IN_GROUP_ADD_ALBUM.equals("0")) {
                txtAddAlbum.setVisibility(View.GONE);
                btnAddAlbum.setVisibility(View.VISIBLE);
            }
            currentList = ALLPHOTOS;
            lnrPhotosHeader.setVisibility(View.GONE);
            if (allAlbumFragment == null) {
                allAlbumFragment = new JomAlbumAllFragment();
            }
            addFragment(R.id.lnrFragment, allAlbumFragment);
        } else if (!IN_PROFILE_COVER.equals("0")) {
            txtAllPhotos.setVisibility(View.GONE);
            txtMyPhotos.setVisibility(View.GONE);
            txtAddAlbum.setVisibility(View.GONE);

            currentList = MYPHOTOS;
            btnAddAlbum.setVisibility(View.VISIBLE);
            lnrPhotosHeader.setVisibility(View.GONE);
            if (myAlbumFragment == null) {
                myAlbumFragment = new JomAlbumMyFragment();
            }
            addFragment(R.id.lnrFragment, myAlbumFragment);
        } else {
            IN_PROFILE_COVER = "0";
            IN_GROUP_ID = "0";
            IN_GROUP_ADD_ALBUM = "0";
            lnrPhotosHeader.setVisibility(View.VISIBLE);
            if (myAlbumFragment == null) {
                myAlbumFragment = new JomAlbumMyFragment();
            }
            addFragment(R.id.lnrFragment, myAlbumFragment);
        }
    }

    @Override
    protected void onResume() {
        if(JomAlbumsDetailsActivity.isResume){
            JomAlbumsDetailsActivity.isResume = false;
            finish();
        }else if (IjoomerApplicationConfiguration.isReloadRequired() || JomPhotoDetailsActivity.isSetCoverChanged) {
            JomPhotoDetailsActivity.isSetCoverChanged = false;
            IjoomerApplicationConfiguration.setReloadRequired(false);
            setCurrentListData();
        }
        super.onResume();
    }

    @Override
    public void setActionListeners() {

        txtAddAlbum.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!currentList.equals(ADDALBUM)) {
                    txtAllPhotos.setTextColor(getResources().getColor(R.color.jom_txt_color));
                    txtMyPhotos.setTextColor(getResources().getColor(R.color.jom_txt_color));
                    txtAddAlbum.setTextColor(getResources().getColor(R.color.jom_blue));
                    ADDALBUMFLAG = true;
                    currentList = ADDALBUM;
                    if (IjoomerGlobalConfiguration.isPhotoUpload()) {
                        if (addAlbumFragment == null) {
                            addAlbumFragment = new JomAlbumAddFragment();
                        }
                        addFragment(R.id.lnrFragment, addAlbumFragment);
                    } else {
                        IjoomerUtilities.getCustomOkDialog(getString(R.string.album), getString(R.string.add_photo_permissin_disable), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                            @Override
                            public void NeutralMethod() {
                            }
                        });
                    }
                }
            }
        });

        btnAddAlbum.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!currentList.equals(ADDALBUM)) {
                    txtAllPhotos.setTextColor(getResources().getColor(R.color.jom_txt_color));
                    txtMyPhotos.setTextColor(getResources().getColor(R.color.jom_txt_color));
                    txtAddAlbum.setTextColor(getResources().getColor(R.color.jom_blue));
                    currentList = ADDALBUM;
                    ADDALBUMFLAG = true;
                    if (addAlbumFragment == null) {
                        addAlbumFragment = new JomAlbumAddFragment();
                    }
                    addFragment(R.id.lnrFragment, addAlbumFragment);
                }
            }
        });

        txtMyPhotos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (!currentList.equals(MYPHOTOS)) {
                    txtAllPhotos.setTextColor(getResources().getColor(R.color.jom_txt_color));
                    txtMyPhotos.setTextColor(getResources().getColor(R.color.jom_blue));
                    txtAddAlbum.setTextColor(getResources().getColor(R.color.jom_txt_color));

                    currentList = MYPHOTOS;

                    if (myAlbumFragment == null) {
                        myAlbumFragment = new JomAlbumMyFragment();
                    }
                    addFragment(R.id.lnrFragment, myAlbumFragment);
                }
            }
        });

        txtAllPhotos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!currentList.equals(ALLPHOTOS)) {
                    txtAllPhotos.setTextColor(getResources().getColor(R.color.jom_blue));
                    txtMyPhotos.setTextColor(getResources().getColor(R.color.jom_txt_color));
                    txtAddAlbum.setTextColor(getResources().getColor(R.color.jom_txt_color));

                    currentList = ALLPHOTOS;
                    if (allAlbumFragment == null) {
                        allAlbumFragment = new JomAlbumAllFragment();
                    }
                    addFragment(R.id.lnrFragment, allAlbumFragment);
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
    private void getIntentData() {
        IN_GROUP_ID = getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ID");
        IN_GROUP_ADD_ALBUM = getIntent().getStringExtra("IN_GROUP_ADD_ALBUM") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ADD_ALBUM");
        IN_PROFILE_COVER = getIntent().getStringExtra("IN_PROFILE_COVER") == null ? "0" : getIntent().getStringExtra("IN_PROFILE_COVER");
    }


    /**
     * This method used to set current list data.
     */
    private void setCurrentListData() {
        if (!IN_GROUP_ID.equals("0") || currentList.equals(ALLPHOTOS)) {
            txtAllPhotos.setTextColor(getResources().getColor(R.color.jom_blue));
            txtAddAlbum.setTextColor(getResources().getColor(R.color.jom_txt_color));
            currentList = ALLPHOTOS;
            if (ADDALBUMFLAG || !IN_GROUP_ID.equals("0")) {
                addFragment(R.id.lnrFragment, allAlbumFragment);
            } else {
                allAlbumFragment.update();
            }
        } else {
            txtMyPhotos.setTextColor(getResources().getColor(R.color.jom_blue));
            txtAddAlbum.setTextColor(getResources().getColor(R.color.jom_txt_color));
            currentList = MYPHOTOS;
            if (ADDALBUMFLAG) {
                addFragment(R.id.lnrFragment, myAlbumFragment);
            } else {
                myAlbumFragment.update();
            }
        }
        ADDALBUMFLAG = false;
    }

}
