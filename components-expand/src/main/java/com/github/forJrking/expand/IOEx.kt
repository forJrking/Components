package com.github.forJrking.expand

import java.io.Closeable

inline fun Closeable?.closeQuietly() {
    try {
        this?.close()
    } catch (e: Throwable) {
        // ignore
    }
}

inline fun <T : Closeable?, R> T.autoClose(block: (T) -> R): R {
    var closed = false
    try {
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            this?.close()
        } catch (closeException: Exception) {
        }
        throw e
    } finally {
        if (!closed) {
            this?.close()
        }
    }
}