package com.steven.metronome

import android.app.Application
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalTime

class MainViewModel(application: Application): AndroidViewModel(application) {

    private var soundPool: SoundPool? = SoundPool.Builder()
            .setAudioAttributes(
                    object: AudioAttributes.Builder() {}
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            )
            .setMaxStreams(1)
            .build()

    private var soundId = soundPool?.load(application.applicationContext, R.raw.hi_hat_closed_1, 0)

    private val _tempo = MutableLiveData<Int>()
    val tempo: LiveData<Int>
        get() = _tempo

    private var delay: Long = 0

    private val _enabled = MutableLiveData<Boolean>()
    val enabled:  LiveData<Boolean>
        get() = _enabled

    private val _tapStart = MutableLiveData<LocalTime>()
    val tapStart: LiveData<LocalTime>
        get() = _tapStart

    init {
        _tempo.value = 120
        _enabled.value = false
        _tapStart.value = null
        calculateDelay()
    }

    private fun calculateDelay() {
        val millies = 60 / tempo.value!!.toDouble()
        delay = (millies * 1000).toLong()
    }

    fun play() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playy()
            }
        }
    }

    private suspend fun playy() {
        while (true) {
            if (enabled.value == true) {
                soundPool?.play(
                        soundId!!,
                        1.0f,
                        1.0f,
                        0,
                        0,
                        1.0f)
                delay(delay)
            } else {
                delay(2500)
            }
        }
    }

    override fun onCleared() {
        soundPool = null
        super.onCleared()
    }

    /*
     * Tap tempo algorithm:
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
        calculateDelay()
    }

    fun increaseTempo() {
        setTempo(_tempo.value?.plus(1))
    }

    fun decreaseTempo() {
        setTempo(_tempo.value?.minus(1))
    }

    fun toggleEnabled() {
        _enabled.value = !_enabled.value!!
    }
}