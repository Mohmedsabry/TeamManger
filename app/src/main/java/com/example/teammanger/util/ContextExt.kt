package com.example.teammanger.util

import android.content.Context

fun Context.saveMember(
    email: String
) {
    getSharedPreferences("login", Context.MODE_PRIVATE).edit()
        .putString("email", email)
        .putBoolean("login", true)
        .apply()
}

fun Context.getMemberEmail(): String? {
    val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
    return sharedPreferences.getString("email", null)
}

fun Context.removeMember() {
    getSharedPreferences("login", Context.MODE_PRIVATE).edit()
        .remove("email")
        .putBoolean("login", false)
        .apply()
}

fun Context.onBoardingDone() {
    getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        .edit()
        .putBoolean("state", true)
        .apply()
}

fun Context.getOnBoardingStatue(): Boolean {
    return getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        .getBoolean("state", false)
}