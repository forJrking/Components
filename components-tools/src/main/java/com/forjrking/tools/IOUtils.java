package com.forjrking.tools;

import java.io.Closeable;
import java.io.IOException;

/**
 * DES:
 * TIME: 2018/12/6 0006 下午 2:06
 */
public class IOUtils {

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeable = null;
        }
    }

    /**
     * 关闭资源
     */
    public static void closeQuietly(Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            for (int i = 0; i < closeables.length; i++) {
                closeQuietly(closeables[i]);
            }
        }
    }
}
