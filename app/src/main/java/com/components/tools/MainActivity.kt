package com.components.tools

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.forjrking.athook.ActivityThreadCallback
import com.github.forjrking.athook.ActivityThreadHooker
import com.github.forjrking.athook.SpBlockHook

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityThreadHooker.hook(object : ActivityThreadCallback() {
            override fun handleMessageAfter(msg: Message) {
            }

            override fun handleMessageBefore(msg: Message) {
                SpBlockHook.handlerMessage(msg)
            }

            override fun handleMessageException(msg: Message, e: Throwable?): Boolean {
                return super.handleMessageException(msg, e)
            }

        })
        val sharedPreferences = this.getSharedPreferences("test_sp", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        val btn: Button = findViewById(R.id.btn)
        btn.setOnClickListener {
            for (i in 0..2000) {
                edit.apply {
                    putInt("key_$i", i)
//                    putString("user_key_$i", "some user id $i")
                    apply()
                }
            }
            Log.d("Main", "apply: 完成")
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("Main", "onStop: 完成")
    }

}