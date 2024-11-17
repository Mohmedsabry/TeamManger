package com.example.teammanger.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDate(): String {
    val simpleDateFormat = SimpleDateFormat(
        "hh:mm MMM dd,yyyy",
        Locale.UK
    )
    return simpleDateFormat.format(Date(this))
}