package com.luckhost.domain.models

import java.util.Date

class NoteModel(
    var header: String,
    var content: String,
    var deadLine: Date,
    var coordinateX: Int,
    var coordinateY: Int,
    var hashCode: Int?,
) {
}