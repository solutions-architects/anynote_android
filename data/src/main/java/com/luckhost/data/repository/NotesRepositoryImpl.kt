package com.luckhost.data.repository

import android.util.Log
import com.luckhost.data.localStorage.models.Note
import com.luckhost.data.localStorage.materials.NotesStorage
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
) : NotesRepositoryInterface {

    // -------------------- AUTH --------------------

    private suspend fun getAuthToken(): AuthToken? {
        return try {
            when (val tokens = authTokensRepoImp.getTokens()) {
                is DomainEither.Left -> {
                    Log.e("NotesRepositoryImpl", tokens.a.toString())
                    null
                }
                is DomainEither.Right -> tokens.b
            }
        } catch (e: Exception) {
            Log.e("NotesRepositoryImpl", e.toString())
            null
        }
    }

    // -------------------- CREATE --------------------

    override fun saveNote(saveObject: NoteModel) {
        CoroutineScope(Dispatchers.IO).launch {

            val localNote = Note(
                serverId = saveObject.serverId,
                content = saveObject.content,
                noteHash = saveObject.hashCode
                    ?: throw NullPointerException("hashCode is null"),
                contentHash = saveObject.content.hashCode().toString()
            )

            // ✅ 1. Всегда сохраняем локально
            notesStorage.saveNote(localNote)

            // ✅ 2. Пытаемся синкнуть
            syncCreateNote(localNote)
        }
    }

    private suspend fun syncCreateNote(note: Note) {
        val token = getAuthToken() ?: return

        val userId = token.userId ?: return

        val response = networkModule.createNewNote(
            accessToken = AccessTokens(token.accessToken, token.refreshToken),
            note = note.copy(user = userId),
            user = userId
        )

        when (response) {
            is Either.Right -> {
                val serverNote = response.b

                // обновляем локальную запись (serverId и hash)
                notesStorage.changeNote(
                    note.noteHash,
                    serverNote
                )
            }
            is Either.Left -> {
                Log.e("NotesRepositoryImpl", "syncCreateNote failed: ${response.a}")
            }
        }
    }

    // -------------------- READ --------------------

    override suspend fun getNotes(): List<NoteModel> {
        return withContext(Dispatchers.IO) {

            // ✅ 1. Сразу локальные данные
            val localNotes = notesStorage.getNotes()
                .map { note ->
                    NoteModel(
                        serverId = note.serverId,
                        content = note.content.toMutableList(),
                        hashCode = note.noteHash
                    )
                }.toList()

            // ✅ 2. Фоновая синхронизация
            launchSyncNotes()

            localNotes
        }
    }

    private fun launchSyncNotes() {
        CoroutineScope(Dispatchers.IO).launch {

            val token = getAuthToken() ?: return@launch

            val result = networkModule.getAllNotes(
                AccessTokens(token.accessToken, token.refreshToken)
            )

            when (result) {
                is Either.Right -> {
                    result.b.forEach { networkNote ->
                        notesStorage.saveNote(networkNote)
                    }
                }
                is Either.Left -> {
                    Log.e("NotesRepositoryImpl", "syncNotes failed: ${result.a}")
                }
            }
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
                getNoteByHash(noteHash)
            } catch (e: Exception) {
                null
            }

            // ✅ 1. Удаляем локально
            notesStorage.deleteNote(noteHash)

            // ✅ 2. Пытаемся синкнуть
            note?.let { syncDelete(it) }
        }
    }

    private suspend fun syncDelete(note: NoteModel) {
        val token = getAuthToken() ?: return
        val userId = token.userId ?: return

        val result = networkModule.deleteNoteById(
            AccessTokens(token.accessToken, token.refreshToken),
            Note(
                serverId = note.serverId,
                content = note.content,
                noteHash = note.hashCode ?: return,
                user = userId,
                contentHash = note.content.hashCode().toString()
            )
        )

        if (result is Either.Left) {
            Log.e("NotesRepositoryImpl", "delete sync failed: ${result.a}")
        }
    }

    // -------------------- UPDATE --------------------

    override suspend fun changeNote(noteHash: String, saveObject: NoteModel) {
        withContext(Dispatchers.IO) {

            val note = Note(
                serverId = saveObject.serverId,
                content = saveObject.content,
                noteHash = noteHash
            )

            // ✅ 1. Обновляем локально
            notesStorage.changeNote(noteHash, note)

            // ✅ 2. Синк
            syncChange(note)
        }
    }

    private suspend fun syncChange(note: Note) {
        val token = getAuthToken() ?: return
        val userId = token.userId ?: return

        val result = networkModule.changeNoteById(
            AccessTokens(token.accessToken, token.refreshToken),
            note.copy(user = userId)
        )

        when (result) {
            is Either.Right -> {
                Log.d("NotesRepositoryImpl", "change sync success")
            }
            is Either.Left -> {
                Log.e("NotesRepositoryImpl", "change sync failed: ${result.a}")
            }
        }
    }
}
