package com.example.s157006.rescuerobot.Networking;

/**
 * Created by joaquin on 7-10-2017.
 *
 */

public enum ConnectionStatus{
    OFFLINE       (android.R.color.holo_red_light),
    CONNECTING    (android.R.color.holo_orange_light),
    ONLINE        (android.R.color.holo_green_light);

    private final int colorResId;

    ConnectionStatus(int resId){
        this.colorResId = resId;
    }

    public int getColorResId() {
        return colorResId;
    }
}
