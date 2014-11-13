package com.ijoomer.common.classes;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.src.R;
import com.ijoomer.theme.ThemeManager;

import org.json.JSONObject;

/**
 * This Class Contains All Method Related To IjoomerWebviewClient.
 *
 * @author tasol
 *
 */
public class IjoomerCometChat extends IjoomerSuperMaster {

    private WebView webExternalLinks;
    private IjoomerButton btnClose;
    private String link = "";

    /**
     * Override methods
     */
    @Override
    public int setLayoutId() {
        return ThemeManager.getInstance().getWebview();
    }

    @Override
    public void initComponents() {
        webExternalLinks = (WebView) findViewById(R.id.webExternalLinks);
        btnClose = (IjoomerButton) findViewById(R.id.btnClose);
        btnClose.setVisibility(View.GONE);

    }

    @SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
    public void prepareViews() {
        ((TextView) getHeaderView().findViewById(R.id.txtHeader)).setText(getScreenCaption());
        getIntentData();
        final SeekBar seekBar = IjoomerUtilities
                .getLoadingDialog(getString(R.string.dialog_loading_please_wait));
        webExternalLinks.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                seekBar.setProgress(progress);
            }
        });

        webExternalLinks.setWebViewClient(new WebViewClient());
        webExternalLinks.getSettings().setJavaScriptEnabled(true);
        webExternalLinks.getSettings().setPluginState(PluginState.ON);

        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            link = "http://" + link;
        }
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    @Override
    public View setLayoutView() {
        return null;
    }

    @Override
    public int setHeaderLayoutId() {
        return R.layout.ijoomer_header;
    }

    @Override
    public int setFooterLayoutId() {
        return R.layout.ijoomer_footer;
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

    private void getIntentData() {
        try {
            link = ((JSONObject) new JSONObject(getIntent().getStringExtra("IN_OBJ")).getJSONObject("itemdata")).getString("url");
        } catch (Exception e) {
        }
    }
}
