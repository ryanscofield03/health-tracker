package com.healthtracking.app.services

import java.text.DecimalFormat

fun Float.toDecimalPoints(decimalPoints: Int): Float {
    return "%.${decimalPoints}f".format(this).toFloat()
}

fun Float.toStringWithDecimalPoints(): String {
    return if (this % 1.0 == 0.0) {
        DecimalFormat("#").format(this)
    } else if (this * 10 % 1.0 == 0.0) {
        DecimalFormat("#.#").format(this)
    } else {
        DecimalFormat("#.##").format(this)
    }
}