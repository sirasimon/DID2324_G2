package it.polito.did.s306067.lab04.classes

class Compartment (
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