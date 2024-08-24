package com.luckhost.data.localStorage.materials.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
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
            put(NotesContract.COLUMN_NAME_SERVER_ID, saveObject.id)
            put(NotesContract.COLUMN_NAME_CONTENT, noteContent)
            put(NotesContract.COLUMN_NAME_HASHCODE, saveObject.noteHash)
        }
        db?.insert(NotesContract.TABLE_NAME, null, values)
    }

    override suspend fun getNotes(noteHashes: List<Int>) = flow<Note> {
        if (noteHashes.isEmpty()) {
            throw IllegalArgumentException("An empty noteHashes list is set")
        }

        val selection = "${NotesContract.COLUMN_NAME_HASHCODE} IN " +
                "(${noteHashes.joinToString(",") { "?" }})"
        val selectionArgs = noteHashes.map { it.toString() }.toTypedArray()

        val cursor = db?.query(
            NotesContract.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        while (cursor?.moveToNext() == true) {
            val getContent = cursor.getString(
                cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_CONTENT)
            )

            val type = object : TypeToken<List<MutableMap<String, String>>>() {}.type
            val convertedContent: List<MutableMap<String, String>> = Gson().fromJson(getContent, type)

            emit(
                Note(
                    id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_SERVER_ID)
                    ),
                    content = convertedContent,
                    noteHash = cursor.getInt(
                        cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_HASHCODE)
                    )
                )
            )
            Log.d("SQLite return", cursor.getInt(
                cursor.getColumnIndexOrThrow(NotesContract.COLUMN_NAME_HASHCODE)
            ).toString())
        }
        cursor?.close()
    }

    override fun deleteNote(noteHash: Int) {
        val selection = "${NotesContract.COLUMN_NAME_HASHCODE} = ?"
        val selectionArgs = arrayOf(noteHash.toString())
        db?.delete(NotesContract.TABLE_NAME, selection, selectionArgs)
    }

    override fun changeNote(noteHash: Int, saveObject: Note) {
        val selection = "${NotesContract.COLUMN_NAME_HASHCODE} = ?"
        val selectionArgs = arrayOf(noteHash.toString())

        val noteContent = Gson().toJson(saveObject.content)
        val values = ContentValues().apply {
            put(NotesContract.COLUMN_NAME_SERVER_ID, saveObject.id)
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
