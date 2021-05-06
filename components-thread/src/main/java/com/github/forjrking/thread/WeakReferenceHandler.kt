package com.github.forjrking.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

/**
 * Convenience class for making a static inner [Handler] class that keeps
 * a [WeakReference] to its parent class. If the reference is cleared, the
 * [WeakReferenceHandler] will stop handling [Message]s.
 *
 *
 * Example usage:
 * <pre>
 * private final MyHandler mHandler = new MyHandler(this);
 *
 * private static class MyHandler extends WeakReferenceHandler<MyClass> {
 * protected void handleMessage(Message msg, MyClass parent) {
 * parent.onMessageReceived(msg.what, msg.arg1);
 * }
 * }
</MyClass></pre> *
 *
 * 主要用于防止内存泄漏问题 需要实现类必须为static
 * @param <T> The handler's parent class.
</T> */
class WeakReferenceHandler<T>(
    parent: T,
    looper: Looper = Looper.myLooper()!!,
    private val handleMessage: (msg: Message?, parent: T) -> Unit
) : Handler(looper), LifecycleObserver {

    private val mParentRef: WeakReference<T> = WeakReference(parent)

    init {
        (parent as? LifecycleOwner)?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        removeCallbacksAndMessages(null)
//        (parent as? LifecycleOwner)?.lifecycle?.removeObserver(this)
    }

    override fun handleMessage(msg: Message) {
        val parent = referenceInstance ?: return
        handleMessage.invoke(msg, parent)
    }

    /**
     * @return The parent class, or `null` if the reference has been
     * cleared.
     */
    private val referenceInstance: T?
        get() = mParentRef.get()

}