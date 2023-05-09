package com.example.simplemedicplan.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.NavDirection

enum class HomeNavigationBarItem(
    @DrawableRes
    val iconId: Int,
    @StringRes
    val labelId: Int,
    val navDirection: NavDirection
) {
    MEDIC_CARD(
        iconId = R.drawable.ic_medication,
        labelId = R.string.medic_card,
        navDirection = NavDirection.MEDIC_CARD
    ),
    PILLS(
        iconId = R.drawable.ic_medical_information,
        labelId = R.string.pills,
        navDirection = NavDirection.PILLS
    ),
    PROFILE(
        iconId = R.drawable.ic_account_box,
        labelId = R.string.profile,
        navDirection = NavDirection.PROFILE
    )
}
