package com.luckhost.data.localStorage.settings

import android.content.Context
import androidx.core.content.edit

class NotesLayoutStorage(context: Context) {

    private val prefs = context.getSharedPreferences(
        "notes_layout",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_COLUMNS = "columns_count"
    }

    fun getColumns(): Int {
        return prefs.getInt(KEY_COLUMNS, 3)
    }

    fun saveColumns(count: Int) {
        prefs.edit {
            putInt(KEY_COLUMNS, count)
        }
    }
}
