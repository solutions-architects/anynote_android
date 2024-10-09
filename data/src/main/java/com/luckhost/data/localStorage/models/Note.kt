package com.luckhost.data.localStorage.models

/**
 * The Note model only for Data module
 *
 * Created for abstraction
 *
 * @param serverId is needed to perform CRUD operations on the server by API
 * @param content the main part that contains media, markdown text etc.
 * @param noteHash is needed to compare notes and check new notes on the server
 */
data class Note(
    var serverId: Int?,
    var content: List<MutableMap<String, String>>,
    val noteHash: String,
)