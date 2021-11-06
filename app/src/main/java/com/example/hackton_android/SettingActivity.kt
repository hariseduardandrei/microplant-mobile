package com.example.hackton_android

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import com.example.hackton_android.databinding.ActivitySettingBinding
import com.example.hackton_android.util.*
import ro.esolutions.selfregistration.data.ResponseStatus

class SettingActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivitySettingBinding
    private lateinit var teamAdapter: AutocompleteDropDownAdapter
    private val dataViewModel: DataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting)

        val lightStartHour = SharedPreferenceUtils.getInt(LIGHT_START_HOUR)
        val lightStartMinute = SharedPreferenceUtils.getInt(LIGHT_START_MINUTE)

        val lightEndHour = SharedPreferenceUtils.getInt(LIGHT_END_HOUR)
        val lightEndMinute = SharedPreferenceUtils.getInt(LIGHT_END_MINUTE)

        val containerVolume = SharedPreferenceUtils.getInt(CONTAINER_VOLUME)

        if (SharedPreferenceUtils.getString(TEAM).isNotEmpty()) {
            dataViewModel.selectedInformation.team = SharedPreferenceUtils.getString(TEAM)
            dataBinding.sTeams.setText(SharedPreferenceUtils.getString(TEAM))
            dataBinding.notifyChange()
        }

        if (lightStartHour != null && lightStartMinute != null && lightStartHour != -1 && lightStartMinute != -1) {
            dataViewModel.selectedInformation.lightStartHour = lightStartHour
            dataViewModel.selectedInformation.lightStartMinute = lightStartMinute
            dataBinding.tvLightStart.text =
                "Light start  ${getFormattedTime(lightStartHour, lightStartMinute)}"
            dataBinding.notifyChange()
        }

        if (lightEndHour != null && lightEndMinute != null && lightEndHour != -1 && lightEndMinute != -1) {
            dataViewModel.selectedInformation.lightEndHour = lightEndHour
            dataViewModel.selectedInformation.lightEndMinute = lightEndMinute
            dataBinding.tvLightEnd.text =
                "Light end  ${getFormattedTime(lightEndHour, lightEndMinute)}"
            dataBinding.notifyChange()
        }

        if (containerVolume != null && containerVolume != -1) {
            dataViewModel.selectedInformation.containerVolume = containerVolume
            dataBinding.etContainerVolume.setText("" + containerVolume)
            dataBinding.notifyChange()
        }


        teamAdapter =
            AutocompleteDropDownAdapter(
                baseContext,
                ::callbackGetTeams,
                emptyList<String>().toMutableList()
            )
        dataBinding.sTeams.setAdapter(teamAdapter)

        attachTeamObserver()


        dataBinding.tvLightEnd.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                getTimePickerDialogListener(dataBinding.tvLightEnd, "Light end"),
                12,
                10,
                true
            )


            timePicker.show()
        }

        dataBinding.tvLightStart.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                getTimePickerDialogListener(dataBinding.tvLightStart, "Light start"),
                12,
                10,
                true
            )
            timePicker.show()
        }

        dataBinding.btnSave.setOnClickListener {
            if (!dataViewModel.selectedInformation.areInvalidInformation()) {
                SharedPreferenceUtils.save(TEAM, dataViewModel.selectedInformation.team.toString())

                SharedPreferenceUtils.save(
                    LIGHT_START_HOUR,
                    dataViewModel.selectedInformation.lightStartHour
                )
                SharedPreferenceUtils.save(
                    LIGHT_START_MINUTE,
                    dataViewModel.selectedInformation.lightStartMinute
                )

                SharedPreferenceUtils.save(
                    LIGHT_END_HOUR,
                    dataViewModel.selectedInformation.lightEndHour
                )
                SharedPreferenceUtils.save(
                    LIGHT_END_MINUTE,
                    dataViewModel.selectedInformation.lightEndMinute
                )

                SharedPreferenceUtils.save(
                    CONTAINER_VOLUME,
                    dataViewModel.selectedInformation.containerVolume
                )

                dataViewModel.saveConfig()
            }
        }

        dataBinding.sTeams.onItemClickListener = AutocompleteListener()

        dataBinding.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Log.i(
                    "[ASDASDASDASDASDASDASDASDASDASDASD]",
                    "${dataViewModel.selectedInformation.areInvalidInformation()}"
                )
                if (!dataViewModel.selectedInformation.areInvalidInformation()) {
                    enabledBtnNext()
                } else {
                    disabledBtnNext()
                }
            }
        })

        dataBinding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        dataBinding.etContainerVolume.inputType = InputType.TYPE_CLASS_NUMBER
        dataBinding.etContainerVolume.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if(s.trim().isNotEmpty()) {
                    dataViewModel.selectedInformation.containerVolume =
                        Integer.valueOf(s.trim().toString())
                    dataBinding.notifyChange()
                }
            }
        })


        attacheObserveLoading()

    }

    private fun attacheObserveLoading() {
        dataViewModel.loading.observe(this) {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }

    private fun enabledBtnNext() {
        dataBinding.btnSave.isEnabled = true
        dataBinding.btnSave.setBackgroundColor(
            ContextCompat.getColor(
                baseContext,
                R.color.forestgreen
            )
        )
        dataBinding.btnSave.setTextColor(ContextCompat.getColor(baseContext, R.color.white))
    }

    private fun disabledBtnNext() {
        dataBinding.btnSave.isEnabled = false
        dataBinding.btnSave.setBackgroundColor(
            ContextCompat.getColor(
                baseContext,
                R.color.disabled_bg_color_button
            )
        )
        dataBinding.btnSave.setTextColor(
            ContextCompat.getColor(
                baseContext,
                R.color.disabled_text_color_button
            )
        )
    }

    private fun getTimePickerDialogListener(
        textView: TextView,
        text: String
    ): TimePickerDialog.OnTimeSetListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> // logic to properly handle
            val formattedTime = getFormattedTime(hourOfDay, minute)
            textView.text = "$text $formattedTime"

            if (textView === dataBinding.tvLightEnd) {
                dataViewModel.selectedInformation.lightEndHour = hourOfDay
                dataViewModel.selectedInformation.lightEndMinute = minute
                dataBinding.notifyChange()
            }

            if (textView === dataBinding.tvLightStart) {
                dataViewModel.selectedInformation.lightStartHour = hourOfDay
                dataViewModel.selectedInformation.lightStartMinute = minute
                dataBinding.notifyChange()
            }

        }

    private fun getFormattedTime(hourOfDay: Int, minute: Int): String {
        return when {
            hourOfDay == 0 -> {
                if (minute < 10) {
                    "${hourOfDay}:0${minute}"
                } else {
                    "${hourOfDay}:${minute}"
                }
            }
            hourOfDay > 12 -> {
                if (minute < 10) {
                    "${hourOfDay}:0${minute}"
                } else {
                    "${hourOfDay}:${minute}"
                }
            }
            hourOfDay == 12 -> {
                if (minute < 10) {
                    "${hourOfDay}:0${minute}"
                } else {
                    "${hourOfDay}:${minute}"
                }
            }
            else -> {
                if (minute < 10) {
                    "${hourOfDay}:${minute}"
                } else {
                    "${hourOfDay}:${minute}"
                }
            }
        }
    }

    private fun callbackGetTeams(searchTerm: String) {
        dataViewModel.getDepartments(searchTerm)
    }

    private fun attachTeamObserver() {
        dataViewModel.teams.observe(this) {
            teamAdapter.clear()
            teamAdapter.addAll(it.orEmpty().toMutableList())
        }
    }

    inner class AutocompleteListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            dataViewModel.selectedInformation.team = dataViewModel.getTeamById(position)
            dataBinding.notifyChange()
        }
    }
}