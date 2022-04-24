package com.pnx.momassignment.util;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonLogPrint {
    private final static String TAG = JsonLogPrint.class.getSimpleName();

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final int JSON_INDENT = 4;

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
}