package it.polito.did.s306067.lab04.classes

import java.sql.Timestamp

data class Order(
    val orderID : String? = null,
    val states : List<StateValue>? = null,
    val creationTime : Timestamp? = null,
    val lastUpdateTime : Timestamp? = null,
    val senderID : String? = null,
    val addresseeID : String? = null,
    val lockerID : String? = null,
    val insertionCode : String? = null,
    val gatheringCode : String? = null
)
