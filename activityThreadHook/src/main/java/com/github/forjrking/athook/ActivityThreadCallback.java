package com.github.forjrking.athook;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Des: 用于捕获由系统引起的ActivityThread异常
 * @Author: forjrking
 * @Time: 2021/05/06 15:00
 * @Version: 1.0.0
 **/
public abstract class ActivityThreadCallback implements Handler.Callback {

    private static final String TAG = "ActivityThreadHooker";

    /**
     * ActivityThread$H
     */
    public Handler mHandler;

    /**
     * 其他框架注入的 ActivityThread.mH.mCallback
     */
    private Handler.Callback mOtherCallback;


    /**
     * 给ActivityThread中的mH设置Callback
     */
    boolean hook() {
        try {
            //反射得到ActivityThread.mH
            Object activityThread = getActivityThread();
            //获取handler
            this.mHandler = getHandler(activityThread);
            //反射得到Handler.mCallback
            Class<?> aClass = this.mHandler.getClass().getSuperclass();
            Field field = aClass.getDeclaredField("mCallback");
            field.setAccessible(true);
            this.mOtherCallback = (Handler.Callback) field.get(this.mHandler);
            if (this.mOtherCallback != null) {
                Log.w(TAG, "ActivityThread.mH.mCallback has already been hooked by " + this.mOtherCallback);
            }
            field.set(this.mHandler, this);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回值决定是否让handler#handleMessage()方法执行,true不执行
     */
    @Override
    public final boolean handleMessage(Message msg) {
        try {
            handleMessageBefore(msg);
            //让第三方注入的callback执行
            if (this.mOtherCallback != null) {
                return this.mOtherCallback.handleMessage(msg);
            }
            //让系统原有逻辑继续执行
            if (this.mHandler != null) {
                this.mHandler.handleMessage(msg);
            }
        } catch (Throwable e) {
            //自己优先处理异常
            if (!handleMessageException(msg, e)) {
                throw e;
            }
        } finally {
            handleMessageAfter(msg);
        }
        return true;
    }

    /**
     * 获取ActivityThread中msg系统处理之前
     */
    public abstract void handleMessageBefore(Message msg);

    /**
     * ActivityThread中msg发生异常时候处理,默认不处理直接抛出
     */
    public boolean handleMessageException(Message msg, Throwable e) {
        return false;
    }

    /**
     * 获取ActivityThread中msg在系统处理之后
     */
    public abstract void handleMessageAfter(Message msg);

    /**
     * 获取ActivityThread中的Handler对象
     */
    private Handler getHandler(Object thread) {
        if (thread == null) {
            return null;
        }
        Handler handler = null;
        try {
            Class<?> kClass = thread.getClass();

            Field field = kClass.getDeclaredField("mH");
            //通过属性变量拿
            field.setAccessible(true);
            handler = (Handler) field.get(thread);
            if (handler != null) {
                Log.d(TAG, "getHandler => mH");
                return handler;
            }

            //通过getHandler()方法返回值拿
            Method method = kClass.getDeclaredMethod("getHandler", new Class[0]);
            method.setAccessible(true);
            handler = (Handler) method.invoke(thread, new Object[0]);
            if (handler != null) {
                Log.d(TAG, "getHandler => getHandler()");
                return handler;
            }

            //通过内部类匹配ActivityThread中的属性拿
            Field handlerF = getFieldByType(kClass, Class.forName("android.app.ActivityThread$H"));
            handlerF.setAccessible(true);
            handler = (Handler) handlerF.get(thread);
            if (handler != null) {
                Log.d(TAG, "getHandler => ActivityThread$H");
            }
        } catch (Throwable e) {
            Log.w(TAG, "Main thread handler is inaccessible", e);
        }

        return handler;
    }

    /**
     * 获得android.app.ActivityThread对象
     */
    private static Object getActivityThread() {
        Object thread = null;
        final String ACTIVITY_THREAD = "android.app.ActivityThread";
        try {
            //通过sCurrentActivityThread属性变量拿
            Class<?> atClass = Class.forName(ACTIVITY_THREAD);
            Field field = atClass.getDeclaredField("sCurrentActivityThread");
            field.setAccessible(true);
            thread = field.get(atClass);
            if (thread != null) {
                return thread;
            }
            Method method = atClass.getDeclaredMethod("currentActivityThread", new Class[0]);
            //通过currentActivityThread()静态方法拿：兼容<api 18
            method.setAccessible(true);
            thread = method.invoke(atClass, new Object[0]);
        } catch (Throwable e) {
            Log.w(TAG, e);
        }
        Log.w(TAG, "ActivityThread instance is inaccessible");
        return thread;
    }

    private Field getFieldByType(final Class<?> klass, final Class<?> type) {
        final Field[] fields = klass.getDeclaredFields();
        if (fields.length <= 0) {
            final Class<?> parent = klass.getSuperclass();
            if (null == parent) {
                return null;
            }
            return getFieldByType(parent, type);
        }

        for (final Field field : fields) {
            if (field.getType() == type) {
                return field;
            }
        }

        return null;
    }

}