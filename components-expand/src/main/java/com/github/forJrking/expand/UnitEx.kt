package com.github.forJrking.expand

import android.content.res.Resources

/** dp转px*/
val Float.dp
    get() = dp2px

/** sp转px*/
val Float.sp
    get() = sp2px

/** dp转px*/
val Int.dp
    get() = toFloat().dp

/** sp转px*/
val Int.sp
    get() = toFloat().sp


/** px转dp*/
val Float.px2dp
    get() = run {
        val scale = metrics().density
        this / scale + .5F
    }.toInt()

/** dp转px*/
val Float.dp2px
    get() = run {
        val scale = metrics().density
        this * scale + .5F
    }.toInt()

/** px转dp*/
val Int.px2dp
    get() = toFloat().px2dp

/** dp转px*/
val Int.dp2px
    get() = toFloat().dp2px

/** sp转px*/
val Float.sp2px
    get() = run {
        val fontScale = metrics().scaledDensity
        this * fontScale + .5F
    }.toInt()

/** px转sp*/
val Float.px2sp
    get() = run {
        val fontScale = metrics().scaledDensity
        this / fontScale + .5f
    }.toInt()

/** px转sp*/
val Int.px2sp
    get() = toFloat().px2sp

/** sp转px*/
val Int.sp2px
    get() = toFloat().sp2px

private inline fun metrics() = Resources.getSystem().displayMetrics