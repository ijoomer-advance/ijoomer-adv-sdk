package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.SelectImageDialogListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To JomVideoAddFragment.
 *
 * @author tasol
 *
 */
@SuppressLint("ValidFragment")
public class JomVideoAddFragment extends SmartFragment implements JomTagHolder {

    private LinearLayout lnrUploadVideo;
    private LinearLayout lnrLinkVideo;
    private IjoomerTextView txtUploadVideo;
    private IjoomerTextView txtLinkVideo;
    private IjoomerTextView txtVideoWhoCanSee;
    @SuppressWarnings("unused")
    private IjoomerTextView txtVideoCategory;
    private IjoomerEditText edtVideoFile;
    private IjoomerEditText edtVideoLink;
    private IjoomerEditText edtVideoTitle;
    private IjoomerEditText edtVideoDescription;
    private IjoomerEditText edtVideoCaption;
    private IjoomerEditText edtVideoLocation;
    private IjoomerButton btnBrowse;
    private IjoomerButton btnUpload;
    private IjoomerButton btnCancle;
    private ImageView imgMap;
    private Spinner spnWhoCanSee;
    private Spinner spnVideoCategory;

    private ArrayList<HashMap<String, String>> categoryList;
    private ArrayList<String> categories;
    private JomGalleryDataProvider providerVideo;

    private String IN_GROUP_ID;
    private String IN_PROFILE;
    private String videoPath;
    private String videoPathForTrimming;
    private int PICK_VIDEO=2;
    private int TAKE_VIDEO=3;
    final private int GET_ADDRESS_FROM_MAP = 1;

    public JomVideoAddFragment() {
    }

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_video_add_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {

        lnrUploadVideo = (LinearLayout) currentView.findViewById(R.id.lnrUploadVideo);
        lnrLinkVideo = (LinearLayout) currentView.findViewById(R.id.lnrLinkVideo);
        txtUploadVideo = (IjoomerTextView) currentView.findViewById(R.id.txtUploadVideo);
        txtLinkVideo = (IjoomerTextView) currentView.findViewById(R.id.txtLinkVideo);
        txtVideoWhoCanSee = (IjoomerTextView) currentView.findViewById(R.id.txtVideoWhoCanSee);
        txtVideoCategory = (IjoomerTextView) currentView.findViewById(R.id.txtVideoCategory);
        edtVideoFile = (IjoomerEditText) currentView.findViewById(R.id.edtVideoFile);
        edtVideoLink = (IjoomerEditText) currentView.findViewById(R.id.edtVideoLink);
        edtVideoTitle = (IjoomerEditText) currentView.findViewById(R.id.edtVideoTitle);
        edtVideoDescription = (IjoomerEditText) currentView.findViewById(R.id.edtVideoDescription);
        edtVideoCaption = (IjoomerEditText) currentView.findViewById(R.id.edtVideoCaption);
        edtVideoLocation = (IjoomerEditText) currentView.findViewById(R.id.edtVideoLocation);
        btnCancle = (IjoomerButton) currentView.findViewById(R.id.btnCancle);
        btnUpload = (IjoomerButton) currentView.findViewById(R.id.btnUpload);
        btnBrowse = (IjoomerButton) currentView.findViewById(R.id.btnBrowse);
        imgMap = (ImageView) currentView.findViewById(R.id.imgMap);
        spnWhoCanSee = (Spinner) currentView.findViewById(R.id.spnWhoCanSee);
        spnVideoCategory = (Spinner) currentView.findViewById(R.id.spnVideoCategory);

        providerVideo = new JomGalleryDataProvider(getActivity());

        categories = new ArrayList<String>();
        categoryList = new ArrayList<HashMap<String, String>>();

        getIntentData();
    }

