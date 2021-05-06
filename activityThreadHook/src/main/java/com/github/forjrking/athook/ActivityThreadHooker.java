package com.github.forjrking.athook;

import android.util.Log;

/**
 * ActivityThread$H是继承自Handler.而Handler支持通过Handler.Callback来改变其自行的行为，
 * 所以，只要通过反射为ActivityThread.mH.mCallback设置一个新的Handler.Callback
 * 然后在这个Handler.Callback中将系统异常catch住就行了。
 */
public class ActivityThreadHooker {

    private static final String TAG = "ActivityThreadHooker";
    private static volatile boolean hooked;


    public static void hook(ActivityThreadCallback callback) {
        // DES：防止重复hook
        if (hooked) {
            return;
        }
        if ((hooked = callback.hook())) {
            Log.d(TAG, "Hook ActivityThread.mH.mCallback success!");
        } else {
            Log.d(TAG, "Hook ActivityThread.mH.mCallback failed");
        }
    }

}