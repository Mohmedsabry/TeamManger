package com.example.domain.errors

import com.example.domain.util.Error

enum class PasswordErrors : Error {
    IS_EMPTY,
    NOT_HAVE_DIGIT,
    NOT_HAVE_UPPERCASE,
    LENGTH
}