package it.polito.did.g2.shopdrop.data

class Store(
    val id : String? = null,
    val address: String? = null,
){
    override fun toString(): String {
        return "STORE ID $id @ $address"
    }
}