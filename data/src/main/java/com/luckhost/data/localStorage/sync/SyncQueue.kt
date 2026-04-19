package com.luckhost.data.localStorage.sync

import com.luckhost.data.localStorage.models.PendingOperation

interface SyncQueue {
    fun add(operation: PendingOperation)
    fun remove(noteHash: String)
    fun getAll(): List<PendingOperation>
}
