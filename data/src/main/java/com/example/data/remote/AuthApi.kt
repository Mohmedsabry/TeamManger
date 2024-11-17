package com.example.data.remote

import com.example.data.remote.dto.RequestPasswordDto
import com.example.data.remote.dto.UserDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AuthApi {
    @POST("/auth")
    @Multipart
    suspend fun signup(
        @Part user: MultipartBody.Part,
        @Part image: MultipartBody.Part? = null
    )

    @GET("/auth")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): UserDto

    @POST("/forgetPassword")
    suspend fun forgetPassword(
        @Query("email") email: String
    )

    @POST("/checkCode")
    suspend fun checkCode(
        @Body request: RequestPasswordDto
    )

    @GET("/updatePassword")
    suspend fun updatePassword(
        @Query("email") email: String,
        @Query("password") password: String
    ): UserDto
}