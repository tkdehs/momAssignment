package com.pnx.momassignment.network

import com.pnx.momassignment.network.recive.RecvUser
import retrofit2.http.*


/**
 * Retrofit API Interface
 */
interface IAPIService {
    /**
     * 사용자 정보 조회
     */
    @GET("/search/users")
    suspend fun userInfo(@Query("q") q: String,
                         @Query("sort") sort: String = "",
                         @Query("order") order: String = "desc",
                         @Query("per_page") per_page: Int = 30,
                         @Query("page") page: Int = 1,
                         @Header("accept")user_token: String = "application/vnd.github.v3+json"): RecvUser

}