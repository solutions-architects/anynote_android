package com.luckhost.data.localStorage.materials.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${NotesContract.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${NotesContract.COLUMN_NAME_SERVER_ID} TEXT," +
            "${NotesContract.COLUMN_NAME_CONTENT} TEXT," +
            "${NotesContract.COLUMN_NAME_HASHCODE} TEXT)"

private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${NotesContract.TABLE_NAME}"

internal class NotesDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "NotesBase.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}