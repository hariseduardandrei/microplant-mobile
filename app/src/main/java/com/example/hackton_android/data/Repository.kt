package com.example.hackton_android.data

import com.example.hackton_android.model.LightsModel
import com.example.hackton_android.model.SelectedInformation
import com.example.hackton_android.model.TelemetryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import ro.esolutions.selfregistration.data.ResponseStatus
import ro.esolutions.selfregistration.data.apiService

class Repository : RepositoryInterface {

    private fun <T> getResponseCall(response: Response<T>): ResponseStatus<T> {
        if (response.isSuccessful) {
            response.body()?.let {
                return ResponseStatus.Success(it)
            }
            return ResponseStatus.None
        } else {
            return ResponseStatus.Error(
                code = response.code()
            )
        }
    }

    override suspend fun getTelemetry(): ResponseStatus<TelemetryModel> {
        var response: Response<TelemetryModel>

        withContext(Dispatchers.IO) {
            response = apiService.getTelemetry()
        }
        return getResponseCall(response)
    }

    override suspend fun saveConfig(selectedInformation: SelectedInformation): ResponseStatus<*> {
        var response: Response<*>
        withContext(Dispatchers.IO) {
            response = apiService.saveConfig(selectedInformation)
        }

        return getResponseCall(response)
    }

    override suspend fun pumpDemo(): ResponseStatus<*> {
        var response: Response<*>
        withContext(Dispatchers.IO) {
            response = apiService.pumpDemo()
        }

        return getResponseCall(response)
    }

    override suspend fun startLights(lightsModel: LightsModel): ResponseStatus<*> {
        var response: Response<*>
        withContext(Dispatchers.IO) {
            response = apiService.startLights(lightsModel)
        }

        return getResponseCall(response)
    }
}