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
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.src.R;
import com.ijoomer.theme.ThemeManager;

/**
 * This Class Contains All Method Related To IjoomerWebviewClient.
 * 
 * @author tasol
 * 
 */
public class IjoomerWebviewClient extends IjoomerSuperMaster {

    private WebView webExternalLinks;
    private IjoomerButton btnClose;
    private String link = "";
    private String IN_CONTENT = "";
    SeekBar seekBar;

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

    }

    @SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
    public void prepareViews() {
        getIntentData();
        seekBar = IjoomerUtilities
                .getLoadingDialog(getString(R.string.dialog_loading_please_wait));
        webExternalLinks.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if(seekBar==null){
                    seekBar = IjoomerUtilities
                            .getLoadingDialog(getString(R.string.dialog_loading_please_wait));
                }
                seekBar.setProgress(progress);
                if(progress==100){
                    seekBar=null;
                }
            }
        });

        webExternalLinks.setWebViewClient(new WebViewClient());
        webExternalLinks.getSettings().setJavaScriptEnabled(true);
        webExternalLinks.getSettings().setPluginState(PluginState.ON);
        webExternalLinks.getSettings().setSupportZoom(true);
        webExternalLinks.getSettings().setBuiltInZoomControls(true);

        if (link != null && link.length() > 0) {
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }
            webExternalLinks.loadUrl(link);
        } else if (IN_CONTENT != null && IN_CONTENT.trim().length() > 0) {
            StringBuilder sb = new StringBuilder(); // StringBuilder();
            sb.append("<HTML><HEAD><link rel=\"stylesheet\" type=\"text/css\" href=\"weblayout.css\" /></HEAD><body>");
            String str = IN_CONTENT.trim();
            str = str.replaceAll("<iframe width=\"[0-9]*",
                    "<iframe width=\"100\\%");
            str = str.replaceAll("<img[\\w]*",
                    "<img height=\"auto\" style=\"max-width:100\\%\";");
            sb.append(str);
            sb.append("</body></HTML>");
            webExternalLinks.loadDataWithBaseURL("file:///android_asset/css/",
                    sb.toString(), "text/html", "utf-8", null);
        }

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
        return 0;
    }

    @Override
    public int setFooterLayoutId() {
        return 0;
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
            link = this.getIntent().getStringExtra("url");
            if (link == null) {
                IN_CONTENT = this.getIntent().getStringExtra("IN_CONTENT");
            }

        } catch (Exception e) {
            IN_CONTENT = this.getIntent().getStringExtra("IN_CONTENT");
        }
    }

    @Override
    public void onBackPressed() {
        if(webExternalLinks.canGoBack()){
            webExternalLinks.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
