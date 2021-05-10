package com.components.tools

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.forJrking.expand.inflate
import com.github.forJrking.expand.onFilterClick
import com.github.forjrking.sharedpreferenceimpl.SharedPreferencesHelper
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = this.inflate(R.layout.activity_main)
        setContentView(root)

//        ActivityThreadHooker.hook(object : ActivityThreadCallback() {
//            override fun handleMessageAfter(msg: Message) {
//            }
//
//            override fun handleMessageBefore(msg: Message) {
//                SpBlockHook.handlerMessage(msg)
//            }
//
//            override fun handleMessageException(msg: Message, e: Throwable?): Boolean {
//                return super.handleMessageException(msg, e)
//            }
//
//        })
        val editBtn: Button = findViewById(R.id.btn1)
        val sharedPreferences =
            SharedPreferencesHelper.getSharedPreferences(this, "test_sp", Context.MODE_PRIVATE)
//        val sharedPreferences = this.getSharedPreferences("test_sp", Context.MODE_PRIVATE)

        editBtn.onFilterClick {
            val edit1 = sharedPreferences.edit()
            val nextInt = Random.nextInt()
            for (i in 0..600) {
                edit1.apply {
                    putString("$nextInt-user_key_$i}", "some user id $i")
                    apply()
                }
            }
            startActivity(Intent(MainActivity@ this, MainActivity2::class.java))
        }

        val btn: Button = findViewById(R.id.btn)

        btn.onFilterClick {
            val edit = sharedPreferences.edit()
            Log.d("Main", "apply: 开始")
            for (i in 0..2000) {
                edit.apply {
                    putInt("key_$i", i)
//                    putString("user_key_$i", "some user id $i")
                    apply()
                }
            }
            Log.d("Main", "apply: 完成")
        }
//
//        val bundle = Bundle().put(
//            arrayOf(
//                Pair("keyInt", 1),
//                Pair("keyString", "abc"),
//                Pair("keyBool", false)
//            )
//        )
//
//        val navigationBarHeight: Int = this.navigationBarHeight
//        val statusBarHeight: Int = this.statusBarHeight
//        val screenHeight: Int = this.screenHeight
//        val screenWidth: Int = this.screenWidth
//        val activityManager: ActivityManager = this.activityManager
//
//
//        /*** 使用Context初始化布局，专为RecyclerView提供*/
////        val view = this.inflateRv(R.layout.activity_main, parent)
//        val file = File(this.cacheDir, "abc.txt")
//        file.createNewFile()
//        val fileInputStream = FileInputStream(file)
//        fileInputStream.closeQuietly()
//
//        val in1 = FileInputStream(file)
//        val in2 = FileInputStream(file)
//        val in3 = FileInputStream(file)
//
//        arrayOf(in1, in2, in3).closeQuietly()
//        //精确到小数点后几位
//        val doExact = 21.6.doExact()
//        Log.d(TAG(), "doExact: $doExact")
//        val noZero = 22.0.noZero()
//        Log.d(TAG(), "noZero: $noZero")
//        val formatNumber = 21000001876.528.formatNumber(addComma = true, modeFloor = false)
//        Log.d(TAG(), "formatNumber: $formatNumber")
//
//        //转换int
//        "456.8".toIntWithNull(default = 0)
//        "456.8".toFloatWithNull()
//        "456.8".toDoubleWithNull()
//        "456.8".toBigDecimalWithNull()
//
//        20.dp
//        20.dp2px
//        val px2dp: Int = 20.px2dp
//        20f.sp
//        val sp2px: Float = 20f.sp2px
//        val px2sp = 20f.px2sp
//
//        // 设置显示状态
////        root.show()
////        root.hide()
////        root.invisible()
//        // 获取显示状态
//        val visible = root.isVisible
//        val gone = root.isGone
//        val invisible = root.isInvisible
//
//        // 扩大点击范围
//        btn.expandTouchArea(size = 40)
//        // 防止重复点击
//        btn.onFilterClick {
//            // 快照
//            val toBitmap = root.toBitmap()
//        }
//        //获取内部activity
//        val activity = btn.getActivity()
//        // 输入框输入监控  可以用于实时搜索功能
//        editView.textChange {
//
//        }
//        editView.hideKeyboard()
//
//        val dm1 = Resources.getSystem().displayMetrics
//
//        Log.d(TAG(), "dm1: $dm1")
//
//        val dm2 = this.resources.displayMetrics
//
//        Log.d(TAG(), "dm2: $dm2")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Main", "onStop: 完成")
    }

}