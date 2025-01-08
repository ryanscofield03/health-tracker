package nz.ac.canterbury.seng303.healthtracking.services

import android.content.Context

fun getExerciseList(context: Context, exerciseGroup: String): Array<String>? {
    val resourceName = exerciseGroup.lowercase() + "_exercises"
    val resourceId = context.resources.getIdentifier(resourceName, "array", context.packageName)

    if (resourceId == 0) {
        return null
    }
    return context.resources.getStringArray(resourceId)
}