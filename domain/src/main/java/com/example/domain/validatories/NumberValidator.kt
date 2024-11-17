package com.example.domain.validatories

import com.example.domain.errors.PhoneNumberErrors
import com.example.domain.util.Result

object NumberValidator {
    operator fun invoke(number: String): Result<Unit, PhoneNumberErrors> {
        if (number.isEmpty()) return Result.Failure(PhoneNumberErrors.IS_EMPTY)
        val pattern = "01[0512][0-9]{8}"
        if (!number.matches(pattern.toRegex())) return Result.Failure(PhoneNumberErrors.NOT_MATCH_PATTERN)
        return Result.Success(Unit)
    }
}