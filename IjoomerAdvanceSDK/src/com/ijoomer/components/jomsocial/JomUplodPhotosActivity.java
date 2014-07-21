package com.ijoomer.components.jomsocial;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerAudioPlayer.AudioListener;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager;
import com.ijoomer.customviews.IjoomerVoiceAndTextMessager.MessageHandler;
import com.ijoomer.customviews.IjoomerVoiceButton;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomUplodPhotosActivity.
 *
 * @author tasol
 *
 */
public class JomUplodPhotosActivity extends JomMasterActivity {

    private LinearLayout lnrPhotos;
    private LayoutInflater mInflater;
    private IjoomerVoiceButton btnPlayVoice;
    private IjoomerButton btnUploadAll;
    private IjoomerButton btnCancelAll;
    private IjoomerVoiceAndTextMessager voiceMessager;
    private ImageView imgDisplay;

    private ArrayList<HashMap<String, String>> uploadData = new ArrayList<HashMap<String, String>>();

    private String[] IN_PHOTOS_PATHS;
    private String IN_ALBUM_ID;
    private int currentIndex = 0;

    /**
     * Overrides methods
     */

    @Override
    public int setLayoutId() {
        return R.layout.jom_upload_multiple_photos;
    }

    @Override
    public void initComponents() {
        mInflater = LayoutInflater.from(this);
        btnCancelAll = (IjoomerButton) findViewById(R.id.btnCancelAll);
        btnUploadAll = (IjoomerButton) findViewById(R.id.btnUploadAll);
        btnPlayVoice = (IjoomerVoiceButton) findViewById(R.id.btnPlayVoice);
        lnrPhotos = (LinearLayout) findViewById(R.id.lnrPhotos);
        imgDisplay = (ImageView) findViewById(R.id.imgDisplay);
        voiceMessager = (IjoomerVoiceAndTextMessager) findViewById(R.id.voiceMessager);
        btnPlayVoice.setCustomText(getString(R.string.play));
        btnPlayVoice.setReportVoice(false);
        getIntentData();
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
        if (IN_PHOTOS_PATHS != null) {
            imgDisplay.setImageBitmap(decodeFile(IN_PHOTOS_PATHS[0]));
            for (int i = 0; i < IN_PHOTOS_PATHS.length; i++) {
                lnrPhotos.addView(addItem(i), new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("imagePath", IN_PHOTOS_PATHS[i]);
                uploadData.add(item);
            }
        }
    }

    @Override
    public void setActionListeners() {

        btnPlayVoice.setAudioListener(new AudioListener() {

            @Override
            public void onReportClicked() {
            }

            @Override
            public void onPrepared() {
                btnPlayVoice.setCustomText(getString(R.string.stop));
            }

            @Override
            public void onPlayClicked(boolean isplaying) {
                String pathString = uploadData.get(currentIndex).get("voice");
                btnPlayVoice.setAudioPath(pathString, false);
                if (isplaying) {
                    btnPlayVoice.setCustomText(getString(R.string.play));
                } else {
                    btnPlayVoice.setCustomText(getString(R.string.stop));
                }
            }

            @Override
            public void onComplete() {
                btnPlayVoice.setCustomText(getString(R.string.play));
            }
        });
        btnCancelAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUploadAll.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                IjoomerUtilities.addToNotificationBar(getString(R.string.photo_upload_starts), getString(R.string.upload_photo), getString(R.string.photo_upload_starts));
                startUploasPhoto(0, IN_ALBUM_ID);
                finish();
            }
        });

        voiceMessager.setMessageHandler(new MessageHandler() {

            @Override
            public void onVoiceMessageRecordingComplete(String message, String voiceMessagePath) {
                HashMap<String, String> item = uploadData.get(currentIndex);
                item.put("voice", voiceMessagePath);
                btnPlayVoice.setVisibility(View.VISIBLE);
                ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setVisibility(View.VISIBLE);
                ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setImageResource(R.drawable.ijoomer_mic_icon);
            }

            @Override
            public void onButtonSend(String message) {
                HashMap<String, String> item = uploadData.get(currentIndex);
                item.put("caption", message);
                ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setVisibility(View.VISIBLE);
                ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setImageResource(R.drawable.ijoomer_text_icon);
            }

            @Override
            public void onToggle(int messager) {
                if (messager == IjoomerVoiceAndTextMessager.TEXT) {
                    if (uploadData.get(currentIndex).containsKey("caption")) {
                        voiceMessager.setMessageString(uploadData.get(currentIndex).get("caption"));
                    }
                }
            }
        });
    }

    /**
     * Class methods
     */

    /**
     * This method used to get intent data.
     */
    private void getIntentData() {
        IN_PHOTOS_PATHS = getIntent().getStringArrayExtra("IN_PHOTOS_PATHS");
        IN_ALBUM_ID = getIntent().getStringExtra("IN_ALBUM_ID");
    }

    /**
     * This method used to add item in view.
     *
     * @param index
     *            represented index
     * @return represented {@link View}
     */
    private View addItem(final int index) {
        final View item = mInflater.inflate(R.layout.jom_upload_multiple_photo_item, null);
        ImageView imgPhoto = (ImageView) item.findViewById(R.id.imgItem);
        final ImageView imgIndicatior = (ImageView) item.findViewById(R.id.imgIndicatior);
        imgPhoto.setImageBitmap(decodeFile(IN_PHOTOS_PATHS[index]));
        if (index == 0) {
            item.setBackgroundColor(Color.BLUE);
        }
        item.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                btnPlayVoice.destroy();
                btnPlayVoice.setCustomText(getString(R.string.play));

                for (int i = 0; i < IN_PHOTOS_PATHS.length; i++) {
                    View other = lnrPhotos.getChildAt(i);
                    other.setBackgroundColor(Color.TRANSPARENT);
                }

                if (uploadData.get(currentIndex).containsKey("voice")) {
                    ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setVisibility(View.VISIBLE);
                    ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setImageResource(R.drawable.ijoomer_mic_icon);
                } else if (uploadData.get(currentIndex).containsKey("caption")) {
                    ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setVisibility(View.VISIBLE);
                    ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setImageResource(R.drawable.ijoomer_text_icon);
                } else {
                    ((ImageView) (lnrPhotos.getChildAt(currentIndex)).findViewById(R.id.imgIndicatior)).setVisibility(View.GONE);
                }

                item.setBackgroundColor(Color.BLUE);
                currentIndex = index;
                imgDisplay.setImageBitmap(decodeFile(IN_PHOTOS_PATHS[index]));

                if (uploadData.get(currentIndex).containsKey("voice")) {
                    btnPlayVoice.setVisibility(View.VISIBLE);
                    imgIndicatior.setVisibility(View.VISIBLE);
                    imgIndicatior.setImageResource(R.drawable.ijoomer_mic_icon);
                    if (voiceMessager.getCurrentMessager() == IjoomerVoiceAndTextMessager.TEXT) {
                        voiceMessager.toggelMessager();
                    }
                } else {
                    imgIndicatior.setVisibility(View.VISIBLE);
                    imgIndicatior.setImageResource(R.drawable.ijoomer_mic_icon);
                    btnPlayVoice.setVisibility(View.GONE);

                    if (uploadData.get(currentIndex).containsKey("caption")) {
                        voiceMessager.setMessageString(uploadData.get(currentIndex).get("caption"));
                        imgIndicatior.setVisibility(View.VISIBLE);
                        imgIndicatior.setImageResource(R.drawable.ijoomer_text_icon);
                        if (voiceMessager.getCurrentMessager() == IjoomerVoiceAndTextMessager.VOICE) {
                            voiceMessager.toggelMessager();
                        }
                    } else {
                        voiceMessager.setMessageString("");
                        imgIndicatior.setVisibility(View.GONE);
                        imgIndicatior.setImageResource(R.drawable.ijoomer_text_icon);
                    }
                }

            }
        });
        return item;

    }

    /**
     * This method used to start upload photo.
     *
     * @param index
     *            represented photo index
     * @param albumID
     *            represented album id
     */
    private void startUploasPhoto(final int index, final String albumID) {

        if (index != (uploadData.size())) {
            String voice = uploadData.get(index).containsKey("voice") ? uploadData.get(index).get("voice") : null;
            String caption = uploadData.get(index).containsKey("caption") ? uploadData.get(index).get("caption") : null;
            String imagePath = uploadData.get(index).get("imagePath");

            new JomGalleryDataProvider(this).uploadPhoto(caption, voice, imagePath, albumID, new WebCallListener() {

                @Override
                public void onProgressUpdate(final int progressCount) {

                }

                @Override
                public void onCallComplete(final int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {

                    if (responseCode == 200) {
                        String ticker = (index + 1) + "/" + uploadData.size() + IjoomerUtilities.mSmartAndroidActivity.getString(R.string.photo_uploaded);
                        IjoomerUtilities.addToNotificationBar(ticker, IjoomerUtilities.mSmartAndroidActivity.getString(R.string.upload_photo), ticker);
                        startUploasPhoto(index + 1, albumID);
                    } else {

                        if (errorMessage != null && errorMessage.length() > 0) {
                            IjoomerUtilities.addToNotificationBar(IjoomerUtilities.mSmartAndroidActivity.getString(R.string.photo_upload_failure),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.upload_photo), errorMessage);
                        } else {
                            IjoomerUtilities.addToNotificationBar(
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.photo_upload_failure),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(R.string.upload_photo),
                                    IjoomerUtilities.mSmartAndroidActivity.getString(IjoomerUtilities.mSmartAndroidActivity.getResources().getIdentifier("code" + responseCode,
                                            "string", IjoomerUtilities.mSmartAndroidActivity.getPackageName())));
                        }

                    }
                }
            });
        }else{
            if(IjoomerUtilities.mSmartAndroidActivity instanceof JomAlbumsActivity){
                IjoomerApplicationConfiguration.setReloadRequired(true);
                ((JomAlbumsActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
            }else if(IjoomerUtilities.mSmartAndroidActivity instanceof JomAlbumsDetailsActivity){
                IjoomerApplicationConfiguration.setReloadRequired(true);
                JomAlbumsDetailsActivity.PHOTO_COUNT += uploadData.size();
                ((JomAlbumsDetailsActivity)IjoomerUtilities.mSmartAndroidActivity).onResume();
            }else if(IjoomerUtilities.mSmartAndroidActivity instanceof JomPhotoDetailsActivity || IjoomerUtilities.mSmartAndroidActivity instanceof JomPhotoTagActivity){
                IjoomerApplicationConfiguration.setReloadRequired(true);
                JomAlbumsDetailsActivity.PHOTO_COUNT += uploadData.size();
            }
        }

    }
}
