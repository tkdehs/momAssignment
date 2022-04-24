package com.pnx.momassignment.network.recive

import java.io.Serializable

open class RecvBase(): Serializable {
    protected val TAG = javaClass.simpleName

    var errors: List<Error> = arrayListOf()
    var message: String = ""    // 에러메시지
    val documentation_url: String = ""

    class Error : Serializable {
        var code: String = ""
        var `field`: String = ""
        var resource: String = ""
    }
}