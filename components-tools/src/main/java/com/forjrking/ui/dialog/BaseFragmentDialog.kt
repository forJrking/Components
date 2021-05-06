package com.forjrking.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window

/** DES: 一般用于给其他dialog继承实现逻辑 */
abstract class BaseFragmentDialog : androidx.fragment.app.DialogFragment() {
    protected lateinit var mRoot: View
    protected var mContext: Context? = null
    protected var mActivity: androidx.fragment.app.FragmentActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /** DES: 防止销毁 */
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRoot = inflater.inflate(getLayoutId(), container, false)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val decorView = dialog?.window?.decorView
        decorView?.background = ColorDrawable(Color.TRANSPARENT)
        isCancelable = cancelable()
        mContext = context
        mActivity = activity
        initView(mRoot)
        return mRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOther(view, dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.invoke()
    }

    /** DES: 处理view */
    protected abstract fun initView(root: View)

    /** DES: 添加监听等 防止取消等等*/
    protected fun initOther(root: View, dialog: Dialog?) {}

    /** DES: 布局资源的id */
    abstract fun getLayoutId(): Int

    /** DES: 距离屏幕外边距  一般取左右平均值 */
    protected fun marginWidth(): Int {
        return 80
    }

    /** DES: 返回键可否取消 */
    protected fun cancelable(): Boolean {
        return false
    }

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog?.window?.setLayout(
            dm.widthPixels - marginWidth(),
            dialog?.window?.attributes!!.height
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mActivity = null
        mContext = null
    }

    fun show(manager: androidx.fragment.app.FragmentManager) {
        show(manager, this.javaClass.name)
    }

    override fun show(manager: androidx.fragment.app.FragmentManager, tag: String?) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commitNowAllowingStateLoss()
            super.show(manager, tag)
        } catch (e: Exception) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }

    private var listener: (() -> Unit)? = null

    fun setOnDismissListener(function: () -> Unit) {
        listener = function
    }
}