package com.luckhost.data.localStorage.sync

import com.luckhost.data.localStorage.models.PendingOperation

class InMemorySyncQueue : SyncQueue {
    private val queue = mutableListOf<PendingOperation>()

    override fun add(operation: PendingOperation) {
        queue.removeAll { it.noteHash == operation.noteHash }
        queue.add(operation)
    }

    override fun remove(noteHash: String) {
        queue.removeAll { it.noteHash == noteHash }
    }

    override fun getAll(): List<PendingOperation> = queue.toList()
}
