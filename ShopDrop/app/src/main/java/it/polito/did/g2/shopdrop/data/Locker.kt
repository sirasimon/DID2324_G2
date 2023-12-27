package it.polito.did.g2.shopdrop.data

data class Locker(
    val id : String? = null,
    val address : String,
    val compartments : List<Compartment>? = null
)
