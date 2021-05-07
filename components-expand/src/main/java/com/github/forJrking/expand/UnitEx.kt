package com.github.forJrking.expand

import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

/** dp转px*/
inline val Float.dp
    get() = dp2px

/** sp转px*/
inline val Float.sp
    get() = sp2px

/** px转dp*/
inline val Float.px2dp
    get() = run {
        val scale = metrics().density
        this / scale
    }

/** dp转px*/
inline val Float.dp2px
    get() = run {
        val scale = metrics().density
        this * scale
    }

/** px转sp*/
inline val Float.px2sp
    get() = run {
        val fontScale = metrics().scaledDensity
        this / fontScale
    }

/** sp转px*/
inline val Float.sp2px
    get() = run {
        val fontScale = metrics().scaledDensity
        this * fontScale
    }

/** dp转px*/
inline val Int.dp
    get() = toFloat().dp.roundToInt()

/** sp转px*/
inline val Int.sp
    get() = toFloat().sp.roundToInt()

/** px转dp*/
inline val Int.px2dp
    get() = toFloat().px2dp.roundToInt()

/** dp转px*/
inline val Int.dp2px
    get() = toFloat().dp2px.roundToInt()

/** px转sp*/
inline val Int.px2sp
    get() = toFloat().px2sp.roundToInt()

/** sp转px*/
inline val Int.sp2px
    get() = toFloat().sp2px.roundToInt()

inline fun metrics(): DisplayMetrics = Resources.getSystem().displayMetrics