package com.example.domain.errors

import com.example.domain.util.Error

enum class EmailErrors:Error {
    USER_IS_EXIST,
    NOT_MATCH_PATTERN,
    IS_EMPTY,
}