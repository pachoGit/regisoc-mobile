package com.pacho.regisoc.ui.common

import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Long.toDateString(): String {
    return try {
        val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
        val day = localDateTime.day.toString().padStart(2, '0')
        val month = localDateTime.month.number.toString().padStart(2, '0')
        val year = localDateTime.year
        "$year-$month-$day"
    } catch (e: Exception) {
        ""
    }
}
