package com.ijoomer.components.jomsocial;

import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerUtilities.MyCustomAdapter;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.CustomClickListner;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomEventDataProvider;
import com.ijoomer.library.jomsocial.JomGroupDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomGroupCreateActivity.
 *
 * @author tasol
 *
 */
public class JomGroupCreateActivity extends JomMasterActivity {

    private LinearLayout lnr_form;
    private IjoomerButton btnCancel;
    private IjoomerButton btnCreate;

    ArrayList<HashMap<String, String>> IN_FIELD_LIST;
    private JomEventDataProvider dataProvider;

    final private String PHOTOPERMISSION_ADMIN = "photopermission-admin";
    final private String PHOTOPERMISSION_MEMBER = "photopermission-member";
    final private String VIDEOPERMISSION_ADMIN = "videopermission-admin";
    final private String VIDEOPERMISSION_MEMBER = "videopermission-member";
    final private String EVENTPERMISSION_ADMIN = "eventpermission-admin";
    final private String EVENTPERMISSION_MEMBER = "eventpermission-member";
    final private String GROUPDISCUSSIONFILESHARING = "groupdiscussionfilesharing";
    final private String DISCUSSORDERING = "discussordering";
    final private String GROUPRECENTPHOTOS = "grouprecentphotos";
    final private String GROUPRECENTVIDEOS = "grouprecentvideos";
    final private String GROUPRECENTEVENTS = "grouprecentevents";
    final private String GROUPANNOUNCEMENTFILESHARING = "groupannouncementfilesharing";
    private String IN_GROUP_ID;

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_dynamic_view;
    }

    @Override
    public void initComponents() {
        lnr_form = (LinearLayout) findViewById(R.id.lnr_form);
        btnCancel = (IjoomerButton) findViewById(R.id.btnCancel);
        btnCreate = (IjoomerButton) findViewById(R.id.btnCreate);

        dataProvider = new JomEventDataProvider(this);
        getIntentData();
    }

    @Override
    public void prepareViews() {
        createForm();
        if (!IN_GROUP_ID.equals("0")) {
            btnCreate.setText(getString(R.string.save));
        }
    }

    @Override
    public void setActionListeners() {
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        btnCreate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                updateGroupDetails();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Class methods
     */

    /**
     * This method used to get intent data.
     */
    @SuppressWarnings("unchecked")
    private void getIntentData() {
        IN_FIELD_LIST = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_FIELD_LIST") == null ? new ArrayList<HashMap<String, String>>()
                : (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("IN_FIELD_LIST");
        IN_GROUP_ID = getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getIntent().getStringExtra("IN_GROUP_ID");
    }


    /**
     * This method used to update group details.
     */
    @SuppressWarnings("unchecked")
    private void updateGroupDetails() {

        boolean validationFlag = true;
        ArrayList<HashMap<String, String>> groupField = new ArrayList<HashMap<String, String>>();
        int size = lnr_form.getChildCount();
        for (int i = 0; i < size; i++) {
            View v = (LinearLayout) lnr_form.getChildAt(i);
            HashMap<String, String> field = new HashMap<String, String>();
            field.putAll((HashMap<String, String>) v.getTag());
            IjoomerEditText edtValue = null;
            Spinner spnrValue = null;
            IjoomerCheckBox chbValue = null;
            if (field != null) {
                if (field.get(TYPE).equals(TEXT)) {
                    edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEdit)).findViewById(R.id.txtValue);
                } else if (field.get(TYPE).equals(TEXTAREA)) {
                    edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditArea)).findViewById(R.id.txtValue);
                } else if (field.get(TYPE).equals(DATETIME)) {
                    edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditClickable)).findViewById(R.id.txtValue);
                } else if (field.get(TYPE).equals(MAP)) {
                    edtValue = (IjoomerEditText) ((LinearLayout) v.findViewById(R.id.lnrEditMap)).findViewById(R.id.txtValue);
                }

                if (field.get(TYPE).equals(CHECKBOX)) {
                    chbValue = (IjoomerCheckBox) ((LinearLayout) v.findViewById(R.id.lnrCheckbox)).findViewById(R.id.txtValue);
                    field.put(VALUE, chbValue.isChecked() ? "1" : "0");
                    groupField.add(field);
                } else if (field.get(TYPE).equals(SELECT)) {
                    spnrValue = (Spinner) ((LinearLayout) v.findViewById(R.id.lnrSpin)).findViewById(R.id.txtValue);
                    try {
                        JSONArray options = new JSONArray(field.get(OPTIONS));
                        field.put(VALUE, ((JSONObject) options.get(spnrValue.getSelectedItemPosition())).getString(VALUE));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    groupField.add(field);
                }

                if (edtValue != null) {
                    if (field.get(REQUIRED).equals("1") && edtValue.getText().toString().length() <= 0) {
                        edtValue.setError(getString(R.string.validation_value_required));
                        validationFlag = false;
                    } else {
                        field.put(VALUE, edtValue.getText().toString().trim());
                        groupField.add(field);
                    }
                }

            }
        }

        if (validationFlag) {

            final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
            new JomGroupDataProvider(JomGroupCreateActivity.this).addOrEditGroupSubmit(IN_GROUP_ID, groupField, new WebCallListener() {

                @Override
                public void onProgressUpdate(int progressCount) {
                    proSeekBar.setProgress(progressCount);
                }

                @Override
                public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                    if (responseCode == 200) {
                        updateHeader(dataProvider.getNotificationData());
                        IjoomerApplicationConfiguration.setReloadRequired(true);
                        finish();
                    } else {
                        if(errorMessage!=null && errorMessage.length()>0 && !errorMessage.equals("null")){
                            IjoomerUtilities.getCustomOkDialog(getString(R.string.group),
                                    errorMessage, getString(R.string.ok), R.layout.ijoomer_ok_dialog,
                                    new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {

                                        }
                                    });
                        }else{
                            IjoomerUtilities.getCustomOkDialog(getString(R.string.group),
                                    getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog,
                                    new CustomAlertNeutral() {

                                        @Override
                                        public void NeutralMethod() {
                                           
                                        }
                                    });
                        }
                    }

                }
            });
        }
    }


    /**
     * This method used to create dynamic form for group.
     */
    private void createForm() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;

        LinearLayout layout = null;
        int size = IN_FIELD_LIST.size();
        for (int j = 0; j < size; j++) {
            final HashMap<String, String> field = IN_FIELD_LIST.get(j);
            View fieldView = inflater.inflate(R.layout.jom_dynamic_view_item, null);

            if (field.get(TYPE).equals(TEXT)) {
                final IjoomerEditText edit;
                layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEdit));
                layout.setVisibility(View.VISIBLE);
                edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
                edit.setText(Html.fromHtml(field.get(VALUE)));
                edit.setHint(field.get(CAPTION));
                if(field.get(NAME).equals(GROUPRECENTPHOTOS) || field.get(NAME).equals(GROUPRECENTVIDEOS) || field.get(NAME).equals(GROUPRECENTEVENTS)){
                    edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            } else if (field.get(TYPE).equals(TEXTAREA)) {
                final IjoomerEditText edit;
                layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditArea));
                layout.setVisibility(View.VISIBLE);
                edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
                edit.setText(Html.fromHtml(field.get(VALUE)));
                edit.setHint(field.get(CAPTION));

            } else if (field.get(TYPE).equals(SELECT)) {
                layout = ((LinearLayout) fieldView.findViewById(R.id.lnrSpin));
                layout.setVisibility(View.VISIBLE);
                MyCustomAdapter adapter = IjoomerUtilities.getSpinnerAdapter(field);
                ((Spinner) layout.findViewById(R.id.txtValue)).setAdapter(adapter);
                ((Spinner) layout.findViewById(R.id.txtValue)).setSelection(adapter.getDefaultPosition());

            } else if (field.get(TYPE).equals(DATETIME)) {
                final IjoomerEditText edit;
                layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
                layout.setVisibility(View.VISIBLE);
                edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
                edit.setText(field.get(VALUE));
                edit.setHint(field.get(CAPTION));
                edit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        IjoomerUtilities.getDateTimeDialog(((IjoomerEditText) v).getText().toString(), new CustomClickListner() {

                            @Override
                            public void onClick(String value) {
                                ((IjoomerEditText) v).setText(value);
                                ((IjoomerEditText) v).setError(null);
                            }
                        });

                    }
                });

            } else if (field.get(TYPE).equals(MULTIPLESELECT)) {
                final IjoomerEditText edit;
                layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditClickable));
                layout.setVisibility(View.VISIBLE);
                edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
                edit.setText(field.get(VALUE));
                edit.setHint(field.get(CAPTION));
                edit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        IjoomerUtilities.getMultiSelectionDialog(field.get(CAPTION), field.get(OPTIONS), "", new CustomClickListner() {

                            @Override
                            public void onClick(String value) {
                                ((IjoomerEditText) v).setText(value);
                            }
                        });

                    }
                });
            } else if (field.get(TYPE).equals(MAP)) {
                final IjoomerEditText edit;
                final ImageView imgMap;
                layout = ((LinearLayout) fieldView.findViewById(R.id.lnrEditMap));
                layout.setVisibility(View.VISIBLE);
                edit = ((IjoomerEditText) layout.findViewById(R.id.txtValue));
                imgMap = ((ImageView) layout.findViewById(R.id.imgMap));
                edit.setText(field.get(VALUE));
                edit.setHint(field.get(CAPTION));
                imgMap.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                });

            } else if (field.get(TYPE).equals(CHECKBOX)) {
                final IjoomerCheckBox chb;
                layout = ((LinearLayout) fieldView.findViewById(R.id.lnrCheckbox));
                layout.setVisibility(View.VISIBLE);
                chb = ((IjoomerCheckBox) layout.findViewById(R.id.txtValue));
                chb.setTextAppearance(JomGroupCreateActivity.this, R.style.ijoomer_textview_h2);
                if (field.get(NAME).equals(DISCUSSORDERING) || field.get(NAME).equals(GROUPDISCUSSIONFILESHARING)) {
                    chb.setText(field.get(CAPTION) + " " + getString(R.string.group_discussion));
                } else if (field.get(NAME).equals(GROUPANNOUNCEMENTFILESHARING)) {
                    chb.setText(field.get(CAPTION) + " " + getString(R.string.group_announcement));
                } else {
                    chb.setText(field.get(CAPTION));
                }
                if (field.get(VALUE).toString().trim().length() > 0) {
                    chb.setChecked(field.get(VALUE).toString().equals("1") ? true : false);
                }

                if (field.get(NAME).equalsIgnoreCase(PHOTOPERMISSION_MEMBER) || field.get(NAME).equalsIgnoreCase(VIDEOPERMISSION_MEMBER)
                        || field.get(NAME).equalsIgnoreCase(EVENTPERMISSION_MEMBER)) {
                    chb.setEnabled(false);
                }
                chb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @SuppressWarnings({ "unchecked" })
                    @Override
                    public void onCheckedChanged(CompoundButton IjoomerButtonView, boolean isChecked) {

                        if (field.get(NAME).equalsIgnoreCase(PHOTOPERMISSION_ADMIN)) {
                            int size = lnr_form.getChildCount();
                            for (int i = 0; i < size; i++) {
                                View v = (LinearLayout) lnr_form.getChildAt(i);
                                HashMap<String, String> field = new HashMap<String, String>();
                                field.putAll((HashMap<String, String>) v.getTag());
                                IjoomerCheckBox chbValue = (IjoomerCheckBox) ((LinearLayout) v.findViewById(R.id.lnrCheckbox)).findViewById(R.id.txtValue);
                                if (field.get(NAME).equalsIgnoreCase(PHOTOPERMISSION_MEMBER) && isChecked) {
                                    chbValue.setChecked(false);
                                    chbValue.setEnabled(true);
                                    break;
                                } else if (field.get(NAME).equalsIgnoreCase(PHOTOPERMISSION_MEMBER)) {
                                    chbValue.setChecked(false);
                                    chbValue.setEnabled(false);
                                    break;
                                }
                            }
                        } else if (field.get(NAME).equalsIgnoreCase(VIDEOPERMISSION_ADMIN)) {
                            int size = lnr_form.getChildCount();
                            for (int i = 0; i < size; i++) {
                                View v = (LinearLayout) lnr_form.getChildAt(i);
                                HashMap<String, String> field = new HashMap<String, String>();
                                field.putAll((HashMap<String, String>) v.getTag());
                                IjoomerCheckBox chbValue = (IjoomerCheckBox) ((LinearLayout) v.findViewById(R.id.lnrCheckbox)).findViewById(R.id.txtValue);
                                if (field.get(NAME).equalsIgnoreCase(VIDEOPERMISSION_MEMBER) && isChecked) {
                                    chbValue.setChecked(false);
                                    chbValue.setEnabled(true);
                                    break;
                                } else if (field.get(NAME).equalsIgnoreCase(VIDEOPERMISSION_MEMBER) && !isChecked) {
                                    chbValue.setChecked(false);
                                    chbValue.setEnabled(false);
                                    break;
                                }
                            }
                        } else if (field.get(NAME).equalsIgnoreCase(EVENTPERMISSION_ADMIN)) {
                            int size = lnr_form.getChildCount();
                            for (int i = 0; i < size; i++) {
                                View v = (LinearLayout) lnr_form.getChildAt(i);
                                HashMap<String, String> field = new HashMap<String, String>();
                                field.putAll((HashMap<String, String>) v.getTag());
                                IjoomerCheckBox chbValue = (IjoomerCheckBox) ((LinearLayout) v.findViewById(R.id.lnrCheckbox)).findViewById(R.id.txtValue);
                                if (field.get(NAME).equalsIgnoreCase(EVENTPERMISSION_MEMBER) && isChecked) {
                                    chbValue.setChecked(false);
                                    chbValue.setEnabled(true);
                                    break;
                                } else if (field.get(NAME).equalsIgnoreCase(EVENTPERMISSION_MEMBER) && !isChecked) {
                                    chbValue.setChecked(false);
                                    chbValue.setEnabled(false);
                                    break;
                                }
                            }
                        }

                    }
                });

            }

            try {
                if (field.get(REQUIRED).equalsIgnoreCase("1")) {
                    ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("* ");
                } else {
                    ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("   ");
                }
            } catch (Exception e) {
                ((IjoomerTextView) layout.findViewById(R.id.txtLable)).setText("   ");
            }

            fieldView.setTag(field);
            lnr_form.addView(fieldView, params);
        }

    }
}
