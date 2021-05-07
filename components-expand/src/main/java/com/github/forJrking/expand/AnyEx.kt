package com.github.forJrking.expand

import android.os.Looper

fun <T : Any> T.TAG() = this::class.simpleName

fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()
