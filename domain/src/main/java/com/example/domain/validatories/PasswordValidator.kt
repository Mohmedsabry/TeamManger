package com.example.domain.validatories

import com.example.domain.errors.PasswordErrors
import com.example.domain.util.Result

object PasswordValidator {
    operator fun invoke(password: String): Result<Unit, PasswordErrors> {
        if (password.isEmpty()) return Result.Failure(PasswordErrors.IS_EMPTY)
        if (password.length < 8) return Result.Failure(PasswordErrors.LENGTH)
        if (!password.any { it.isUpperCase() }) return Result.Failure(PasswordErrors.NOT_HAVE_UPPERCASE)
        if (!password.any { it.isDigit() }) return Result.Failure(PasswordErrors.NOT_HAVE_DIGIT)
        return Result.Success(Unit)
    }
}