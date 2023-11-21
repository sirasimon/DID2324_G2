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
    val adresseeID : String,
    val lockerID : String,
    val insertionCode : Int?,
    val gatheringCode : Int?
)

class Compartment(
    val ID : Int,
    var insertionCode : Int?,
    var gatheringCode : Int?,
    var isOpen : Boolean,
    var isEmpty : Boolean,
    var isWorking : Boolean,
    var orderID : String?
){
    fun setOutOfOrder(){
        isWorking = false
    }

    fun flagAsCollected(){
        isEmpty = true
        orderID = null
        insertionCode = null
        gatheringCode = null
    }

    fun generateCodes(insertionCode: Int?, gatheringCode: Int?){
        if(insertionCode!=null){
            this.insertionCode = insertionCode
        }else{
            //TODO: generare i codici randomicamente
        }

        if(gatheringCode!=null){
            this.gatheringCode = gatheringCode
        }else{
            //TODO: generare i codici randomicamente
        }
    }
}

/**
 * Classe relativa al Locker caratterizzata da:
 * @param ID la stringa univoca relativa del locker che coincide con l'indirizzo MAC del dispositivo
 * @param compartments la lista di scompartimenti di cui dispone il locker
 */
data class Locker(
    val ID : String,
    val compartments : List<Compartment>
)