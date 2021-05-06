package com.forjrking.tools

import android.content.Context

object DpUtil {
    /*** 获取手机屏幕宽度 */
    @JvmStatic
    fun getScreenWidth(context: Context? = null): Int {
        return context?.resources?.displayMetrics?.widthPixels
            ?: resources().displayMetrics.widthPixels
    }

    /*** 获取手机屏幕高度 */
    @JvmStatic
    fun getScreenHeight(context: Context? = null): Int {
        return context?.resources?.displayMetrics?.heightPixels
            ?: resources().displayMetrics.heightPixels
    }

    /**
     * Value of dp to value of px.
     *
     * @param dpValue The value of dp.
     * @return value of px
     */
    @JvmStatic
    fun dp2px(dpValue: Float): Int {
        val scale = resources().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    @JvmStatic
    fun sp2px(spValue: Float): Float {
        val fontScale = resources().displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }

    /**
     * Value of px to value of dp.
     *
     * @param pxValue The value of px.
     * @return value of dp
     */
    @JvmStatic
    fun px2dp(pxValue: Float): Int {
        val scale = resources().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * Value of px to value of sp.
     *
     * @param pxValue The value of px.
     * @return value of sp
     */
    @JvmStatic
    fun px2sp(pxValue: Float): Int {
        val fontScale = resources().displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    private fun resources() = Cxt.res
}