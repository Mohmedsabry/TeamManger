package com.example.domain.util

sealed interface Result<D, E : Error> {
    data class Success<D, E : Error>(val data: D) : Result<D, E>
    data class Failure<D, E : Error>(val error: E, val optional: D? = null) : Result<D, E>
}