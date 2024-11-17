package com.example.domain.errors

import com.example.domain.util.Error

enum class PhoneNumberErrors:Error {
    IS_EMPTY,
    NOT_MATCH_PATTERN,
}