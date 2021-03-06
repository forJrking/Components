package com.github.forJrking.expand

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

/**
 * Context 加载布局方法，包括 [RecyclerView] item 的加载方法 [inflateRv(Int, ViewGroup)]
 * @since 1.0.0
 */

/*** 使用Context初始化布局*/
inline fun Context.inflate(
    @LayoutRes id: Int, parent: ViewGroup? = null,
    attachToParent: Boolean = parent != null
): View {
    return LayoutInflater.from(this).inflate(id, parent, attachToParent)
}

/*** 使用Context初始化布局，专为RecyclerView提供*/
inline fun Context.inflateRv(@LayoutRes id: Int, parent: ViewGroup?): View {
    return LayoutInflater.from(this).inflate(id, parent, false)
}

/*** 使用Fragment初始化布局*/
inline fun Fragment.inflate(
    @LayoutRes id: Int, parent: ViewGroup? = null,
    attachToParent: Boolean = parent != null
): View {
    return LayoutInflater.from(context).inflate(id, parent, attachToParent)
}

/*** 使用Fragment初始化布局，专为RecyclerView提供*/
inline fun Fragment.inflateRv(@LayoutRes id: Int, parent: ViewGroup?): View {
    return LayoutInflater.from(context).inflate(id, parent, false)
}

/*** 获取状态栏高度*/
val Context.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }

        return result
    }

/*** 获取底部栏高度*/
val Context.navigationBarHeight: Int
    get() {
        var result = 0
        val resourceId = this.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = this.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

/*** screen width in pixels*/
inline val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

/*** screen height in pixels*/
inline val Context.screenHeight
    get() = resources.displayMetrics.heightPixels

/**
 * 判断是不是黑夜模式
 * @return `true`: 黑夜模式
 */
fun Context.isNightMode(): Boolean {
    if (this is AppCompatActivity) {
        return delegate.localNightMode == AppCompatDelegate.MODE_NIGHT_YES
    }

    return resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
}