package com.components.tools

import android.app.Application
import xcrash.XCrash
import java.io.File

/**
 * @description:
 * @author: forjrking
 * @date: 2021/5/10 10:39 上午
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val parameters: XCrash.InitParameters = XCrash.InitParameters()
            .setLogDir(
                File(filesDir, "tombstones").absolutePath
            )
        XCrash.init(this, parameters)
    }
}