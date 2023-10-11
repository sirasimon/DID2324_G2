package it.polito.did

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    private val _counter= MutableLiveData<Int>().also { it.value=0 }
    fun increment(){
        _counter.value = ( _counter.value ?: 0) +1;
    }
    val counter: LiveData<Int> = _counter;

}