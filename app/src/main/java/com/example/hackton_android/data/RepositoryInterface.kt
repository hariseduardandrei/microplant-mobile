package com.example.hackton_android.data

import com.example.hackton_android.model.LightsModel
import com.example.hackton_android.model.SelectedInformation
import com.example.hackton_android.model.TelemetryModel
import ro.esolutions.selfregistration.data.ResponseStatus

interface RepositoryInterface {

    suspend fun getTelemetry(): ResponseStatus<TelemetryModel>

    suspend fun saveConfig(selectedInformation: SelectedInformation): ResponseStatus<*>

    suspend fun pumpDemo(): ResponseStatus<*>

    suspend fun startLights(lightsModel: LightsModel): ResponseStatus<*>
}