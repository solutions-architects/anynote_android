package com.luckhost.data.network.retrofit

import com.luckhost.data.network.dto.AccessToken
import com.luckhost.data.network.dto.AccountAnswerBody
import com.luckhost.data.network.dto.AccountParams
import com.luckhost.data.network.dto.CreateNoteRequest
import com.luckhost.data.network.dto.LoginAnswerBody
import com.luckhost.data.network.dto.LoginRequest
import com.luckhost.data.network.dto.RefreshToken
import com.luckhost.data.network.dto.VerifyTokenRequest
import com.luckhost.data.localStorage.models.Note
import okhttp3.Request
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): Response<LoginAnswerBody>

    @POST("auth/register/")
    suspend fun register(@Body request: LoginRequest): Response<AccountAnswerBody>

    @POST("auth/refresh/")
    suspend fun refreshAccessToken(@Body request: RefreshToken): Response<AccessToken>

    @POST("auth/verify")
    suspend fun verifyToken(@Body request: VerifyTokenRequest): Response<Request>

    @GET("account")
    suspend fun getAccountParams(
        @Header("Authorization") token: String): Response<AccountAnswerBody>

    @PATCH("account")
    suspend fun changeAccountParams(@Header("Authorization") token: String,
                                    @Body request: AccountParams): Response<Request>

    @GET("account/notes")
    suspend fun getAllNotes(@Header("Authorization") token: String): Response<List<Note>>

    @POST("account/notes")
    suspend fun createNewNote(@Header("Authorization") token: String,
                              @Body note: CreateNoteRequest): Response<Note>

    // still in development on the server
    @PATCH("account/notes/{id}")
    suspend fun changeNoteById(@Header("Authorization") token: String,
                            @Path("id") id: Int, @Body request: Note): Response<Request>
}