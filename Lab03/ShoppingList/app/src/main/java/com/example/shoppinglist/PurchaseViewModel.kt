package com.example.shoppinglist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PurchaseViewModel : ViewModel() {
    private val _mode = MutableLiveData<AppMode>().also { it.value=AppMode.SHOPPING }


    fun switchAppMode(){
        Log.d("AppMode", "changeAppMode > App mode is ${_mode.value}")

        when(_mode.value){
            AppMode.COMPOSING -> _mode.value = AppMode.SHOPPING
            AppMode.SHOPPING -> _mode.value = AppMode.COMPOSING
            else -> {
                Log.w("AppMode", "changeAppMode > else statement entered")
            }
        }

        Log.d("AppMode", "changeAppMode > New app mode is ${_mode.value}")
    }

    fun getAppMode() : AppMode{
        return _mode.value?:AppMode.COMPOSING
    }
}