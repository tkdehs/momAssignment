package com.pnx.momassignment

import android.app.Application

class BaseApplication: Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
    }

    companion object {
        private var instance: BaseApplication? = null
        fun applicationContext() : BaseApplication {
            return instance as BaseApplication
        }
    }
}