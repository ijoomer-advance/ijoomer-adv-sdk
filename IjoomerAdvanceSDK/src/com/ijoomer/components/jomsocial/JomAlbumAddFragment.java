package com.ijoomer.components.jomsocial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.ijoomer.common.classes.IjoomerMapAddress;
import com.ijoomer.common.classes.IjoomerSuperMaster;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.library.jomsocial.JomGalleryDataProvider;
import com.ijoomer.src.R;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;
import com.smart.framework.SmartFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomAlbumAddFragment.
 * 
 * @author tasol
 * 
 */
@SuppressLint("ValidFragment")
public class JomAlbumAddFragment extends SmartFragment implements JomTagHolder {

	@SuppressWarnings("unused")
	private IjoomerTextView txtAlbumWhoCanSee;
	private IjoomerEditText edtAlbumName;
	private IjoomerEditText edtAlbumLocation;
	private IjoomerEditText edtAlbumDescription;
	private IjoomerButton btnCancle;
	private IjoomerButton btnCreate;
	private ImageView imgMap;
	private Spinner spnWhoCanSee;

	private JomGalleryDataProvider providerAlbum;

	private String IN_GROUP_ID;

	final private int GET_ADDRESS_FROM_MAP = 1;

	public JomAlbumAddFragment() {
	}

	/**
	 * Overrides method
	 */

	@Override
	public int setLayoutId() {
		return R.layout.jom_album_add_fragment;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {

		txtAlbumWhoCanSee = (IjoomerTextView) currentView.findViewById(R.id.txtAlbumWhoCanSee);
		edtAlbumName = (IjoomerEditText) currentView.findViewById(R.id.edtAlbumName);
		edtAlbumLocation = (IjoomerEditText) currentView.findViewById(R.id.edtAlbumLocation);
		edtAlbumDescription = (IjoomerEditText) currentView.findViewById(R.id.edtAlbumDescription);
		btnCancle = (IjoomerButton) currentView.findViewById(R.id.btnCancle);
		btnCreate = (IjoomerButton) currentView.findViewById(R.id.btnCreate);
		imgMap = (ImageView) currentView.findViewById(R.id.imgMap);
		spnWhoCanSee = (Spinner) currentView.findViewById(R.id.spnWhoCanSee);

		providerAlbum = new JomGalleryDataProvider(getActivity());

		getIntentData();
	}

	@Override
	public void prepareViews(View currentView) {
		spnWhoCanSee.setAdapter(new IjoomerUtilities.MyCustomAdapter(getActivity(), new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.wall_post_type)))));

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					final String address = IjoomerUtilities.getAddressFromLatLong(0, 0).getSubAdminArea();

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							edtAlbumLocation.setText(address);
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

		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((IjoomerSuperMaster) getActivity()).hideSoftKeyboard();
				if (edtAlbumName.getText().toString().trim().length() > 0) {

					final SeekBar proSeekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_sending_request));
					Address address = IjoomerUtilities.getLatLongFromAddress(edtAlbumLocation.getText().toString().trim());
					providerAlbum.addAlbum("0", IN_GROUP_ID, edtAlbumName.getText().toString().trim(), edtAlbumDescription.getText().toString().trim(), address != null ? address.getLatitude() : 0, address != null ? address.getLongitude() : 0, ((JomMasterActivity) getActivity()).getPrivacyCode(spnWhoCanSee.getSelectedItem().toString().trim()).toString(), new WebCallListener() {

						@Override
						public void onProgressUpdate(int progressCount) {
							proSeekBar.setProgress(progressCount);
						}

						@Override
						public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
							try {
								if (responseCode == 200) {
                                    clearAlbumField();
									((JomMasterActivity) getActivity()).updateHeader(providerAlbum.getNotificationData());
									IjoomerApplicationConfiguration.setReloadRequired(true);
									((JomAlbumsActivity) getActivity()).onResume();
								} else {
									IjoomerUtilities.getCustomOkDialog(getString(R.string.add_album), getString(getResources().getIdentifier("code" + responseCode, "string", getActivity().getPackageName())), getString(R.string.ok), R.layout.ijoomer_ok_dialog, new CustomAlertNeutral() {

										@Override
										public void NeutralMethod() {
											IjoomerApplicationConfiguration.setReloadRequired(true);
											((JomAlbumsActivity) getActivity()).onResume();
										}
									});
								}
							} catch (Throwable e) {
							}

						}

					});
				} else {
					edtAlbumName.setError(getString(R.string.validation_value_required));
				}
			}
		});
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				IjoomerApplicationConfiguration.setReloadRequired(true);
				((JomAlbumsActivity) getActivity()).onResume();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == GET_ADDRESS_FROM_MAP) {
				edtAlbumLocation.setText(((HashMap<String, String>) data.getSerializableExtra("MAP_ADDRESSS_DATA")).get("address"));
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

	/**
	 * Class method
	 */

	/**
	 * This method used to get intent data.
	 */
	private void getIntentData() {
		IN_GROUP_ID = getActivity().getIntent().getStringExtra("IN_GROUP_ID") == null ? "0" : getActivity().getIntent().getStringExtra("IN_GROUP_ID");
	}

    private void clearAlbumField(){
        edtAlbumName.setText("");
        edtAlbumDescription.setText("");
        edtAlbumLocation.setText("");
    }


}
