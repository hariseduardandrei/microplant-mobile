package com.example.hackton_android.model

import java.math.BigDecimal

class TelemetryModel(
    val temperature: BigDecimal,
    val airHumidity: BigDecimal,
    val soilHumidity: BigDecimal,
    val containerVolume: BigDecimal
)
