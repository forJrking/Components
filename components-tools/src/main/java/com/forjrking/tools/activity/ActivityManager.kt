package com.forjrking.tools.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @description: activity 管理类需要初始化
 * @author: 岛主
 * @date: 2020/7/6 10:24
 * @version: 1.0.0
 */
class ActivityManager() {

    private val CHECK_DELAY = 600L

    private val lifecycleCallbacks: IActivityLifecycleCallbacks = IActivityLifecycleCallbacks()

    private fun activityStack() = lifecycleCallbacks.activityStack

    private val listeners = CopyOnWriteArrayList<ForegroundCallbacks>()

    companion object {
        @JvmStatic
        val instances by lazy { ActivityManager() }
    }

    fun initialize(_application: Application) {
        _application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    /** DES: 查看最上面可见的一个 */
    open fun topActivity(): Activity? =
        activityStack().let { if (it.isNotEmpty()) it.peek() else null }

    /** DES: 查看最后一个 */
    open fun bottomActivity(): Activity? =
        activityStack().let { if (it.isNotEmpty()) it.firstElement() else null }

    /**彻底退出*/
    fun finishAllActivity() {
        var activity: Activity
        while (!activityStack().empty()) {
            activity = activityStack().pop()
            activity?.finish()
        }
    }

    /**杀死当前进程*/
    fun appExit() {
        finishAllActivity()
        //杀死进程
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls clazz
     */
    fun finishActivity(vararg cls: Class<Activity>, isOther: Boolean = false) {
        val iterator = activityStack().iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (isOther) {
                if (!cls.contains(next.javaClass)) {
                    next.finish()
                    iterator.remove()
                }
            } else {
                if (cls.contains(next.javaClass)) {
                    next.finish()
                    iterator.remove()
                }
            }
        }
    }

    /**
     * 结束指定的Activity
     * @param activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack().remove(activity)
            activity.finish()
        }
    }

    /** DES: 查找指定的 activity 第一个*/
    fun findActivity(cls: Class<Activity>): Activity? {
        activityStack().forEach { activity ->
            if (activity.javaClass == cls) {
                return@findActivity activity
            }
        }
        return null
    }

    /** DES: 查找指定的 activity 多个*/
    fun findActivitys(cls: Class<Activity>): List<Activity> =
        activityStack().filter {
            it.javaClass == cls
        }


    /**
     * 查找栈中是否存在指定的activity
     * @param cls
     * @return >=0 成功
     */
    fun hasActivity(cls: Class<Activity>): Int {
        activityStack().forEachIndexed { index, activity ->
            if (activity.javaClass == cls) {
                return@hasActivity index
            }
        }
        return -1
    }

    /**
     * 跳转到管理栈堆的指定的activity 会杀死它之前的
     * @param cls
     */
    fun jump2Activity(cls: Class<Activity>): Boolean {
        val index = hasActivity(cls)
        if (index >= 0) {
            repeat(index) {
                val activity = activityStack().removeAt(it)
                activity.finish()
            }
            return true
        }
        return false
    }


    private inner class IActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

        open val activityStack: Stack<Activity> by lazy { Stack<Activity>() }

        /**前后台监控 延时*/
        private val handler by lazy { Handler(Looper.getMainLooper()) }

        /**前后台监控*/
        private var check: Runnable? = null

        // 监听切换到前台
        private var foreground = false
        private var paused = true

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityStack.push(activity)
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {
            paused = false
            val wasBackground: Boolean = !foreground
            foreground = true
            if (check != null) {
                handler.removeCallbacks(check)
            }
            if (wasBackground) {
                listeners.forEach {
                    it.onBecameForeground(activity)
                }
            }
        }

        override fun onActivityPaused(activity: Activity) {
            paused = true
            if (check != null) {
                handler.removeCallbacks(check)
            }
            handler.postDelayed(Runnable {
                if (foreground && paused) {
                    foreground = false
                    listeners.forEach {
                        it.onBecameBackground()
                    }
                }
            }.also { check = it }, CHECK_DELAY)
        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            activityStack.remove(activity)
        }

    }

    /** DES: 添加前后台监听 */
    fun addListener(listener: ForegroundCallbacks?) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    /** DES: 移除前后台监听 */
    fun removeListener(listener: ForegroundCallbacks?) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
        }
    }

}