package com.example.s157006.rescuerobot.UI.Binding;

import android.databinding.BindingConversion;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntegerRes;

import com.example.s157006.rescuerobot.Networking.ConnectionStatus;

/**
 * Created by joaquin on 2-2-2017.
 *
 */
public final class BindingConversions {

    private BindingConversions(){
        throw new AssertionError();
    }

    @BindingConversion
    public static int statusToColorResId(ConnectionStatus status){
        return status.getColorResId();
    }

}
