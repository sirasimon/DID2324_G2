package it.polito.did

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountdownViewModel : ViewModel(){
    private val _timerValue = MutableLiveData<Long>().also { it.value=0 }
    private var _timer : CountDownTimer ?= null
    private var _isTimeout = MutableLiveData<Boolean>().also{it.value=false}

    val timerValue: LiveData<Long> = _timerValue;
    val isTimeout: LiveData<Boolean> = _isTimeout

    fun createTimer(timeout:Long){
        _timer = object : CountDownTimer(timeout, 1000){

            override fun onTick(remainingTime: Long) {
                if(_isTimeout.value==true)
                    _isTimeout.value = false

                Log.d("onTick", "Remaining time is $remainingTime")

                _timerValue.value = remainingTime

                Log.d("onTick", "Updated timer value is ${_timerValue.value}")
            }

            override fun onFinish() {
                _isTimeout.value = true;
            }
        }.start()
    }

    fun cancelTimer(){
        _timer?.cancel();
    }

    fun getTimerValue() : Long{
        return _timerValue.value ?: 0;
    }

    fun getTimerString() : String{
        val totalSeconds = _timerValue.value?.div(1000)
        val minutes = totalSeconds?.div(60)
        val hours = totalSeconds?.div(3600)

        return String.format("%02d:%02d:%02d", hours, minutes?.mod(60), totalSeconds?.mod(60))
    }

    fun isTimerNull() : Boolean{
        return _timer==null
    }
}