package com.luckhost.data.repository

import com.luckhost.data.localStorage.models.Note
import com.luckhost.data.localStorage.materials.NotesStorage
import com.luckhost.data.localStorage.models.OperationType
import com.luckhost.data.localStorage.models.PendingOperation
import com.luckhost.data.localStorage.models.SyncState
import com.luckhost.data.localStorage.sync.SyncQueue
import com.luckhost.data.network.NetworkModule
import com.luckhost.data.network.dto.AccessTokens
import com.luckhost.data.network.models.Either
import com.luckhost.domain.models.Either as DomainEither
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
    private val syncQueue: SyncQueue,
) : NotesRepositoryInterface {

    // -------------------- AUTH --------------------

    private suspend fun getAuthToken(): AuthToken? {
        return try {
            when (val tokens = authTokensRepoImp.getTokens()) {
                is DomainEither.Left -> null
                is DomainEither.Right -> tokens.b
            }
        } catch (e: Exception) {
            null
        }
    }

    // -------------------- CREATE --------------------

    override fun saveNote(saveObject: NoteModel) {
        CoroutineScope(Dispatchers.IO).launch {

            val note = Note(
                serverId = saveObject.serverId,
                content = saveObject.content,
                noteHash = saveObject.hashCode
                    ?: throw NullPointerException("hashCode is null"),
                contentHash = saveObject.content.hashCode().toString(),
                syncState = SyncState.PENDING_CREATE
            )

            notesStorage.saveNote(note)

            syncQueue.add(PendingOperation(note.noteHash, OperationType.CREATE))

            processQueue()
        }
    }

    // -------------------- READ --------------------

    override suspend fun getNotes(): List<NoteModel> {
        return withContext(Dispatchers.IO) {

            val local = notesStorage.getNotes()
                .map {
                    NoteModel(
                        serverId = it.serverId,
                        content = it.content.toMutableList(),
                        hashCode = it.noteHash
                    )
                }.toList()

            processQueue()

            local
        }
    }

    override suspend fun getNoteByHash(noteHash: String): NoteModel {
        return getNotes().find {
            it.hashCode == noteHash
        } ?: throw NullPointerException("getNoteByHash fail")
    }

    // -------------------- DELETE --------------------

    override suspend fun deleteNote(noteHash: String) {
        withContext(Dispatchers.IO) {

            val note = try {
                notesStorage.getNoteByHash(noteHash)
            } catch (e: Exception) {
                null
            }

            note?.let {
                it.syncState = SyncState.PENDING_DELETE
                notesStorage.changeNote(noteHash, it)

                syncQueue.add(PendingOperation(noteHash, OperationType.DELETE))
            }

            processQueue()
        }
    }

    // -------------------- UPDATE --------------------

    override suspend fun changeNote(noteHash: String, saveObject: NoteModel) {
        withContext(Dispatchers.IO) {

            val note = Note(
                serverId = saveObject.serverId,
                content = saveObject.content,
                noteHash = noteHash,
                syncState = SyncState.PENDING_UPDATE
            )

            notesStorage.changeNote(noteHash, note)

            syncQueue.add(PendingOperation(noteHash, OperationType.UPDATE))

            processQueue()
        }
    }

    // -------------------- SYNC ENGINE --------------------

    private fun processQueue() {
        CoroutineScope(Dispatchers.IO).launch {

            val token = getAuthToken() ?: return@launch
            val userId = token.userId ?: return@launch

            val operations = syncQueue.getAll()

            operations.forEach { operation ->

                val note = try {
                    notesStorage.getNoteByHash(operation.noteHash)
                } catch (e: Exception) {
                    syncQueue.remove(operation.noteHash)
                    return@forEach
                }

                when (operation.type) {

                    OperationType.CREATE -> {
                        val result = networkModule.createNewNote(
                            AccessTokens(token.accessToken, token.refreshToken),
                            note.copy(user = userId),
                            userId
                        )

                        if (result is Either.Right) {
                            notesStorage.changeNote(
                                note.noteHash,
                                result.b.copy(syncState = SyncState.SYNCED)
                            )
                            syncQueue.remove(note.noteHash)
                        }
                    }

                    OperationType.UPDATE -> {
                        val result = networkModule.changeNoteById(
                            AccessTokens(token.accessToken, token.refreshToken),
                            note.copy(user = userId)
                        )

                        if (result is Either.Right) {
                            note.syncState = SyncState.SYNCED
                            notesStorage.changeNote(note.noteHash, note)
                            syncQueue.remove(note.noteHash)
                        }
                    }

                    OperationType.DELETE -> {
                        val result = networkModule.deleteNoteById(
                            AccessTokens(token.accessToken, token.refreshToken),
                            note.copy(user = userId)
                        )

                        if (result is Either.Right) {
                            notesStorage.deleteNote(note.noteHash)
                            syncQueue.remove(note.noteHash)
                        }
                    }
                }
            }
        }
    }
}

