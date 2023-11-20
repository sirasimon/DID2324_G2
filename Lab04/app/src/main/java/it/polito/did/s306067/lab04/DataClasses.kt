package it.polito.did.s306067.lab04

import java.sql.Timestamp

enum class StateName{   //L'ordine è...
    PROCESSING,         //... in preparazione
    SHIPPING,           //... in consegna
    AVAILABLE,          //... disponibile al ritiro
    COLLECTED,          //... stato ritirato
    CANCELLED,          //... stato cancellato
    TIMEOUT             //... stato riportato indietro causa superamento tempo massimo
}

data class State(
    val stateName: StateName,
    val timestamp: Timestamp?
)

data class Shipment(
    val ID : String,
    val states : List<State>
)

data class User(
    val UID : String,
    val nickname : String,
    val email : String,
    val pending : List<Shipment>?
)

data class Order(
    val orderID : String,
    val states : List<State>,
    val creationTime : Timestamp,
    val lastUpdateTime : Timestamp,
    val senderID : String,
    val receiverID : String,
    val lockerID : String,
    val insertionCode : String,
    val gatheringCode : String
)

data class Compartment(
    val ID : Int,
    val insertionCode : String,
    val gatheringCode : String,
    val isOpen : Boolean,
    val isEmpty : Boolean,
    val isWorking : Boolean,
    val orderID : String?
)

data class Locker(
    val ID : String,
    val compartments : List<Compartment>
)