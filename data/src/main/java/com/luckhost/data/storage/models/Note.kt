package com.luckhost.data.storage.models

import java.util.Date
import java.util.Dictionary

class Note(
    var id: Int?,
    var content: List<MutableMap<String, String>>,
    val noteHash: Int,
) {

}