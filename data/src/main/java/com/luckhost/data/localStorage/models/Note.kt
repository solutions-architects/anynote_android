package com.luckhost.data.localStorage.models

class Note(
    var id: Int?,
    var content: List<MutableMap<String, String>>,
    val noteHash: Int,
) {

}