package com.components.tools

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.github.forJrking.expand.*
import com.github.forjrking.athook.ActivityThreadCallback
import com.github.forjrking.athook.ActivityThreadHooker
import com.github.forjrking.athook.SpBlockHook
import java.io.File
import java.io.FileInputStream

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
        val editView: EditText = findViewById(R.id.edit)
//        btn.setOnClickListener {
//            for (i in 0..2000) {
//                edit.apply {
//                    putInt("key_$i", i)
////                    putString("user_key_$i", "some user id $i")
//                    apply()
//                }
//            }
//            Log.d("Main", "apply: 完成")
//        }

        val bundle = Bundle().put(
            arrayOf(
                Pair("keyInt", 1),
                Pair("keyString", "abc"),
                Pair("keyBool", false)
            )
        )

        val navigationBarHeight: Int = this.navigationBarHeight
        val statusBarHeight: Int = this.statusBarHeight
        val screenHeight: Int = this.screenHeight
        val screenWidth: Int = this.screenWidth
        val activityManager: ActivityManager = this.activityManager
        val root = this.inflate(R.layout.activity_main)

        /*** 使用Context初始化布局，专为RecyclerView提供*/
//        val view = this.inflateRv(R.layout.activity_main, parent)
        val file = File(this.cacheDir, "abc.txt")
        file.createNewFile()
        val fileInputStream = FileInputStream(file)
        fileInputStream.closeQuietly()
        //精确到小数点后几位
        val doExact = 21.6.doExact()
        Log.d(TAG(), "doExact: $doExact")
        val noZero = 22.0.noZero()
        Log.d(TAG(), "noZero: $noZero")
        val formatNumber = 21000001876.528.formatNumber(addComma = true, modeFloor = false)
        Log.d(TAG(), "formatNumber: $formatNumber")

        //转换int
        "456.8".toIntWithNull(default = 0)
        "456.8".toFloatWithNull()
        "456.8".toDoubleWithNull()
        "456.8".toBigDecimalWithNull()

        20.dp
        20.dp2px
        20.px2dp
        20f.sp
        val sp2px: Int = 20f.sp2px
        val px2sp = 20f.px2sp

        // 设置显示状态
        root.show()
        root.hide()
        root.invisible()
        // 获取显示状态
        val visible = root.isVisible
        val gone = root.isGone
        val invisible = root.isInvisible
        // 快照
        val toBitmap = root.toBitmap()
        // 扩大点击范围
        btn.expandTouchArea(size = 40)
        // 防止重复点击
        btn.onFilterClick {

        }
        //获取内部activity
        val activity = btn.getActivity()
        // 输入框输入监控  可以用于实时搜索功能
        editView.textChange {

        }
        editView.hideKeyboard()
    }

    override fun onStop() {
        super.onStop()
        Log.d("Main", "onStop: 完成")
    }

}