package com.pnx.momassignment.util;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonLogPrint {
    private final static String TAG = JsonLogPrint.class.getSimpleName();

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";

    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "KLog";
    private static final String SUFFIX = ".java";

    public static final int JSON_INDENT = 4;
    public static final int V = 0x1;

    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;

    public static final int E = 0x5;
    public static final int A = 0x6;

    private static final int JSON = 0x7;
    private static final int XML = 0x8;

    private static final int STACK_TRACE_INDEX = 5;

    private static String mGlobalTag;
    private static boolean mIsGlobalTagEmpty = true;
    private static boolean IS_SHOW_LOG = true;


//    public static String printJson(String msg) {
//        return printJson(TAG, msg);
//    }

    public static String printJson(String tag, String msg) {
        return printJson(tag, msg, false);
    }
    public static String printJson(String tag, String msg, Boolean isLog) {
        String headString = "";
        String message;

        if (TextUtils.isEmpty(msg))
            return msg;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        String outString = "";
        for (String line : lines) {
            line = line.replaceAll(LINE_SEPARATOR, "");
            if (isLog)
                Log.i(tag, line);
            outString += line + "\n";
        }
        return outString;
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
}