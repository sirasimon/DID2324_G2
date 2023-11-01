package com.example.shoppinglist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PurchaseViewModel : ViewModel() {
    private val _mode = MutableLiveData<AppMode>().also { it.value=AppMode.SHOPPING }   //TODO: silenziare e modificare (è superfluo)

    private val _items = mutableSetOf<PurchasableItem>()
    private val _itemsLiveData = MutableLiveData<Set<PurchasableItem>>()



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

    fun getPurchasedNum(): Int{
        return _items.count { it -> it.purchased } ?: 0
    }

    fun getTotalItems(): Int{
        return _items.count()
    }

    fun getProgress(): Float{

        if(getPurchasedNum()==0 && getTotalItems()==0) {
            Log.d("Progress", "getProgress > Current progress is 0")
            return 0f
        }
        else {
            Log.d("Progress", "getProgress > Current progress is ${(getPurchasedNum().toFloat() / getTotalItems())}")
            return getPurchasedNum().toFloat() / getTotalItems()
        }
    }

    fun getCategories() : List<ItemCategory>{
        return _items.map{it -> it.category}.toSet().sorted()
    }

    fun addItem(newItem : PurchasableItem){
        Log.d("ItemMgm", "addItem > Trying to add new Item ($newItem)")

        _items.add(newItem)
        _itemsLiveData.value = _items

        Log.d("ItemMgm", "addItem > Updated item list is $_items\nand related live data is ${_itemsLiveData.value}")

    }

    fun getItems(): List<PurchasableItem>{
        return _itemsLiveData.value?.toList() ?: listOf()
    }

    fun clearItems(){
        _items.clear()
    }

    fun deleteItem(target : PurchasableItem){
        _items.remove(_items.find { it -> it.name == target.name })
    }

    fun setPurchase(item: PurchasableItem, p: Boolean){
        _items.find { it -> it.name == item.name }?.purchased = p
    }
}