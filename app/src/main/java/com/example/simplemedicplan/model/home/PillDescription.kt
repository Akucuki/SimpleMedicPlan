package com.example.simplemedicplan.model.home

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.simplemedicplan.R
import kotlinx.parcelize.Parcelize
import java.util.UUID

//enum class PillFormType(@StringRes val labelId: Int) {
//    TABLETS(labelId = R.string.tablets),
//    CAPSULES(labelId = R.string.capsules),
//    SYRUP(labelId = R.string.syrup),
//    SPRAY(labelId = R.string.spray),
//    OINTMENT(labelId = R.string.ointment),
//    GEL(labelId = R.string.gel),
//    CREAM(labelId = R.string.cream),
//    INJECTION_SOLUTION(labelId = R.string.injection_solution),
//    SUPPOSITORIES(labelId = R.string.suppositories),
//    POWDER(labelId = R.string.powder),
//    DROPS(labelId = R.string.drops),
//    PATCH(labelId = R.string.patch),
//}

enum class PillDosageType(@StringRes val labelId: Int) {
    MILLIGRAMS(labelId = R.string.milligrams),
    MILLILITERS(labelId = R.string.milliliters),
    TABLETS(labelId = R.string.tablets),
    CAPSULES(labelId = R.string.capsules),
    DROPS(labelId = R.string.drops),
    PUFFS(labelId = R.string.puffs),
    GRAMS(labelId = R.string.grams),
    TEASPOONS(labelId = R.string.teaspoons),
    TABLESPOONS(labelId = R.string.tablespoons),
    PIECES(labelId = R.string.pieces),
}

@Parcelize
data class PillDescription(
    val uuid: String = UUID.randomUUID().toString(),
    val name: String = "",
//    val formType: PillFormType,
    val dosageType: PillDosageType = PillDosageType.MILLIGRAMS,
    val dosage: Float = .0f,
//    val endDate: Long, // TODO it should be LocalDataTime instead of a Long
    val notes: String = "",
    val remaindersDates: List<String> = emptyList()
) : Parcelable {

    fun toUI() = PillDescriptionUI(
        uuid = uuid,
        name = name,
//        formType = formType,
        dosageType = dosageType,
        dosage = dosage,
//        endDate = dateFormatter.format(LocalDate.ofEpochDay(endDate)),
        notes = notes,
        remaindersDates = remaindersDates,
    )

    companion object {
        fun getNotificationId(pillUuid: String, encodedDate: String) = "$pillUuid-$encodedDate"

        fun composeNotificationText(
            context: Context,
            pillName: String,
            pillDosage: Float,
            pillDosageType: PillDosageType
        ): String {
            val pillDosageTypeName = context.getString(pillDosageType.labelId)
            return context.getString(R.string.notification_text, pillName, pillDosage, pillDosageTypeName)
        }

    }
}

@Parcelize
data class PillDescriptionUI(
    val uuid: String,
    val name: String,
//    val formType: PillFormType,
    val dosageType: PillDosageType,
    val dosage: Float,
//    val endDate: String,
    val notes: String = "",
    val remaindersDates: List<String> = emptyList(),
    val isExpanded: Boolean = false,
) : Parcelable

fun List<PillDescription>.toUI(): List<PillDescriptionUI> = map { it.toUI() }
