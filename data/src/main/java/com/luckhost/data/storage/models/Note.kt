package com.luckhost.data.storage.models

import java.util.Date

class Note(
    val header: String,
    val content: String,
    val deadLine: Date,
    val coordinateX: Int,
    val coordinateY: Int,
    val noteHash: Int,
) {

}