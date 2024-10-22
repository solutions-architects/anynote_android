package com.luckhost.data.localStorage.models

import com.google.gson.Gson
import java.security.MessageDigest
/**
 * The Note model only for Data module
 * Can`t be an internal class due to Dagger
 *
 * Created for abstraction
 *
 * @param serverId is needed to perform CRUD operations on the server by API.
 * Can be nullable, which means that this note is not yet on the server
 * @param content the main part that contains media, markdown text etc.
 * @param contentHash is needed to compare notes and check new notes on the server.
 * Can`t be an integer value because of SHA256 usage
 * @param noteHash is needed to provide local CRUD operations
 */
data class Note(
    var serverId: Int?,
    var content: List<MutableMap<String, String>>,
    var contentHash: String = "",
    val noteHash: String
) {
    init {
        if (contentHash == "") {
            contentHash = generateContentHashCode()
        }
    }

    // SHA256 noteHash generation
    private fun generateContentHashCode(): String {
        val noteContentString = Gson().toJson(content)

        val bytes = noteContentString.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}