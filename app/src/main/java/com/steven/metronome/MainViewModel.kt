package com.steven.metronome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): AndroidViewModel(application) {

    val _tempo = MutableLiveData<Int>()
    val tempo: LiveData<Int>
        get() = _tempo

    val _enabled = MutableLiveData<Boolean>()
    val enabled:  LiveData<Boolean>
        get() = _enabled

    init {
        _tempo.value = 120
        _enabled.value = false
    }

    fun setTempo(value: Int?) {
        _tempo.value = value
    }

    fun increaseTempo() {
        _tempo.value = _tempo.value?.plus(1)
    }

    fun decreaseTempo() {
        _tempo.value = _tempo.value?.minus(1)
    }

    fun toggleEnabled() {
        _enabled.value = !_enabled.value!!
    }
}