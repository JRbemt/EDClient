package com.example.s157006.rescuerobot.ViewModel;

import android.content.Context;
import android.databinding.ObservableField;

import com.example.s157006.rescuerobot.Networking.ConnectionStatus;

/**
 * Created by joaquin on 8-10-2017.
 *
 */

public class ArduinoViewModel {
    public final ObservableField<ConnectionStatus> status = new ObservableField<>(ConnectionStatus.OFFLINE);

    private final Context context;

    public ArduinoViewModel(Context context){
        this.context = context;
    }
}
