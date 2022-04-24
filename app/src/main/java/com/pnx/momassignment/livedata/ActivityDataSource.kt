package com.pnx.momassignment.livedata

import android.os.Bundle
import android.view.View
import com.pnx.momassignment.BaseApplication
import com.pnx.momassignment.R
import com.pnx.momassignment.activity.ActivityViewModel
import com.pnx.momassignment.util.NetworkUtil
import kotlinx.coroutines.*

/**
 * Activity와 Fragment에서 값을 공유 하며 ViewModel에서 Base역활을 한다.
 * Alert 메시지와 Progress등 공통적인 기능 처리 담당
 */
class ActivityDataSource(private val activityViewModel: ActivityViewModel) {
    private val TAG = ActivityDataSource::class.simpleName

    /**
     * 네트워크 Thread 처리
     * 프로그래스 상태 처리 및 오류 알림을 위한 Try/Catch처리
     */
    fun launchNetworkLoad(viewModelScope: CoroutineScope, block: suspend() -> Unit): Job {
        return viewModelScope.launch {
            delay(10)
            try {
                if (!NetworkUtil.checkConnectNetwork(BaseApplication.applicationContext())) {
                    return@launch
                }
                showProgress()
                block()
            } catch (error: Throwable) {
                if (error is CancellationException) {
                    // job cancelAndJoin() 에러 제외
//                    Log.e(TAG, "launchNetworkLoad CancellationException 에러 제외")
                } else {
                    error.printStackTrace()
                    hideProgress()
                }
            } finally {
                hideProgress()
            }
        }
    }

    fun launchNetworkLoad(viewModelScope: CoroutineScope, successBlock: suspend() -> Unit, errorBlock: suspend(error: Throwable) -> Unit): Job {
        return viewModelScope.launch {
            try {
                if (!NetworkUtil.checkConnectNetwork(BaseApplication.applicationContext())) {
//                    alertMessage(VmUtil.getResourceString(R.string.connection_error_message))
//                    return@launch
                    throw Exception()
                }
                showProgress()
                successBlock()
            } catch (error: Throwable) {
                if (error is CancellationException) {
                    // job cancelAndJoin() 에러 제외
//                    Log.e(TAG, "launchNetworkLoad CancellationException 에러 제외")
                } else {
                    error.printStackTrace()
                    errorBlock(error)
                }
            } finally {
                hideProgress()
            }
        }
    }
    /**
     * Progress 상태 처리
     */
    fun progressStatus(state: Int) {
        activityViewModel.progressStatus(state)
    }

    /**
     * Progress visible 처리
     */
    fun showProgress() {
        progressStatus(View.VISIBLE)
    }

    /**
     * Progress gone 처리
     */
    fun hideProgress() {
        progressStatus(View.GONE)
    }
}