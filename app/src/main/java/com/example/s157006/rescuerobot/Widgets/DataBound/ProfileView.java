package com.example.s157006.rescuerobot.Widgets.DataBound;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.webkit.WebViewClient;

import com.example.s157006.rescuerobot.databinding.ProfileHeaderBinding;


/**
 * Created by s157006 on 15-10-2017.
 *
 */

public class ProfileView extends DataBoundChild<ProfileHeaderBinding> {

    public ProfileView(Context context) {
        super(context);
    }

    public ProfileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProfileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onBind(ProfileHeaderBinding binding) {
        binding.userPicture.setWebViewClient(new WebViewClient());
        binding.userPicture.loadUrl("file:///android_asset/stream.html");
    }
}
