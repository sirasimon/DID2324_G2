package it.polito.did.g2.shopdrop.data

class Locker(
    val id : String? = null,
    val address : String,
    val compartments : List<Compartment>? = null,
    var isOnline : Boolean = false,
    var isWorking : Boolean = false,
    var isFull : Boolean = false,
    var isEmpty : Boolean = false
){
    fun checkAvailability(): List<Int>?{
        var freeCompartment = compartments?.filter { it.isAvailable && it.isEmpty }?.map{it.id}

        if(freeCompartment?.isEmpty() == true){
            isFull=true
            isEmpty=false
        }
        if(freeCompartment?.size==compartments?.size) {
            isEmpty = true
            isFull = false
        }

        return freeCompartment
    }
}
