package com.example.hackton_android.model

class SelectedInformation(
    var team: String? = "", var lightStartHour: Int? = null, var lightStartMinute: Int? = null,
    var lightEndHour: Int? = null, var lightEndMinute: Int? = null,
    var containerVolume: Int? = null
) {


    fun areInvalidInformation(): Boolean {
        return team?.trim().isNullOrEmpty()
                && lightStartHour != null
                && lightStartMinute != null
                && lightEndHour != null
                && lightEndMinute != null
                && containerVolume != null
    }
}