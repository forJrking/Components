package com.forjrking.tools

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

/**
 * DES: CXT 全局Context 控制存取类
 * CHANGED: 岛主
 * TIME: 2019/3/8 0008 下午 3:23
 */
object Cxt {

    private var _cxt: Context? = null
    private var _res: Resources? = null

    /**全局实例*/
    lateinit var application: Application
        private set

    /**调试开关*/
    var debugEnable = false

    fun initialize(_application: Application) {
        application = _application
        _cxt = _application.applicationContext
    }

    /**
     * @param reflect 通过反射获取
     * */
    @JvmStatic
    @JvmOverloads
    fun get(reflect: Boolean = false): Context {
        return if (reflect) {
            reflectContext()!!
        } else {
            if (_cxt == null) throw NullPointerException("must set() Cxt")
            _cxt!!
        }
    }

    @JvmStatic
    val res: Resources
        get() {
            if (_res == null) {
                _res = _cxt!!.resources
            }
            return _res!!
        }

    @JvmStatic
    fun <T> getSystemService(name: String): T {
        return _cxt!!.getSystemService(name) as T
    }

    @JvmStatic
    fun getStr(@StringRes resId: Int): String {
        return _cxt!!.getString(resId)
    }

    @JvmStatic
    fun getColor(@ColorRes resId: Int): Int {
        return if (Build.VERSION.SDK_INT >= 23) {
            _cxt!!.getColor(resId)
        } else {
            _cxt!!.resources.getColor(resId)
        }
    }

    @JvmStatic
    fun getStr(resId: Int, vararg fmtArgs: Any?): String {
        return _cxt!!.getString(resId, *fmtArgs)
    }

    @JvmStatic
    fun getStrArray(@ArrayRes resId: Int): Array<String> {
        return res!!.getStringArray(resId)
    }

    /**
     * DES: 反射获取全局Context
     */
    @JvmStatic
    fun reflectContext(): Context? {
        if (_cxt == null) {
            try {
                return Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                return Class.forName("android.app.AppGlobals")
                    .getMethod("getInitialApplication")
                    .invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }
            throw IllegalStateException("Context is not initialed")
        }
        return _cxt
    }
}