package com.ijoomer.components.jomsocial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomMessageDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertMagnatic;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomMessageDetailsActivity.
 *
 * @author tasol
 *
 */
public class JomMessageDetailsActivity extends JomMasterActivity implements JomTagHolder {

    private LinearLayout listFooter;
    private ListView lstMessageDetails;

    private IjoomerVoiceAndTextMessager voiceMessager;

    private AQuery androidQuery;
    private ArrayList<SmartListItem> listData;
    private HashMap<String, String> IN_MESSAGE_DETAILS;
    private SmartListAdapterWithHolder lstMessageAdapter;

    private JomMessageDataProvider providerMessage;
    private JomMessageDataProvider provider;


    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_message_details;
    }

    @Override
    public void initComponents() {

        listFooter = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.ijoomer_list_footer, null);
        lstMessageDetails = (ListView) findViewById(R.id.lstMessageDetails);
        lstMessageDetails.addFooterView(listFooter, null, false);
        lstMessageDetails.setAdapter(null);
        voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);

        androidQuery = new AQuery(this);
        listData = new ArrayList<SmartListItem>();

        providerMessage = new JomMessageDataProvider(this);
        provider = new JomMessageDataProvider(this);

        getIntentData();
    }

    @Override
    public void prepareViews() {
        lstMessageDetails.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lstMessageDetails.setStackFromBottom(true);
        getMessageList(false);
    }

    @Override
    public void setActionListeners() {
        voiceMessager.setMessageHandler(new MessageHandler() {

            @Override
            public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
                System.out.println("Message : " + message + " VoicePath : " + voiceMessagePath);
                provider.replyMessage(IN_MESSAGE_DETAILS.get(ID), message, voiceMessagePath, new WebCallListener() {
                    @Override
                    public void onProgressUpdate(int progressCount) {

                    }

                    @Override
                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                        if (responseCode == 200) {
                            updateHeader(provider.getNotificationData());
                            providerMessage.restorePagingSettings();
                            getMessageList(false);
                        } else {
                            responseErrorMessageHandler(responseCode, false);
                        }
                    }
                });
            }

            @Override
            public void onButtonSend(String message) {
                System.out.println("Message On Send : " + message);
                hideSoftKeyboard();
                provider.replyMessage(IN_MESSAGE_DETAILS.get(ID), message, null, new WebCallListener() {
                    @Override
                    public void onProgressUpdate(int progressCount) {

                    }

                    @Override
                    public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                        if (responseCode == 200) {
                            updateHeader(provider.getNotificationData());
                            providerMessage.restorePagingSettings();
                            getMessageList(false);
                        } else {
                            responseErrorMessageHandler(responseCode, false);
                        }
                    }
                });
            }

            @Override
            public void onToggle(int messager) {

            }
        });

        lstMessageDetails.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }

            @Override
            public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) >= totalItemCount && totalItemCount > 1) {
                    if (!providerMessage.isCalling() && providerMessage.hasNextPage()) {
                        listFooterVisible();
                        providerMessage.getMessageDetailsList(IN_MESSAGE_DETAILS.get(ID), IN_MESSAGE_DETAILS.get(USER_ID), new WebCallListener() {

                            @Override
                            public void onProgressUpdate(int progressCount) {

                            }

                            @Override
                            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                listFooterInvisible();
                                if (responseCode == 200) {
                                    updateHeader(provider.getNotificationData());
                                    prepareList(data1, true);
                                } else {
                                    responseErrorMessageHandler(responseCode, false);
                                }
                            }
                        });
                    }
                }

            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {

    }

    /**
     * Class methods
     */

    /**
     * This method used to get intent data.
     */
    @SuppressWarnings("unchecked")
    private void getIntentData() {
        IN_MESSAGE_DETAILS = (HashMap<String, String>) getIntent().getSerializableExtra("IN_MESSAGE_DETAILS") == null ? new HashMap<String, String>()
                : (HashMap<String, String>) getIntent().getSerializableExtra("IN_MESSAGE_DETAILS");
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
        IjoomerUtilities.getCustomOkDialog(getString(R.string.message), getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())),
                getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

            @Override
            public void NeutralMethod() {
                
            }
        });
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
     * This method used to get message.
     *
     * @param append
     *            represented data append
     */
    private void getMessageList(final boolean append) {
        providerMessage.getMessageDetailsList(IN_MESSAGE_DETAILS.containsKey(PARENT) ? IN_MESSAGE_DETAILS.get(PARENT) : IN_MESSAGE_DETAILS.get(ID),
                IN_MESSAGE_DETAILS.get(USER_ID), new WebCallListener() {
            final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));

            @Override
            public void onProgressUpdate(int progressCount) {
                proSeekBar.setProgress(progressCount);
            }

            @Override
            public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                if (responseCode == 200) {
                    updateHeader(providerMessage.getNotificationData());
                    prepareList(data1, append);
                    lstMessageAdapter = getListAdapter();
                    lstMessageDetails.setAdapter(lstMessageAdapter);
                } else {
                    responseErrorMessageHandler(responseCode, true);
                }
            }
        });
    }

    /**
     * This method used to prepare list message.
     *
     * @param data
     *            represented message data
     * @param append
     *            represented data append
     */
    public void prepareList(ArrayList<HashMap<String, String>> data, boolean append) {
        if (data != null && data.size() > 0) {
            if (append) {

                int size = data.size();
                for (int i = size - 1; i >= 0; i--) {
                    SmartListItem item = new SmartListItem();
                    item.setItemLayout(R.layout.jom_message_details_list_item);
                    ArrayList<Object> obj = new ArrayList<Object>();
                    obj.add(data.get(i));
                    item.setValues(obj);
                    lstMessageAdapter.insert(item, i);
                }

            } else {
                listData.clear();
                for (HashMap<String, String> hashMap : data) {
                    SmartListItem item = new SmartListItem();
                    item.setItemLayout(R.layout.jom_message_details_list_item);
                    ArrayList<Object> obj = new ArrayList<Object>();
                    obj.add(hashMap);
                    item.setValues(obj);
                    listData.add(item);
                }
            }
        }
    }

    private SmartListAdapterWithHolder getListAdapter() {
        SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(JomMessageDetailsActivity.this, R.layout.jom_message_details_list_item, listData,
                new ItemView() {
                    @Override
                    public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
                        holder.lnrSent = (LinearLayout) v.findViewById(R.id.lnrSent);
                        holder.txtSentMessage = (IjoomerTextView) v.findViewById(R.id.txtSentMessage);
                        holder.txtSentMessageDate = (IjoomerTextView) v.findViewById(R.id.txtSentMessageDate);
                        holder.imgSentUser = (ImageView) v.findViewById(R.id.imgSentUser);
                        holder.btnSentMessageRemove = (IjoomerButton) v.findViewById(R.id.btnSentMessageRemove);
                        holder.btnSentMessagePlayVoice = (IjoomerVoiceButton) v.findViewById(R.id.btnSentMessagePlayVoice);

                        holder.lnrReceive = (LinearLayout) v.findViewById(R.id.lnrReceive);
                        holder.txtReceiveMessage = (IjoomerTextView) v.findViewById(R.id.txtReceiveMessage);
                        holder.txtReceiveMessageDate = (IjoomerTextView) v.findViewById(R.id.txtReceiveMessageDate);
                        holder.imgReceiveUser = (ImageView) v.findViewById(R.id.imgReceiveUser);
                        holder.btnReceiveMessageRemove = (IjoomerButton) v.findViewById(R.id.btnReceiveMessageRemove);
                        holder.btnReceiveMessagePlayVoice = (IjoomerVoiceButton) v.findViewById(R.id.btnReceiveMessagePlayVoice);

                        holder.btnSentMessagePlayVoice.setVisibility(View.GONE);
                        holder.btnReceiveMessagePlayVoice.setVisibility(View.GONE);

                        @SuppressWarnings("unchecked")
                        final HashMap<String, String> row = (HashMap<String, String>) item.getValues().get(0);

                        if (row.get("outgoing").equals("1")) {
                            holder.lnrSent.setVisibility(View.VISIBLE);
                            holder.lnrReceive.setVisibility(View.GONE);
                            androidQuery.id(holder.imgSentUser).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
                            holder.txtSentMessage.setText(getPlainText(row.get(BODY)));
                            if (holder.txtSentMessage.getText().toString().length() <= 0) {
                                holder.txtSentMessage.setVisibility(View.GONE);
                            }
                            holder.txtSentMessageDate.setText(row.get(DATE));

                            if (getAudio(row.get(BODY)) != null) {

                                holder.btnSentMessagePlayVoice.setVisibility(View.VISIBLE);
                                holder.btnSentMessagePlayVoice.setText(getAudioLength(row.get(BODY)));
                                holder.btnSentMessagePlayVoice.setAudioPath(getAudio(row.get(BODY)), false);
                                holder.btnSentMessagePlayVoice.setAudioListener(new AudioListener() {

                                    @Override
                                    public void onReportClicked() {
                                        reportVoice(getAudio(row.get(BODY)));
                                    }

                                    @Override
                                    public void onPrepared() {
                                    }

                                    @Override
                                    public void onPlayClicked(boolean isplaying) {
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });

                            }

                            holder.imgSentUser.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {

                                    if (row.get(USER_PROFILE).equals("1")) {
                                        gotoProfile(row.get(USER_ID));
                                    }
                                }
                            });

                            holder.btnSentMessageRemove.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {

                                    IjoomerUtilities.getCustomConfirmDialog(getString(R.string.message_title_remove), getString(R.string.are_you_sure), getString(R.string.yes),
                                            getString(R.string.no), new CustomAlertMagnatic() {

                                        @Override
                                        public void PositiveMethod() {
                                            provider.removeMessage(row.get(ID), false, new WebCallListener() {
                                                @Override
                                                public void onProgressUpdate(int progressCount) {

                                                }

                                                @Override
                                                public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1,
                                                                           Object data2) {
                                                    if (responseCode == 200) {
                                                        updateHeader(provider.getNotificationData());
                                                        lstMessageAdapter.remove(lstMessageAdapter.getItem(position));
                                                        IjoomerApplicationConfiguration.setReloadRequired(true);
                                                        if (lstMessageAdapter.getCount() == 0) {
                                                            finish();
                                                        }
                                                    } else {
                                                        responseErrorMessageHandler(responseCode, false);
                                                    }
                                                }

                                            });
                                        }

                                        @Override
                                        public void NegativeMethod() {

                                        }

                                    });

                                }
                            });

                        } else {
                            holder.lnrReceive.setVisibility(View.VISIBLE);
                            holder.lnrSent.setVisibility(View.GONE);
                            androidQuery.id(holder.imgReceiveUser).image(row.get(USER_AVATAR), true, true, getDeviceWidth(), 0);
                            holder.txtReceiveMessage.setText(getPlainText(row.get(BODY)));

                            if (holder.txtReceiveMessage.getText().toString().length() <= 0) {
                                holder.txtReceiveMessage.setVisibility(View.GONE);
                            }
                            holder.txtReceiveMessageDate.setText(row.get(DATE));

                            if (getAudio(row.get(BODY)) != null) {


                                holder.btnReceiveMessagePlayVoice.setVisibility(View.VISIBLE);
                                holder.btnReceiveMessagePlayVoice.setText(getAudioLength(row.get(BODY)));
                                holder.btnReceiveMessagePlayVoice.setAudioPath(getAudio(row.get(BODY)), false);
                                holder.btnReceiveMessagePlayVoice.setAudioListener(new AudioListener() {

                                    @Override
                                    public void onReportClicked() {
                                        reportVoice(getAudio(row.get(BODY)));
                                    }

                                    @Override
                                    public void onPrepared() {
                                    }

                                    @Override
                                    public void onPlayClicked(boolean isplaying) {
                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });

                            }
                            holder.imgReceiveUser.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {

                                    if (row.get(USER_PROFILE).equals("1")) {
                                        gotoProfile(row.get(USER_ID));
                                    }
                                }
                            });

                            holder.btnReceiveMessageRemove.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {

                                    IjoomerUtilities.getCustomConfirmDialog(getString(R.string.message_title_remove), getString(R.string.are_you_sure), getString(R.string.yes),
                                            getString(R.string.no), new CustomAlertMagnatic() {

                                        @Override
                                        public void PositiveMethod() {
                                            provider.removeMessage(row.get(ID), false, new WebCallListener() {
                                                @Override
                                                public void onProgressUpdate(int progressCount) {

                                                }

                                                @Override
                                                public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1,
                                                                           Object data2) {
                                                    if (responseCode == 200) {
                                                        updateHeader(provider.getNotificationData());
                                                        lstMessageAdapter.remove(lstMessageAdapter.getItem(position));
                                                        IjoomerApplicationConfiguration.setReloadRequired(true);
                                                        if (lstMessageAdapter.getCount() == 0) {
                                                            finish();
                                                        }
                                                    } else {
                                                        responseErrorMessageHandler(responseCode, false);
                                                    }
                                                }

                                            });
                                        }

                                        @Override
                                        public void NegativeMethod() {

                                        }

                                    });

                                }
                            });

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
