package com.components.tools

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.forjrking.tools.Cxt
import com.forjrking.tools.EmptyUtils
import com.forjrking.tools.activity.ActivityManager
import com.forjrking.tools.log.KLog
import com.forjrking.tools.startup.DelayInitDispatcher
import com.forjrking.tools.thread.GoHandler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityManager.instances.initialize(this.application)

        GoHandler.getInstance().runOnMainThread {
            //do something
        }

        KLog.getConfig().setLogSwitch(Cxt.debugEnable)

    }
}