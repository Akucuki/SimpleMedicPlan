package com.example.simplemedicplan.feature.home.pills

import com.example.simplemedicplan.model.home.PillDosageType
import java.time.LocalDateTime

sealed class PillsEvents {
    data class NavigateToEditPill(
        val pillDescriptionUuid: String? = null
    ) : PillsEvents()

    data class RegisterReminder(
        val date: LocalDateTime,
        val notificationTag: String,
        val pillName: String,
        val pillDosage: Float,
        val pillDosageType: PillDosageType
    ) : PillsEvents()

    data class DismissReminder(
        val date: LocalDateTime,
        val notificationTag: String,
        val pillName: String,
        val pillDosage: Float,
        val pillDosageType: PillDosageType
    ) : PillsEvents()
}
