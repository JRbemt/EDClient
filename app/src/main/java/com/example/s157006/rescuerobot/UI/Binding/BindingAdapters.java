package com.example.s157006.rescuerobot.UI.Binding;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.view.View;

/**
 * Created by joaquin on 1-2-2017.
 *
 */

public final class BindingAdapters {

    private BindingAdapters(){
        throw new AssertionError();
    }

    @BindingAdapter("android:background")
    public static void setColor(View v, @ColorRes int color){
        v.setBackgroundColor(v.getResources().getColor(color));
    }
}
