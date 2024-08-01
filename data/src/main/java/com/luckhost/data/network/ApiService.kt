package com.luckhost.data.network

import com.luckhost.data.storage.models.network.AccountAnswerBody
import com.luckhost.data.storage.models.network.LoginAnswerBody
import com.luckhost.data.storage.models.network.LoginRequest
import com.luckhost.data.storage.models.Note
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("account/notes/")
    suspend fun getAllNotes(): Note

    @GET("notes/{id}")
    suspend fun getNoteById(@Path("id") id: Int): Note

    @POST("notes")
    suspend fun saveNote(@Body note: Note)

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginAnswerBody

    @POST("auth/register")
    suspend fun register(@Body request: LoginRequest): AccountAnswerBody
}