package com.github.forjrking.athook;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 用于在apply导致的ANR
 * <p>
 * <p>
 * SharedPreferences.apply调用次数过多容易引起ANR。
 * 所有此类ANR都是经由 QueuedWork.waitToFinish()触发的
 * 解决方法:在调用此函数之前，将其中保存的队列手动清空
 * for:https://mp.weixin.qq.com/s/IFgXvPdiEYDs5cDriApkxQ
 */
public class SpBlockHook {

    private static final String TAG = "SpBlockHook";

    private static final String CLASS_QUEUED_WORK = "android.app.QueuedWork";
    /**
     * 防止重复反射
     */
    private static volatile boolean init = false;
    /**
     * 8.0 以后是这个
     */
    private static final String FIELD_PENDING_FINISHERS_VERSION8 = "sFinishers";
    private static LinkedList<Runnable> sPendingWorkFinishersV8 = null;

    /**
     * 8.0以前是这个
     */
    private static final String FIELD_PENDING_FINISHERS = "sPendingWorkFinishers";
    private static ConcurrentLinkedQueue<Runnable> sPendingWorkFinishers = null;


    private static final int SERVICE_ARGS = 115;
    private static final int STOP_SERVICE = 116;
    private static final int SLEEPING = 137;
    private static final int STOP_ACTIVITY_SHOW = 103;
    private static final int STOP_ACTIVITY_HIDE = 104;

    //根据消息类型清理队列
    public static void handlerMessage(Message msg) {
        int what = msg.what;
        switch (what) {
            case SERVICE_ARGS:
                clearQueuedWork("SERVICE_ARGS");
                break;
            case STOP_SERVICE:
                clearQueuedWork("STOP_SERVICE");
                break;
            case SLEEPING:
                clearQueuedWork("SLEEPING");
                break;
            case STOP_ACTIVITY_SHOW:
            case STOP_ACTIVITY_HIDE:
                clearQueuedWork("STOP_ACTIVITY");
                break;
            default:
        }
    }

    public static void clearQueuedWork(String what) {
        if (!init) {
            getPendingWorkFinishers();
            init = true;
        }
        if (sPendingWorkFinishers != null) {
            sPendingWorkFinishers.clear();
            Log.d(TAG, "sPendingWorkFinishers clear Success! " + what);
        }
        if (sPendingWorkFinishersV8 != null) {
            sPendingWorkFinishersV8.clear();
            Log.d(TAG, "sFinishers clear Success! " + what);
        }
    }

    @SuppressLint("PrivateApi")
    private static void getPendingWorkFinishers() {
        try {
            Class<?> clazz = Class.forName(CLASS_QUEUED_WORK);
            Field field = clazz.getDeclaredField(
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.O
                            ? FIELD_PENDING_FINISHERS
                            : FIELD_PENDING_FINISHERS_VERSION8
            );
            field.setAccessible(true);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                sPendingWorkFinishers = (ConcurrentLinkedQueue<Runnable>) field.get(null);
                Log.d(TAG, "sPendingWorkFinishers get Success!");
            } else {
                sPendingWorkFinishersV8 = (LinkedList<Runnable>) field.get(null);
                Log.d(TAG, "sFinishers get Success!");
            }
        } catch (Throwable e) {
            Log.w(TAG, e);
        }

    }
}