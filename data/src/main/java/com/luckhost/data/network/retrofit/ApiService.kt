package com.luckhost.data.network.retrofit

import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.AccountParams
import com.luckhost.data.network.dto.CreateNoteRequest
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.network.dto.RefreshToken
import com.luckhost.data.network.dto.VerifyTokenAnswer
import com.luckhost.data.network.dto.VerifyTokenRequest
import com.luckhost.data.storage.models.Note
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginAnswerBody

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): AccountAnswerBody

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshToken)

    @POST("auth/verify")
    suspend fun verifyToken(@Body request: VerifyTokenRequest): VerifyTokenAnswer

    @GET("account")
    suspend fun getAccountParams(@Header("Authorization") token: String): AccountAnswerBody

    @PATCH("account")
    suspend fun changeAccountParams(@Header("Authorization") token: String,
                                    @Body request: AccountParams)

    @GET("account/notes")
    suspend fun getAllNotes(@Header("Authorization") token: String): Note

    @POST("account/notes")
    suspend fun createNewNote(@Body note: CreateNoteRequest): Note

    // still in development on the server
    @PATCH("account/notes/{id}")
    suspend fun changeNoteById(@Header("Authorization") token: String,
                            @Path("id") id: Int,
                            @Body request: Note,
    )
}