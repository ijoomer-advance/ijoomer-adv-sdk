package com.ijoomer.customviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.common.classes.IjoomerWebviewClient;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.custom.interfaces.URLSpanClickListener;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This Class Contains All Method Related To IjoomerTextView.
 * 
 * @author tasol
 * 
 */
public class IjoomerTextView extends TextView {

    private boolean isDecodeEmojis() {
        return isDecodeEmojis;
    }

    public void setDecodeEmojis(boolean decodeEmojis) {
        isDecodeEmojis = decodeEmojis;
    }

    private boolean isDecodeEmojis = true;
    private Context context;

    public URLSpanClickListener getUrlSpanClickListener() {
        return urlSpanClickListener;
    }

    public void setUrlSpanClickListener(URLSpanClickListener urlSpanClickListener) {
        this.urlSpanClickListener = urlSpanClickListener;
    }

    private URLSpanClickListener urlSpanClickListener;

    public IjoomerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public IjoomerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IjoomerTextView(Context context) {
        super(context);
        this.context=context;
        init(context);
    }

    private void init(Context mContext) {
        setLineSpacing(2, 1);
        try {
            if (IjoomerApplicationConfiguration.getFontFace() != null) {
                setTypeface(IjoomerApplicationConfiguration.getFontFace());
            } else {
                Typeface tf = Typeface.createFromAsset(mContext.getAssets(), IjoomerApplicationConfiguration.getFontNameWithPath());
                setTypeface(tf);
                IjoomerApplicationConfiguration.setFontFace(tf);
            }
        } catch (Throwable e) {
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isDecodeEmojis) {
            try {
                super.setText(getSmiledText(setLinks(text)), type);
            } catch (Exception e) {
            }
        } else {
            super.setText(setLinks(text), type);
        }
        ;
    }

    public Spannable setLinks(CharSequence text) {
        SpannableStringBuilder builder;
        boolean isContainUrl=false;

        try {
            builder = (SpannableStringBuilder) text;
        }catch (Exception e){
            builder = new SpannableStringBuilder(text);
        }
        if(builder.toString().trim().length()>0){
            String[] linkHttpPatterns = {"([Hh][tT][tT][pP][sS]?:\\/\\/[^ ,'\">\\]\\)]*[^\\. ,'\">\\]\\)])"};
            String[] linkWwwPatterns = {"([wW][wW][wW]?.[^ ,'\">\\]\\)]*[^\\. ,'\">\\]\\)])"};

            Pattern patternHttp = Pattern.compile(linkHttpPatterns[0]);
            Matcher matcherHttp = patternHttp.matcher(builder);
            while (matcherHttp.find()) {
                isContainUrl=true;
                int x = matcherHttp.start();
                int y = matcherHttp.end();
                InternalURLSpan span = new InternalURLSpan();
                span.text = text.toString().substring(x, y);
                builder.setSpan(span, x, y, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            Pattern patternWww = Pattern.compile(linkWwwPatterns[0]);
            Matcher matcherWww = patternWww.matcher(builder);
            while (matcherWww.find()) {
                isContainUrl=true;
                int x = matcherWww.start();
                int y = matcherWww.end();
                InternalURLSpan span = new InternalURLSpan();
                span.text = text.toString().substring(x, y);
                builder.setSpan(span, x, y, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if(isContainUrl){
                setLinkTextColor(Color.BLUE);
                setLinksClickable(true);
                setMovementMethod(LinkMovementMethod.getInstance());
                setFocusable(false);
            }
        }
        return builder;
    }

    class InternalURLSpan extends ClickableSpan {
        public String text;
        @Override
        public void onClick(View widget) {
            handleLinkClicked(text);
        }

    }

    public void handleLinkClicked(String value) {
        if(getUrlSpanClickListener()!=null){
            getUrlSpanClickListener().onClick(value.trim());
        }else{
            try {
                Intent intent = new Intent(getContext(),IjoomerWebviewClient.class);
                intent.putExtra("url",value.trim());
                getContext().startActivity(intent);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public Spannable getSmiledText(CharSequence text) {
        SpannableStringBuilder builder;

        try {
            builder = (SpannableStringBuilder) text;
        }catch (Exception e){
            builder = new SpannableStringBuilder(text);
        }
        if (IjoomerUtilities.getEmojisHashMap().size() > 0) {
            int index;
            for (index = 0; index < builder.length(); index++) {
                if (Character.toString(builder.charAt(index)).equals(":")) {
                    for (Map.Entry<String, Integer> entry : IjoomerUtilities.getEmojisHashMap().entrySet()) {
                        int length = entry.getKey().length();
                        if (index + length > builder.length())
                            continue;
                        if (builder.subSequence(index, index + length).toString().equals(entry.getKey())) {
                            builder.setSpan(new ImageSpan(getContext(), entry.getValue()), index, index + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            index += length - 1;
                            break;
                        }
                    }
                }
            }
        }
        return builder;
    }

}
