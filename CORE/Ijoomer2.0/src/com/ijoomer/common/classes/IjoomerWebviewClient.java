package com.ijoomer.common.classes;

import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.src.R;

public class IjoomerWebviewClient extends IjoomerSuperMaster {

	private WebView webExternalLinks;
	private IjoomerButton btnClose;
	private String link;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
	}

	@Override
	public int setLayoutId() {
		return R.layout.ijoomer_custom_webview;
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
	public void initComponents() {
		webExternalLinks = (WebView) findViewById(R.id.webExternalLinks);
		btnClose = (IjoomerButton) findViewById(R.id.btnClose);
	}

	@Override
	public void prepareViews() {
		final SeekBar seekBar = IjoomerUtilities.getLoadingDialog(getString(R.string.dialog_loading_please_wait));
		webExternalLinks.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				seekBar.setProgress(progress);
			}
		});

		webExternalLinks.setWebViewClient(new WebViewClient());
		webExternalLinks.getSettings().setJavaScriptEnabled(true);
		webExternalLinks.getSettings().setPluginsEnabled(true);
		link = getIntent().getStringExtra("url");
		if (!link.startsWith("http://") && !link.startsWith("https://")) {
			link = "http://" + link;
		}
		System.out.println(link);
		webExternalLinks.loadUrl(link);
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void setActionListeners() {
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
}
