package com.ijoomer.customviews;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.ViewHolder;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.common.configuration.IjoomerGlobalConfiguration;
import com.ijoomer.src.R;
import com.smart.framework.ItemView;
import com.smart.framework.SmartListAdapterWithHolder;
import com.smart.framework.SmartListItem;

/**
 * This Class Contains Method IjoomerVoiceAndTextMessager.
 *
 * @author tasol
 *
 */
public class IjoomerVoiceAndTextMessager extends LinearLayout {

    private LinearLayout lnrRecorder;
    private IjoomerTextView txtCompleteMessage;
    private IjoomerEditText edtMessage;
    private IjoomerButton btnVoiceMsg;
    private IjoomerButton btnSend;
    private IjoomerGifView gifRecorder;
    private TextView txtTimer;
    private ImageView imgVoiceMsg;
    private ImageView imgTextMsg;
    private ImageView imgSmiley;
    private ImageView imgMicSymbol;
    private TwoWayGridView igvEmojis;
    private Context context;
    private MessageHandler messageHandler;
    private MediaRecorder mRecorder = null;
    private Timer timer;
    private String mFileName;
    private AQuery androidQuery;
    private boolean isMaxLengthReached;
    private boolean BOTH;
    private boolean VOICEONLY;
    private boolean TEXTONLY;
    private boolean isSmileyAllow;
    private boolean canBlank;
    private boolean showPopup;
    private boolean isSpaceAvailable = true;
    public static final int TEXT = 1;
    public static final int VOICE = 2;
    private int voiceButtonImageResourceId;
    private int textButtonImageResourceId;
    private int smileyButtonImageResourceId;
    private int micSymbol;
    private int gifRecorderImagetResourceId;
    private int sendButtonBackground;
    private int voiceButtonBackground;
    private int voiceButtonCaption;
    private int senddButtonCaption;
    private int messageHint;
    private int MESSAGER;
    private int minute;
    private int seconds = -1;

    private ArrayList<SmartListItem> emojisList = new ArrayList<SmartListItem>();
    private SmartListAdapterWithHolder emojisAdapter;

    private LinkedHashMap<String,Integer> emojisMap;
    private EmojisListener emojisListener;

    public EmojisListener getEmojisListener() {
        return emojisListener;
    }

    public void setEmojisListener(EmojisListener emojisListener) {
        this.emojisListener = emojisListener;
    }

