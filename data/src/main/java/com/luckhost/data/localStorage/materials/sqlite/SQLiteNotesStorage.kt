package com.luckhost.data.localStorage.materials.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getIntOrNull
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luckhost.data.localStorage.materials.NotesStorage
import com.luckhost.data.localStorage.models.Note
import kotlinx.coroutines.flow.flow

class SQLiteNotesStorage(context: Context): NotesStorage {
    private val dbHelper = NotesDBHelper(context)
    private var db: SQLiteDatabase? = dbHelper.writableDatabase

    override fun saveNote(saveObject: Note) {
        val noteContent = Gson().toJson(saveObject.content)
        val values = ContentValues().apply {
            put(NotesContract.COLUMN_NAME_SERVER_ID, saveObject.serverId)
            put(NotesContract.COLUMN_NAME_CONTENT, noteContent)
            put(NotesContract.COLUMN_NAME_HASHCODE, saveObject.noteHash)
        }
        db?.insert(NotesContract.TABLE_NAME, null, values)
    }

    override suspend fun getNotes() = flow<Note> {
        val cursor = db?.query(
            NotesContract.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val getContent = cursor.getString(
                cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_CONTENT)
            )

            val type = object : TypeToken<List<MutableMap<String, String>>>() {}.type
            val convertedContent:
                    List<MutableMap<String, String>> = Gson().fromJson(getContent, type)

            emit(
                Note(
                    serverId = cursor.getIntOrNull(
                        cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_SERVER_ID)
                    ),
                    content = convertedContent,
                    noteHash = cursor.getString(
                        cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_HASHCODE)
                    )
                )
            )
        }
        cursor?.close()
    }

    override fun getNoteByHash(noteHash: String): Note {
        val cursor = db?.query(
            NotesContract.TABLE_NAME,
            null,
            "${NotesContract.COLUMN_NAME_HASHCODE} = ?",
            arrayOf(noteHash),
            null,
            null,
            null
        )

        var note: Note? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val id = cursor.getIntOrNull(
                    cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_SERVER_ID)
                )
                val getContent = cursor.getString(
                    cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_CONTENT)
                )

                val type = object : TypeToken<List<MutableMap<String, String>>>() {}.type

                val content:
                    List<MutableMap<String, String>> = Gson().fromJson(getContent, type)

                val hash =
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_HASHCODE))

                note = Note(
                    serverId = id,
                    content = content,
                    noteHash = hash
                )
            }
        }
        cursor?.close()

        note?.let {
            return note
        }
        throw NoSuchElementException("An attempt to get a note from the " +
                "database returned null")
    }

    override fun deleteNote(noteHash: String) {
        val selection = "${NotesContract.COLUMN_NAME_HASHCODE} = ?"
        val selectionArgs = arrayOf(noteHash)
        db?.delete(NotesContract.TABLE_NAME, selection, selectionArgs)
    }

    override fun changeNote(noteHash: String, saveObject: Note) {
        val selection = "${NotesContract.COLUMN_NAME_HASHCODE} = ?"
        val selectionArgs = arrayOf(noteHash)

        val noteContent = Gson().toJson(saveObject.content)
        val values = ContentValues().apply {
            put(NotesContract.COLUMN_NAME_SERVER_ID, saveObject.serverId)
            put(NotesContract.COLUMN_NAME_CONTENT, noteContent)
            put(NotesContract.COLUMN_NAME_HASHCODE, saveObject.noteHash)
        }

        db?.update(
            NotesContract.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }
}
