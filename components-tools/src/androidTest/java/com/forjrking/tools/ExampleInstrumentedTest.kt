package com.forjrking.tools

import android.content.res.Resources
import android.util.TypedValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.forJrking.expand.dp2px
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.components_tools.test", appContext.packageName)
    }

    @Test
    fun dp2px() {
        val start = System.nanoTime()
        repeat(10000) {
            it.dp2px
        }
        println("测试1" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start))

        val start2 = System.nanoTime()
        repeat(10000) {
            dp2px(it.toFloat())
        }
        println("测试2" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start2))

    }
}

fun dp2px(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        Resources.getSystem().getDisplayMetrics()
    )
}

fun sp2px(sp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        Resources.getSystem().getDisplayMetrics()
    )
}