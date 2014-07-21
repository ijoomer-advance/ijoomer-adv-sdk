package com.ijoomer.common.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ijoomer.customviews.IjoomerTextView;

/**
 * Created by tasol on 1/11/13.
 */
public class URLImageParser implements Html.ImageGetter {
    Context c;
    IjoomerTextView container;
    AQuery aQuery;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the
     * container
     *
     * @param t
     * @param c
     */
    public URLImageParser(View t, Context c) {
        this.c = c;
        this.container = (IjoomerTextView) t;
    }

    public Drawable getDrawable(String source) {
        aQuery = new AQuery(c);
        final URLDrawable urlDrawable = new URLDrawable();

        if(aQuery.getCachedImage(source)!=null){
            Drawable result = new BitmapDrawable(c.getResources(),aQuery.getCachedImage(source));
            result.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
            urlDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
            urlDrawable.drawable = result;
            URLImageParser.this.container.setMinimumHeight(result.getIntrinsicHeight());
            URLImageParser.this.container.requestLayout();
            URLImageParser.this.container.invalidate();
        }else{
            aQuery.ajax(source,Bitmap.class,0,new AjaxCallback<Bitmap>(){
                @Override
                public void callback(String url, Bitmap object, AjaxStatus status) {
                    super.callback(url, object, status);
                    Drawable result = new BitmapDrawable(c.getResources(),object);
                    result.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
                    urlDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
                    urlDrawable.drawable = result;
                    URLImageParser.this.container.setMinimumHeight(result.getIntrinsicHeight());
                    URLImageParser.this.container.requestLayout();
                    URLImageParser.this.container.invalidate();
                }
            });
        }

        return urlDrawable;
    }
}
