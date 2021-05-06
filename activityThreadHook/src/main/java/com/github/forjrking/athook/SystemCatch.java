package com.github.forjrking.athook;

import android.content.res.Resources;
import android.os.Message;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author: forjrking
 * @date: 2021/5/6 11:13 AM
 */
public class SystemCatch {

    /**
     * 系统升级引发的一些异常，可以直接退出，重启会正常
     */
    private static final String LOADED_APK_GET_ASSETS = "android.app.LoadedApk.getAssets";
    private static final String ASSET_MANAGER_GET_RESOURCE_VALUE = "android.content.res.AssetManager.getResourceValue";

    /**
     * 系统类的包名
     */
    private static final String[] SYSTEM_PACKAGE_PREFIXES = {
            "java.",
            "android.",
            "androidx.",
            "dalvik.",
            "com.android.",
    };

    /**
     * 堆栈信息。通过堆栈中是否存在非系统的类，便可判断异常是否由APP自身业务导致的
     * <p>
     * * @param ignorePackages 白名单设置(全路径)，多个路径用逗号隔离
     */
    private final Set<String> mIgnorePackages;


    public SystemCatch(String ignorePackages) {
        final String pkgs = null == ignorePackages ? "" : ignorePackages.trim();
        String[] split = pkgs.split("\\s*,\\s*");
        Set<String> packages = new HashSet<>(Arrays.asList(SYSTEM_PACKAGE_PREFIXES));
        for (String pkg : split) {
            if (TextUtils.isEmpty(pkg)) {
                continue;
            }
            packages.add(pkg);
        }
        //返回不可修改的set
        this.mIgnorePackages = Collections.unmodifiableSet(packages);
    }

    /**
     * 返回值决定是否让
     */
    public boolean handleMessageException(Message msg, Throwable e) {
        if (e instanceof NullPointerException) {
            //通常发生在app升级安装之后，看起来像是系统bug
            if (hasStackTraceElement(e, ASSET_MANAGER_GET_RESOURCE_VALUE, LOADED_APK_GET_ASSETS)) {
                //终止应用
                return abort();
            }
            //重新处理异常
            return rethrowIfCausedByUser(e);
        } else if (e instanceof SecurityException
                || e instanceof IllegalArgumentException
                || e instanceof AndroidRuntimeException
                || e instanceof Resources.NotFoundException
                || e instanceof WindowManager.BadTokenException) {
            //完全交由自己处理，不影响后续操作
            return rethrowIfCausedByUser(e);
        } else if (e instanceof RuntimeException) {
            Throwable cause = e.getCause();
            // 通常发生在app升级安装之后，看起来像是系统bug
            if (isCausedBy(cause, NullPointerException.class) && hasStackTraceElement(e, LOADED_APK_GET_ASSETS)) {
                return abort();
            }
            //解决DeadSystemException、DeadObjectException
            return rethrowIfCausedByUser(e);
        } else if (e instanceof Error) {
            return rethrowIfCausedByUser(e);
        } else {
            return false;
        }
    }

    /**
     * 如果由用户引起，则重新抛出
     */
    private boolean rethrowIfCausedByUser(Throwable t) {
        if (t == null) return false;
        for (Throwable cause = t; cause != null; cause = cause.getCause()) {
            for (StackTraceElement element : cause.getStackTrace()) {
                //不在白名单里面，认为是用户app业务造成的异常
                if (!isStackTraceInIgnorePackages(element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前堆栈信息是否在白名单里面
     */
    private boolean isStackTraceInIgnorePackages(StackTraceElement element) {
        String name = element.getClassName();
        for (String prefix : mIgnorePackages) {
            //在mIgnorePackages中找，只要有一个符合便认为是系统异常
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasStackTraceElement(Throwable t, String... traces) {
        return hasStackTraceElement(t, new HashSet<>(Arrays.asList(traces)));
    }

    /**
     * Throwable是否存在该条堆栈
     */
    private static boolean hasStackTraceElement(Throwable t, Set<String> traces) {
        if (t == null || traces == null || traces.isEmpty()) {
            return false;
        }

        for (StackTraceElement element : t.getStackTrace()) {
            if (traces.contains(element.getClassName() + "." + element.getMethodName())) {
                return true;
            }
        }

        return hasStackTraceElement(t.getCause(), traces);
    }

    /**
     * Throwable是否由该种类型异常引发
     */
    @SafeVarargs
    private static boolean isCausedBy(Throwable t, Class<? extends Throwable>... causes) {
        return isCausedBy(t, new HashSet<>(Arrays.asList(causes)));
    }

    private static boolean isCausedBy(Throwable t, Set<Class<? extends Throwable>> causes) {
        if (t == null) {
            return false;
        }

        if (causes.contains(t.getClass())) {
            return true;
        }

        return isCausedBy(t.getCause(), causes);
    }

    /**
     * 出现一些系统异常情况，必须中止应用
     */
    private static boolean abort() {
        int pid = android.os.Process.myPid();
        //非正常退出
        android.os.Process.killProcess(pid);
        System.exit(1);
        return true;
    }

}
