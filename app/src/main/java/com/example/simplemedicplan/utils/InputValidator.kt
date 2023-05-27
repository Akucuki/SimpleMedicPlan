package com.example.simplemedicplan.utils

import com.example.simplemedicplan.R

object InputValidator {

    fun getEmailErrorIdOrNull(input: String): Int? = when {
        input.isEmpty() -> R.string.error_email_is_empty
        input.isEmail -> null
        else -> R.string.error_email_is_invalid
    }

    fun getPasswordErrorIdOrNull(input: String): Int? = when {
        input.isEmpty() -> R.string.error_password_is_empty
        input.isPassword -> null
        else -> R.string.error_password_is_invalid
    }

    fun getPasswordMismatchErrorIdOrNull(password: String, repeatedPassword: String): Int? {
        return when (password) {
            repeatedPassword -> null
            else -> R.string.error_passwords_do_not_match
        }
    }

    fun getMedicineNameErrorIdOrNull(name: String): Int? = when {
        name.isEmpty() -> R.string.error_name_cannot_be_empty
        else -> null
    }
}