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
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/reg/")
    suspend fun register(@Body request: LoginRequest): Response<AccountAnswerBody>


    @GET("note/")
    suspend fun getAllNotes(@Header("Authorization") token: String): Response<List<Note>>

    @POST("note/")
    suspend fun createNewNote(@Header("Authorization") token: String,
                              @Body note: CreateNoteRequest): Response<Note>

    @DELETE("note/{id}")
    suspend fun deleteNote(@Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<Request>

    @PATCH("note/{id}")
    suspend fun changeNoteById(@Header("Authorization") token: String,
                               @Path("id") id: Int, @Body request: Note): Response<Request>

    @POST("token/")
    suspend fun login(@Body request: LoginRequest): Response<LoginAnswerBody>




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
}