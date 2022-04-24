package com.pnx.momassignment.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pnx.momassignment.component.ProgressDialog

open class BaseActivity: AppCompatActivity() {
    private val TAG = javaClass.simpleName
    protected val activityViewModel: ActivityViewModel by viewModels()

    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)

        activityViewModel.progressState.observe(this) { visible ->
            if (View.VISIBLE == visible)
                showProgress()
            else
                hideProgress()
        }
    }
    fun showProgress() {
        progressDialog.let {
            progressDialog.show()
        }
    }

    fun hideProgress() {
        progressDialog.let {
            progressDialog.dismiss()
        }
    }

}