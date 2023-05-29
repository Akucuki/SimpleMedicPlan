package com.example.simplemedicplan.feature.home.pills.edit

import com.example.simplemedicplan.model.home.PillDosageType
import java.time.LocalDateTime

sealed class PillEditEvents {
    object NavigateBack : PillEditEvents()
    data class DismissReminder(
        val date: LocalDateTime,
        val notificationTag: String,
        val pillName: String,
        val pillDosage: Float,
        val pillDosageType: PillDosageType
    ) : PillEditEvents()
}
