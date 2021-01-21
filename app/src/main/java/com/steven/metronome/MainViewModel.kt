package com.steven.metronome

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.Duration
import java.time.LocalTime

class MainViewModel(application: Application): AndroidViewModel(application) {

    val _tempo = MutableLiveData<Int>()
    val tempo: LiveData<Int>
        get() = _tempo

    val _enabled = MutableLiveData<Boolean>()
    val enabled:  LiveData<Boolean>
        get() = _enabled

    val _tapStart = MutableLiveData<LocalTime>()
    val tapStart: LiveData<LocalTime>
        get() = _tapStart

    init {
        _tempo.value = 120
        _enabled.value = false
        _tapStart.value = null
    }

    /*
     * Tap tempo algorith:
     * Duration in ms between 2 taps
     * 60_000 ms / Duration
     */
    fun tapTempo() {
        if (_tapStart.value != null) {
            val durationMillis = Duration.between(_tapStart.value, LocalTime.now()).toMillis()
            if (durationMillis < 1000*120) { // only if the tap are within 2 minutes of eachother, should probably adjust thig
                val newTempo = 60_000 / durationMillis
                setTempo(newTempo.toInt())
            }
        }

        _tapStart.value = LocalTime.now()
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