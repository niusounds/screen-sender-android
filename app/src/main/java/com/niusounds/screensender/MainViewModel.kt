package com.niusounds.screensender

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context get() = getApplication()
    private val serviceRunning = MutableStateFlow(false)
    val isServiceRunning: Flow<Boolean> = serviceRunning

    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    private val ipAddressInputMutable = MutableStateFlow(
        value = prefs.getString("addr", "")!!
    )
    val ipAddressInput: Flow<String> = ipAddressInputMutable

    fun clickFAB() {
        val ipAddress = ipAddressInputMutable.value ?: return

        // Save remote address
        prefs.edit {
            putString("addr", ipAddress)
        }
        val serviceIsRunning = serviceRunning.value

        if (serviceIsRunning) {
            ScreenSenderService.stop(context)
        } else {
            ScreenSenderService.start(context, ipAddress)
        }

        serviceRunning.value = !serviceIsRunning
    }

    fun ipAddressInput(ipAddressInput: String) {
        if (ipAddressInputMutable.value != ipAddressInput) {
            ipAddressInputMutable.value = ipAddressInput
        }
    }
}