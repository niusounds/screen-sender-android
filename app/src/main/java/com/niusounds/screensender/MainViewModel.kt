package com.niusounds.screensender

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val context: Context get() = getApplication()
    private val serviceRunning: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isServiceRunning: LiveData<Boolean> get() = serviceRunning

    private val ipAddressInputMutable: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val ipAddressInput: LiveData<String> get() = ipAddressInputMutable

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    init {
        ipAddressInputMutable.value = prefs.getString("addr", "")
    }

    fun clickFAB() {
        val ipAddress = ipAddressInputMutable.value ?: return

        // Save remote address
        prefs.edit {
            putString("addr", ipAddress)
        }
        val serviceIsRunning = serviceRunning.value == true

        if (serviceIsRunning) {
            ScreenSenderService.stop(context)
        } else {
            ScreenSenderService.start(context, ipAddress)
        }

        serviceRunning.postValue(!serviceIsRunning)
    }

    fun ipAddressInput(ipAddressInput: String) {
        if (ipAddressInputMutable.value != ipAddressInput) {
            ipAddressInputMutable.value = ipAddressInput
        }
    }
}