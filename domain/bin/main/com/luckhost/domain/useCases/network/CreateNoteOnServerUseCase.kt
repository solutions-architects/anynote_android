package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.models.network.UserAccountParams
import com.luckhost.domain.repository.NetworkServiceInterface

class CreateNoteOnServerUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(
        accessToken: AuthToken,
        noteModel: NoteModel
    ): Either<NetworkErrorDescription, NoteModel> {
        val response = netApi.createNote(
            accessToken = accessToken,
            note = noteModel,
        )

        return response
    }
}