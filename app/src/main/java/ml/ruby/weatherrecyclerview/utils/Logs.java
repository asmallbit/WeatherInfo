package ml.ruby.weatherrecyclerview.utils;

import android.util.Log;

/**
 * @author: jwhan
 * @createTime: 2022/05/02 5:18 PM
 * @description:
 */
public class Logs {
    private static boolean isShow = true;

    public static void logVerbose(String tag, String content) {
        if (isShow) {
            Log.v(tag, content);
        }
    }

    public static void logDebug(String tag, String content) {
        if (isShow) {
            Log.d(tag, content);
        }
    }

    public static void logInfo(String tag, String content) {
        if (isShow) {
            Log.d(tag, content);
        }
    }

    public static void logWarn(String tag, String content) {
        if (isShow) {
            Log.d(tag, content);
        }
    }

    public static void logError(String tag, String content) {
        if (isShow) {
            Log.d(tag, content);
        }
    }

    public static void logAssert(String tag, String content) {
        if (isShow) {
            Log.d(tag, content);
        }
    }
}
