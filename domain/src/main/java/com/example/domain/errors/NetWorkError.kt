package com.example.domain.errors

import com.example.domain.util.Error

enum class NetWorkError : Error {
    INTERNAL_SERVER_ERROR,
    NO_INTERNET_CONNECTION,
    UNKnown,

}