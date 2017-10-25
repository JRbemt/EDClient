package com.example.s157006.rescuerobot.Widgets.DataBound;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.s157006.rescuerobot.Networking.IConnectionHandler;
import com.example.s157006.rescuerobot.R;

/**
 * Created by joaquin on 7-10-2017.
 * Elke included view heeft zn eigen ViewDataBinding. We gebruiken dit om GUI logix op te splitsen.
 * Deze basis class handelt het verkrijgen van de ViewDatabinding en een eventuele IConnectionHandler af.
 */

public class DataBoundChild<T extends ViewDataBinding> extends FrameLayout{

    private T binding;
    private IConnectionHandler connectionHandlerHandler;

    public DataBoundChild(Context context) {
        super(context);
        init();
    }

    public DataBoundChild(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DataBoundChild(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DataBoundChild(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        Context ctx = getContext();
        if (ctx instanceof IConnectionHandler){
            connectionHandlerHandler = (IConnectionHandler) ctx;
            onConnection(connectionHandlerHandler);
        }
    }


    @Override
    public final void setTag(int key, Object tag) {
        super.setTag(key, tag);

        // After an included (databinding) layout is found (DataBindingUtil#mapBindings())
        // the first thing after binding will be to set the tag
        if (key == R.id.dataBinding) {
            //binding = DataBindingUtil.getBinding(this);
            binding = (T) tag;
            onBind(binding);
        }
    }

    @Override
    public final void setTag(Object tag) {
        super.setTag(tag);

        // The tag is being set with or without key depending on (ViewDataBinding.USE_TAG_ID
        if (tag instanceof ViewDataBinding) {
            binding = (T) tag;
            onBind(binding);
        }
    }

    protected void onBind(T binding){

    }

    protected void onConnection(IConnectionHandler handler){

    }

    public final T getBinding() {
        return binding;
    }

    public final IConnectionHandler getConnectionHandlerHandler() {
        return connectionHandlerHandler;
    }

    public final boolean isConnected(){
        return connectionHandlerHandler != null;
    }

}
