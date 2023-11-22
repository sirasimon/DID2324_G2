package it.polito.did.s306067.lab04.classes

import it.polito.did.s306067.lab04.State
import java.sql.Timestamp

data class Order(
    val orderID : String? = null,
    val states : List<State>? = null,
    val creationTime : Timestamp? = null,
    val lastUpdateTime : Timestamp? = null,
    val senderID : String? = null,
    val adresseeID : String? = null,
    val lockerID : String? = null,
    val insertionCode : Int? = null,
    val gatheringCode : Int? = null
)
