package it.polito.did.g2.shopdrop.data

class Order(
    val id : String? = null,
    val storeID : String? = null,
    val carrierID : String? = null,
    val customerID : String? = null,
    val lockerID : String? = null,
    var stateList : List<OrderState>? = null,
)