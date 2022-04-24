package com.pnx.momassignment.component

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.pnx.momassignment.R
import com.pnx.momassignment.databinding.CdialogLayoutBinding

/**
 * 공통 메시지 다이얼로그
 */
open class CDialog : Dialog {

    private lateinit var binding: CdialogLayoutBinding

    private val mCancelClickListener: View.OnClickListener? = null
    private var mOkClickListener: View.OnClickListener? = null
    private var gravityType = Gravity.CENTER
    private var usePadding = true
    private var isBackPressed = true
    private var mIsAutoDismiss = true
    private var isBackGroundClose = false       // 백그라운드 터치시 닫기 여부 default false

    constructor(context: Context) : super(context) {// android.R.style.Theme_Translucent_NoTitleBar) { // Dialog 배경을 투명 처리 해준다.
        init()
    }

    //다이알로그 위치 지정
    constructor(context: Context,
                gravityType: Int = Gravity.CENTER,
                usePadding: Boolean = false,
                isBackGroundClose : Boolean = false) : super(context) {//, android.R.style.Theme_Translucent_NoTitleBar);

        init()

        window!!.setGravity(gravityType)
        this.usePadding = usePadding
        this.gravityType = gravityType
        this.isBackGroundClose = isBackGroundClose
    }

    open fun init() {
        binding = CdialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        window!!.setGravity(Gravity.TOP) // default 상단 배치
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        // Dialog에 생기는 패딩을 제거 한다.
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(gravityType) // default 하단 배치

        binding.topXBtn.setOnClickListener { dismiss() }
        setClickListener()

        setCanceledOnTouchOutside(isBackGroundClose) // 백그라운드 터치 dissmiss
        binding.dialogBtnOk.visibility = View.GONE
        binding.dialogBtnOk.setOnClickListener(null)

        binding.dialogBtnCancelLayout.visibility  = View.GONE
        binding.dialogBtnCancel.setOnClickListener(null)

        binding.dialogBtnLayout.visibility = View.GONE

        binding.dialogTitle.text = context.getString(R.string.text_confirm)

        setOnDismissListener(null)
        try {
            if (!isShowing)
                show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setView(view: View, minHeight: Int, titleVisible:Boolean = false): CDialog {
        binding.dialogContentLayout.removeAllViews()
        binding.dialogContentLayout.addView(view)

        if(titleVisible) binding.titleLayout.visibility = View.VISIBLE
        else binding.titleLayout.visibility = View.GONE

        binding.dialogContentLayout.setPadding(0, 0, 0, 0)
        if (minHeight > 0) binding.dialogContentLayout.minimumHeight = minHeight
        binding.dialogBtnOk.visibility = View.GONE
        binding.dialogBtnLayout.visibility = View.GONE
        return this
    }



    /**
     * 타이틀 세팅
     * 타이틀이 없으면 타이틀 영역을 Gone 처리
     *
     * @param title
     */
    fun setTitle(title: Any, vararg formatArgs: Any?): CDialog {
        var titleStr = ""
        if (title is Int) {
            titleStr = context.getString(title, *formatArgs)
            if (!TextUtils.isEmpty(titleStr)) {
                binding.dialogTitle.visibility = View.VISIBLE
                binding.dialogTitle.text = titleStr
            }
        } else if (title is String) {
            titleStr = String.format(title.toString(), *formatArgs)
            if (!TextUtils.isEmpty(titleStr)) {
                binding.dialogTitle.visibility = View.VISIBLE
                binding.dialogTitle.text = titleStr
            }
        }
        return this
    }

    fun setMessage(title: Any, vararg formatArgs: Any?): CDialog {
        var titleStr = ""
        if (title is Int) {
            titleStr = context.getString(title, *formatArgs)
            if (!TextUtils.isEmpty(titleStr)) {
                binding.dialogMessageTv.visibility = View.VISIBLE
                binding.dialogMessageTv.text = titleStr
            }
        } else if (title is String) {
            titleStr = String.format(title.toString(), *formatArgs)
            if (!TextUtils.isEmpty(titleStr) || titleStr != "") {
                binding.dialogMessageTv.visibility = View.VISIBLE
                binding.dialogMessageTv.text = titleStr
            } else {
                binding.dialogContentLayout.visibility = View.GONE
            }
        }
        return this
    }

    private fun setClickListener() {   //final View.OnClickListener cancelClickListener, final View.OnClickListener okClickListener) {
        Log.i("", "setClickListener=$binding.dialogBtnCancel")
        if (mCancelClickListener == null) {
            binding.dialogBtnCancelLayout.visibility = View.GONE
        }
        binding.dialogBtnCancel.setOnClickListener {
            if (mCancelClickListener != null) {
                mCancelClickListener.onClick(it)
                if (mIsAutoDismiss) {
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
        binding.dialogBtnOk.setOnClickListener {
            if (mOkClickListener != null) {
                mOkClickListener!!.onClick(it)
                if (mIsAutoDismiss) {
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }

    /**
     * 왼쪽 버튼 세팅
     */
    fun setCancelButton(label: Any = context.getString(R.string.text_cancel), cancelClickListener: View.OnClickListener?): CDialog {
        binding.dialogBtnLayout.visibility = View.VISIBLE
        binding.dialogBtnCancelLayout.visibility = View.VISIBLE
        if (label is Int) {
            binding.dialogBtnCancel.text = context.getString(label)
        } else if (label is String)
            binding.dialogBtnCancel.text = label
        setAlertButtonClickListener(binding.dialogBtnCancel, cancelClickListener)
        return this
    }

    /**
     * 왼쪽 버튼 세팅
     */
    fun setAutoDismiss(isAutoDismiss: Boolean): CDialog {
        mIsAutoDismiss = isAutoDismiss
        return this
    }

    /**
     * 오른쪽 버튼 세팅
     *
     * @param okClickListener
     */
//    fun setOkButton(okClickListener: View.OnClickListener?): CDialog {
//        binding.dialogBtnLayout.visibility = View.VISIBLE
//        val label = binding.dialogBtnOk.text.toString()
//        return setOkButton(label, okClickListener)
//    }

//    fun setOkButton(label: Int, okClickListener: View.OnClickListener?): CDialog {
//        return setOkButton(context.resources.getString(label), okClickListener)
//    }

    fun setOkButton(label: Any = context.getString(R.string.text_confirm)
                    , okClickListener: View.OnClickListener?): CDialog {
        mOkClickListener = okClickListener
        binding.dialogBtnLayout.visibility = View.VISIBLE
        binding.dialogBtnOk.visibility = View.VISIBLE
        if (label is Int) {
            binding.dialogBtnOk.text = context.getString(label)
        } else if (label is String)
            binding.dialogBtnOk.text = label
        setAlertButtonClickListener(binding.dialogBtnOk, okClickListener)
        return this
    }

    /**
     * X 버튼
     *
     * @param isVisible
     * @return
     */
    fun setVisibleXButton(isVisible: Boolean): CDialog {
        binding.topXBtn.visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.topXBtn.setOnClickListener {
            dismiss()
        }
        return this
    }

    /**
     * X 버튼 클릭 리스너
     */
    fun setOnXButtonListener(clickListener: View.OnClickListener): CDialog {
        setVisibleXButton(true)
        binding.topXBtn.setOnClickListener{
            clickListener.onClick(it)
            dismiss()
        }
        return this
    }

    fun setOkButtonDissmissListener(
        button: Button?,
        clickListener: View.OnClickListener?
    ): CDialog {
        mOkClickListener = clickListener
        return this
    }

    fun setCancelButtonDissmissListener(
        button: Button,
        clickListener: View.OnClickListener?
    ): CDialog {
        button.hasOnClickListeners()
        return this
    }

//    override fun dismiss() {
//        super.dismiss()
//        mDismissListener?.onDissmiss()
//    }

//    interface DismissListener {
//        fun onDissmiss()
//    }

    fun setIsBackPressed(isBackPressed: Boolean): CDialog {
        this.isBackPressed = isBackPressed
        return this
    }

    private fun setAlertButtonClickListener(button: Button?, clickListener: View.OnClickListener?) {
        button!!.setOnClickListener {
            if (clickListener != null) {
                clickListener.onClick(it)
                if (mIsAutoDismiss) {
                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }

    override fun onBackPressed() {
        if (isBackPressed) {
            super.onBackPressed()
        }
    }

    companion object {
//        private var mDismissListener: DismissListener? = null

        fun showDlg(context: Context, message: String): CDialog {
            val dlg = CDialog(context)
            dlg.setMessage(message)
            dlg.setOkButton{ dlg.dismiss() }
            return dlg
        }

    }
}