    @SuppressLint("NewApi")
    public IjoomerVoiceAndTextMessager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }

    public IjoomerVoiceAndTextMessager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public IjoomerVoiceAndTextMessager(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    private void init(AttributeSet attrs) {

        setGravity(Gravity.CENTER_VERTICAL);
        setIsSmileyAllow(true);
        if (attrs == null) {
            voiceButtonImageResourceId = R.drawable.ijoomer_mic_icon;
            textButtonImageResourceId = R.drawable.ijoomer_text_icon;
            smileyButtonImageResourceId = R.drawable.ijoomer_smiley_icon;
            micSymbol = R.drawable.ijoomer_mic_symbol;
            gifRecorderImagetResourceId = R.drawable.ijoomer_recording_gif;
            voiceButtonCaption = R.string.push_to_talk;
            senddButtonCaption = R.string.send;
            sendButtonBackground = 0;
            voiceButtonBackground = 0;
            showPopup = true;
            BOTH = true;
            TEXTONLY = false;
            VOICEONLY = false;
            MESSAGER = VOICE;

        } else {

            String namespace = "http://schemas.android.com/apk/res/" + getContext().getPackageName();

            int messagerValue = attrs.getAttributeIntValue(namespace, "messager", 3);
            if (messagerValue == 3) {
                BOTH = true;
                MESSAGER = VOICE;
            } else if (messagerValue == 2) {
                VOICEONLY = true;
                MESSAGER = VOICE;
            } else if (messagerValue == 1) {
                TEXTONLY = true;
                MESSAGER = TEXT;
            }
            voiceButtonImageResourceId = attrs.getAttributeResourceValue(namespace, "voice_image_icon", R.drawable.ijoomer_mic_icon);
            textButtonImageResourceId = attrs.getAttributeResourceValue(namespace, "text_image_icon", R.drawable.ijoomer_text_icon);
            smileyButtonImageResourceId = attrs.getAttributeResourceValue(namespace, "smiley_image_icon", R.drawable.ijoomer_smiley_icon);

            micSymbol = attrs.getAttributeResourceValue(namespace, "mic_image", R.drawable.ijoomer_mic_symbol);
            gifRecorderImagetResourceId = attrs.getAttributeResourceValue(namespace, "recording_gif_image", R.drawable.ijoomer_recording_gif);

            sendButtonBackground = attrs.getAttributeResourceValue(namespace, "send_button_background", 0);
            voiceButtonBackground = attrs.getAttributeResourceValue(namespace, "voice_button_background", 0);

            voiceButtonCaption = attrs.getAttributeResourceValue(namespace, "voice_button_caption", R.string.push_to_talk);
            senddButtonCaption = attrs.getAttributeResourceValue(namespace, "send_button_caption", R.string.send);

            messageHint = attrs.getAttributeResourceValue(namespace, "message_hint", 0);
            canBlank = attrs.getAttributeBooleanValue(namespace, "can_blank", false);
            showPopup = attrs.getAttributeBooleanValue(namespace, "showpopup", true);
        }
        createView();
    }

    /**
     * This method used to create view.
     */
    private void createView() {

        androidQuery = new AQuery(context);
        setIsSmileyAllow(true);
        if (showPopup) {
            if (imgTextMsg == null) {

                View v = LayoutInflater.from(getContext()).inflate(R.layout.ijoomer_voice_text_messager, null);
                lnrRecorder = (LinearLayout) v.findViewById(R.id.lnrRecorder);
                imgVoiceMsg = (ImageView) v.findViewById(R.id.imgVoiceMsg);
                imgTextMsg = (ImageView) v.findViewById(R.id.imgTextMsg);
                imgSmiley = (ImageView) v.findViewById(R.id.imgSmiley);
                edtMessage = (IjoomerEditText) v.findViewById(R.id.edtMessage);
                btnVoiceMsg = (IjoomerButton) v.findViewById(R.id.btnVoiceMsg);
                btnSend = (IjoomerButton) v.findViewById(R.id.btnSend);
                btnSend.setPadding(convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7), convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7));
                imgMicSymbol = (ImageView) v.findViewById(R.id.imgMicSymbol);
                txtTimer = (IjoomerTextView) v.findViewById(R.id.txtTimer);
                gifRecorder = (IjoomerGifView) v.findViewById(R.id.gifRecorder);
                txtCompleteMessage = (IjoomerTextView) v.findViewById(R.id.txtCompleteMessage);
                igvEmojis = (TwoWayGridView) v.findViewById(R.id.igvEmojis);
                igvEmojis.setVisibility(GONE);

                addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                updateView();
                setActionListener();

            }
        } else {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.ijoomer_voice_text_messager_without_popup, null);

            imgVoiceMsg = (ImageView) v.findViewById(R.id.imgVoiceMsg);
            imgTextMsg = (ImageView) v.findViewById(R.id.imgTextMsg);
            imgSmiley = (ImageView) v.findViewById(R.id.imgSmiley);
            edtMessage = (IjoomerEditText) v.findViewById(R.id.edtMessage);
            btnVoiceMsg = (IjoomerButton) v.findViewById(R.id.btnVoiceMsg);
            btnSend = (IjoomerButton) v.findViewById(R.id.btnSend);
            btnSend.setPadding(convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7), convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7));
            txtTimer = (IjoomerTextView) v.findViewById(R.id.txtTimer);
            gifRecorder = (IjoomerGifView) v.findViewById(R.id.gifRecorder);
            txtCompleteMessage = (IjoomerTextView) v.findViewById(R.id.txtCompleteMessage);

            igvEmojis = (TwoWayGridView) v.findViewById(R.id.igvEmojis);
            igvEmojis.setVisibility(GONE);
            addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            updateView();
            setActionListener();

        }

    }

    /**
     * This method used to update view.
     */
    private void updateView() {
        androidQuery = new AQuery(context);
        prepareEmojisList();
        if (emojisAdapter == null) {
            emojisAdapter = getEmojisGridAdapter();
        }
        igvEmojis.setAdapter(emojisAdapter);
        if (showPopup) {
            txtTimer.setText("00:00");
            imgVoiceMsg.setImageResource(getVoiceButtonImageResourceId());
            imgSmiley.setImageResource(getSmileyButtonImageResourceId());
            imgTextMsg.setImageResource(getTextButtonImageResourceId());
            imgMicSymbol.setImageResource(getMicSymbol());
            btnSend.setText(getContext().getString(getSenddButtonCaption()));
            btnSend.setPadding(convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7), convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7));
            btnVoiceMsg.setText(getContext().getString(getVoiceButtonCaption()));
            gifRecorder.setGifImageResourceID(getGifRecorderImagetResourceId());
            edtMessage.setHint(getMessageHint() != 0 ? getContext().getString(getMessageHint()) : "");
            edtMessage.setDecodeEmojis(true);

            if (getVoiceButtonBackground() != 0) {
                btnVoiceMsg.setBackgroundColor(getVoiceButtonBackground());
            }
            if (getSendButtonBackground() != 0) {
                btnSend.setBackgroundColor(getSendButtonBackground());
            }

            lnrRecorder.setVisibility(View.GONE);
            txtCompleteMessage.setVisibility(View.GONE);

            if (!IjoomerGlobalConfiguration.isEnableVoice()) {
                imgVoiceMsg.setVisibility(View.GONE);
                imgTextMsg.setVisibility(View.GONE);
                btnVoiceMsg.setVisibility(View.GONE);
                edtMessage.setVisibility(View.VISIBLE);
                if (isSmileyAllow()) {
                    imgSmiley.setVisibility(VISIBLE);
                }

            } else if (VOICEONLY) {
                imgVoiceMsg.setVisibility(View.GONE);
                imgTextMsg.setVisibility(View.GONE);
                btnVoiceMsg.setVisibility(View.VISIBLE);
                edtMessage.setVisibility(View.GONE);
                btnSend.setVisibility(View.GONE);
            } else if (TEXTONLY) {
                imgVoiceMsg.setVisibility(View.GONE);
                imgTextMsg.setVisibility(View.GONE);
                btnVoiceMsg.setVisibility(View.GONE);
                edtMessage.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                if (isSmileyAllow()) {
                    imgSmiley.setVisibility(VISIBLE);
                }
            } else {
                if (MESSAGER == TEXT) {
                    imgVoiceMsg.setVisibility(View.VISIBLE);
                    imgTextMsg.setVisibility(View.GONE);
                    if (isSmileyAllow()) {
                        imgSmiley.setVisibility(VISIBLE);
                    }

                    edtMessage.setVisibility(View.VISIBLE);
                    btnVoiceMsg.setVisibility(View.GONE);

                } else {
                    imgVoiceMsg.setVisibility(View.GONE);
                    imgTextMsg.setVisibility(View.VISIBLE);
                    if (isSmileyAllow()) {
                        imgSmiley.setVisibility(GONE);
                        if (igvEmojis.getVisibility() == VISIBLE) {
                            igvEmojis.setVisibility(GONE);
                        }
                    }

                    edtMessage.setVisibility(View.GONE);
                    btnVoiceMsg.setVisibility(View.VISIBLE);

                }
            }
        } else {
            txtTimer.setText("00:00");
            imgVoiceMsg.setImageResource(getVoiceButtonImageResourceId());
            imgSmiley.setImageResource(getSmileyButtonImageResourceId());
            imgTextMsg.setImageResource(getTextButtonImageResourceId());
            btnSend.setText(getContext().getString(getSenddButtonCaption()));
            btnSend.setPadding(convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7), convertSizeToDeviceDependent(5), convertSizeToDeviceDependent(7));
            btnVoiceMsg.setText(getContext().getString(getVoiceButtonCaption()));
            gifRecorder.setGifImageResourceID(getGifRecorderImagetResourceId());
            edtMessage.setHint(getMessageHint() != 0 ? getContext().getString(getMessageHint()) : "");
            edtMessage.setDecodeEmojis(true);

            if (getVoiceButtonBackground() != 0) {
                btnVoiceMsg.setBackgroundColor(getVoiceButtonBackground());
            }
            if (getSendButtonBackground() != 0) {
                btnSend.setBackgroundColor(getSendButtonBackground());
            }

            txtCompleteMessage.setVisibility(View.GONE);

            if (!IjoomerGlobalConfiguration.isEnableVoice()) {
                imgVoiceMsg.setVisibility(View.GONE);
                imgTextMsg.setVisibility(View.GONE);
                btnVoiceMsg.setVisibility(View.GONE);
                edtMessage.setVisibility(View.VISIBLE);
                if (isSmileyAllow()) {
                    imgSmiley.setVisibility(VISIBLE);
                }
            } else if (VOICEONLY) {
                imgVoiceMsg.setVisibility(View.GONE);
                imgTextMsg.setVisibility(View.GONE);
                btnVoiceMsg.setVisibility(View.VISIBLE);
                edtMessage.setVisibility(View.GONE);
                btnSend.setVisibility(View.GONE);
            } else if (TEXTONLY) {
                imgVoiceMsg.setVisibility(View.GONE);
                imgTextMsg.setVisibility(View.GONE);
                btnVoiceMsg.setVisibility(View.GONE);
                edtMessage.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.VISIBLE);
                if (isSmileyAllow()) {
                    imgSmiley.setVisibility(VISIBLE);
                }
            } else {
                if (MESSAGER == TEXT) {
                    imgVoiceMsg.setVisibility(View.VISIBLE);
                    imgTextMsg.setVisibility(View.GONE);
                    if (isSmileyAllow()) {
                        imgSmiley.setVisibility(VISIBLE);
                    }

                    edtMessage.setVisibility(View.VISIBLE);
                    btnVoiceMsg.setVisibility(View.GONE);

                } else {
                    imgVoiceMsg.setVisibility(View.GONE);
                    imgTextMsg.setVisibility(View.VISIBLE);
                    if (isSmileyAllow()) {
                        imgSmiley.setVisibility(GONE);
                        if (igvEmojis.getVisibility() == VISIBLE) {
                            igvEmojis.setVisibility(GONE);
                        }
                    }

                    edtMessage.setVisibility(View.GONE);
                    btnVoiceMsg.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    private boolean isSmileyAllow() {
        if(IjoomerApplicationConfiguration.isEnableSmiley){
            return isSmileyAllow;
        }else{
            return false;
        }
    }

    public void setIsSmileyAllow(boolean isSmileyAllow) {
        this.isSmileyAllow = isSmileyAllow;
    }

    /**
     * This method used to set action listener.
     */
    private void setActionListener() {
        btnSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                igvEmojis.setVisibility(GONE);
                String msg = edtMessage.getText().toString();
                edtMessage.setText("");

                if (!isCanBlank()) {
                    if (msg.trim().length() > 0) {
                        if (getMessageHandler() != null) {
                            messageHandler.onButtonSend(msg);
                        }
                    }
                } else {
                    messageHandler.onButtonSend(msg);
                }
            }
        });
        btnVoiceMsg.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                handleStartRecording();
                return true;
            }
        });
        btnVoiceMsg.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handleStopRecording();
                }
                return false;
            }
        });

        imgTextMsg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MESSAGER = TEXT;
                updateView();
                if (messageHandler != null) {
                    messageHandler.onToggle(MESSAGER);
                }
            }
        });

        imgVoiceMsg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MESSAGER = VOICE;
                updateView();
                if (messageHandler != null) {
                    messageHandler.onToggle(MESSAGER);
                }

            }
        });

        imgSmiley.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (igvEmojis.getVisibility() == VISIBLE) {
                    igvEmojis.setVisibility(GONE);
                } else {
                    prepareEmojisList();
                    igvEmojis.setVisibility(VISIBLE);
                    edtMessage.setSelection(edtMessage.getText().length());
                }
            }
        });

        edtMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                igvEmojis.setVisibility(GONE);
                edtMessage.setSelection(edtMessage.getText().length());
            }
        });

    }

    /**
     * This method used to toggle messager.
     */
    public void toggelMessager() {
        if (MESSAGER == TEXT) {
            MESSAGER = VOICE;
        } else {
            MESSAGER = TEXT;
        }
        updateView();
        if (messageHandler != null) {
            messageHandler.onToggle(MESSAGER);
        }
    }

    /**
     * This method used to start recording.
     */
    private void startRecording() {
        isMaxLengthReached = false;
        isSpaceAvailable = true;
        txtCompleteMessage.setVisibility(View.GONE);
        txtCompleteMessage.setText(getContext().getString(R.string.max_audio_length));
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mFileName = getRecordDefaultFileName();
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                System.out.println("prepare() failed");
            }

            mRecorder.start();
        } catch (Exception e) {
            txtCompleteMessage.setVisibility(View.VISIBLE);
            txtCompleteMessage.setText(getContext().getString(R.string.no_space));
            isSpaceAvailable = false;
            return;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (((minute * 60) + (seconds)) >= IjoomerGlobalConfiguration.getMaxAudioLength()) {
                    isMaxLengthReached = true;
                    timer.cancel();
                    mRecorder.stop();
                    mRecorder.release();
                    ((Activity) getContext()).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            txtCompleteMessage.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    if (seconds >= 60) {
                        minute = minute + 1;
                        seconds = 0;
                    }
                    seconds = seconds + 1;
                    ((Activity) getContext()).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (seconds <= 9) {
                                if (minute <= 9) {
                                    txtTimer.setText("0" + minute + ":0" + seconds);
                                } else {
                                    txtTimer.setText(minute + ":0" + seconds);
                                }
                            } else {
                                if (minute <= 9) {
                                    txtTimer.setText("0" + minute + ":" + seconds);
                                } else {
                                    txtTimer.setText(minute + ":" + seconds);
                                }
                            }
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    /**
     * This method used to get record default file name.
     *
     * @return represented {@link String}
     */
    private String getRecordDefaultFileName() {
        // String fileName;
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "iJoomerAdvance" + "/");
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        // if (wallpaperDirectory.listFiles() != null) {
        // fileName = "record" + wallpaperDirectory.listFiles().length;
        // } else {
        // fileName = "record" + 1;
        // }

        return wallpaperDirectory.getAbsolutePath() + File.separator + "iarecord" + ".3gp";
    }

    /**
     * this method used to stop recording.
     */
    private void stopRecording() {
        try {
            if (mRecorder != null) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                minute = 0;
                seconds = -1;
                try {
                    if (!isMaxLengthReached) {
                        mRecorder.stop();
                        mRecorder.release();
                    }
                } catch (Throwable e) {
                }
                mRecorder = null;
            }
        } catch (Exception e) {
        }
    }

    /**
     * This method used to set voice only.
     */
    public void setVoiceOnly() {
        TEXTONLY = false;
        VOICEONLY = true;
        BOTH = false;
        MESSAGER = VOICE;
        updateView();
    }

    /**
     * This method used to set text only.
     */
    public void setTextOnly() {
        TEXTONLY = true;
        VOICEONLY = false;
        BOTH = false;
        MESSAGER = TEXT;
        updateView();
    }

    /**
     * This method used to is show Popup.
     *
     * @return represented {@link Boolean}
     */
    public boolean isShowPop() {
        return showPopup;
    }

    /**
     * This method used to set is show Popup.
     *
     * @param showPopup
     *            represented {@link Boolean}
     */
    public void setShowPop(boolean showPopup) {
        this.showPopup = showPopup;
    }

    /**
     * This method used to get current messager.
     *
     * @return represented {@link Integer}
     */
    public int getCurrentMessager() {
        return MESSAGER;
    }

    /**
     * This method used to set current messager.
     *
     * @param messager
     *            represented messager
     */
    public void setCurrentMessager(int messager) {
        MESSAGER = messager;
    }

    /**
     * This method used to get edittext message string.
     *
     * @return represented {@link String}
     */
    public String getMessageString() {
        return edtMessage.getText().toString();
    }

    /**
     * This method used to set edittext message string.
     *
     * @param messageString
     *            represented edittext message
     */
    public void setMessageString(String messageString) {
        edtMessage.setText(messageString);
        edtMessage.setSelection(messageString.length());
    }

    /**
     * This method used to get edittext message hint.
     *
     * @return represented {@link Integer}
     */
    private int getMessageHint() {
        return messageHint;
    }

    /**
     * This method used to set edittext message hint.
     *
     * @param msessageHint
     *            represented edittext message hint
     */
    public void setMessageHint(int msessageHint) {
        this.messageHint = msessageHint;
        updateView();
    }

    /**
     * This method used to check is can blank.
     *
     * @return represented {@link Boolean}
     */
    private boolean isCanBlank() {
        return canBlank;
    }

    /**
     * This method used to set is can blank.
     *
     * @param canBlank
     *            represented can blank
     */
    public void setCanBlank(boolean canBlank) {
        this.canBlank = canBlank;
    }

    /**
     * This method used to get message handler.
     *
     * @return represented {@link MessageHandler}
     */
    private MessageHandler getMessageHandler() {
        return messageHandler;
    }

    /**
     * This method used to set message handler
     *
     * @param messageHandler
     *            represented message handler
     */
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * This method used to get voice button image resource id.
     *
     * @return represented {@link Integer}
     */
    private int getVoiceButtonImageResourceId() {
        return voiceButtonImageResourceId;
    }

    /**
     * This method used to set voice button resource id.
     *
     * @param voiceButtonImageResourceId
     *            represented voice button image resource id
     */
    public void setVoiceButtonImageResourceId(int voiceButtonImageResourceId) {
        this.voiceButtonImageResourceId = voiceButtonImageResourceId;
    }

    /**
     * This method used to get voice text button image resource id.
     *
     * @return represented {@link Integer}
     */
    private int getTextButtonImageResourceId() {
        return textButtonImageResourceId;
    }

    /**
     * This method used to set voice text button image resource id.
     *
     * @param textButtonImageResourceId
     *            represented voice text image resource image id
     */
    public void setTextButtonImageResourceId(int textButtonImageResourceId) {
        this.textButtonImageResourceId = textButtonImageResourceId;
    }

    /**
     * This method used to get voice smiley button image resource id.
     *
     * @return represented {@link Integer}
     */
    private int getSmileyButtonImageResourceId() {
        return smileyButtonImageResourceId;
    }

    /**
     * This method used to set voice smiley button image resource id.
     *
     * @param smileyButtonImageResourceId
     *            represented voice text image resource image id
     */
    public void setSmileyButtonImageResourceId(int smileyButtonImageResourceId) {
        this.smileyButtonImageResourceId = smileyButtonImageResourceId;
    }

    /**
     * This method used to get mic symbol.
     *
     * @return represented {@link Integer}
     */
    private int getMicSymbol() {
        return micSymbol;
    }

    /**
     * This method used to set mic symbol.
     *
     * @param micSymbol
     *            represented mic symbol id
     */
    public void setMicSymbol(int micSymbol) {
        this.micSymbol = micSymbol;
    }

    /**
     * This method used to get gif recorder image resource id.
     *
     * @return represented {@link Integer}
     */
    private int getGifRecorderImagetResourceId() {
        return gifRecorderImagetResourceId;
    }

    /**
     * This method used to set gif recorder image resource id
     *
     * @param gifRecorderImagetResourceId
     *            gif image id
     */
    public void setGifRecorderImagetResourceId(int gifRecorderImagetResourceId) {
        this.gifRecorderImagetResourceId = gifRecorderImagetResourceId;
    }

    /**
     * This method used to get send button background.
     *
     * @return represented {@link Integer}
     */
    private int getSendButtonBackground() {
        return sendButtonBackground;
    }

    /**
     * This method used to set send button background id.
     *
     * @param sendButtonBackground
     *            represented button background id
     */
    public void setSendButtonBackground(int sendButtonBackground) {
        this.sendButtonBackground = sendButtonBackground;
    }

    /**
     * This method used to get voice button background id.
     *
     * @return represented {@link Integer}
     */
    private int getVoiceButtonBackground() {
        return voiceButtonBackground;
    }

    /**
     * This method used to set voice button background id.
     *
     * @param voiceButtonBackground
     *            represented voice button background id
     */
    public void setVoiceButtonBackground(int voiceButtonBackground) {
        this.voiceButtonBackground = voiceButtonBackground;
    }

    /**
     * This method used to get voice button caption id.
     *
     * @return represented {@link Integer}
     */
    private int getVoiceButtonCaption() {
        return voiceButtonCaption;
    }

    /**
     * This method used to set voice button caption id.
     *
     * @param voiceButtonCaption
     *            represented button caption id
     */
    public void setVoiceButtonCaption(int voiceButtonCaption) {
        this.voiceButtonCaption = voiceButtonCaption;
    }

    /**
     * This method used to get send button caption id.
     *
     * @return represented {@link Integer}
     */
    private int getSenddButtonCaption() {
        return senddButtonCaption;
    }

    /**
     * This method used to set send button caption id.
     *
     * @param setndButtonCaption
     *            represented button caption id
     */
    public void setSenddButtonCaption(int setndButtonCaption) {
        this.senddButtonCaption = setndButtonCaption;
    }

    /**
     * This method used to convert value to device dependent size.
     *
     * @param value
     *            represented value
     * @return represented {@link Integer}
     */
    public int convertSizeToDeviceDependent(int value) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return ((dm.densityDpi * value) / 160);
    }

    /**
     * Inner Interface
     *
     * @author tasol
     *
     */
    public interface MessageHandler {
        void onVoiceMessageRecordingComplete(String message, String voiceMessagePath);

        void onButtonSend(String message);

        void onToggle(int messager);
    }

    private void handleStartRecording() {
        if (mRecorder == null) {
            if (showPopup) {
                lnrRecorder.setVisibility(View.VISIBLE);
            } else {
                if (BOTH) {
                    btnSend.setVisibility(View.GONE);
                    imgTextMsg.setVisibility(View.GONE);
                }
                txtTimer.setVisibility(View.VISIBLE);
                gifRecorder.setVisibility(View.VISIBLE);
            }
            txtTimer.setText("00:00");
            startRecording();
        }
    }

    private void handleStopRecording() {
        if (mRecorder != null) {

            int tmpSec = seconds;
            int tmpMin = minute;

            stopRecording();
            if (showPopup) {
                lnrRecorder.setVisibility(View.GONE);
            } else {
                if (BOTH) {
                    btnSend.setVisibility(View.VISIBLE);
                    imgTextMsg.setVisibility(View.VISIBLE);
                }
                gifRecorder.setVisibility(View.GONE);
                txtTimer.setVisibility(View.GONE);
            }
            if (getMessageHandler() != null && isSpaceAvailable && (tmpMin > 0 || tmpSec > 0)) {
                edtMessage.setText("");
                messageHandler.onVoiceMessageRecordingComplete("", mFileName);
            }
        }
    }

    public interface EmojisListener{
        LinkedHashMap<String,Integer> getEmojisSet();
    }

    private void prepareEmojisList() {
        emojisList.clear();
        if(getEmojisListener()!=null && getEmojisListener().getEmojisSet()!=null){
            emojisMap = getEmojisListener().getEmojisSet();
            Iterator<String> itr  = emojisMap.keySet().iterator();
            while (itr.hasNext()) {
                String rowKey = itr.next();
                LinkedHashMap<String, Integer> row = new LinkedHashMap<String, Integer>();
                row.put(rowKey, emojisMap.get(rowKey));
                SmartListItem item = new SmartListItem();
                item.setItemLayout(R.layout.ijoomer_message_emojis_grid_item);
                ArrayList<Object> obj = new ArrayList<Object>();
                obj.add(row);
                item.setValues(obj);
                emojisList.add(item);
            }
        }else{
            Iterator<String> itr  = IjoomerUtilities.getEmojisHashMap().keySet().iterator();
            while (itr.hasNext()) {
                String rowKey = itr.next();
                LinkedHashMap<String, Integer> row = new LinkedHashMap<String, Integer>();
                row.put(rowKey, IjoomerUtilities.getEmojisHashMap().get(rowKey));
                SmartListItem item = new SmartListItem();
                item.setItemLayout(R.layout.ijoomer_message_emojis_grid_item);
                ArrayList<Object> obj = new ArrayList<Object>();
                obj.add(row);
                item.setValues(obj);
                emojisList.add(item);
            }
        }


    }

    /**
     * List adapter for message.
     */

    private SmartListAdapterWithHolder getEmojisGridAdapter() {

        SmartListAdapterWithHolder adapterWithHolder = new SmartListAdapterWithHolder(context, R.layout.ijoomer_message_emojis_grid_item, emojisList, new ItemView() {
            @Override
            public View setItemView(final int position, View v, SmartListItem item, final ViewHolder holder) {
                holder.imgEmojis = (ImageView) v.findViewById(R.id.imgEmojis);

                final LinkedHashMap<String, Integer> row = (LinkedHashMap<String, Integer>) item.getValues().get(0);
                Iterator<String> itr = row.keySet().iterator();
                if (itr.hasNext()) {
                    String emojisCode = itr.next();
                    int emojisImageId = row.get(emojisCode);
                    androidQuery.id(holder.imgEmojis).image(emojisImageId);
                    holder.imgEmojis.setTag(emojisCode);
                }
                holder.imgEmojis.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String id = view.getTag().toString();
                        edtMessage.setText(id);
                    }
                });

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
