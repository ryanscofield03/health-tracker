package com.healthtracking.app.services

import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun calculateTimeSlept(
    startTime: LocalTime,
    endTime: LocalTime
): Int {
    val until = startTime.until(endTime, ChronoUnit.HOURS).toInt()
    val hoursSlept = if (until < 0) {
        until + 24
    } else {
        until
    }

    return hoursSlept
}