package com.example.domain.validatories

import com.example.domain.errors.EmailErrors
import com.example.domain.util.Result

object EmailValidator {
    operator fun invoke(email: String, emails: List<String>): Result<Unit, EmailErrors> {
        if (email.isEmpty()) return Result.Failure(EmailErrors.IS_EMPTY)
        if (emails.contains(email)) return Result.Failure(EmailErrors.USER_IS_EXIST)
        val pattern = "^[a-zA-Z][a-zA-Z0-9.]*@gmail\\.com\$"
        if (!email.matches(pattern.toRegex())) return Result.Failure(EmailErrors.NOT_MATCH_PATTERN)
        return Result.Success(Unit)
    }
}