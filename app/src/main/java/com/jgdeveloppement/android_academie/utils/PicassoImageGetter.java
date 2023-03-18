package com.jgdeveloppement.android_academie.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class PicassoImageGetter implements Html.ImageGetter {

    private TextView textView;
    private Boolean isLandscape;
    private View view;
    private Boolean haveDrawable;

    public PicassoImageGetter(TextView target, Boolean isLandscape, View view) {
        this.textView = target;
        this.isLandscape = isLandscape;
        this.view = view;
    }

    @Override
    public Drawable getDrawable(String source) {
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        Picasso.get().load(source).into(drawable);
        this.haveDrawable = drawable.haveImage();
        return drawable;
    }

    public Boolean haveImage(){
        return  this.haveDrawable;
    }


    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public Boolean haveImage(){
            return drawable != null;
        }


        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            int left = 0;
            if (isLandscape){
                width = width*2;
                height = height*2;
                left = 50;
            }
            drawable.setBounds(left, 0, width, height);
            setBounds(left, 0, width, height);
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(Utils.myApp.getResources(), bitmap));
            view.setVisibility(View.GONE);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            view.setVisibility(View.VISIBLE);
        }
    }


}
