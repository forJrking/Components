package com.forjrking.tools.thread

import android.os.Handler
import android.os.Looper
import android.os.Message
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
open abstract class WeakReferenceHandler<T> : Handler {
    private val mParentRef: WeakReference<T>

    /**
     * Constructs a new [WeakReferenceHandler] with a reference to its
     * parent class.
     *
     * @param parent The handler's parent class.
     */
    constructor(parent: T) {
        mParentRef = WeakReference(parent)
    }

    /**
     * Constructs a new [WeakReferenceHandler] with a reference to its
     * parent class.
     *
     * @param parent The handler's parent class.
     * @param looper The looper.
     */
    constructor(parent: T, looper: Looper?) : super(looper) {
        mParentRef = WeakReference(parent)
    }

    override fun handleMessage(msg: Message) {
        val parent = referenceInstance ?: return
        handleMessage(msg, parent)
    }

    /**
     * @return The parent class, or `null` if the reference has been
     * cleared.
     */
    open val referenceInstance: T?
        get() = mParentRef.get()

    /**
     * Subclasses must implement this to receive messages.
     */
    protected abstract fun handleMessage(msg: Message?, parent: T)
}