    @Override
    public void prepareViews(View currentView) {

        edtVideoCaption.setText("");
        edtVideoDescription.setText("");
        edtVideoFile.setText("");
        edtVideoLink.setText("");
        edtVideoTitle.setText("");

        edtVideoFile.setHint(String.format(getString(R.string.videos_select_file), IjoomerGlobalConfiguration.getVideoUploadSize()));
        spnWhoCanSee.setAdapter(new IjoomerUtilities.MyCustomAdapter(getActivity(), new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)))));

        if (!IjoomerGlobalConfiguration.isVideoUpload()) {
            txtLinkVideo.setTextColor(Color.parseColor(getString(R.color.blue)));
            lnrLinkVideo.setVisibility(View.VISIBLE);
            txtUploadVideo.setVisibility(View.GONE);
            if (!IN_GROUP_ID.equals("0")) {
                txtVideoWhoCanSee.setVisibility(View.GONE);
                spnWhoCanSee.setVisibility(View.GONE);
            }
            lnrUploadVideo.setVisibility(View.GONE);
            edtVideoTitle.setVisibility(View.GONE);
            edtVideoDescription.setVisibility(View.GONE);
        }
        if (IN_PROFILE.equals("1")) {
            edtVideoCaption.setVisibility(View.VISIBLE);
        }
        providerVideo.getVideoCategoryList(new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {
            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                try {
                    if (responseCode == 200) {
                        ((JomMasterActivity) getActivity()).updateHeader(providerVideo.getNotificationData());
                        categoryList.addAll(data1);
                        for (HashMap<String, String> hashMap : data1) {
                            categories.add(hashMap.get(NAME));
                        }
                        spnVideoCategory.setAdapter(new IjoomerUtilities.MyCustomAdapter(getActivity(), categories));
                    } else if (responseCode != 204) {
                        responseErrorMessageHandler(responseCode, false);
                    }
                } catch (Throwable e) {
                }

            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    final String address = IjoomerUtilities.getAddressFromLatLong(0, 0).getSubAdminArea();

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            edtVideoLocation.setText(address);
                        }
                    });
                } catch (Throwable e) {
                }
            }
        }).start();
    }

    @Override
    public void setActionListeners(View currentView) {
        imgMap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), IjoomerMapAddress.class);
                startActivityForResult(intent, GET_ADDRESS_FROM_MAP);
            }
        });

        btnUpload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ((IjoomerSuperMaster) getActivity()).hideSoftKeyboard();
                if (lnrUploadVideo.getVisibility() == View.VISIBLE) {
                    boolean validationFlag = true;

                    if (edtVideoFile.getText().toString().trim().length() <= 0) {
                        validationFlag = false;
                        edtVideoFile.setError(getString(R.string.validation_value_required));
                    }
                    if (edtVideoTitle.getText().toString().trim().length() <= 0) {
                        edtVideoTitle.setError(getString(R.string.validation_value_required));
                        validationFlag = false;
                    }

                    if (validationFlag) {

                        if ((new File(videoPath).length() / (1024 * 1024)) > IjoomerGlobalConfiguration.getVideoUploadSize()) {
                            IjoomerUtilities.getCustomOkCancelDialog(getString(R.string.video), getString(R.string.video_select_size_limit_exceeded), getString(R.string.yes),
                                    getString(R.string.no), R.layout.ijoomer_ok_cancel_dialog, new CustomAlertNeutral() {

                                @Override
                                public void NeutralMethod() {
                                    videoPathForTrimming = edtVideoFile.getText().toString();
                                    edtVideoFile.setText(null);

                                    Address address = IjoomerUtilities.getLatLongFromAddress(edtVideoLocation.getText().toString().trim());
                                    String IN_VIDEO_FILE = edtVideoFile.getText().toString().trim();
                                    String IN_VIDEO_TITLE = edtVideoTitle.getText().toString().trim();
                                    String IN_VIDEO_DESCRIPTION = edtVideoDescription.getText().toString().trim();
                                    String IN_VIDEO_CAPTION = edtVideoCaption.getText().toString();
                                    String IN_LOCATION = edtVideoLocation.getText().toString();
                                    double IN_LATITUDE = address != null ? address.getLatitude() : 0;
                                    double IN_LONGITUDE = address != null ? address.getLongitude() : 0;
                                    String IN_CAT_ID = getCategoryId(spnVideoCategory.getSelectedItemPosition());
                                    String IN_PRIVACY = (!IN_GROUP_ID.equals("0") ? null : ((JomMasterActivity) getActivity()).getPrivacyCode(
                                            spnWhoCanSee.getSelectedItem().toString().trim()).toString());
                                    edtVideoDescription.setText(null);
                                    edtVideoTitle.setText(null);

                                    try {
                                        ((SmartActivity) getActivity()).loadNew(JomVideoTrimmerActivity.class, getActivity(), false, "IN_VIDEO_PATH_FOR_TRIMMING",
                                                videoPathForTrimming, "IN_GROUP_ID", IN_GROUP_ID, "IN_CAT_ID", IN_CAT_ID, "IN_VIDEO_FILE", IN_VIDEO_FILE, "IN_VIDEO_TITLE",
                                                IN_VIDEO_TITLE, "IN_VIDEO_CAPTION", IN_VIDEO_CAPTION,"IN_VIDEO_DESCRIPTION",IN_VIDEO_DESCRIPTION,"IN_LATITUDE", IN_LATITUDE, "IN_LONGITUDE", IN_LONGITUDE,
                                                "IN_PRIVACY", IN_PRIVACY,"IN_LOCATION",IN_LOCATION);
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } else {
                            Address address = IjoomerUtilities.getLatLongFromAddress(edtVideoLocation.getText().toString().trim());
                            startVideoUpload(IN_GROUP_ID, edtVideoFile.getText().toString().trim(), edtVideoTitle.getText().toString().trim(), edtVideoDescription.getText()
                                    .toString().trim(), edtVideoCaption.getText().toString(), address != null ? address.getLatitude() : 0, address != null ? address.getLongitude()
                                    : 0, getCategoryId(spnVideoCategory.getSelectedItemPosition()), !IN_GROUP_ID.equals("0") ? null : ((JomMasterActivity) getActivity())
                                    .getPrivacyCode(spnWhoCanSee.getSelectedItem().toString().trim()).toString());
                        }
                    }

                } else {
                    if (edtVideoLink.getText().toString().trim().length() <= 0) {
                        edtVideoLink.setError(getString(R.string.validation_value_required));
                    } else {
                        Address address = IjoomerUtilities.getLatLongFromAddress(edtVideoLocation.getText().toString().trim());
                        startVideoLinking(IN_GROUP_ID, edtVideoLink.getText().toString().trim(), edtVideoCaption.getText().toString().trim(),
                                address != null ? address.getLatitude() : 0, address != null ? address.getLongitude() : 0,
                                getCategoryId(spnVideoCategory.getSelectedItemPosition()),
                                !IN_GROUP_ID.equals("0") ? null : ((JomMasterActivity) getActivity()).getPrivacyCode(spnWhoCanSee.getSelectedItem().toString().trim()).toString());

                    }
                }

            }
        });
        btnCancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!IN_PROFILE.equals("0")) {
                    getActivity().finish();
                } else {
                    IjoomerApplicationConfiguration.setReloadRequired(true);
                    ((JomVideoActivity) getActivity()).onResume();
                }
            }
        });

        txtUploadVideo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                txtLinkVideo.setTextColor(Color.parseColor(getString(R.color.txt_color)));
                txtUploadVideo.setTextColor(Color.parseColor(getString(R.color.blue)));
                lnrLinkVideo.setVisibility(View.GONE);
                lnrUploadVideo.setVisibility(View.VISIBLE);
                if (!IN_GROUP_ID.equals("0")) {
                    txtVideoWhoCanSee.setVisibility(View.GONE);
                    spnWhoCanSee.setVisibility(View.GONE);
                }
                edtVideoTitle.setVisibility(View.VISIBLE);
                edtVideoDescription.setVisibility(View.VISIBLE);
            }
        });

        txtLinkVideo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                txtLinkVideo.setTextColor(Color.parseColor(getString(R.color.blue)));
                txtUploadVideo.setTextColor(Color.parseColor(getString(R.color.txt_color)));
                lnrLinkVideo.setVisibility(View.VISIBLE);
                if (!IN_GROUP_ID.equals("0")) {
                    txtVideoWhoCanSee.setVisibility(View.GONE);
                    spnWhoCanSee.setVisibility(View.GONE);
                }
                lnrUploadVideo.setVisibility(View.GONE);
                edtVideoTitle.setVisibility(View.GONE);
                edtVideoDescription.setVisibility(View.GONE);
            }
        });

        btnBrowse.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                IjoomerUtilities.selectImageDialog(new SelectImageDialogListner() {

                    @Override
                    public void onPhoneGallery() {
                        try {
                            Intent intent = new Intent();
                            intent.setType("video/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);

                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onCapture() {
                        try {
                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            startActivityForResult(takeVideoIntent, TAKE_VIDEO);
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_VIDEO) {
                videoPath = ((IjoomerSuperMaster) getActivity()).getAbsolutePath(data.getData());
                edtVideoFile.setText(videoPath);

            } else if (requestCode == TAKE_VIDEO) {
                videoPath = ((IjoomerSuperMaster) getActivity()).getAbsolutePath(data.getData());
                edtVideoFile.setText(videoPath);
                if ((new File(videoPath).length() / (1024 * 1024)) > IjoomerGlobalConfiguration.getVideoUploadSize()) {
                    IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(R.string.video_select_size_limit_exceeded), getString(R.string.ok),
                            R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                        @Override
                        public void NeutralMethod() {
                            edtVideoFile.setText(null);
                        }
                    });
                }
            }
            if (requestCode == GET_ADDRESS_FROM_MAP) {
                edtVideoLocation.setText(((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("address"));
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        }
    }

    /**
     * Class methods
     */


    /**
     * This method used to get category id from category index.
     * @param categoryIndex represented category index
     * @return represented {@link String}
     */
    private String getCategoryId(int categoryIndex) {
        return categoryList.get(categoryIndex).get(ID);
    }

    /**
     * This method used to get intent data.
     */
    private void getIntentData() {
        IN_GROUP_ID = getActivity().getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_GROUP_ID");
        IN_PROFILE = getActivity().getIntent().getStringExtra("IN_PROFILE") == null ? "0" : getActivity().getIntent().getStringExtra("IN_PROFILE");
    }


    /**
     * This method used to start upload video.
     * @param groupID represented group id
     * @param videoFilePath represented video file path
     * @param videoTitle represented video title
     * @param description represented video description
     * @param videoCaption represented video caption
     * @param lat represented latitude
     * @param lng represented longitude
     * @param categoryID represented category id
     * @param privacy represented privacy
     */
    private void startVideoUpload(final String groupID, final String videoFilePath, final String videoTitle, final String description, final String videoCaption, final double lat,
                                  final double lng, final String categoryID, final String privacy) {
        IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_starts),
                IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video), IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_starts));

        providerVideo.uploadVideo(groupID, videoFilePath, videoTitle, description, videoCaption, lat, lng, categoryID, privacy, new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {
            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                try {
                    if (responseCode == 200) {
                        clearVideoField();
                        IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_successfully),
                                IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video),
                                IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_successfully));

                        if(IjoomerUtilities.mSmartAndroidActivity instanceof JomVideoActivity){
                            IjoomerApplicationConfiguration.setReloadRequired(true);
                            ((JomVideoActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
                        }
                        if(IjoomerUtilities.mSmartAndroidActivity instanceof JomProfileActivity){
                            IjoomerApplicationConfiguration.setReloadRequired(true);
                            ((JomProfileActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
                        }
                    } else {
                        if (errorMessage != null && errorMessage.length() > 0) {
                            IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_failure),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video), errorMessage);
                        } else {
                            IjoomerUtilities.addToNotificationBar(
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_upload_failure),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.uplod_video),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(IjoomerUtilities.mSmartAndroidActivity.getResources().getIdentifier("code" + responseCode,
                                            "string", IjoomerUtilities.mSmartAndroidActivity.getPackageName())));
                        }
                    }
                } catch (Throwable e) {
                }
            }

        });
        if (!IN_PROFILE.equals("0")) {
            getActivity().finish();
        } else {
            JomVideoActivity.ADDVIDEOFLAG = true;
            IjoomerApplicationConfiguration.setReloadRequired(true);
            ((JomVideoActivity) getActivity()).onResume();
        }
    }

    /**
     * This method used to upload video link.
     * @param groupID represented group id
     * @param videoUrl represented video link url
     * @param videoCaption represented video caption
     * @param lat represented latitude
     * @param lng represented longitude
     * @param categoryID represented category id
     * @param privacy represented privacy
     */
    private void startVideoLinking(final String groupID, final String videoUrl, final String videoCaption, final double lat, final double lng, final String categoryID,
                                   final String privacy) {

        IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_starts),
                IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video), IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_starts));

        providerVideo.linkVideo(groupID, videoUrl, videoCaption, lat, lng, categoryID, privacy, new WebCallListener() {

            @Override
            public void onProgressUpdate(int progressCount) {
            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                try {
                    if (responseCode == 200) {
                        clearVideoField();
                        IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_linked_successfully),
                                IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video),
                                IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_linked_successfully));
                        if(IjoomerUtilities.mSmartAndroidActivity instanceof JomVideoActivity){
                            IjoomerApplicationConfiguration.setReloadRequired(true);
                            ((JomVideoActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
                        }
                        if(IjoomerUtilities.mSmartAndroidActivity instanceof JomProfileActivity){
                            IjoomerApplicationConfiguration.setReloadRequired(true);
                            ((JomProfileActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
                        }
                    } else {
                        if (errorMessage != null && errorMessage.length() > 0) {
                            IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_failure),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video), errorMessage);

                        } else {
                            IjoomerUtilities.addToNotificationBar(
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.video_link_failure),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.link_video),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(IjoomerUtilities.mSmartAndroidActivity.getResources().getIdentifier("code" + responseCode,
                                            "string", IjoomerUtilities.mSmartAndroidActivity.getPackageName())));
                        }
                    }
                } catch (Throwable e) {
                }

            }

        });

        if (!IN_PROFILE.equals("0")) {
            getActivity().finish();
        } else {
            JomVideoActivity.ADDVIDEOFLAG = true;
            IjoomerApplicationConfiguration.setReloadRequired(true);
            ((JomVideoActivity) getActivity()).onResume();
        }
    }


    /**
     * This method used to shown response message.
     * @param responseCode represented response code
     * @param finishActivityOnConnectionProblem represented finish activity on connection problem
     */
    private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
        IjoomerUtilities.getCustomOkDialog(getString(R.string.video), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
                getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

            @Override
            public void NeutralMethod() {
                
            }
        });
    }

    private void clearVideoField(){
        edtVideoCaption.setText("");
        edtVideoDescription.setText("");
        edtVideoFile.setText("");
        edtVideoLink.setText("");
        edtVideoLocation.setText("");
        edtVideoTitle.setText("");
    }

}
