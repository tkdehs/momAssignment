package com.pnx.momassignment.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object UIUtil {

    /**
     * 키패드 띄우기
     */
    fun showKeyboard(activity: Activity, view: View): InputMethodManager {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
        return imm
    }

    /**
     * 소프트웨어 키보드 내리기
     */
    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}