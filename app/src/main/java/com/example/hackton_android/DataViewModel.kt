package com.example.hackton_android

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackton_android.model.GenericModel
import com.example.hackton_android.model.LightsModel
import com.example.hackton_android.model.SelectedInformation
import com.example.hackton_android.model.TelemetryModel
import kotlinx.coroutines.launch
import ro.esolutions.selfregistration.data.ResponseStatus
import ro.esolutions.selfregistration.data.repositoryInterface
import java.util.stream.Collectors
import java.util.stream.Stream


class DataViewModel : ViewModel() {

    private val TEAMS = listOf(
        "CFR CLUJ",
        "FCSB",
        "UNIVERSITATEA CRAIOVA",
        "FC BOTOSANI",
        "FC VOLUNTARI",
        "FC RAPID 1923",
        "UTA ARAD",
        "FARUL CONSTANTA",
        "FC ARGES",
        "AFC CHINDIA",
        "GAZ METAN",
        "SEPSI OSK",
        "U CRAIOVA 1948",
        "CS MIOVENI",
        "FC DINAMO 1948",
        "ACADEMICA CLINCENI"
    )

    protected var _loading: MutableLiveData<ResponseStatus<*>> = MutableLiveData()
    val loading: LiveData<ResponseStatus<*>> = _loading

    private val _telemetry: MutableLiveData<TelemetryModel> = MutableLiveData()
    val telemetry: LiveData<TelemetryModel> = _telemetry

    private val _teams: MutableLiveData<List<String>> = MutableLiveData()
    val teams: LiveData<List<String>> = _teams

    val selectedInformation: SelectedInformation = SelectedInformation()
    val lightsModel = LightsModel()

    fun getDepartments(searchTerm: String) {
        viewModelScope.launch {
            _teams.value = TEAMS.stream()
                .filter { it.lowercase().contains(searchTerm.lowercase()) }
                .collect(Collectors.toList())
        }
    }

    fun getTelemetry() {
        viewModelScope.launch {
            val response = repositoryInterface.getTelemetry()
            if (response is ResponseStatus.Success) {
                _telemetry.value = response.data
            }
        }

    }

    fun getTeamById(position: Int): String? {
        val value = teams.value.orEmpty()

        if (value.isNotEmpty()) {
            return value[position]
        }

        return null
    }

    fun saveConfig() {
        viewModelScope.launch {
            val response = repositoryInterface.saveConfig(selectedInformation)
            if (response is ResponseStatus.Success) {
                _loading.value = ResponseStatus.Success(response.data)
            }

            if (response is ResponseStatus.Error) {
                _loading.value = response
            }
        }
    }

    fun startGame() {
        viewModelScope.launch {
            repositoryInterface.startLights(lightsModel)
        }
    }

    fun pumpDemo() {
        viewModelScope.launch {
            repositoryInterface.pumpDemo()
        }
    }
}