package com.healthtracking.app.viewmodels.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.healthtracking.app.viewmodels.database.MealViewModel

class MealScreenViewModel(private val mealViewModel: MealViewModel): ViewModel() {
    val goalCalories get() = mealViewModel.goalCalories
    val goalProtein get() = mealViewModel.goalProtein
    val goalCarbohydrates get() = mealViewModel.goalCarbohydrates
    val goalFats get() = mealViewModel.goalFats

    private val _dialogProteinValue: MutableState<String?> = mutableStateOf(null)
    val dialogProteinValue: String? get() = _dialogProteinValue.value
    private val proteinCanError: MutableState<Boolean> = mutableStateOf(false)
    private val dialogProteinValueValid: Boolean get() = dialogProteinValue != null &&
            dialogProteinValue!!.toIntOrNull() != null &&
            dialogProteinValue!!.toInt() >= 0
    val proteinDialogValueValid: Boolean get() = dialogProteinValueValid || !proteinCanError.value

    private val _dialogCarbohydratesValue: MutableState<String?> = mutableStateOf(null)
    val dialogCarbohydratesValue: String? get() = _dialogCarbohydratesValue.value
    private val carbsCanError: MutableState<Boolean> = mutableStateOf(false)
    private val dialogCarbohydratesValueValid: Boolean get() = dialogCarbohydratesValue != null &&
            dialogCarbohydratesValue!!.toIntOrNull() != null &&
            dialogCarbohydratesValue!!.toInt() >= 0
    val carbsDialogValueValid: Boolean get() = dialogCarbohydratesValueValid || !carbsCanError.value

    private val _dialogFatsValue: MutableState<String?> = mutableStateOf(null)
    val dialogFatsValue: String? get() = _dialogFatsValue.value
    private val fatsCanError: MutableState<Boolean> = mutableStateOf(false)
    private val dialogFatsValueValid: Boolean get() = dialogFatsValue != null &&
                dialogFatsValue!!.toIntOrNull() != null &&
                dialogFatsValue!!.toInt() >= 0
    val fatDialogValueValid: Boolean get() = dialogFatsValueValid || !fatsCanError.value

    /**
     * Populate the dialog box with the current goals
     */
    fun populateDialogEntries() {
        _dialogProteinValue.value = goalProtein.toString()
        _dialogCarbohydratesValue.value = goalCarbohydrates.toString()
        _dialogFatsValue.value = goalFats.toString()
    }

    /**
     * Calculate the calories goal based on the macros
     */
    fun getCalorieGoal(): String {
        return if (dialogProteinValue?.toIntOrNull() != null &&
            dialogCarbohydratesValue?.toIntOrNull() != null &&
            dialogFatsValue?.toIntOrNull() != null)
        {
            (dialogProteinValue!!.toInt() * 4 + dialogCarbohydratesValue!!.toInt() * 4 +
                    dialogFatsValue!!.toInt() * 9).toString()
        } else {
            "error"
        }
    }

    /**
     * Update the string value of the dialog for protein
     */
    fun updateDialogProtein(newProteinValue: String?) {
        _dialogProteinValue.value = newProteinValue

        proteinCanError.value = true
    }

    /**
     * Update the string value of the dialog for carbs
     */
    fun updateDialogCarbohydrates(newCarbohydratesValue: String?) {
        _dialogCarbohydratesValue.value = newCarbohydratesValue

        carbsCanError.value = true
    }

    /**
     * Update the string value of the dialog for fats
     */
    fun updateDialogFats(newFatsValue: String?) {
        _dialogFatsValue.value = newFatsValue

        fatsCanError.value = true
    }

    /**
     * Updates the goals of the user's calories and macros to what is in the dialog if valid
     */
    fun updateGoals(): Boolean {
        if (dialogProteinValueValid  && dialogCarbohydratesValueValid && dialogFatsValueValid) {
            mealViewModel.updateProteinGoal(dialogProteinValue!!.toInt())
            mealViewModel.updateCarbohydratesGoal(dialogCarbohydratesValue!!.toInt())
            mealViewModel.updateFatsGoal(dialogFatsValue!!.toInt())

            return true
        }

        proteinCanError.value = true
        carbsCanError.value = true
        fatsCanError.value = true

        return false
    }

    /**
     * Clears the dialog box
     */
    fun clearDialog() {
        proteinCanError.value = false
        carbsCanError.value = false
        fatsCanError.value = false
    }
}