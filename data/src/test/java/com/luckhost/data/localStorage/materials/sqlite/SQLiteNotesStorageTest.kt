package com.luckhost.data.localStorage.materials.sqlite

import com.luckhost.data.localStorage.models.Note
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SQLiteNotesStorageTest {
    @Test
    fun `should save simple note`() = runBlocking {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SQLiteNotesStorage(context)

        val note = Note(
            serverId = 1,
            content = listOf(mutableMapOf("key" to "value")),
            noteHash = "hash123"
        )

        storage.saveNote(note)
        val savedNote = storage.getNotes().first()
        assertEquals(note, savedNote)
    }


    @Test
    fun `should return a note with null in serverId`() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SQLiteNotesStorage(context)

        val expectedNotes = listOf<Note>(
            Note(
                serverId = null,
                content = listOf(mutableMapOf("key" to "value")),
                noteHash = "hash123"
            ),
        )

        expectedNotes.forEach {
            storage.saveNote(it)
        }

        var actualNotes: List<Note>

        runBlocking {
            actualNotes = storage.getNotes().toList()
        }

        assertEquals(expectedNotes, actualNotes)
    }

    @Test
    fun deleteNote() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SQLiteNotesStorage(context)

        val savedNotes = listOf<Note>(
            Note(
                serverId = 1,
                content = listOf(mutableMapOf("key" to "value")),
                noteHash = "hash123"
            )
        )

        savedNotes.forEach {
            storage.saveNote(it)
        }
        savedNotes.forEach {
            storage.deleteNote(it.noteHash)
        }

        val expectedNotes = listOf<Note>()
        var actualNotes: List<Note>

        runBlocking {
            actualNotes = storage.getNotes().toList()
        }

        assertEquals(expectedNotes, actualNotes)
    }

    @Test
    fun changeNote() {
        val context = RuntimeEnvironment.getApplication().applicationContext
        val storage = SQLiteNotesStorage(context)

        val savedNotes = listOf<Note>(
            Note(
                serverId = null,
                content = listOf(mutableMapOf("key" to "value")),
                noteHash = "hash123"
            )
        )

        savedNotes.forEach {
            storage.saveNote(it)
        }

        val expectedNotes = listOf<Note>(
            Note(
                serverId = 1,
                content = listOf(mutableMapOf("key" to "value", "md" to "text")),
                noteHash = "hash123"
            )
        )

        expectedNotes.forEach {
            storage.changeNote(it.noteHash, it)
        }

        var actualNotes: List<Note>
        runBlocking {
            actualNotes = storage.getNotes().toList()
        }

        assertEquals(expectedNotes, actualNotes)
    }
}
