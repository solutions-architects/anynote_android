package com.luckhost.data.storage.models

import java.util.Date
import java.util.Dictionary

class Note(
    var content: List<MutableMap<String, String>>,
    val noteHash: Int,
) {

}