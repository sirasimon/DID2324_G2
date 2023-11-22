package it.polito.did.s306067.lab04.classes

import it.polito.did.s306067.lab04.Order

data class User(
    val UID : String? = null,
    val nickname : String? = null,
    val email : String? = null,
    val pending : List<Order>? = null
)
