package com.example.hackton_android

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.hackton_android.databinding.ActivityMainBinding
import com.example.hackton_android.util.SharedPreferenceUtils
import java.math.MathContext

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding
    private val dataViewModel: DataViewModel by viewModels()

    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPreferenceUtils.init(this)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        attacheObserverTelemetry()
        run()

        dataBinding.ivSettings.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        dataBinding.btnPumpDemo.setOnClickListener {
            dataViewModel.pumpDemo()
        }

        dataBinding.btnStartGame.setOnClickListener {
            showdialog()
        }
    }

    private fun showdialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Duration")

        val input = EditText(this)

        input.setHint("Enter game duration (min)")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)


        builder.setPositiveButton("OK") { dialog, which ->
            val durationValue = input.text.toString()
            dataViewModel.lightsModel.duration = Integer.valueOf(durationValue)
            dataViewModel.startGame()
        }
        builder.setNegativeButton(
            "Cancel",
            { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private fun attacheObserverTelemetry() {
        dataViewModel.telemetry.observe(this) {
            if (it != null) {
                val temperatureText = "Temperature\n${it.temperature}°C"
                val airHumidityText = "Air humidity\n${it.airHumidity}%"
                val soilHumidityText = "Soil Humidity\n${it.soilHumidity}%"
                val containerVolumeText = "Container Volume\n${it.containerVolume} ml"


                dataBinding.tvTemperature.text = temperatureText
                dataBinding.tvAirHumidity.text = airHumidityText
                dataBinding.tvSoilHumidity.text = soilHumidityText
                dataBinding.tvSoilHumidity.text = soilHumidityText
                dataBinding.tvContainerVolume.text = containerVolumeText

                dataBinding.progressTemperature.setProgress(
                    it.temperature.round(MathContext(1)).intValueExact(), true
                )
                dataBinding.progressAirHumidity.setProgress(
                    it.airHumidity.round(MathContext(1)).intValueExact(), true
                )
                dataBinding.progressSoilHumidity.setProgress(
                    it.soilHumidity.round(MathContext(1)).intValueExact(), true
                )
                dataBinding.progressContainerVolume.setProgress(
                    it.containerVolume.intValueExact(),
                    true
                )
            }
        }
    }

    private fun run() {
        handler.removeCallbacksAndMessages(null)
        val runnable = getTelemetryRunnable()
        handler.postDelayed(runnable, 0)
    }

    private fun getTelemetryRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                dataViewModel.getTelemetry()
                handler.postDelayed(this, 3000)

            }
        }
    }
}