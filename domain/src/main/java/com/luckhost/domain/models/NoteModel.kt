package com.luckhost.domain.models

import java.util.Date

class NoteModel(
    var header: String,
    val content: String,
    val deadLine: Date,
    val coordinateX: Int,
    val coordinateY: Int,
    val hashCode: Int?,
) {
}