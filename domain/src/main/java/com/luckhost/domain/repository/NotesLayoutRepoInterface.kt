package com.luckhost.domain.repository

interface NotesLayoutRepoInterface {
    fun getColumnsCount(): Int
    fun setColumnsCount(count: Int)
}
