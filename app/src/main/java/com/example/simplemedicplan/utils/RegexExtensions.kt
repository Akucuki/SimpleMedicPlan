package com.example.simplemedicplan.utils

inline val CharSequence?.isEmail: Boolean get() = isMatch(REGEX_EMAIL)

inline val CharSequence?.isPassword: Boolean get() = isMatch(REGEX_PASSWORD)

fun CharSequence?.isMatch(regex: String): Boolean {
    return !this.isNullOrEmpty() && Regex(regex).matches(this)
}

const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"
const val REGEX_PASSWORD = "^" +
        "(?=.*[0-9])" + // at least 1 digit
        "(?=.*[a-zA-Z])" + // any letter
        "(?=\\S+$)" + // no white spaces
        ".{8,}" + // at least 8 characters
        "$"
