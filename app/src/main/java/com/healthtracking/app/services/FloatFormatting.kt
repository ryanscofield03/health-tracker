package com.healthtracking.app.services

fun Float.toDecimalPoints(decimalPoints: Int): Float {
    return "%.${decimalPoints}f".format(this).toFloat()
}