package com.luckhost.data.repository

import com.luckhost.data.localStorage.settings.NotesLayoutStorage
import com.luckhost.domain.repository.NotesLayoutRepoInterface

class NotesLayoutRepoImpl(
    private val storage: NotesLayoutStorage
) : NotesLayoutRepoInterface {

    override fun getColumnsCount(): Int = storage.getColumns()

    override fun setColumnsCount(count: Int) {
        storage.saveColumns(count)
    }
}
