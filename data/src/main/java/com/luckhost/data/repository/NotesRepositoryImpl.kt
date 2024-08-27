package com.luckhost.data.repository

import android.util.Log
import com.luckhost.data.localStorage.models.Note
import com.luckhost.data.localStorage.materials.NotesStorage
import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.dto.SuccessMessage
import com.luckhost.data.network.models.NetworkError
import com.luckhost.data.network.models.Either
import com.luckhost.domain.models.Either as DomainEither
import com.luckhost.domain.models.ErrorDescription
import com.luckhost.domain.models.NoteModel
import com.luckhost.domain.models.network.AuthToken
import com.luckhost.domain.repository.AuthTokensRepoInterface
import com.luckhost.domain.repository.NotesRepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesRepositoryImpl(
    private val notesStorage: NotesStorage,
    private val networkModule: NetworkModule,
    private val authTokensRepoImp: AuthTokensRepoInterface,
): NotesRepositoryInterface {

    private suspend fun getAuthToken(
        onSuccess: suspend (AuthToken) -> Unit,
        onError: suspend () -> Unit = {},
    ) {
        try {
            val tokens = authTokensRepoImp.getTokens()

            when(tokens) {
                is DomainEither.Left<ErrorDescription> -> {
                    Log.e("NotesRepositoryImpl", tokens.a.toString())

                    onError()
                }
                is DomainEither.Right<AuthToken> -> {
                    onSuccess(tokens.b)
                }
            }
        } catch (e: Exception) {
            Log.e("NotesRepositoryImpl", e.toString())

            onError()

        }
    }

    override fun saveNote(saveObject: NoteModel) {
        
        try {
            val tokens = authTokensRepoImp.getTokens()
            
            when(tokens) {
                is DomainEither.Left<ErrorDescription> -> {
                    Log.e("NotesRepositoryImpl", tokens.a.toString())
                }
                is DomainEither.Right<AuthToken> -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = networkModule.createNewNote(
                            accessToken =
                                AccessTokens(
                                    accessToken = tokens.b.accessToken,
                                    refreshToken = tokens.b.refreshToken
                                ),
                            note = Note(
                                serverId = null,
                                content = saveObject.content,
                                noteHash = saveObject.hashCode ?:
                                    throw NullPointerException("NotesRepositoryImpl user id is null"),
                                user =
                                    tokens.b.userId
                                        ?: throw NullPointerException("NotesRepositoryImpl user id is null"),
                                contentHash = saveObject.content.hashCode().toString()
                            ),
                            user = tokens.b.userId
                                ?: throw NullPointerException("NotesRepositoryImpl user id is null"),
                            noteHash = null
                        )

                        when(response) {
                            is Either.Left<NetworkError> -> {
                                Log.e("NotesRepositoryImpl response", response.a.toString())

                            }
                            is Either.Right<Note> -> {
                                val note = Note(
                                    serverId = response.b.serverId,
                                    content = saveObject.content,
                                    noteHash = response.b.noteHash,
                                    contentHash = response.b.contentHash,
                                    user = response.b.user
                                )
                                notesStorage.saveNote(note)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("NotesRepositoryImpl", e.toString())

        }
    }

    private suspend fun getNetworkNotes(): List<NoteModel>? {
        var result: List<NoteModel>? = null

        getAuthToken(
            onSuccess = { token ->
                val notes = networkModule.getAllNotes(
                    AccessTokens(
                        accessToken = token.accessToken,
                        refreshToken = token.refreshToken
                    )
                )

                result = when(notes) {
                    is Either.Right<List<Note>> -> {
                        notes.b.map { networkNote ->
                            NoteModel(
                                serverId = networkNote.serverId,
                                content = networkNote.content.toMutableList(),
                                hashCode = networkNote.noteHash,
                            )
                        }
                    }
                    else -> null
                }
            },
            onError = {
                result = null
            }
        )

        return result
    }

    override suspend fun getNotes(): List<NoteModel> {
        return withContext(Dispatchers.IO) {
            // Пробуем получить заметки из сети
            val networkNotes = try {
                getNetworkNotes()
            } catch (e: Exception) {
                null
            }

            // Если получили с сети - возвращаем их
            networkNotes?.let { return@withContext it }

            // Иначе возвращаем из локального хранилища
            notesStorage.getNotes()
                .map { note ->
                    NoteModel(
                        content = note.content.toMutableList(),
                        hashCode = note.noteHash,
                    )
                }.toList()
        }
    }

    override suspend fun getNoteByHash(noteHash: String): NoteModel {
        //val noteFromDb = notesStorage.getNoteByHash(noteHash)

        return getNotes().find {
            it.hashCode == noteHash
        } ?: throw NullPointerException("getNoteByHash fail")

//        return NoteModel(
//            content = noteFromDb.content.toMutableList(),
//            hashCode = noteFromDb.noteHash,
//        )
    }

    override suspend fun deleteNote(noteHash: String) {


        try {
            val token = getAuthToken()
            token?.let {

                val note = getNoteByHash(noteHash)

                val delete = networkModule.deleteNoteById(
                    AccessTokens(
                        accessToken = it.accessToken,
                        refreshToken = it.refreshToken
                    ),
                    note = Note(
                        serverId = note.serverId,
                        content = note.content,
                        noteHash = note.hashCode ?:
                        throw NullPointerException("NotesRepositoryImpl user id is null"),
                        user =
                            token.userId
                                ?: throw NullPointerException("NotesRepositoryImpl user id is null"),
                        contentHash = note.content.hashCode().toString()
                    ),
                )

                when(delete) {
                    is Either.Left<NetworkError> -> {
                        Log.e("NotesRepositoryImpl", delete.a.toString())
                    }
                    is Either.Right<SuccessMessage> -> {
                        Log.e("NotesRepositoryImpl", delete.b.toString())

                        notesStorage.deleteNote(noteHash)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("NotesRepositoryImpl", "Network error: $e")
            null
        }

    }
    // Улучшенная версия с прямым возвратом результата
    private suspend fun getAuthToken(): AuthToken? {
        return try {
            val tokens = authTokensRepoImp.getTokens()

            when(tokens) {
                is DomainEither.Left<ErrorDescription> -> {
                    Log.e("NotesRepositoryImpl", tokens.a.toString())
                    null
                }
                is DomainEither.Right<AuthToken> -> {
                    tokens.b
                }
            }
        } catch (e: Exception) {
            Log.e("NotesRepositoryImpl", e.toString())
            null
        }
    }

    // Тогда основной метод упрощается:
    private suspend fun getNetworkChangeResult(note: Note): Either<NetworkError, SuccessMessage>? {
        return try {
            val token = getAuthToken()
            token?.let {

                token.userId?.let {
                    note.user = it
                } ?: run {
                    Log.e("NotesRepositoryImpl", "userId is null!!")
                }

                networkModule.changeNoteById(
                    AccessTokens(
                        accessToken = it.accessToken,
                        refreshToken = it.refreshToken
                    ),
                    note
                )
            }
        } catch (e: Exception) {
            Log.e("NotesRepositoryImpl", "Network error: $e")
            null
        }
    }

    override suspend fun changeNote(noteHash: String, saveObject: NoteModel) {
        withContext(Dispatchers.IO) {
            // Создаем Note
            val note = Note(
                serverId = saveObject.serverId,
                content = saveObject.content,
                noteHash = noteHash,
            )

            try {
                val networkResult = getNetworkChangeResult(note)

                when(networkResult) {
                    is Either.Left<NetworkError> -> {
                        Log.e("NotesRepositoryImpl", "Sync failed: ${networkResult.a}")
                    }
                    is Either.Right<SuccessMessage> -> {
                        Log.d("NotesRepositoryImpl", "Sync successful: ${networkResult.b}")
                        notesStorage.changeNote(noteHash, note)
                    }
                    null -> {
                        Log.e("NotesRepositoryImpl", "Sync failed: auth error")
                    }
                }
            } catch (e: Exception) {
                Log.e("NotesRepositoryImpl", "Sync error: $e")
            }
        }
    }
}