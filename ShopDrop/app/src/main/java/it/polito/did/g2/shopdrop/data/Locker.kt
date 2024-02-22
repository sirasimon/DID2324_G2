package it.polito.did.g2.shopdrop.data

class Locker(
    val id : String? = null,
    val name: String? = null,
    val address : String,
    val compartments : List<Compartment>? = null,
    var isOnline : Boolean = false,
    var isWorking : Boolean = false,
    var isFull : Boolean = false,
    var isEmpty : Boolean = false,
    var isInteracting: Boolean = false,
    var otp: String = "9999",
    var showCode: Boolean = false
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

    fun showCode(flag : Boolean = true){
        showCode = flag
    }

    fun hideCode(flag : Boolean = true){
        showCode(!flag)
    }

    override fun toString(): String {

        var compartmentslist = ""
        compartments?.forEach { compartmentslist += it.toString()+"\n" }

        return "Locker \"$name\" ($id) @ $address\n\tSTATUS: online = $isOnline – working = $isWorking – full = $isFull – empty = $isEmpty" +
                "\n\ttotal compartments: ${compartments?.size}" + compartments?.forEach { it.toString() }
    }
}
