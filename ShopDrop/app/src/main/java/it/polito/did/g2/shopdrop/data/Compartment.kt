package it.polito.did.g2.shopdrop.data

class Compartment (
    val id : Int,
    var isOpen : Boolean,
    var isEmpty : Boolean,
    var isWorking : Boolean,
    var isAvailable : Boolean,
    var orderID : String?
){
    fun setOutOfOrder(){
        isWorking = false
    }

    fun flagAsCollected(){
        isEmpty = true
        orderID = null
    }

    fun assignOrder(orderID : String){
        this.orderID = orderID
        isAvailable = false
    }

    fun free(){
        orderID = null
        isAvailable = false
    }
}