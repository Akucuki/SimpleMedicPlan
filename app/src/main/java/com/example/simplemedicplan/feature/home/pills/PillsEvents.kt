package com.example.simplemedicplan.feature.home.pills

sealed class PillsEvents {
    data class NavigateToEditPill(
        val pillDescriptionUuid: String? = null
    ) : PillsEvents()
}
