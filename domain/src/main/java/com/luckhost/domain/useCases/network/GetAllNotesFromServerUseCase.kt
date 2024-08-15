package com.luckhost.domain.useCases.network

import com.luckhost.domain.models.Either
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.models.network.NetworkErrorDescription
import com.luckhost.domain.repository.NetworkServiceInterface

class GetAllNotesFromServerUseCase(
    private val netApi: NetworkServiceInterface,
) {
    suspend fun execute(
        accessToken: AuthToken
    ): Either<NetworkErrorDescription, List<NoteModel>> {
        val response = netApi.getAllNotes(
            accessToken = accessToken
        )
        return response
    }
}