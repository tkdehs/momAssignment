package com.pnx.momassignment.activity

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ActivityViewModel: ViewModel(){
    private val _progressState = MutableLiveData(View.GONE)
    val progressState: LiveData<Int> = _progressState

    /**
     * Progress Layout 상태 처리
     * @param showState Int : View.VISIBLE, View.GONE로 상태 변경 처리
     */
    fun progressStatus(showState: Int) {
        _progressState.value = showState
    }
}