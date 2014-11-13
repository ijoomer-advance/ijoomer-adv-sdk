package com.ijoomer.components.k2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.common.classes.IjoomerShareActivity;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerListView;
import com.ijoomer.customviews.IjoomerRadioButton;
import com.ijoomer.customviews.IjoomerRatingBar;
import com.ijoomer.customviews.IjoomerRatingBar.RatingHandler;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.k2.k2MainDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartActivity;
import com.smart.framework.SmartFragment;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Fragment Contains All Method Related To k2CatalogDetailFragment.
 *
 * @author tasol
 *
 */
@SuppressLint("ValidFragment")
public class k2CatalogDetailFragment extends SmartFragment implements K2TagHolder, IjoomerSharedPreferences {

    private LinearLayout lnrWriteComment;
    private LinearLayout lnrItemDescription;
    private LinearLayout lnrItemBrand;
    private LinearLayout lnrItemColor1;
    private LinearLayout lnrItemColor2;
    private LinearLayout lnrItemColor3;
    private IjoomerListView lstItemComment;
    private IjoomerTextView txtItemTitle;
    private IjoomerTextView txtItemDescription;
    private IjoomerTextView txtItemBrandName;
    private IjoomerTextView txtItemBrandDescription;
    private IjoomerTextView txtItemNoComment;
    private IjoomerTextView txtItemNoBrand;
    private IjoomerTextView txtItemNoDescription;
    private IjoomerTextView txtItemRatingVote;
    private IjoomerEditText edtCommentMessage;
    private IjoomerEditText edtCommentUserName;
    private IjoomerEditText edtCommentEmail;
    private IjoomerEditText edtCommentSiteUrl;
    private IjoomerButton btnCommentSubmit;
    private IjoomerButton btnItemPrice;
    private IjoomerRatingBar rtbItem;
    private IjoomerRadioButton rdbtItemDescription;
    @SuppressWarnings("unused")
    private IjoomerRadioButton rdbItemBrand;
    @SuppressWarnings("unused")
    private IjoomerRadioButton rdbItemReviews;
    private IjoomerRadioButton rdbItemSizeSmall;
    private IjoomerRadioButton rdbItemSizeMedium;
    private IjoomerRadioButton rdbItemSizeLarge;
    private IjoomerRadioButton rdbItemSizeXtraLarge;
    private IjoomerRadioButton rdbItemSizeDoubleXtraLarge;
    private RadioGroup rdgDescriptionBrandReviews;
    private ImageView imgItem;
    private ImageView imgItemBrandLogo;
    private ImageView imgSubmitRating;
    private ImageView imgCancelRating;
    private ImageView imgItemShare;
    private ViewGroup commentHeader;
    private ProgressBar pbrItemDetail;
    private View viewItemColor1;
    private View viewItemColor2;
    private View viewItemColor3;
    private  ProgressBar pbrImage;

    private ArrayList<SmartListItem> listData;
    private HashMap<String, String> itemData;
    private AQuery androidQuery;
    private k2MainDataProvider provider;
    private SmartListAdapterWithHolder commentAdapterWithHolder;

    private String IN_MENUID;
    private boolean isBrandAvilable;

    /**
     * Constructor
     *
     * @param mContext
     *            represented {@link Context}
     * @param itemData
     *            represented item detail data
     */
    public k2CatalogDetailFragment(Context mContext, HashMap<String, String> itemData) {
        this.itemData = itemData;
    }

