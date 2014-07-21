package com.ijoomer.common.classes;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ijoomer.custom.interfaces.ShareListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.src.R;
import com.ijoomer.theme.ThemeManager;

/**
 * This Class Contains All Method Related To IJoomerTwitterShareActivity.
 *
 * @author tasol
 *
 */
public class IJoomerMailShareActivity extends IjoomerSuperMaster {

    private LinearLayout lnrSayAboutStory;
    private LinearLayout lnrEmailShare;
    private ImageView imgShareAddEmail;
    private ImageView imgShareClose;
    private IjoomerEditText edtShareEmail;
    private IjoomerEditText edtShareEmailMessage;
    private IjoomerButton btnSend;
    private IjoomerButton btnCancel;
    private RadioGroup rdgShare;


    private ArrayList<HashMap<String, Object>> selectedData;

    private String IN_SHARE_CAPTION;
    private String IN_SHARE_DESCRIPTION;
    private String IN_SHARE_SHARELINK;



    /**
     * Overrides methods
     */
    @Override
    public int setLayoutId() {
        return ThemeManager.getInstance().getShare();
    }


    @Override
    public void initComponents() {

        lnrSayAboutStory = (LinearLayout) findViewById(R.id.lnrSayAboutStory);
        lnrEmailShare = (LinearLayout) findViewById(R.id.lnrEmailShare);
        rdgShare = (RadioGroup) findViewById(R.id.rdgShare);
        imgShareAddEmail = (ImageView) findViewById(R.id.imgShareAddEmail);
        imgShareClose = (ImageView) findViewById(R.id.imgShareClose);
        edtShareEmail = (IjoomerEditText) findViewById(R.id.edtShareEmail);
        edtShareEmailMessage = (IjoomerEditText) findViewById(R.id.edtShareEmailMessage);
        btnSend = (IjoomerButton) findViewById(R.id.btnSend);
        btnCancel = (IjoomerButton) findViewById(R.id.btnCancel);

        selectedData = new ArrayList<HashMap<String, Object>>();
        getIntentData();
        rdgShare.setVisibility(View.GONE);
        lnrSayAboutStory.setVisibility(View.GONE);
        lnrEmailShare.setVisibility(View.VISIBLE);
    }

    @Override
    public void setActionListeners() {
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        btnSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (edtShareEmail.getText().toString().trim().length() > 0) {
                    onEmail(edtShareEmail.getText().toString(), edtShareEmailMessage.getText().toString().trim());
                } else {
                    ting(getString(R.string.validation_value_required));
                }
            }
        });
        imgShareAddEmail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                IjoomerUtilities.getContactDialog(selectedData, new ShareListner() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onClick(String shareOn, Object value, String message) {
                        selectedData.clear();
                        selectedData.addAll((ArrayList<HashMap<String, Object>>) value);
                        edtShareEmail.setText(message);
                    }
                });
            }
        });
        imgShareClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {

    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public int setHeaderLayoutId() {
        return 0;
    }

    @Override
    public int setFooterLayoutId() {
        return 0;
    }

    @Override
    public void prepareViews() {

    }

    @Override
    public String[] setTabItemNames() {
        return null;
    }

    @Override
    public int setTabBarDividerResId() {
        return 0;
    }

    @Override
    public int setTabItemLayoutId() {
        return 0;
    }

    @Override
    public int[] setTabItemOnDrawables() {
        return null;
    }

    @Override
    public int[] setTabItemOffDrawables() {
        return null;
    }

    @Override
    public int[] setTabItemPressDrawables() {
        return null;
    }


    /**
     * Class methods
     */

    /**
     * This method used to get Intent data.
     */
    private void getIntentData() {
        IN_SHARE_CAPTION = getIntent().getStringExtra("IN_SHARE_CAPTION") != null ? getIntent().getStringExtra("IN_SHARE_CAPTION") : "";
        IN_SHARE_DESCRIPTION = getIntent().getStringExtra("IN_SHARE_DESCRIPTION") != null ? getIntent().getStringExtra("IN_SHARE_DESCRIPTION") : "";
        IN_SHARE_SHARELINK = getIntent().getStringExtra("IN_SHARE_SHARELINK") != null ? getIntent().getStringExtra("IN_SHARE_SHARELINK") : "";
    }

    /**
     * This method used to share data on mail.
     * @param value represented sender id with (,) separated
     * @param message represented message share
     */
    private void onEmail(String value, String message) {
        String[] to = value.toString().split(",");
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/html");
        i.putExtra(Intent.EXTRA_EMAIL, to);
        i.putExtra(Intent.EXTRA_SUBJECT, String.format(getString(R.string.share_email_subject), IN_SHARE_CAPTION));
        i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(IjoomerUtilities.prepareEmailBody(message == null ? "" : message, getSmartApplication().readSharedPreferences().getString(SP_USERNAME, "") + " " + getString(R.string.saw_this_story_on_the) + " " + getString(R.string.app_name) + " " + getString(R.string.thought_you_should_see_it), IN_SHARE_CAPTION, IN_SHARE_DESCRIPTION, IN_SHARE_SHARELINK, getString(R.string.try_ijoomeradvance), getString(R.string.site_url))));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            ting(getString(R.string.share_email_no_client));
        }
    }

}
