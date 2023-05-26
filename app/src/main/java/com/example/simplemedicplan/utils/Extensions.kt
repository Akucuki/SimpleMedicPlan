package com.example.simplemedicplan.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

private val currentZoneOffset = ZoneId.systemDefault().rules.getOffset(Instant.now())

fun String.decodeToLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.parse(this), currentZoneOffset)
}

fun LocalDateTime.encodeToString(): String {
    return toInstant(currentZoneOffset).toString()
}

fun Collection<String>.decodeToLocalDateTimeCollection(): Collection<LocalDateTime> {
    return this.map { stringEncodedDate ->
        stringEncodedDate.decodeToLocalDateTime()
    }
}

fun Collection<LocalDateTime>.encodeToStringsCollection(): Collection<String> {
    return this.map { localDateTime ->
        localDateTime.encodeToString()
    }
}
