package com.example.s157006.rescuerobot

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

/**
 * Created by s157006 on 18-10-2017.
 *
 */
fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE){ findViewById(res) as T }
}