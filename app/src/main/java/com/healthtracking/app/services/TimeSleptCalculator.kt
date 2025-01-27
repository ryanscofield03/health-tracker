package com.healthtracking.app.services

import java.text.DecimalFormat
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * Used to calculate the number of hours slept as a String to 1 dp
 */
fun calculateTimeSlept(
    startTime: LocalTime,
    endTime: LocalTime
): String {
    val minutes = startTime.until(endTime, ChronoUnit.MINUTES).toFloat()

    val minutesSlept = if (minutes < 0) {
        minutes + (24 * 60)
    } else {
        minutes
    }

    val hoursSlept: Float = minutesSlept / 60
    return DecimalFormat("#.#").format(hoursSlept)
}