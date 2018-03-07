package com.fala.emba.team.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * 日志管理类
 * Created by SoMustYY on 2017/7/4.
 */
public class LogManager {
    public static final String PATH_LOG_ERROR = FileUtil.APP_LOG_PATH + "error/";
    /**
     * 日志开关
     */
    public static boolean LOG_OPEN_DEBUG = false;
    private static final String AUTHOR = "fala-emba:";
    private static final
    ThreadLocal<ReusableFormatter>
            thread_local_formatter =
            new ThreadLocal<ReusableFormatter>() {
                protected ReusableFormatter initialValue() {
                    return new ReusableFormatter();
                }
            };
    public static void d(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG) {
                Log.d(tag, AUTHOR + message);
            }
        }
    }
    public static void i(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG) {
                Log.i(tag, AUTHOR + message);
            }
        }
    }
    public static void w(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG) {
                Log.w(tag, AUTHOR + message);
            }
        }
    }
    public static void e(String tag, String message) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG) {
                Log.e(tag, AUTHOR + message);
            }
        }
    }
    public static void e(String tag, String message, Throwable e) {
        if (message != null && message != null) {
            if (LOG_OPEN_DEBUG) {
                Log.e(tag, AUTHOR + message, e);
            }
        }
    }
    /**
     * A little trick to reuse a formatter pop_in the same thread
     */
    private static class ReusableFormatter {
        private Formatter formatter;
        private StringBuilder builder;
        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }
    }
}
