package it.polito.did.g2.shopdrop.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Order(
    val id : String? = null,
    val storeID : String? = null,
    val carrierID : String? = null,
    val customerID : String? = null,
    var lockerID : String? = null,
    var items: Map<String, Int>? = null,
    var stateList : MutableList<OrderState>? = null,
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateState(){
        if(stateList!=null) {
            stateList?.add(
                OrderState(
                    OrderStateName.values()[stateList!!.last().state.ordinal + 1],
                    LocalDateTime.now()
                )
            )
        }
        else{
            stateList = mutableListOf(
                OrderState(
                    OrderStateName.CREATED,
                    LocalDateTime.now()
                )
            )
        }
    }

    override fun toString(): String {

        var statesString = ""

        stateList?.forEach { statesString+=" ${it.state} @ ${it.timestamp} –> " }

        return "ID: $id – last state: ${if(stateList?.isNotEmpty()==true) stateList?.last()?.state.toString() else "EMPTY LIST"}\n\tstoreID: $storeID | carrierID: $carrierID | customerID: $customerID | lockerID: $lockerID\n\titems: ${items?.toString()}\n\tstate list: [$statesString]"
    }
}