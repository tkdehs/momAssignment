package com.pnx.momassignment.network

import com.pnx.momassignment.network.recive.RecvUser
import com.pnx.momassignment.room.data.UserItem
import retrofit2.http.*


/**
 * Retrofit API Interface
 */
interface IAPIService {
    /**
     * 사용자 정보 조회
     */
    @GET("/search/users")
    suspend fun userList(@Query("q") q: String,
                         @Query("sort") sort: String = "",
                         @Query("order") order: String = "desc",
                         @Query("per_page") per_page: Int = 30,
                         @Query("page") page: Int = 1,
                         @Header("accept")user_token: String = "application/vnd.github.v3+json"): RecvUser

    @GET("/users/{login}")
    suspend fun userDetail(@Path("login")login: String,
                         @Header("accept")user_token: String = "application/vnd.github.v3+json"): UserItem
}