package com.luckhost.data.localStorage.materials.sqlite

import android.provider.BaseColumns


object NotesContract : BaseColumns {
    const val TABLE_NAME = "notesDB"
    const val COLUMN_NAME_SERVER_ID = "id"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_HASHCODE = "hashCode"
}


