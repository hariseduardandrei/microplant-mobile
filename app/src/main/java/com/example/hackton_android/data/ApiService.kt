package com.example.hackton_android.data

import com.example.hackton_android.model.LightsModel
import com.example.hackton_android.model.SelectedInformation
import com.example.hackton_android.model.TelemetryModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("/telemetry")
    suspend fun getTelemetry(): Response<TelemetryModel>

    @POST("/save_config")
    suspend fun saveConfig(@Body selectedInformation: SelectedInformation): Response<*>

    @POST("/pump_demo")
    suspend fun pumpDemo(): Response<*>

    @POST("/start_lights")
    suspend fun startLights(@Body lightsModel: LightsModel): Response<*>

}