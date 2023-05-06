package com.example.simplemedicplan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextFieldValueWrapper(
    val value: String = "",
    val errorId: Int? = null
) : Parcelable