    /**
     * Overrides methods
     */
    @Override
    public int setLayoutId() {
        return R.layout.k2_catalog_details_fragment;
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public void initComponents(View currentView) {

        commentHeader = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.k2_catalog_item_comment_list_header, null);

        lnrItemDescription = (LinearLayout) commentHeader.findViewById(R.id.lnrItemDescription);
        lnrItemBrand = (LinearLayout) commentHeader.findViewById(R.id.lnrItemBrand);
        lnrWriteComment = (LinearLayout) commentHeader.findViewById(R.id.lnrWriteComment);
        lnrItemColor1 = (LinearLayout) commentHeader.findViewById(R.id.lnrItemColor1);
        lnrItemColor2 = (LinearLayout) commentHeader.findViewById(R.id.lnrItemColor2);
        lnrItemColor3 = (LinearLayout) commentHeader.findViewById(R.id.lnrItemColor3);
        lstItemComment = (IjoomerListView) currentView.findViewById(R.id.lstItemComment);
        lstItemComment.addHeaderView(commentHeader);
        lstItemComment.setAdapter(null);

        txtItemDescription = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemDescription);
        txtItemBrandName = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemBrandName);
        txtItemBrandDescription = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemBrandDescription);
        txtItemTitle = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemTitle);
        txtItemNoComment = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemNoComment);
        txtItemNoBrand = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemNoBrand);
        txtItemNoDescription = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemNoDescription);
        txtItemRatingVote = (IjoomerTextView) commentHeader.findViewById(R.id.txtItemRatingVote);
        edtCommentEmail = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentEmail);
        edtCommentMessage = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentMessage);
        edtCommentSiteUrl = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentSiteUrl);
        edtCommentUserName = (IjoomerEditText) commentHeader.findViewById(R.id.edtCommentUserName);
        btnCommentSubmit = (IjoomerButton) commentHeader.findViewById(R.id.btnCommentSubmit);
        btnItemPrice = (IjoomerButton) commentHeader.findViewById(R.id.btnItemPrice);
        rdgDescriptionBrandReviews = (RadioGroup) commentHeader.findViewById(R.id.rdgDescriptionBrandReviews);
        rtbItem = (IjoomerRatingBar) commentHeader.findViewById(R.id.rtbItem);
        rdbItemReviews = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbItemReviews);
        rdbItemBrand = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbItemBrand);
        rdbtItemDescription = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbtItemDescription);
        rdbItemSizeSmall = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbItemSizeSmall);
        rdbItemSizeMedium = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbItemSizeMedium);
        rdbItemSizeLarge = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbItemSizeLarge);
        rdbItemSizeXtraLarge = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbItemSizeXtraLarge);
        rdbItemSizeDoubleXtraLarge = (IjoomerRadioButton) commentHeader.findViewById(R.id.rdbItemSizeDoubleXtraLarge);
        imgCancelRating = (ImageView) commentHeader.findViewById(R.id.imgCancelRating);
        imgSubmitRating = (ImageView) commentHeader.findViewById(R.id.imgSubmitRating);
        imgItem = (ImageView) commentHeader.findViewById(R.id.imgItem);
        imgItemShare = (ImageView) commentHeader.findViewById(R.id.imgItemShare);
        imgItemBrandLogo = (ImageView) commentHeader.findViewById(R.id.imgItemBrandLogo);
        pbrItemDetail = (ProgressBar) currentView.findViewById(R.id.pbrItemDetail);
        viewItemColor1 = (View) commentHeader.findViewById(R.id.viewItemColor1);
        viewItemColor2 = (View) commentHeader.findViewById(R.id.viewItemColor2);
        viewItemColor3 = (View) commentHeader.findViewById(R.id.viewItemColor3);
        pbrImage = (ProgressBar) commentHeader.findViewById(R.id.pbrImage);

        listData = new ArrayList<SmartListItem>();
        androidQuery = new AQuery(getActivity());
        provider = new k2MainDataProvider(getActivity());
        getIntentData();
    }

    private void getIntentData() {
        IN_MENUID = getActivity().getIntent().getStringExtra("IN_MENUID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_MENUID");
    }

    @Override
    public void prepareViews(View currentView) {
        rdbtItemDescription.setChecked(true);
        rtbItem.setStarBgColor(getResources().getColor(R.color.k2_orange));
        edtCommentEmail.setText("");
        edtCommentMessage.setText("");
        edtCommentSiteUrl.setText("");
        edtCommentUserName.setText("");
        edtCommentEmail.setError(null);
        edtCommentMessage.setError(null);
        edtCommentUserName.setError(null);
        commentAdapterWithHolder = getListAdapter();
        prepareItemDetail();

    }

    @Override
    public void setActionListeners(View currentView) {

        imgItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    @SuppressWarnings("unused")
                    JSONArray imageGallerries = new JSONArray(itemData.get(IMAGEGALLERIES));
                    ((SmartActivity) getActivity()).loadNew(K2GalleryActivity.class, getActivity(), false, "IN_PHOTOS_PATHS", itemData.get(IMAGEGALLERIES));
                } catch (Throwable e) {

                }
            }
        });

        rdgDescriptionBrandReviews.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                if (R.id.rdbtItemDescription == id) {
                    lstItemComment.setAdapter(null);
                    if (itemData.get(INTROTEXT).length() > 0) {
                        lnrItemDescription.setVisibility(View.VISIBLE);
                        txtItemNoDescription.setVisibility(View.GONE);
                    } else {
                        lnrItemDescription.setVisibility(View.GONE);
                        txtItemNoDescription.setVisibility(View.VISIBLE);
                    }
                    lnrWriteComment.setVisibility(View.GONE);
                    lnrItemBrand.setVisibility(View.GONE);
                    txtItemNoComment.setVisibility(View.GONE);
                    txtItemNoBrand.setVisibility(View.GONE);
                } else if (R.id.rdbItemBrand == id) {
                    lstItemComment.setAdapter(null);
                    lnrWriteComment.setVisibility(View.GONE);
                    lnrItemDescription.setVisibility(View.GONE);
                    txtItemNoComment.setVisibility(View.GONE);
                    txtItemNoDescription.setVisibility(View.GONE);
                    if (isBrandAvilable) {
                        lnrItemBrand.setVisibility(View.VISIBLE);
                        txtItemNoBrand.setVisibility(View.GONE);
                    } else {
                        lnrItemBrand.setVisibility(View.GONE);
                        txtItemNoBrand.setVisibility(View.VISIBLE);
                    }
                } else {
                    lstItemComment.setAdapter(commentAdapterWithHolder);
                    lnrItemDescription.setVisibility(View.GONE);
                    lnrItemBrand.setVisibility(View.GONE);
                    txtItemNoBrand.setVisibility(View.GONE);
                    txtItemNoDescription.setVisibility(View.GONE);
                    if (IjoomerGlobalConfiguration.isEnableCommentK2()) {
                        lnrWriteComment.setVisibility(View.VISIBLE);
                    } else {
                        lnrWriteComment.setVisibility(View.GONE);
                    }
                    if (listData.size() <= 0) {
                        if (IjoomerGlobalConfiguration.isEnableCommentK2()) {
                            txtItemNoComment.setVisibility(View.VISIBLE);
                            txtItemNoComment.setText(getActivity().getString(R.string.k2_be_the_first_to_comment));
                        } else {
                            txtItemNoComment.setVisibility(View.VISIBLE);
                            txtItemNoComment.setText(getActivity().getString(R.string.k2_no_comment));
                        }
                    }
                }
            }
        });

        btnCommentSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                boolean validation = true;
                if (edtCommentEmail.getText().toString().length() <= 0) {
                    edtCommentEmail.setError(getActivity().getString(R.string.validation_value_required));
                    validation = false;
                } else if (!IjoomerUtilities.emailValidator(edtCommentEmail.getText().toString())) {
                    edtCommentEmail.setError(getActivity().getString(R.string.validation_invalid_email));
                    validation = false;
                }
                if (edtCommentMessage.getText().toString().length() <= 0) {
                    edtCommentMessage.setError(getActivity().getString(R.string.validation_value_required));
                    validation = false;
                }
                if (edtCommentUserName.getText().toString().length() <= 0) {
                    edtCommentUserName.setError(getActivity().getString(R.string.validation_value_required));
                    validation = false;
                }
                if (validation) {
                    final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
                    provider.addOrPostComment(itemData.get(ID), IN_MENUID, edtCommentUserName.getText().toString().trim(), edtCommentMessage.getText().toString().trim(), edtCommentEmail.getText().toString().trim(), edtCommentSiteUrl.getText()
                            .toString().trim(), new WebCallListener() {

                        @Override
                        public void onProgressUpdate(int progressCount) {
                            proSeekBar.setProgress(progressCount);
                        }

                        @Override
                        public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, final Object data2) {
                            try {
                                if (responseCode == 200) {
                                    IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_item_details), errorMessage, getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {
                                            prepareList((JSONArray) data2);
                                            itemData.put(COMMENTS, ((JSONArray) data2).toString());
                                            commentAdapterWithHolder.notifyDataSetChanged();
                                            edtCommentEmail.setText("");
                                            edtCommentMessage.setText("");
                                            edtCommentSiteUrl.setText("");
                                            edtCommentUserName.setText("");
                                            if (listData.size() > 0) {
                                                txtItemNoComment.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                } else {
                                    responseErrorMessageHandler(responseCode, true);
                                }
                            } catch (Throwable e) {

                            }
                        }
                    });
                }
            }
        });

        rtbItem.setRatingHandler(new RatingHandler() {

            @Override
            public void onRatingChangedListener(float rating) {
                imgCancelRating.setVisibility(View.VISIBLE);
                imgSubmitRating.setVisibility(View.VISIBLE);
                txtItemRatingVote.setVisibility(View.GONE);

            }
        });

        imgCancelRating.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                imgCancelRating.setVisibility(View.GONE);
                imgSubmitRating.setVisibility(View.GONE);
                txtItemRatingVote.setVisibility(View.VISIBLE);
                try {
                    JSONObject rating = new JSONObject(itemData.get(RATINGS));
                    if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
                        rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
                        txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
                    } else {
                        rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
                        txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), Integer.parseInt(rating.getString(RATINGCOUNT))));
                    }
                } catch (Throwable e) {
                    txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
                    rtbItem.setStarRating(0);
                }

            }
        });

        imgSubmitRating.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (rtbItem.getStarRating() > 0) {
                    final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
                    provider.rating(String.valueOf(rtbItem.getStarRating()), IN_MENUID, itemData.get(ID), new WebCallListener() {

                        @Override
                        public void onProgressUpdate(int progressCount) {
                            proSeekBar.setProgress(progressCount);
                        }

                        @Override
                        public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                            try {
                                if (responseCode == 200) {
                                    try {
                                        JSONObject rating = (JSONObject) data2;
                                        itemData.put(RATINGS, rating.toString());
                                        if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
                                            rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
                                            txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
                                        } else {
                                            rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
                                            txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), Integer.parseInt(rating.getString(RATINGCOUNT))));
                                        }
                                    } catch (Throwable e) {
                                        txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
                                        rtbItem.setStarRating(0);
                                    }

                                } else {
                                    if (responseCode == 416) {
                                        IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_item_details), errorMessage, getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

                                            @Override
                                            public void NeutralMethod() {
                                                try {
                                                    JSONObject rating = new JSONObject(itemData.get(RATINGS));
                                                    if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
                                                        rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
                                                        txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
                                                    } else {
                                                        rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
                                                        txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), Integer.parseInt(rating.getString(RATINGCOUNT))));
                                                    }
                                                } catch (Throwable e) {
                                                    txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
                                                    rtbItem.setStarRating(0);
                                                }
                                            }
                                        });
                                    } else {
                                        responseErrorMessageHandler(responseCode, false);
                                    }
                                }
                            } catch (Throwable e) {

                            }

                        }
                    });
                }

                imgCancelRating.setVisibility(View.GONE);
                imgSubmitRating.setVisibility(View.GONE);
                txtItemRatingVote.setVisibility(View.VISIBLE);

            }
        });

        imgItemShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    ((SmartActivity) getActivity()).loadNewResult(IjoomerShareActivity.class, getActivity(), 0, "IN_SHARE_CAPTION", itemData.get(TITLE), "IN_SHARE_DESCRIPTION", itemData.get(INTROTEXT), "IN_SHARE_THUMB", itemData.get(IMAGESMALL),
                            "IN_SHARE_SHARELINK", itemData.get(SHARELINK));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Class methods
     */

    /**
     * This method used to prepare item details.
     */
    private void prepareItemDetail() {
        pbrItemDetail.setVisibility(View.VISIBLE);

        pbrImage.setVisibility(View.VISIBLE);
        androidQuery.ajax(itemData.get(IMAGESLARGE), Bitmap.class, 0,
                new AjaxCallback<Bitmap>() {
                    @Override
                    public void callback(String url, Bitmap object,
                                         AjaxStatus status) {
                        super.callback(url, object, status);
                        if(status.getCode()==200){
                            imgItem.setImageBitmap(object);
                        }else{
                            imgItem.setImageDrawable(getResources().getDrawable(R.drawable.k2_default));
                        }
                        pbrImage.setVisibility(View.GONE);
                    }
                });
        txtItemTitle.setText(itemData.get(TITLE));
        if (itemData.get(INTROTEXT).length() > 0) {
            txtItemDescription.setText(Html.fromHtml(itemData.get(INTROTEXT)));
            lnrItemDescription.setVisibility(View.VISIBLE);
            txtItemNoDescription.setVisibility(View.GONE);
        } else {
            lnrItemDescription.setVisibility(View.GONE);
            txtItemNoDescription.setVisibility(View.VISIBLE);
        }
        txtItemNoComment.setVisibility(View.GONE);

        try {
            JSONObject rating = new JSONObject(itemData.get(RATINGS));
            if (Integer.parseInt(rating.getString(RATINGCOUNT)) > 1) {
                rtbItem.setStarRating((Float.parseFloat(rating.getString(RATINGSUM)) / Float.parseFloat(rating.getString(RATINGCOUNT))));
                txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_votes), Integer.parseInt(rating.getString(RATINGCOUNT))));
            } else {
                rtbItem.setStarRating(Float.parseFloat(rating.getString(RATINGSUM)));
                txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), Integer.parseInt(rating.getString(RATINGCOUNT))));
            }
        } catch (Throwable e) {
            txtItemRatingVote.setText(String.format(getActivity().getString(R.string.k2_rating_vote), 0));
            rtbItem.setStarRating(0);
        }

        try {
            JSONArray extraFieldsArray = new JSONArray(itemData.get(EXTRAFIELDS));
            if (extraFieldsArray.length() > 0) {
                for (int i = 0; i < extraFieldsArray.length(); i++) {
                    JSONObject fieldJson = (JSONObject) extraFieldsArray.get(i);
                    try {
                        if (fieldJson.getString(NAME).equals("Price") && fieldJson.getString(VALUE).trim().length() > 0) {
                            btnItemPrice.setText("$ " + fieldJson.getString(VALUE));
                            btnItemPrice.setVisibility(View.VISIBLE);
                        } else if (fieldJson.getString(NAME).equals("Size") && fieldJson.getString(VALUE).trim().length() > 0) {
                            String[] splitSizeValue = ((IjoomerSuperMaster) getActivity()).getStringArray(fieldJson.getString(VALUE));
                            for (String sizeValue : splitSizeValue) {
                                if (sizeValue.equals("S")) {
                                    rdbItemSizeSmall.setChecked(true);
                                } else if (sizeValue.equals("M")) {
                                    rdbItemSizeMedium.setChecked(true);
                                } else if (sizeValue.equals("L")) {
                                    rdbItemSizeLarge.setChecked(true);
                                } else if (sizeValue.equals("XL")) {
                                    rdbItemSizeXtraLarge.setChecked(true);
                                } else if (sizeValue.equals("XXL")) {
                                    rdbItemSizeDoubleXtraLarge.setChecked(true);
                                }
                            }
                        } else if (fieldJson.getString(NAME).equals(BRANDLOGO) && fieldJson.getString(VALUE).trim().length() > 0) {
                            isBrandAvilable = true;
                            androidQuery.id(imgItemBrandLogo).image(fieldJson.getString(VALUE), true, true);
                        } else if (fieldJson.getString(NAME).equals(BRANDDESCRIPTION) && fieldJson.getString(VALUE).trim().length() > 0) {
                            isBrandAvilable = true;
                            txtItemBrandDescription.setText(Html.fromHtml(fieldJson.getString(VALUE)));
                        } else if (fieldJson.getString(NAME).equals(BRANDNAME) && fieldJson.getString(VALUE).trim().length() > 0) {
                            isBrandAvilable = true;
                            txtItemBrandName.setText(fieldJson.getString(VALUE));
                        } else if (fieldJson.getString(NAME).equals(COLOR) && fieldJson.getString(VALUE).trim().length() > 0) {
                            String[] splitColorValue = ((IjoomerSuperMaster) getActivity()).getStringArray(fieldJson.getString(VALUE));
                            for (int j = 0; j < splitColorValue.length; j++) {
                                if (j == 0) {
                                    lnrItemColor1.setVisibility(View.VISIBLE);
                                    viewItemColor1.setBackgroundColor(Color.parseColor(splitColorValue[j]));
                                }
                                if (j == 1) {
                                    lnrItemColor2.setVisibility(View.VISIBLE);
                                    viewItemColor2.setBackgroundColor(Color.parseColor(splitColorValue[j]));
                                }
                                if (j == 2) {
                                    lnrItemColor3.setVisibility(View.VISIBLE);
                                    viewItemColor3.setBackgroundColor(Color.parseColor(splitColorValue[j]));
                                }
                            }
                        }
                    } catch (Throwable e) {

                    }

                }
            }

        } catch (Throwable e) {

        }

        try {
            JSONArray commentArray = new JSONArray(itemData.get(COMMENTS));
            if (commentArray.length() > 0) {
                prepareList(commentArray);

            } else {
                lstItemComment.setAdapter(null);
            }

        } catch (Throwable e) {
            lstItemComment.setAdapter(null);
        }
        pbrItemDetail.setVisibility(View.GONE);

    }

    /**
     * This method used to prepare list item comment.
     *
     * @param data
     *            represented comment data
     */
    public void prepareList(JSONArray data) {
        if (data != null) {
            listData.clear();
            for (int i = 0; i < data.length(); i++) {
                SmartListItem item = new SmartListItem();
                item.setItemLayout(R.layout.k2_item_details_comment_list_item);
                ArrayList<Object> obj = new ArrayList<Object>();
                try {
                    if (data.getJSONObject(i).getString(PUBLISHED).equals("1")) {
                        obj.add(data.get(i));
                        item.setValues(obj);
                        listData.add(item);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }

        }
    }

    /**
     * This method used to shown response message.
     *
     * @param responseCode
     *            represented response code
     * @param finishActivityOnConnectionProblem
     *            represented finish activity on connection problem
     */
    private void responseErrorMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {
        IjoomerUtilities.getCustomOkDialog(getActivity().getString(R.string.dialog_k2_item_details), getActivity().getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())),
                getActivity().getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

            @Override
            public void NeutralMethod() {
            }
        });
    }

    /**
     * List adapter for item comment.
     */
    private SmartListAdapterWithHolder getListAdapter() {
        SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(getActivity(), R.layout.k2_item_details_comment_list_item, listData, new ItemView() {
            @Override
            public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {

                holder.imgK2CommentUserAvatar = (ImageView) v.findViewById(R.id.imgK2CommentUserAvatar);
                holder.txtK2CommentUserName = (IjoomerTextView) v.findViewById(R.id.txtK2CommentUserName);
                holder.txtK2CommentDate = (IjoomerTextView) v.findViewById(R.id.txtK2CommentDate);
                holder.txtK2CommentTitle = (IjoomerTextView) v.findViewById(R.id.txtK2CommentTitle);
                holder.txtK2CommentUrl = (IjoomerTextView) v.findViewById(R.id.txtK2CommentUrl);

                final JSONObject row = (JSONObject) item.getValues().get(0);
                androidQuery.id(holder.imgK2CommentUserAvatar).image(IjoomerGlobalConfiguration.getDefaultAvatar(), true, true, 60, R.drawable.k2_default);
                try {
                    holder.txtK2CommentTitle.setText(row.getString(COMMENTTEXT));
                    holder.txtK2CommentUserName.setText(row.getString(USERNAME));
                    holder.txtK2CommentDate.setText(row.getString(COMMENTDATE));
                    holder.txtK2CommentUrl.setText(row.getString(COMMENTURL));
                    holder.txtK2CommentUrl.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            try {
                                ((SmartActivity) getActivity()).loadNew(IjoomerWebviewClient.class, getActivity(), false, URL, row.getString(COMMENTURL));
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Throwable e) {
                    e.printStackTrace();
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
