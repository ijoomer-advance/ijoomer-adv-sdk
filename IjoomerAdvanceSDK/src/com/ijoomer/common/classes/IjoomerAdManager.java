package com.ijoomer.common.classes;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class IjoomerAdManager {

	static IjoomerAdManager adManager;
	Activity activity;

	public interface OnAddLoadListener {
		void onLoadComplete();
	}

	private IjoomerAdManager() {
	}

	public static IjoomerAdManager getInstance() {

		if (adManager == null)
			adManager = new IjoomerAdManager();
		return adManager;
	}

	public View getTopAdvertisement(Activity activity) {

		this.activity = activity;
		// code to get AdItemData

		// if adType is AdMob
		return getAdFromAdMob("", AdSize.BANNER, null);

	}

	public View getBottomAdvertisement(Activity activity) {
		this.activity = activity;
		// code to get AdItemData

		// if adType is AdMob
		return getAdFromAdMob("", AdSize.BANNER, null);

	}

	public void getOnLoadAdvertisement(Activity activity) {
//		this.activity = activity;
//		// code to get AdItemData
//
//		// if adType is AdMob
//		final Dialog dialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
//
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.ijoomer_onload_ad_dialog);
//		dialog.setCancelable(false);
//		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.lnrView);
//		final ImageView image = (ImageView) dialog.findViewById(R.id.imgClose);
//
//		if (STRICT_MODE) {
//			image.setVisibility(View.GONE);
//		}
//		layout.addView(getAdFromAdMob("0445b7141d9d4e1b", AdSize.MEDIUM_RECTANGLE, new OnAddLoadListener() {
//
//			@Override
//			public void onLoadComplete() {
//				image.setVisibility(View.VISIBLE);
//			}
//		}));
//		image.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		dialog.show();

	}

	public View getMediumRectAdvertisement(Activity activity) {
		this.activity = activity;
		// code to get AdItemData

		// if adType is AdMob
		return getAdFromAdMob("", AdSize.MEDIUM_RECTANGLE, new OnAddLoadListener() {

			@Override
			public void onLoadComplete() {
			}
		});

	}

	public View getAdFromAdMob(String addId, AdSize adSize, final OnAddLoadListener target) {
		if (addId != null && addId.trim().length() > 0) {
			RelativeLayout addLayout = new RelativeLayout(activity);
			final ProgressBar pbr = new ProgressBar(activity);

			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.CENTER_IN_PARENT);
			pbr.setLayoutParams(lp);
			addLayout.setLayoutParams(rlp);

			addLayout.addView(pbr);
			AdView adView = new AdView(activity);
			adView.setAdUnitId(addId);
			adView.setAdSize(adSize);
			adView.setAdListener(new AdListener() {

				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					if (pbr.isShown()) {
						pbr.setVisibility(View.GONE);
					}
					if(target!=null){
						target.onLoadComplete();
					}
				}

				@Override
				public void onAdFailedToLoad(int errorCode) {
					super.onAdFailedToLoad(errorCode);
					if (pbr.isShown()) {
						pbr.setVisibility(View.GONE);
					}
					if(target!=null){
						target.onLoadComplete();
					}
				}

				@Override
				public void onAdOpened() {
					super.onAdOpened();

				}
			});
			addLayout.addView(adView);
			AdRequest.Builder builder = new AdRequest.Builder();
			builder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
			adView.loadAd(builder.build());
			return addLayout;
		} else {
			return null;
		}
	}

}
